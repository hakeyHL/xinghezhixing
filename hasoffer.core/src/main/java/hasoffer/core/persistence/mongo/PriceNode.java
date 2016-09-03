package hasoffer.core.persistence.mongo;

import hasoffer.base.utils.TimeUtils;

import java.util.Date;

/**
 * Created by chevy on 2016/8/27.
 */
public class PriceNode {

    private String ymd;
    private Date priceTime;
    private long priceTimeL;
    private float price;

    public PriceNode(Date priceTime, float price) {
        this.priceTime = priceTime;
        this.price = price;
        this.priceTimeL = this.priceTime.getTime();
        this.ymd = TimeUtils.parse(priceTime, "yyyyMMdd");
    }

    public PriceNode() {
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public Date getPriceTime() {
        return priceTime;
    }

    public void setPriceTime(Date priceTime) {
        this.priceTime = priceTime;
    }

    public long getPriceTimeL() {
        return priceTimeL;
    }

    public void setPriceTimeL(long priceTimeL) {
        this.priceTimeL = priceTimeL;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceNode priceNode = (PriceNode) o;

        return !(ymd != null ? !ymd.equals(priceNode.ymd) : priceNode.ymd != null);

    }

    @Override
    public int hashCode() {
        return ymd != null ? ymd.hashCode() : 0;
    }
}
