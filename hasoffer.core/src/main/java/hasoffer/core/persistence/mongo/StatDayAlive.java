package hasoffer.core.persistence.mongo;

import hasoffer.base.utils.HexDigestUtil;
import hasoffer.base.utils.TimeUtils;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

/**
 * Created on 2016/4/7.
 */
@Document(collection = "StatDayAlive")
public class StatDayAlive {

    @Id
    private String id;

    private String ymd;

    private String osVersion;

    private String brand;

    private String marketChannel;

    private String campaign;

    private String adSet;

    private Date updateTime;

    private long allAlive;

    private long newAlive;

    private long eCommerceAll;

    private long eCommerceNew;

    private long bindAssist;

    private long assistIsFirst;

    private long assistNotFirst;

    private long wakeUpAll;

    private long wakeUpNew;

    private long showIconAll;

    private long showIconNew;

    private long clickIconAll;

    private long clickIconNew;

    private long clickShopAll;

    private long clickShopNew;

    private long cmpAll;

    private long cmpNew;

    @PersistenceConstructor
    public StatDayAlive() {
    }

    public StatDayAlive(String date, String osVersion, String brand, String marketChannel,
                        String campaign, String adSet) {

        this.id = HexDigestUtil.md5(marketChannel + date + osVersion + brand + campaign + adSet);
        this.ymd = date;
        this.osVersion = osVersion;
        this.brand = brand;
        this.marketChannel = marketChannel;
        this.campaign = campaign;
        this.adSet = adSet;
        this.updateTime = TimeUtils.nowDate();
    }

    public StatDayAlive(String date, String osVersion, String brand, String marketChannel,
                        String campaign, String ADset,
                        Date updateTime,
                        Integer allAlive, Integer newAlive, Integer eCommerceAll,
                        Integer eCommerceNew, Integer bindAssist,
                        Integer wakeUpAll, Integer wakeUpNew,
                        Integer showIconAll, Integer showIconNew, Integer clickIconAll,
                        Integer clickIconNew, Integer clickShopAll, Integer clickShopNew) {
        this(date, osVersion, brand, marketChannel, campaign, ADset);

        this.allAlive = allAlive;
        this.newAlive = newAlive;
        this.eCommerceAll = eCommerceAll;
        this.eCommerceNew = eCommerceNew;
        this.bindAssist = bindAssist;
        this.showIconAll = showIconAll;
        this.showIconNew = showIconNew;
        this.clickIconAll = clickIconAll;
        this.clickIconNew = clickIconNew;
        this.clickShopAll = clickShopAll;
        this.clickShopNew = clickShopNew;
        this.wakeUpAll = wakeUpAll;
        this.wakeUpNew = wakeUpNew;
    }

    public long getWakeUpAll() {
        return wakeUpAll;
    }

    public void setWakeUpAll(long wakeUpAll) {
        this.wakeUpAll = wakeUpAll;
    }

    public long getWakeUpNew() {
        return wakeUpNew;
    }

    public void setWakeUpNew(long wakeUpNew) {
        this.wakeUpNew = wakeUpNew;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMarketChannel() {
        return marketChannel;
    }

    public void setMarketChannel(String marketChannel) {
        this.marketChannel = marketChannel;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getAdSet() {
        return adSet;
    }

    public void setAdSet(String adSet) {
        this.adSet = adSet;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public long getAllAlive() {
        return allAlive;
    }

    public void setAllAlive(long allAlive) {
        this.allAlive = allAlive;
    }

    public long getNewAlive() {
        return newAlive;
    }

    public void setNewAlive(long newAlive) {
        this.newAlive = newAlive;
    }

    public long geteCommerceAll() {
        return eCommerceAll;
    }

    public void seteCommerceAll(long eCommerceAll) {
        this.eCommerceAll = eCommerceAll;
    }

    public long geteCommerceNew() {
        return eCommerceNew;
    }

    public void seteCommerceNew(long eCommerceNew) {
        this.eCommerceNew = eCommerceNew;
    }

    public long getAssistIsFirst() {
        return assistIsFirst;
    }

    public void setAssistIsFirst(long assistIsFirst) {
        this.assistIsFirst = assistIsFirst;
    }

    public long getAssistNotFirst() {
        return assistNotFirst;
    }

    public void setAssistNotFirst(long assistNotFirst) {
        this.assistNotFirst = assistNotFirst;
    }

    public long getShowIconAll() {
        return showIconAll;
    }

    public void setShowIconAll(long showIconAll) {
        this.showIconAll = showIconAll;
    }

    public long getShowIconNew() {
        return showIconNew;
    }

    public void setShowIconNew(long showIconNew) {
        this.showIconNew = showIconNew;
    }

    public long getClickIconAll() {
        return clickIconAll;
    }

    public void setClickIconAll(long clickIconAll) {
        this.clickIconAll = clickIconAll;
    }

    public long getClickIconNew() {
        return clickIconNew;
    }

    public void setClickIconNew(long clickIconNew) {
        this.clickIconNew = clickIconNew;
    }

    public long getClickShopAll() {
        return clickShopAll;
    }

    public void setClickShopAll(long clickShopAll) {
        this.clickShopAll = clickShopAll;
    }

    public long getClickShopNew() {
        return clickShopNew;
    }

    public void setClickShopNew(long clickShopNew) {
        this.clickShopNew = clickShopNew;
    }

    public long getCmpAll() {
        return cmpAll;
    }

    public void setCmpAll(long cmpAll) {
        this.cmpAll = cmpAll;
    }

    public long getCmpNew() {
        return cmpNew;
    }

    public void setCmpNew(long cmpNew) {
        this.cmpNew = cmpNew;
    }

    public long getBindAssist() {
        return bindAssist;
    }

    public void setBindAssist(long bindAssist) {
        this.bindAssist = bindAssist;
    }

    @Override
    public String toString() {
        return "StatDayAlive{" +
                "ymd='" + ymd + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", brand='" + brand + '\'' +
                ", marketChannel='" + marketChannel + '\'' +
                ", allAlive=" + allAlive +
                ", newAlive=" + newAlive +
                ", eCommerceAll=" + eCommerceAll +
                ", eCommerceNew=" + eCommerceNew +
                ", bindAssist=" + bindAssist +
                ", showIconAll=" + showIconAll +
                ", showIconNew=" + showIconNew +
                ", clickIconAll=" + clickIconAll +
                ", clickIconNew=" + clickIconNew +
                ", clickShopAll=" + clickShopAll +
                ", clickShopNew=" + clickShopNew +
                ", cmpAll=" + cmpAll +
                ", cmpNew=" + cmpNew +
                '}';
    }
}
