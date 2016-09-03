package hasoffer.core.persistence.mongo;

import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.bo.product.SearchedSku;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.fetch.model.WebFetchResult;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date : 2016/5/17
 * Function :
 */
@Document(collection = "SrmAutoSearchResult")
public class SrmAutoSearchResult implements Serializable {

    @Id
    private String id;

    private Date updateTime;
    private long lUpdateTime;

    private long relatedProId;
    private String fromWebsite;
    private String title;
    private float price;

    private Map<Website, WebFetchResult> sitePros = new HashMap<Website, WebFetchResult>();
    private Map<Website, List<SearchedSku>> finalSkus;

    private long lRelateTime = 0;

    @PersistenceConstructor
    public SrmAutoSearchResult() {
        this.updateTime = TimeUtils.nowDate();
        this.lUpdateTime = updateTime.getTime();
    }

    public SrmAutoSearchResult(String id) {
        this();
        this.id = id;
    }

    public SrmAutoSearchResult(SrmSearchLog log) {
        this(log.getId());
        this.relatedProId = log.getPtmProductId();
        this.fromWebsite = log.getSite();
        this.title = log.getKeyword();
        this.price = log.getPrice();
    }

    public long getlRelateTime() {
        return lRelateTime;
    }

    public void setlRelateTime(long lRelateTime) {
        this.lRelateTime = lRelateTime;
    }

    public long getRelatedProId() {
        return relatedProId;
    }

    public void setRelatedProId(long relatedProId) {
        this.relatedProId = relatedProId;
    }

    public String getFromWebsite() {
        return fromWebsite;
    }

    public void setFromWebsite(String fromWebsite) {
        this.fromWebsite = fromWebsite;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public long getlUpdateTime() {
        if (updateTime == null) {
            updateTime = new Date();
        }
        return updateTime.getTime();
    }

    //public void setlUpdateTime(long lUpdateTime) {
    //    this.lUpdateTime = lUpdateTime;
    //}

    public Map<Website, WebFetchResult> getSitePros() {
        return sitePros;
    }

    public void setSitePros(Map<Website, WebFetchResult> sitePros) {
        this.sitePros = sitePros;
    }

    public Map<Website, List<SearchedSku>> getFinalSkus() {
        return finalSkus;
    }

    public void setFinalSkus(Map<Website, List<SearchedSku>> finalSkus) {
        this.finalSkus = finalSkus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SrmAutoSearchResult that = (SrmAutoSearchResult) o;

        if (fromWebsite != null ? !fromWebsite.equals(that.fromWebsite) : that.fromWebsite != null) return false;
        return !(title != null ? !title.equals(that.title) : that.title != null);

    }

    @Override
    public int hashCode() {
        int result = fromWebsite != null ? fromWebsite.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SrmAutoSearchResult{" +
                "id='" + id + '\'' +
                ", updateTime=" + updateTime +
                ", lUpdateTime=" + lUpdateTime +
                ", relatedProId=" + relatedProId +
                ", fromWebsite='" + fromWebsite + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", sitePros=" + sitePros +
                ", finalSkus=" + finalSkus +
                ", lRelateTime=" + lRelateTime +
                '}';
    }
}
