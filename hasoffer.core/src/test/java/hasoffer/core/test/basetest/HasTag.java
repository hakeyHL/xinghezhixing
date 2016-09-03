package hasoffer.core.test.basetest;

/**
 * Date : 2016/6/15
 * Function :
 */
public class HasTag {

    private String tag = "";

    private String alias = ""; // 逗号隔开

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "HasTag{" +
                "tag='" + tag + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
