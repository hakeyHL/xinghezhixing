package hasoffer.core.test.m;

import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.UrmDeviceRequestLog;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import hasoffer.core.user.IDeviceService;
import hasoffer.core.worker.ListRequestLogsWorker;
import hasoffer.core.worker.StatDeviceWorker;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/3/30
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class StatTest {

    private static final String Q_DEVICE_APP =
            "SELECT t FROM UrmDevice t " +
                    " WHERE t.appType = 'APP' " +
                    "   AND t.shopApp is not null " +
                    "   AND t.shopApp <> '' " +
                    "   AND t.createTime >= ?0 " +
                    "   AND t.createTime < ?1  ";
    private static final String Q_DEVICE_SDK =
            "SELECT t FROM UrmDevice t " +
                    " WHERE t.appType = 'SDK' " +
                    "   AND t.marketChannel = ?0 " +
//                    "   AND (t.appVersion = 219 or t.appVersion = 220) " +
                    "   AND t.shopApp is not null " +
                    "   AND t.shopApp <> '' " +
                    "   AND t.createTime >= ?1 " +
                    "   AND t.createTime < ?2  ";
    @Resource
    IDeviceService deviceService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;
    private Logger logger = LoggerFactory.logger(StatTest.class);

    @Test
    public void testStatBylogs(){

        List<String> ymds = new ArrayList<String>();
        TimeUtils.fillDays(ymds, "20160513", "20160515", TimeUtils.PATTERN_YMD);

        for (String ymd : ymds) {
            logger.debug(ymd);
            predeviceByLog(ymd, Arrays.asList(ymd, ymd));
        }
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

    @Test
    public void testStat() {

        final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

        Date startDate = TimeUtils.stringToDate("2016-04-10 00:00:00", TIME_PATTERN);
        Date endDate = TimeUtils.stringToDate("2016-04-11 00:00:00", TIME_PATTERN);

        Date lStartDate = TimeUtils.stringToDate("2016-04-10 00:00:00", TIME_PATTERN);
        Date lEndDate = TimeUtils.stringToDate("2016-04-15 00:00:00", TIME_PATTERN);

//        Date l2StartDate = TimeUtils.stringToDate("2016-03-29 00:00:00", TIME_PATTERN);
//        Date l2EndDate = TimeUtils.stringToDate("2016-03-31 00:00:00", TIME_PATTERN);

        int page = 1;
        int size = Integer.MAX_VALUE;

        // list device by date
//        List<UrmDevice> devices = dbm.query(Q_DEVICE_APP, page, size, Arrays.asList(startDate, endDate));
        List<UrmDevice> devices = dbm.query(Q_DEVICE_SDK, page, size, Arrays.asList(MarketChannel.SHANCHUAN, startDate, endDate));

        Date start = startDate;
        while (start.getTime() < lEndDate.getTime()) {
            Date end = new Date(start.getTime() + TimeUtils.MILLISECONDS_OF_1_DAY);
            statDevices(devices, start, end);

            start = end;
        }

    }

    private void statDevices(List<UrmDevice> devices, Date lStartDate, Date lEndDate) {
        System.out.println(TimeUtils.timeAsString(lStartDate) + "\t" + TimeUtils.timeAsString(lEndDate));

        StatSets ss1 = new StatSets();
        StatSets ss2 = new StatSets();

        Map<String, Integer> countMap = new HashMap<String, Integer>();

        for (UrmDevice device : devices) {
            statDevice(ss1, device, lStartDate, lEndDate);
//            statDevice(ss2, device, l2StartDate, l2EndDate);
//            statDevice2(ss1, countMap, device, lStartDate, lEndDate);
        }

        // show result
        System.out.println("----------------------");
        ss1.showCount();
//        System.out.println("----------------------");
//        ss2.showCount();
//        System.out.println("----------------------");

        /*for (String s : ss2.getSetAmas()) {
            *//*if (!ss1.getSetAmas().contains(s)) {
                System.out.println(s);
            }
            System.out.println("----------------------");*//*
            if (ss1.getSetAmas().contains(s)) {
                System.out.println(s);
            }
        }*/
//        System.out.println("----------------------");
//        int c = 0;
//        for (Map.Entry<String, Integer> kv : countMap.entrySet()){
//            if (kv.getValue() >= 2){
//                c ++;
//                System.out.println(kv.getKey() + "\t" + kv.getValue());
//            }
//        }
//        System.out.println(c);
    }

    private void statDevice(StatSets ss, UrmDevice device, Date startTime, Date endTime) {
        String deviceId = device.getId();
        PageableResult<UrmDeviceRequestLog> pagedLogs = deviceService.findDeviceLogs(deviceId, startTime, endTime, 1, Integer.MAX_VALUE);

        List<UrmDeviceRequestLog> logs = pagedLogs.getData();

        ss.getSetAll().add(deviceId);

        String shopApp = device.getShopApp();
        if (!StringUtils.isEmpty(shopApp)) {
            ss.getSetWithShop().add(deviceId);
        }

        for (UrmDeviceRequestLog log : logs) {

            String requestUri = log.getRequestUri();
            String queryStr = log.getQuery();

            if (requestUri.equals("/cmp/getcmpskus")) {
                ss.getSetCmp().add(deviceId);
            } else if (requestUri.equals("/app/dot")) {

                if (queryStr.contains("wakeUp")) {
                    ss.getSetWake().add(deviceId);
                } else if (queryStr.contains("load")) {
                    ss.getSetLoad().add(deviceId);
                } else if (queryStr.contains("splash")) {
                    ss.getSetSplash().add(deviceId);
                } else if (queryStr.contains("visit1")) {
                    ss.getSetV1().add(deviceId);
                } else if (queryStr.contains("visit2")) {
                    ss.getSetV2().add(deviceId);
                } else if (queryStr.contains("visit3")) {
                    ss.getSetV3().add(deviceId);
                } else if (queryStr.contains("visit4")) {
                    ss.getSetV4().add(deviceId);
                } else if (queryStr.contains("autoModifyAccessSuccess")) {
                    ss.getSetAmas().add(deviceId);
                } else if (queryStr.contains("mainPage")) {
                    ss.getSetMainPage().add(deviceId);
                } else if (queryStr.contains("shop")) {
                    ss.getSetBuy().add(deviceId);
                }

            } else if (requestUri.equals("/app/latest")) {
                ss.getSetLatest().add(deviceId);
            }

        }
    }

    private void statDevice2(StatSets ss, Map<String, Integer> countMap, UrmDevice device, Date startTime, Date endTime) {
        String deviceId = device.getId();
        PageableResult<UrmDeviceRequestLog> pagedLogs = deviceService.findDeviceLogs(deviceId, startTime, endTime, 1, Integer.MAX_VALUE);

        List<UrmDeviceRequestLog> logs = pagedLogs.getData();

        ss.getSetAll().add(deviceId);

        String shopApp = device.getShopApp();
        if (!StringUtils.isEmpty(shopApp)) {
            ss.getSetWithShop().add(deviceId);
        }

        for (UrmDeviceRequestLog log : logs) {

            String requestUri = log.getRequestUri();
            String queryStr = log.getQuery();

            if (requestUri.equals("/cmp/getcmpskus")) {
                ss.getSetCmp().add(deviceId);
            } else if (requestUri.equals("/app/dot")) {

                if (queryStr.contains("wakeUp")) {
                    if (countMap.containsKey(deviceId)) {
                        countMap.put(deviceId, countMap.get(deviceId) + 1);
                    } else {
                        countMap.put(deviceId, 1);
                    }
                    ss.getSetWake().add(deviceId);
                } else if (queryStr.contains("load")) {
                    ss.getSetLoad().add(deviceId);
                } else if (queryStr.contains("splash")) {
                    ss.getSetSplash().add(deviceId);
                } else if (queryStr.contains("visit1")) {
                    ss.getSetV1().add(deviceId);
                } else if (queryStr.contains("visit2")) {
                    ss.getSetV2().add(deviceId);
                } else if (queryStr.contains("visit3")) {
                    ss.getSetV3().add(deviceId);
                } else if (queryStr.contains("visit4")) {
                    ss.getSetV4().add(deviceId);
                } else if (queryStr.contains("autoModifyAccessSuccess")) {
                    ss.getSetAmas().add(deviceId);
                } else if (queryStr.contains("mainPage")) {
                    ss.getSetMainPage().add(deviceId);
                } else if (queryStr.contains("shop")) {
                    ss.getSetBuy().add(deviceId);
                }

            } else if (requestUri.equals("/app/latest")) {
                ss.getSetLatest().add(deviceId);
            }

        }
    }
}
