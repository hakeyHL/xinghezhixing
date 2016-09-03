package hasoffer.core.persistence.po.urm;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;
import java.util.Date;

/**
 * 降价通知表
 */
@Entity
public class PriceOffNotice implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//主键

    @Column(nullable = false)
    private String userid;//用户id

    @Column(nullable = false)
    private long skuid;//skuid

    @Column(nullable = false)
    private float originPrice;//关注时价格

    private float noticePrice;//用户设置的提醒价格，低于该值提醒，默认是关注时价格

    private Date createTime = TimeUtils.nowDate();//记录的创建时间

    private boolean latestPushStatus;//最近一次推送状态

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long aLong) {
        this.id = aLong;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isLatestPushStatus() {
        return latestPushStatus;
    }

    public void setLatestPushStatus(boolean latestPushStatus) {
        this.latestPushStatus = latestPushStatus;
    }

    public float getNoticePrice() {
        return noticePrice;
    }

    public void setNoticePrice(float noticePrice) {
        this.noticePrice = noticePrice;
    }

    public float getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(float originPrice) {
        this.originPrice = originPrice;
    }

    public long getSkuid() {
        return skuid;
    }

    public void setSkuid(long skuid) {
        this.skuid = skuid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceOffNotice that = (PriceOffNotice) o;

        if (skuid != that.skuid) return false;
        if (Float.compare(that.originPrice, originPrice) != 0) return false;
        if (Float.compare(that.noticePrice, noticePrice) != 0) return false;
        if (latestPushStatus != that.latestPushStatus) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userid != null ? !userid.equals(that.userid) : that.userid != null) return false;
        return !(createTime != null ? !createTime.equals(that.createTime) : that.createTime != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userid != null ? userid.hashCode() : 0);
        result = 31 * result + (int) (skuid ^ (skuid >>> 32));
        result = 31 * result + (originPrice != +0.0f ? Float.floatToIntBits(originPrice) : 0);
        result = 31 * result + (noticePrice != +0.0f ? Float.floatToIntBits(noticePrice) : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (latestPushStatus ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PriceOffNotice{" +
                "id=" + id +
                ", userid='" + userid + '\'' +
                ", skuid=" + skuid +
                ", originPrice=" + originPrice +
                ", noticePrice=" + noticePrice +
                ", createTime=" + createTime +
                ", latestPushStatus=" + latestPushStatus +
                '}';
    }
}
