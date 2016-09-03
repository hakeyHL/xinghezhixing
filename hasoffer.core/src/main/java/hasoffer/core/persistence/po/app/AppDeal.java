package hasoffer.core.persistence.po.app;

import hasoffer.base.model.Website;
import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;
import java.util.Date;

/**
 * Created on 2016/6/17.
 */
@Entity
public class AppDeal implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Website website;//deal来源网站

    private String title;//deal标题
    @Column(columnDefinition = "text", nullable = false)
    private String linkUrl;//deal跳转地址

    @Column(columnDefinition = "text")
    private String imageUrl;//本地图片服务器地址

    @Column(nullable = false)
    private Date createTime;//deal创建时间
    @Column(nullable = false)
    private Date expireTime;//deal失效时间

    private boolean push = false;//是否推送到banner,默认false，不推送

    @Column(columnDefinition = "text")
    private String description;//deal描述

    @Column(length = 1024)
    private String priceDescription;//deal价格描述

    private boolean display;//是否显示
    @Column(columnDefinition = "text")
    private String listPageImage;//deal列表页图片

    @Column(columnDefinition = "text")
    private String infoPageImage;//deal详情页图片

    @Column
    private Long dealClickCount = 0l;//deal的点击次数

    @Column
    private Long dealCategoryId = -1l;//deal的目录id

    @Column
    private int discount = 50;//deal的折扣额度


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public String getPriceDescription() {
        return priceDescription;
    }

    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public String getListPageImage() {
        return listPageImage;
    }

    public void setListPageImage(String listPageImage) {
        this.listPageImage = listPageImage;
    }

    public String getInfoPageImage() {
        return infoPageImage;
    }

    public void setInfoPageImage(String infoPageImage) {
        this.infoPageImage = infoPageImage;
    }

    public Long getDealClickCount() {
        return dealClickCount;
    }

    public void setDealClickCount(Long dealClickCount) {
        this.dealClickCount = dealClickCount;
    }

    public Long getDealCategoryId() {
        return dealCategoryId;
    }

    public void setDealCategoryId(Long dealCategoryId) {
        this.dealCategoryId = dealCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppDeal appDeal = (AppDeal) o;

        if (push != appDeal.push) return false;
        if (display != appDeal.display) return false;
        if (discount != appDeal.discount) return false;
        if (id != null ? !id.equals(appDeal.id) : appDeal.id != null) return false;
        if (website != appDeal.website) return false;
        if (title != null ? !title.equals(appDeal.title) : appDeal.title != null) return false;
        if (linkUrl != null ? !linkUrl.equals(appDeal.linkUrl) : appDeal.linkUrl != null) return false;
        if (imageUrl != null ? !imageUrl.equals(appDeal.imageUrl) : appDeal.imageUrl != null) return false;
        if (createTime != null ? !createTime.equals(appDeal.createTime) : appDeal.createTime != null) return false;
        if (expireTime != null ? !expireTime.equals(appDeal.expireTime) : appDeal.expireTime != null) return false;
        if (description != null ? !description.equals(appDeal.description) : appDeal.description != null) return false;
        if (priceDescription != null ? !priceDescription.equals(appDeal.priceDescription) : appDeal.priceDescription != null)
            return false;
        if (listPageImage != null ? !listPageImage.equals(appDeal.listPageImage) : appDeal.listPageImage != null)
            return false;
        if (infoPageImage != null ? !infoPageImage.equals(appDeal.infoPageImage) : appDeal.infoPageImage != null)
            return false;
        if (dealClickCount != null ? !dealClickCount.equals(appDeal.dealClickCount) : appDeal.dealClickCount != null)
            return false;
        return !(dealCategoryId != null ? !dealCategoryId.equals(appDeal.dealCategoryId) : appDeal.dealCategoryId != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (linkUrl != null ? linkUrl.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (expireTime != null ? expireTime.hashCode() : 0);
        result = 31 * result + (push ? 1 : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (priceDescription != null ? priceDescription.hashCode() : 0);
        result = 31 * result + (display ? 1 : 0);
        result = 31 * result + (listPageImage != null ? listPageImage.hashCode() : 0);
        result = 31 * result + (infoPageImage != null ? infoPageImage.hashCode() : 0);
        result = 31 * result + (dealClickCount != null ? dealClickCount.hashCode() : 0);
        result = 31 * result + (dealCategoryId != null ? dealCategoryId.hashCode() : 0);
        result = 31 * result + discount;
        return result;
    }
}
