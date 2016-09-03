package hasoffer.api.controller.vo;

import hasoffer.core.persistence.po.app.AppVersion;

import java.util.Date;

/**
 * Created on 2015/12/30.
 */
public class AppVersionVo {
    private String version;

    private String url;

    private Date publishTime;

    private String msg;

    public AppVersionVo(AppVersion appVersion) {
        if (appVersion == null) {
            return;
        }
        this.msg = appVersion.getMsg();
        this.version = appVersion.getVersion();
        this.url = appVersion.getUrl();
        this.publishTime = appVersion.getPublishTime();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
}
