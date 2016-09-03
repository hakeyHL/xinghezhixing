package hasoffer.core.test.m;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.user.IDeviceService;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * Date : 2016/3/30
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class StatDeviceTest {

    private static final String Q_DEVICE =
            "SELECT t FROM UrmDevice t " +
                    " WHERE t.shopApp is not null " +
                    "   AND t.shopApp <> '' " +
                    "   AND t.createTime >= ?0 " +
                    "   AND t.createTime < ?1  " +
                    "   AND t.brand = 'samsung' ";
    @Resource
    IDeviceService deviceService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;

    @Test
    public void testStat() {
        Map<String, Long> modelCountMap = new LinkedHashMap<String, Long>();

        final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

        Date startDate = TimeUtils.stringToDate("2016-02-09 00:00:00", TIME_PATTERN);
        Date endDate = TimeUtils.stringToDate("2016-04-15 00:00:00", TIME_PATTERN);

        // list device by date
        List<UrmDevice> devices = dbm.query(Q_DEVICE, 1, Integer.MAX_VALUE, Arrays.asList(startDate, endDate));

        for (UrmDevice device : devices) {

            if (device.getBrand().equalsIgnoreCase("samsung")) {
                String dname = device.getDeviceName();
                String[] bms = dname.split(" ");
                if (bms[1].startsWith("SM") || bms[1].startsWith("SPH") || bms[1].startsWith("GT")) {
                    if (modelCountMap.containsKey(bms[1])) {
                        modelCountMap.put(bms[1], modelCountMap.get(bms[1]) + 1);
                    } else {
                        modelCountMap.put(bms[1], 1L);
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> kv : modelCountMap.entrySet()) {
            sb.append(kv.getValue() + "\t" + kv.getKey() + "\n");
        }

        File file = new File("d:/TMP/out.txt");
        if(file.exists()){
            file.delete();
        }
        try{
            file.createNewFile();
            FileUtils.writeByteArrayToFile(file, sb.toString().getBytes());
        }catch (Exception e){

        }
    }

}
