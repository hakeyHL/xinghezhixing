package hasoffer.api.controller.vo;

/**
 * Date : 2016/4/8
 * Function :
 */
public class AppConfigVo {

    private boolean showToast;

    private boolean showPrice;

    public AppConfigVo(boolean showToast, boolean showPrice) {
        this.showToast = showToast;
        this.showPrice = showPrice;
    }

    public AppConfigVo() {
        this(false, false);
    }

    public boolean isShowToast() {
        return showToast;
    }

    public void setShowToast(boolean showToast) {
        this.showToast = showToast;
    }

    public boolean isShowPrice() {
        return showPrice;
    }

    public void setShowPrice(boolean showPrice) {
        this.showPrice = showPrice;
    }
}
