package hasoffer.api.controller.vo;

/**
 * Date : 2016/4/20
 * Function :
 */
public class AppCooperative {

    private String sit;

    private String packageName;

    private int order;

    private String accessClassName;

    public AppCooperative(String sit, String packageName, int order, String accessClassName) {
        this.sit = sit;
        this.packageName = packageName;
        this.order = order;
        this.accessClassName = accessClassName;
    }

    public String getSit() {
        return sit;
    }

    public void setSit(String sit) {
        this.sit = sit;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getAccessClassName() {
        return accessClassName;
    }

    public void setAccessClassName(String accessClassName) {
        this.accessClassName = accessClassName;
    }
}
