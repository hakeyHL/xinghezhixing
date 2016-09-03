package hasoffer.core.persistence.mongo;

import hasoffer.base.enums.AppType;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;


/**
 * Created on 2016/3/24.
 */
@Document(collection = "UrmDeviceBuyLog")
public class UrmDeviceBuyLog {

    @Id
    public String id;
    private String deviceId;

    private Date createTime = TimeUtils.nowDate();

    //跳转APP
    private Website toWebsite;
    private Website fromWebsite;

    private long ptmProductId;
    private String title;

    private String shopApp;

    @Enumerated(EnumType.STRING)
    private AppType appType;
    @Enumerated(EnumType.STRING)
    private MarketChannel marketChannel;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Website getFromWebsite() {
        return fromWebsite;
    }

    public void setFromWebsite(Website fromWebsite) {
        this.fromWebsite = fromWebsite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MarketChannel getMarketChannel() {
        return marketChannel;
    }

    public void setMarketChannel(MarketChannel marketChannel) {
        this.marketChannel = marketChannel;
    }

    public long getPtmProductId() {
        return ptmProductId;
    }

    public void setPtmProductId(long ptmProductId) {
        this.ptmProductId = ptmProductId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Website getToWebsite() {
        return toWebsite;
    }

    public void setToWebsite(Website toWebsite) {
        this.toWebsite = toWebsite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrmDeviceBuyLog buyLog = (UrmDeviceBuyLog) o;

        if (ptmProductId != buyLog.ptmProductId) return false;
        if (id != null ? !id.equals(buyLog.id) : buyLog.id != null) return false;
        if (deviceId != null ? !deviceId.equals(buyLog.deviceId) : buyLog.deviceId != null) return false;
        if (createTime != null ? !createTime.equals(buyLog.createTime) : buyLog.createTime != null) return false;
        if (toWebsite != buyLog.toWebsite) return false;
        if (fromWebsite != buyLog.fromWebsite) return false;
        if (title != null ? !title.equals(buyLog.title) : buyLog.title != null) return false;
        if (shopApp != null ? !shopApp.equals(buyLog.shopApp) : buyLog.shopApp != null) return false;
        if (appType != buyLog.appType) return false;
        return marketChannel == buyLog.marketChannel;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (toWebsite != null ? toWebsite.hashCode() : 0);
        result = 31 * result + (fromWebsite != null ? fromWebsite.hashCode() : 0);
        result = 31 * result + (int) (ptmProductId ^ (ptmProductId >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (shopApp != null ? shopApp.hashCode() : 0);
        result = 31 * result + (appType != null ? appType.hashCode() : 0);
        result = 31 * result + (marketChannel != null ? marketChannel.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UrmDeviceBuyLog{" +
                "id='" + id + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", createTime=" + createTime +
                ", toWebsite=" + toWebsite +
                ", fromWebsite=" + fromWebsite +
                ", ptmProductId=" + ptmProductId +
                ", title='" + title + '\'' +
                ", shopApp='" + shopApp + '\'' +
                ", appType=" + appType +
                ", marketChannel=" + marketChannel +
                '}';
    }
}


