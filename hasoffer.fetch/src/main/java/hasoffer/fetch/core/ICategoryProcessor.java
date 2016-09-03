package hasoffer.fetch.core;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.fetch.model.Category;

import java.util.List;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/26
 */
public interface ICategoryProcessor {

	List<Category> parseCategories() throws HttpFetchException, ContentParseException;

}
