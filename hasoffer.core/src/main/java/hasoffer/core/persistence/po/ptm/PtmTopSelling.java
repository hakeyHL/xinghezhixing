package hasoffer.core.persistence.po.ptm;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.bo.enums.TopSellStatus;
import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;

/**
 * Created on 2015/12/29.
 * 用于每天的topselling
 */
@Entity
public class PtmTopSelling implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    private Long id; // 商品ID , 对应 PtmProduct - id

    private Long count;

    @Enumerated(EnumType.STRING)
    private TopSellStatus status = TopSellStatus.WAIT;
    private long lUpdateTime = TimeUtils.now();

    public PtmTopSelling() {
    }

    public PtmTopSelling(long productId, Long count) {
        this.id = productId;
        this.count = count;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public TopSellStatus getStatus() {
        return status;
    }

    public void setStatus(TopSellStatus status) {
        this.status = status;
    }

    public long getlUpdateTime() {
        return lUpdateTime;
    }

    public void setlUpdateTime(long lUpdateTime) {
        this.lUpdateTime = lUpdateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PtmTopSelling that = (PtmTopSelling) o;

        if (lUpdateTime != that.lUpdateTime) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        return status == that.status;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (int) (lUpdateTime ^ (lUpdateTime >>> 32));
        return result;
    }
}
