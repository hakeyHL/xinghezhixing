package hasoffer.api.controller.vo;

import hasoffer.base.utils.StringUtils;
import hasoffer.core.bo.user.DeviceEventBo;
import hasoffer.core.bo.user.DeviceInfoBo;
import hasoffer.core.bo.user.DeviceRequestBo;

/**
 * Date : 2016/1/15
 * Function :
 */
public class DeviceRequestVo {

    private DeviceInfoVo deviceInfoVo;

    private String requestUri;

    private String query;

    private DeviceEventVo deviceEvent;

    public DeviceRequestVo(DeviceInfoVo deviceInfoVo, String requestUri, String query) {
        this.deviceInfoVo = deviceInfoVo;
        this.requestUri = requestUri;
        this.query = query;
    }

    public DeviceInfoVo getDeviceInfoVo() {
        return deviceInfoVo;
    }

    public void setDeviceInfoVo(DeviceInfoVo deviceInfoVo) {
        this.deviceInfoVo = deviceInfoVo;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public DeviceEventVo getDeviceEvent() {
        return deviceEvent;
    }

    public void setDeviceEvent(DeviceEventVo deviceEvent) {
        this.deviceEvent = deviceEvent;
    }

    public DeviceRequestBo getBo() {

        DeviceInfoVo div = this.getDeviceInfoVo();

        if (div == null) {
            return null;
        }

        DeviceInfoBo deviceInfoBo = new DeviceInfoBo(
                div.getMac(),
                div.getBrand(), div.getImeiId(),
                div.getDeviceId(), div.getSerial(),
                div.getDeviceName(), div.getOsVersion(),
                div.getAppVersion(), div.getScreen(),
                StringUtils.arrayToString(div.getShopApp(), ","),
                div.getAppType(), div.getMarketChannel(),
                div.getCurShopApp(),
                StringUtils.arrayToString(div.getOtherApp(), ","),
                div.getScreenSize(), div.getRamSize(),
                div.getCurNetState(), div.getAppCount(),
                div.getGcmToken()
        );

        DeviceRequestBo drb = new DeviceRequestBo(deviceInfoBo, this.getRequestUri(), this.getQuery());

        DeviceEventVo de = this.getDeviceEvent();
        if (de != null) {
            DeviceEventBo debo = new DeviceEventBo(de.getEvent(), de.getInfo());
            drb.setDeviceEventBo(debo);
        }

        return drb;
    }
}
