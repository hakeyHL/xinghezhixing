package hasoffer.fetch.core;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.fetch.model.ListJob;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.PageModel;

import java.util.List;
import java.util.Set;


public interface IListProcessor {

    void setExistingProductIds(Set<String> existingProductIds);

    void extractProductJobs(ListJob job);

    PageModel getPageModel(String pageUrl);

    List getProductByAjaxUrl(String ajaxUrl, Long ptmCateId) throws HttpFetchException, ContentParseException;

    List<ListProduct> getProductSetByKeyword(String keyword, int resultCount) throws HttpFetchException, ContentParseException;

}
