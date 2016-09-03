package hasoffer.core.bo.push;

/**
 * Date : 2016/4/27
 * Function :
 */
public class AppMsgClick {

    private AppMsgClickType type;// access main deeplink webview googleplay

    private String url;//dl, webview, googleplay

    private String packageName;// 只有在deeplink下有效

    public AppMsgClick(AppMsgClickType type, String url, String packageName) {
        this.type = type;
        this.url = url;
        this.packageName = packageName;
    }

    public AppMsgClickType getType() {
        return type;
    }

    public void setType(AppMsgClickType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
