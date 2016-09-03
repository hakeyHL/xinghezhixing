package hasoffer.core.test.m;

import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.UrmDeviceBuyLog;
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
public class StatTest8 {

    @Resource
    IDeviceService deviceService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;

    @Test
    public void testStat() {

        final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

        Date startDate = TimeUtils.stringToDate("2016-04-10 00:00:00", TIME_PATTERN);
        Date endDate = TimeUtils.stringToDate("2016-04-18 00:00:00", TIME_PATTERN);

        int page = 1;
        int size = Integer.MAX_VALUE;

        Map<Website, Map<String, Integer>> buyMap = new HashMap<Website, Map<String, Integer>>();

        Date start = startDate;

        while (start.getTime() < endDate.getTime()) {
            String ymd = TimeUtils.parse(start, "yyyyMMdd");
            Date end = new Date(start.getTime() + TimeUtils.MILLISECONDS_OF_1_DAY);

            PageableResult<UrmDeviceBuyLog> pagedBuyLogs = deviceService.findUrmDeviceBuyLog(null, null, start, end, page, size, null, 0);

            List<UrmDeviceBuyLog> buyLogs = pagedBuyLogs.getData();

            for (UrmDeviceBuyLog buyLog : buyLogs) {
                Website website = buyLog.getToWebsite();
                Map<String, Integer> counts = buyMap.get(website);
                if (counts == null) {
                    counts = new HashMap<String, Integer>();
                    buyMap.put(website, counts);
                }

                if (counts.containsKey(ymd)) {
                    counts.put(ymd, counts.get(ymd) + 1);
                } else {
                    counts.put(ymd, 1);
                }

            }

            start = end;
        }

        String[] ymds = new String[]{
                "20160410", "20160411", "20160412", "20160413", "20160414",
                "20160415", "20160416", "20160417", "20160418"
        };
        for (Map.Entry<Website, Map<String, Integer>> kv : buyMap.entrySet()) {
            Website website = kv.getKey();
            Map<String, Integer> values = kv.getValue();
            System.out.print(website.name());
            for (String ymd : ymds) {
                System.out.print("\t");
                if (values.containsKey(ymd)) {
                    System.out.print(values.get(ymd));
                } else {
                    System.out.print(0);
                }
            }
            System.out.println();
        }

    }
}
