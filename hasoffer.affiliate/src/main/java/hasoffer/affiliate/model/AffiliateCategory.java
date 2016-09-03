package hasoffer.affiliate.model;

import hasoffer.base.model.Website;

/**
 * Created on 2016/3/14.
 */
public class AffiliateCategory {

    private Long id;
    private long parentId;
    private String name;
    private String url;
    private String imageUrl;
    private int proCount;
    private int depth;
    private long sourceId;
    private long ptmCategoryId;
    private Website website;

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getProCount() {
        return proCount;
    }

    public void setProCount(int proCount) {
        this.proCount = proCount;
    }

    public long getPtmCategoryId() {
        return ptmCategoryId;
    }

    public void setPtmCategoryId(long ptmCategoryId) {
        this.ptmCategoryId = ptmCategoryId;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }
}
