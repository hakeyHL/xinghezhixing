package hasoffer.task.controller;

import hasoffer.base.enums.IndexNeed;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.StatHijackFetch;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import hasoffer.task.worker.MongoListStatHijackFetchWorker;
import hasoffer.task.worker.MysqlListWorker2;
import hasoffer.task.worker.SaveOrUpdateIndexWorker;
import hasoffer.task.worker.UpdateFkSdIndexWorker;
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
 * Created on 2016/5/27.
 */
@Controller
@RequestMapping(value = "/updatefksd")
public class FixIndexController {

    //初始状态为false，表示任务没有开始
    private static final AtomicBoolean flag1 = new AtomicBoolean(false);
    private static final AtomicBoolean flag2 = new AtomicBoolean(false);

    private static final String Q_SKU_INDEX_UPDATE = "SELECT t FROM PtmCmpSku t WHERE t.indexNeed = '" + IndexNeed.YES.name() + "'";

    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;
    @Resource
    ICmpSkuService cmpSkuService;

    //updatefksd/skutitle/start
    @RequestMapping(value = "/skutitle/start", method = RequestMethod.GET)
    @ResponseBody
    public String skutitle() {

        if (flag1.get()) {
            return "task running";
        }

        ExecutorService es = Executors.newCachedThreadPool();

        ConcurrentLinkedQueue<StatHijackFetch> queue = new ConcurrentLinkedQueue<StatHijackFetch>();

        es.execute(new MongoListStatHijackFetchWorker(queue, mdm));

        for (int i = 0; i < 15; i++) {
            es.execute(new SaveOrUpdateIndexWorker(queue, cmpSkuService, dbm, mdm));
        }

        flag1.set(true);

        return "ok";
    }

    //updatefksd/index/start
    @RequestMapping(value = "/index/start", method = RequestMethod.GET)
    @ResponseBody
    public String index() {

        if (flag2.get()) {
            return "task running";
        }

        ExecutorService es = Executors.newCachedThreadPool();

        ListProcessWorkerStatus<PtmCmpSku> ws = new ListProcessWorkerStatus<PtmCmpSku>();

        es.execute(new MysqlListWorker2<PtmCmpSku>(Q_SKU_INDEX_UPDATE, ws, dbm));

        for (int i = 0; i < 5; i++) {
            es.execute(new UpdateFkSdIndexWorker(ws, dbm, cmpSkuService));
        }

        flag2.set(true);

        return "ok";
    }
}
