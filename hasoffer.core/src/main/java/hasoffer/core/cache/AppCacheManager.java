package hasoffer.core.cache;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.bo.product.CategoryBo;
import hasoffer.core.bo.product.CategoryVo;
import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.core.redis.ICacheService;
import hasoffer.core.system.impl.AppServiceImpl;
import hasoffer.core.utils.ImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hs on 2016/6/30.
 */
@Component
public class AppCacheManager {
    private static final String CACHE_KEY_PRE = "APP_PTM_CATEGORY";
    private static final long CACHE_EXPIRE_TIME = TimeUtils.MILLISECONDS_OF_1_DAY;
    @Resource
    ICacheService<CategoryBo> CategoryBoService;
    @Resource
    ICacheService<PtmCategory> PtmCategoryService;
    @Resource
    AppServiceImpl appService;
    private Logger logger = LoggerFactory.getLogger(AppCacheManager.class);

    public List getCategorys(String categoryId) {
        String key = null;
        List categorys = new ArrayList();
        if (StringUtils.isBlank(categoryId)) {
            key = CACHE_KEY_PRE + "_LEVEL1";
            CategoryBo categoryBo = CategoryBoService.get(CategoryBo.class, key, 0);
            if (categoryBo != null) {
                categorys = categoryBo.getCategorys();
            } else {
                categoryBo = new CategoryBo();
                categorys = new ArrayList();
                List<PtmCategory> ptmCategorys = appService.getCategory();
                for (PtmCategory ptmCategory : ptmCategorys) {
                    CategoryVo categoryVo = new CategoryVo();
                    categoryVo.setId(ptmCategory.getId());
                    categoryVo.setHasChildren(1);
                    categoryVo.setImage(ptmCategory.getImageUrl() == null ? "" : ImageUtil.getImageUrl(ptmCategory.getImageUrl()));
                    categoryVo.setLevel(ptmCategory.getLevel());
                    categoryVo.setName(ptmCategory.getName());
                    categoryVo.setParentId(ptmCategory.getParentId());
                    categoryVo.setRank(ptmCategory.getRank());
                    List<PtmCategory> ptmCategorysTemp = appService.getChildCategorys(categoryVo.getId().toString());
                    if (ptmCategorysTemp == null && ptmCategorysTemp.size() < 1) {
                        categoryVo.setHasChildren(0);
                    }
                    categorys.add(categoryVo);
                }
            }
            categoryBo.setCategorys(categorys);
            if (categoryBo.getCategorys() != null && categoryBo.getCategorys().size() > 0) {
                CategoryBoService.add(key, categoryBo, CACHE_EXPIRE_TIME);
            }
        } else {
            key = CACHE_KEY_PRE + categoryId;
            CategoryBo categoryBo = CategoryBoService.get(CategoryBo.class, key, 0);
            if (categoryBo != null) {
                categorys = categoryBo.getCategorys();
            } else {
                categoryBo = new CategoryBo();
                List<PtmCategory> ptmCategorys = null;
                ptmCategorys = appService.getChildCategorys(categoryId);
                for (PtmCategory ptmCategory : ptmCategorys) {
                    List childCategory = new ArrayList();
                    CategoryVo categoryVo = new CategoryVo();
                    categoryVo.setId(ptmCategory.getId());
                    categoryVo.setImage(ptmCategory.getImageUrl() == null ? "" : ImageUtil.getImageUrl(ptmCategory.getImageUrl()));
                    categoryVo.setLevel(ptmCategory.getLevel());
                    categoryVo.setName(ptmCategory.getName());
                    categoryVo.setParentId(ptmCategory.getParentId());
                    categoryVo.setRank(ptmCategory.getRank());
                    categoryVo.setHasChildren(1);
                    List<PtmCategory> ptmCategorysTemp = appService.getChildCategorys(ptmCategory.getId().toString());
                    if (ptmCategorysTemp != null && ptmCategorysTemp.size() > 0) {
                        for (PtmCategory ptmCates : ptmCategorysTemp) {
                            CategoryVo cate = new CategoryVo();
                            cate.setId(ptmCates.getId());
                            cate.setHasChildren(0);
                            cate.setImage(ptmCates.getImageUrl() == null ? "" : ImageUtil.getImageUrl(ptmCates.getImageUrl()));
                            cate.setLevel(ptmCates.getLevel());
                            cate.setName(ptmCates.getName());
                            cate.setParentId(ptmCates.getParentId());
                            cate.setRank(ptmCates.getRank());
                            childCategory.add(cate);
                        }
                    } else {
                        categoryVo.setHasChildren(0);
                    }
                    categoryVo.setCategorys(childCategory);
                    categorys.add(categoryVo);
                }
                categoryBo.setCategorys(categorys);
                if (categoryBo.getCategorys() != null && categoryBo.getCategorys().size() > 0) {
                    CategoryBoService.add(key, categoryBo, CACHE_EXPIRE_TIME);
                }
            }
        }
        return categorys;
    }

    public PtmCategory getCategoryById(Long cateId) {
        PtmCategory ptmCategory = null;
        String key = null;
        key = CACHE_KEY_PRE + "_BYID_" + cateId;
        ptmCategory = PtmCategoryService.get(PtmCategory.class, key, 0);
        if (ptmCategory != null) {
            //缓存中有
            return ptmCategory;
        } else {
            ptmCategory = appService.getCategoryInfo(cateId);
            if (ptmCategory != null) {
                System.out.println("将类目加入缓存 :" + ptmCategory.getId());
                PtmCategoryService.add(key, ptmCategory, CACHE_EXPIRE_TIME);
                return ptmCategory;
            }
        }
        return ptmCategory;
    }
}
