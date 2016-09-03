package hasoffer.api.controller.vo;

/**
 * Date : 2016/4/8
 * Function :
 */
public class ParseConfigSetting {

    private String[] titleResIds;

    private String[] priceResIds;

    private String[] subTitleResIds;

    private String flag;//word/view

    private String[] key;

    public ParseConfigSetting() {
    }

    public String[] getTitleResIds() {
        return titleResIds;
    }

    public void setTitleResIds(String[] titleResIds) {
        this.titleResIds = titleResIds;
    }

    public String[] getPriceResIds() {
        return priceResIds;
    }

    public void setPriceResIds(String[] priceResIds) {
        this.priceResIds = priceResIds;
    }

    public String[] getSubTitleResIds() {
        return subTitleResIds;
    }

    public void setSubTitleResIds(String[] subTitleResIds) {
        this.subTitleResIds = subTitleResIds;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String[] getKey() {
        return key;
    }

    public void setKey(String[] key) {
        this.key = key;
    }
}
