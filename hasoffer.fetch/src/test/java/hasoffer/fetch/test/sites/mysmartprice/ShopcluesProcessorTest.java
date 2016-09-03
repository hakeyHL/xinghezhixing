package hasoffer.fetch.test.sites.mysmartprice;

import hasoffer.fetch.sites.shopclues.ShopcluesProductProcessor;
import org.junit.Test;

/**
 * Created by chevy on 2015/12/14.
 */
public class ShopcluesProcessorTest {

	@Test
	public void t() {
		String url =
				"http://www.shopclues.com/apple-iphone-6s-16gb-9.html?utm_source=mysmartprice&utm_medium=CPS";

		ShopcluesProductProcessor p = new ShopcluesProductProcessor();
		try {
			p.getPirce(url);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
