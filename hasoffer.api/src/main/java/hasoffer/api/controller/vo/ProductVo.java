package hasoffer.api.controller.vo;

/**
 * Date : 2016/1/11
 * Function :
 */
public class ProductVo {

    private long id;

    private String title;

    private String masterImageUrl;

    private float currentLowestPrice;
    private String currentDeeplink;

    public ProductVo(long id, String title, String masterImageUrl, float currentLowestPrice, String currentDeeplink) {
        this.id = id;
        this.title = title;
        this.masterImageUrl = masterImageUrl;
        this.currentLowestPrice = currentLowestPrice;
        this.currentDeeplink = currentDeeplink;
    }

    public String getCurrentDeeplink() {
        return currentDeeplink;
    }

    public void setCurrentDeeplink(String currentDeeplink) {
        this.currentDeeplink = currentDeeplink;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMasterImageUrl() {
        return masterImageUrl;
    }

    public void setMasterImageUrl(String masterImageUrl) {
        this.masterImageUrl = masterImageUrl;
    }

    public float getCurrentLowestPrice() {
        return currentLowestPrice;
    }

    public void setCurrentLowestPrice(float currentLowestPrice) {
        this.currentLowestPrice = currentLowestPrice;
    }
}
