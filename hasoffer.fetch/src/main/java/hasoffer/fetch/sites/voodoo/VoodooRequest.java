package hasoffer.fetch.sites.voodoo;

/**
 * Date : 2016/5/4
 * Function :
 */
public class VoodooRequest {
    boolean cache = true;
    String currentPrice = "Rs. 13,590";
    String originalPrice = "Rs. 13,590";
    String merchant = "snapdeal";
    String pid;
    String title = "UNBOXED OnePlus X (16 GB-Onyx}";

    public VoodooRequest() {
    }

    public VoodooRequest(String title) {
        this.title = title;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
