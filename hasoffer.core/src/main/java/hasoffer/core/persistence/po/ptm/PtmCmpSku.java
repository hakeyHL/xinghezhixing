package hasoffer.core.persistence.po.ptm;

import hasoffer.base.enums.IndexNeed;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.Identifiable;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.sites.mysmartprice.model.MySmartPriceCmpSku;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PtmCmpSku implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long productId; // PtmProduct # id

    private Long categoryId;
    @ColumnDefault(value = "0")
    private Long categoryId2 = 0L;//保存flipkart的sku的三级或者末尾级别的类目

    @Enumerated(EnumType.STRING)
    private Website website;
    private String seller;

    private String skuTitle;// 带商品的color，size属性的
    private String title;

    private float price;

    private float cashBack = -1;

    private String rating;

    private String imagePath; // 下载后的图片路径
    private String smallImagePath;
    private String bigImagePath;
    private String oriImageUrl;// 原图片url

    @Column(columnDefinition = "text")
    private String deeplink;
    @Column(columnDefinition = "text")
    private String url;
    @Column(columnDefinition = "text")
    private String oriUrl; //原始URL 可能带有其他网站的联盟信息

    private String color;
    private String size;

    private Date updateTime = TimeUtils.nowDate();
    private Date createTime;//该条sku记录的创建时间

    private Date titleUpdateTime;

    private boolean checked = false;//人工审核标志位
    private boolean failLoadImage = false; //下载图片是否失败

    private String sourcePid; // 源网站的商品id
    private String sourceSid; // 源网站的商品sku id

    @Enumerated(EnumType.STRING)
    private IndexNeed indexNeed = IndexNeed.NO;
    @Enumerated(EnumType.STRING)
    private SkuStatus status = SkuStatus.ONSALE;

    @ColumnDefault(value = "0")
    private long commentsNumber = 0;//评论数
    @ColumnDefault(value = "0")
    private int ratings = 0;//星级，存放百分比的整数位如 88即表示88%
    @ColumnDefault(value = "-1")
    private float shipping = -1;//邮费，默认值为-1,free shipping时值为0
    private String supportPayMethod;//支付方式  ex：COD,EMI,...,
    private String deliveryTime;//送达时间 ex: 1-3
    @ColumnDefault(value = "0")
    private int returnDays = 0;

    private String brand;//品牌
    private String model;//型号

    public PtmCmpSku() {
    }

    public PtmCmpSku(long productId, float price, String url) {
        this.productId = productId;
        this.price = price;
        this.oriUrl = url;
        this.url = url;
        this.website = WebsiteHelper.getWebSite(url);

        this.sourcePid = WebsiteHelper.getProductIdFromUrl(website, url);
        this.sourceSid = WebsiteHelper.getSkuIdFromUrl(website, url);
    }

    public PtmCmpSku(long productId, float price, String url, String title, String imageUrl) {
        this(productId, price, url);
        this.title = title;
        this.oriImageUrl = imageUrl;
        this.skuTitle = title;
    }

    public PtmCmpSku(long productId, float price, String url, String title, String imageUrl, String deeplink) {
        this(productId, price, url, title, imageUrl);
        this.deeplink = deeplink;
    }

    public PtmCmpSku(long productId, MySmartPriceCmpSku cmpSku) {
        this.productId = productId;
        this.rating = cmpSku.getRating();
        this.price = cmpSku.getPrice();
        this.oriUrl = cmpSku.getUrl();
        this.url = WebsiteHelper.getRealUrl(oriUrl);
        this.color = cmpSku.getColor();
        this.size = cmpSku.getSize();
        this.seller = cmpSku.getSeller();
        this.website = WebsiteHelper.getWebSite(cmpSku.getUrl());
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getOriUrl() {
        return oriUrl;
    }

    public void setOriUrl(String oriUrl) {
        this.oriUrl = oriUrl;
    }

    public SkuStatus getStatus() {
        return status;
    }

    public void setStatus(SkuStatus status) {
        this.status = status;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getOriImageUrl() {
        return oriImageUrl;
    }

    public void setOriImageUrl(String oriImageUrl) {
        this.oriImageUrl = oriImageUrl;
    }

    public String getSkuTitle() {
        return skuTitle;
    }

    public void setSkuTitle(String skuTitle) {
        this.skuTitle = skuTitle;
    }

    public String getDeeplink() {
        return deeplink;
    }

    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
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

    public Date getTitleUpdateTime() {
        return titleUpdateTime;
    }

    public void setTitleUpdateTime(Date titleUpdateTime) {
        this.titleUpdateTime = titleUpdateTime;
    }

    public IndexNeed getIndexNeed() {
        return indexNeed;
    }

    public void setIndexNeed(IndexNeed indexNeed) {
        this.indexNeed = indexNeed;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSmallImagePath() {
        return smallImagePath;
    }

    public void setSmallImagePath(String smallImagePath) {
        this.smallImagePath = smallImagePath;
    }

    public String getBigImagePath() {
        return bigImagePath;
    }

    public void setBigImagePath(String bigImagePath) {
        this.bigImagePath = bigImagePath;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isFailLoadImage() {
        return failLoadImage;
    }

    public void setFailLoadImage(boolean failLoadImage) {
        this.failLoadImage = failLoadImage;
    }

    public long getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(long commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
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

    public float getShipping() {
        return shipping;
    }

    public void setShipping(float shipping) {
        this.shipping = shipping;
    }

    public String getSupportPayMethod() {
        return supportPayMethod;
    }

    public void setSupportPayMethod(String supportPayMethod) {
        this.supportPayMethod = supportPayMethod;
    }

    public float getCashBack() {
        return cashBack;
    }

    public void setCashBack(float cashBack) {
        this.cashBack = cashBack;
    }

    public Long getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(Long categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PtmCmpSku sku = (PtmCmpSku) o;

        if (productId != sku.productId) return false;
        if (Float.compare(sku.price, price) != 0) return false;
        if (Float.compare(sku.cashBack, cashBack) != 0) return false;
        if (checked != sku.checked) return false;
        if (failLoadImage != sku.failLoadImage) return false;
        if (commentsNumber != sku.commentsNumber) return false;
        if (ratings != sku.ratings) return false;
        if (Float.compare(sku.shipping, shipping) != 0) return false;
        if (returnDays != sku.returnDays) return false;
        if (id != null ? !id.equals(sku.id) : sku.id != null) return false;
        if (categoryId != null ? !categoryId.equals(sku.categoryId) : sku.categoryId != null) return false;
        if (categoryId2 != null ? !categoryId2.equals(sku.categoryId2) : sku.categoryId2 != null) return false;
        if (website != sku.website) return false;
        if (seller != null ? !seller.equals(sku.seller) : sku.seller != null) return false;
        if (skuTitle != null ? !skuTitle.equals(sku.skuTitle) : sku.skuTitle != null) return false;
        if (title != null ? !title.equals(sku.title) : sku.title != null) return false;
        if (rating != null ? !rating.equals(sku.rating) : sku.rating != null) return false;
        if (imagePath != null ? !imagePath.equals(sku.imagePath) : sku.imagePath != null) return false;
        if (smallImagePath != null ? !smallImagePath.equals(sku.smallImagePath) : sku.smallImagePath != null)
            return false;
        if (bigImagePath != null ? !bigImagePath.equals(sku.bigImagePath) : sku.bigImagePath != null) return false;
        if (oriImageUrl != null ? !oriImageUrl.equals(sku.oriImageUrl) : sku.oriImageUrl != null) return false;
        if (deeplink != null ? !deeplink.equals(sku.deeplink) : sku.deeplink != null) return false;
        if (url != null ? !url.equals(sku.url) : sku.url != null) return false;
        if (oriUrl != null ? !oriUrl.equals(sku.oriUrl) : sku.oriUrl != null) return false;
        if (color != null ? !color.equals(sku.color) : sku.color != null) return false;
        if (size != null ? !size.equals(sku.size) : sku.size != null) return false;
        if (updateTime != null ? !updateTime.equals(sku.updateTime) : sku.updateTime != null) return false;
        if (createTime != null ? !createTime.equals(sku.createTime) : sku.createTime != null) return false;
        if (titleUpdateTime != null ? !titleUpdateTime.equals(sku.titleUpdateTime) : sku.titleUpdateTime != null)
            return false;
        if (sourcePid != null ? !sourcePid.equals(sku.sourcePid) : sku.sourcePid != null) return false;
        if (sourceSid != null ? !sourceSid.equals(sku.sourceSid) : sku.sourceSid != null) return false;
        if (indexNeed != sku.indexNeed) return false;
        if (status != sku.status) return false;
        if (supportPayMethod != null ? !supportPayMethod.equals(sku.supportPayMethod) : sku.supportPayMethod != null)
            return false;
        if (deliveryTime != null ? !deliveryTime.equals(sku.deliveryTime) : sku.deliveryTime != null) return false;
        if (brand != null ? !brand.equals(sku.brand) : sku.brand != null) return false;
        return !(model != null ? !model.equals(sku.model) : sku.model != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        result = 31 * result + (categoryId2 != null ? categoryId2.hashCode() : 0);
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
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        return result;
    }
}
