package hasoffer.task.controller;

import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmTopSelling;
import hasoffer.core.product.IProductService;
import hasoffer.core.redis.ICacheService;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import hasoffer.task.worker.MysqlListWorker;
import hasoffer.task.worker.TopSellingPriceUpdateWorker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created on 2016/7/19.
 */
@Controller
@RequestMapping(value = "/updatetopselling")
public class UpdateTopSellingPriceController {

    private static AtomicBoolean taskRunning1 = new AtomicBoolean(false);

    @Resource
    IProductService productService;
    @Resource
    ICacheService cacheService;
    @Resource
    IDataBaseManager dbm;

    //updatetopselling/start
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    @ResponseBody
    public String start() {

        if (taskRunning1.get()) {
            return "task running.";
        }

        String queryString = "SELECT t FROM PtmTopSelling t ORDER BY t.count DESC";

        ExecutorService es = Executors.newCachedThreadPool();

        ListProcessWorkerStatus<PtmTopSelling> ws = new ListProcessWorkerStatus<PtmTopSelling>();

        es.execute(new MysqlListWorker<PtmTopSelling>(queryString, ws, dbm));

        for (int i = 0; i < 2; i++) {
            es.execute(new TopSellingPriceUpdateWorker(ws, productService, cacheService));
        }

        taskRunning1.set(true);

        return "ok";
    }

}
