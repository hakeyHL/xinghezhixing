package hasoffer.core.product.solr;

import hasoffer.base.config.AppConfig;
import hasoffer.base.utils.StringUtils;
import hasoffer.data.solr.*;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryIndexServiceImpl extends AbstractIndexService<Long, CategoryModel> {
    @Override
    protected String getSolrUrl() {
        return AppConfig.get(AppConfig.SOLR_CATEGORY_URL);
    }

    public List<CategoryModel> simpleSearch(String key) {
        return simpleSearch(key, 1, 10);
    }

    public List<CategoryModel> simpleSearch(String key, int page, int size) {
        String q = key;
        if (StringUtils.isEmpty(key)) {
            q = "*:*";
        }

        FilterQuery[] fqs = null;
        Sort[] sorts = null;
        PivotFacet[] pivotFacets = null;

        SearchResult<CategoryModel> sr = searchObjs(q, fqs, sorts, pivotFacets, page, size, true);

        return sr.getResult();
    }
}
