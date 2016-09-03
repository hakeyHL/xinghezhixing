package hasoffer.admin.controller.vo;

import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.utils.ImageUtil;

import java.util.Date;

/**
 * Created on 2015/12/22.
 */
public class CmpSkuVo {

    private Long id;

    private Website website;
    private String seller;

    private String rating;

    private String title;
    private float price;

    private String url;
    private String imageUrl;

    private String color;
    private String size;

    private String flag;

    private String status;

    private Date updateTime = TimeUtils.nowDate();

    public CmpSkuVo() {
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public CmpSkuVo(PtmCmpSku cmpSku) {
        this.id = cmpSku.getId();
        this.website = cmpSku.getWebsite();
        this.seller = cmpSku.getSeller();
        this.rating = cmpSku.getRating();
        this.title = cmpSku.getTitle();
        this.price = cmpSku.getPrice();
        this.url = cmpSku.getUrl();
        this.color = cmpSku.getColor();
        this.size = cmpSku.getSize();
        this.updateTime = cmpSku.getUpdateTime();
        this.status = cmpSku.getStatus().name();

        if (StringUtils.isEmpty(cmpSku.getImagePath())) {
            this.imageUrl = ImageUtil.getImage3rdUrl(cmpSku.getOriImageUrl());
        } else {
            this.imageUrl = ImageUtil.getImageUrl(cmpSku.getImagePath());
        }
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
