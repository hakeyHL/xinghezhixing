package hasoffer.core.persistence.mongo;

import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created on 2016/6/6.
 */
@Document(collection = "StatHijackFetchCount")
public class StatHijackFetchCount {

    @Id
    private String id;//当日零点和website的MD5

    private Website website;
    private Date date;//记录该条记录对应的时间
    private long updateTime;
    private String updateDate;

    private long totalAmount;//当日应劫持总数
    private long statusSuccessAmount;//劫持成功数
    private long statusFailAmount;//劫持失败数
    private long differentUrlAmount;//因重名失败数
    private long noIndexAmount;//因no index失败数
    private long noIndexSuccessAmount;//抓取成功数
    private long noIndexFailAmount;//抓取失败数

    public String getUpdateDate() {
        return TimeUtils.parse(updateTime,"yyyy-MM-dd HH:mm:ss");
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public StatHijackFetchCount() {
        this.date = TimeUtils.toDate(TimeUtils.today());
    }

    public long getDifferentUrlAmount() {
        return differentUrlAmount;
    }

    public void setDifferentUrlAmount(long differentUrlAmount) {
        this.differentUrlAmount = differentUrlAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getNoIndexAmount() {
        return noIndexAmount;
    }

    public void setNoIndexAmount(long noIndexAmount) {
        this.noIndexAmount = noIndexAmount;
    }

    public long getNoIndexFailAmount() {
        return noIndexFailAmount;
    }

    public void setNoIndexFailAmount(long noIndexFailAmount) {
        this.noIndexFailAmount = noIndexFailAmount;
    }

    public long getNoIndexSuccessAmount() {
        return noIndexSuccessAmount;
    }

    public void setNoIndexSuccessAmount(long noIndexSuccessAmount) {
        this.noIndexSuccessAmount = noIndexSuccessAmount;
    }

    public long getStatusFailAmount() {
        return statusFailAmount;
    }

    public void setStatusFailAmount(long statusFailAmount) {
        this.statusFailAmount = statusFailAmount;
    }

    public long getStatusSuccessAmount() {
        return statusSuccessAmount;
    }

    public void setStatusSuccessAmount(long statusSuccessAmount) {
        this.statusSuccessAmount = statusSuccessAmount;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatHijackFetchCount that = (StatHijackFetchCount) o;

        if (updateTime != that.updateTime) return false;
        if (totalAmount != that.totalAmount) return false;
        if (statusSuccessAmount != that.statusSuccessAmount) return false;
        if (statusFailAmount != that.statusFailAmount) return false;
        if (differentUrlAmount != that.differentUrlAmount) return false;
        if (noIndexAmount != that.noIndexAmount) return false;
        if (noIndexSuccessAmount != that.noIndexSuccessAmount) return false;
        if (noIndexFailAmount != that.noIndexFailAmount) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (website != that.website) return false;
        return !(date != null ? !date.equals(that.date) : that.date != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (int) (updateTime ^ (updateTime >>> 32));
        result = 31 * result + (int) (totalAmount ^ (totalAmount >>> 32));
        result = 31 * result + (int) (statusSuccessAmount ^ (statusSuccessAmount >>> 32));
        result = 31 * result + (int) (statusFailAmount ^ (statusFailAmount >>> 32));
        result = 31 * result + (int) (differentUrlAmount ^ (differentUrlAmount >>> 32));
        result = 31 * result + (int) (noIndexAmount ^ (noIndexAmount >>> 32));
        result = 31 * result + (int) (noIndexSuccessAmount ^ (noIndexSuccessAmount >>> 32));
        result = 31 * result + (int) (noIndexFailAmount ^ (noIndexFailAmount >>> 32));
        return result;
    }
}
