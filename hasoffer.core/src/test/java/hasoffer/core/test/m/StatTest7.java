package hasoffer.core.test.m;

import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.UrmDeviceRequestLog;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.user.IDeviceService;
import jodd.io.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Date : 2016/3/30
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class StatTest7 {

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
//                    "   AND t.marketChannel = ?0 " +
//                    "   AND (t.appVersion = 219 or t.appVersion = 220) " +
                    "   AND t.shopApp is not null " +
                    "   AND t.shopApp <> '' " +
                    "   AND t.createTime >= ?0 " +
                    "   AND t.createTime < ?1  ";
    private static final String Q_DEVICE =
            "SELECT t FROM UrmDevice t " +
//                    "   AND t.marketChannel = ?0 " +
//                    "   AND (t.appVersion = 219 or t.appVersion = 220) " +
                    "   WHERE t.shopApp is not null " +
                    "   AND t.shopApp <> '' " +
                    "   AND t.createTime >= ?0 " +
                    "   AND t.createTime < ?1  ";


    @Resource
    IDeviceService deviceService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;


    /**
     * 统计有wakeup大于2次，但是没有比价的,有电商app的手机型号
     *
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {

        File file = new File("C:/Users/wing/Desktop/errorPhoneName.txt");

        String header = "手机型号\n";

        FileUtil.appendString(file, header);

        final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

        Date startDate = TimeUtils.stringToDate("2016-04-01 00:00:00", TIME_PATTERN);
        Date endDate = TimeUtils.stringToDate("2016-04-15 00:00:00", TIME_PATTERN);

        while (startDate.getTime() < endDate.getTime()) {
            Set<String> phoneNameSets = getInfo1(startDate);

            if (phoneNameSets != null && phoneNameSets.size() != 0) {

                StringBuilder stringBuilder = new StringBuilder();

                for (String set : phoneNameSets) {

                    String phoneName = set.toString();
                    stringBuilder.append(phoneName + "\n");
                }

                FileUtil.appendString(file, stringBuilder.toString());

            }

            startDate = TimeUtils.addDay(startDate, 1);
        }

    }

    //统计sdk的
    @Test
    public void test2() throws IOException {

        File file = new File("C:/Users/wing/Desktop/统计sdk.txt");

        String header = "日期\t新设备数\t新开启辅助功能设备数\t次日辅助功能仍开启设备数\t七日辅助功能仍开启设备数\n";

        FileUtil.appendString(file, header);

        final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

        Date startDate = TimeUtils.stringToDate("2016-04-03 00:00:00", TIME_PATTERN);
        Date endDate = TimeUtils.stringToDate("2016-04-12 00:00:00", TIME_PATTERN);

        while (startDate.getTime() < endDate.getTime()) {
            String string = getInfo(startDate);

            FileUtil.appendString(file, string);

            startDate = TimeUtils.addDay(startDate, 1);
        }
    }

    @Test
    public void test3() throws IOException {

        File file = new File("d:/TMP/111.txt");

        String header = "日期\t新设备数\t新开启辅助功能设备数\t次日辅助功能仍开启设备数\t七日辅助功能仍开启设备数\n";

        FileUtil.appendString(file, header);

        final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

        Date startDate = TimeUtils.stringToDate("2016-04-01 00:00:00", TIME_PATTERN);
        Date endDate = TimeUtils.stringToDate("2016-04-12 00:00:00", TIME_PATTERN);

        while (startDate.getTime() < endDate.getTime()) {
            String string = getInfo2(startDate);

            FileUtil.appendString(file, string);

            startDate = TimeUtils.addDay(startDate, 1);
        }
    }

    private String getInfo2(Date startDate) {
        StringBuilder stringBuilder = new StringBuilder();

        int page = 1;
        int size = Integer.MAX_VALUE;

        //新设备集合
        PageableResult<UrmDevice> pageableResult = dbm.queryPage(Q_DEVICE_APP, page, size, Arrays.asList(startDate, TimeUtils.addDay(startDate, 1)));

        //当日新增设备数
        long todayNewDevice = pageableResult.getNumFund();

        List<UrmDevice> devices = pageableResult.getData();

        int todayOpenAccessSuccess = 0;
        int nextDayStillOpen = 0;
        int nextWeekStillOpen = 0;

        for (UrmDevice device : devices) {

            String deviceId = device.getId();

            if (getOpenAccessSuccess(startDate, TimeUtils.addDay(startDate, 1), deviceId)) {
                todayOpenAccessSuccess++;
            }
            if (getOpenAccessSuccess(TimeUtils.addDay(startDate, 1), TimeUtils.addDay(startDate, 2), deviceId)) {
                nextDayStillOpen++;
            }
            if (getOpenAccessSuccess(TimeUtils.addDay(startDate, 7), TimeUtils.addDay(startDate, 8), deviceId)) {
                nextWeekStillOpen++;
            }

        }

        String today = new SimpleDateFormat("yyyy-MM-dd").format(startDate);

        stringBuilder.append(today + "\t" + todayNewDevice + "\t" + todayOpenAccessSuccess + "\t" + nextDayStillOpen + "\t" + nextWeekStillOpen + "\n");

        return stringBuilder.toString();
    }


    private String getInfo(Date startDate) {

        StringBuilder stringBuilder = new StringBuilder();

        int page = 1;
        int size = Integer.MAX_VALUE;

        //新设备集合
        PageableResult<UrmDevice> pageableResult = dbm.queryPage(Q_DEVICE_SDK, page, size, Arrays.asList(startDate, TimeUtils.addDay(startDate, 1)));

        //当日新增设备数
        long todayNewDevice = pageableResult.getNumFund();

        List<UrmDevice> devices = pageableResult.getData();

        int todayOpenAccessSuccess = 0;
        int nextDayStillOpen = 0;
        int nextWeekStillOpen = 0;

        for (UrmDevice device : devices) {

            String deviceId = device.getId();

            if (getOpenAccessSuccess(startDate, TimeUtils.addDay(startDate, 1), deviceId)) {
                todayOpenAccessSuccess++;
            }
            if (getOpenAccessSuccess(TimeUtils.addDay(startDate, 1), TimeUtils.addDay(startDate, 2), deviceId)) {
                nextDayStillOpen++;
            }
            if (getOpenAccessSuccess(TimeUtils.addDay(startDate, 7), TimeUtils.addDay(startDate, 8), deviceId)) {
                nextWeekStillOpen++;
            }

        }

        String today = new SimpleDateFormat("yyyy-MM-dd").format(startDate);

        stringBuilder.append(today + "\t" + todayNewDevice + "\t" + todayOpenAccessSuccess + "\t" + nextDayStillOpen + "\t" + nextWeekStillOpen + "\n");

        return stringBuilder.toString();
    }

    private Set<String> getInfo1(Date startDate) {

        int page = 1;
        int size = Integer.MAX_VALUE;

        Set<String> phoneNameSet = getErrorPhoneName(startDate, page, size);

        return phoneNameSet;
    }

    private Set<String> getErrorPhoneName(Date startDate, int page, int size) {
        //新设备集合
        PageableResult<UrmDevice> pageableResult = dbm.queryPage(Q_DEVICE, page, size, Arrays.asList(startDate, TimeUtils.addDay(startDate, 1)));

        List<UrmDevice> devices = pageableResult.getData();

        Set<String> phoneNameSet = new HashSet<String>();

        for (UrmDevice device : devices) {

            String shopApp = device.getShopApp();
            if (StringUtils.isEmpty(shopApp)) {
                continue;
            }

            String deviceId = device.getId();
            PageableResult<UrmDeviceRequestLog> pageableResult1 = deviceService.findDeviceLogs(deviceId, startDate, TimeUtils.addDay(startDate, 1), 1, Integer.MAX_VALUE);

            List<UrmDeviceRequestLog> logs = pageableResult1.getData();

            int wakeupTime = 0;
            int priceListTime = 0;

            for (UrmDeviceRequestLog log : logs) {

                String queryStr = log.getQuery();
                if (StringUtils.isEmpty(queryStr)) {
                    continue;
                }

                if (queryStr.contains("wakeUp")) {
                    wakeupTime++;
                }
                if (queryStr.contains("priceList")) {
                    priceListTime++;
                    break;
                }
            }

            if (priceListTime == 0 && wakeupTime > 2) {
                String phoneName = device.getBrand() + "_" + device.getDeviceName();
                phoneNameSet.add(phoneName);
            }
        }

        return phoneNameSet;
    }


    private boolean getOpenAccessSuccess(Date startDate, Date endDate, String deviceId) {

        PageableResult<UrmDeviceRequestLog> pagedLogs = deviceService.findDeviceLogs(deviceId, startDate, endDate, 1, Integer.MAX_VALUE);

        List<UrmDeviceRequestLog> logs = pagedLogs.getData();

        for (UrmDeviceRequestLog log : logs) {

            String requestUri = log.getRequestUri();
            String queryStr = log.getQuery();

            if (requestUri.equals("/cmp/getcmpskus")) {
                return true;
            } else if (requestUri.equals("/app/dot")) {
                if (queryStr.contains("wakeUp")) {
                    return true;
                } else if (queryStr.contains("autoModifyAccessSuccess")) {
                    return true;
                }
            }
        }
        return false;
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
