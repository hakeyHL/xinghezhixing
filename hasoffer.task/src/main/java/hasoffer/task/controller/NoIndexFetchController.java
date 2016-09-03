package hasoffer.task.controller;

import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.task.worker.HijackStatTestWorker;
import hasoffer.task.worker.SrmSearchLogListWorker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created on 2016/5/13.
 */
@Controller
@RequestMapping(value = "/fetchtask")
public class NoIndexFetchController {

    private static AtomicBoolean taskRunning1 = new AtomicBoolean(false);

    @Resource
    IMongoDbManager mdm;
    @Resource
    IDataBaseManager dbm;
    @Resource
    ICmpSkuService cmpSkuService;

    //fetchtask/fetchnoindextitle
    @RequestMapping(value = "/fetchnoindextitle", method = RequestMethod.GET)
    @ResponseBody
    public String fetchNoIndexTitle() {

        if (taskRunning1.get()) {
            return "task running.";
        }

        ExecutorService es = Executors.newCachedThreadPool();

        ConcurrentLinkedQueue<SrmSearchLog> queue = new ConcurrentLinkedQueue<SrmSearchLog>();

        es.execute(new SrmSearchLogListWorker(dbm, queue));

        for (int i = 0; i < 10; i++) {
            es.execute(new HijackStatTestWorker(queue, cmpSkuService, mdm));
        }

        taskRunning1.set(true);

        return "ok";
    }

}
