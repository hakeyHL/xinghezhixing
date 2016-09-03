package hasoffer.fetch.test.sites;

import hasoffer.base.model.Website;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.helper.WebsiteProcessorFactory;
import hasoffer.fetch.model.Product;
import org.junit.Test;

/**
 * Created on 2016/3/13.
 */
public class IProductProcessorTest {

    @Test
    public void testIProductProcessor() {

        String urlTemplate = "http://www.snapdeal.com/product/philips-bt99015-pro-skin-trimmer/638081794574#bcrumbSearch:Philips%20BT990/15%20Trimmer%20For%20Men";
        Website website = WebsiteHelper.getWebSite(urlTemplate);
        IProductProcessor productProcessor = WebsiteProcessorFactory.getProductProcessor(website);

        try {

            Product product = productProcessor.parseProduct(urlTemplate);
            System.out.println(product);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
