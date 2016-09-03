package hasoffer.core.persistence.aws;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.aws.converter.SkuStatusTypeConverter;
import hasoffer.core.persistence.dbm.aws.converter.WebsiteTypeConverter;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Date;

/**
 * Date : 2016/5/1
 * Function :
 */
@DynamoDBTable(tableName = "AwsSummaryProduct")
public class AwsSummaryProduct {

    @DynamoDBHashKey(attributeName = "id")
    private long id;

    @DynamoDBMarshalling(marshallerClass = WebsiteTypeConverter.class)
    private Website website;
    private String url;
    private String sourceId;// source-sku-id

    private String title;
    private String subTitle;

    private float price;

    private String imageUrl;
    @DynamoDBMarshalling(marshallerClass = SkuStatusTypeConverter.class)
    private SkuStatus skuStatus;

    private long lCreateTime;
    private Date createTime;

    private long lUpdateTime;
    private Date updateTime;

    @PersistenceConstructor
    public AwsSummaryProduct() {
        this.lCreateTime = TimeUtils.now();
        this.createTime = TimeUtils.nowDate();
        this.lUpdateTime = lCreateTime;
        this.updateTime = createTime;
    }

    public AwsSummaryProduct(long id, Website website, String url, String sourceId,
                             String title, String subTitle, float price, String imageUrl,
                             SkuStatus skuStatus) {
        this();
        this.id = id;
        this.website = website;
        this.url = url;
        this.sourceId = sourceId;
        this.title = title;
        this.price = price;
        this.subTitle = subTitle;
        this.imageUrl = imageUrl;
        this.skuStatus = skuStatus;
    }

    public AwsSummaryProduct(PtmCmpSku cmpSku) {
        this(cmpSku.getId(), cmpSku.getWebsite(), cmpSku.getUrl(),
                cmpSku.getSourcePid(), cmpSku.getTitle(), cmpSku.getSkuTitle(),
                cmpSku.getPrice(), cmpSku.getOriImageUrl(), cmpSku.getStatus());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getlCreateTime() {
        return lCreateTime;
    }

    public void setlCreateTime(long lCreateTime) {
        this.lCreateTime = lCreateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getlUpdateTime() {
        return lUpdateTime;
    }

    public void setlUpdateTime(long lUpdateTime) {
        this.lUpdateTime = lUpdateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public SkuStatus getSkuStatus() {
        return skuStatus;
    }

    public void setSkuStatus(SkuStatus skuStatus) {
        this.skuStatus = skuStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AwsSummaryProduct that = (AwsSummaryProduct) o;

        if (id != that.id) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (lCreateTime != that.lCreateTime) return false;
        if (lUpdateTime != that.lUpdateTime) return false;
        if (website != that.website) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (sourceId != null ? !sourceId.equals(that.sourceId) : that.sourceId != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (subTitle != null ? !subTitle.equals(that.subTitle) : that.subTitle != null) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) return false;
        if (skuStatus != that.skuStatus) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        return !(updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (subTitle != null ? subTitle.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (skuStatus != null ? skuStatus.hashCode() : 0);
        result = 31 * result + (int) (lCreateTime ^ (lCreateTime >>> 32));
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (int) (lUpdateTime ^ (lUpdateTime >>> 32));
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AwsSummaryProduct{" +
                "id=" + id +
                ", website=" + website +
                ", url='" + url + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", skuStatus=" + skuStatus +
                ", lCreateTime=" + lCreateTime +
                ", createTime=" + createTime +
                ", lUpdateTime=" + lUpdateTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
