package hasoffer.api.worker;

import hasoffer.core.bo.system.SearchLogBo;
import hasoffer.core.search.ISearchService;

import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/1/14
 * Function :
 */
public class SearchLogSaveWorker implements Runnable {

    private ISearchService searchService;

    public SearchLogSaveWorker(ISearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public void run() {
        int sleep = 3;
        int sleepCount = 0;
        while (true) {
            try {
                SearchLogBo searchLogBo = SearchLogQueue.get();

                if (searchLogBo == null) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                        sleepCount++;
                    } catch (InterruptedException e) {
                    }
                    continue;
                }

                searchService.saveSearchLog(searchLogBo);
            } catch (Exception e) {
                // 忽略异常
            }
        }
    }
}
