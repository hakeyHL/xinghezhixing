package hasoffer.core.persistence.mongo;

import hasoffer.base.model.Website;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created on 2016/5/4.
 */
@Document(collection = "HijackLog")
public class HijackLog {

    @Id
    public String id;

    private Website website;
    private Date createTime ;
    private long lCreateTime;

    public long getlCreateTime() {
        return lCreateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        this.lCreateTime = createTime.getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HijackLog hijackLog = (HijackLog) o;

        if (lCreateTime != hijackLog.lCreateTime) return false;
        if (id != null ? !id.equals(hijackLog.id) : hijackLog.id != null) return false;
        if (website != hijackLog.website) return false;
        return !(createTime != null ? !createTime.equals(hijackLog.createTime) : hijackLog.createTime != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (int) (lCreateTime ^ (lCreateTime >>> 32));
        return result;
    }
}
