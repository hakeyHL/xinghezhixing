package hasoffer.admin.controller.vo;

/**
 * Created on 2016/7/6.
 */
public class TopSellingVo {

    private long id;
    private String name;
    private String imageurl;
    private long skuNumber;
    private String logid;
    private long count;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public long getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(long skuNumber) {
        this.skuNumber = skuNumber;
    }

    public String getLogid() {
        return logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
    }
}
