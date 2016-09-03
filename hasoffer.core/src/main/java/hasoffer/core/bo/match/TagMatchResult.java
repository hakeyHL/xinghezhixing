package hasoffer.core.bo.match;

import hasoffer.nlp.core.model.HasTag;

/**
 * Created by chevy on 2016/7/3.
 */
public class TagMatchResult {

    private String tag;

    private HasTag hasTag;

    public TagMatchResult() {
    }

    public TagMatchResult(String tag, HasTag hasTag) {
        this.tag = tag;
        this.hasTag = hasTag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public HasTag getHasTag() {
        return hasTag;
    }

    public void setHasTag(HasTag hasTag) {
        this.hasTag = hasTag;
    }
}
