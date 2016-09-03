package hasoffer.fetch.sites.shopclues.model;

/**
 * Created on 2016/3/3.
 */
public class ShopCluesProduct {

    private long categoryId;
    private String sourceId;
    private String url;
    private String imageUrl;
    private String title;
    private float price;

    public ShopCluesProduct() {
    }

    public ShopCluesProduct(long categoryId, String sourceId, String url, String imageUrl, String title, float price) {
        this.categoryId = categoryId;
        this.sourceId = sourceId;
        this.url = url;
        this.imageUrl = imageUrl;
        this.title = title;
        this.price = price;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
