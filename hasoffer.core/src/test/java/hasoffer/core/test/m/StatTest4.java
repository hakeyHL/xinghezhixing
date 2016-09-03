package hasoffer.core.test.m;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date : 2016/3/30
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class StatTest4 {


    @Resource
    IDeviceService deviceService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;
    @Resource
    ICmpSkuService cmpSkuService;

    //    Website[] websites = {Website.EBAY, Website.SHOPCLUES, Website.INDIATIMES, Website.INFIBEAM, Website.PAYTM};
    Website[] websites = {Website.FLIPKART};

    @Test
    public void testStat() {

        Date sdate = TimeUtils.stringToDate("2016-04-07", "yyyy-MM-dd");
        Date edate = TimeUtils.stringToDate("2016-04-08", "yyyy-MM-dd");

        PageableResult<UrmDeviceBuyLog> pagedBuyLogs = deviceService.findUrmDeviceBuyLog(null, Website.SNAPDEAL.name(), sdate, edate, 1, 1000, "", 1);

        Map<String, Integer> deviceMap = new HashMap<String, Integer>();
        Map<Long, Integer> proMap = new HashMap<Long, Integer>();

        List<UrmDeviceBuyLog> buyLogs = pagedBuyLogs.getData();
        for (UrmDeviceBuyLog buyLog : buyLogs) {
            String deviceId = buyLog.getDeviceId();
            long proId = buyLog.getPtmProductId();

            if (proId == 124081) {
                System.out.println(deviceId);
                continue;
            }

            if (deviceMap.containsKey(deviceId)) {
                deviceMap.put(deviceId, deviceMap.get(deviceId) + 1);
            } else {
                deviceMap.put(deviceId, 1);
            }

            if (proMap.containsKey(proId)) {
                proMap.put(proId, proMap.get(proId) + 1);
            } else {
                proMap.put(proId, 1);
            }
        }

        System.out.println(buyLogs.size());
        for (Map.Entry<String, Integer> kv : deviceMap.entrySet()) {
            String deviceId = kv.getKey();
            UrmDevice device = deviceService.findDevice(deviceId);
            System.out.println(deviceId + "\t" + kv.getValue() + "\t" + device.getDeviceName() + "\t" + device.getShopApp() + "\t" + device.getMarketChannel() + "\t" + device.getAppVersion());
        }


        /*for (Map.Entry<Long, Integer> kv : proMap.entrySet()) {
            long proId = kv.getKey();

            PtmCmpSku cmpSku = cmpSkuService.getCmpSku(proId, Website.SNAPDEAL);

            System.out.println(kv.getValue() + "\t" + proId + "\t" + cmpSku.getTitle() + "\t" + WebsiteHelper.getUrlWithAff(Website.SNAPDEAL, cmpSku.getUrl(), cmpSku.getId()));
        }*/
    }

}

