package hasoffer.core.persistence.po.stat;

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Date : 2016/1/22
 * Function : 统计每天价格更新了多少数据
 */
@Entity
public class StatSkuPriceUpdateResult implements Identifiable<String> {

    @Id
    @Column(unique = true, nullable = false)
    private String id;

    private long count;

    public StatSkuPriceUpdateResult() {
    }

    public StatSkuPriceUpdateResult(String ymd, long count) {
        this.id = ymd;
        this.count = count;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatSkuPriceUpdateResult that = (StatSkuPriceUpdateResult) o;

        if (count != that.count) return false;
        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (count ^ (count >>> 32));
        return result;
    }
}
