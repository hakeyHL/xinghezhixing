package hasoffer.fetch.test.sites;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.fetch.core.ICategoryProcessor;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.core.ISummaryProductProcessor;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.model.Product;
import hasoffer.fetch.sites.amazon.AmazonCategoryProcessor;
import hasoffer.fetch.sites.amazon.AmazonProductProcessor;
import hasoffer.fetch.sites.amazon.AmazonSummaryProductProcessor;
import hasoffer.fetch.sites.amazon.UsaAmazonSummaryProductProcessor;
import hasoffer.fetch.sites.shopclues.ShopCluesSummaryProductProcessor;
import org.apache.http.conn.ConnectTimeoutException;
import org.htmlcleaner.XPatherException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/19
 */
public class AmazonProcessorTest {

    private Logger logger = LoggerFactory.getLogger(AmazonProcessorTest.class);

    @Test
    public void f2() throws Exception {
        String url = "http://www.shopclues.com/apple-iphone-5-ear-phones-5844.html";
        ISummaryProductProcessor productProcessor = new ShopCluesSummaryProductProcessor();
        OriFetchedProduct oriFetchedProduct = productProcessor.getSummaryProductByUrl(url);
        System.out.println(oriFetchedProduct.getPrice());
    }

    @Test
    public void f() {
        String url = "http://www.amazon.in/gp/product/B01ABYRPZ2";
        IProductProcessor productProcessor = new AmazonProductProcessor();

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
    public void testCategoryProcessor() {
        ICategoryProcessor categoryProcessor = new AmazonCategoryProcessor();
        try {
            categoryProcessor.parseCategories();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testamazonupdate() throws HttpFetchException, ContentParseException {

        String url = "https://www.amazon.com/gp/product/B00TKFDKMQ";

        UsaAmazonSummaryProductProcessor summaryProductProcessor = new UsaAmazonSummaryProductProcessor();

        OriFetchedProduct fetchedProduct = summaryProductProcessor.getSummaryProductByUrl(url);

        System.out.println(fetchedProduct);

    }

}
