package hasoffer.fetch.test.sites;

import hasoffer.base.model.Website;
import hasoffer.fetch.model.ListJob;
import hasoffer.fetch.model.PageModel;
import hasoffer.fetch.sites.geekbuying.GeekbuyingListProcessor;
import hasoffer.fetch.sites.geekbuying.GeekbuyingProductProcessor;
import org.junit.Test;

/**
 * Created by chevy on 2015/11/1.
 */
public class GeekbuyingProcessorTest {

	@Test
	public void f2() {
		String url =
				"http://www.geekbuying.com/item/ONEPLUS-ONE-5-5Inch-Qualcomm-Core-Smartphone-Quad-Core-CPU-2-5GHz-LTPS-Screen-3GB-RAM-64GB-ROM-13-0MP-4G-FDD-LTE-OTG-NFC-Miracast---White-329245.html";

		GeekbuyingProductProcessor productProcessor = new GeekbuyingProductProcessor();
		try {
			productProcessor.parseProduct(url);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void f1() {
		String url = "http://www.geekbuying.com/category/Tablet-PCs---Accessories-971/";
		GeekbuyingListProcessor listProcessor = new GeekbuyingListProcessor();
		PageModel pageModel = listProcessor.getPageModel(url);

		System.out.println(pageModel.getPageCount());

		ListJob job = new ListJob(Website.TINYDEAL, url);
		listProcessor.extractProductJobs(job);
	}

	@Test
	public void f0() {
		String url = "http://www.geekbuying.com/category/HDD---Enclosures-1347/2-40-3-0-0-0-grid.html";
		GeekbuyingListProcessor listProcessor = new GeekbuyingListProcessor();
		url = listProcessor.getPageUrlTemplate(url);
		System.out.print(url + "\n");
	}

}
