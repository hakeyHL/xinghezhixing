package hasoffer.admin.controller.vo;

import hasoffer.base.enums.AppType;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.Website;

import java.util.Date;

/**
 * Date : 2016/3/12
 * Function :
 */
public class DeviceBuyLogVo {

    private String deviceId;

    private Date createTime;

    private String shopApp;

    private AppType appType;

    private MarketChannel marketChannel;

    private long proId;
    private String title;

    private Website toWebsite;
    private Website fromWebsite;

    public Website getFromWebsite() {
        return fromWebsite;
    }

    public void setFromWebsite(Website fromWebsite) {
        this.fromWebsite = fromWebsite;
    }

    public Website getToWebsite() {
        return toWebsite;
    }

    public void setToWebsite(Website toWebsite) {
        this.toWebsite = toWebsite;
    }

    public DeviceBuyLogVo() {
    }

    public DeviceBuyLogVo(AppType appType, Date createTime, String deviceId, Website fromWebsite, MarketChannel marketChannel, long proId, String shopApp, String title, Website toWebsite) {
        this.appType = appType;
        this.createTime = createTime;
        this.deviceId = deviceId;
        this.fromWebsite = fromWebsite;
        this.marketChannel = marketChannel;
        this.proId = proId;
        this.shopApp = shopApp;
        this.title = title;
        this.toWebsite = toWebsite;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public long getProId() {
        return proId;
    }

    public void setProId(long proId) {
        this.proId = proId;
    }

}
