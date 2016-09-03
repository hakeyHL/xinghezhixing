package hasoffer.core.persistence.mongo;

import hasoffer.base.utils.TimeUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

/**
 * Date : 2016/3/31
 * Function :
 */
@Document(collection = "UrmFbLog")
public class UrmFbLog {

    @Id
    private String id;
    private Date createTime = TimeUtils.nowDate();

    private String queryString;
    private String idfa;
    private String android_id;
    private String deviceName;
    private String osName;
    private String osVersion;
    private String country;
    private String language;
    private String timezone;
    private String userAgent;
    private String deviceIp;
    private String appName;
    private String appVersion;
    private String trackerId;
    private String trackerName;
    private String eventTime;

    private String campaign_name;
    private String adgroup_name;

    private String fb_campaign_group_name;
    private String fb_campaign_group_id;
    private String fb_campaign_name;
    private String fb_campaign_id;
    private String fb_adgroup_name;
    private String fb_adgroup_id;

    public UrmFbLog(String queryString, String idfa,
                    String android_id, String deviceName,
                    String osName, String osVersion,
                    String country, String language,
                    String timezone, String userAgent,
                    String deviceIp, String appName,
                    String appVersion, String trackerId,
                    String trackerName, String eventTime,
                    String campaign_name, String adgroup_name,
                    String fb_campaign_group_name, String fb_campaign_group_id,
                    String fb_campaign_name, String fb_campaign_id,
                    String fb_adgroup_name, String fb_adgroup_id) {
        this.queryString = queryString;
        this.idfa = idfa;
        this.android_id = android_id;
        this.deviceName = deviceName;
        this.osName = osName;
        this.osVersion = osVersion;
        this.country = country;
        this.language = language;
        this.timezone = timezone;
        this.userAgent = userAgent;
        this.deviceIp = deviceIp;
        this.appName = appName;
        this.appVersion = appVersion;
        this.trackerId = trackerId;
        this.trackerName = trackerName;
        this.eventTime = eventTime;
        this.campaign_name = campaign_name;
        this.adgroup_name = adgroup_name;
        this.fb_campaign_group_name = fb_campaign_group_name;
        this.fb_campaign_group_id = fb_campaign_group_id;
        this.fb_campaign_name = fb_campaign_name;
        this.fb_campaign_id = fb_campaign_id;
        this.fb_adgroup_name = fb_adgroup_name;
        this.fb_adgroup_id = fb_adgroup_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public String getAndroid_id() {
        return android_id;
    }

    public void setAndroid_id(String android_id) {
        this.android_id = android_id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(String trackerId) {
        this.trackerId = trackerId;
    }

    public String getTrackerName() {
        return trackerName;
    }

    public void setTrackerName(String trackerName) {
        this.trackerName = trackerName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getFb_campaign_group_name() {
        return fb_campaign_group_name;
    }

    public void setFb_campaign_group_name(String fb_campaign_group_name) {
        this.fb_campaign_group_name = fb_campaign_group_name;
    }

    public String getFb_campaign_group_id() {
        return fb_campaign_group_id;
    }

    public void setFb_campaign_group_id(String fb_campaign_group_id) {
        this.fb_campaign_group_id = fb_campaign_group_id;
    }

    public String getFb_campaign_name() {
        return fb_campaign_name;
    }

    public void setFb_campaign_name(String fb_campaign_name) {
        this.fb_campaign_name = fb_campaign_name;
    }

    public String getFb_campaign_id() {
        return fb_campaign_id;
    }

    public void setFb_campaign_id(String fb_campaign_id) {
        this.fb_campaign_id = fb_campaign_id;
    }

    public String getFb_adgroup_name() {
        return fb_adgroup_name;
    }

    public void setFb_adgroup_name(String fb_adgroup_name) {
        this.fb_adgroup_name = fb_adgroup_name;
    }

    public String getFb_adgroup_id() {
        return fb_adgroup_id;
    }

    public void setFb_adgroup_id(String fb_adgroup_id) {
        this.fb_adgroup_id = fb_adgroup_id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCampaign_name() {
        return campaign_name;
    }

    public void setCampaign_name(String campaign_name) {
        this.campaign_name = campaign_name;
    }

    public String getAdgroup_name() {
        return adgroup_name;
    }

    public void setAdgroup_name(String adgroup_name) {
        this.adgroup_name = adgroup_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrmFbLog urmFbLog = (UrmFbLog) o;

        if (id != null ? !id.equals(urmFbLog.id) : urmFbLog.id != null) return false;
        if (createTime != null ? !createTime.equals(urmFbLog.createTime) : urmFbLog.createTime != null) return false;
        if (queryString != null ? !queryString.equals(urmFbLog.queryString) : urmFbLog.queryString != null)
            return false;
        if (idfa != null ? !idfa.equals(urmFbLog.idfa) : urmFbLog.idfa != null) return false;
        if (android_id != null ? !android_id.equals(urmFbLog.android_id) : urmFbLog.android_id != null) return false;
        if (deviceName != null ? !deviceName.equals(urmFbLog.deviceName) : urmFbLog.deviceName != null) return false;
        if (osName != null ? !osName.equals(urmFbLog.osName) : urmFbLog.osName != null) return false;
        if (osVersion != null ? !osVersion.equals(urmFbLog.osVersion) : urmFbLog.osVersion != null) return false;
        if (country != null ? !country.equals(urmFbLog.country) : urmFbLog.country != null) return false;
        if (language != null ? !language.equals(urmFbLog.language) : urmFbLog.language != null) return false;
        if (timezone != null ? !timezone.equals(urmFbLog.timezone) : urmFbLog.timezone != null) return false;
        if (userAgent != null ? !userAgent.equals(urmFbLog.userAgent) : urmFbLog.userAgent != null) return false;
        if (deviceIp != null ? !deviceIp.equals(urmFbLog.deviceIp) : urmFbLog.deviceIp != null) return false;
        if (appName != null ? !appName.equals(urmFbLog.appName) : urmFbLog.appName != null) return false;
        if (appVersion != null ? !appVersion.equals(urmFbLog.appVersion) : urmFbLog.appVersion != null) return false;
        if (trackerId != null ? !trackerId.equals(urmFbLog.trackerId) : urmFbLog.trackerId != null) return false;
        if (trackerName != null ? !trackerName.equals(urmFbLog.trackerName) : urmFbLog.trackerName != null)
            return false;
        if (eventTime != null ? !eventTime.equals(urmFbLog.eventTime) : urmFbLog.eventTime != null) return false;
        if (campaign_name != null ? !campaign_name.equals(urmFbLog.campaign_name) : urmFbLog.campaign_name != null)
            return false;
        if (adgroup_name != null ? !adgroup_name.equals(urmFbLog.adgroup_name) : urmFbLog.adgroup_name != null)
            return false;
        if (fb_campaign_group_name != null ? !fb_campaign_group_name.equals(urmFbLog.fb_campaign_group_name) : urmFbLog.fb_campaign_group_name != null)
            return false;
        if (fb_campaign_group_id != null ? !fb_campaign_group_id.equals(urmFbLog.fb_campaign_group_id) : urmFbLog.fb_campaign_group_id != null)
            return false;
        if (fb_campaign_name != null ? !fb_campaign_name.equals(urmFbLog.fb_campaign_name) : urmFbLog.fb_campaign_name != null)
            return false;
        if (fb_campaign_id != null ? !fb_campaign_id.equals(urmFbLog.fb_campaign_id) : urmFbLog.fb_campaign_id != null)
            return false;
        if (fb_adgroup_name != null ? !fb_adgroup_name.equals(urmFbLog.fb_adgroup_name) : urmFbLog.fb_adgroup_name != null)
            return false;
        return !(fb_adgroup_id != null ? !fb_adgroup_id.equals(urmFbLog.fb_adgroup_id) : urmFbLog.fb_adgroup_id != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (queryString != null ? queryString.hashCode() : 0);
        result = 31 * result + (idfa != null ? idfa.hashCode() : 0);
        result = 31 * result + (android_id != null ? android_id.hashCode() : 0);
        result = 31 * result + (deviceName != null ? deviceName.hashCode() : 0);
        result = 31 * result + (osName != null ? osName.hashCode() : 0);
        result = 31 * result + (osVersion != null ? osVersion.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (timezone != null ? timezone.hashCode() : 0);
        result = 31 * result + (userAgent != null ? userAgent.hashCode() : 0);
        result = 31 * result + (deviceIp != null ? deviceIp.hashCode() : 0);
        result = 31 * result + (appName != null ? appName.hashCode() : 0);
        result = 31 * result + (appVersion != null ? appVersion.hashCode() : 0);
        result = 31 * result + (trackerId != null ? trackerId.hashCode() : 0);
        result = 31 * result + (trackerName != null ? trackerName.hashCode() : 0);
        result = 31 * result + (eventTime != null ? eventTime.hashCode() : 0);
        result = 31 * result + (campaign_name != null ? campaign_name.hashCode() : 0);
        result = 31 * result + (adgroup_name != null ? adgroup_name.hashCode() : 0);
        result = 31 * result + (fb_campaign_group_name != null ? fb_campaign_group_name.hashCode() : 0);
        result = 31 * result + (fb_campaign_group_id != null ? fb_campaign_group_id.hashCode() : 0);
        result = 31 * result + (fb_campaign_name != null ? fb_campaign_name.hashCode() : 0);
        result = 31 * result + (fb_campaign_id != null ? fb_campaign_id.hashCode() : 0);
        result = 31 * result + (fb_adgroup_name != null ? fb_adgroup_name.hashCode() : 0);
        result = 31 * result + (fb_adgroup_id != null ? fb_adgroup_id.hashCode() : 0);
        return result;
    }
}
