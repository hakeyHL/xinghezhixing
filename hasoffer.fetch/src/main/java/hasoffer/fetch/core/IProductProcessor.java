package hasoffer.fetch.core;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.fetch.model.Product;
import org.apache.http.conn.ConnectTimeoutException;
import org.htmlcleaner.XPatherException;

public interface IProductProcessor {

	/**
	 * 根据商品 URL 反推 productId
	 *
	 * @param productId
	 * @return
	 */
	String getUrlByProductId(String productId);

	/**
	 * 根据 productId 推算出商品 URL
	 *
	 * @param pageUrl
	 * @return
	 */
	String getProductIdByUrl(String pageUrl);

	/**
	 * 根据主站的商品详情链接抓取商品信息
	 *
	 * @param url
	 * @return
	 */
	Product parseProduct(String url) throws ConnectTimeoutException, HttpFetchException, XPatherException, ContentParseException;
}
