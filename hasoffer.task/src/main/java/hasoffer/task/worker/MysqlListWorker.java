package hasoffer.task.worker;

import hasoffer.base.model.PageableResult;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/5/3.
 */
public class MysqlListWorker<T> implements Runnable {

    private Logger logger = LoggerFactory.getLogger(MysqlListWorker.class);
    private String queryString;
    private IDataBaseManager dbm;
    private ListProcessWorkerStatus<T> ws;

    public MysqlListWorker(String queryString, ListProcessWorkerStatus<T> ws, IDataBaseManager dbm) {
        this.queryString = queryString;
        this.ws = ws;
        this.dbm = dbm;
    }

    @Override
    public void run() {

        int page = 1;
        int pageSize = 1000;

        PageableResult<T> pagedResults = dbm.queryPage(queryString, page, pageSize);
        List<T> cmpSkus = pagedResults.getData();

        long pageTotal = pagedResults.getTotalPage();
        while (page <= pageTotal) {
            if (ws.getSdQueue().size() > 2000) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    break;
                }
                continue;
            }

            logger.debug(String.format("PAGE [%d/%d].", page, pageTotal));

            if (page > 1) {
                cmpSkus = dbm.query(queryString, page, pageSize);
            }

            ws.getSdQueue().addAll(cmpSkus);

            page++;
//            for test
//            break;
        }

        ws.setListWorkFinished(true);

        logger.debug("work finished.");
    }

}
