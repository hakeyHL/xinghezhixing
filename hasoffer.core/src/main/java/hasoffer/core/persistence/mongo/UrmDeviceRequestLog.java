package hasoffer.core.persistence.mongo;

import hasoffer.base.enums.AppType;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Date;

/**
 * Date : 2016/1/15
 * Function :
 */
@Document(collection = "UrmDeviceRequestLog")
public class UrmDeviceRequestLog implements Serializable {

    @Id
    private String id;

    private String deviceId;

    private Date createTime;
    private long lCreateTime;

    private String requestUri;

    private String query;

    private Website curShopApp;
    private String shopApp;

    @Enumerated(EnumType.STRING)
    private AppType appType;

    @Enumerated(EnumType.STRING)
    private MarketChannel marketChannel;

    private String appVersion;

    private String curNetState;

    @PersistenceConstructor
    UrmDeviceRequestLog() {
        this.createTime = TimeUtils.nowDate();
        this.lCreateTime = createTime.getTime();
    }

    public UrmDeviceRequestLog(String deviceId, String requestUri, String query, String shopApp,
                               AppType appType, MarketChannel marketChannel, Website curShopApp,
                               String appVersion, String curNetState) {
        this();
        this.deviceId = deviceId;
        this.requestUri = requestUri;
        this.query = query;
        this.shopApp = shopApp;
        this.appType = appType;
        this.marketChannel = marketChannel;
        this.curShopApp = curShopApp;
        this.curNetState = curNetState;
        this.appVersion = appVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Website getCurShopApp() {
        return curShopApp;
    }

    public void setCurShopApp(Website curShopApp) {
        this.curShopApp = curShopApp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getCurNetState() {
        return curNetState;
    }

    public void setCurNetState(String curNetState) {
        this.curNetState = curNetState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrmDeviceRequestLog that = (UrmDeviceRequestLog) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (requestUri != null ? !requestUri.equals(that.requestUri) : that.requestUri != null) return false;
        if (query != null ? !query.equals(that.query) : that.query != null) return false;
        if (curShopApp != that.curShopApp) return false;
        if (shopApp != null ? !shopApp.equals(that.shopApp) : that.shopApp != null) return false;
        if (appType != that.appType) return false;
        if (marketChannel != that.marketChannel) return false;
        if (appVersion != null ? !appVersion.equals(that.appVersion) : that.appVersion != null) return false;
        return !(curNetState != null ? !curNetState.equals(that.curNetState) : that.curNetState != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (requestUri != null ? requestUri.hashCode() : 0);
        result = 31 * result + (query != null ? query.hashCode() : 0);
        result = 31 * result + (curShopApp != null ? curShopApp.hashCode() : 0);
        result = 31 * result + (shopApp != null ? shopApp.hashCode() : 0);
        result = 31 * result + (appType != null ? appType.hashCode() : 0);
        result = 31 * result + (marketChannel != null ? marketChannel.hashCode() : 0);
        result = 31 * result + (appVersion != null ? appVersion.hashCode() : 0);
        result = 31 * result + (curNetState != null ? curNetState.hashCode() : 0);
        return result;
    }
}
