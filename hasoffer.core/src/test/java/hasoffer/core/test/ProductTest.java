package hasoffer.core.test;

import hasoffer.base.model.PageableResult;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.admin.IDealService;
import hasoffer.core.bo.system.SearchCriteria;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.PriceNode;
import hasoffer.core.persistence.mongo.PtmCmpSkuHistoryPrice;
import hasoffer.core.persistence.mongo.PtmCmpSkuLog;
import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmImage;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.search.SrmProductSearchCount;
import hasoffer.core.product.*;
import hasoffer.core.product.solr.*;
import hasoffer.core.search.ISearchService;
import hasoffer.core.task.ListProcessTask;
import hasoffer.core.task.worker.ILister;
import hasoffer.core.task.worker.IProcessor;
import jodd.util.NameValue;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Created by chevy on 2015/12/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class ProductTest {

    private static final String Q_PRODUCT_BY_CATEGORY =
            "SELECT t FROM PtmProduct t WHERE t.categoryId = ?0";
    private static final String Q_PRODUCT_ID =
            "SELECT COUNT(t.id),t.productId FROM PtmCmpSku t " +
                    "WHERE t.productId > 0 " +
                    "GROUP BY t.productId " +
                    "HAVING COUNT(t.id)>50 " +
                    "ORDER BY COUNT(t.id) DESC";
    private static String Q_PRODUCT_WEBSITE = "SELECT t from PtmProduct t where t.sourceSite = ?0";
    @Resource
    ProductIndexServiceImpl productIndexService;
    @Resource
    CmpskuIndexServiceImpl cmpskuIndexService;
    @Resource
    IProductService productService;
    @Resource
    IImageService imageService;
    @Resource
    ICategoryService categoryService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    ICmpSkuService cmpSkuService;
    @Resource
    ISearchService searchService;
    @Resource
    IFetchService fetchService;
    @Resource
    IDealService dealService;
    @Resource
    ProductIndex2ServiceImpl productIndex2Service;
    @Resource
    IMongoDbManager mdm;

    @Resource
    MongoDbFactory mongoDbFactory;
    private Pattern PATTERN_IN_WORD = Pattern.compile("[^0-9a-zA-Z\\-]");

    private Pattern PATTERN_Brand = Pattern.compile("[\t*?]([a-zA-Z])[\t*?]");

    @Test
    public void countModel() {

    }


    @Test
    public void fffff() throws Exception {
        List<String> lines = FileUtils.readLines(new File("d:/zzz/1.txt"));

        Set<Long> idSet = new HashSet<>();
        long count = 0;

        for (String line : lines) {
            if (NumberUtils.isNumber(line)) {
                count++;
                idSet.add(Long.valueOf(line));
            }
        }

        print("count=" + count + ", id count=" + idSet.size());
    }

    /**
     * 根据sku的品牌标记品牌
     * 规则：查询sku的品牌，如果都相同，同时商品的品牌为空，则取该品牌作为商品的品牌
     * <p>
     * param cateId
     */
//    @RequestMapping(value = "/del_sku_diff_brand", method = RequestMethod.GET)
//    @ResponseBody
//    public void tag_brand(@RequestParam final long cateId) {
    @Test
    public void tag_brand() {
        final long cateId = 5L;
        ListProcessTask<PtmProduct> productListAndProcessTask2 = new ListProcessTask<>(
                new ILister() {
                    @Override
                    public PageableResult getData(int page) {
                        return productService.listPagedProducts(cateId, page, 1000);
                    }

                    @Override
                    public boolean isRunForever() {
                        return false;
                    }

                    @Override
                    public void setRunForever(boolean runForever) {

                    }
                },
                new IProcessor<PtmProduct>() {
                    @Override
                    public void process(PtmProduct o) {
                        if (StringUtils.isEmpty(o.getBrand())) {
                            return;
                        }
                        String proBrand = o.getBrand().toLowerCase();

                        List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(o.getId());

                        if (ArrayUtils.hasObjs(cmpSkus)) {
                            for (PtmCmpSku cmpSku : cmpSkus) {
                                String skuBrand = cmpSku.getBrand();
                                if (!StringUtils.isEmpty(skuBrand)) {
                                    skuBrand = skuBrand.replaceAll("\t", "").trim();

                                    if (skuBrand.equalsIgnoreCase(proBrand) || skuBrand.contains(proBrand) || proBrand.contains(skuBrand)) {

                                    } else {
                                        print(o.getId() + "\t" + o.getBrand() + "\t" + cmpSku.getId() + "\t" + cmpSku.getBrand());
                                    }
                                }
                            }
                        } else {
                            print(o.getId() + "\t no skus.");
                        }
                    }
                }
        );

        productListAndProcessTask2.setProcessorCount(10);
        productListAndProcessTask2.setQueueMaxSize(1500);

        productListAndProcessTask2.go();
    }

    private void print(String str) {
        System.out.println(str);
    }

    @Test
    public void testArray() {
        Date date1 = TimeUtils.stringToDate("2016-07-15 08:00:00", "yyyy-MM-dd HH:mm:ss");
        Date date2 = TimeUtils.addDay(date1, 1);
        Query query = new Query(Criteria.where("pcsId").is(7134867L).and("priceTime").gt(date1).lte(date2));
        List<PtmCmpSkuLog> cmpSkuLogs = mdm.query(PtmCmpSkuLog.class, query);
        print(cmpSkuLogs.size() + "");
    }

    private void showList(List<Integer> list) {
        System.out.println("------------------");
        for (Integer i : list) {
            System.out.print(i + "\t");
        }
        System.out.println();
    }

    /**
     * 1- sku 有多少有brand，model，同时都有
     */
    @Test
    public void querySku() {

        final long cateId = 5L;

        ListProcessTask<PtmProduct> productListAndProcessTask2 = new ListProcessTask<>(
                new ILister() {
                    @Override
                    public PageableResult getData(int page) {
                        return productService.listPagedProducts(cateId, page, 1000);
                    }

                    @Override
                    public boolean isRunForever() {
                        return false;
                    }

                    @Override
                    public void setRunForever(boolean runForever) {

                    }
                },
                new IProcessor<PtmProduct>() {
                    @Override
                    public void process(PtmProduct o) {

                    }
                }
        );

        productListAndProcessTask2.setProcessorCount(10);
        productListAndProcessTask2.setQueueMaxSize(1500);

        productListAndProcessTask2.go();
    }

    @Test
    public void querySkuPrice2() {

        Criteria baseCriteria = Criteria.where("id").is(30665);

        Query q = new Query();
        q.addCriteria(baseCriteria);

        List<PtmCmpSkuHistoryPrice> datas = mdm.query(PtmCmpSkuHistoryPrice.class, q);

        if (datas.size() > 0) {
            PtmCmpSkuHistoryPrice historyPrice = datas.get(0);
            PriceNode pn = historyPrice.getPriceNodes().get(0);

            cmpSkuService.saveHistoryPrice(30665, pn.getPriceTime(), pn.getPrice());

            System.out.println(historyPrice.getPriceNodes().size());
        }
    }

    @Test
    public void querySkuPrice0() {
        cmpSkuService.saveHistoryPrice(4L, TimeUtils.addDay(TimeUtils.nowDate(), -13), 200);
    }

    @Test
    public void convertdatas() {
        final Date startD = TimeUtils.stringToDate("2016-08-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        final Date endD = TimeUtils.add(startD, 1);
        ListProcessTask<PtmCmpSkuLog> listAndProcessTask2 = new ListProcessTask<>(
                new ILister<PtmCmpSkuLog>() {
                    @Override
                    public PageableResult<PtmCmpSkuLog> getData(int page) {
                        Query query = new Query(Criteria.where("priceTime").gt(startD).lte(endD));
                        PageableResult<PtmCmpSkuLog> datas = mdm.queryPage(PtmCmpSkuLog.class, new Query(), page, 200);
                        print("page=" + page + ", datas=");
                        return datas;
                    }

                    @Override
                    public boolean isRunForever() {
                        return false;
                    }

                    @Override
                    public void setRunForever(boolean runForever) {

                    }
                },
                new IProcessor<PtmCmpSkuLog>() {
                    @Override
                    public void process(PtmCmpSkuLog o) {
                        cmpSkuService.saveHistoryPrice(o.getPcsId(), o.getPriceTime(), o.getPrice());
                    }
                }
        );

        listAndProcessTask2.setQueueMaxSize(100);
        listAndProcessTask2.setProcessorCount(1);

        listAndProcessTask2.go();
    }

    @Test
    public void querySkuPrice1() {
        long id = 4L;
        if (mdm.exists(PtmCmpSkuHistoryPrice.class, id)) {
//            PtmCmpSkuHistoryPrice historyPrice = mdm.queryOne(PtmCmpSkuHistoryPrice.class, id);
//            System.out.println(historyPrice.getId());
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testSpellCheck() {
        String brand = "iphone mobila";
        Map<String, List<String>> strs = null;//productIndex2Service.spellCheck(brand);
        for (Map.Entry<String, List<String>> str : strs.entrySet()) {
            System.out.println(str.getKey() + "\n");
            for (String s : str.getValue()) {
                System.out.print(s + "\t");
            }
            System.out.println("\t");
        }
    }

    @Test
    public void show() {
        String str = "\\n3.1737967 = sum of:\\n  3.1403005 = sum of:\\n    0.7952872 = max of:\\n      0.7952872 = weight(title:lenovo^50.0 in 40304) [DefaultSimilarity], result of:\\n        0.7952872 = score(doc=40304,freq=1.0), product of:\\n          0.15440796 = queryWeight, product of:\\n            50.0 = boost\\n            5.150558 = idf(docFreq=2843, maxDocs=180507)\\n            5.995776E-4 = queryNorm\\n          5.150558 = fieldWeight in 40304, product of:\\n            1.0 = tf(freq=1.0), with freq of:\\n              1.0 = termFreq=1.0\\n            5.150558 = idf(docFreq=2843, maxDocs=180507)\\n            1.0 = fieldNorm(doc=40304)\\n    2.3450134 = max of:\\n      0.45247084 = weight(model:appl^10.0 in 40304) [DefaultSimilarity], result of:\\n        0.45247084 = score(doc=40304,freq=3.0), product of:\\n          0.039576527 = queryWeight, product of:\\n            10.0 = boost\\n            6.6007347 = idf(docFreq=666, maxDocs=180507)\\n            5.995776E-4 = queryNorm\\n          11.432808 = fieldWeight in 40304, product of:\\n            1.7320508 = tf(freq=3.0), with freq of:\\n              3.0 = termFreq=3.0\\n            6.6007347 = idf(docFreq=666, maxDocs=180507)\\n            1.0 = fieldNorm(doc=40304)\\n      1.314008 = weight(title:appl^50.0 in 40304) [DefaultSimilarity], result of:\\n        1.314008 = score(doc=40304,freq=2.0), product of:\\n          0.16689727 = queryWeight, product of:\\n            50.0 = boost\\n            5.567161 = idf(docFreq=1874, maxDocs=180507)\\n            5.995776E-4 = queryNorm\\n          7.8731546 = fieldWeight in 40304, product of:\\n            1.4142135 = tf(freq=2.0), with freq of:\\n              2.0 = termFreq=2.0\\n            5.567161 = idf(docFreq=1874, maxDocs=180507)\\n            1.0 = fieldNorm(doc=40304)\\n      2.3450134 = weight(brand:appl^80.0 in 40304) [DefaultSimilarity], result of:\\n        2.3450134 = score(doc=40304,freq=1.0), product of:\\n          0.33538246 = queryWeight, product of:\\n            80.0 = boost\\n            6.9920573 = idf(docFreq=450, maxDocs=180507)\\n            5.995776E-4 = queryNorm\\n          6.9920573 = fieldWeight in 40304, product of:\\n            1.0 = tf(freq=1.0), with freq of:\\n              1.0 = termFreq=1.0\\n            6.9920573 = idf(docFreq=450, maxDocs=180507)\\n            1.0 = fieldNorm(doc=40304)\\n  0.033496123 = FunctionQuery(sum(100.0*float(sqrt(log(1.0*float(long(searchCount))+2.0)))+1.0)), product of:\\n    55.8662 = sum(100.0*float(sqrt(log(1.0*float(long(searchCount)=0)+2.0)))+1.0)\\n    1.0 = boost\\n    5.995776E-4 = queryNorm\\n";
        print(str);
    }

    @Test
    public void testNewSolr() {
        SearchCriteria sc = new SearchCriteria();
        sc.setKeyword("iphone 6s");//"iphone 6s", 1, 10, SearchResultSort.RELEVANCE
        sc.setPage(1);
        sc.setPageSize(10);
        sc.setPivotFields(Arrays.asList("cate2", "cate3"));
        PageableResult<ProductModel2> pms = productIndex2Service.searchProducts(sc);
        List<ProductModel2> pmList = pms.getData();
        Map<String, List<NameValue>> pfvs = pms.getPivotFieldVals();

        for (Map.Entry<String, List<NameValue>> pfv : pfvs.entrySet()) {
            String id = pfv.getKey();
            List<NameValue> datas = pfv.getValue();
            for (NameValue nv : datas) {

                long cateId = (long) nv.getName();
                PtmCategory category = categoryService.getCategory(cateId);
                if (category == null) {
                    continue;
                }
                System.out.println(nv.getValue() + "\t" + nv.getName() + "\t" + category.getName());
            }
        }
    }

    @Test
    public void expPriceExcept2() throws Exception {
        String sql = "SELECT t FROM PtmProduct t WHERE t.categoryId=?0";

        List<PtmProduct> products = dbm.query(sql, Arrays.asList(5L));

        File file = hasoffer.base.utils.FileUtils.createFile("d:/tmp/price_3.txt", true);

        StringBuilder sb = new StringBuilder();

        int count = 0;
        for (PtmProduct product : products) {
            long productId = product.getId();
            if (product == null) {
                print(String.format("product is null...[%d]", productId));
            } else {
                List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(productId);
                float minPrice = -1, maxPrice = -1;
                for (PtmCmpSku cmpSku : cmpSkus) {
                    float price = cmpSku.getPrice();
                    if (cmpSku.getStatus() == SkuStatus.OFFSALE || price <= 0) {
                        continue;
                    }

                    if (minPrice < 0) {
                        minPrice = price;
                        maxPrice = minPrice;
                    }

                    if (minPrice > price) {
                        minPrice = price;
                    }

                    if (maxPrice < price) {
                        maxPrice = price;
                    }
                }

                if (maxPrice < 0) {
                    print(String.format("no price...[%d]", productId));
                    continue;
                }

                if (maxPrice / minPrice > 6) {
                    sb.append(productId).append("\t").append(maxPrice).append("\t").append(minPrice).append("\n");
                    if (count++ % 100 == 0) {
                        FileUtils.write(file, sb.toString(), true);
                        sb = new StringBuilder();
                    }
                    print(String.format("price max/min > 6...[%d]", productId));
                } else {
                    print("ok");
                }
            }
        }
    }

    @Test
    public void expPriceExcept() throws Exception {
        String sql = "SELECT t FROM SrmProductSearchCount t WHERE t.ymd='20160815' order by t.count DESC";

        List<SrmProductSearchCount> searchCounts = dbm.query(sql);

        File file = hasoffer.base.utils.FileUtils.createFile("d:/tmp/price.txt", true);

        StringBuilder sb = new StringBuilder();

        int count = 0;
        for (int i = 0; i < searchCounts.size(); i++) {
            SrmProductSearchCount searchCount = searchCounts.get(i);
            long productId = searchCount.getProductId();
            PtmProduct product = dbm.get(PtmProduct.class, productId);
            if (product == null) {
                print(String.format("product is null...[%d]", productId));
            } else {
                List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(productId);
                float minPrice = -1, maxPrice = -1;
                for (PtmCmpSku cmpSku : cmpSkus) {
                    float price = cmpSku.getPrice();
                    if (cmpSku.getStatus() == SkuStatus.OFFSALE || price <= 0) {
                        continue;
                    }

                    if (minPrice < 0) {
                        minPrice = price;
                        maxPrice = minPrice;
                    }

                    if (minPrice > price) {
                        minPrice = price;
                    }

                    if (maxPrice < price) {
                        maxPrice = price;
                    }
                }

                if (maxPrice < 0) {
                    print(String.format("no price...[%d]", productId));
                    continue;
                }

                if (maxPrice / minPrice > 6) {
                    sb.append(productId).append("\t").append(maxPrice).append("\t").append(minPrice).append("\n");
                    if (count++ % 100 == 0) {
                        print(count + "/" + i);
                        FileUtils.write(file, sb.toString(), true);
                        sb = new StringBuilder();
                    }
                    print(String.format("price max/min > 6...[%d]", productId));
                } else {
                    print("ok");
                }
            }
        }
    }

    @Test
    public void testFlipkartSKu() {
        final Website[] sites = new Website[]{Website.SNAPDEAL, Website.SHOPCLUES, Website.EBAY, Website.AMAZON, Website.PAYTM};

        final SiteCount siteCount = new SiteCount();

        ListProcessTask<PtmProduct> listAndProcessTask2 = new ListProcessTask<>(
                new ILister() {
                    @Override
                    public PageableResult getData(int page) {
                        return dbm.queryPage(Q_PRODUCT_WEBSITE, page, 2000, Arrays.asList(siteCount.site.name()));
                    }

                    @Override
                    public boolean isRunForever() {
                        return false;
                    }

                    @Override
                    public void setRunForever(boolean runForever) {

                    }
                },
                new IProcessor<PtmProduct>() {
                    @Override
                    public void process(PtmProduct o) {
                        List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(o.getId());
                        boolean hasFlipSKu = false;

                        for (PtmCmpSku cmpSku : cmpSkus) {
                            if (cmpSku.getWebsite() == Website.FLIPKART) {
                                hasFlipSKu = true;
                            }
                        }

                        if (hasFlipSKu) {
                            siteCount.countHasFlipkartSku.addAndGet(1);
                        } else {
                            siteCount.countNoFlipkartSku.addAndGet(1);
                        }
                    }
                }
        );

        for (Website site : sites) {
            siteCount.reset(site);

            listAndProcessTask2.go();

            siteCount.show();
        }

    }

    @Test
    public void importDeal2Solr() {
        dealService.reimportAllDeals2Solr();
    }

    @Test
    public void testCmpskuSolr() {
        PageableResult<CmpSkuModel> pagedResults = cmpskuIndexService.searchSku("Nike Kobe Mamba", 1, 5);

        List<CmpSkuModel> cmpSkuModels = pagedResults.getData();
        for (CmpSkuModel cmpSkuModel : cmpSkuModels) {
            System.out.println(cmpSkuModel.getTitle());
        }
    }

    @Test
    public void downloadskuimage() {
        PtmCmpSku sku = dbm.get(PtmCmpSku.class, 1L);
        cmpSkuService.downloadImage(sku);
    }

    @Test
    public void stat() {
        long cateId = 459;

        int page = 1, PAGE_SIZE = 500;

        PageableResult<PtmProduct> pagedProducts = productService.listPagedProducts(cateId, 1, 500);

        List<PtmProduct> products = pagedProducts.getData();
        long totalPage = pagedProducts.getTotalPage();
//        totalPage = 5;
        Map<String, Long> statMap = new HashMap<String, Long>();

        long productCount = 0;
        while (page <= totalPage) {
            if (page > 1) {
                products = dbm.query(Q_PRODUCT_BY_CATEGORY, page, PAGE_SIZE, Arrays.asList(cateId));
            }
            if (ArrayUtils.hasObjs(products)) {
                for (PtmProduct product : products) {
                    analysis(statMap, product.getTitle());
                    productCount++;
                }
            }
            page++;
        }

        double t = productCount * 0.1;
        for (Map.Entry<String, Long> kv : statMap.entrySet()) {
            if (kv.getValue() <= t) {
                continue;
            }
            System.out.println(kv.getKey() + "\t\t" + kv.getValue());
        }

        System.out.println("products : " + productCount + ",total found products : " + pagedProducts.getNumFund());
    }

    @Test
    public void createCateIndex() {
        List<PtmCategory> cates = categoryService.listSubCategories(0L);
        for (PtmCategory cate : cates) {
            System.out.println(cate.toString());

            List<PtmCategory> subCates = categoryService.listSubCategories(cate.getId());

            for (PtmCategory subCate : subCates) {
                System.out.println("----" + subCate.toString());
                String keys = statForSolr(subCate.getId(), subCate.getLevel());
                if (!StringUtils.isEmpty(keys)) {
                    categoryService.updateCategoryIndex(subCate.getId(), keys);
                }
            }
        }
    }

    @Test
    public void countCate() {
        List<PtmCategory> cates = categoryService.listSubCategories(0L);
        int count1 = 0, count2 = 0;
        for (PtmCategory cate : cates) {
            List<PtmCategory> subCates = categoryService.listSubCategories(cate.getId());

            for (PtmCategory subCate : subCates) {
                if (StringUtils.isEmpty(subCate.getKeyword())) {
                    System.out.println(subCate);
                    count2++;
                } else {
                    count1++;
                }

            }
        }

        System.out.println(count1 + "\t" + count2);
    }

    @Test
    public void statForSolr() {
        statForSolr(39, 2);
    }

    public String statForSolr(long cateId, int level) {

        int page = 1, PAGE_SIZE = 500;
        PageableResult<Long> pagedIds = productIndexService.searchPro(cateId, level, null, page, PAGE_SIZE);
        List<PtmProduct> products = productService.getProducts(pagedIds.getData());

        long totalPage = pagedIds.getTotalPage();

        Map<String, Long> statMap = new HashMap<String, Long>();

        long productCount = 0;
        while (page <= totalPage) {
            if (page > 1) {
                pagedIds = productIndexService.searchPro(cateId, level, null, page, PAGE_SIZE);
                products = productService.getProducts(pagedIds.getData());
            }
            if (ArrayUtils.hasObjs(products)) {
                for (PtmProduct product : products) {
                    analysis(statMap, product.getTitle());
                    productCount++;
                }
            }
            page++;
        }

        double t = productCount * 0.05;
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Long> kv : statMap.entrySet()) {
            if (kv.getValue() <= t) {
                continue;
            }
            sb.append(kv.getKey()).append(" ");
            System.out.println(kv.getKey() + "\t\t" + kv.getValue());
        }

        System.out.println("category: " + cateId + ", products : " + productCount + ",total found products : " + pagedIds.getNumFund());
        return sb.toString();
    }

    private void analysis(Map<String, Long> statMap, String title) {
        String[] words = title.split(" ");
        for (String w : words) {
            w = w.toLowerCase().trim();
            if (PATTERN_IN_WORD.matcher(w).find()) {
//                System.out.println(w);
                continue;
            }
            Long count = statMap.get(w);
            if (count == null) {
                count = new Long(1);
                statMap.put(w, count);
            } else {
                count++;
                statMap.put(w, count);
            }
        }
    }

    @Test
    public void solr() {
        productService.reimport2Solr(true);
    }

    @Test
    public void image() {
        PtmImage image = dbm.get(PtmImage.class, 20L);

        boolean t = imageService.downloadImage(image);

        t = false;
    }

    @Test
    public void f() {
        String title = "LG 43LF6300 10cm(43) Full HD Smart LED Television";
        title = title.replaceAll("\\s+", " ");
        String key0 = title;
        String key1 = "";
        if (!StringUtils.isEmpty(title)) {
            String[] ts = title.split(" ");
            if (ts.length > 3) {
                int index = title.indexOf(ts[2]) + ts[2].length();
                key0 = title.substring(0, index);
                key1 = title.substring(index + 1);
            }
        }
        System.out.println(key0);
        System.out.println(key1);
    }

    @Test
    public void getImage() {
        String url = "https://d1nfvnlhmjw5uh.cloudfront.net/8936-2-desktop-normal.jpg";
        PtmImage image = new PtmImage();
        image.setImageUrl(url);
        image.setId(123l);
        imageService.downloadImage(image);
    }

    class SiteCount {
        final AtomicInteger countHasFlipkartSku = new AtomicInteger(0);
        final AtomicInteger countNoFlipkartSku = new AtomicInteger(0);
        Website site;

        @Override
        public String toString() {
            return "SiteCount{" +
                    "site=" + site +
                    ", countHasFlipkartSku=" + countHasFlipkartSku.get() +
                    ", countNoFlipkartSku=" + countNoFlipkartSku.get() +
                    '}';
        }

        public void show() {
            System.out.println(this.toString());
        }

        public void reset(Website site) {
            this.site = site;
            this.countHasFlipkartSku.set(0);
            this.countNoFlipkartSku.set(0);
        }
    }
}
