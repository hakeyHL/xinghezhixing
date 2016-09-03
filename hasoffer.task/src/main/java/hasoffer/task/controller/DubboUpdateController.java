package hasoffer.task.controller;

import hasoffer.base.enums.TaskLevel;
import hasoffer.base.enums.TaskStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.JSONUtil;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.product.IProductService;
import hasoffer.core.product.IPtmCmpSkuImageService;
import hasoffer.core.user.IPriceOffNoticeService;
import hasoffer.data.redis.IRedisListService;
import hasoffer.dubbo.api.fetch.service.IFetchDubboService;
import hasoffer.spider.model.FetchResult;
import hasoffer.spider.model.FetchUrlResult;
import hasoffer.spider.model.FetchedProduct;
import hasoffer.task.worker.CmpSkuDubboUpdateWorker;
import hasoffer.task.worker.SrmProductSearchCountListWorker;
import hasoffer.task.worker.TopSellingListWorker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created on 2016/6/22.
 */
@Controller
@RequestMapping(value = "/dubbofetchtask")
public class DubboUpdateController {

    private static AtomicBoolean taskRunning1 = new AtomicBoolean(false);
    private static AtomicBoolean taskRunning2 = new AtomicBoolean(false);

    @Resource
    @Qualifier("fetchDubboService")
    IFetchDubboService fetchDubboService;
    @Resource
    ICmpSkuService cmpSkuService;
    @Resource
    IProductService productService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;
    @Resource
    IPtmCmpSkuImageService ptmCmpSkuImageService;
    @Resource
    IPriceOffNoticeService priceOffNoticeService;
    @Resource
    IRedisListService redisListService;

    //dubbofetchtask/updatestart
    @RequestMapping(value = "/updatestart", method = RequestMethod.GET)
    @ResponseBody
    public String updatestart() {

        if (taskRunning1.get()) {
            return "task running.";
        }

        ExecutorService es = Executors.newCachedThreadPool();

        ConcurrentLinkedQueue<PtmCmpSku> queue = new ConcurrentLinkedQueue<>();

        es.execute(new SrmProductSearchCountListWorker(dbm, queue, fetchDubboService));

        //保证list任务优先执行
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 60; i++) {
            es.execute(new CmpSkuDubboUpdateWorker(dbm, queue, fetchDubboService, cmpSkuService, priceOffNoticeService, redisListService));
        }

        taskRunning1.set(true);

        return "ok";
    }

    //dubbofetchtask/updateTopSellingSpec
    @RequestMapping(value = "/updateTopSellingSpec", method = RequestMethod.GET)
    @ResponseBody
    public String updateTopSellingSpec() {

        if (taskRunning2.get()) {
            return "task running.";
        }

        ExecutorService es = Executors.newCachedThreadPool();

        ConcurrentLinkedQueue<PtmCmpSku> queue = new ConcurrentLinkedQueue<>();

        es.execute(new TopSellingListWorker(dbm, queue, fetchDubboService));

        for (int i = 0; i < 30; i++) {
            es.execute(new CmpSkuDubboUpdateWorker(dbm, queue, fetchDubboService, cmpSkuService, priceOffNoticeService, redisListService));
        }

        taskRunning2.set(true);

        return "ok";
    }

    //dubbofetchtask/testSingle
    @RequestMapping(value = "/testSingle/{skuid}", method = RequestMethod.GET)
    @ResponseBody
    public String testSingle(@PathVariable long skuid) {

        PtmCmpSku ptmCmpSku = dbm.get(PtmCmpSku.class, skuid);

        if (ptmCmpSku == null) {
            return "sku do not exists";
        }

        Website website = ptmCmpSku.getWebsite();
        String url = ptmCmpSku.getUrl();

        fetchDubboService.sendUrlTask(website, url, TaskLevel.LEVEL_1);
        System.out.println("send single url success for " + skuid);

        while (true) {
            TaskStatus taskStatus = fetchDubboService.getUrlTaskStatus(website, url);
            if (TaskStatus.FINISH.equals(taskStatus)) {
                break;
            } else {
                System.out.println(taskStatus);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {

                }
            }
        }

        FetchUrlResult fetchUrlResult = fetchDubboService.getProductsByUrl(skuid, website, url);

        FetchedProduct fetchedProduct = fetchUrlResult.getFetchProduct();

        System.out.println(JSONUtil.toJSON(fetchedProduct).toString() + "id=" + skuid);

        cmpSkuService.createDescription(ptmCmpSku, fetchedProduct);

        cmpSkuService.updateCmpSkuBySpiderFetchedProduct(skuid, fetchedProduct);

        cmpSkuService.createPtmCmpSkuImage(skuid, fetchedProduct);

        return "ok";
    }

    //dubbofetchtask/testListSingle
    @RequestMapping(value = "/testListSingle/{websiteString}/{keyword}", method = RequestMethod.GET)
    @ResponseBody
    public String testListSingle(@PathVariable String websiteString, @PathVariable String keyword) {

        Website website = Website.valueOf(websiteString.toUpperCase());

        fetchDubboService.sendKeyWordTask(website, keyword);
        System.out.println("send single list processor success for " + websiteString + " " + keyword);

        while (true) {
            TaskStatus taskStatus = fetchDubboService.getKeyWordTaskStatus(website, keyword);
            if (TaskStatus.FINISH.equals(taskStatus)) {
                break;
            } else {
                System.out.println(taskStatus);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {

                }
            }
        }

        FetchResult fetchResult = fetchDubboService.getProductsKeyWord(website, keyword);

        List<FetchedProduct> products = fetchResult.getFetchProducts();

        for (FetchedProduct product : products) {
            System.out.println(product);
        }

        return "ok";
    }
}
