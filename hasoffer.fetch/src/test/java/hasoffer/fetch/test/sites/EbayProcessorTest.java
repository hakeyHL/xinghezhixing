package hasoffer.fetch.test.sites;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.sites.ebay.EbayListProcessor;
import org.junit.Test;

import java.util.List;

/**
 * Created on 2016/4/8.
 */
public class EbayProcessorTest {

    @Test
    public void testEbayListProcessor() throws HttpFetchException, ContentParseException {

        EbayListProcessor processor = new EbayListProcessor();

        List<ListProduct> productList = processor.getProductSetByKeyword("iphone", 15);

        System.out.println(productList);

    }

}
