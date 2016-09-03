package hasoffer.fetch.test.sites;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ExchangeUtils;
import hasoffer.fetch.core.IListProcessor;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.model.ListJob;
import hasoffer.fetch.model.PageModel;
import hasoffer.fetch.model.Product;
import hasoffer.fetch.sites.banggood.BanggoodListProcessor;
import hasoffer.fetch.sites.banggood.BanggoodProductProcessor;
import org.apache.http.conn.ConnectTimeoutException;
import org.htmlcleaner.XPatherException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/19
 */
public class BanggoodProcessorTest {

	private Logger logger = LoggerFactory.getLogger(BanggoodProcessorTest.class);

	@Test
	public void testProductProcessor() {
		ExchangeUtils.init();
		String url = "http://www.banggood.com/Coolpad-F1-8297W-5-inch-900-2100MHz-Octa-core-Smartphone-White-p-942228.html";
		IProductProcessor productProcessor = new BanggoodProductProcessor();

		try {
//			ExchangeUtils.init();

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
	public void testListProcessor() {
		String url = "http://www.banggood.com/search/smart/1091-0-0-0-0-0-0-ptm-0-0_p-1.html";
		IListProcessor listProcessor = new BanggoodListProcessor();

		PageModel pageModel = listProcessor.getPageModel(url);
		String urlTemplate = pageModel.getUrlTemplate();
		logger.debug(urlTemplate);

		int pageCount = pageModel.getPageCount();

		ListJob listJob = new ListJob(Website.BANGGOOD, url);
		listProcessor.extractProductJobs(listJob);

		logger.debug(pageCount + "");
	}

	@Test
	public void testCategoryProcessor() {

	}
}
