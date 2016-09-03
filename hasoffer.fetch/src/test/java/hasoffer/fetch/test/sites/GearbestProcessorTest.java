package hasoffer.fetch.test.sites;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.fetch.core.ICategoryProcessor;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.model.Product;
import hasoffer.fetch.sites.gearbest.GearbestCategoryProcessor;
import hasoffer.fetch.sites.gearbest.GearbestListProcessor;
import hasoffer.fetch.sites.gearbest.GearbestProductProcessor;
import org.apache.http.conn.ConnectTimeoutException;
import org.htmlcleaner.XPatherException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/19
 */
public class GearbestProcessorTest {

	private Logger logger = LoggerFactory.getLogger(GearbestProcessorTest.class);

	@Test
	public void f() {
		String url = "http://www.gearbest.com/rc-quadcopters/pp_164620.html";
		IProductProcessor productProcessor = new GearbestProductProcessor();

		try {
			Product product = productProcessor.parseProduct(url);

			logger.debug(product.toString());

		} catch (ContentParseException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (HttpFetchException e) {
			e.printStackTrace();
		} catch (XPatherException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void f2() {
		String url = "http://www.gearbest.com/phone-_gear/";
		GearbestListProcessor listProcessor = new GearbestListProcessor();
		try {
			listProcessor.getPageModel(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void f3() {
		ICategoryProcessor categoryProcessor = new GearbestCategoryProcessor();
		try {
			categoryProcessor.parseCategories();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
