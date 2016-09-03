package hasoffer.core.bo.product;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hs on 2016/6/21.
 */
public class Banners {
    private  String sourceUrl;
    private  int source;
    private  Long rank;
    private String link;
    private  String expireDate;
    private Long dealId;

    public Long getDealId() {
        return dealId;
    }

    public void setDealId(Long dealId) {
        this.dealId = dealId;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date time) {
        this.expireDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(time);
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
