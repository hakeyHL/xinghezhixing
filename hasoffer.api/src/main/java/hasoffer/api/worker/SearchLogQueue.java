package hasoffer.api.worker;

import hasoffer.core.bo.system.SearchLogBo;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Date : 2016/1/14
 * Function :
 */
public class SearchLogQueue {

    private static SearchLogQueue queue = new SearchLogQueue();
    private LinkedBlockingQueue<SearchLogBo> searchLogBos;

    private SearchLogQueue() {
        searchLogBos = new LinkedBlockingQueue<SearchLogBo>();
    }

    public static void addLog(SearchLogBo searchLogBo) {
        queue.searchLogBos.add(searchLogBo);
    }

    public static SearchLogBo get() {
        return queue.searchLogBos.poll();
    }

    public static Object getCount() {
        return queue.searchLogBos.size();
    }
}
