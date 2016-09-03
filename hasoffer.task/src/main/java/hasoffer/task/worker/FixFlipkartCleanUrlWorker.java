package hasoffer.task.worker;

import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.updater.PtmCmpSkuUpdater;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import hasoffer.fetch.sites.flipkart.FlipkartHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/5/26.
 */
public class FixFlipkartCleanUrlWorker implements Runnable {

    private Logger logger = LoggerFactory.getLogger(FixFlipkartSourceSidWorker.class);

    private ListProcessWorkerStatus<PtmCmpSku> ws;
    private IDataBaseManager dbm;
    private int count = 0;

    public FixFlipkartCleanUrlWorker(ListProcessWorkerStatus<PtmCmpSku> ws, IDataBaseManager dbm) {
        this.ws = ws;
        this.dbm = dbm;
    }

    @Override
    public void run() {
        while (true) {

            PtmCmpSku sku = ws.getSdQueue().poll();

            if (sku == null) {
                logger.debug("fix flipart sourcesid worker get null sleep 3 seconds");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            String oriUrl = sku.getOriUrl();

            String url = FlipkartHelper.getUrlByDeeplink(oriUrl);

            String cleanUrl = FlipkartHelper.getCleanUrl(url);

            PtmCmpSkuUpdater updater = new PtmCmpSkuUpdater(sku.getId());

            updater.getPo().setUrl(cleanUrl);

            dbm.update(updater);

            logger.debug(cleanUrl);

            count++;

            if (count % 1000 == 0) {
                logger.debug("**************************************************" + count / 1000);
            }
        }
    }
}
