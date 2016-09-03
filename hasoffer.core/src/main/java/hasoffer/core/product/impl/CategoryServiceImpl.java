package hasoffer.core.product.impl;

import hasoffer.base.utils.ArrayUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCateTag;
import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.core.persistence.po.ptm.PtmCategory3;
import hasoffer.core.persistence.po.ptm.updater.PtmCategoryUpdater;
import hasoffer.core.product.ICategoryService;
import hasoffer.core.product.solr.CategoryIndexServiceImpl;
import hasoffer.core.product.solr.CategoryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final static String Q_CATEGORY = "SELECT t FROM PtmCategory t";

    private final static String Q_CATEGORY_TAG = "SELECT t FROM PtmCateTag t";

    private static final String Q_CATEGORY_BY_PARENTID =
            "SELECT t FROM PtmCategory t WHERE t.parentId = ?0";
    private final static String CACHE_KEY = "category";
    @Resource
    CategoryIndexServiceImpl categoryIndexService;
    @Resource
    IDataBaseManager dbm;
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    /*@Resource
    private EhCacheCacheManager cacheManager;*/

    @Override
    public PtmCategory getCategory(long cateId) {
        return dbm.get(PtmCategory.class, cateId);
    }

    @Override
    public List<PtmCateTag> listAllCategoryTags() {
        return dbm.query(Q_CATEGORY_TAG);
    }

    @Override
    public List<PtmCategory> listCates() {
        return dbm.query(Q_CATEGORY);
    }

    @Override
//    @CacheEvict(value = CACHE_KEY, key = "'getRouterCategoryList_' + #root.args[0]")
    @Transactional(rollbackFor = Exception.class)
    public void updateCategoryName(long cateId, String categoryName) {
        PtmCategory category = dbm.get(PtmCategory.class, cateId);
        if (category == null) {
            return;
        }

        PtmCategoryUpdater ptmCategoryUpdater = new PtmCategoryUpdater(cateId);
        ptmCategoryUpdater.getPo().setName(categoryName);
        dbm.update(ptmCategoryUpdater);

        category.setName(categoryName);
        categoryIndexService.createOrUpdate(new CategoryModel(category));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategoryKeyword(long cateId, String key) {
        PtmCategory category = dbm.get(PtmCategory.class, cateId);
        if (category == null) {
            return;
        }

        PtmCategoryUpdater ptmCategoryUpdater = new PtmCategoryUpdater(cateId);
        ptmCategoryUpdater.getPo().setKeyword(key);
        dbm.update(ptmCategoryUpdater);

        category.setKeyword(key);
        categoryIndexService.createOrUpdate(new CategoryModel(category));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(long id) {
        dbm.delete(PtmCategory.class, id);
        categoryIndexService.remove(String.valueOf(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PtmCategory3 createAppCategory(PtmCategory3 category) {
        Long aLong = dbm.create(category);
        category.setId(aLong);
        return category;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tempDeleteCategoryForCategoryUpdate(long id) {
        dbm.delete(PtmCategory.class, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategoryLevel(long cateId, int level) {
        PtmCategoryUpdater ptmCategoryUpdater = new PtmCategoryUpdater(cateId);
        ptmCategoryUpdater.getPo().setLevel(level);
        dbm.update(ptmCategoryUpdater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategoryIndex(long cateId, String keyword) {

        PtmCategory category = dbm.get(PtmCategory.class, cateId);
        if (category == null) {
            return;
        }

        PtmCategoryUpdater ptmCategoryUpdater = new PtmCategoryUpdater(cateId);
        ptmCategoryUpdater.getPo().setKeyword(keyword);
        dbm.update(ptmCategoryUpdater);

        // update solr
        categoryIndexService.createOrUpdate(new CategoryModel(category));
    }

    @Override
    public void reimportCategoryIndex() {
        try {
            categoryIndexService.removeAll();
        } catch (Exception e) {
        }

        List<PtmCategory> categories = dbm.query(Q_CATEGORY);

        for (PtmCategory category : categories) {
            if (category.getLevel() == 2) {
                categoryIndexService.createOrUpdate(new CategoryModel(category));
            }
        }
    }

    @Override
    public List<PtmCategory> listSubCategories(Long parentId) {
        return dbm.query(Q_CATEGORY_BY_PARENTID, Arrays.asList(parentId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PtmCategory createCategory(long parentId, String name, String imageUrl) {
        PtmCategory category = new PtmCategory(parentId, name, imageUrl);
        dbm.create(category);
        return category;
    }

    @Override
    public void findRouterCategories(Queue<PtmCategory> categoryQueue, long cateId) {
        if (cateId <= 0) {
            return;
        }

        PtmCategory category = dbm.get(PtmCategory.class, cateId);

        if (category == null) {
            return;
        }

        findRouterCategories(categoryQueue, category.getParentId());

        categoryQueue.offer(category);
    }

    @Override
//    @Cacheable(value = CACHE_KEY, key = "#root.methodName + '_' + #root.args[0]")
    public List<PtmCategory> getRouterCategoryList(long categoryId) {
        logger.debug("getRouterCategoryList_" + categoryId);

        List<PtmCategory> categories = new ArrayList<PtmCategory>();

        if (categoryId == 0) {
            return categories;
        }

        Queue<PtmCategory> categoryQueue = new LinkedList<PtmCategory>();
        findRouterCategories(categoryQueue, categoryId);

        if (ArrayUtils.hasObjs(categoryQueue)) {
            PtmCategory category = null;
            while (true) {
                category = categoryQueue.poll();
                if (category == null) {
                    break;
                }
                categories.add(category);
            }
        }
        return categories;
    }
}
