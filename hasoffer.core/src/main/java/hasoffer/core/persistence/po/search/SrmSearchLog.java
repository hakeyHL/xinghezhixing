package hasoffer.core.persistence.po.search;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.Identifiable;
import hasoffer.core.persistence.enums.SearchPrecise;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

/**
 * Created on 2015/12/29.
 */
@Entity
public class SrmSearchLog implements Identifiable<String> {

    @Id
    @Column(unique = true, nullable = false)
    private String id;

    private String site;
    private String keyword;
    private String brand;
    private float price;

    private int count = 1;// 被搜索次数
    private long category = 0;
    private long ptmProductId;
    private long ptmCmpSkuId;

    private String sourceId;

    private Date createTime = TimeUtils.nowDate();
    private Date updateTime = TimeUtils.nowDate();

    @ColumnDefault(value = "0")
    private long lUpdateTime = 0L;// 默认值设为0
    private Date manualSetTime;

    @Enumerated(EnumType.STRING)
    private SearchPrecise precise;//  NOCHECK,MANUALSET

    public SrmSearchLog() {
    }

    public SrmSearchLog(String id, String site, String sourceId,
                        String keyword, String brand, float price, long category,
                        long ptmProductId, long ptmCmpSkuId) {
        this.id = id;
        this.site = site;
        this.sourceId = sourceId;
        this.keyword = keyword;
        this.price = price;
        this.brand = brand;
        this.category = category;
        this.ptmProductId = ptmProductId;
        this.ptmCmpSkuId = ptmCmpSkuId;
        this.precise = SearchPrecise.NOCHECK;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getPtmProductId() {
        return ptmProductId;
    }

    public void setPtmProductId(long ptmProductId) {
        this.ptmProductId = ptmProductId;
    }

    public SearchPrecise getPrecise() {
        return precise;
    }

    public void setPrecise(SearchPrecise precise) {
        this.precise = precise;
    }

    public long getPtmCmpSkuId() {
        return ptmCmpSkuId;
    }

    public void setPtmCmpSkuId(long ptmCmpSkuId) {
        this.ptmCmpSkuId = ptmCmpSkuId;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getManualSetTime() {
        return manualSetTime;
    }

    public void setManualSetTime(Date manualSetTime) {
        this.manualSetTime = manualSetTime;
    }

    public String getBrand() {

        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
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

        SrmSearchLog that = (SrmSearchLog) o;

        if (Float.compare(that.price, price) != 0) return false;
        if (count != that.count) return false;
        if (category != that.category) return false;
        if (ptmProductId != that.ptmProductId) return false;
        if (ptmCmpSkuId != that.ptmCmpSkuId) return false;
        if (lUpdateTime != that.lUpdateTime) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (site != null ? !site.equals(that.site) : that.site != null) return false;
        if (keyword != null ? !keyword.equals(that.keyword) : that.keyword != null) return false;
        if (brand != null ? !brand.equals(that.brand) : that.brand != null) return false;
        if (sourceId != null ? !sourceId.equals(that.sourceId) : that.sourceId != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
        if (manualSetTime != null ? !manualSetTime.equals(that.manualSetTime) : that.manualSetTime != null)
            return false;
        return precise == that.precise;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (site != null ? site.hashCode() : 0);
        result = 31 * result + (keyword != null ? keyword.hashCode() : 0);
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + count;
        result = 31 * result + (int) (category ^ (category >>> 32));
        result = 31 * result + (int) (ptmProductId ^ (ptmProductId >>> 32));
        result = 31 * result + (int) (ptmCmpSkuId ^ (ptmCmpSkuId >>> 32));
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) (lUpdateTime ^ (lUpdateTime >>> 32));
        result = 31 * result + (manualSetTime != null ? manualSetTime.hashCode() : 0);
        result = 31 * result + (precise != null ? precise.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SrmSearchLog{" +
                "site='" + site + '\'' +
                ", keyword='" + keyword + '\'' +
                ", brand='" + brand + '\'' +
                '}';
    }
}
