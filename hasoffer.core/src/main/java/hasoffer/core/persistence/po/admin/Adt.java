package hasoffer.core.persistence.po.admin;

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hs on 2016年07月26日.
 * Time 12:02
 */
@Entity
public class Adt implements Identifiable<Long> {

    Date date = new Date();
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date startTime = date;
    private Date endTime = getTimeAfterNDays(date, 7);
    private int count;
    private String aderLogoUrl;
    private String aderName;
    private String adMinmage;
    private String adMaxmage;
    private String adSlogan;
    private String adLink;
    private String adBtnContent;
    private String aderSiteUrl;
    private boolean isShow = false;
    @Transient
    private String packageName;
    private int adLocation;
    //为适配客户端显示的格式化的时间字符串--开始时间
    @Transient
    private String sTime;
    //为适配客户端显示的格式化的时间字符串---结束时间
    @Transient
    private String eTime;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getsTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.startTime);
    }

    public String geteTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.endTime);
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAderLogoUrl() {
        return aderLogoUrl;
    }

    public void setAderLogoUrl(String aderLogoUrl) {
        this.aderLogoUrl = aderLogoUrl;
    }

    public String getAderName() {
        return aderName;
    }

    public void setAderName(String aderName) {
        this.aderName = aderName;
    }

    public String getAdMinmage() {
        return adMinmage;
    }

    public void setAdMinmage(String adMinmage) {
        this.adMinmage = adMinmage;
    }

    public String getAdMaxmage() {
        return adMaxmage;
    }

    public void setAdMaxmage(String adMaxmage) {
        this.adMaxmage = adMaxmage;
    }

    public String getAdSlogan() {
        return adSlogan;
    }

    public void setAdSlogan(String adSlogan) {
        this.adSlogan = adSlogan;
    }

    public String getAdLink() {
        return adLink;
    }

    public void setAdLink(String adLink) {
        this.adLink = adLink;
    }

    public String getAdBtnContent() {
        return adBtnContent;
    }

    public void setAdBtnContent(String adBtnContent) {
        this.adBtnContent = adBtnContent;
    }

    public String getAderSiteUrl() {
        return aderSiteUrl;
    }

    public void setAderSiteUrl(String aderSiteUrl) {
        this.aderSiteUrl = aderSiteUrl;
    }

    public int getAdLocation() {
        return adLocation;
    }

    public void setAdLocation(int adLocation) {
        this.adLocation = adLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Adt adt = (Adt) o;

        if (count != adt.count) return false;
        if (isShow != adt.isShow) return false;
        if (adLocation != adt.adLocation) return false;
        if (id != null ? !id.equals(adt.id) : adt.id != null) return false;
        if (startTime != null ? !startTime.equals(adt.startTime) : adt.startTime != null) return false;
        if (endTime != null ? !endTime.equals(adt.endTime) : adt.endTime != null) return false;
        if (aderLogoUrl != null ? !aderLogoUrl.equals(adt.aderLogoUrl) : adt.aderLogoUrl != null) return false;
        if (aderName != null ? !aderName.equals(adt.aderName) : adt.aderName != null) return false;
        if (adMinmage != null ? !adMinmage.equals(adt.adMinmage) : adt.adMinmage != null) return false;
        if (adMaxmage != null ? !adMaxmage.equals(adt.adMaxmage) : adt.adMaxmage != null) return false;
        if (adSlogan != null ? !adSlogan.equals(adt.adSlogan) : adt.adSlogan != null) return false;
        if (adLink != null ? !adLink.equals(adt.adLink) : adt.adLink != null) return false;
        if (adBtnContent != null ? !adBtnContent.equals(adt.adBtnContent) : adt.adBtnContent != null) return false;
        return !(aderSiteUrl != null ? !aderSiteUrl.equals(adt.aderSiteUrl) : adt.aderSiteUrl != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + count;
        result = 31 * result + (aderLogoUrl != null ? aderLogoUrl.hashCode() : 0);
        result = 31 * result + (aderName != null ? aderName.hashCode() : 0);
        result = 31 * result + (adMinmage != null ? adMinmage.hashCode() : 0);
        result = 31 * result + (adMaxmage != null ? adMaxmage.hashCode() : 0);
        result = 31 * result + (adSlogan != null ? adSlogan.hashCode() : 0);
        result = 31 * result + (adLink != null ? adLink.hashCode() : 0);
        result = 31 * result + (adBtnContent != null ? adBtnContent.hashCode() : 0);
        result = 31 * result + (aderSiteUrl != null ? aderSiteUrl.hashCode() : 0);
        result = 31 * result + (isShow ? 1 : 0);
        result = 31 * result + adLocation;
        return result;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public Date getTimeAfterNDays(Date date, int n) {
        date.setTime(date.getTime() + 1000 * 60 * 60 * 24 * n);
        return date;
    }
}
