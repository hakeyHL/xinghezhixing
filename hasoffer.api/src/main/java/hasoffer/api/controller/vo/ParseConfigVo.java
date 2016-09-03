package hasoffer.api.controller.vo;

import hasoffer.base.model.Website;

/**
 * Date : 2016/4/8
 * Function :
 */
public class ParseConfigVo {

    private Website website;

    private String packageName;

    private String displayIcon;

    private ParseConfigSetting parseConfigSetting;

    public ParseConfigVo() {
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDisplayIcon() {
        return displayIcon;
    }

    public void setDisplayIcon(String displayIcon) {
        this.displayIcon = displayIcon;
    }

    public ParseConfigSetting getParseConfigSetting() {
        return parseConfigSetting;
    }

    public void setParseConfigSetting(ParseConfigSetting parseConfigSetting) {
        this.parseConfigSetting = parseConfigSetting;
    }
}
