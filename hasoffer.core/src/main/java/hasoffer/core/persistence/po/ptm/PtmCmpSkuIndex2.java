package hasoffer.core.persistence.po.ptm;

import hasoffer.base.model.Website;
import hasoffer.base.utils.HexDigestUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.dbm.osql.Identifiable;
import hasoffer.fetch.helper.WebsiteHelper;

import javax.persistence.*;
import java.util.Date;

/**
 * Created on 2016/5/25.
 */
@Entity
public class PtmCmpSkuIndex2 implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long productId;

    @Enumerated(EnumType.STRING)
    private Website website;

    private String sourcePid;
    private String sourceSid;

    private String title;
    private String skuTitle;

    private String skuTitleIndex;
    private String skuUrlIndex;

    private String siteSourceSidIndex;
    private String siteSkuTitleIndex;

    private float price;

    private String url;

    private Date createTime;//索引创建时间
    private Date updateTime;//索引更新时间

    public PtmCmpSkuIndex2() {
        this.id = 0L;
    }

    public PtmCmpSkuIndex2(long id, long productId, Website website, String sourcePid, String sourceSid,
                           String title, String skuTitle, float price, String url) {
        this.id = id;
        this.productId = productId;
        this.website = website;
        this.sourcePid = sourcePid;
        this.sourceSid = sourceSid;
        this.title = title;
        this.skuTitle = skuTitle;
        if (StringUtils.isEmpty(skuTitle)) {
            this.skuTitle = title;
        }
        this.skuTitleIndex = HexDigestUtil.md5(StringUtils.getCleanChars(this.skuTitle));
        this.siteSkuTitleIndex = HexDigestUtil.md5(website.name() + StringUtils.getCleanChars(this.skuTitle));
        this.siteSourceSidIndex = HexDigestUtil.md5(website.name() + this.sourceSid);
        this.price = price;
        this.url = url;
        this.skuUrlIndex = HexDigestUtil.md5(WebsiteHelper.getCleanUrl(website, url));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getSiteSkuTitleIndex() {
        return siteSkuTitleIndex;
    }

    public void setSiteSkuTitleIndex(String siteSkuTitleIndex) {
        this.siteSkuTitleIndex = siteSkuTitleIndex;
    }

    public String getSkuTitle() {
        return skuTitle;
    }

    public void setSkuTitle(String skuTitle) {
        this.skuTitle = skuTitle;
    }

    public String getSkuTitleIndex() {
        return skuTitleIndex;
    }

    public void setSkuTitleIndex(String skuTitleIndex) {
        this.skuTitleIndex = skuTitleIndex;
    }

    public String getSkuUrlIndex() {
        return skuUrlIndex;
    }

    public void setSkuUrlIndex(String skuUrlIndex) {
        this.skuUrlIndex = skuUrlIndex;
    }

    public String getSourcePid() {
        return sourcePid;
    }

    public void setSourcePid(String sourcePid) {
        this.sourcePid = sourcePid;
    }

    public String getSourceSid() {
        return sourceSid;
    }

    public void setSourceSid(String sourceSid) {
        this.sourceSid = sourceSid;
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

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSiteSourceSidIndex() {
        return siteSourceSidIndex;
    }

    public void setSiteSourceSidIndex(String siteSourceSidIndex) {
        this.siteSourceSidIndex = siteSourceSidIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PtmCmpSkuIndex2 that = (PtmCmpSkuIndex2) o;

        if (productId != that.productId) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (website != that.website) return false;
        if (sourcePid != null ? !sourcePid.equals(that.sourcePid) : that.sourcePid != null) return false;
        if (sourceSid != null ? !sourceSid.equals(that.sourceSid) : that.sourceSid != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (skuTitle != null ? !skuTitle.equals(that.skuTitle) : that.skuTitle != null) return false;
        if (skuTitleIndex != null ? !skuTitleIndex.equals(that.skuTitleIndex) : that.skuTitleIndex != null)
            return false;
        if (skuUrlIndex != null ? !skuUrlIndex.equals(that.skuUrlIndex) : that.skuUrlIndex != null) return false;
        if (siteSourceSidIndex != null ? !siteSourceSidIndex.equals(that.siteSourceSidIndex) : that.siteSourceSidIndex != null)
            return false;
        if (siteSkuTitleIndex != null ? !siteSkuTitleIndex.equals(that.siteSkuTitleIndex) : that.siteSkuTitleIndex != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        return !(updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (sourcePid != null ? sourcePid.hashCode() : 0);
        result = 31 * result + (sourceSid != null ? sourceSid.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (skuTitle != null ? skuTitle.hashCode() : 0);
        result = 31 * result + (skuTitleIndex != null ? skuTitleIndex.hashCode() : 0);
        result = 31 * result + (skuUrlIndex != null ? skuUrlIndex.hashCode() : 0);
        result = 31 * result + (siteSourceSidIndex != null ? siteSourceSidIndex.hashCode() : 0);
        result = 31 * result + (siteSkuTitleIndex != null ? siteSkuTitleIndex.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }
}
