package hasoffer.core.product.solr;

import hasoffer.base.config.AppConfig;
import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.StringUtils;
import hasoffer.data.solr.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@Service
public class ProductIndexServiceImpl extends AbstractIndexService<Long, ProductModel> {
    @Override
    protected String getSolrUrl() {
        return AppConfig.get(AppConfig.SOLR_PRODUCT_URL);
    }

    public PageableResult<ProductModel> searchProductsByKey(String title, int page, int size) {
        Sort[] sorts = null;
        PivotFacet[] pivotFacets = null;

        List<FilterQuery> fqList = new ArrayList<FilterQuery>();
        fqList.add(new FilterQuery("price", "[1 TO *]"));
        FilterQuery[] fqs = fqList.toArray(new FilterQuery[0]);

        SearchResult<ProductModel> sr = searchObjs(title, fqs, sorts, pivotFacets, page <= 1 ? 1 : page, size, true);

        return new PageableResult<ProductModel>(sr.getResult(), sr.getTotalCount(), page, size);
    }

    public PageableResult<ProductModel> searchPro(long cateId, int level, int page, int size) {
        if (level < 1 || level > 3) {
            return null;
        }
        Sort[] sorts = new Sort[]{new Sort("searchCount", Order.DESC)};
        String q = "*:*";
        PivotFacet[] pivotFacets = null;

        List<FilterQuery> fqList = new ArrayList<FilterQuery>();
        fqList.add(new FilterQuery("cate" + level, String.valueOf(cateId)));
        fqList.add(new FilterQuery("price", "[1 TO *]"));
        FilterQuery[] fqs = fqList.toArray(new FilterQuery[0]);
        System.out.println(Thread.currentThread().getName() + " page " + page + "  size " + size);
        SearchResult<ProductModel> sr = searchObjs(q, fqs, sorts, pivotFacets, page <= 1 ? 1 : page, size, true);

        return new PageableResult<ProductModel>(sr.getResult(), sr.getTotalCount(), page, size);
    }

    public PageableResult searchPro(long cateId, int level, String title, int page, int size) {
        long cate1 = 0, cate2 = 0, cate3 = 0;

        if (level == 1) {
            cate1 = cateId;
        } else if (level == 2) {
            cate2 = cateId;
        } else if (level == 3) {
            cate3 = cateId;
        }

        return searchPro(cate1, cate2, cate3, title, page, size);
    }

    public PageableResult searchPro(long category1, long category2, long category3, String title, int page, int size) {
        String q = title;
        if (StringUtils.isEmpty(q)) {
            q = "*:*";
        }

        List<FilterQuery> fqList = new ArrayList<FilterQuery>();
        if (category3 > 0) {
            fqList.add(new FilterQuery("cate3", String.valueOf(category3)));
        } else if (category2 > 0) {
            fqList.add(new FilterQuery("cate2", String.valueOf(category2)));
        } else if (category1 > 0) {
            fqList.add(new FilterQuery("cate1", String.valueOf(category1)));
        }

        FilterQuery[] fqs = fqList.toArray(new FilterQuery[0]);
        Sort[] sorts = null;
        PivotFacet[] pivotFacets = null;

        SearchResult<Long> sr = search(q, fqs, sorts, pivotFacets, page, size);

        long totalCount = sr.getTotalCount();

        return new PageableResult<Long>(sr.getResult(), totalCount, page, size);
    }
}
