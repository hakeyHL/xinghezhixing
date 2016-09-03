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
public class FixFlipkartSourceSidWorker implements Runnable {

    private Logger logger = LoggerFactory.getLogger(FixFlipkartSourceSidWorker.class);

    private ListProcessWorkerStatus<PtmCmpSku> ws;
    private IDataBaseManager dbm;

    public FixFlipkartSourceSidWorker(ListProcessWorkerStatus<PtmCmpSku> ws, IDataBaseManager dbm) {
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

            Long id = sku.getId();

            String sourceSid = FlipkartHelper.getSkuIdByUrl(sku.getUrl());

            if (sourceSid == null) {
                logger.debug("get null" + sku.getUrl());
                continue;
            } else if (sourceSid.contains("itm")) {
                logger.debug("get itm" + sku.getUrl());
                continue;
            } else {
                logger.debug("sourceSid= " + sourceSid);
            }

            PtmCmpSkuUpdater updater = new PtmCmpSkuUpdater(id);

            updater.getPo().setSourceSid(sourceSid);

            dbm.update(updater);


        }
    }
}
