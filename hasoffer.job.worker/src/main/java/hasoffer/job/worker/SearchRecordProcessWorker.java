package hasoffer.job.worker;

import hasoffer.base.config.AppConfig;
import hasoffer.base.enums.HasofferRegion;
import hasoffer.base.enums.TaskStatus;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.mongo.SrmAutoSearchResult;
import hasoffer.core.search.ISearchService;
import hasoffer.core.search.SearchProductService;
import hasoffer.core.search.impl.SearchServiceImpl;
import hasoffer.dubbo.api.fetch.service.IFetchDubboService;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.model.WebFetchResult;
import hasoffer.spider.model.FetchResult;
import hasoffer.spider.model.FetchedProduct;
import hasoffer.spring.context.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/3/14
 * Function :
 */
public class SearchRecordProcessWorker implements Runnable {

    private Logger logger = LoggerFactory.getLogger(SearchRecordProcessWorker.class);

    private LinkedBlockingQueue<SrmAutoSearchResult> searchLogQueue;
    private SearchProductService searchProductService;
    private IFetchDubboService fetchService;
    private ISearchService searchService;

    //private TaskScheduleReqClient scheduleReqClient;

    public SearchRecordProcessWorker(SearchProductService searchProductService, IFetchDubboService flipkartFetchService, LinkedBlockingQueue<SrmAutoSearchResult> searchLogQueue) {
        this.searchProductService = searchProductService;
        this.searchLogQueue = searchLogQueue;
        this.fetchService = flipkartFetchService;
        this.searchService = SpringContextHolder.getBean(SearchServiceImpl.class);
        //this.scheduleReqClient = (TaskScheduleReqClient) SpringContextHolder.getBean("taskScheduleReqClient");
    }

    @Override
    public void run() {

        while (true) {
            try {
                SrmAutoSearchResult autoSearchResult = searchLogQueue.poll();
                if (autoSearchResult == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("SearchRecordProcessWorker. search-log-queue is null. go to sleep!");
                    }
                    TimeUnit.SECONDS.sleep(5);
                    continue;
                }
                //if (logger.isDebugEnabled()) {
                //    logger.debug("SearchRecordProcessWorker. search keyword {}. begin", autoSearchResult);
                //}

                // 获取mongo 中存储的数据并转换成java对象。
                boolean isFetch = false;
                String serRegion = AppConfig.get(AppConfig.SER_REGION);
                if (HasofferRegion.INDIA.toString().equals(serRegion)) {
                    isFetch = fetchForIndia(autoSearchResult);
                } else if (HasofferRegion.USA.toString().equals(serRegion)) {
                    isFetch = fetchForUsa(autoSearchResult);
                }
                //fetchByTaskSchedule(autoSearchResult);

                //是否需要重新抓取
                if (isFetch) {
                    searchLogQueue.add(autoSearchResult);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //private void fetchByTaskSchedule(SrmAutoSearchResult autoSearchResult) {
    //
    //    String keyWord = autoSearchResult.getTitle();
    //
    //    TaskSchedule taskSchedule = new TaskSchedule();
    //    taskSchedule.setWebsite(Website.AMAZON);
    //    taskSchedule.setUrlType(TaskSchedule.UrlType.LIST);
    //    taskSchedule.setRegion(HasofferRegion.INDIA);
    //    taskSchedule.setUrl(IndiaWebsiteHelper.getSearchUrl(Website.AMAZON, keyWord));
    //    scheduleReqClient.pushTaskScheduleInfo(taskSchedule);
    //}

    /**
     * 抓取并判断是否需要更新到mongodb中。
     *
     * @param autoSearchResult
     * @return
     */
    private boolean fetchForUsa(SrmAutoSearchResult autoSearchResult) {

        String keyword = StringUtils.getCleanWordString(autoSearchResult.getTitle());
        Map<Website, WebFetchResult> sitePros = autoSearchResult.getSitePros();
        FetchResult amazonFetchResult = getFetchResult(Website.AMAZON, keyword, sitePros);
        FetchResult ebayFetchResult = getFetchResult(Website.EBAY, keyword, sitePros);
        FetchResult walmartFetchResult = getFetchResult(Website.WALMART, keyword, sitePros);
        FetchResult geekFetchResult = getFetchResult(Website.GEEK, keyword, sitePros);
        FetchResult newEggFetchResult = getFetchResult(Website.NEWEGG, keyword, sitePros);
        FetchResult bestbuyFetchResult = getFetchResult(Website.BESTBUY, keyword, sitePros);

        initResultMap(autoSearchResult, amazonFetchResult);
        initResultMap(autoSearchResult, ebayFetchResult);
        initResultMap(autoSearchResult, walmartFetchResult);
        initResultMap(autoSearchResult, geekFetchResult);
        initResultMap(autoSearchResult, newEggFetchResult);
        initResultMap(autoSearchResult, bestbuyFetchResult);

        //判断是否需要重新抓取，如果需要，这放回队列中。
        if (isUpdate(amazonFetchResult, ebayFetchResult, walmartFetchResult, geekFetchResult, newEggFetchResult, bestbuyFetchResult)) {
            updateMongo(autoSearchResult);
            analysisAndRelate(autoSearchResult);
        }

        return isReFetch(amazonFetchResult, ebayFetchResult, walmartFetchResult, geekFetchResult, newEggFetchResult, bestbuyFetchResult);

    }

    /**
     * 1.判断是否需要更新该网站（website）的该商品（keyword）<br>
     * 2.需要的话，则加入更新队列。并返回一个实体。如果不需要，则返回空。
     *
     * @param website
     * @param keyword
     * @param sitePros
     * @return
     */
    private FetchResult getFetchResult(Website website, String keyword, Map<Website, WebFetchResult> sitePros) {
        WebFetchResult fetchResult = sitePros.get(website);
        long updateCycle = TimeUtils.MILLISECONDS_OF_1_HOUR * 12;
        //判断是否需要更新该网站（website）的该商品（keyword）
        boolean isFetch = fetchResult == null || System.currentTimeMillis() - fetchResult.getlUpdateDate() > updateCycle;
        // 需要的话，则加入更新队列。并返回一个实体。如果不需要，这返回空
        if (isFetch) {
            try {
                TaskStatus keyWordTaskStatus = fetchService.getKeyWordTaskStatus(website, keyword);
                if (TaskStatus.NONE.equals(keyWordTaskStatus)) {
                    fetchService.sendKeyWordTask(website, keyword);
                    return FetchResult.createFetchResult(website, keyword, TaskStatus.RUNNING);
                }
                if (TaskStatus.START.equals(keyWordTaskStatus) || TaskStatus.RUNNING.equals(keyWordTaskStatus)) {
                    return FetchResult.createFetchResult(website, keyword, TaskStatus.RUNNING);
                }
                return fetchService.getProductsKeyWord(website, keyword);
            } catch (Exception e) {
                return FetchResult.createFetchResult(website, keyword, TaskStatus.RUNNING);
            }
        }
        return null;
    }

    private boolean fetchForIndia(SrmAutoSearchResult autoSearchResult) {
        String keyword = StringUtils.getCleanWordString(autoSearchResult.getTitle());
        Map<Website, WebFetchResult> sitePros = autoSearchResult.getSitePros();
        FetchResult flipkartFetchResult = getFetchResult(Website.FLIPKART, keyword, sitePros);
        FetchResult amazonFetchResult = getFetchResult(Website.AMAZON, keyword, sitePros);
        FetchResult snapdealFetchResult = getFetchResult(Website.SNAPDEAL, keyword, sitePros);
        FetchResult shopcluesFetchResult = getFetchResult(Website.SHOPCLUES, keyword, sitePros);
        FetchResult paytmFetchResult = getFetchResult(Website.PAYTM, keyword, sitePros);
        FetchResult ebayFetchResult = getFetchResult(Website.EBAY, keyword, sitePros);
        //FetchResult myntraFetchResult = getFetchResult(Website.MYNTRA, keyword, sitePros);
        FetchResult jabongFetchResult = getFetchResult(Website.JABONG, keyword, sitePros);
        FetchResult voonikFetchResult = getFetchResult(Website.VOONIK, keyword, sitePros);
        FetchResult homeShopResult = getFetchResult(Website.HOMESHOP18, keyword, sitePros);
        FetchResult limeRoadResult = getFetchResult(Website.LIMEROAD, keyword, sitePros);
        initResultMap(autoSearchResult, flipkartFetchResult);
        initResultMap(autoSearchResult, amazonFetchResult);
        initResultMap(autoSearchResult, snapdealFetchResult);
        initResultMap(autoSearchResult, shopcluesFetchResult);
        initResultMap(autoSearchResult, paytmFetchResult);
        initResultMap(autoSearchResult, ebayFetchResult);
        //initResultMap(autoSearchResult, myntraFetchResult);
        initResultMap(autoSearchResult, jabongFetchResult);
        initResultMap(autoSearchResult, voonikFetchResult);
        initResultMap(autoSearchResult, homeShopResult);
        initResultMap(autoSearchResult, limeRoadResult);

        Boolean isUpdate = isUpdate(flipkartFetchResult, amazonFetchResult, snapdealFetchResult, shopcluesFetchResult, paytmFetchResult, ebayFetchResult, jabongFetchResult, voonikFetchResult, homeShopResult, limeRoadResult);
        //Boolean isUpdate = isUpdate(flipkartFetchResult, amazonFetchResult, snapdealFetchResult, shopcluesFetchResult, paytmFetchResult, ebayFetchResult, myntraFetchResult, jabongFetchResult, voonikFetchResult, homeShopResult, limeRoadResult);
        if (isUpdate) {
            updateMongo(autoSearchResult);
            analysisAndRelate(autoSearchResult);
        }
        //return isReFetch(flipkartFetchResult, amazonFetchResult, snapdealFetchResult, shopcluesFetchResult, paytmFetchResult, ebayFetchResult, myntraFetchResult, jabongFetchResult, voonikFetchResult, homeShopResult, limeRoadResult);
        return isReFetch(flipkartFetchResult, amazonFetchResult, snapdealFetchResult, shopcluesFetchResult, paytmFetchResult, ebayFetchResult, jabongFetchResult, voonikFetchResult, homeShopResult, limeRoadResult);
    }

    private boolean isReFetch(FetchResult... fetchResultList) {
        for (FetchResult result : fetchResultList) {
            if (result != null && !isClose(result)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否更新到mongodb。
     *
     * @param fetchResultList
     * @return
     */
    private boolean isUpdate(FetchResult... fetchResultList) {
        boolean b = false;
        for (FetchResult fetchResult : fetchResultList) {
            b = b || isClose(fetchResult);
        }
        return b;
    }

    private void updateMongo(SrmAutoSearchResult autoSearchResult) {
        autoSearchResult.setUpdateTime(new Date());
        searchProductService.saveSearchProducts(autoSearchResult);
    }

    private void analysisAndRelate(SrmAutoSearchResult autoSearchResult) {
        try {
            searchService.analysisAndRelate(autoSearchResult);
        } catch (Exception e) {
            logger.debug("[" + autoSearchResult.getId() + "]" + e.getMessage());
        }

    }

    private boolean isClose(FetchResult result) {
        return result != null && (TaskStatus.FINISH.equals(result.getTaskStatus()) || TaskStatus.STOPPED.equals(result.getTaskStatus()) || TaskStatus.EXCEPTION.equals(result.getTaskStatus()));
    }

    private void initResultMap(SrmAutoSearchResult autoSearchResult, FetchResult fetchResult) {
        //1 判断抓取有没有返回商品，没有的话直接退出。
        if (fetchResult == null || !fetchResult.overFetch()) {
            return;
        }
        Map<Website, WebFetchResult> fetchResultMap = autoSearchResult.getSitePros();
        WebFetchResult webFetchResult = fetchResultMap.get(fetchResult.getWebsite());
        if (webFetchResult == null) {
            webFetchResult = new WebFetchResult();
            fetchResultMap.put(fetchResult.getWebsite(), webFetchResult);
        }
        webFetchResult.setUpdateDate(new Date());
        webFetchResult.setTaskStatus(fetchResult.getTaskStatus());
        //List<ListProduct> listProducts = webFetchResult.getProductList();
        List<ListProduct> listProducts = new ArrayList<ListProduct>();
        List<FetchedProduct> listProductsResult = fetchResult.getFetchProducts();
        for (FetchedProduct product : listProductsResult) {
            ListProduct listProduct = new ListProduct();
            listProduct.setImageUrl(product.getImageUrl());
            listProduct.setPrice(product.getPrice());
            listProduct.setSourceId(product.getSourceId());
            SkuStatus skuStatus = product.getSkuStatus();
            if (SkuStatus.OFFSALE.equals(skuStatus)) {
                listProduct.setStatus(ProductStatus.OFFSALE);
            } else if (SkuStatus.ONSALE.equals(skuStatus)) {
                listProduct.setStatus(ProductStatus.ONSALE);
            } else if (SkuStatus.OUTSTOCK.equals(skuStatus)) {
                listProduct.setStatus(ProductStatus.OUTSTOCK);
            }
            listProduct.setSubTitle(product.getSubTitle());
            listProduct.setTitle(product.getTitle());
            listProduct.setUrl(product.getUrl());
            listProduct.setWebsite(product.getWebsite());
            listProducts.add(listProduct);
            //String id=HexDigestUtil.md5(listProduct.getTitle()+"_"+listProduct.getWebsite());
            //tempProductMap.put(id, listProduct);
        }

        webFetchResult.setProductList(listProducts);

    }
}
