package hasoffer.fetch.test.sites;

import hasoffer.base.model.Website;
import hasoffer.fetch.model.ListJob;
import hasoffer.fetch.model.PageModel;
import hasoffer.fetch.sites.tinydeal.TinydealHelper;
import hasoffer.fetch.sites.tinydeal.TinydealListProcessor;
import hasoffer.fetch.sites.tinydeal.TinydealProductProcessor;
import org.junit.Test;

/**
 * Created by chevy on 2015/11/1.
 */
public class TinydealProcessorTest {

	@Test
	public void f3() {
		String url = "http://www.tinydeal.com/syma-x5c-x5c-1-24g-rc-quadcopter-with-2mp-camera-p-126482.html";

		TinydealProductProcessor productProcessor = new TinydealProductProcessor();
		try {
			productProcessor.parseProduct(url);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void f2() {
		String url = "http://www.tinydeal.com/index.php?main_page=ws_search_result&keyword=SSD&categories_id=&cat_change=true";
		TinydealListProcessor listProcessor = new TinydealListProcessor();
		PageModel pageModel = listProcessor.getPageModel(url);

		System.out.println(pageModel.getPageCount());

		ListJob job = new ListJob(Website.TINYDEAL, url);
		listProcessor.extractProductJobs(job);
	}

	@Test
	public void f1() {
		String url = "http://www.tinydeal.com/doogee-homtom-ht6-55-mtk6735p-64-bit-quad-core-android-51-4g-phone-p-156364.html";
		String id = TinydealHelper.getProductIdByUrl(url);

		System.out.println(id);
	}

}
