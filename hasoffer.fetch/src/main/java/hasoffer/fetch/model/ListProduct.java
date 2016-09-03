package hasoffer.fetch.model;

import hasoffer.base.model.Website;

import java.io.Serializable;

/**
 * Created on 2016/3/3.
 */
public class ListProduct implements Serializable {

    private long categoryId;
    private String sourceId;
    private String url;
    private String imageUrl;
    private String title;
    private String subTitle;
    private float price;
    private Website website;
    private ProductStatus status;

    public ListProduct() {
    }

    public ListProduct(long categoryId, String sourceId, String url, String imageUrl,
                       String title, float price, Website website, ProductStatus status) {
        this.categoryId = categoryId;
        this.sourceId = sourceId;
        this.url = url;
        this.imageUrl = imageUrl;
        this.title = title;
        this.price = price;
        this.website = website;
        this.status = status;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListProduct that = (ListProduct) o;

        if (categoryId != that.categoryId) return false;
        if (Float.compare(that.price, price) != 0) return false;
        if (sourceId != null ? !sourceId.equals(that.sourceId) : that.sourceId != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return website == that.website;

    }

    @Override
    public int hashCode() {
        int result = (int) (categoryId ^ (categoryId >>> 32));
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ListProduct{" +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", url='" + url + '\'' +
                "sourceId='" + sourceId + '\'' +
                ", website=" + website +
                ", status=" + status +
                '}';
    }
}