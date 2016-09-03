package hasoffer.core.test.m;

import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.UrmDeviceRequestLog;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.user.IDeviceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * Date : 2016/3/30
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class StatDeviceTest2 {


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
    @Resource
    ICmpSkuService cmpSkuService;
    Set<String> allSet = new HashSet<String>();
    Set<String> bindSet = new HashSet<String>();
    Set<String> wakeSet = new HashSet<String>();

    @Test
    public void testStat() {

        Date sdate = TimeUtils.stringToDate("2016-04-01", "yyyy-MM-dd");
        Date edate = TimeUtils.stringToDate("2016-04-15", "yyyy-MM-dd");

        PageableResult<UrmDevice> pagedDevices = dbm.queryPage(Q_DEVICE_SDK, 1, Integer.MAX_VALUE, Arrays.asList(MarketChannel.SHANCHUAN, sdate, edate));

        List<UrmDevice> devices = pagedDevices.getData();

        for (UrmDevice device : devices) {
            getDevcieIdSet(device, sdate, edate);
        }

        System.out.println(allSet.size());
        System.out.println(bindSet.size());
        System.out.println(wakeSet.size());
    }

    private void getDevcieIdSet(UrmDevice device, Date sdate, Date edate) {
        String deviceId = device.getId();

        if (bindSet.contains(deviceId) && wakeSet.contains(deviceId)) {
            return;
        }

        int page = 1;
        int PAGE_SIZE = Integer.MAX_VALUE;

        PageableResult<UrmDeviceRequestLog> pagedLogs = deviceService.findDeviceLogs(deviceId, page, PAGE_SIZE);

        List<UrmDeviceRequestLog> logs = pagedLogs.getData();

        long total_page = pagedLogs.getTotalPage();

        while (page <= total_page) {
            System.out.println("page : " + page + "/" + total_page);
            if (page > 1) {
                pagedLogs = deviceService.findDeviceLogs(device.getId(), page, PAGE_SIZE);
                logs = pagedLogs.getData();
            }

            for (UrmDeviceRequestLog log : logs) {
                String requestUri = log.getRequestUri();
                String queryStr = log.getQuery();

                if (requestUri.equals("/cmp/getcmpskus")) {
                    bindSet.add(deviceId);
                    wakeSet.add(deviceId);
                } else if (requestUri.equals("/app/dot")) {

                    if (queryStr.contains("wakeUp")) {
                        wakeSet.add(deviceId);
                    } else if (queryStr.contains("autoModifyAccessSuccess")) {
                        bindSet.add(deviceId);
                    }

                }
            }

            page++;
        }
    }

}

