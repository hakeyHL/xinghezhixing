package hasoffer.fetch.test.sites.mysmartprice;

import hasoffer.fetch.sites.snapdealDeprecated.SnapdealProductProcessor;
import org.junit.Test;

/**
 * Created by chevy on 2015/12/11.
 */
public class SnapdealProcessorTest {

	public static void main(String[] args) {
		String url = "http://www.snapdealDeprecated.com/product/samsung-galaxy-s4-gti9500-white/1351153";
		SnapdealProductProcessor spp = new SnapdealProductProcessor();
		try {
			spp.getPirce(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void f() {
		String url = "http://www.snapdealDeprecated.com/product/samsung-galaxy-s4-gti9500-white/1351153";

	}

}
