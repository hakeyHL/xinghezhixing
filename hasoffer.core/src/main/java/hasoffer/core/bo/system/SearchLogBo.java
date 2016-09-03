package hasoffer.core.bo.system;

/**
 * Date : 2016/1/14
 * Function :
 */
public class SearchLogBo {

    String keyword;
    String site;
    long productId;
    long cmpSkuId;
    float price;
    long category;
    String brand;
    String sourceId;

    public SearchLogBo(String sourceId, String keyword, String brand, String site,
                       long category, long productId, long cmpSkuId, float price) {
        this.sourceId = sourceId;
        this.keyword = keyword;
        this.site = site;
        this.productId = productId;
        this.price = price;
        this.category = category;
        this.cmpSkuId = cmpSkuId;
        this.brand = brand;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getCmpSkuId() {
        return cmpSkuId;
    }

    public void setCmpSkuId(long cmpSkuId) {
        this.cmpSkuId = cmpSkuId;
    }
}
