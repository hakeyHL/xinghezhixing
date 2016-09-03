package hasoffer.fetch.test.sites;

import hasoffer.fetch.sites.amazon.ext.AmazonProductExtProcessor;
import hasoffer.fetch.sites.amazon.ext.AmazonSearchPageProcessor;
import org.junit.Test;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/13
 */
public class AmazonExtProcessorTest {

	@Test
	public void f2(){
		String url = "http://www.amazon.com/gp/product/B00D3IN11Q";

		AmazonProductExtProcessor productExtProcessor = new AmazonProductExtProcessor();

		try {
			productExtProcessor.parse(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void f() {
		String searchUrl = "http://www.amazon.com/s/ref=nb_sb_noss_1?url=search-alias%3Dtoys-and-games&field-keywords=quadcopter";
		AmazonSearchPageProcessor searchPageProcessor = new AmazonSearchPageProcessor();

		try {
			searchPageProcessor.parse(searchUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
