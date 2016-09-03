package hasoffer.api.controller.vo;

import hasoffer.base.model.Website;

import java.io.Serializable;

/**
 * Created by hs on 2016年08月18日.
 * Time 11:06
 * app下载引导中引导下载的APP的信息
 */
public class ThirdAppVo implements Serializable {

    //deeplink、logo、简介、评分值、评论数、下载数(googleplay的下载链接需添加联盟id)
    private String packageName;
    private Website website;   //site
    private String downloadLink;//下载地址
    private String logoUrl;//logo图片地址
    private String introduction;//简介
    private float ratings;//评分值
    private String comments;//评论数
    private String downloads;//下载量
    private String packageSize;

    public ThirdAppVo() {
    }

    public ThirdAppVo(Website website, String packageName, String downloadLink, String logoUrl, String introduction, float ratings, String comments, String downloads, String packageSize) {
        this.packageName = packageName;
        this.website = website;
        this.downloadLink = downloadLink;
        this.logoUrl = logoUrl;
        this.introduction = introduction;
        this.ratings = ratings;
        this.comments = comments;
        this.downloads = downloads;
        this.packageSize = packageSize;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public float getRatings() {
        return ratings;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDownloads() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public String getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(String packageSize) {
        this.packageSize = packageSize;
    }
}
