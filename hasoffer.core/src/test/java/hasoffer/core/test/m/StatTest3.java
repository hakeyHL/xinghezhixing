package hasoffer.core.test.m;

import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date : 2016/3/30
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class StatTest3 {


    @Resource
    IDeviceService deviceService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;
    @Resource
    ICmpSkuService cmpSkuService;

    Website[] websites = {Website.EBAY, Website.SHOPCLUES, Website.INDIATIMES, Website.INFIBEAM, Website.PAYTM};

    @Test
    public void testStat() {

        Date sdate = TimeUtils.stringToDate("2016-04-04", "yyyy-MM-dd");
        Date edate = TimeUtils.stringToDate("2016-04-05", "yyyy-MM-dd");

        Set<String> deviceIdSet = new HashSet<String>();

        getDevcieIdSet(deviceIdSet, sdate, edate);

        System.out.println(deviceIdSet.size());
    }

    private void getDevcieIdSet(Set<String> deviceIdSet, Date sdate, Date edate) {
        int page = 1;
        int PAGE_SIZE = 1000;

        PageableResult<UrmDeviceRequestLog> pagedLogs = deviceService.findDeviceLogsByRequestUri("/cmp/getcmpskus", sdate, edate, page, PAGE_SIZE);

        List<UrmDeviceRequestLog> logs = pagedLogs.getData();

        long total_page = pagedLogs.getTotalPage();

        while (page <= total_page) {
            System.out.println("page : " + page + "/" + total_page);
            if (page > 1) {
                pagedLogs = deviceService.findDeviceLogsByRequestUri("/cmp/getcmpskus", sdate, edate, page, PAGE_SIZE);
                logs = pagedLogs.getData();
            }

            for (UrmDeviceRequestLog log : logs) {
                String deviceId = log.getDeviceId();

                if (deviceIdSet.contains(deviceId)) {
                    continue;
                }

                UrmDevice device = deviceService.findDevice(deviceId);
                if (device.getMarketChannel() == MarketChannel.SHANCHUAN) {
                    deviceIdSet.add(deviceId);
                }

            }

            page++;
        }
    }

}

