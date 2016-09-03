package hasoffer.admin.controller.vo;

import hasoffer.core.persistence.po.search.SrmProductSearchStat;

import java.util.Date;

/**
 * Created by chevy on 2016/7/10.
 */
public class SearchResultCountVo {

    private String ymd;

    private Date createTime;

    private int noMatchedCount;// 没有匹配到sku的日志
    private int matchedCount;// 没有匹配到sku的日志

    private int skuCount0;// 比价数量是0个
    private int skuCount1;// 比价数量是1个
    private int skuCount2;// 比价数量是2个
    private int skuCount3;// 比价数量是3个
    private int skuCount4;// 比价数量是4个以上的

    private int skuCount11;// 比价数量是11-50个的
    private int skuCount51;// 比价数量是51+个的

    private int totalCount;

    public SearchResultCountVo(SrmProductSearchStat productSearchStat) {
        this.ymd = productSearchStat.getId();
        this.createTime = productSearchStat.getCreateTime();
        this.skuCount0 = productSearchStat.getSkuCount0();
        this.skuCount1 = productSearchStat.getSkuCount1();
        this.skuCount2 = productSearchStat.getSkuCount2();
        this.skuCount3 = productSearchStat.getSkuCount3();
        this.skuCount4 = productSearchStat.getSkuCount4();

        this.skuCount11 = productSearchStat.getSkuCount11();
        this.skuCount51 = productSearchStat.getSkuCount51();

        this.noMatchedCount = productSearchStat.getNoMatchedCount();
        this.matchedCount = productSearchStat.getMatchedCount();

        this.totalCount = this.skuCount0
                + this.skuCount1 + this.skuCount2
                + this.skuCount3 + this.skuCount4
                + this.skuCount11 + this.skuCount51;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getNoMatchedCount() {
        return noMatchedCount;
    }

    public void setNoMatchedCount(int noMatchedCount) {
        this.noMatchedCount = noMatchedCount;
    }

    public int getMatchedCount() {
        return matchedCount;
    }

    public void setMatchedCount(int matchedCount) {
        this.matchedCount = matchedCount;
    }

    public int getSkuCount0() {
        return skuCount0;
    }

    public void setSkuCount0(int skuCount0) {
        this.skuCount0 = skuCount0;
    }

    public int getSkuCount1() {
        return skuCount1;
    }

    public void setSkuCount1(int skuCount1) {
        this.skuCount1 = skuCount1;
    }

    public int getSkuCount2() {
        return skuCount2;
    }

    public void setSkuCount2(int skuCount2) {
        this.skuCount2 = skuCount2;
    }

    public int getSkuCount3() {
        return skuCount3;
    }

    public void setSkuCount3(int skuCount3) {
        this.skuCount3 = skuCount3;
    }

    public int getSkuCount4() {
        return skuCount4;
    }

    public void setSkuCount4(int skuCount4) {
        this.skuCount4 = skuCount4;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSkuCount11() {
        return skuCount11;
    }

    public void setSkuCount11(int skuCount11) {
        this.skuCount11 = skuCount11;
    }

    public int getSkuCount51() {
        return skuCount51;
    }

    public void setSkuCount51(int skuCount51) {
        this.skuCount51 = skuCount51;
    }
}
