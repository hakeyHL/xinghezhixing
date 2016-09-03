package hasoffer.core.persistence.po.ptm;

import hasoffer.base.enums.IndexNeed;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.Identifiable;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

/**
 * Created on 2016/8/1.
 * 该表用于自然流量获取，表结果与PtmCmpSku表结构一致
 */
@Entity
public class PtmCmpSku2 implements Identifiable<Long> {


    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long productId; //该表中，productid值为0

    private Long categoryId;
    @Enumerated(EnumType.STRING)
    private Website website;
    private String seller;

    private String skuTitle;//
    private String title;

    private float price;

    private float cashBack = -1;

    private String rating;

    private String imagePath;
    private String smallImagePath;
    private String bigImagePath;
    private String oriImageUrl;

    @Column(columnDefinition = "text")
    private String deeplink;
    @Column(columnDefinition = "text")
    private String url;
    @Column(columnDefinition = "text")
    private String oriUrl;

    private String color;
    private String size;

    private Date updateTime = TimeUtils.nowDate();
    private Date createTime;

    private Date titleUpdateTime;

    private boolean checked = false;
    private boolean failLoadImage = false;

    private String sourcePid; // 源网站的商品id
    private String sourceSid; // 源网站的商品sku id

    @Enumerated(EnumType.STRING)
    private IndexNeed indexNeed = IndexNeed.NO;
    @Enumerated(EnumType.STRING)
    private SkuStatus status = SkuStatus.ONSALE;

    @ColumnDefault(value = "0")
    private long commentsNumber;
    @ColumnDefault(value = "0")
    private int ratings;
    @ColumnDefault(value = "-1")
    private float shipping = -1;
    private String supportPayMethod;
    private String deliveryTime;
    @ColumnDefault(value = "0")
    private int returnDays;

    public String getBigImagePath() {
        return bigImagePath;
    }

    public void setBigImagePath(String bigImagePath) {
        this.bigImagePath = bigImagePath;
    }

    public float getCashBack() {
        return cashBack;
    }

    public void setCashBack(float cashBack) {
        this.cashBack = cashBack;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(long commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDeeplink() {
        return deeplink;
    }

    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public boolean isFailLoadImage() {
        return failLoadImage;
    }

    public void setFailLoadImage(boolean failLoadImage) {
        this.failLoadImage = failLoadImage;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public IndexNeed getIndexNeed() {
        return indexNeed;
    }

    public void setIndexNeed(IndexNeed indexNeed) {
        this.indexNeed = indexNeed;
    }

    public String getOriImageUrl() {
        return oriImageUrl;
    }

    public void setOriImageUrl(String oriImageUrl) {
        this.oriImageUrl = oriImageUrl;
    }

    public String getOriUrl() {
        return oriUrl;
    }

    public void setOriUrl(String oriUrl) {
        this.oriUrl = oriUrl;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public int getReturnDays() {
        return returnDays;
    }

    public void setReturnDays(int returnDays) {
        this.returnDays = returnDays;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public float getShipping() {
        return shipping;
    }

    public void setShipping(float shipping) {
        this.shipping = shipping;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSkuTitle() {
        return skuTitle;
    }

    public void setSkuTitle(String skuTitle) {
        this.skuTitle = skuTitle;
    }

    public String getSmallImagePath() {
        return smallImagePath;
    }

    public void setSmallImagePath(String smallImagePath) {
        this.smallImagePath = smallImagePath;
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

    public SkuStatus getStatus() {
        return status;
    }

    public void setStatus(SkuStatus status) {
        this.status = status;
    }

    public String getSupportPayMethod() {
        return supportPayMethod;
    }

    public void setSupportPayMethod(String supportPayMethod) {
        this.supportPayMethod = supportPayMethod;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTitleUpdateTime() {
        return titleUpdateTime;
    }

    public void setTitleUpdateTime(Date titleUpdateTime) {
        this.titleUpdateTime = titleUpdateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PtmCmpSku2 that = (PtmCmpSku2) o;

        if (productId != that.productId) return false;
        if (Float.compare(that.price, price) != 0) return false;
        if (Float.compare(that.cashBack, cashBack) != 0) return false;
        if (checked != that.checked) return false;
        if (failLoadImage != that.failLoadImage) return false;
        if (commentsNumber != that.commentsNumber) return false;
        if (ratings != that.ratings) return false;
        if (Float.compare(that.shipping, shipping) != 0) return false;
        if (returnDays != that.returnDays) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null) return false;
        if (website != that.website) return false;
        if (seller != null ? !seller.equals(that.seller) : that.seller != null) return false;
        if (skuTitle != null ? !skuTitle.equals(that.skuTitle) : that.skuTitle != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (rating != null ? !rating.equals(that.rating) : that.rating != null) return false;
        if (imagePath != null ? !imagePath.equals(that.imagePath) : that.imagePath != null) return false;
        if (smallImagePath != null ? !smallImagePath.equals(that.smallImagePath) : that.smallImagePath != null)
            return false;
        if (bigImagePath != null ? !bigImagePath.equals(that.bigImagePath) : that.bigImagePath != null) return false;
        if (oriImageUrl != null ? !oriImageUrl.equals(that.oriImageUrl) : that.oriImageUrl != null) return false;
        if (deeplink != null ? !deeplink.equals(that.deeplink) : that.deeplink != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (oriUrl != null ? !oriUrl.equals(that.oriUrl) : that.oriUrl != null) return false;
        if (color != null ? !color.equals(that.color) : that.color != null) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (titleUpdateTime != null ? !titleUpdateTime.equals(that.titleUpdateTime) : that.titleUpdateTime != null)
            return false;
        if (sourcePid != null ? !sourcePid.equals(that.sourcePid) : that.sourcePid != null) return false;
        if (sourceSid != null ? !sourceSid.equals(that.sourceSid) : that.sourceSid != null) return false;
        if (indexNeed != that.indexNeed) return false;
        if (status != that.status) return false;
        if (supportPayMethod != null ? !supportPayMethod.equals(that.supportPayMethod) : that.supportPayMethod != null)
            return false;
        return !(deliveryTime != null ? !deliveryTime.equals(that.deliveryTime) : that.deliveryTime != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (seller != null ? seller.hashCode() : 0);
        result = 31 * result + (skuTitle != null ? skuTitle.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + (cashBack != +0.0f ? Float.floatToIntBits(cashBack) : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (imagePath != null ? imagePath.hashCode() : 0);
        result = 31 * result + (smallImagePath != null ? smallImagePath.hashCode() : 0);
        result = 31 * result + (bigImagePath != null ? bigImagePath.hashCode() : 0);
        result = 31 * result + (oriImageUrl != null ? oriImageUrl.hashCode() : 0);
        result = 31 * result + (deeplink != null ? deeplink.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (oriUrl != null ? oriUrl.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (titleUpdateTime != null ? titleUpdateTime.hashCode() : 0);
        result = 31 * result + (checked ? 1 : 0);
        result = 31 * result + (failLoadImage ? 1 : 0);
        result = 31 * result + (sourcePid != null ? sourcePid.hashCode() : 0);
        result = 31 * result + (sourceSid != null ? sourceSid.hashCode() : 0);
        result = 31 * result + (indexNeed != null ? indexNeed.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (int) (commentsNumber ^ (commentsNumber >>> 32));
        result = 31 * result + ratings;
        result = 31 * result + (shipping != +0.0f ? Float.floatToIntBits(shipping) : 0);
        result = 31 * result + (supportPayMethod != null ? supportPayMethod.hashCode() : 0);
        result = 31 * result + (deliveryTime != null ? deliveryTime.hashCode() : 0);
        result = 31 * result + returnDays;
        return result;
    }
}
