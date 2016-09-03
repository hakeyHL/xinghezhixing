package hasoffer.core.cache;

import com.alibaba.fastjson.JSON;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.JSONUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.po.ptm.PtmCateTag;
import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.core.product.ICategoryService;
import hasoffer.core.redis.ICacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Date : 2016/5/7
 * Function :
 */
@Component
public class CategoryCacheManager {

    private static final Class CACHE_CLASS = PtmCategory.class;
    private static final String CACHE_KEY_PRE = "CATEGORY_";
    private static final long CACHE_EXPIRE_TIME = TimeUtils.SECONDS_OF_1_DAY;

    @Resource
    ICacheService<PtmCategory> cacheService;
    @Resource
    ICategoryService categoryService;
    Logger logger = LoggerFactory.getLogger(CategoryCacheManager.class);

    public PtmCategory getCategory(long id) {

        String key = CACHE_KEY_PRE + id;

        PtmCategory category = cacheService.get(CACHE_CLASS, key, 0);

        if (category == null) {
            category = categoryService.getCategory(id);
            if (category != null) {
                cacheService.add(key, category, CACHE_EXPIRE_TIME);
            }
        }

        return category;
    }

    public List<PtmCategory> getRouterCategoryList(long categoryId) {
        String key = CACHE_KEY_PRE + "_getRouterCategoryList_" + categoryId;

        String categoryJson = cacheService.get(key, 0);

        if ("NULL".equalsIgnoreCase(categoryJson)) {
            return null;
        }

        List<PtmCategory> categories = null;
        try {
            if (StringUtils.isEmpty(categoryJson)) {

                categories = categoryService.getRouterCategoryList(categoryId);

                if (categories != null && categories.size() > 0) {
                    cacheService.add(key, JSONUtil.toJSON(categories), CACHE_EXPIRE_TIME);
                } else {
                    cacheService.add(key, "NULL", CACHE_EXPIRE_TIME);
                }
            } else {
                categories = JSON.parseArray(categoryJson, PtmCategory.class);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return categories;
    }

    public String getCategoryTag(long categoryId) {
        String key = CACHE_KEY_PRE + "Tag_Map";

        String cateTag = "";

        boolean exists = cacheService.exists(key);
        if (exists) {
            cateTag = cacheService.mapGet(key, String.valueOf(categoryId));
            if (StringUtils.isEmpty(cateTag)) {
                cateTag = "";
            }
        } else {
            List<PtmCateTag> cateTags = categoryService.listAllCategoryTags();
            if (ArrayUtils.hasObjs(cateTags)) {
                for (PtmCateTag ct : cateTags) {
                    cacheService.mapPut(key, ct.getId() + "", ct.getTag());
                    if (categoryId == ct.getId()) {
                        cateTag = ct.getTag();
                    }
                }
            }
        }

        return cateTag;
    }
}
