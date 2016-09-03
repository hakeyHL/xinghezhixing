package hasoffer.task.worker;

import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.enums.SearchPrecise;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.core.search.ISearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/3/14
 * Function : 旧数据修复
 */
public class UnmatchedSearchRecordListWorker implements Runnable {
    private static final String SQL_SEARCHLOG = "select t from SrmSearchLog t where t.updateTime >?0 order by t.updateTime ASC ";
    LinkedBlockingQueue<SrmSearchLog> searchLogQueue;
    ISearchService searchService;
    IDataBaseManager dbm;
    private Logger logger = LoggerFactory.getLogger(UnmatchedSearchRecordListWorker.class);

    public UnmatchedSearchRecordListWorker(ISearchService searchService, IDataBaseManager dbm,
                                           LinkedBlockingQueue<SrmSearchLog> searchLogQueue) {
        this.dbm = dbm;
        this.searchService = searchService;
        this.searchLogQueue = searchLogQueue;
    }

    public void run() {
        String PATTERN_TIME = "yyyy-MM-dd HH:mm:ss";
        Date startTime = new Date(TimeUtils.now() - TimeUtils.MILLISECONDS_OF_1_HOUR);
        Date searchTime = startTime;

        while (true) {
            logger.debug("UnmatchedSearchRecordListWorker START{}. Queue size{}", TimeUtils.parse(startTime, PATTERN_TIME), searchLogQueue.size());
            try {
                if (searchLogQueue.size() > 800) {
                    TimeUnit.SECONDS.sleep(10);
                    logger.debug("UnmatchedSearchRecordListWorker go to sleep!");
                    continue;
                }
                searchTime = startTime;
                PageableResult<SrmSearchLog> pagedSearchLog = dbm.queryPage(SQL_SEARCHLOG, 1, 1000, Arrays.asList(searchTime));
                List<SrmSearchLog> searchLogs = pagedSearchLog.getData();

                if (ArrayUtils.hasObjs(searchLogs)) {
                    for (SrmSearchLog searchLog : searchLogs) {

                        // 是否处理这个搜索日志
                        if (ifIgnore(searchLog)) {
                            continue;
                        }

                        if (searchLog.getPtmProductId() > 15 * 10000 && searchLog.getPtmProductId() <= 80 * 10000) {
                            searchLog.setPtmProductId(0);
                        }

                        searchLogQueue.add(searchLog);

                        startTime = searchLog.getUpdateTime();
                    }
                }

                if (startTime.compareTo(searchTime) == 0) {
                    TimeUtils.add(startTime, 1000);
                }

            } catch (Exception e) {
                logger.error(e.getMessage());
            }

        }
    }

    // 是否忽略不处理
    private boolean ifIgnore(SrmSearchLog searchLog) {

        if (searchLog.getPrecise() == SearchPrecise.MANUALSET) {
            return true;
        }

        if (Website.FLIPKART.name().equals(searchLog.getSite())
                || Website.SNAPDEAL.name().equals(searchLog.getSite())) {
            // 目前仅处理这两个网站：FLIPKART & SNAPDEAL
            return false;
        } else {
            return true;
        }

    }
}
