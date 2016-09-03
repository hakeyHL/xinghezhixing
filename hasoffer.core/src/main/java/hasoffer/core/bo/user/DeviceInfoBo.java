package hasoffer.core.bo.user;

import hasoffer.base.enums.AppType;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.Website;

/**
 * Date : 2016/1/15
 * Function :
 */
public class DeviceInfoBo {

    private String brand;
    private String imeiId;
    private String deviceId;
    private String serial;

    private String mac;

    private String deviceName;
    private String osVersion;
    private String appVersion;
    private String screen;

    private String screenSize;
    private String ramSize;
    private int appCount; // app安装数量

    private String curNetState;

    private Website curShopApp;
    private String shopApp;
    private String otherApp;
    private AppType appType;
    private MarketChannel marketChannel;

    private String gcmToken; // push 用的

    public DeviceInfoBo(String mac,
                        String brand, String imeiId,
                        String deviceId, String serial,
                        String deviceName, String osVersion,
                        String appVersion, String screen,
                        String shopApp, AppType appType,
                        MarketChannel marketChannel,
                        Website curShopApp, String otherApp,
                        String screenSize, String ramSize,
                        String curNetState, int appCount,
                        String gcmToken) {
        this.mac = mac;
        this.shopApp = shopApp;
        this.brand = brand;
        this.imeiId = imeiId;
        this.deviceId = deviceId;
        this.serial = serial;
        this.deviceName = deviceName;
        this.osVersion = osVersion;
        this.appVersion = appVersion;
        this.screen = screen;
        this.appType = appType;
        this.marketChannel = marketChannel;
        this.curShopApp = curShopApp;
        this.otherApp = otherApp;
        this.screenSize = screenSize;
        this.ramSize = ramSize;
        this.curNetState = curNetState;
        this.appCount = appCount;
        this.gcmToken = gcmToken;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public String getRamSize() {
        return ramSize;
    }

    public void setRamSize(String ramSize) {
        this.ramSize = ramSize;
    }

    public int getAppCount() {
        return appCount;
    }

    public void setAppCount(int appCount) {
        this.appCount = appCount;
    }

    public String getCurNetState() {
        return curNetState;
    }

    public void setCurNetState(String curNetState) {
        this.curNetState = curNetState;
    }

    public String getOtherApp() {
        return otherApp;
    }

    public void setOtherApp(String otherApp) {
        this.otherApp = otherApp;
    }

    public Website getCurShopApp() {
        return curShopApp;
    }

    public void setCurShopApp(Website curShopApp) {
        this.curShopApp = curShopApp;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImeiId() {
        return imeiId;
    }

    public void setImeiId(String imeiId) {
        this.imeiId = imeiId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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

    public AppType getAppType() {
        return appType;
    }

    public void setAppType(AppType appType) {
        this.appType = appType;
    }

    public MarketChannel getMarketChannel() {
        return marketChannel;
    }

    public void setMarketChannel(MarketChannel marketChannel) {
        this.marketChannel = marketChannel;
    }
}
