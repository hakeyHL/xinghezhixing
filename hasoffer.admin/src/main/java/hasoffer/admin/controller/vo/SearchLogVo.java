package hasoffer.admin.controller.vo;

import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.enums.SearchPrecise;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.fetch.helper.WebsiteHelper;

import java.util.Date;
import java.util.List;

/**
 * Date : 2016/1/8
 * Function :
 */
public class SearchLogVo {
    List<CategoryVo> categories;

    private String id;
    private String site;
    private String keyword;
    private String searchUrl;
    private float price;
    private int count = 1;// 被搜索次数

    private long ptmProductId;
    private long ptmCmpSkuId;
    private SearchPrecise precise;

    private String title;

    private Date createTime = TimeUtils.nowDate();
    private Date updateTime = TimeUtils.nowDate();

    private float skuMinPrice;
    private float skuMaxPrice;
    private int skuCount;

    private Date manualSetTime;

    public SearchLogVo(SrmSearchLog srmSearchLog, String title, List<CategoryVo> categories, float minPrice, float maxPrice, int skuCount) {
        this.id = srmSearchLog.getId();
        this.site = srmSearchLog.getSite();
        this.keyword = srmSearchLog.getKeyword();
        this.price = srmSearchLog.getPrice();
        this.count = srmSearchLog.getCount();
        this.createTime = srmSearchLog.getCreateTime();
        this.ptmProductId = srmSearchLog.getPtmProductId();
        this.ptmCmpSkuId = srmSearchLog.getPtmCmpSkuId();
        this.title = title;
        this.categories = categories;
        this.precise = srmSearchLog.getPrecise();
        this.updateTime = srmSearchLog.getUpdateTime();

        this.searchUrl = WebsiteHelper.getSearchUrl(Website.valueOf(site), keyword);
        this.manualSetTime = srmSearchLog.getManualSetTime();

        this.skuMinPrice = minPrice;
        this.skuMaxPrice = maxPrice;
        this.skuCount = skuCount;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getPtmProductId() {
        return ptmProductId;
    }

    public void setPtmProductId(long ptmProductId) {
        this.ptmProductId = ptmProductId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SearchPrecise getPrecise() {
        return precise;
    }

    public void setPrecise(SearchPrecise precise) {
        this.precise = precise;
    }

    public long getPtmCmpSkuId() {
        return ptmCmpSkuId;
    }

    public void setPtmCmpSkuId(long ptmCmpSkuId) {
        this.ptmCmpSkuId = ptmCmpSkuId;
    }

    public List<CategoryVo> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryVo> categories) {
        this.categories = categories;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public float getSkuMinPrice() {
        return skuMinPrice;
    }

    public void setSkuMinPrice(float skuMinPrice) {
        this.skuMinPrice = skuMinPrice;
    }

    public float getSkuMaxPrice() {
        return skuMaxPrice;
    }

    public void setSkuMaxPrice(float skuMaxPrice) {
        this.skuMaxPrice = skuMaxPrice;
    }

    public int getSkuCount() {
        return skuCount;
    }

    public void setSkuCount(int skuCount) {
        this.skuCount = skuCount;
    }

    public Date getManualSetTime() {
        return manualSetTime;
    }

    public void setManualSetTime(Date manualSetTime) {
        this.manualSetTime = manualSetTime;
    }
}
