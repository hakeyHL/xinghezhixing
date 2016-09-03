package hasoffer.task.worker;

import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.product.IFetchService;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.sites.flipkart.FlipkartHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2015/12/21.
 */
public class CmpSkuUpdateWorker implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(CmpSkuUpdateWorker.class);
    private ConcurrentLinkedQueue<PtmCmpSku> skuQueue;
    private ICmpSkuService cmpSkuService;
    private IFetchService fetchService;

    public CmpSkuUpdateWorker(ConcurrentLinkedQueue<PtmCmpSku> skuQueue, ICmpSkuService cmpSkuService, IFetchService fetchService) {
        this.skuQueue = skuQueue;
        this.cmpSkuService = cmpSkuService;
        this.fetchService = fetchService;
    }

    private static List<Website> websiteList = new ArrayList<Website>();

    static {

        websiteList.add(Website.FLIPKART);
        websiteList.add(Website.SNAPDEAL);
        websiteList.add(Website.PAYTM);
        websiteList.add(Website.AMAZON);

    }

    @Override
    public void run() {
        while (true) {
            try {
                PtmCmpSku sku = skuQueue.poll();

                if (sku == null) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                        logger.debug("task update get null sleep 3 seconds");
                    } catch (InterruptedException e) {
                        return;
                    }
                    continue;
                }

                if (!websiteList.contains(sku.getWebsite())) {
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

                OriFetchedProduct oriFetchedProduct = null;

                logger.debug("parse start");
                oriFetchedProduct = fetchService.fetchSummaryProductByUrl(url);
                logger.debug("parse finish");

                //此处是FK、SD正常更新逻辑放弃对title字段的更新，该有另外的task统一维护
                if (oriFetchedProduct != null) {
                    if (Website.FLIPKART.equals(oriFetchedProduct.getWebsite()) || Website.SNAPDEAL.equals(oriFetchedProduct.getWebsite())) {
                        oriFetchedProduct.setTitle(null);
                    }
                }

                try {
                    logger.debug("start update");
                    cmpSkuService.updateCmpSkuByOriFetchedProduct(sku.getId(), oriFetchedProduct);
                    logger.debug("update finish");
                    logger.debug(sku.getId() + " fetch success " + website);
                } catch (Exception e) {
                    logger.debug(e.toString());
                    if (oriFetchedProduct != null) {
                        logger.debug("title:" + oriFetchedProduct.getTitle());
                    }
                }

            } catch (Throwable e) {
                logger.debug(e.toString());
            }
        }

    }
}
