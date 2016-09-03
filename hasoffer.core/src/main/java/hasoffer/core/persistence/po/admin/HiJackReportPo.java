package hasoffer.core.persistence.po.admin;

import hasoffer.core.persistence.dbm.osql.Identifiable;
import hasoffer.core.persistence.dbm.osql.Updater;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "report_hijack")
public class HiJackReportPo implements Identifiable<Integer> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String webSite;

    @Column(nullable = false)
    private Date calStartTime;

    @Column(nullable = false)
    private Date calEndTime;

    private int cmpSkuCount;
    private int pricelistCount;
    private int rediToAffiliateUrlCount;
    private int deeplinkCount;
    private int deeplinkNullCount;
    private int deeplinkDoubleCount;
    private int deeplinkExceptionCount;
    private int captureSingleSuccess;
    private int captureMultipleSuccess;
    private int captureFail;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime=new Date();

    public Integer getId() {
        return id;
    }


    @Override
    public void setId(Integer id) {
        this.id = id;
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

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
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

    public static class HiJackReportPoUpdater extends Updater<Integer, HiJackReportPo> {

        public HiJackReportPoUpdater(Integer aLong) {
            super(HiJackReportPo.class, aLong);
        }
    }

    @Override
    public String toString() {
        return "HiJackReportPo{" +
                "id=" + id +
                ", webSite='" + webSite + '\'' +
                ", calStartTime=" + calStartTime +
                ", calEndTime=" + calEndTime +
                ", pricelistCount=" + pricelistCount +
                ", rediToAffiliateUrlCount=" + rediToAffiliateUrlCount +
                ", deeplinkCount=" + deeplinkCount +
                ", deeplinkNullCount=" + deeplinkNullCount +
                ", deeplinkDoubleCount=" + deeplinkDoubleCount +
                ", captureSingleSuccess=" + captureSingleSuccess +
                ", captureMultipleSuccess=" + captureMultipleSuccess +
                ", captureFail=" + captureFail +
                ", updateTime=" + updateTime +
                '}';
    }
}
