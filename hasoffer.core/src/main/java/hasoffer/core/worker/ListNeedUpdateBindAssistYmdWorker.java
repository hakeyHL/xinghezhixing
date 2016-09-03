package hasoffer.core.worker;

import hasoffer.base.model.PageableResult;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.mongo.StatDevice;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/4/22
 * Function :
 */
public class ListNeedUpdateBindAssistYmdWorker implements Runnable {

    private static final String Q_STATS_DEVICE =
            "    SELECT t FROM StatDevice t " +
                    "       WHERE t.bindAssist > 0 " +
                    "     AND t.firstBindAssistYmd IS NULL " +
                    "       AND t.ymd=?0 ";
    //    IDataBaseManager dbm;
    IMongoDbManager mdm;
    ListProcessWorkerStatus ws;
    List<String> ymds;
    private Logger logger = LoggerFactory.getLogger(ListNeedUpdateBindAssistYmdWorker.class);

    public ListNeedUpdateBindAssistYmdWorker(IMongoDbManager mdm, ListProcessWorkerStatus ws, List<String> ymds) {
        this.mdm = mdm;
        this.ws = ws;
        this.ymds = ymds;
    }

    @Override
    public void run() {

        int i = 0;
        String ymd;
        while (i < ymds.size()) {
            if (ws.getSdQueue().size() > 0) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    break;
                }
                continue;
            }
            ymd = ymds.get(i);
            list(ymd);
            i++;
        }

        ws.setListWorkFinished(true);
    }

    private void list(String ymd) {
        logger.debug(ymd);
        int page = 1, PAGE_SIZE = 2000;
        PageableResult<StatDevice> pagedDeviceStats = queryPage(ymd, 1, PAGE_SIZE);

        long TOTAL_PAGE = pagedDeviceStats.getTotalPage();

        while (page <= TOTAL_PAGE) {
            if (ws.getSdQueue().size() > 1000) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                }
                continue;
            }
            logger.debug("page = " + page);

            if (page > 1) {
                pagedDeviceStats = queryPage(ymd, page, PAGE_SIZE);//dbm.queryPage(Q_STATS_DEVICE, 1, PAGE_SIZE, Arrays.asList(ymd));
            }

            List<StatDevice> deviceStats = pagedDeviceStats.getData();

            ws.getSdQueue().addAll(deviceStats);

            page++;
        }
    }

    private PageableResult<StatDevice> queryPage(String ymd, int page, int size) {
        Query query = Query.query(Criteria.where("ymd").is(ymd).andOperator(Criteria.where("bindAssist").gt(0)));

        return mdm.queryPage(StatDevice.class, query, page, size);
    }

}
