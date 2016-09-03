package hasoffer.admin.manager;

import hasoffer.admin.common.CategoryHelper;
import hasoffer.admin.controller.vo.SearchLogVo;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.core.cache.SearchLogCacheManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.core.product.ICategoryService;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.search.ISearchService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Date : 2016/4/20
 * Function :
 */
@Component
public class SearchLogManager {

    @Resource
    IDataBaseManager dbm;

    @Resource
    ICategoryService categoryService;

    @Resource
    ICmpSkuService cmpSkuService;
    @Resource
    SearchLogCacheManager searchLogCacheManager;
    @Resource
    ISearchService searchService;

    public List<SearchLogVo> getSearchLogs(List<SrmSearchLog> logs) {
        List<SearchLogVo> searchLogVos = new ArrayList<SearchLogVo>();

        if (ArrayUtils.hasObjs(logs)) {
            for (SrmSearchLog srmSearchLog : logs) {
                String title = "";
                long proId = srmSearchLog.getPtmProductId();

                int skuCount = 0;
                float min = 0, max = 0;

                if (proId > 0) {
                    PtmProduct ptmProduct = dbm.get(PtmProduct.class, srmSearchLog.getPtmProductId());
                    if (ptmProduct != null) {
                        title = ptmProduct.getTitle();
                    }

                    List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(proId);

                    skuCount = cmpSkus.size();

                    for (PtmCmpSku cmpSku : cmpSkus) {
                        float price = cmpSku.getPrice();

                        if (min <= 0 || price < min) {
                            min = price;
                        }
                        if (max <= 0 || price > max) {
                            max = price;
                        }
                    }
                }

                List<PtmCategory> categories = categoryService.getRouterCategoryList(srmSearchLog.getCategory());

                searchLogVos.add(
                        new SearchLogVo(
                                srmSearchLog,
                                title,
                                CategoryHelper.getCategoryVos(categories),
                                min,
                                max,
                                skuCount)
                );
            }
        }

        return searchLogVos;
    }
}
