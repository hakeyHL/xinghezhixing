package hasoffer.fetch.test.sites;

import hasoffer.fetch.model.Product;
import hasoffer.fetch.sites.Ali.AliexpressBandListProcessor;
import hasoffer.fetch.sites.Ali.AliexpressProductProcessor;
import org.junit.Test;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/20
 */
public class AliexpressProcessorTest {

	@Test
	public void test_list() {
		String url = "http://www.aliexpress.com/spulist.html?catId=5090301&page=81";

		AliexpressBandListProcessor listProcessor = new AliexpressBandListProcessor();

		listProcessor.extractProducts(url);
	}

	@Test
	public void f() {
		String url =
				"http://www.aliexpress.com/item/Original-Xiaomi-Redmi-Note-2-TD-4G-FDD-LTE-CellPhone-MTK6795-Octa-Core-5-5-2GB/32463256279.html?spm=2114.01020208.3.1.hniloe&ws_ab_test=searchweb201556_6_79_78_77_80,searchweb201644_5,searchweb201560_9";
		AliexpressProductProcessor productProcessor = new AliexpressProductProcessor();

		try {
			Product product = productProcessor.parseProduct(url);
			System.out.println(product.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
