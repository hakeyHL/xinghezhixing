package hasoffer.core.product.solr;

import hasoffer.base.config.AppConfig;
import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.StringUtils;
import hasoffer.data.solr.*;
import org.springframework.stereotype.Service;


@Service
public class CmpskuIndexServiceImpl extends AbstractIndexService<Long, CmpSkuModel> {
    @Override
    protected String getSolrUrl() {
        return AppConfig.get(AppConfig.SOLR_CMPSKU_URL);
    }

    public PageableResult<CmpSkuModel> searchSku(String key, int page, int size) {

        String q = key;
        if (StringUtils.isEmpty(key)) {
            q = "*:*";
        }

        FilterQuery[] fqs = null;
        Sort[] sorts = null;
        PivotFacet[] pivotFacets = null;

        SearchResult<CmpSkuModel> sr = searchObjs(q, fqs, sorts, pivotFacets, page, size, true);

        return new PageableResult<CmpSkuModel>(sr.getResult(), sr.getTotalCount(), page, size);
    }

    public PageableResult<CmpSkuModel> search(String column, String value, int page, int size) {
        String q = "*:*";

        FilterQuery[] fqs = new FilterQuery[1];
        fqs[0] = new FilterQuery(column, value);
        Sort[] sorts = null;
        PivotFacet[] pivotFacets = null;

        SearchResult<CmpSkuModel> sr = searchObjs(q, fqs, sorts, pivotFacets, page, size, true);

        return new PageableResult<CmpSkuModel>(sr.getResult(), sr.getTotalCount(), page, size);
    }

}
