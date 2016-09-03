package hasoffer.core.test.m;

import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.UrmDeviceRequestLog;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.user.IDeviceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Date : 2016/3/30
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class StatTest11 {

    private static final String Q_DEVICE =
            "SELECT t FROM UrmDevice t ";

    @Resource
    IDeviceService deviceService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;

    @Test
    public void test1() throws IOException {
        List<UrmDevice> devices = dbm.query(Q_DEVICE);
        for (UrmDevice device : devices) {
            analysis(device.getId());
        }
    }

    private void analysis(String deviceId) {

        PageableResult<UrmDeviceRequestLog> pagedLogs = deviceService.findDeviceLogs(deviceId, 1, Integer.MAX_VALUE);

        List<UrmDeviceRequestLog> logs = pagedLogs.getData();

        boolean hasCmp = false;
        boolean hasShop = false;

        for (UrmDeviceRequestLog log : logs) {

            String requestUri = log.getRequestUri();
            String queryStr = log.getQuery();

            if (requestUri.equals("/cmp/getcmpskus")) {
                hasCmp = true;
            } else if (requestUri.equals("/app/dot")) {
                if (queryStr.contains("action=shop")) {
                    System.out.println(queryStr);
                    hasShop = true;
                }
            }

            if (hasCmp && hasShop) {
                break;
            }
        }

        System.out.println(deviceId + "\t" + (hasCmp ? "1" : "0") + "\t" + (hasShop ? "1" : "0"));
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
