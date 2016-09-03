package hasoffer.core.persistence.mongo;

import hasoffer.base.enums.MarketChannel;
import hasoffer.base.utils.HexDigestUtil;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

/**
 * Created on 2016/4/11.
 */
@Document(collection = "StatDevice")
public class StatDevice {

    @Id
    private String id; // ymd + deviceId + marketChannel

    private String ymd;
    private String deviceYmd; // 设备创建时间

    private String deviceId;

    private String deviceName;

    private String osVersion;

    private MarketChannel marketChannel;

    private String campaign;

    private String adSet;

    private String brand;

    private String shopApp;

    private String firstBindAssistYmd; // 首次打开辅助功能的日期

    private int wakeUp;// 辅助功能打开的话，每天至少发一次请求

    private int bindAssist;// 绑定辅助功能

    private int cmpPrice; // 比价次数

    private int showBall; // 显示小球

    private int clickBall; // 点击小球

    private int shop; // 点击shop

    private int otherDot;

    @PersistenceConstructor
    public StatDevice() {
    }

    public StatDevice(MarketChannel marketChannel, String ymd, String deviceYmd,
                      String deviceId, String deviceName, String osVersion,
                      String campaign, String ADset, String brand, String shopApp) {
        this.id = HexDigestUtil.md5(marketChannel.name() + ymd + deviceId);
        this.ymd = ymd;
        this.deviceYmd = deviceYmd;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.osVersion = osVersion;
        this.marketChannel = marketChannel;
        this.campaign = campaign;
        this.adSet = ADset;
        this.brand = brand;
        this.shopApp = shopApp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public MarketChannel getMarketChannel() {
        return marketChannel;
    }

    public void setMarketChannel(MarketChannel marketChannel) {
        this.marketChannel = marketChannel;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getShopApp() {
        return shopApp;
    }

    public void setShopApp(String shopApp) {
        this.shopApp = shopApp;
    }

    public int getWakeUp() {
        return wakeUp;
    }

    public void setWakeUp(int wakeUp) {
        this.wakeUp = wakeUp;
    }

    public int getBindAssist() {
        return bindAssist;
    }

    public void setBindAssist(int bindAssist) {
        this.bindAssist = bindAssist;
    }

    public int getCmpPrice() {
        return cmpPrice;
    }

    public void setCmpPrice(int cmpPrice) {
        this.cmpPrice = cmpPrice;
    }

    public int getShowBall() {
        return showBall;
    }

    public void setShowBall(int showBall) {
        this.showBall = showBall;
    }

    public int getClickBall() {
        return clickBall;
    }

    public void setClickBall(int clickBall) {
        this.clickBall = clickBall;
    }

    public int getShop() {
        return shop;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    public String getDeviceYmd() {
        return deviceYmd;
    }

    public void setDeviceYmd(String deviceYmd) {
        this.deviceYmd = deviceYmd;
    }

    public int getOtherDot() {
        return otherDot;
    }

    public void setOtherDot(int otherDot) {
        this.otherDot = otherDot;
    }

    public String getFirstBindAssistYmd() {
        return firstBindAssistYmd;
    }

    public void setFirstBindAssistYmd(String firstBindAssistYmd) {
        this.firstBindAssistYmd = firstBindAssistYmd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatDevice that = (StatDevice) o;

        if (wakeUp != that.wakeUp) return false;
        if (bindAssist != that.bindAssist) return false;
        if (cmpPrice != that.cmpPrice) return false;
        if (showBall != that.showBall) return false;
        if (clickBall != that.clickBall) return false;
        if (shop != that.shop) return false;
        if (otherDot != that.otherDot) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (ymd != null ? !ymd.equals(that.ymd) : that.ymd != null) return false;
        if (deviceYmd != null ? !deviceYmd.equals(that.deviceYmd) : that.deviceYmd != null) return false;
        if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) return false;
        if (deviceName != null ? !deviceName.equals(that.deviceName) : that.deviceName != null) return false;
        if (osVersion != null ? !osVersion.equals(that.osVersion) : that.osVersion != null) return false;
        if (marketChannel != that.marketChannel) return false;
        if (campaign != null ? !campaign.equals(that.campaign) : that.campaign != null) return false;
        if (adSet != null ? !adSet.equals(that.adSet) : that.adSet != null) return false;
        if (brand != null ? !brand.equals(that.brand) : that.brand != null) return false;
        if (shopApp != null ? !shopApp.equals(that.shopApp) : that.shopApp != null) return false;
        return !(firstBindAssistYmd != null ? !firstBindAssistYmd.equals(that.firstBindAssistYmd) : that.firstBindAssistYmd != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (ymd != null ? ymd.hashCode() : 0);
        result = 31 * result + (deviceYmd != null ? deviceYmd.hashCode() : 0);
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + (deviceName != null ? deviceName.hashCode() : 0);
        result = 31 * result + (osVersion != null ? osVersion.hashCode() : 0);
        result = 31 * result + (marketChannel != null ? marketChannel.hashCode() : 0);
        result = 31 * result + (campaign != null ? campaign.hashCode() : 0);
        result = 31 * result + (adSet != null ? adSet.hashCode() : 0);
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (shopApp != null ? shopApp.hashCode() : 0);
        result = 31 * result + (firstBindAssistYmd != null ? firstBindAssistYmd.hashCode() : 0);
        result = 31 * result + wakeUp;
        result = 31 * result + bindAssist;
        result = 31 * result + cmpPrice;
        result = 31 * result + showBall;
        result = 31 * result + clickBall;
        result = 31 * result + shop;
        result = 31 * result + otherDot;
        return result;
    }

    @Override
    public String toString() {
        return "StatDevice{" +
                "id='" + id + '\'' +
                ", ymd='" + ymd + '\'' +
                ", deviceYmd='" + deviceYmd + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", marketChannel=" + marketChannel +
                ", wakeUp=" + wakeUp +
                ", bindAssist=" + bindAssist +
                ", cmpPrice=" + cmpPrice +
                ", showBall=" + showBall +
                ", clickBall=" + clickBall +
                ", shop=" + shop +
                ", otherDot=" + otherDot +
                '}';
    }
}
