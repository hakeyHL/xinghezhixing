package hasoffer.core.bo.match;

import java.util.List;
import java.util.Map;

/**
 * Created by chevy on 2016/7/3.
 */
public class AnalysisResult {

    private String title;

    private Map<TagType, List<TagMatchResult>> tagMap;

    public AnalysisResult() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<TagType, List<TagMatchResult>> getTagMap() {
        return tagMap;
    }

    public void setTagMap(Map<TagType, List<TagMatchResult>> tagMap) {
        this.tagMap = tagMap;
    }
}
