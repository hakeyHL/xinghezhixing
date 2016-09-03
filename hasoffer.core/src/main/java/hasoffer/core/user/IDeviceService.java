package hasoffer.core.user;

import hasoffer.base.enums.AppType;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.PageableResult;
import hasoffer.core.bo.user.DayVisitBo;
import hasoffer.core.bo.user.DeviceRequestBo;
import hasoffer.core.persistence.mongo.StatDayAlive;
import hasoffer.core.persistence.mongo.StatDevice;
import hasoffer.core.persistence.mongo.UrmDeviceBuyLog;
import hasoffer.core.persistence.mongo.UrmDeviceRequestLog;
import hasoffer.core.persistence.po.urm.UrmDayVisit;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.persistence.po.urm.UrmDeviceConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Date : 2016/1/15
 * Function :
 */
public interface IDeviceService {

    PageableResult<UrmDeviceRequestLog> findDeviceLogs(String deviceId, Date start, Date end, int page, int size);

    PageableResult<UrmDevice> findPagedDevices(int page, int size);

    PageableResult<UrmDevice> findPagedDevices(Date sDate, Date eDate, int page, int size);

    PageableResult<UrmDevice> findPagedDevicesAsc(int page, int size);

    PageableResult<UrmDevice> findPagedDevices(String deviceName, AppType appType, MarketChannel marketChannel, Date startDate, Date endDate, int page, int size, String orderByFieldName, int orderType);

    PageableResult<UrmDeviceRequestLog> findDeviceLogsByRequestUri(String requestUri, int page, int size);

    PageableResult<UrmDeviceRequestLog> findDeviceLogsByRequestUri(String requestUri, Date start, Date end, int page, int size);

    PageableResult<UrmDeviceRequestLog> findDeviceLogsByUriAndQuery(String requestUri, String query, Date start, Date end, int page, int size);

    PageableResult<UrmDeviceRequestLog> findDeviceLogsByUriAndQuery(String requestUri, String query, boolean eq, Date start, Date end, int page, int size);

    PageableResult<UrmDeviceRequestLog> findDeviceLogs(String deviceId, int page, int size);

    PageableResult<UrmDeviceRequestLog> findRequestLogs(Date startDate, Date endDate, int page, int size);

    @Transactional(rollbackFor = Exception.class)
    void batchSaveDeviceRequest(List<DeviceRequestBo> requestBoList);

    void saveDeviceRequest(DeviceRequestBo bo);

    DayVisitBo statDayVisit(String ymd);

    void saveOrUpdate(DayVisitBo dvb);

    List<UrmDayVisit> listDayVisits();

    boolean restatIfInHour();

    void updateDeviceShopApp(String id, String shopApp);

    UrmDevice findDevice(String deviceId);

    List<UrmDevice> findDeviceByIdList(List<String> deviceList);

    void deviceRequestLogsAnalysis(Date butLogsMaxCreateTime);

    UrmDeviceConfig getDeviceConfig(String deviceId);

    PageableResult<UrmDeviceBuyLog> findUrmDeviceBuyLog(String fromWebsite, String toWebsite, Date start, Date end, int page, int size, String orderFieldName, int order);

    Map<MarketChannel, StatDevice> statByLog(String deviceId, String ymd);

    Map<MarketChannel, StatDevice> statByLog(UrmDevice device, String ymd);

    StatDayAlive statDayAlive(String ymd, String deviceBrand, String osVersion, MarketChannel channel);

    void saveDayAlive(StatDayAlive sda);

    void saveDeviceStatResult(StatDevice sd);

    List<StatDevice> listDeviceStats(String deviceId);

    PageableResult<StatDevice> listPagedStatDevice(String ymd, int page, int size);

    List<StatDevice> listStatDevice(String ymd, int page, int size);

    void updateFirstBindAssistYmd(String deviceId);

    PageableResult<StatDayAlive> findAliveStats(String startYmd, String endYmd, String marketChannel, String brand, String osVersion, int page, int size, String order);


    List<StatDevice> findCmpskuStat(String marketChannel, int days, String startYmd, String endYmd);
}
