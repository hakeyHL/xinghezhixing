package hasoffer.admin.controller.vo;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.Date;

public class HiJackReportVo {

    private Integer id;
    private String webSite;
    private Date calStartTime;
    private Date calEndTime;
    private int cmpSkuCount;
    private int pricelistCount;
    private int rediToAffiliateUrlCount;
    private BigDecimal successPerc;
    private int deeplinkCount;
    private int deeplinkNullCount;
    private int deeplinkDoubleCount;
    private int deeplinkExceptionCount;
    private int captureSingleSuccess;
    private int captureMultipleSuccess;
    private int captureFail;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public void setSuccessPerc(BigDecimal successPerc) {
        this.successPerc = successPerc;
    }

    public Date getCalStartTime() {
        return calStartTime;
    }

    public void setCalStartTime(Date calStartTime) {
        this.calStartTime = calStartTime;
    }

    public Date getCalEndTime() {
        return calEndTime;
    }

    public void setCalEndTime(Date calEndTime) {
        this.calEndTime = calEndTime;
    }

    public int getCmpSkuCount() {
        return cmpSkuCount;
    }

    public void setCmpSkuCount(int cmpSkuCount) {
        this.cmpSkuCount = cmpSkuCount;
    }

    public int getPricelistCount() {
        return pricelistCount;
    }

    public void setPricelistCount(int pricelistCount) {
        this.pricelistCount = pricelistCount;
    }

    public int getRediToAffiliateUrlCount() {
        return rediToAffiliateUrlCount;
    }

    public void setRediToAffiliateUrlCount(int rediToAffiliateUrlCount) {
        this.rediToAffiliateUrlCount = rediToAffiliateUrlCount;
    }

    public BigDecimal getSuccessPerc() {
        if (pricelistCount != 0) {
            return new BigDecimal(rediToAffiliateUrlCount).multiply(new BigDecimal(100).divide(new BigDecimal(pricelistCount), 2, BigDecimal.ROUND_HALF_UP));
        }
        return successPerc;
    }

    public int getDeeplinkCount() {
        return deeplinkCount;
    }

    public void setDeeplinkCount(int deeplinkCount) {
        this.deeplinkCount = deeplinkCount;
    }

    public int getDeeplinkNullCount() {
        return deeplinkNullCount;
    }

    public void setDeeplinkNullCount(int deeplinkNullCount) {
        this.deeplinkNullCount = deeplinkNullCount;
    }

    public int getDeeplinkDoubleCount() {
        return deeplinkDoubleCount;
    }

    public void setDeeplinkDoubleCount(int deeplinkDoubleCount) {
        this.deeplinkDoubleCount = deeplinkDoubleCount;
    }

    public int getDeeplinkExceptionCount() {
        return deeplinkExceptionCount;
    }

    public void setDeeplinkExceptionCount(int deeplinkExceptionCount) {
        this.deeplinkExceptionCount = deeplinkExceptionCount;
    }

    public int getCaptureSingleSuccess() {
        return captureSingleSuccess;
    }

    public void setCaptureSingleSuccess(int captureSingleSuccess) {
        this.captureSingleSuccess = captureSingleSuccess;
    }

    public int getCaptureMultipleSuccess() {
        return captureMultipleSuccess;
    }

    public void setCaptureMultipleSuccess(int captureMultipleSuccess) {
        this.captureMultipleSuccess = captureMultipleSuccess;
    }

    public int getCaptureFail() {
        return captureFail;
    }

    public void setCaptureFail(int captureFail) {
        this.captureFail = captureFail;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDateStr() {
        return DateFormatUtils.format(calStartTime, "yyyy-MM-dd");
    }
}
