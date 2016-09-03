package hasoffer.core.persistence.po.stat;

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Date : 2016/4/11
 * Function :
 */
@Entity
public class StatUserBuy implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    private Long id; // ptmproduct id

    private int count;

    private Date lastBuyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getLastBuyTime() {
        return lastBuyTime;
    }

    public void setLastBuyTime(Date lastBuyTime) {
        this.lastBuyTime = lastBuyTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatUserBuy that = (StatUserBuy) o;

        if (count != that.count) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(lastBuyTime != null ? !lastBuyTime.equals(that.lastBuyTime) : that.lastBuyTime != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + count;
        result = 31 * result + (lastBuyTime != null ? lastBuyTime.hashCode() : 0);
        return result;
    }
}
