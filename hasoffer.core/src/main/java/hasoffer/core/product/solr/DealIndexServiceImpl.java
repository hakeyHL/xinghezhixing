package hasoffer.core.product.solr;

import hasoffer.base.config.AppConfig;
import hasoffer.data.solr.*;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DealIndexServiceImpl extends AbstractIndexService<Long, DealModel> {
    @Override
    protected String getSolrUrl() {
        return AppConfig.get(AppConfig.SOLR_DEAL_URL);
    }

    public List<DealModel> simpleSearch(String key, int page, int size) {
        String q = key;

        Sort[] sorts = null;
        FilterQuery[] fqs = null;
        PivotFacet[] pivotFacets = null;

        SearchResult<DealModel> sr = searchObjs(q, fqs, sorts, pivotFacets, page, size, true);

        return sr.getResult();
    }

}
