package hasoffer.core.persistence.po.search;

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;

/**
 * Created on 2015/12/29.
 * 1 记录每天被搜索到的商品
 * 2 统计匹配比价数量
 * 3 保存每天被搜索次数最多的20个商品 - top selling
 */
@Entity
public class SrmProductSearchCount implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long productId;

    private String ymd;//日期

    private Long count;
    private int skuCount;//sku 的数量

    public SrmProductSearchCount() {
    }

    public SrmProductSearchCount(String ymd, long productId, Long count, int skuCount) {
        this.productId = productId;
        this.ymd = ymd;
        this.count = count;
        this.skuCount = skuCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public int getSkuCount() {
        return skuCount;
    }

    public void setSkuCount(int skuCount) {
        this.skuCount = skuCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SrmProductSearchCount that = (SrmProductSearchCount) o;

        if (productId != that.productId) return false;
        if (skuCount != that.skuCount) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (ymd != null ? !ymd.equals(that.ymd) : that.ymd != null) return false;
        return !(count != null ? !count.equals(that.count) : that.count != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + (ymd != null ? ymd.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + skuCount;
        return result;
    }
}
