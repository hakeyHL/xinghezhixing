package hasoffer.job.listener;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.product.IProductService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/8/16.
 */
public class PtmProductPriceUpdateWorker implements Runnable {

    private IDataBaseManager dbm;
    private IProductService productService;

    public PtmProductPriceUpdateWorker(IDataBaseManager dbm, IProductService productService) {
        this.dbm = dbm;
        this.productService = productService;
    }

    @Override
    public void run() {

        //可以写，记得加索引
        Date t1 = TimeUtils.addDay(TimeUtils.nowDate(), -1);
        Date t2 = TimeUtils.add(t1, TimeUtils.MILLISECONDS_OF_1_MINUTE * 10);


        while (true) {

//            System.out.println("product price udpateworker start time from " + t1 + "-to-" + t2);

            //保证更新时间与当前时间有1小时时差
            if (TimeUtils.now() - t1.getTime() < TimeUtils.MILLISECONDS_OF_1_HOUR) {
                try {
                    TimeUnit.HOURS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            List<Long> productIdList = dbm.query("SELECT distinct t.productId FROM PtmCmpSku t WHERE t.updateTime > ?0 and t.updateTime < ?1", Arrays.asList(t1, t2));

            for (long productid : productIdList) {
                productService.updatePtmProductPrice(productid);
//                System.out.println("update Ptmproduct price success for " + productid);
            }

            t1 = t2;
            t2 = TimeUtils.add(t2, TimeUtils.MILLISECONDS_OF_1_MINUTE * 10);
        }

    }
}
