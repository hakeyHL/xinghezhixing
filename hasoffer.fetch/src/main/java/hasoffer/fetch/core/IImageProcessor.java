package hasoffer.fetch.core;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;

/**
 * Created on 2016/7/21.
 */
public interface IImageProcessor {

    String getWebsiteImageUrl(String url) throws HttpFetchException, ContentParseException;

}
