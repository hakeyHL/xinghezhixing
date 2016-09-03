package hasoffer.core.persistence.po.ptm;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PtmStdSku implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long stdProId; // PtmStdProduct # id

    private String title;// 带商品的color，size属性的

    private float refPrice; // 参考价格

    private Date createTime;//该条sku记录的创建时间

    private PtmStdSku() {
        this.createTime = TimeUtils.nowDate();
    }

    public PtmStdSku(long stdProId, String title, float refPrice) {
        this();
        this.stdProId = stdProId;
        this.title = title;
        this.refPrice = refPrice;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public long getStdProId() {
        return stdProId;
    }

    public void setStdProId(long stdProId) {
        this.stdProId = stdProId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getRefPrice() {
        return refPrice;
    }

    public void setRefPrice(float refPrice) {
        this.refPrice = refPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PtmStdSku ptmStdSku = (PtmStdSku) o;

        if (stdProId != ptmStdSku.stdProId) return false;
        if (Float.compare(ptmStdSku.refPrice, refPrice) != 0) return false;
        if (id != null ? !id.equals(ptmStdSku.id) : ptmStdSku.id != null) return false;
        if (title != null ? !title.equals(ptmStdSku.title) : ptmStdSku.title != null) return false;
        return !(createTime != null ? !createTime.equals(ptmStdSku.createTime) : ptmStdSku.createTime != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (stdProId ^ (stdProId >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (refPrice != +0.0f ? Float.floatToIntBits(refPrice) : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}
