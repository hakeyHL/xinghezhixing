package hasoffer.admin.controller.vo;

import hasoffer.core.persistence.dbm.osql.Identifiable;
import hasoffer.core.persistence.po.urm.UrmDevice;

import java.util.Date;

public class DeviceVo implements Identifiable<String> {
    private String id;
    private Date createTime;

    private String deviceName;
    private String osVersion;
    private String appVersion;
    private String screen;

    private String shopApp;

    public DeviceVo(UrmDevice device) {
        this.id = device.getId();
        this.createTime = device.getCreateTime();
        this.deviceName = device.getDeviceName();
        this.osVersion = device.getOsVersion();
        this.appVersion = device.getAppVersion();
        this.shopApp = device.getShopApp();
        this.screen = device.getScreen();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getShopApp() {
        return shopApp;
    }

    public void setShopApp(String shopApp) {
        this.shopApp = shopApp;
    }
}
