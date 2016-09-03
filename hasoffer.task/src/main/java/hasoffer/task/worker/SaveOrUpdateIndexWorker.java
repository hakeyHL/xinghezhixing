package hasoffer.task.worker;

import hasoffer.affiliate.affs.flipkart.FlipkartAffiliateProductProcessor;
import hasoffer.affiliate.affs.snapdeal.SnapdealProductProcessor;
import hasoffer.affiliate.exception.AffiliateAPIException;
import hasoffer.affiliate.model.AffiliateProduct;
import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.HexDigestUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.bo.product.IndexHistory;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.StatHijackFetch;
import hasoffer.core.persistence.po.ptm.PtmCmpSku2;
import hasoffer.core.persistence.po.ptm.PtmCmpSkuIndex2;
import hasoffer.core.persistence.po.ptm.updater.PtmCmpSku2Updater;
import hasoffer.core.persistence.po.ptm.updater.PtmCmpSkuIndex2Updater;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.sites.flipkart.FlipkartHelper;
import hasoffer.fetch.sites.flipkart.FlipkartListProcessor;
import hasoffer.fetch.sites.flipkart.FlipkartSummaryProductProcessor;
import hasoffer.fetch.sites.shopclues.ShopcluesHelper;
import hasoffer.fetch.sites.shopclues.ShopcluesListProcessor;
import hasoffer.fetch.sites.snapdeal.SnapdealHelper;
import hasoffer.fetch.sites.snapdeal.SnapdealListProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/5/27.
 */
public class SaveOrUpdateIndexWorker implements Runnable {

    private static final String Q_PTMCMPSKU_SOURCESID = "SELECT t FROM PtmCmpSku2 t WHERE t.sourceSid = ?0 AND t.website = ?1";
    //    private static Logger logger = LoggerFactory.getLogger(SaveOrUpdateIndexWorker.class);
    private ConcurrentLinkedQueue<StatHijackFetch> queue;
    private IDataBaseManager dbm;
    private IMongoDbManager mdm;
    private ICmpSkuService cmpSkuService;

    public SaveOrUpdateIndexWorker(ConcurrentLinkedQueue<StatHijackFetch> queue, ICmpSkuService cmpSkuService, IDataBaseManager dbm, IMongoDbManager mdm) {
        this.queue = queue;
        this.cmpSkuService = cmpSkuService;
        this.dbm = dbm;
        this.mdm = mdm;
    }


    @Override
    public void run() {
        while (true) {

            StatHijackFetch statHijackFetch = queue.poll();

            if (statHijackFetch == null) {
                try {
                    TimeUnit.SECONDS.sleep(3);
//                    logger.info("update flipkartSkuTitle worker get null sleep 3 seconds");
                } catch (InterruptedException e) {
                    return;
                }
                continue;
            }


            Website website = statHijackFetch.getWebsite();
            if (!Website.FLIPKART.equals(website) && !Website.SNAPDEAL.equals(website) && !Website.SHOPCLUES.equals(website)) {
                continue;
            }

            String cliQ = statHijackFetch.getCliQ();
            String sourceId = statHijackFetch.getSourceId();

            //在此处，对不同的网站进行不同的处理动作，结果3种  1.创建sku 2.updateSku 3.获取失败
            List<PtmCmpSku2> skuList = null;
            List<IndexHistory> indexHistoryList = statHijackFetch.getIndexHistoryList();
            if (indexHistoryList == null) {
                indexHistoryList = new ArrayList<IndexHistory>();
            }

            try {

                skuList = getSku(website, cliQ, sourceId);

            } catch (HttpFetchException e) {

                IndexHistory history = new IndexHistory("HttpFetchException", TimeUtils.nowDate());
                indexHistoryList.add(history);

            } catch (AffiliateAPIException e) {

                IndexHistory history = new IndexHistory("AffiliateAPIException", TimeUtils.nowDate());
                indexHistoryList.add(history);

            } catch (ContentParseException e) {

                IndexHistory history = new IndexHistory("ContentParseException", TimeUtils.nowDate());
                indexHistoryList.add(history);

            } catch (Exception e) {

                IndexHistory history = new IndexHistory(e.toString(), TimeUtils.nowDate());
                indexHistoryList.add(history);

            }


            if (ArrayUtils.hasObjs(skuList)) {

                IndexHistory history = new IndexHistory("GetSkuSuccess", TimeUtils.nowDate());
                indexHistoryList.add(history);
                createOrUpdateIndex(skuList, statHijackFetch);

            } else {

                IndexHistory history = new IndexHistory("GetZeroProduct", TimeUtils.nowDate());
                indexHistoryList.add(history);

                statHijackFetch.setResult("fail");

            }

            statHijackFetch.setIndexHistoryList(indexHistoryList);

            mdm.save(statHijackFetch);
        }
    }

    private void createOrUpdateIndex(List<PtmCmpSku2> skuList, StatHijackFetch statHijackFetch) {

        for (PtmCmpSku2 sku : skuList) {

            List<PtmCmpSku2> resultList = dbm.query(Q_PTMCMPSKU_SOURCESID, Arrays.asList(sku.getSourceSid(), sku.getWebsite()));

            //如果有结果，将所有结果的url和tltle等字段进行全部的更新
            //如果没有结果，创建sku，新建索引
            if (ArrayUtils.hasObjs(resultList)) {
                for (PtmCmpSku2 oldSku : resultList) {

                    //更新sku
                    PtmCmpSku2Updater updater = new PtmCmpSku2Updater(oldSku.getId());

                    updater.getPo().setSkuTitle(sku.getSkuTitle());
                    updater.getPo().setUrl(sku.getUrl());

                    dbm.update(updater);

                    PtmCmpSkuIndex2 ptmCmpSkuIndex2 = dbm.get(PtmCmpSkuIndex2.class, oldSku.getId());

                    if (ptmCmpSkuIndex2 != null) {
                        //更新index
                        PtmCmpSkuIndex2Updater updater1 = new PtmCmpSkuIndex2Updater(oldSku.getId());

                        updater1.getPo().setSkuTitle(sku.getSkuTitle());
                        updater1.getPo().setSkuTitleIndex(HexDigestUtil.md5(StringUtils.getCleanChars(sku.getSkuTitle())));
                        updater1.getPo().setUrl(sku.getUrl());
                        updater1.getPo().setSiteSkuTitleIndex(HexDigestUtil.md5(oldSku.getWebsite().name() + StringUtils.getCleanChars(sku.getSkuTitle())));
                        updater1.getPo().setSkuUrlIndex(HexDigestUtil.md5(WebsiteHelper.getCleanUrl(oldSku.getWebsite(), sku.getUrl())));
                        updater1.getPo().setUpdateTime(TimeUtils.nowDate());

                        dbm.update(updater1);
                    } else {
                        cmpSkuService.createPtmCmpSkuIndexToMysql(oldSku);
                    }

                    statHijackFetch.getAffectSkuIdList().add(oldSku.getId() + "");
                }
            } else {

                PtmCmpSku2 cmpSkuForIndex = cmpSkuService.createCmpSkuForIndex(sku);
                cmpSkuService.createPtmCmpSkuIndexToMysql(cmpSkuForIndex);
                statHijackFetch.getAffectSkuIdList().add(cmpSkuForIndex.getId() + "");
            }
        }

        statHijackFetch.setResult("success");
        mdm.save(statHijackFetch);
    }

    private List<PtmCmpSku2> getSku(Website website, String cliQ, String sourceId) throws ContentParseException, HttpFetchException, AffiliateAPIException, IOException {

        if (Website.FLIPKART.equals(website)) {
            return getFlipkartProduct(cliQ, sourceId);
        } else if (Website.SNAPDEAL.equals(website)) {
            return getSnapdealProduct(cliQ, sourceId);
        } else if (Website.SHOPCLUES.equals(website)) {
            return getShopcluesProduct(cliQ, sourceId);
        }

        return null;
    }

    private List<PtmCmpSku2> getShopcluesProduct(String cliQ, String sourceId) throws HttpFetchException, ContentParseException {

        List<PtmCmpSku2> skuList = new ArrayList<PtmCmpSku2>();

        ShopcluesListProcessor processor = new ShopcluesListProcessor();

        List<ListProduct> productList = processor.getProductSetByKeyword(cliQ, 10);

        for (ListProduct product : productList) {

            if (StringUtils.isEqual(product.getSourceId(), sourceId)) {

                if (ProductStatus.OFFSALE.equals(product.getStatus())) {
                    continue;
                }

                SkuStatus skuStatus = null;
                if (ProductStatus.OUTSTOCK.equals(product.getStatus())) {
                    skuStatus = skuStatus.OUTSTOCK;
                } else {
                    skuStatus = skuStatus.ONSALE;
                }

                PtmCmpSku2 sku = new PtmCmpSku2();

                sku.setProductId(0);
                sku.setUpdateTime(TimeUtils.nowDate());
                sku.setUrl(ShopcluesHelper.getCleanUrl(product.getUrl()));
                sku.setOriImageUrl(product.getImageUrl());
                sku.setPrice(product.getPrice());
                sku.setWebsite(product.getWebsite());
                sku.setTitle(product.getTitle());
                sku.setSkuTitle(product.getTitle());
                sku.setSourceSid(product.getSourceId());
                sku.setStatus(skuStatus);

                skuList.add(sku);
            }
        }

        return skuList;
    }


    private List<PtmCmpSku2> getFlipkartProduct(String cliQ, String sourceId) throws HttpFetchException, ContentParseException, AffiliateAPIException, IOException {

        List<PtmCmpSku2> skuList = new ArrayList<PtmCmpSku2>();

        if (StringUtils.isEmpty(sourceId) || "0".equals(sourceId)) {//sourceId为空，走页面解析
            FlipkartListProcessor processor = new FlipkartListProcessor();

            List<ListProduct> productList = processor.getProductSetByKeyword(cliQ, 10);
            for (ListProduct product : productList) {
                String cleanChars1 = StringUtils.getCleanChars(product.getTitle());
                String cleanChars2 = StringUtils.getCleanChars(cliQ);
                if (StringUtils.isEqual(cleanChars1, cleanChars2)) {

                    if (ProductStatus.OFFSALE.equals(product.getStatus())) {
                        continue;
                    }

                    SkuStatus skuStatus = null;
                    if (ProductStatus.OUTSTOCK.equals(product.getStatus())) {
                        skuStatus = skuStatus.OUTSTOCK;
                    } else {
                        skuStatus = skuStatus.ONSALE;
                    }

                    PtmCmpSku2 sku = new PtmCmpSku2();

                    sku.setProductId(0);
                    sku.setUpdateTime(TimeUtils.nowDate());
                    sku.setUrl(FlipkartHelper.getCleanUrl(product.getUrl()));
                    sku.setOriImageUrl(product.getImageUrl());
                    sku.setPrice(product.getPrice());
                    sku.setWebsite(product.getWebsite());
                    sku.setTitle(product.getTitle());
                    sku.setSkuTitle(product.getTitle());
                    sku.setSourceSid(product.getSourceId());
                    sku.setStatus(skuStatus);

                    skuList.add(sku);
                }
            }
//            logger.info("flipkart page parse get " + skuList.size() + " sku for [" + cliQ + "]");

        } else {

            FlipkartAffiliateProductProcessor processor = new FlipkartAffiliateProductProcessor();

            AffiliateProduct affiliateProduct = processor.getAffiliateProductBySourceId(sourceId);

            SkuStatus status = null;
            if (StringUtils.isEqual("true", affiliateProduct.getProductStatus())) {
                status = SkuStatus.ONSALE;
            } else if (StringUtils.isEqual("false", affiliateProduct.getProductStatus())) {
                status = SkuStatus.OUTSTOCK;
            }

            PtmCmpSku2 sku = new PtmCmpSku2();

            sku.setProductId(0);
            sku.setUpdateTime(TimeUtils.nowDate());
            sku.setUrl(FlipkartHelper.getCleanUrl(affiliateProduct.getUrl()));
            sku.setOriImageUrl(affiliateProduct.getImageUrl());
            sku.setPrice(affiliateProduct.getPrice());
            sku.setWebsite(affiliateProduct.getWebsite());
            sku.setTitle(affiliateProduct.getTitle());
            sku.setSkuTitle(affiliateProduct.getTitle());
            sku.setSourceSid(affiliateProduct.getSourceId());
            sku.setStatus(status);

            //联盟返回的需要更新title
            FlipkartSummaryProductProcessor productProcessor = new FlipkartSummaryProductProcessor();

            OriFetchedProduct product = productProcessor.getSummaryProductByUrl(sku.getUrl());
            String skutitle = product.getTitle() + product.getSubTitle();
            sku.setSkuTitle(skutitle);

            skuList.add(sku);

//            logger.info("flipkart affiliate get sku success [" + sourceId + "]");
        }

        return skuList;
    }

    private List<PtmCmpSku2> getSnapdealProduct(String cliQ, String sourceId) {

        List<PtmCmpSku2> skuList = new ArrayList<PtmCmpSku2>();

        if (StringUtils.isEmpty(sourceId) || "0".equals(sourceId)) {
            SnapdealListProcessor processor = new SnapdealListProcessor();
            try {
                List<ListProduct> productList = processor.getProductSetByKeyword(cliQ, 10);

                for (ListProduct product : productList) {
                    String cleanChars1 = StringUtils.getCleanChars(product.getTitle());
                    String cleanChars2 = StringUtils.getCleanChars(cliQ);
                    if (StringUtils.isEqual(cleanChars1, cleanChars2)) {

                        if (ProductStatus.OFFSALE.equals(product.getStatus())) {
                            continue;
                        }

                        SkuStatus status = null;
                        if (ProductStatus.OUTSTOCK.equals(product.getStatus())) {
                            status = SkuStatus.OUTSTOCK;
                        } else if (ProductStatus.ONSALE.equals(product.getStatus())) {
                            status = SkuStatus.ONSALE;
                        }

                        PtmCmpSku2 sku = new PtmCmpSku2();

                        sku.setProductId(0);
                        sku.setUpdateTime(TimeUtils.nowDate());
                        sku.setUrl(SnapdealHelper.getCleanUrl(product.getUrl()));
                        sku.setOriImageUrl(product.getImageUrl());
                        sku.setPrice(product.getPrice());
                        sku.setWebsite(product.getWebsite());
                        sku.setTitle(product.getTitle());
                        sku.setSkuTitle(product.getTitle());
                        sku.setSourceSid(product.getSourceId());
                        sku.setStatus(status);

                        skuList.add(sku);
                    }
                }
//                logger.info("snapdeal page parse get " + productList.size() + " sku for [" + cliQ + "]");
            } catch (Exception e) {
//                logger.info("snapdeal page parse get sku fail for [" + cliQ + "]");
//                logger.info(e.toString());
            }
        } else {
            SnapdealProductProcessor processor = new SnapdealProductProcessor();
            try {
                AffiliateProduct affiliateProduct = processor.getAffiliateProductBySourceId(sourceId);

                SkuStatus status = null;
                if (StringUtils.isEqual("true", affiliateProduct.getProductStatus())) {
                    status = SkuStatus.ONSALE;
                } else if (StringUtils.isEqual("false", affiliateProduct.getProductStatus())) {
                    status = SkuStatus.OUTSTOCK;
                }

                PtmCmpSku2 sku = new PtmCmpSku2();

                sku.setProductId(0);
                sku.setUpdateTime(TimeUtils.nowDate());
                sku.setUrl(SnapdealHelper.getCleanUrl(affiliateProduct.getUrl()));
                sku.setOriImageUrl(affiliateProduct.getImageUrl());
                sku.setPrice(affiliateProduct.getPrice());
                sku.setWebsite(affiliateProduct.getWebsite());
                sku.setTitle(affiliateProduct.getTitle());
                sku.setSkuTitle(affiliateProduct.getTitle());
                sku.setSourceSid(affiliateProduct.getSourceId());
                sku.setStatus(status);

                skuList.add(sku);

//                logger.info("snapdeal affiliate get sku success [" + sourceId + "]");
            } catch (Exception e) {
//                logger.info("snapdeal affiliate create sku fail for [" + sourceId + "]");
            }
        }

        return skuList;
    }
}
