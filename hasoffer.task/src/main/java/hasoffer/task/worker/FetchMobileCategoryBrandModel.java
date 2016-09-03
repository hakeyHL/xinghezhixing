package hasoffer.task.worker;

import hasoffer.base.enums.TaskLevel;
import hasoffer.base.enums.TaskStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.JSONUtil;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.dubbo.api.fetch.service.IFetchDubboService;
import hasoffer.spider.model.FetchUrlResult;
import hasoffer.spider.model.FetchedProduct;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/8/26.
 */
public class FetchMobileCategoryBrandModel implements Runnable {

    private ConcurrentLinkedQueue<PtmCmpSku> cmpSkuQueue;
    private IFetchDubboService fetchDubboService;
    private ICmpSkuService cmpSkuService;

    public FetchMobileCategoryBrandModel(ConcurrentLinkedQueue<PtmCmpSku> cmpSkuQueue, IFetchDubboService fetchDubboService, ICmpSkuService cmpSkuService) {
        this.cmpSkuQueue = cmpSkuQueue;
        this.fetchDubboService = fetchDubboService;
        this.cmpSkuService = cmpSkuService;
    }

    @Override
    public void run() {
        while (true) {

            try {


                System.out.println("thread alive");
                PtmCmpSku ptmcmpsku = cmpSkuQueue.poll();

                if (ptmcmpsku == null) {
                    System.out.println("pull get null wait 5 seconds");
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {

                    }
                    continue;
                }

                long skuid = ptmcmpsku.getId();
                Website website = ptmcmpsku.getWebsite();
                String url = ptmcmpsku.getUrl();

                System.out.println(skuid + "_" + website + "_" + url);
                TaskStatus taskStatus = fetchDubboService.getUrlTaskStatus(website, url);
                System.out.println(taskStatus);

                FetchUrlResult fetchUrlResult = null;

                //如果返回结果状态为running，那么将sku返回队列
                if (TaskStatus.RUNNING.equals(taskStatus) || TaskStatus.START.equals(taskStatus)) {
                    cmpSkuQueue.add(ptmcmpsku);
                    System.out.println("taskstatus RUNNING for [" + skuid + "]");
                    continue;
                } else if (TaskStatus.STOPPED.equals(taskStatus)) {
                    System.out.println("taskstatus STOPPED for [" + skuid + "]");
                    continue;
                } else if (TaskStatus.EXCEPTION.equals(taskStatus)) {
                    System.out.println("taskstatus EXCEPTION for [" + skuid + "]");
                    continue;
                } else if (TaskStatus.NONE.equals(taskStatus)) {
                    cmpSkuQueue.add(ptmcmpsku);
                    if (Website.SNAPDEAL.equals(website) || Website.FLIPKART.equals(website) || Website.AMAZON.equals(website) || Website.EBAY.equals(website)) {
                        cmpSkuQueue.add(ptmcmpsku);
                        fetchDubboService.sendUrlTask(ptmcmpsku.getWebsite(), ptmcmpsku.getUrl(), TaskLevel.LEVEL_2);
                    }
                    System.out.println("taskstatus NONE for [" + skuid + "] , resend success");
                    continue;
                } else {//(TaskStatus.FINISH.equals(taskStatus)))
                    System.out.println("taskstatus FINISH for [" + skuid + "]");
                    fetchUrlResult = fetchDubboService.getProductsByUrl(skuid, ptmcmpsku.getWebsite(), ptmcmpsku.getUrl());

                    FetchedProduct fetchedProduct = fetchUrlResult.getFetchProduct();

                    System.out.println(JSONUtil.toJSON(fetchedProduct).toString() + "id=" + skuid);

                    try {
                        cmpSkuService.createDescription(ptmcmpsku, fetchedProduct);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        cmpSkuService.updateCmpSkuBySpiderFetchedProduct(skuid, fetchedProduct);
                    } catch (Exception e) {

                    }

                    try {
                        cmpSkuService.createPtmCmpSkuImage(skuid, fetchedProduct);
                    } catch (Exception e) {

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

