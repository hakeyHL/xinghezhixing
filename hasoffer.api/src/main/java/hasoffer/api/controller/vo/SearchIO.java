package hasoffer.api.controller.vo;

import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.Website;
import hasoffer.base.utils.PriceUtil;
import hasoffer.base.utils.StringUtils;

/**
 * Date : 2016/5/27
 * Function :
 */
public class SearchIO {
    // cli 开头的都是客户端传上来的参数
    final String cliQ;//客户端传上来的字符串
    final Website cliSite;
    final String cliQBrand;
    final float cliPrice;
    final String cliSourceId;
    // hs 开头的都是搜到的结果 hs - hasoffer
    long hsCateId;
    long hsProId;
    long hsSkuId;
    boolean firstSearch;

    String keyword; // 服务器用于匹配的字符串， 由处理q得来

    MarketChannel marketChannel;
    String deviceId;

    int page;
    int size;

    public SearchIO(String sourceId, String q, String brand, String site, String price,
                    MarketChannel marketChannel, String deviceId,
                    int page, int size) {
        this.cliSourceId = sourceId;
        this.cliQ = q.trim();
        this.cliSite = Website.valueOf(site);
        this.cliPrice = (float) PriceUtil.getPrice(price);
        this.cliQBrand = brand;

        this.keyword = StringUtils.getCleanWordString(q);

        this.marketChannel = marketChannel;
        this.deviceId = deviceId;

        this.page = page;
        this.size = size;

        this.firstSearch = false;
    }

    @Override
    public String toString() {
        return "SearchIO{" +
                "cliQ='" + cliQ + '\'' +
                ", cliSite=" + cliSite +
                ", cliQBrand='" + cliQBrand + '\'' +
                ", cliPrice=" + cliPrice +
                ", cliSourceId='" + cliSourceId + '\'' +
                ", hsCateId=" + hsCateId +
                ", hsProId=" + hsProId +
                ", hsSkuId=" + hsSkuId +
                ", firstSearch=" + firstSearch +
                ", keyword='" + keyword + '\'' +
                ", marketChannel=" + marketChannel +
                ", deviceId='" + deviceId + '\'' +
                ", page=" + page +
                ", size=" + size +
                '}';
    }

    public boolean isFirstSearch() {
        return firstSearch;
    }

    public void setFirstSearch(boolean firstSearch) {
        this.firstSearch = firstSearch;
    }

    public MarketChannel getMarketChannel() {
        return marketChannel;
    }

    public void setMarketChannel(MarketChannel marketChannel) {
        this.marketChannel = marketChannel;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCliSourceId() {
        return cliSourceId;
    }

    public long getHsCateId() {
        return hsCateId;
    }

    public void setHsCateId(long hsCateId) {
        this.hsCateId = hsCateId;
    }

    public long getHsProId() {
        return hsProId;
    }

    public void setHsProId(long hsProId) {
        this.hsProId = hsProId;
    }

    public long getHsSkuId() {
        return hsSkuId;
    }

    public void setHsSkuId(long hsSkuId) {
        this.hsSkuId = hsSkuId;
    }

    public String getCliQ() {
        return cliQ;
    }

    public Website getCliSite() {
        return cliSite;
    }

    public float getCliPrice() {
        return cliPrice;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void set(long cateId, long productId, long skuId) {
        this.hsCateId = cateId;
        this.hsProId = productId;
        this.hsSkuId = skuId;
    }

    public String getCliQBrand() {
        return cliQBrand;
    }
}
