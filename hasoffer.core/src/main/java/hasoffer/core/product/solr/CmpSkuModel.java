package hasoffer.core.product.solr;

import hasoffer.base.model.SkuStatus;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.data.solr.IIdentifiable;

/**
 * Date : 2016/5/19
 * Function :
 */
public class CmpSkuModel implements IIdentifiable<Long> {

    private Long id;
    private long productId; // PtmProduct # id

    private String website;

    private String skuTitle;// 带商品的color，size属性的
    private String title;
    private float price;

    private String imagePath; // 下载后的图片路径
    private String oriImageUrl;// 原图片url

    private String deeplink;
    private String url;
    private String oriUrl; //原始URL 可能带有其他网站的联盟信息

    private String color;
    private String size;

    private String sourcePid; // 源网站的商品id
    private String sourceSid; // 源网站的商品sku id

    private SkuStatus status = SkuStatus.ONSALE;

    public CmpSkuModel() {
    }

    public CmpSkuModel(PtmCmpSku cmpSku) {
        this.id = cmpSku.getId();
        this.productId = cmpSku.getProductId();
        this.website = cmpSku.getWebsite() != null ? cmpSku.getWebsite().name() : "";
        this.skuTitle = cmpSku.getSkuTitle();
        this.title = cmpSku.getTitle();
        this.price = cmpSku.getPrice();
        this.imagePath = cmpSku.getImagePath();
        this.oriImageUrl = cmpSku.getOriImageUrl();
        this.deeplink = cmpSku.getDeeplink();
        this.url = cmpSku.getUrl();
        this.oriUrl = cmpSku.getOriUrl();
        this.color = cmpSku.getColor();
        this.size = cmpSku.getSize();
        this.sourcePid = cmpSku.getSourcePid();
        this.sourceSid = cmpSku.getSourceSid();
        this.status = cmpSku.getStatus();
    }

    @Override
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSkuTitle() {
        return skuTitle;
    }

    public void setSkuTitle(String skuTitle) {
        this.skuTitle = skuTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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

    public String getDeeplink() {
        return deeplink;
    }

    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOriUrl() {
        return oriUrl;
    }

    public void setOriUrl(String oriUrl) {
        this.oriUrl = oriUrl;
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
}
