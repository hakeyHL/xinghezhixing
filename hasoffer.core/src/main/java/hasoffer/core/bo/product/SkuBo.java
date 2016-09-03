package hasoffer.core.bo.product;

/**
 * Created on 2016/4/14.
 */
public class SkuBo {

    private long id;
    private String title;
    private float price;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
