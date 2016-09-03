package hasoffer.core.bo.product;

/**
 * Date : 2016/1/22
 * Function :
 */
public class SkuPriceUpdateResultBo {

    private String ymd;

    private long count;

    public SkuPriceUpdateResultBo(String ymd, long count) {
        this.ymd = ymd;
        this.count = count;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
