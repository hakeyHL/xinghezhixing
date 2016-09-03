package hasoffer.core.bo.product;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmImage;
import hasoffer.core.persistence.po.ptm.PtmProduct;

import java.util.Date;
import java.util.List;

/**
 * Date : 2016/2/26
 * Function :
 */
public class ProductBo {

    private Long id;
    private Date createTime = TimeUtils.nowDate();

    private long categoryId;
    private String title;// 标题
    private String tag;
    private float price;

    private String sourceSite;
    private String sourceUrl;
    private String sourceId;

    private List<PtmCmpSku> cmpSkus;
    private PtmImage masterImage;

    public ProductBo(PtmProduct product, List<PtmCmpSku> cmpSkus, PtmImage masterImage) {
        this.id = product.getId();
        this.createTime = product.getCreateTime();
        this.categoryId = product.getCategoryId();
        this.title = product.getTitle();
        this.tag = product.getTag();
        this.price = product.getPrice();
        this.sourceSite = product.getSourceSite();
        this.sourceUrl = product.getSourceUrl();
        this.sourceId = product.getSourceId();
        this.cmpSkus = cmpSkus;
        this.masterImage = masterImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSourceSite() {
        return sourceSite;
    }

    public void setSourceSite(String sourceSite) {
        this.sourceSite = sourceSite;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public List<PtmCmpSku> getCmpSkus() {
        return cmpSkus;
    }

    public void setCmpSkus(List<PtmCmpSku> cmpSkus) {
        this.cmpSkus = cmpSkus;
    }

    public PtmImage getMasterImage() {
        return masterImage;
    }

    public void setMasterImage(PtmImage masterImage) {
        this.masterImage = masterImage;
    }
}
