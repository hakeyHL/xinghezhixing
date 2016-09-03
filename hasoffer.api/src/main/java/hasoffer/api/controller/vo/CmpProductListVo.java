package hasoffer.api.controller.vo;

import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hs on 2016/6/25.
 */
public class CmpProductListVo {
    List<String> support = new ArrayList<String>();
    List<String> offers = new ArrayList<String>();
    private String imageUrl;
    private String image;
    private int ratingNum;
    private Long totalRatingsNum;
    private String skuPrice;
    private int price;
    private float freight;
    private String distributionTime;
    private Long coins;
    private float backRate;
    private int returnGuarantee;
    private String deepLink;
    private String deepLinkUrl;
    private Website website;
    private String title;
    private float cashBack;
    private float saved;
    private Long id;
    private SkuStatus status;
    private boolean isAlert = false;
//    private int min_deliveryTime;
//    private int max_deliveryTime;

    public CmpProductListVo() {
    }

    public CmpProductListVo(PtmCmpSku cmpSku, String logoImage) {
        this.id = cmpSku.getId();
        this.coins = cmpSku.getWebsite() == Website.FLIPKART ? Math.round(0.015 * cmpSku.getPrice()) : 0;
        this.ratingNum = cmpSku.getWebsite().equals(Website.EBAY) ? 0 : cmpSku.getRatings();
        this.imageUrl = cmpSku.getSmallImagePath() == null ? "" : ImageUtil.getImageUrl(cmpSku.getSmallImagePath());
        this.totalRatingsNum = cmpSku.getWebsite().equals(Website.EBAY) ? 0 : cmpSku.getCommentsNumber();
        this.image = logoImage;
        if (cmpSku.getWebsite().equals(Website.FLIPKART)) {
            if (cmpSku.getTitle() != null) {
                this.title = cmpSku.getTitle();
                if (cmpSku.getSkuTitle() != null) {
                    this.title += cmpSku.getSkuTitle();
                }
            } else {
                if (cmpSku.getSkuTitle() != null) {
                    this.title = cmpSku.getSkuTitle();
                }
            }
        } else {
            this.title = cmpSku.getTitle() == null ? "" : cmpSku.getTitle();
        }
        this.status = cmpSku.getStatus();
        this.price = Math.round(cmpSku.getPrice());
        this.website = cmpSku.getWebsite();
        this.freight = cmpSku.getShipping();
//        this.min_deliveryTime = 1;
//        this.max_deliveryTime = 5;
//        String deliveryTime = cmpSku.getDeliveryTime();
//        if (!StringUtils.isEmpty(deliveryTime)) {
//            String[] split = deliveryTime.split("-");
//            if (split.length == 2) {
//                min_deliveryTime = Integer.valueOf(split[0]);
//                max_deliveryTime = Integer.valueOf(split[1]);
//            }
//        }
        this.distributionTime = cmpSku.getDeliveryTime();
        this.backRate = cmpSku.getWebsite() == Website.FLIPKART ? 1.5f : 0;
        this.returnGuarantee = cmpSku.getReturnDays();
        String payMethod = cmpSku.getSupportPayMethod();
        if (!StringUtils.isEmpty(payMethod)) {
            String[] temps = payMethod.split(",");
            for (String str : temps) {
                this.support.add(str);
            }
        }
    }

    public CmpProductListVo(PtmCmpSku cmpSku, float cliPrice) {
        this.id = cmpSku.getId();
        this.status = cmpSku.getStatus();
        this.title = cmpSku.getTitle();
        this.imageUrl = cmpSku.getSmallImagePath() == null ? "" : ImageUtil.getImageUrl(cmpSku.getSmallImagePath());
        this.cashBack = cmpSku.getCashBack();
        this.saved = cliPrice == 0 ? 0 : Math.round(cliPrice - cmpSku.getPrice());
        String tempPrice = Math.round(cmpSku.getPrice()) + "";
        StringBuffer sb = new StringBuffer();
        for (int i = tempPrice.length() - 1; i >= 0; i--) {
            sb.append(tempPrice.charAt(i));
            if ((tempPrice.length() - i) % 3 == 0 && i != 0) {
                sb.append(",");
            }
        }
        this.id = cmpSku.getId();
        this.skuPrice = sb.reverse().toString();
        this.website = cmpSku.getWebsite();
    }

    public CmpProductListVo(String image, int ratingNum, Long totalRatingsNum, int price, int freight, String distributionTime, Long coins, float backRate, int returnGuarantee, List<String> support) {
        this.image = image;
        this.ratingNum = ratingNum;
        this.totalRatingsNum = totalRatingsNum;
        this.price = price;
        this.freight = freight;
        this.distributionTime = distributionTime;
        this.coins = coins;
        this.backRate = backRate;
        this.returnGuarantee = returnGuarantee;
        this.support = support;
    }

    public static void main(String[] args) {
        System.out.println(Math.round(0 - 3.5));
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSkuPrice() {
        return skuPrice;
    }

    public void setSkuPrice(String skuPrice) {
        this.skuPrice = skuPrice;
    }

    public SkuStatus getStatus() {
        return status;
    }

    public void setStatus(SkuStatus status) {
        this.status = status;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getDeepLinkUrl() {
        return deepLinkUrl;
    }

    public void setDeepLinkUrl(String deepLinkUrl) {
        this.deepLinkUrl = deepLinkUrl;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public int getRatingNum() {
        return ratingNum;
    }

    public void setRatingNum(int ratingNum) {
        this.ratingNum = ratingNum;
    }

    public Long getTotalRatingsNum() {
        return totalRatingsNum;
    }

    public void setTotalRatingsNum(Long totalRatingsNum) {
        this.totalRatingsNum = totalRatingsNum;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setFreight(float freight) {
        this.freight = freight;
    }

    public float getFreight() {
        return freight;
    }

    public void setFreight(int freight) {
        this.freight = freight;
    }

    public String getDistributionTime() {
        return distributionTime;
    }

    public void setDistributionTime(String distributionTime) {
        this.distributionTime = distributionTime;
    }

    public Long getCoins() {
        return coins;
    }

    public void setCoins(Long coins) {
        this.coins = coins;
    }

    public float getBackRate() {
        return backRate;
    }

    public void setBackRate(float backRate) {
        this.backRate = backRate;
    }

    public int getReturnGuarantee() {
        return returnGuarantee;
    }

    public void setReturnGuarantee(int returnGuarantee) {
        this.returnGuarantee = returnGuarantee;
    }

    public List<String> getSupport() {
        return support;
    }

    public void setSupport(List<String> support) {
        this.support = support;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getCashBack() {
        return cashBack;
    }

    public void setCashBack(float cashBack) {
        this.cashBack = cashBack;
    }

    public float getSaved() {
        return saved;
    }

    public void setSaved(float saved) {
        this.saved = saved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getOffers() {
        return offers;
    }

//    public int getMin_deliveryTime() {
//        return min_deliveryTime;
//    }
//
//    public void setMin_deliveryTime(int min_deliveryTime) {
//        this.min_deliveryTime = min_deliveryTime;
//    }
//
//    public int getMax_deliveryTime() {
//        return max_deliveryTime;
//    }
//
//    public void setMax_deliveryTime(int max_deliveryTime) {
//        this.max_deliveryTime = max_deliveryTime;
//    }

    public void setOffers(List<String> offers) {
        this.offers = offers;
    }

    public boolean isAlert() {
        return isAlert;
    }

    public void setIsAlert(boolean isAlert) {
        this.isAlert = isAlert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CmpProductListVo that = (CmpProductListVo) o;

        if (ratingNum != that.ratingNum) return false;
        if (price != that.price) return false;
        if (Float.compare(that.freight, freight) != 0) return false;
        if (Float.compare(that.backRate, backRate) != 0) return false;
        if (returnGuarantee != that.returnGuarantee) return false;
        if (Float.compare(that.cashBack, cashBack) != 0) return false;
        if (Float.compare(that.saved, saved) != 0) return false;
        if (isAlert != that.isAlert) return false;
        if (support != null ? !support.equals(that.support) : that.support != null) return false;
        if (offers != null ? !offers.equals(that.offers) : that.offers != null) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (totalRatingsNum != null ? !totalRatingsNum.equals(that.totalRatingsNum) : that.totalRatingsNum != null)
            return false;
        if (skuPrice != null ? !skuPrice.equals(that.skuPrice) : that.skuPrice != null) return false;
        if (distributionTime != null ? !distributionTime.equals(that.distributionTime) : that.distributionTime != null)
            return false;
        if (coins != null ? !coins.equals(that.coins) : that.coins != null) return false;
        if (deepLink != null ? !deepLink.equals(that.deepLink) : that.deepLink != null) return false;
        if (deepLinkUrl != null ? !deepLinkUrl.equals(that.deepLinkUrl) : that.deepLinkUrl != null) return false;
        if (website != that.website) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return status == that.status;

    }

    @Override
    public int hashCode() {
        int result = support != null ? support.hashCode() : 0;
        result = 31 * result + (offers != null ? offers.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + ratingNum;
        result = 31 * result + (totalRatingsNum != null ? totalRatingsNum.hashCode() : 0);
        result = 31 * result + (skuPrice != null ? skuPrice.hashCode() : 0);
        result = 31 * result + price;
        result = 31 * result + (freight != +0.0f ? Float.floatToIntBits(freight) : 0);
        result = 31 * result + (distributionTime != null ? distributionTime.hashCode() : 0);
        result = 31 * result + (coins != null ? coins.hashCode() : 0);
        result = 31 * result + (backRate != +0.0f ? Float.floatToIntBits(backRate) : 0);
        result = 31 * result + returnGuarantee;
        result = 31 * result + (deepLink != null ? deepLink.hashCode() : 0);
        result = 31 * result + (deepLinkUrl != null ? deepLinkUrl.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (cashBack != +0.0f ? Float.floatToIntBits(cashBack) : 0);
        result = 31 * result + (saved != +0.0f ? Float.floatToIntBits(saved) : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (isAlert ? 1 : 0);
        return result;
    }
}
