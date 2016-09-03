package hasoffer.core.bo.user;

/**
 * Date : 2016/1/15
 * Function :
 */
public class DeviceRequestBo {

    private DeviceInfoBo deviceInfoBo;

    private DeviceEventBo deviceEventBo;

    private String requestUri;

    private String query;

    public DeviceRequestBo(DeviceInfoBo deviceInfoBo, String requestUri, String query) {
        this.deviceInfoBo = deviceInfoBo;
        this.requestUri = requestUri;
        this.query = query;
    }

    public DeviceInfoBo getDeviceInfoBo() {
        return deviceInfoBo;
    }

    public void setDeviceInfoBo(DeviceInfoBo deviceInfoBo) {
        this.deviceInfoBo = deviceInfoBo;
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

    public DeviceEventBo getDeviceEventBo() {
        return deviceEventBo;
    }

    public void setDeviceEventBo(DeviceEventBo deviceEventBo) {
        this.deviceEventBo = deviceEventBo;
    }
}
