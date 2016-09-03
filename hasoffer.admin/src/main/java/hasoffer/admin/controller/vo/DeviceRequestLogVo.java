package hasoffer.admin.controller.vo;

import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.mongo.UrmDeviceRequestLog;

import java.util.Date;

/**
 * Date : 2016/1/15
 * Function :
 */
public class DeviceRequestLogVo {

    private String id;

    private DeviceVo deviceInfo;

    private Date createTime = TimeUtils.nowDate();

    private String requestUri;

    private String query;
    private String[] queryParams;

    public DeviceRequestLogVo(UrmDeviceRequestLog log, DeviceVo deviceInfo) {
        this.id = log.getId();
        this.createTime = log.getCreateTime();
        this.requestUri = log.getRequestUri();
        this.query = log.getQuery();
        this.deviceInfo = deviceInfo;

        if (!StringUtils.isEmpty(query)) {
            this.queryParams = StringUtils.urlDecode(query).split("&");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DeviceVo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceVo deviceInfo) {
        this.deviceInfo = deviceInfo;
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

    public String[] getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String[] queryParams) {
        this.queryParams = queryParams;
    }
}
