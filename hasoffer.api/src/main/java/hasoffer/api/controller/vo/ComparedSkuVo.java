package hasoffer.api.controller.vo;

import hasoffer.base.config.AppConfig;
import hasoffer.base.enums.HasofferRegion;
import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.utils.ImageUtil;
import hasoffer.fetch.helper.WebsiteHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created on 2015/12/17.
 */
public class ComparedSkuVo {

    private Long id;
    private long productId; // MspProduct # id

    private Website website;
    private String websiteLogoUrl = "";
    private String seller;

    private String rating = "";
    private String title = "";
    private float price;

    private String priceStr;

    private float priceOff;// 省了多少钱

    private String priceOffStr;

    private String url = "";
    private String deeplink = "";

    private String color;
    private String size;

    private String imageUrl = "";
    private boolean hasImgTitle;

    public ComparedSkuVo(Website website, String title, double price) {
        this.website = website;
        this.title = title;
        this.price = (float) price;
        this.websiteLogoUrl = WebsiteHelper.getLogoUrl(website);
    }

    public ComparedSkuVo(PtmCmpSku sku, String[] affs) {
        this(sku.getWebsite(), sku.getTitle(), sku.getPrice());
        this.id = sku.getId();
        this.productId = sku.getProductId();
        this.seller = sku.getSeller();
        this.rating = sku.getRating();

        this.url = WebsiteHelper.getUrlWithAff(website, sku.getUrl(), affs);//sku.getWebsite() == null ? sku.getOriUrl() : sku.getUrl();
        this.deeplink = WebsiteHelper.getDeeplinkWithAff(website, sku.getUrl(), affs);

//        this.url = WebsiteHelper.getSiteUrl(productId + "_" + sku.getId(), website, sku.getUrl(), sku.getOriUrl());
//        this.deeplink = WebsiteHelper.getDeeplink2(productId + "_" + sku.getId(), website, sku.getUrl(), sku.getOriUrl());

        this.color = sku.getColor();
        this.size = sku.getSize();

        this.imageUrl = ImageUtil.getImageUrl(sku);

        this.hasImgTitle = !StringUtils.isEmpty(title);
    }

    public boolean isHasImgTitle() {
        return hasImgTitle;
    }

    public void setHasImgTitle(boolean hasImgTitle) {
        this.hasImgTitle = hasImgTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDeeplink() {
        return deeplink;
    }

    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }

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

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public float getPrice() {
        if (HasofferRegion.INDIA.equals(AppConfig.getSerRegion())) {
            return Float.valueOf(new DecimalFormat("#").format(price));
        } else {
            return price;

        }
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

    public String getPriceStr() {
        if (price == 0) {
            return "";
        } else {
            NumberFormat ddf1 = NumberFormat.getNumberInstance();
            ddf1.setMaximumFractionDigits(2);
            if (HasofferRegion.INDIA.equals(AppConfig.getSerRegion())) {
                ddf1.setMaximumFractionDigits(0);
            }
            return ddf1.format(price);
        }
    }

    public String getPriceOffStr() {
        if (priceOff == 0) {
            return "";
        } else {
            NumberFormat ddf1 = NumberFormat.getNumberInstance();
            ddf1.setMaximumFractionDigits(2);
            if (HasofferRegion.INDIA.equals(AppConfig.getSerRegion())) {
                ddf1.setMaximumFractionDigits(0);
            }
            Float temp = Math.abs(priceOff);
            return ddf1.format(temp);
        }
    }

    public float getPriceOff() {
        return priceOff;
    }

    public void setPriceOff(float priceOff) {
        this.priceOff = priceOff;
    }

    public String getWebsiteLogoUrl() {
        return websiteLogoUrl;
    }

    public void setWebsiteLogoUrl(String websiteLogoUrl) {
        this.websiteLogoUrl = websiteLogoUrl;
    }
}
