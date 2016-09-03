package hasoffer.job.worker;

import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.dubbo.api.fetch.service.IFetchDubboService;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.sites.flipkart.FlipkartHelper;
import hasoffer.spider.model.FetchUrlResult;
import hasoffer.spider.model.FetchedProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2015/12/21.
 */
public class CmpSkuUpdateWorker implements Runnable {
    public static List<String> ipPortList = new ArrayList<String>();
    private static Logger logger = LoggerFactory.getLogger(CmpSkuUpdateWorker.class);
    ConcurrentLinkedQueue<PtmCmpSku> skuQueue;
    ICmpSkuService cmpSkuService;
    IFetchDubboService fetchService;

    public CmpSkuUpdateWorker(ConcurrentLinkedQueue<PtmCmpSku> skuQueue, ICmpSkuService cmpSkuService, IFetchDubboService fetchService) {
        this.skuQueue = skuQueue;
        this.cmpSkuService = cmpSkuService;
        this.fetchService = fetchService;
    }

    @Override
    public void run() {

        while (true) {

            PtmCmpSku sku = skuQueue.poll();

            if (sku == null) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("update job has no jobs. go to sleep!");
                } catch (InterruptedException e) {
                    return;
                }
                continue;
            }

            // 判断，如果该sku 当天更新过价格, 直接跳过
            Date updateTime = sku.getUpdateTime();
            if (updateTime != null) {
                if (updateTime.compareTo(TimeUtils.toDate(TimeUtils.today())) > 0) {
                    continue;
                }
            }

            // try update sku
            String url = sku.getUrl();

            if (url == null) {
                continue;
            }

            Website website = WebsiteHelper.getWebSite(url);

            if (website == null) {
                logger.debug(url + " parse website get null");
            }

            if (Website.FLIPKART.equals(website)) {
                String oriUrl = sku.getOriUrl();
                if (!StringUtils.isEmpty(oriUrl)) {
                    String sourceId = FlipkartHelper.getProductIdByUrl(oriUrl);
                    if (!StringUtils.isEmpty(sourceId)) {
                        url = oriUrl;
                    }
                }
            }


            //System.setProperty("http.maxRedirects", "50");
            //System.getProperties().setProperty("proxySet", "true");
            //System.getProperties().setProperty("http.proxyHost", ip);
            //System.getProperties().setProperty("http.proxyPort", port);


            FetchedProduct fetchedProduct = null;
            try {
                FetchUrlResult productsByUrl = fetchService.getProductsByUrl(sku.getId(), null, url);
                if (productsByUrl != null) {
                    fetchedProduct = productsByUrl.getFetchProduct();
                }
            } catch (Exception e) {

                //亚马逊解析空指针，重新解析
                if (Website.AMAZON.equals(website)) {
                    if (e instanceof IOException) {
                        logger.debug("AmazonAffiliateException");
                        skuQueue.add(sku);
                    }
                }

                String message = e.getMessage();
                if (message != null) {
                    if (message.contains("302") || message.contains("404")) {
                        fetchedProduct = new FetchedProduct();
                        fetchedProduct.setTitle("url expire");
                        fetchedProduct.setSkuStatus(SkuStatus.OFFSALE);
                        fetchedProduct.setWebsite(website);
                        fetchedProduct.setUrl(url);
                    } else {
                        logger.error(e.toString() + "\n" + sku.getUrl());
                    }
                } else {
                    logger.error(e.toString() + "\n" + sku.getUrl());
                }

            }
            System.out.println(fetchedProduct);
            //try {
            //    cmpSkuService.updateCmpSkuByOriFetchedProduct(sku.getId(), fetchedProduct);
            //} catch (Exception e) {
            //    logger.debug(e.toString());
            //    if (fetchedProduct != null) {
            //        logger.debug("title:" + fetchedProduct.getTitle());
            //    }
            //}
        }
    }

}
