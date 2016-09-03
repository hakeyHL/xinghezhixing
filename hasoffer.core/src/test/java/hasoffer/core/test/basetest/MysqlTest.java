package hasoffer.core.test.basetest;

import hasoffer.affiliate.affs.flipkart.FlipkartAffiliateProductProcessor;
import hasoffer.affiliate.model.AffiliateProduct;
import hasoffer.base.model.PageableResult;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.JSONUtil;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmImage;
import hasoffer.core.persistence.po.ptm.updater.PtmImageUpdater;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.product.IPtmCmpSkuImageService;
import hasoffer.core.search.ISearchService;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.sites.flipkart.FlipkartHelper;
import hasoffer.spider.model.FetchedProduct;
import jodd.io.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/4/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class MysqlTest {

    private static final String Q_PTM_CMPSKU =
            "SELECT t FROM PtmCmpSku t " +
                    " ORDER BY t.id ";

    private static final String Q_PTM_CMPSKU_AMAZON =
            "SELECT t FROM PtmCmpSku t WHERE t.website = 'AMAZON' " +
                    " ORDER BY t.id ";
    private static final String Q_PTM_CMPSKU_NOTAMAZON =
            "SELECT t FROM PtmCmpSku t WHERE t.website <> 'AMAZON' " +
                    "ORDER BY t.id";

    private static final String Q_PTMCMPSKU_ERROR = "SELECT t FROM PtmCmpSku t WHERE t.productId = 999999999 ORDER BY t.id ASC ";
    private static final String Q_PTMCMPSKU_ERROR_BYID = "SELECT t FROM PtmCmpSku t WHERE t.id = ?0 ";

    private static final String Q_PTMCMPSKU_BYSID = "SELECT t FROM PtmCmpSku t WHERE t.sourceSid = ?0 ";

    private static final String Q_WEBSITE_PTMCMPSKU_ONSALE = "SELECT COUNT(*) FROM PtmCmpSku t WHERE t.website = ?0 AND t.status = 'ONSALE' ";

    private static final String Q_SRMSEARCHLOG_TIMERSET2 = "SELECT t FROM SrmSearchLog t WHERE t.precise = 'TIMERSET2' ";

    private static final String Q_PTMCMPSKU_PRODUCTID = "SELECT t FROM PtmCmpSku t WHERE t.productId = ?0 ";
    @Resource
    IDataBaseManager dbm;
    @Resource
    ICmpSkuService cmpSkuService;
    @Resource
    ISearchService searchService;
    @Resource
    IPtmCmpSkuImageService ptmCmpSkuImageService;
    private Logger logger = LoggerFactory.getLogger(MysqlTest.class);
    private ConcurrentLinkedQueue<PtmCmpSku> skuQueue = new ConcurrentLinkedQueue<PtmCmpSku>();

    @Test
    public void testSkuUpdate() {

        FetchedProduct fetchedProduct = new FetchedProduct();
//     \\\\\\N":"13810840","Duration":"1 year","UPC":"does not apply"}', description='null', model='Redmi Note 3S,', offers='null', categoryPathList=null}
        fetchedProduct.setBrand("Xiaomi");
        fetchedProduct.setTitle("Xiaomi Redmi 3S PRIME 32GB 3GB RAM - Sealed Pack");
        fetchedProduct.setWebsite(Website.EBAY);
        fetchedProduct.setSubTitle("One Year Manufacturer Warranty");
        fetchedProduct.setSourceId("121969226604");
        fetchedProduct.setSkuStatus(SkuStatus.ONSALE);
        fetchedProduct.setPrice(11999);
        fetchedProduct.setUrl("http://www.ebay.in/itm/Xiaomi-Redmi-Note-3-Gold-32-GB-");

        cmpSkuService.updateCmpSkuBySpiderFetchedProduct(123L, fetchedProduct);

    }

    @Test
    public void testDistinctQuery() {

        List<String> skuList = dbm.query("SELECT distinct t.title FROM PtmCmpSku t WHERE t.productId = ?0", Arrays.asList(12312L));

        System.out.println(skuList);

    }

    @Test
    public void testV() {
//        searchService.statSearchCount("20160306");//stime = 1457193600000
//        etime = 1457280000000
        System.out.println(TimeUtils.parse(1457193600000L, "yyyy-MM-dd HH:mm:ss"));
        System.out.println(TimeUtils.parse(1457280000000L, "yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void testLinkedQueue() {

        new Runnable() {
            @Override
            public void run() {
                while (true) {
                    r();
                }
            }
        }.run();

    }

    public void r() {
        int notAmazonPageNum = 1;
        int amazonPageNum = 1;
        int PAGE_SIZE = 500;

        PageableResult<PtmCmpSku> notAmazonResult = dbm.queryPage(Q_PTM_CMPSKU_NOTAMAZON, notAmazonPageNum, PAGE_SIZE);
        PageableResult<PtmCmpSku> amazonResult = dbm.queryPage(Q_PTM_CMPSKU_AMAZON, amazonPageNum, PAGE_SIZE);

        int notAmazonCount = (int) notAmazonResult.getTotalPage();
        int amazonCount = (int) amazonResult.getTotalPage();

        List<PtmCmpSku> notAmazonSkus = notAmazonResult.getData();
        List<PtmCmpSku> amazonSkus = notAmazonResult.getData();

        while (notAmazonPageNum <= notAmazonCount) {

            if (skuQueue.size() > 600) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                    continue;
                } catch (InterruptedException e) {
                    break;
                }
            }

            logger.info(String.format("update sku : %d/%d .", notAmazonPageNum, notAmazonCount));

            if (notAmazonPageNum > 1) {
                notAmazonSkus = dbm.query(Q_PTM_CMPSKU_NOTAMAZON, notAmazonPageNum, PAGE_SIZE);
            }

            if (ArrayUtils.hasObjs(notAmazonSkus)) {

                for (int i = 0; i < notAmazonSkus.size(); i++) {

                    PtmCmpSku cmpSku = notAmazonSkus.get(i);

                    if (cmpSku.getUpdateTime().getTime() < TimeUtils.today()) {
                        skuQueue.add(cmpSku);
                    }
                    skuQueue.add(cmpSku);
                    if (i % 10 == 0) {
                        if (amazonSkus.get(0).getUpdateTime().getTime() < TimeUtils.today()) {
                            skuQueue.add(amazonSkus.remove(0));
                            amazonCount--;
                            if (amazonCount == 0) {
                                amazonPageNum++;
                            }
                        }
                    }
                }
            }
            notAmazonPageNum++;
        }
    }


    @Test
    public void test() {

        List<Object> object = dbm.query(Q_PTMCMPSKU_BYSID, Arrays.asList("Q_PTMCMPSKU_BYSID"));

        System.out.println();

    }

    @Test
    public void get() {

        PtmCmpSku ptmCmpSku = new PtmCmpSku();

        cmpSkuService.createCmpSku(ptmCmpSku);
        System.out.println(ptmCmpSku);
    }

    @Test
    public void test1() {

        String url = "http://www.flipkart.com/samsung-galaxy-s7-edge/p/itmegmkzvcpxxqhz?pid=MOBEGFZPCHJHAZVU&ref=L%3A7915091748451274242&srno=p_1&query=Samsung+Galaxy+S7+Edge&otracker=from-search";
        String sourceId = FlipkartHelper.getSkuIdByUrl(url);
        FlipkartAffiliateProductProcessor flipkartAffilicateProductProcessor = new FlipkartAffiliateProductProcessor();
        AffiliateProduct flipkartProduct = null;
        try {
            flipkartProduct = flipkartAffilicateProductProcessor.getAffiliateProductBySourceId("MOBECCA5Y5HBYR3Q");

        } catch (Exception e) {
            e.printStackTrace();
        }

        String imageUrl = flipkartProduct.getImageUrl();
        float price = flipkartProduct.getPrice();
        String title = flipkartProduct.getTitle();
        String productStatus = flipkartProduct.getProductStatus();
        String url1 = flipkartProduct.getUrl();

        OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();
        oriFetchedProduct.setPrice(price);
        oriFetchedProduct.setSourceSid(sourceId);
        oriFetchedProduct.setImageUrl(imageUrl);
        oriFetchedProduct.setTitle(title);
        oriFetchedProduct.setUrl(url1);

        //todo 商品状态对于flipkart联盟的解析有一些问题，待改进
        if ("false".equals(productStatus)) {
            oriFetchedProduct.setProductStatus(ProductStatus.OUTSTOCK);
        } else if ("true".equals(productStatus)) {
            oriFetchedProduct.setProductStatus(ProductStatus.ONSALE);
        } else if ("none".equals(productStatus)) {
            oriFetchedProduct.setProductStatus(ProductStatus.OFFSALE);
        } else {
            oriFetchedProduct.setProductStatus(null);
        }

        cmpSkuService.updateCmpSkuByOriFetchedProduct(168542, oriFetchedProduct);

    }


    /**
     * 修复
     * 修复价格差距太大
     * 造成的错误
     * <p>
     * 记录所有被更新的sku，然后恢复
     */
    @Test
    public void fixErrorPrice() throws IOException {

        List<PtmCmpSku> skuList = dbm.query(Q_PTMCMPSKU_ERROR);

        for (PtmCmpSku sku : skuList) {

            FileUtil.appendString("C:/Users/wing/Desktop/skuid.txt", sku.getId() + "\n");

        }
    }

    @Test
    public void fixErrorPrice1() throws IOException {

        String[] strings = FileUtil.readLines("C:/Users/wing/Desktop/skuid.txt");

        for (String str : strings) {

            PtmCmpSku sku = dbm.querySingle(Q_PTMCMPSKU_ERROR_BYID, Arrays.asList(Long.parseLong(str)));

            if (sku == null) {
                logger.debug(str + " get null");
                continue;
            }

            long productId = sku.getProductId();

            FileUtil.appendString("C:/Users/wing/Desktop/skufix.sql", "update ptmcmpsku set productId = " + productId + " where id = " + sku.getId() + ";\n");
        }

    }

    @Test
    public void testSingle() {

        long number = dbm.querySingle(Q_WEBSITE_PTMCMPSKU_ONSALE, Arrays.asList(Website.FLIPKART));

        System.out.println(number);

    }

    @Test
    public void testAmount() {

        int count = 0;
        int curPage = 1;
        int pageSize = 1000;

        PageableResult<SrmSearchLog> pageableResult = dbm.queryPage(Q_SRMSEARCHLOG_TIMERSET2, curPage, pageSize);

        long totalPage = pageableResult.getTotalPage();
        List<SrmSearchLog> srmSearchLogList = pageableResult.getData();

        while (curPage <= totalPage) {

            if (curPage > 1) {
                pageableResult = dbm.queryPage(Q_SRMSEARCHLOG_TIMERSET2, curPage, pageSize);
                srmSearchLogList = pageableResult.getData();
            }

            for (SrmSearchLog log : srmSearchLogList) {

                long ptmProductId = log.getPtmProductId();

                List<PtmCmpSku> skuList = dbm.query(Q_PTMCMPSKU_PRODUCTID, Arrays.asList(ptmProductId));

                for (PtmCmpSku sku : skuList) {
                    if (Website.FLIPKART.equals(sku.getWebsite())) {
                        count++;
                        break;
                    }
                }

            }

            System.out.println("curpage = " + curPage);
            System.out.println("totalPage = " + totalPage);
            System.out.println("count = " + count);
            System.out.println("-----------------------------");
            curPage++;
        }

        System.out.println("count = " + count);
    }

    @Test
    public void testUpdate() {

        PtmImageUpdater updater = new PtmImageUpdater(15684854848L);

        PtmImage po = updater.getPo();

        System.out.println();

    }

    @Test
    public void testFind() throws IOException {

        PageableResult<PtmCmpSku> pageableResult = dbm.queryPage("SElECT t FROM PtmCmpSku t WHERE t.id < 12", 1, 3);

        String cmpSkusJson = JSONUtil.toJSON(pageableResult);

        PageableResult datas = JSONUtil.toObject(cmpSkusJson, PageableResult.class);

        List<Map> data = datas.getData();

        for (Map<String, Object> map : data) {

            Object id = map.get("id");

            if (id instanceof Integer) {
                System.out.println(1);
            }

            if (id instanceof Long) {
                System.out.println(2);
            }

        }

    }

    @Test
    public void test123() throws IOException {

        File file = new File("C:/Users/wing/Desktop/category.txt");

        List<PtmCategory> categoryList = dbm.query("SELECT t FROM PtmCategory t");

        for (PtmCategory category : categoryList) {

            PtmCategory ptmcategory = dbm.querySingle("SELECT t FROM PtmCategory t WHERE t.id = ?0 ", Arrays.asList(category.getParentId()));

            if (ptmcategory == null) {
                FileUtil.appendString(file, category.getId() + "\n");
            }

        }
    }

    @Test
    public void testFlipkartAffiliateProduct() {

        ExecutorService es = Executors.newCachedThreadPool();

        final ConcurrentLinkedQueue<PtmCmpSku> queue = new ConcurrentLinkedQueue<>();

        es.execute(new Runnable() {

            @Override
            public void run() {

                int page = 1;

                PageableResult<PtmCmpSku> pageableResult = dbm.queryPage("SELECT t FROM PtmCmpSku t WHERE t.website = 'FLIPKART' and t.id > 77594 order by t.id asc", page, 1000);

                while (true) {

                    if (queue.size() > 5000) {
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }

                    if (page > 1) {
                        pageableResult = dbm.queryPage("SELECT t FROM PtmCmpSku t WHERE t.website = 'FLIPKART' order by t.id asc", page, 1000);
                    }

                    List<PtmCmpSku> skuList = pageableResult.getData();

                    queue.addAll(skuList);

                    page++;

                }

            }
        });

        for (int i = 0; i < 10; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {

                        PtmCmpSku sku = queue.poll();

                        if (sku == null) {
                            try {
                                TimeUnit.SECONDS.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                        }

                        String sourceId = FlipkartHelper.getSkuIdByUrl(sku.getUrl());

                        FlipkartAffiliateProductProcessor flipkartAffilicateProductProcessor = new FlipkartAffiliateProductProcessor();
                        AffiliateProduct flipkartProduct = null;
                        try {

                            File file = new File("C:/Users/wing/Desktop/catepath.txt");

                            String catepath = flipkartAffilicateProductProcessor.getCatePath(sourceId);

                            FileUtil.appendString(file, catepath + "\n");
                        } catch (Exception e) {
                        }
                    }
                }
            });
        }

        while (true) {

        }

    }

}
