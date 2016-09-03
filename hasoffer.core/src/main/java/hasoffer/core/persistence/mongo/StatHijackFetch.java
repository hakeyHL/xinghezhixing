package hasoffer.core.persistence.mongo;

import hasoffer.base.model.Website;
import hasoffer.core.bo.product.IndexHistory;
import hasoffer.core.persistence.enums.IndexStat;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 2016/6/1.
 */
@Document(collection = "StatHijackFetch")
public class StatHijackFetch {

    @Id
    private String id;

    private Website website;

    private String sourceId;

    private String cliQ;

    private Date createTime;
    private long lCreateTime;

    @Enumerated(EnumType.STRING)
    private IndexStat status;

    private String result;

    private List<IndexHistory> indexHistoryList;

    private List<String> affectSkuIdList = new ArrayList<String>();

    @PersistenceConstructor
    StatHijackFetch() {

    }

    public StatHijackFetch(String id, Website website, String sourceId, String cliQ, Date createTime, long lCreateTime, IndexStat status, List<IndexHistory> indexHistoryList) {
        this.id = id;
        this.website = website;
        this.sourceId = sourceId;
        this.cliQ = cliQ;
        this.createTime = createTime;
        this.lCreateTime = lCreateTime;
        this.status = status;
        this.indexHistoryList = indexHistoryList;
    }

    public List<String> getAffectSkuIdList() {
        return affectSkuIdList;
    }

    public void setAffectSkuIdList(List<String> affectSkuIdList) {
        this.affectSkuIdList = affectSkuIdList;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getCliQ() {
        return cliQ;
    }

    public void setCliQ(String cliQ) {
        this.cliQ = cliQ;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getlCreateTime() {
        return lCreateTime;
    }

    public void setlCreateTime(long lCreateTime) {
        this.lCreateTime = lCreateTime;
    }

    public IndexStat getStatus() {
        return status;
    }

    public void setStatus(IndexStat status) {
        this.status = status;
    }

    public List<IndexHistory> getIndexHistoryList() {
        return indexHistoryList;
    }

    public void setIndexHistoryList(List<IndexHistory> indexHistoryList) {
        this.indexHistoryList = indexHistoryList;
    }
}
