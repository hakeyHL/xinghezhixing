package hasoffer.core.product;

import hasoffer.affiliate.exception.AffiliateAPIException;
import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.model.Product;

import java.io.IOException;

/**
 * Date : 2015/10/28
 */
public interface IFetchService {

    Product fetchByUrl(String url);

    OriFetchedProduct fetchSummaryProductByUrl(String url) throws ContentParseException, HttpFetchException, AffiliateAPIException, IOException, InterruptedException;

    OriFetchedProduct udpateSkuInAnyWay(String url, Website website) throws AffiliateAPIException, IOException, HttpFetchException, ContentParseException;

    String fetchWebsiteImageUrl(Website website, String url) throws HttpFetchException, ContentParseException;

}
