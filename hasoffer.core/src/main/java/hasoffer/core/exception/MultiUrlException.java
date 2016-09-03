package hasoffer.core.exception;

import hasoffer.base.model.Website;

/**
 * Date : 2016/6/1
 * Function :
 */
public class MultiUrlException extends BaseException {

    Website website;
    String sourceId;
    String cliQ;

    public MultiUrlException(Website website, String sourceId, String cliQ) {
        super("MultiUrlException");
        this.website = website;
        this.sourceId = sourceId;
        this.cliQ = cliQ;
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
}
