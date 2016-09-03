package hasoffer.admin.controller.vo;

import java.util.Map;

/**
 * Date : 2016/2/14
 * Function :
 */
public class TitleStatResultVo {

    private String title;

    private Map<String , Integer> statMap;

    public TitleStatResultVo(String title, Map<String, Integer> statMap) {
        this.title = title;
        this.statMap = statMap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Integer> getStatMap() {
        return statMap;
    }

    public void setStatMap(Map<String, Integer> statMap) {
        this.statMap = statMap;
    }
}
