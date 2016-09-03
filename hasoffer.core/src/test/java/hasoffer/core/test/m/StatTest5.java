package hasoffer.core.test.m;

import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.UrmDeviceBuyLog;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.user.IDeviceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Date : 2016/3/30
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class StatTest5 {


    @Resource
    IDeviceService deviceService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;
    @Resource
    ICmpSkuService cmpSkuService;

//    Website[] websites = {Website.EBAY, Website.SHOPCLUES, Website.INDIATIMES, Website.INFIBEAM, Website.PAYTM};

    Website[] websites = {Website.SNAPDEAL};

    @Test
    public void testStat() {

        Date sdate = TimeUtils.stringToDate("2016-04-12", "yyyy-MM-dd");
        Date edate = TimeUtils.stringToDate("2016-04-13", "yyyy-MM-dd");

        getUrls(sdate, edate);
    }

    private void getUrls(Date sdate, Date edate) {
        Map<String, Long> urlCountMap = new LinkedHashMap<String, Long>();

        int shanchuan = 0;

        for (Website website : websites) {
            PageableResult<UrmDeviceBuyLog> pagedBuyLogs = deviceService.findUrmDeviceBuyLog("", website.name(), sdate, edate, 1, 1000, "", 1);

            List<UrmDeviceBuyLog> buyLogs = pagedBuyLogs.getData();

            for (UrmDeviceBuyLog buyLog : buyLogs) {
                String deviceId = buyLog.getDeviceId();

                UrmDevice device = deviceService.findDevice(deviceId);

                if (device.getMarketChannel() == MarketChannel.SHANCHUAN) {
                    shanchuan++;
                }
            }
        }
        System.out.println(shanchuan);
    }
}

