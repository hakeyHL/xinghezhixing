package hasoffer.fetch.model;

import hasoffer.base.model.Website;

/**
 * Date : 2016/2/29
 * Function :
 */
public class OriFetchedProduct {

    private String sourceSid;
    private String sourcePid;
    private Website website;

    private String title;
    private String subTitle;

    private String imageUrl;

    private ProductStatus productStatus;

    private float price;

    private String url;
    private String deeplink;
    private String pageHtml;

    public OriFetchedProduct() {
    }

    public OriFetchedProduct(String sourceId, Website website, String title, String imageUrl,
                             ProductStatus productStatus, float price, String url, String deeplink) {
        this.sourceSid = sourceId;
        this.website = website;
        this.title = title;
        this.imageUrl = imageUrl;
        this.productStatus = productStatus;
        this.price = price;
        this.url = url;
        this.deeplink = deeplink;
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

    public String getPageHtml() {
        return pageHtml;
    }

    public void setPageHtml(String pageHtml) {
        this.pageHtml = pageHtml;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public String getSourceSid() {
        return sourceSid;
    }

    public void setSourceSid(String sourceSid) {
        this.sourceSid = sourceSid;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @Override
    public String toString() {
        return "SummaryProduct{" +
                "imageUrl='" + imageUrl + '\'' +
                ", sourceSid='" + sourceSid + '\'' +
                ", sourcePid='" + sourcePid + '\'' +
                ", website=" + website +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", productStatus=" + productStatus +
                ", price=" + price +
                ", url='" + url + '\'' +
                ", pageHtml='" + pageHtml + '\'' +
                '}';
    }
}
