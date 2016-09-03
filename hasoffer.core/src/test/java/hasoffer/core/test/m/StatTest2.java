package hasoffer.core.test.m;

import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.UrmDeviceBuyLog;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.user.IDeviceService;
import hasoffer.fetch.helper.WebsiteHelper;
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
public class StatTest2 {


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

        Date sdate = TimeUtils.stringToDate("2016-04-10", "yyyy-MM-dd");
        Date edate = TimeUtils.stringToDate("2016-04-13", "yyyy-MM-dd");

        getUrls(sdate, edate);
    }

    private void getUrls(Date sdate, Date edate) {
        Map<String, Long> urlCountMap = new LinkedHashMap<String, Long>();

        for (Website website : websites) {
            PageableResult<UrmDeviceBuyLog> pagedBuyLogs = deviceService.findUrmDeviceBuyLog("", website.name(), sdate, edate, 1, 1000, "", 1);

            List<UrmDeviceBuyLog> buyLogs = pagedBuyLogs.getData();

            for (UrmDeviceBuyLog buyLog : buyLogs) {

                PtmCmpSku cmpSku = cmpSkuService.getCmpSku(buyLog.getPtmProductId(), buyLog.getToWebsite());

                if (cmpSku != null) {

                    String url = WebsiteHelper.getDeeplinkWithAff(cmpSku.getWebsite(), cmpSku.getUrl(), new String[]{});

                    setMap(urlCountMap, url);
//                    setMap(urlCountMap, url2);
                }
            }
        }

        for (Map.Entry<String, Long> kv : urlCountMap.entrySet()) {
            System.out.println(kv.getValue() + "\t\t\t" + kv.getKey());
        }
    }

    private void setMap(Map<String, Long> urlCountMap, String url){
        if (urlCountMap.containsKey(url)) {
            urlCountMap.put(url, urlCountMap.get(url) + 1);
        } else {
            urlCountMap.put(url, 1L);
        }
    }
}

