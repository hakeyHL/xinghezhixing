package hasoffer.core.analysis;

import hasoffer.core.bo.match.TagType;
import hasoffer.core.persistence.po.match.TagBrand;
import hasoffer.core.persistence.po.match.TagModel;
import hasoffer.nlp.core.model.HasTag;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chevy on 2016/7/3.
 */
public class TagMapHelper {

    private static ConcurrentHashMap<String, TagBrand> brandMap = new ConcurrentHashMap<String, TagBrand>();

    private static ConcurrentHashMap<String, TagModel> modelMap = new ConcurrentHashMap<String, TagModel>();

    public static void addToMap(TagType tagType, String tag, Object o) {
        switch (tagType) {
            case BRAND:
                brandMap.put(tag, (TagBrand) o);
                break;
            case MODEL:
                modelMap.put(tag, (TagModel) o);
                break;
            default:
                break;
        }
    }

    public static HasTag get(TagType tagType, String tag) {
        switch (tagType) {
            case BRAND:
                return brandMap.get(tag);
            case MODEL:
                return modelMap.get(tag);
            default:
                return null;
        }
    }
}
