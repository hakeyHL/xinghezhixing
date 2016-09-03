package hasoffer.admin.controller.vo;

import hasoffer.core.persistence.mongo.StatDayAlive;

import java.util.Date;

/**
 * Created on 2016/4/7.
 */
public class DeviceAliveVo {

    private String date;

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

    private long assistIsFirst;

    private long assistNotFirst;

    private long bindAssist;

    private long wakeUpAll;
    private long wakeUpNew;

    private long showIconAll;

    private long showIconNew;

    private long cmpAll;
    private long cmpNew;

    private long clickIconAll;

    private long clickIconNew;

    private long clickShopAll;

    private long clickShopNew;

    public DeviceAliveVo(StatDayAlive sda) {
        this(sda.getYmd(), sda.getOsVersion(), sda.getBrand(), sda.getMarketChannel(),
                sda.getCampaign(), sda.getAdSet(), sda.getUpdateTime(),
                sda.getAllAlive(), sda.getNewAlive(),
                sda.geteCommerceAll(), sda.geteCommerceNew(),
                sda.getAssistIsFirst(), sda.getAssistNotFirst(), sda.getBindAssist(),
                sda.getShowIconAll(), sda.getShowIconNew(),
                sda.getClickIconAll(), sda.getClickIconNew(),
                sda.getClickShopAll(), sda.getClickShopNew(),
                sda.getCmpAll(), sda.getCmpNew(),
                sda.getWakeUpAll(), sda.getWakeUpNew());
    }

    public DeviceAliveVo(String date, String osVersion, String brand, String marketChannel,
                         String campaign, String adSet, Date updateTime) {
        this.date = date;
        this.osVersion = osVersion;
        this.brand = brand;
        this.marketChannel = marketChannel;
        this.campaign = campaign;
        this.adSet = adSet;
        this.updateTime = updateTime;
    }

    public DeviceAliveVo(String date, String osVersion, String brand, String marketChannel,
                         String campaign, String ADset,
                         Date updateTime,
                         long allAlive, long newAlive, long eCommerceAll,
                         long eCommerceNew, long assistIsFirst, long assistNotFirst, long bindAssist,
                         long showIconAll, long showIconNew, long clickIconAll,
                         long clickIconNew, long clickShopAll, long clickShopNew,
                         long cmpAll, long cmpNew, long wakeUpAll, long wakeUpNew) {

        this(date, osVersion, brand, marketChannel, campaign, ADset, updateTime);

        this.allAlive = allAlive;
        this.newAlive = newAlive;
        this.eCommerceAll = eCommerceAll;
        this.eCommerceNew = eCommerceNew;
        this.assistIsFirst = assistIsFirst;
        this.assistNotFirst = assistNotFirst;
        this.bindAssist = bindAssist;
        this.showIconAll = showIconAll;
        this.showIconNew = showIconNew;
        this.clickIconAll = clickIconAll;
        this.clickIconNew = clickIconNew;
        this.clickShopAll = clickShopAll;
        this.clickShopNew = clickShopNew;
        this.cmpAll = cmpAll;
        this.cmpNew = cmpNew;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public long getBindAssist() {
        return bindAssist;
    }

    public void setBindAssist(long bindAssist) {
        this.bindAssist = bindAssist;
    }
}
