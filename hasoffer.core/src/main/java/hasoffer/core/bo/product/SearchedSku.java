package hasoffer.core.bo.product;

import hasoffer.base.model.Website;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.model.ProductStatus;

/**
 * Date : 2016/5/14
 * Function :
 */
public class SearchedSku {

    private Website website;

    private String title;
    private float titleScore; // 给title 打的分
    private float price;
    private float priceScore;

    private String sourceId;
    private String url;
    private String imageUrl;

    private ProductStatus status;

    public SearchedSku(Website website, String title, float titleScore, float price, float priceScore,
                       String sourceId, String url, String imageUrl, ProductStatus status) {
        this.website = website;
        this.title = title;
        this.titleScore = titleScore;
        this.price = price;
        this.priceScore = priceScore;
        this.sourceId = sourceId;
        this.url = WebsiteHelper.getCleanUrl(website, url);
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public float getTitleScore() {
        return titleScore;
    }

    public void setTitleScore(float titleScore) {
        this.titleScore = titleScore;
    }

    public float getPriceScore() {
        return priceScore;
    }

    public void setPriceScore(float priceScore) {
        this.priceScore = priceScore;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
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

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
