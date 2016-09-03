package hasoffer.core.bo.product;

import java.util.Date;

/**
 * Created on 2016/6/1.
 */
public class IndexHistory {

    private String errorMsg;
    private Date date;

    public IndexHistory(String errorMsg, Date date) {
        this.errorMsg = errorMsg;
        this.date = date;
    }

    public String getStat() {
        return errorMsg;
    }

    public void setStat(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
