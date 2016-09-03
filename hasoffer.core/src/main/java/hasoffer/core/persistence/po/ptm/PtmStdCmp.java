package hasoffer.core.persistence.po.ptm;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PtmStdCmp implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long stdSkuId; // ptm_std_sku # id

    private String title;// 带商品的color，size属性的

    private String imageUrl;
    private String oriImagePath;
    private String bigImagePath;
    private String smallImagePath;

    private String url;

    private float price;

    private Date updateTime = TimeUtils.nowDate();
    private Date createTime;//该条sku记录的创建时间

    public PtmStdCmp() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public long getStdSkuId() {
        return stdSkuId;
    }

    public void setStdSkuId(long stdSkuId) {
        this.stdSkuId = stdSkuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOriImagePath() {
        return oriImagePath;
    }

    public void setOriImagePath(String oriImagePath) {
        this.oriImagePath = oriImagePath;
    }

    public String getBigImagePath() {
        return bigImagePath;
    }

    public void setBigImagePath(String bigImagePath) {
        this.bigImagePath = bigImagePath;
    }

    public String getSmallImagePath() {
        return smallImagePath;
    }

    public void setSmallImagePath(String smallImagePath) {
        this.smallImagePath = smallImagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PtmStdCmp ptmStdCmp = (PtmStdCmp) o;

        if (stdSkuId != ptmStdCmp.stdSkuId) return false;
        if (Float.compare(ptmStdCmp.price, price) != 0) return false;
        if (id != null ? !id.equals(ptmStdCmp.id) : ptmStdCmp.id != null) return false;
        if (title != null ? !title.equals(ptmStdCmp.title) : ptmStdCmp.title != null) return false;
        if (imageUrl != null ? !imageUrl.equals(ptmStdCmp.imageUrl) : ptmStdCmp.imageUrl != null) return false;
        if (oriImagePath != null ? !oriImagePath.equals(ptmStdCmp.oriImagePath) : ptmStdCmp.oriImagePath != null)
            return false;
        if (bigImagePath != null ? !bigImagePath.equals(ptmStdCmp.bigImagePath) : ptmStdCmp.bigImagePath != null)
            return false;
        if (smallImagePath != null ? !smallImagePath.equals(ptmStdCmp.smallImagePath) : ptmStdCmp.smallImagePath != null)
            return false;
        if (url != null ? !url.equals(ptmStdCmp.url) : ptmStdCmp.url != null) return false;
        if (updateTime != null ? !updateTime.equals(ptmStdCmp.updateTime) : ptmStdCmp.updateTime != null) return false;
        return !(createTime != null ? !createTime.equals(ptmStdCmp.createTime) : ptmStdCmp.createTime != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (stdSkuId ^ (stdSkuId >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (oriImagePath != null ? oriImagePath.hashCode() : 0);
        result = 31 * result + (bigImagePath != null ? bigImagePath.hashCode() : 0);
        result = 31 * result + (smallImagePath != null ? smallImagePath.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}
