package hasoffer.task.controller;

import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
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
 * Created on 2016/7/13.
 */
@Controller
@RequestMapping(value = "/productPriceUpate")
public class PtmProductPriceUpdateController {

    private static AtomicBoolean taskRunning1 = new AtomicBoolean(false);

    @Resource
    IMongoDbManager mdm;

    //todo 这个更新策略一会儿再讨论下吧
    //productPriceUpate/start
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    @ResponseBody
    public String start() {

        if (taskRunning1.get()) {
            return "task running.";
        }

        ExecutorService es = Executors.newCachedThreadPool();

        ConcurrentLinkedQueue<PtmCmpSku> queue = new ConcurrentLinkedQueue<PtmCmpSku>();

        for (int i = 0; i < 10; i++) {

        }

        taskRunning1.set(true);

        return "ok";
    }


}



