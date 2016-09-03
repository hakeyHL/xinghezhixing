package hasoffer.core.persistence.po.urm;

import hasoffer.base.enums.AppType;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;
import java.util.Date;

/**
 * Date : 2016/1/15
 * Function :
 * <p>
 * id, createTime, mac, brand, imei, deviceId, serial, deviceName,
 * osVersion, appVersion, screen, shopApp, otherApp, appType, marketChannel
 * ramSize, appCount, gcmToken
 */
@Entity
public class UrmDevice implements Identifiable<String> {

    @Id
    @Column(unique = true, nullable = false)
    private String id;
    private Date createTime;
    private Date updateTime;

    private String mac;
    private String brand;
    private String imeiId;
    private String deviceId;// 仅用在此表,其他地方的deviceId应该是 本表的id
    private String serial;

    private String deviceName;
    private String osVersion;
    private String appVersion;
    private String screen;

    private String shopApp;
    private String otherApp;

    @Enumerated(EnumType.STRING)
    private AppType appType;
    @Enumerated(EnumType.STRING)
    private MarketChannel marketChannel;

    private boolean statAble;// 是否统计

    private String screenSize;
    private String ramSize;
    private int appCount; // app安装数量

    private String campaign;
    private String adSet;

    private String firstBindAssistYmd; // 首次打开辅助功能的日期

    private String gcmToken;

    public UrmDevice() {
        this.statAble = true;
    }

    public UrmDevice(String id, String mac,
                     String brand, String imeiId,
                     String deviceId, String serial,
                     String deviceName, String osVersion,
                     String appVersion, String screen,
                     AppType appType, MarketChannel marketChannel,
                     String shopApp, String otherApp,
                     String screenSize, String ramSize,
                     int appCount, String gcmToken) {
        this();
        this.id = id;
        this.mac = mac;
        this.createTime = TimeUtils.nowDate();
        this.updateTime = this.createTime;
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
        this.shopApp = shopApp;
        this.otherApp = otherApp;
        this.screenSize = screenSize;
        this.ramSize = ramSize;
        this.appCount = appCount;
        this.gcmToken = gcmToken;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public boolean isStatAble() {
        return statAble;
    }

    public void setStatAble(boolean statAble) {
        this.statAble = statAble;
    }

    public String getOtherApp() {
        return otherApp;
    }

    public void setOtherApp(String otherApp) {
        this.otherApp = otherApp;
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

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getAdSet() {
        return adSet;
    }

    public void setAdSet(String adSet) {
        this.adSet = adSet;
    }

    public String getFirstBindAssistYmd() {
        return firstBindAssistYmd;
    }

    public void setFirstBindAssistYmd(String firstBindAssistYmd) {
        this.firstBindAssistYmd = firstBindAssistYmd;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrmDevice device = (UrmDevice) o;

        if (statAble != device.statAble) return false;
        if (appCount != device.appCount) return false;
        if (id != null ? !id.equals(device.id) : device.id != null) return false;
        if (createTime != null ? !createTime.equals(device.createTime) : device.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(device.updateTime) : device.updateTime != null) return false;
        if (mac != null ? !mac.equals(device.mac) : device.mac != null) return false;
        if (brand != null ? !brand.equals(device.brand) : device.brand != null) return false;
        if (imeiId != null ? !imeiId.equals(device.imeiId) : device.imeiId != null) return false;
        if (deviceId != null ? !deviceId.equals(device.deviceId) : device.deviceId != null) return false;
        if (serial != null ? !serial.equals(device.serial) : device.serial != null) return false;
        if (deviceName != null ? !deviceName.equals(device.deviceName) : device.deviceName != null) return false;
        if (osVersion != null ? !osVersion.equals(device.osVersion) : device.osVersion != null) return false;
        if (appVersion != null ? !appVersion.equals(device.appVersion) : device.appVersion != null) return false;
        if (screen != null ? !screen.equals(device.screen) : device.screen != null) return false;
        if (shopApp != null ? !shopApp.equals(device.shopApp) : device.shopApp != null) return false;
        if (otherApp != null ? !otherApp.equals(device.otherApp) : device.otherApp != null) return false;
        if (appType != device.appType) return false;
        if (marketChannel != device.marketChannel) return false;
        if (screenSize != null ? !screenSize.equals(device.screenSize) : device.screenSize != null) return false;
        if (ramSize != null ? !ramSize.equals(device.ramSize) : device.ramSize != null) return false;
        if (campaign != null ? !campaign.equals(device.campaign) : device.campaign != null) return false;
        if (adSet != null ? !adSet.equals(device.adSet) : device.adSet != null) return false;
        if (firstBindAssistYmd != null ? !firstBindAssistYmd.equals(device.firstBindAssistYmd) : device.firstBindAssistYmd != null)
            return false;
        return !(gcmToken != null ? !gcmToken.equals(device.gcmToken) : device.gcmToken != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (mac != null ? mac.hashCode() : 0);
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (imeiId != null ? imeiId.hashCode() : 0);
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + (serial != null ? serial.hashCode() : 0);
        result = 31 * result + (deviceName != null ? deviceName.hashCode() : 0);
        result = 31 * result + (osVersion != null ? osVersion.hashCode() : 0);
        result = 31 * result + (appVersion != null ? appVersion.hashCode() : 0);
        result = 31 * result + (screen != null ? screen.hashCode() : 0);
        result = 31 * result + (shopApp != null ? shopApp.hashCode() : 0);
        result = 31 * result + (otherApp != null ? otherApp.hashCode() : 0);
        result = 31 * result + (appType != null ? appType.hashCode() : 0);
        result = 31 * result + (marketChannel != null ? marketChannel.hashCode() : 0);
        result = 31 * result + (statAble ? 1 : 0);
        result = 31 * result + (screenSize != null ? screenSize.hashCode() : 0);
        result = 31 * result + (ramSize != null ? ramSize.hashCode() : 0);
        result = 31 * result + appCount;
        result = 31 * result + (campaign != null ? campaign.hashCode() : 0);
        result = 31 * result + (adSet != null ? adSet.hashCode() : 0);
        result = 31 * result + (firstBindAssistYmd != null ? firstBindAssistYmd.hashCode() : 0);
        result = 31 * result + (gcmToken != null ? gcmToken.hashCode() : 0);
        return result;
    }
}
