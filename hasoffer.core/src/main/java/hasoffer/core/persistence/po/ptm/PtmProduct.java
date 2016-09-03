package hasoffer.core.persistence.po.ptm;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;
import java.util.Date;


@Entity
public class PtmProduct implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createTime = TimeUtils.nowDate();
    private Date updateTime;

    private long categoryId;
    private String title;// 标题
    private String tag;

    private String brand;
    private String model;

    private float price;

    private String color;
    private String size;

    @Column(columnDefinition = "longtext")
    private String description;
    private int rating;

    private String sourceSite;
    private String sourceUrl;
    private String sourceId;

    @Column
    private boolean std = true; // 是否标品

    public PtmProduct() {
    }

    public PtmProduct(Long id) {
        this.id = id;
    }

    public PtmProduct(long categoryId, String title, float price,
                      String sourceSite, String sourceUrl, String sourceId) {
        this.categoryId = categoryId;
        this.title = title;
        this.price = price;
        this.sourceId = sourceId;
        this.sourceSite = sourceSite;
        this.sourceUrl = sourceUrl;
    }

    @Deprecated
    public PtmProduct(long categoryId, String title, float price,
                      String description, String color, String size,
                      int rating, String sourceSite, String sourceId) {
        this.categoryId = categoryId;
        this.title = title;
        this.price = price;
        this.description = description;
        this.color = color;
        this.size = size;
        this.rating = rating;
        this.sourceId = sourceId;
        this.sourceSite = sourceSite;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getSourceSite() {
        return sourceSite;
    }

    public void setSourceSite(String sourceSite) {
        this.sourceSite = sourceSite;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isStd() {
        return std;
    }

    public void setStd(boolean std) {
        this.std = std;
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

        PtmProduct product = (PtmProduct) o;

        if (categoryId != product.categoryId) return false;
        if (Float.compare(product.price, price) != 0) return false;
        if (rating != product.rating) return false;
        if (std != product.std) return false;
        if (id != null ? !id.equals(product.id) : product.id != null) return false;
        if (createTime != null ? !createTime.equals(product.createTime) : product.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(product.updateTime) : product.updateTime != null) return false;
        if (title != null ? !title.equals(product.title) : product.title != null) return false;
        if (tag != null ? !tag.equals(product.tag) : product.tag != null) return false;
        if (brand != null ? !brand.equals(product.brand) : product.brand != null) return false;
        if (model != null ? !model.equals(product.model) : product.model != null) return false;
        if (color != null ? !color.equals(product.color) : product.color != null) return false;
        if (size != null ? !size.equals(product.size) : product.size != null) return false;
        if (description != null ? !description.equals(product.description) : product.description != null) return false;
        if (sourceSite != null ? !sourceSite.equals(product.sourceSite) : product.sourceSite != null) return false;
        if (sourceUrl != null ? !sourceUrl.equals(product.sourceUrl) : product.sourceUrl != null) return false;
        return !(sourceId != null ? !sourceId.equals(product.sourceId) : product.sourceId != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) (categoryId ^ (categoryId >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + rating;
        result = 31 * result + (sourceSite != null ? sourceSite.hashCode() : 0);
        result = 31 * result + (sourceUrl != null ? sourceUrl.hashCode() : 0);
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        result = 31 * result + (std ? 1 : 0);
        return result;
    }
}
