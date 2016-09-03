package hasoffer.task.controller;

import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.product.ICategoryService;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import hasoffer.task.worker.FKCateAndParamWorker;
import hasoffer.task.worker.FlipkartBrandModelFetchWorker;
import hasoffer.task.worker.MysqlListWorker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created on 2016/6/20.
 */
@Controller
@RequestMapping(value = "/flipkart")
public class FlipkartCategoryParamController {

    private static final String Q_FLIPKART_CMP = "SELECT t FROM PtmCmpSku t WHERE t.website = 'FLIPKART' AND t.categoryId = 0 ORDER BY t.id";
    private static final String Q_FLIPKART_SKU = "SELECT t FROM PtmCmpSku t WHERE t.website = 'FLIPKART' ORDER BY t.id ASC";
    private static AtomicBoolean taskRunning1 = new AtomicBoolean(false);
    private static AtomicBoolean taskRunning2 = new AtomicBoolean(false);
    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;
    @Resource
    ICategoryService categoryService;
    @Resource
    ICmpSkuService cmpSkuService;

    //flipkart/cateandparam
    @RequestMapping(value = "/cateandparam", method = RequestMethod.GET)
    @ResponseBody
    public String getFKCategoryParam() {

        if (taskRunning1.get()) {
            return "task running.";
        }

        ExecutorService es = Executors.newCachedThreadPool();

        ListProcessWorkerStatus<PtmCmpSku> ws = new ListProcessWorkerStatus<PtmCmpSku>();

        es.execute(new MysqlListWorker(Q_FLIPKART_CMP, ws, dbm));

        for (int i = 0; i < 20; i++) {
            es.execute(new FKCateAndParamWorker(dbm, ws, categoryService, cmpSkuService));// mdm,
        }

        taskRunning1.set(true);

        return "ok";
    }

    //flipkart/fetchBrandAndModel
    @RequestMapping(value = "/fetchBrandAndModel", method = RequestMethod.GET)
    @ResponseBody
    public String fetchBrandAndModel() {

        if (taskRunning2.get()) {
            return "task running.";
        }

        ExecutorService es = Executors.newCachedThreadPool();

        ListProcessWorkerStatus<PtmCmpSku> ws = new ListProcessWorkerStatus<PtmCmpSku>();

        es.execute(new MysqlListWorker(Q_FLIPKART_SKU, ws, dbm));

        for (int i = 0; i < 20; i++) {
            es.execute(new FlipkartBrandModelFetchWorker(cmpSkuService, ws));// mdm,
        }

        taskRunning2.set(true);

        return "ok";
    }
}
