package hasoffer.core.product.impl;

import hasoffer.base.exception.ImageDownloadOrUploadException;
import hasoffer.base.model.ImagePath;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.HexDigestUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.bo.product.SkuPriceUpdateResultBo;
import hasoffer.core.exception.CmpSkuUrlNotFoundException;
import hasoffer.core.exception.MultiUrlException;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.*;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmCmpSku2;
import hasoffer.core.persistence.po.ptm.PtmCmpSkuImage;
import hasoffer.core.persistence.po.ptm.PtmCmpSkuIndex2;
import hasoffer.core.persistence.po.ptm.updater.PtmCmpSkuUpdater;
import hasoffer.core.persistence.po.stat.StatSkuPriceUpdateResult;
import hasoffer.core.persistence.po.stat.updater.StatSkuPriceUpdateResultUpdater;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.product.IFetchService;
import hasoffer.core.product.IPtmCmpSkuImageService;
import hasoffer.core.product.solr.CmpSkuModel;
import hasoffer.core.product.solr.CmpskuIndexServiceImpl;
import hasoffer.core.utils.ImageUtil;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.sites.flipkart.FlipkartHelper;
import hasoffer.fetch.sites.snapdeal.SnapdealHelper;
import hasoffer.spider.model.FetchedProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created on 2016/1/4.
 */
@Service
public class CmpSkuServiceImpl implements ICmpSkuService {

    private static final String Q_CMPSKU_BY_PRODUCTID =
            "SELECT t FROM PtmCmpSku t WHERE t.productId = ?0 ";
    private static final String Q_CMPSKU_BY_PRODUCTID_WEBSITE =
            "SELECT t FROM PtmCmpSku t WHERE t.productId = ?0 AND t.website = ?1 ";
    private static final String Q_CMPSKU_BY_TITLE =
            "SELECT t FROM PtmCmpSku t WHERE t.skuTitle = ?0 ";
    private static final java.lang.String Q_CMPSKU_BY_PRODUCTID_AND_STATUS =
            "SELECT t FROM PtmCmpSku t WHERE t.productId = ?0 AND t.status = ?1 ";

    private final String Q_CMPSKU_INDEX_BY_TITLEINDEX = "select t from PtmCmpSkuIndex2 t where t.siteSkuTitleIndex = ?0 ";

    private final String Q_CMPSKU_INDEX_BY_SOURCESID = "select t from PtmCmpSkuIndex2 t where t.siteSourceSidIndex = ?0 ";
    private final String Q_CMPSKU_STORES_BY_PRODUCTID = "SELECT  DISTINCT t.website  from PtmCmpSku t where t.productId=?0 and t.status='ONSALE'";

    @Resource
    IFetchService fetchService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;
    @Resource
    CmpskuIndexServiceImpl cmpskuIndexService;
    @Resource
    IPtmCmpSkuImageService ptmCmpSkuImageService;

    private Logger logger = LoggerFactory.getLogger(CmpSkuServiceImpl.class);

    @Override
    public List<PriceNode> queryHistoryPrice(long id) {
        PtmCmpSkuHistoryPrice historyPrice = mdm.queryOne(PtmCmpSkuHistoryPrice.class, id);
        if (historyPrice == null) {
            return null;
        } else {
            return historyPrice.getPriceNodes();
        }
    }

    @Override
    public void saveHistoryPrice(Long sid, List<PriceNode> priceNodes) {
        if (ArrayUtils.isNullOrEmpty(priceNodes)) {
            return;
        }
        final int PRICE_HISTORY_SIZE = 90;

        Set<PriceNode> priceNodeSet = new LinkedHashSet<>();
        priceNodeSet.addAll(priceNodes);

        PtmCmpSkuHistoryPrice historyPrice = mdm.queryOne(PtmCmpSkuHistoryPrice.class, sid);
        if (historyPrice != null) {
            priceNodeSet.addAll(historyPrice.getPriceNodes());
        }

        List<PriceNode> priceNodes1 = new ArrayList<>();
        priceNodes1.addAll(priceNodeSet);

        if (historyPrice == null) {
            historyPrice = new PtmCmpSkuHistoryPrice(sid, priceNodes1);
        }

        historyPrice.setPriceNodes(priceNodes1);

        // 排序
        Collections.sort(priceNodes1, new Comparator<PriceNode>() {
            @Override
            public int compare(PriceNode o1, PriceNode o2) {
                if (o1.getPriceTimeL() > o2.getPriceTimeL()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        if (priceNodes1.size() > PRICE_HISTORY_SIZE) {
            historyPrice.setPriceNodes(priceNodes1.subList(priceNodes1.size() - PRICE_HISTORY_SIZE, priceNodes1.size()));
        }

        mdm.save(historyPrice);
    }

    @Override
    public void saveHistoryPrice(long id, Date time, float price) {
        saveHistoryPrice(id, Arrays.asList(new PriceNode(time, price)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCmpSku(PtmCmpSkuUpdater updater) {
        dbm.update(updater);
    }

    @Override
    public PtmCmpSkuIndex2 getCmpSkuIndex2(Website website, String sourceId, String cliQ) {

        List<PtmCmpSkuIndex2> cmpSkuIndexs = null;
        if (StringUtils.isEmpty(sourceId) || StringUtils.isEqual(sourceId, "0")) {
            String qIndex = HexDigestUtil.md5(website.name() + StringUtils.getCleanChars(cliQ));
            cmpSkuIndexs = dbm.query(Q_CMPSKU_INDEX_BY_TITLEINDEX, Arrays.asList(qIndex));
        } else {
            String qIndex = HexDigestUtil.md5(website.name() + sourceId);
            cmpSkuIndexs = dbm.query(Q_CMPSKU_INDEX_BY_SOURCESID, Arrays.asList(qIndex));
        }
        // todo fix bug
        PtmCmpSkuIndex2 finalIndex = null;
        if (ArrayUtils.hasObjs(cmpSkuIndexs)) {

            finalIndex = cmpSkuIndexs.get(0);

            if (StringUtils.isEmpty(finalIndex.getUrl())) {
                throw new CmpSkuUrlNotFoundException(finalIndex.getId());
            }

            if ((StringUtils.isEmpty(sourceId) || StringUtils.isEqual("0", sourceId)) && cmpSkuIndexs.size() != 1) {
                for (int i = 1; i < cmpSkuIndexs.size(); i++) {

                    PtmCmpSkuIndex2 index = cmpSkuIndexs.get(i);

                    if (!finalIndex.getUrl().equals(index.getUrl())) {
                        throw new MultiUrlException(website, sourceId, cliQ);
                    }
                }
            }
        }

        return finalIndex;
    }

    @Override
    public int getSkuSoldStoreNum(Long id) {
        List li = dbm.query(Q_CMPSKU_STORES_BY_PRODUCTID, Arrays.asList(id));
        return li.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fixUrls(long id, String url, String dl) {
        PtmCmpSkuUpdater cmpSkuUpdater = new PtmCmpSkuUpdater(id);
        cmpSkuUpdater.getPo().setUrl(url);
        cmpSkuUpdater.getPo().setDeeplink(url);
        dbm.update(cmpSkuUpdater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCmpSku(Long id, String newTitle) {
        PtmCmpSkuUpdater cmpSkuUpdater = new PtmCmpSkuUpdater(id);
        cmpSkuUpdater.getPo().setSkuTitle(newTitle);
        dbm.update(cmpSkuUpdater);
    }

    @Override
//    @Cacheable(value = CACHE_KEY, key = "#root.methodName + '_' + #root.args[0]")
    public PtmCmpSku getCmpSku(String title) {
        List<PtmCmpSku> cmpSkus = dbm.query(Q_CMPSKU_BY_TITLE, Arrays.asList(title));
        return ArrayUtils.isNullOrEmpty(cmpSkus) ? null : cmpSkus.get(0);
    }

    @Override
    public PtmCmpSku getCmpSku(long proId, Website website) {

        List<PtmCmpSku> cmpSkus = dbm.query(Q_CMPSKU_BY_PRODUCTID_WEBSITE, Arrays.asList(proId, website));

        if (ArrayUtils.hasObjs(cmpSkus)) {
            return cmpSkus.get(0);
        }

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadImage(PtmCmpSku sku) {
        String oriImageUrl = sku.getOriImageUrl();
        if (StringUtils.isEmpty(oriImageUrl)) {
            return;
        }

        PtmCmpSkuUpdater ptmCmpSkuUpdater = new PtmCmpSkuUpdater(sku.getId());

        try {
            String path = ImageUtil.downloadAndUpload(oriImageUrl);

            ptmCmpSkuUpdater.getPo().setImagePath(path);

            dbm.update(ptmCmpSkuUpdater);

        } catch (ImageDownloadOrUploadException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadImage2(PtmCmpSku sku) {
        PtmCmpSkuUpdater ptmCmpSkuUpdater = new PtmCmpSkuUpdater(sku.getId());

        String oriImageUrl = sku.getOriImageUrl();

        try {
            if (StringUtils.isEmpty(oriImageUrl)) {
                throw new ImageDownloadOrUploadException();
            }

            ImagePath imagePath = ImageUtil.downloadAndUpload2(oriImageUrl);

            logger.info(imagePath.getOriginalPath() + "\t" + imagePath.getBigPath() + "\t" + imagePath.getSmallPath());

            ptmCmpSkuUpdater.getPo().setImagePath(imagePath.getOriginalPath());
            ptmCmpSkuUpdater.getPo().setSmallImagePath(imagePath.getSmallPath());
            ptmCmpSkuUpdater.getPo().setBigImagePath(imagePath.getBigPath());

        } catch (Exception e) {
            logger.error("download image or upload error, sku id = " + sku.getId() + ", oriImageUrl = " + oriImageUrl);

            // 下载图片失败
            ptmCmpSkuUpdater.getPo().setFailLoadImage(true);
        } finally {
            dbm.update(ptmCmpSkuUpdater);
            logger.info(String.format("update sku [%d]..........OK ! ", sku.getId()));
        }
    }

    @Override
    public List<PtmCmpSku> listCmpSkus(long productId) {
        return dbm.query(Q_CMPSKU_BY_PRODUCTID, Arrays.asList(productId));
    }

    @Override
    public List<PtmCmpSku> listCmpSkus(long productId, SkuStatus status) {
        return dbm.query(Q_CMPSKU_BY_PRODUCTID_AND_STATUS, Arrays.asList(productId, status));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategoryid(long ptmcmpskuid, long categoryid) {

        PtmCmpSkuUpdater updater = new PtmCmpSkuUpdater(ptmcmpskuid);

        updater.getPo().setCategoryId(categoryid);

        dbm.update(updater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategoryid2(Long ptmcmpskuid, long categoryid2) {
        PtmCmpSkuUpdater updater = new PtmCmpSkuUpdater(ptmcmpskuid);

        updater.getPo().setCategoryId(categoryid2);

        dbm.update(updater);
    }

    @Override
    public List<StatSkuPriceUpdateResult> listUpdateResults() {
        return dbm.query("select t from StatSkuPriceUpdateResult t order by t.id desc");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateSkuPriceUpdateResult(SkuPriceUpdateResultBo updateResultBo) {

        String ymd = updateResultBo.getYmd();
        StatSkuPriceUpdateResult statSkuPriceUpdateResult = dbm.get(StatSkuPriceUpdateResult.class, ymd);

        if (statSkuPriceUpdateResult == null) {
            statSkuPriceUpdateResult = new StatSkuPriceUpdateResult(ymd, updateResultBo.getCount());
            dbm.create(statSkuPriceUpdateResult);
        } else {
            StatSkuPriceUpdateResultUpdater statSkuPriceUpdateResultUpdater = new StatSkuPriceUpdateResultUpdater(ymd);
            statSkuPriceUpdateResultUpdater.getPo().setCount(updateResultBo.getCount());
            dbm.update(statSkuPriceUpdateResultUpdater);
        }
    }

    @Override
    public SkuPriceUpdateResultBo countUpdate(String ymd) {
        Date startDate = TimeUtils.toDate(TimeUtils.getDayStart(ymd, "yyyyMMdd"));
        Date endDate = TimeUtils.toDate(TimeUtils.getDayStart(ymd, "yyyyMMdd") + TimeUtils.MILLISECONDS_OF_1_DAY);

        Query query = new Query(Criteria.where("priceTime").gte(startDate).lt(endDate));
        long count = mdm.count(PtmCmpSkuLog.class, query);

        return new SkuPriceUpdateResultBo(ymd, count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCmpSku(long id) {
        System.out.println("del PtmCmpSku : " + id);
        dbm.delete(PtmCmpSku.class, id);
        cmpskuIndexService.remove(String.valueOf(id));
    }

    @Override
    public PtmCmpSku getCmpSkuById(long id) {
        return dbm.get(PtmCmpSku.class, id);
    }

    /**
     * 新增一条cmpSku记录
     *
     * @param productId
     * @param url
     * @param color
     * @param size
     * @param price
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PtmCmpSku createCmpSku(long productId, String url, String color, String size, float price) {

        PtmCmpSku ptmCmpSku = new PtmCmpSku();

        ptmCmpSku.setUrl(url);
        ptmCmpSku.setProductId(productId);
        ptmCmpSku.setColor(color);
        ptmCmpSku.setSize(size);
        ptmCmpSku.setPrice(price);

        Website website = WebsiteHelper.getWebSite(url);
        if (website != null) {
            ptmCmpSku.setWebsite(website);
        }

        createCmpSku(ptmCmpSku);

        return ptmCmpSku;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PtmCmpSku createCmpSku(PtmCmpSku ptmCmpSku) {

        //sku创建时，添加创建时间字段
        ptmCmpSku.setCreateTime(TimeUtils.nowDate());

        long skuid = dbm.create(ptmCmpSku);

        //创建sku的时候，将固定网站的商品导入到mongodb中,只导入url、website、id

        /*if (WebsiteHelper.DEFAULT_WEBSITES.contains(ptmCmpSku.getWebsite())) {

            SummaryProduct summaryProduct = new SummaryProduct();

            summaryProduct.setId(skuid);
            summaryProduct.setWebsite(ptmCmpSku.getWebsite());
            summaryProduct.setUrl(ptmCmpSku.getUrl());

            mdm.save(summaryProduct);
        }*/

//        createPtmCmpSkuIndexToMongo(ptmCmpSku);
//        createPtmCmpSkuIndexToMysql(ptmCmpSku);

        importCmpSku2solr(ptmCmpSku);

        return ptmCmpSku;
    }

    @Override
    public void createDescription(PtmCmpSku ptmCmpSku, FetchedProduct fetchedProduct) {

        String jsonParam = fetchedProduct.getJsonParam();
        String description = fetchedProduct.getDescription();
        String offers = fetchedProduct.getOffers();

        //在fetch包暂时无法跟新升级的时候，先在这里回避掉这种错误
        if (StringUtils.isEqual("[]", description)) {
            description = "";
        }

        //save ptmcmpskuDescription
        PtmCmpSkuDescription ptmCmpSkuDescription = mdm.queryOne(PtmCmpSkuDescription.class, ptmCmpSku.getId());
        if (ptmCmpSkuDescription == null) {//不存在该条记录

            ptmCmpSkuDescription = new PtmCmpSkuDescription();

            ptmCmpSkuDescription.setId(ptmCmpSku.getId());
            ptmCmpSkuDescription.setJsonParam(jsonParam);
            ptmCmpSkuDescription.setJsonDescription(description);
            ptmCmpSkuDescription.setOffers(offers);

            if (StringUtils.isEmpty(jsonParam) && StringUtils.isEmpty(description)) {
                return;
            }
            mdm.save(ptmCmpSkuDescription);
        } else {//存在该条记录

            boolean flagDescription = false;
            boolean flagJsonParam = false;
            boolean flagOffers = false;

            String oldJsonDescription = ptmCmpSkuDescription.getJsonDescription();
            String oldJsonParam = ptmCmpSkuDescription.getJsonParam();
            String oldOffers = ptmCmpSkuDescription.getOffers();

            //新的参数不为空，且新的参数和原有的不相同，更新
            if (!StringUtils.isEmpty(jsonParam) && !StringUtils.isEqual(jsonParam, oldJsonParam)) {
                ptmCmpSkuDescription.setJsonParam(jsonParam);
                flagDescription = true;
            }

            //新的描述不为空，且和旧的参数不相同
            if (!StringUtils.isEmpty(description) && !StringUtils.isEqual(description, oldJsonDescription)) {
                ptmCmpSkuDescription.setJsonDescription(description);
                flagJsonParam = true;
            }

            //新的offers不为空，且和就得offers不相同
            //注意offer内容只要不相同就要更新，有的offer不在可用，需要更新成空
            if (!StringUtils.isEqual(offers, oldOffers)) {
                ptmCmpSkuDescription.setOffers(offers);
                flagOffers = true;
            }


            if (flagDescription || flagJsonParam || flagOffers) {
                mdm.save(ptmCmpSkuDescription);
            }
        }

        //save productDescription
        PtmProductDescription ptmProductDescription = mdm.queryOne(PtmProductDescription.class, ptmCmpSku.getProductId());

        if (ptmProductDescription == null) {//如果不存在该记录

            ptmProductDescription = new PtmProductDescription();

            ptmProductDescription.setId(ptmCmpSku.getProductId());
            //最开始需求没说明白描述和参数问题，字段写错了，修改通知前台
            ptmProductDescription.setJsonDescription(jsonParam);
            ptmProductDescription.setJsonParam(description);

            if (StringUtils.isEmpty(jsonParam) && StringUtils.isEmpty(description)) {
                return;
            }

            mdm.save(ptmProductDescription);
        } else {//如果存在

            boolean flagDescription = false;
            boolean flagJsonParam = false;

            //最一开始设计的错误，修改需要通知api
            String oldJsonDescription = ptmProductDescription.getJsonParam();//product 描述
            String oldJsonParam = ptmCmpSkuDescription.getJsonDescription();//product 参数

            //新的参数不为空，且新的参数和原有的不相同，更新
            if (!StringUtils.isEmpty(jsonParam) && !StringUtils.isEqual(jsonParam, oldJsonParam)) {
                ptmProductDescription.setJsonDescription(jsonParam);
                flagDescription = true;
            }

            //新的描述不为空，且和旧的参数不相同
            if (!StringUtils.isEmpty(description) && StringUtils.isEqual(description, oldJsonDescription)) {
                ptmCmpSkuDescription.setJsonParam(description);
                flagJsonParam = true;
            }

            if (flagDescription || flagJsonParam) {
                mdm.save(ptmProductDescription);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPtmCmpSkuImage(long skuId, FetchedProduct fetchedProduct) {
        if (skuId <= 0 || fetchedProduct == null || fetchedProduct.getImageUrlList() == null || fetchedProduct.getImageUrlList().size() == 0) {
            return;
        }

        List<String> imageUrlList = fetchedProduct.getImageUrlList();

        PtmCmpSkuImage ptmCmpSkuImage = dbm.querySingle("SELECT t FROM PtmCmpSkuImage t WHERE t.id = ?0 ", Arrays.asList(skuId));

        if (ptmCmpSkuImage == null) {//如果不存在
            createOrUpdatePtmCmpSkuImage(skuId, imageUrlList);
        } else {//如果存在
            if (!StringUtils.isEqual(ptmCmpSkuImage.getOriImageUrl1(), imageUrlList.get(0)) && imageUrlList.size() > ptmCmpSkuImage.getOriImageUrlNumber()) {//只有在第一张图片url不一致，并且新的图片数量比旧的总数大（该值最大为4）的情况下更新
                ptmCmpSkuImageService.delete(skuId);
                createOrUpdatePtmCmpSkuImage(skuId, imageUrlList);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PtmCmpSku2 createCmpSkuForIndex(PtmCmpSku2 ptmCmpSku) {

        //sku创建时，添加创建时间字段
        ptmCmpSku.setCreateTime(TimeUtils.nowDate());
        ptmCmpSku.setCategoryId(0L);
        long skuid = dbm.create(ptmCmpSku);

        //创建sku的时候，将固定网站的商品导入到mongodb中,只导入url、website、id
        /*if (WebsiteHelper.DEFAULT_WEBSITES.contains(ptmCmpSku.getWebsite())) {

            SummaryProduct summaryProduct = new SummaryProduct();

            summaryProduct.setId(skuid);
            summaryProduct.setWebsite(ptmCmpSku.getWebsite());
            summaryProduct.setUrl(ptmCmpSku.getUrl());

            mdm.save(summaryProduct);
        }*/

        return ptmCmpSku;
    }

    @Override
    public void importCmpSku2solrByProductId(Long proId) {
        List<PtmCmpSku> cmpSkus = listCmpSkus(proId);
        for (PtmCmpSku cmpSku : cmpSkus) {
            importCmpSku2solr(cmpSku);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteCmpSku(Long[] ids) {
        for (Long id : ids) {
            dbm.delete(PtmCmpSku.class, id);
            cmpskuIndexService.remove(String.valueOf(id));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fixSmallImagePath(long skuid, String smallImagePath) {

        PtmCmpSkuUpdater updater = new PtmCmpSkuUpdater(skuid);

        updater.getPo().setSmallImagePath(smallImagePath);

        dbm.update(updater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fixFlipkartSkuTitleNull(long skuid, String skutitle) {
        PtmCmpSkuUpdater updater = new PtmCmpSkuUpdater(skuid);

        updater.getPo().setSkuTitle(skutitle);

        dbm.update(updater);
    }

    @Override
    @Transactional
    public void updateCmpSkuBrandModel(Long id, String brand, String model) {
        PtmCmpSkuUpdater ptmCmpSkuUpdater = new PtmCmpSkuUpdater(id);
        ptmCmpSkuUpdater.getPo().setBrand(brand);
        ptmCmpSkuUpdater.getPo().setModel(model);
        dbm.update(ptmCmpSkuUpdater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFlipakrtSkuBrandAndModel(long skuid, String brand, String model) {

        PtmCmpSkuUpdater updater = new PtmCmpSkuUpdater(skuid);

        if (!StringUtils.isEmpty(brand)) {
            updater.getPo().setBrand(brand);
        }

        if (!StringUtils.isEmpty(model)) {
            updater.getPo().setModel(model);
        }

        dbm.update(updater);
    }

    public void importCmpSku2solr(PtmCmpSku ptmCmpSku) {
        logger.debug(String.format("import or update to solr-sku {%d}", ptmCmpSku.getId()));
        cmpskuIndexService.createOrUpdate(new CmpSkuModel(ptmCmpSku));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCmpSku(long id, String url, String color, String size, float price) {
        PtmCmpSkuUpdater ptmCmpSkuUpdater = new PtmCmpSkuUpdater(id);

        ptmCmpSkuUpdater.getPo().setUpdateTime(TimeUtils.nowDate());
        ptmCmpSkuUpdater.getPo().setUrl(url);
        ptmCmpSkuUpdater.getPo().setPrice(price);
        ptmCmpSkuUpdater.getPo().setColor(color);
        ptmCmpSkuUpdater.getPo().setSize(size);

        Website website = WebsiteHelper.getWebSite(url);
        if (website != null) {
            ptmCmpSkuUpdater.getPo().setWebsite(website);
        }

        dbm.update(ptmCmpSkuUpdater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPtmCmpSkuIndexToMysql(PtmCmpSku2 ptmCmpSku) {

        if (ptmCmpSku.getWebsite() == null) {
            return;
        }

        Website website = ptmCmpSku.getWebsite();
        if (WebsiteHelper.DEFAULT_WEBSITES.contains(website)) {

            Long id = ptmCmpSku.getId();
            long productId = ptmCmpSku.getProductId();
            String sourcePid = ptmCmpSku.getSourcePid();
            String sourceSid = ptmCmpSku.getSourceSid();
            float price = ptmCmpSku.getPrice();
            String oriUrl = ptmCmpSku.getOriUrl();
            if (StringUtils.isEmpty(oriUrl)) {
                oriUrl = ptmCmpSku.getUrl();
            }
            String url = WebsiteHelper.getCleanUrl(website, oriUrl);
            String title = ptmCmpSku.getTitle();
            String skuTitle = StringUtils.isEmpty(ptmCmpSku.getSkuTitle()) ? title : ptmCmpSku.getSkuTitle();

            if (Website.SNAPDEAL.equals(website)) {
                sourceSid = SnapdealHelper.getSkuIdByUrl(oriUrl);
            } else if (Website.FLIPKART.equals(website)) {
                sourceSid = FlipkartHelper.getSkuIdByUrl(oriUrl);
                sourcePid = FlipkartHelper.getProductIdByUrl(oriUrl);
                url = FlipkartHelper.getUrlByDeeplink(url);
                url = FlipkartHelper.getCleanUrl(url);
            }


            if (StringUtils.isEmpty(skuTitle)) {
                return;
            }

            PtmCmpSkuIndex2 index = new PtmCmpSkuIndex2(id, productId, website, sourcePid, sourceSid, title, skuTitle, price, url);

            //设置新增时间
            index.setCreateTime(TimeUtils.nowDate());
            dbm.create(index);

            logger.debug("id = [" + id + "],website = [" + website + "],sourceSid = [" + sourceSid + "],skutitle = [" + skuTitle + "]");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCmpSkuByOriFetchedProduct(long id, OriFetchedProduct oriFetchedProduct) {

        //summaryProduct为null
        if (oriFetchedProduct == null) {
            return;
        }

        //根据id，获得该商品信息
        PtmCmpSku cmpSku = dbm.get(PtmCmpSku.class, id);
        if (cmpSku == null) {
            return;
        }
//        更新mongodb
//        PtmCmpSkuLog ptmCmpSkuLog = new PtmCmpSkuLog(cmpSku);
//        mdm.save(ptmCmpSkuLog);

        PtmCmpSkuUpdater ptmCmpSkuUpdater = new PtmCmpSkuUpdater(id);
        //获取商品的status
        if (ProductStatus.OFFSALE.equals(oriFetchedProduct.getProductStatus())) {//如果OFFSALE

            ptmCmpSkuUpdater.getPo().setStatus(SkuStatus.OFFSALE);

        } else {
            //如果售空了修改状态
            if (ProductStatus.OUTSTOCK.equals(oriFetchedProduct.getProductStatus())) {

                ptmCmpSkuUpdater.getPo().setStatus(SkuStatus.OUTSTOCK);

            } else {//修改价格

                float price = oriFetchedProduct.getPrice();
                if (price > 0) {
                    if (cmpSku.getPrice() != oriFetchedProduct.getPrice()) {
                        ptmCmpSkuUpdater.getPo().setPrice(price);
                    }
                }
                ptmCmpSkuUpdater.getPo().setStatus(SkuStatus.ONSALE);
            }

            if (!StringUtils.isEmpty(oriFetchedProduct.getTitle())) {
                if (StringUtils.isEmpty(cmpSku.getTitle()) || !StringUtils.isEqual(cmpSku.getTitle(), oriFetchedProduct.getTitle())) {
                    ptmCmpSkuUpdater.getPo().setTitle(oriFetchedProduct.getTitle());
                }
            }

            String imageUrl = oriFetchedProduct.getImageUrl();
            if (StringUtils.isEmpty(cmpSku.getOriImageUrl()) || !StringUtils.isEqual(imageUrl, cmpSku.getOriImageUrl())) {
                if (!StringUtils.isEmpty(imageUrl)) {
                    ptmCmpSkuUpdater.getPo().setOriImageUrl(imageUrl);
                }
            }
        }

        if (cmpSku.getWebsite() == null) {
            Website website = oriFetchedProduct.getWebsite();
            if (website != null) {
                ptmCmpSkuUpdater.getPo().setWebsite(oriFetchedProduct.getWebsite());
            }
        }

        if (!StringUtils.isEmpty(oriFetchedProduct.getSubTitle())) {
            ptmCmpSkuUpdater.getPo().setSkuTitle(oriFetchedProduct.getTitle() + oriFetchedProduct.getSubTitle());
        }

        ptmCmpSkuUpdater.getPo().setUpdateTime(TimeUtils.nowDate());
        dbm.update(ptmCmpSkuUpdater);
    }

    @Override
    public void updateCmpSkuBySpiderFetchedProduct(long skuId, FetchedProduct fetchedProduct) {

        //fetchedProduct为null
        if (fetchedProduct == null) {
            return;
        }

        //根据id，获得该商品信息
        PtmCmpSku cmpSku = dbm.get(PtmCmpSku.class, skuId);
        if (cmpSku == null) {
            return;
        }

//        更新mongodb
//        PtmCmpSkuLog ptmCmpSkuLog = new PtmCmpSkuLog(cmpSku);
//        mdm.save(ptmCmpSkuLog);

        //保存新抓来的价格
        if (fetchedProduct.getPrice() != 0.0f) {
            saveHistoryPrice(skuId, TimeUtils.nowDate(), fetchedProduct.getPrice());
        }

        PtmCmpSkuUpdater ptmCmpSkuUpdater = new PtmCmpSkuUpdater(skuId);

        //更新逻辑如下
//        1.如果状态为OFFSALE，更新status和updateTime
//        2.如果状态为OUTSTOCK，不更新价格
//        3.如果状态为ONSALE，且价格大于0，更新价格
//        4.在2和3状态下的
//                4.1如果title不为空，且和原来数据不一致，更新title
//                4.2如果imageurl不为空，且和原理数据不一致，更新oriImageUrl
//        5.如果原来的website为空，且新抓的website不为空，更新website
//        6.如果新抓的skutitle不为空，且和原来的不一样，更新skutitle
//        7.如果新抓的（只更新onsale的数据）
//        commentsNumber;//评论数大于0，更新该值
//        ratings;//星级，该值大于0，更新该值
//        shipping = -1;//邮费，该值大于0，更新该值
//        supportPayMethod;//支付方式，不为空，且和原来的字符串不一致，更新该值
//        returnDays;//如果该值大于0，更新
//        8.brand,model,如果新抓的不为null且和原来的不一样，更新

        //7.最终设置更新时间
//        deliveryTime;//送达时间 ex: 1-3   app2.0---暂定为5

        if (SkuStatus.OFFSALE.equals(fetchedProduct.getSkuStatus())) {//如果OFFSALE

            ptmCmpSkuUpdater.getPo().setStatus(SkuStatus.OFFSALE);

        } else {
            //如果售空了修改状态
            if (SkuStatus.OUTSTOCK.equals(fetchedProduct.getSkuStatus())) {

                ptmCmpSkuUpdater.getPo().setStatus(SkuStatus.OUTSTOCK);

            } else {

                ptmCmpSkuUpdater.getPo().setStatus(SkuStatus.ONSALE);

                //更新 commentsNumber
                long commentsNumber = fetchedProduct.getCommentsNumber();
                if (commentsNumber > 0) {
                    ptmCmpSkuUpdater.getPo().setCommentsNumber(commentsNumber);
                }

                //更新ratings
                int ratings = fetchedProduct.getRatings();
                if (ratings > 0) {
                    ptmCmpSkuUpdater.getPo().setRatings(ratings);
                }

                //更新 shipping
                float shipping = fetchedProduct.getShipping();
                if (shipping > 0) {
                    ptmCmpSkuUpdater.getPo().setShipping(shipping);
                }

                //更新 supportPayMethod
                String supportPayMethod = fetchedProduct.getSupportPayMethod();
                if (!StringUtils.isEmpty(supportPayMethod) && !StringUtils.isEqual(supportPayMethod, cmpSku.getSupportPayMethod())) {
                    ptmCmpSkuUpdater.getPo().setSupportPayMethod(supportPayMethod);
                }

                //更新 returnDays
                int returnDays = fetchedProduct.getReturnDays();
                if (returnDays > 0) {
                    ptmCmpSkuUpdater.getPo().setReturnDays(returnDays);
                }

            }

            //更新 price
            //策略更新  2016-08-24由于outstock商品返回前台，所以现在offsale不更新价格，别的状态都要更新价格
            float price = fetchedProduct.getPrice();
            if (price > 0) {
                if (cmpSku.getPrice() != fetchedProduct.getPrice()) {
                    ptmCmpSkuUpdater.getPo().setPrice(price);
                }
            }

            if (!StringUtils.isEmpty(fetchedProduct.getTitle())) {
                if (StringUtils.isEmpty(cmpSku.getTitle()) || !StringUtils.isEqual(cmpSku.getTitle(), fetchedProduct.getTitle())) {
                    ptmCmpSkuUpdater.getPo().setTitle(fetchedProduct.getTitle());
                }
            }

            String imageUrl = fetchedProduct.getImageUrl();
            if (StringUtils.isEmpty(cmpSku.getOriImageUrl()) || !StringUtils.isEqual(imageUrl, cmpSku.getOriImageUrl())) {
                if (!StringUtils.isEmpty(imageUrl)) {
                    ptmCmpSkuUpdater.getPo().setOriImageUrl(imageUrl);
                }
            }

            //更新skutitle,只要新旧不一样就更新
            if (!StringUtils.isEqual(cmpSku.getSkuTitle(), fetchedProduct.getSubTitle())) {
                ptmCmpSkuUpdater.getPo().setSkuTitle(fetchedProduct.getSubTitle());
            }
        }

        if (cmpSku.getWebsite() == null) {
            Website website = fetchedProduct.getWebsite();
            if (website != null) {
                ptmCmpSkuUpdater.getPo().setWebsite(fetchedProduct.getWebsite());
            }
        }

        //更新brand
        if (!StringUtils.isEmpty(fetchedProduct.getBrand()) && !StringUtils.isEqual(fetchedProduct.getBrand(), cmpSku.getBrand())) {
            ptmCmpSkuUpdater.getPo().setBrand(fetchedProduct.getBrand());
        }

        //更新model
        if (!StringUtils.isEmpty(fetchedProduct.getModel()) && !StringUtils.isEqual(fetchedProduct.getModel(), cmpSku.getModel())) {
            ptmCmpSkuUpdater.getPo().setModel(fetchedProduct.getModel());
        }

        ptmCmpSkuUpdater.getPo().setUpdateTime(TimeUtils.nowDate());

        //更新 deliveryTime
//        String deliveryTime = fetchedProduct.getDeliveryTime();
        String deliveryTime = "1-5";
        ptmCmpSkuUpdater.getPo().setDeliveryTime(deliveryTime);

        dbm.update(ptmCmpSkuUpdater);
    }

    private void createOrUpdatePtmCmpSkuImage(long id, List<String> imageUrlList) {

        PtmCmpSkuImage ptmCmpSkuImage = new PtmCmpSkuImage();

        ptmCmpSkuImage.setId(id);
        ptmCmpSkuImage.setOriImageUrlNumber(imageUrlList.size() >= 4 ? 4 : imageUrlList.size());//如果数量大于4，就存4张

        for (int i = 0; i < imageUrlList.size(); i++) {

            if (i == 0) {
                ptmCmpSkuImage.setOriImageUrl1(imageUrlList.get(i));
            } else if (i == 1) {
                ptmCmpSkuImage.setOriImageUrl2(imageUrlList.get(i));
            } else if (i == 2) {
                ptmCmpSkuImage.setOriImageUrl3(imageUrlList.get(i));
            } else if (i == 3) {
                ptmCmpSkuImage.setOriImageUrl4(imageUrlList.get(i));
            } else {
                continue;
            }
        }

        ptmCmpSkuImageService.createPtmCmpSkuImage(ptmCmpSkuImage);
    }
}
