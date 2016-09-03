package hasoffer.core.bo.cache;

/**
 * Date : 2016/5/30
 * Function :
 */
public class DeviceFlowControllRecord {

    private String deviceId;

    private String cliSite;

    private boolean flowed;// 是否返回过

    public DeviceFlowControllRecord() {
    }

    public DeviceFlowControllRecord(String deviceId, String cliSite, boolean flowed) {
        this.deviceId = deviceId;
        this.cliSite = cliSite;
        this.flowed = flowed;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCliSite() {
        return cliSite;
    }

    public void setCliSite(String cliSite) {
        this.cliSite = cliSite;
    }

    public boolean isFlowed() {
        return flowed;
    }

    public void setFlowed(boolean flowed) {
        this.flowed = flowed;
    }
}
