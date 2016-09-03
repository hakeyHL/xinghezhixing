package hasoffer.task.controller;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.mongo.StatDevice;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import hasoffer.core.user.IDeviceService;
import hasoffer.core.worker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/4/22
 * Function : 统计设备日活等数据
 * perdeviceperday - 第一步，按照每设备每天发的请求统计一条数据
 * updatefirstbindassist - 第二步，根据第一步的统计结果，计算设备第一次打开辅助功能的时间
 * statdayalive - 第三步，根据1、2步统计结果，统计整日的各纬度（客户端品牌、渠道、系统版本）统计日活
 * <p>
 * http://192.168.1.202:8021/statdevice/start?start=20160223&end=20160505
 */
@Controller
@RequestMapping(value = "/statdevice")
public class StatDeviceController {

    @Resource
    IDeviceService deviceService;
    @Resource
    IMongoDbManager mdm;
//    IDataBaseManager dbm;

    private Logger logger = LoggerFactory.getLogger(StatDeviceController.class);

    @RequestMapping(value = "/bylogs/{start}/{end}", method = RequestMethod.GET)
    public
    @ResponseBody
    void bylogs(@PathVariable String start,
                @PathVariable String end) {

        logger.debug(start + " - " + end);

        List<String> ymds = new ArrayList<String>();
        TimeUtils.fillDays(ymds, start, end, TimeUtils.PATTERN_YMD);

        for (String ymd : ymds) {
            logger.debug(ymd);
            predeviceByLog(ymd, Arrays.asList(ymd, ymd));
        }
        
    }

    @RequestMapping(value = "/first", method = RequestMethod.GET)
    public
    @ResponseBody
    void first(@RequestParam(defaultValue = "20160223") String start,
               @RequestParam(defaultValue = "20160512") String end) {

        perdeviceperday(start, end);

//        updatebinddate(start, end);
    }

    @RequestMapping(value = "/second", method = RequestMethod.GET)
    public
    @ResponseBody
    void second(@RequestParam(defaultValue = "20160223") String start,
                @RequestParam(defaultValue = "20160512") String end) {

        logger.debug("update bind date from " + start + " to " + end);

        updatebinddate(start, end);
    }

    private void perdeviceperday(String start, String end) {
        List<String> ymds = new ArrayList<String>();
        TimeUtils.fillDays(ymds, start, end, TimeUtils.PATTERN_YMD);

        ListProcessWorkerStatus<UrmDevice> ws = new ListProcessWorkerStatus();

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(new ListDeviceWorker(deviceService, ws, ymds));
        for (int i = 0; i < 50; i++) {
            es.execute(new StatDeviceWorker(deviceService, ws, ymds));
        }

        while (true) {
            if (ws.getSdQueue().size() == 0 && ws.isListWorkFinished()) {
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
            }
            logger.debug("queue size : " + ws.getSdQueue().size());
        }
        logger.debug("stat work finished.");
    }

    private void updatebinddate(String start, String end) {
        List<String> ymds = new ArrayList<String>();
        TimeUtils.fillDays(ymds, start, end, TimeUtils.PATTERN_YMD);

        ListProcessWorkerStatus<StatDevice> ws = new ListProcessWorkerStatus<StatDevice>();
        ExecutorService es = Executors.newCachedThreadPool();

        es.execute(new ListNeedUpdateBindAssistYmdWorker(mdm, ws, ymds));
        for (int i = 0; i < 50; i++) {
            es.execute(new UpdateBindAssistYmdWorker(deviceService, ws));
        }

        while (true) {
            if (ws.isListWorkFinished() && ws.getSdQueue().size() == 0) {
                break;
            }

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
            }
            logger.debug("queue size : " + ws.getSdQueue().size());
        }

        logger.debug("update bind work finished.");
    }

    private void predeviceByLog(String logYmd, List<String> ymds) {
        ListProcessWorkerStatus<UrmDevice> ws = new ListProcessWorkerStatus();

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(new ListRequestLogsWorker(deviceService, ws, logYmd));
        for (int i = 0; i < 50; i++) {
            es.execute(new StatDeviceWorker(deviceService, ws, ymds));
        }

        while (true) {
            if (ws.getSdQueue().size() == 0 && ws.isListWorkFinished()) {
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
            }
            logger.debug("queue size : " + ws.getSdQueue().size());
        }
        logger.debug("stat work finished.");
    }
}
