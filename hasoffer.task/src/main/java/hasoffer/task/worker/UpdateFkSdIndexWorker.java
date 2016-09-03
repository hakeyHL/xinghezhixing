package hasoffer.task.worker;

import hasoffer.base.enums.IndexNeed;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.utils.HexDigestUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmCmpSkuIndex2;
import hasoffer.core.persistence.po.ptm.updater.PtmCmpSkuIndex2Updater;
import hasoffer.core.persistence.po.ptm.updater.PtmCmpSkuUpdater;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/5/26.
 */
public class UpdateFkSdIndexWorker implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(UpdateFkSdIndexWorker.class);

    private ListProcessWorkerStatus<PtmCmpSku> ws;
    private IDataBaseManager dbm;
    private ICmpSkuService cmpSkuService;

    public UpdateFkSdIndexWorker(ListProcessWorkerStatus<PtmCmpSku> ws, IDataBaseManager dbm, ICmpSkuService cmpSkuService) {
        this.ws = ws;
        this.dbm = dbm;
        this.cmpSkuService = cmpSkuService;
    }

    @Override
    public void run() {
        while (true) {

            PtmCmpSku sku = ws.getSdQueue().poll();

            if (sku == null) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                    logger.debug("update flipkartSkuTitle worker get null sleep 3 seconds");
                } catch (InterruptedException e) {
                    return;
                }
                continue;
            }

            PtmCmpSkuIndex2 index = dbm.get(PtmCmpSkuIndex2.class, sku.getId());

            try {

                if (index == null) {
                    if (SkuStatus.OFFSALE.equals(sku.getStatus())) {
                        continue;
                    }
//                    cmpSkuService.createPtmCmpSkuIndexToMysql(sku);
                    logger.debug("create index id = " + sku.getId());
                } else {

                    PtmCmpSkuIndex2Updater updater = new PtmCmpSkuIndex2Updater(sku.getId());

                    updater.getPo().setSourceSid(sku.getSourceSid());

                    updater.getPo().setSkuTitle(sku.getSkuTitle());

                    updater.getPo().setSkuTitleIndex(HexDigestUtil.md5(StringUtils.getCleanChars(sku.getSkuTitle())));

                    updater.getPo().setUpdateTime(TimeUtils.nowDate());

                    dbm.update(updater);

                    logger.debug("update id = [" + sku.getId() + "],sourceSid = [" + sku.getSourceSid() + "],skutitle = [" + sku.getSkuTitle() + "],website = [" + sku.getWebsite());
                }

                PtmCmpSkuUpdater updater = new PtmCmpSkuUpdater(sku.getId());

                updater.getPo().setIndexNeed(IndexNeed.NO);

                dbm.update(updater);

            } catch (Exception e) {
                logger.debug(e.toString());
            }
        }
    }
}
