package hasoffer.core.persistence.po.sys;

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;
import java.util.Date;

/**
 * Date : 2016/3/11
 * Function :
 */
@Entity
public class SysCheckTask implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long adminId;

    private Date createTime;

    /**
     * 指 搜索记录 第一次被搜索的时间范围： start - end
     */
    private Date startTime;
    private Date endTime;

    public SysCheckTask() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysCheckTask checkTask = (SysCheckTask) o;

        if (adminId != checkTask.adminId) return false;
        if (id != null ? !id.equals(checkTask.id) : checkTask.id != null) return false;
        if (createTime != null ? !createTime.equals(checkTask.createTime) : checkTask.createTime != null) return false;
        if (startTime != null ? !startTime.equals(checkTask.startTime) : checkTask.startTime != null) return false;
        return !(endTime != null ? !endTime.equals(checkTask.endTime) : checkTask.endTime != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (adminId ^ (adminId >>> 32));
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }
}
