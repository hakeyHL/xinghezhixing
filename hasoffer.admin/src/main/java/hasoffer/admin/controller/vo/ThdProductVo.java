package hasoffer.admin.controller.vo;

import hasoffer.core.bo.enums.RelateType;

/**
 * Created on 2016/3/1.
 */
public class ThdProductVo {

    private long id;
    private String imageUrl ;
    private String title;
    private float price;

    private long ptmProductId;
    private String ptmProductTitle;
    private RelateType relateType;

    public RelateType getRelateType() {
        return relateType;
    }

    public void setRelateType(RelateType relateType) {
        this.relateType = relateType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getPtmProductId() {
        return ptmProductId;
    }

    public void setPtmProductId(long ptmProductId) {
        this.ptmProductId = ptmProductId;
    }

    public String getPtmProductTitle() {
        return ptmProductTitle;
    }

    public void setPtmProductTitle(String ptmProductTitle) {
        this.ptmProductTitle = ptmProductTitle;
    }
}
