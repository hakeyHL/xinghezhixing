package hasoffer.task.worker;

import hasoffer.core.persistence.mongo.SrmAutoSearchResult;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.core.search.ISearchService;
import hasoffer.core.search.SearchProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/3/14
 * Function :
 */
public class UnmatchedSearchRecordProcessWorker implements Runnable {

    LinkedBlockingQueue<SrmSearchLog> searchLogQueue;
    ISearchService searchService;
    SearchProductService searchProductService;
    private Logger logger = LoggerFactory.getLogger(UnmatchedSearchRecordProcessWorker.class);

    public UnmatchedSearchRecordProcessWorker(SearchProductService searchProductService, ISearchService searchService,
                                              LinkedBlockingQueue<SrmSearchLog> searchLogQueue) {
        this.searchProductService = searchProductService;
        this.searchService = searchService;
        this.searchLogQueue = searchLogQueue;
    }

    public void run() {
        logger.debug("UnmatchedSearchRecordProcessWorker START");

        while (true) {

            try {

                SrmSearchLog searchLog = searchLogQueue.poll();

                if (searchLog == null) {
                    TimeUnit.SECONDS.sleep(5);
                    logger.debug("UnmatchedSearchRecordProcessWorker . search-log-queue job has no jobs. go to sleep!");
                    continue;
                }

                SrmAutoSearchResult autoSearchResult = new SrmAutoSearchResult(searchLog);

                // fetch
                searchProductService.saveSearchProducts(autoSearchResult);

                /*// clean
                searchProductService.analysisProducts(autoSearchResult);

                // relate
                searchService.relateUnmatchedSearchLogx(autoSearchResult);*/

            } catch (Exception e) {
                logger.debug(e.getMessage());
            }
        }
    }

}
