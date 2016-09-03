package hasoffer.task.worker;

import hasoffer.core.persistence.po.ptm.PtmTopSelling;
import hasoffer.core.product.IProductService;
import hasoffer.core.redis.ICacheService;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;

import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/7/19.
 */
public class TopSellingPriceUpdateWorker implements Runnable {

    private IProductService productService;
    private ListProcessWorkerStatus<PtmTopSelling> ws;
    private ICacheService cacheService;

    public TopSellingPriceUpdateWorker(ListProcessWorkerStatus<PtmTopSelling> ws, IProductService productService, ICacheService cacheService) {
        this.ws = ws;
        this.productService = productService;
        this.cacheService = cacheService;
    }

    @Override
    public void run() {
        while (true) {

            PtmTopSelling topSelling = ws.getSdQueue().poll();

            if (topSelling == null) {
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {

                }
                continue;
            }

            productService.updatePtmProductPrice(topSelling.getId());

            //清除商品的缓存记录
            cacheService.del("PRODUCT_" + topSelling.getId());

            System.out.println("update success for [" + topSelling.getId() + "]");

        }
    }
}
