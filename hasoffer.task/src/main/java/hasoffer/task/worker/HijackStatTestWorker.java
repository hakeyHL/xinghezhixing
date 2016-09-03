package hasoffer.task.worker;

import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.exception.CmpSkuUrlNotFoundException;
import hasoffer.core.exception.MultiUrlException;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.enums.IndexStat;
import hasoffer.core.persistence.mongo.StatHijackFetch;
import hasoffer.core.persistence.po.ptm.PtmCmpSkuIndex2;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.core.product.ICmpSkuService;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/5/13.
 */
public class HijackStatTestWorker implements Runnable {

    private ConcurrentLinkedQueue<SrmSearchLog> queue;
    private ICmpSkuService cmpSkuService;
    private IMongoDbManager mdm;

    public HijackStatTestWorker(ConcurrentLinkedQueue<SrmSearchLog> queue, ICmpSkuService cmpSkuService, IMongoDbManager mdm) {
        this.queue = queue;
        this.cmpSkuService = cmpSkuService;
        this.mdm = mdm;
    }


    @Override
    public void run() {

        while (true) {

            SrmSearchLog log = queue.poll();

            if (log == null) {
                try {
                    TimeUnit.MINUTES.sleep(1);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String cliQ = log.getKeyword();
            String sourceId = log.getSourceId();
            String site = log.getSite();
            Website website = Website.valueOf(site);

            if (!website.equals(Website.FLIPKART) && !website.equals(Website.SNAPDEAL) && !website.equals(Website.SHOPCLUES)) {
                continue;
            }

            PtmCmpSkuIndex2 cmpSkuIndex2 = null;
            StatHijackFetch statHijackFetch = null;

            try {
//                此处屏蔽掉历史数据，要求每天只要被更新了的，都需要进行一次测试
//                StatHijackFetch hijackFetch = mdm.queryOne(StatHijackFetch.class, log.getId());
//                if (hijackFetch != null) {
//                    continue;
//                }

                cmpSkuIndex2 = cmpSkuService.getCmpSkuIndex2(website, sourceId, cliQ);

                if (cmpSkuIndex2 == null) {//no index需要抓取
                    statHijackFetch = new StatHijackFetch(log.getId(), Website.valueOf(log.getSite()), log.getSourceId(), log.getKeyword(), TimeUtils.nowDate(), TimeUtils.now(), IndexStat.NO_INDEX, null);
                } else {
                    statHijackFetch = new StatHijackFetch(log.getId(), Website.valueOf(log.getSite()), log.getSourceId(), log.getKeyword(), TimeUtils.nowDate(), TimeUtils.now(), IndexStat.SUCCESS, null);
                }

            } catch (CmpSkuUrlNotFoundException e1) {
                //url为空
                statHijackFetch = new StatHijackFetch(log.getId(), Website.valueOf(log.getSite()), log.getSourceId(), log.getKeyword(), TimeUtils.nowDate(), TimeUtils.now(), IndexStat.NULL_URL, null);

            } catch (MultiUrlException e2) {
                //different url
                statHijackFetch = new StatHijackFetch(log.getId(), Website.valueOf(log.getSite()), log.getSourceId(), log.getKeyword(), TimeUtils.nowDate(), TimeUtils.now(), IndexStat.DIFFERENT_URL, null);

            }

            mdm.save(statHijackFetch);
        }
    }
}
