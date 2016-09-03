package hasoffer.fetch.test.sites;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.fetch.core.IListProcessor;
import hasoffer.fetch.helper.WebsiteProcessorFactory;
import hasoffer.fetch.model.ListProduct;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created on 2016/4/8.
 */
public class ListProcessorTest {


    @Test
    public void testListProcessor() throws HttpFetchException, ContentParseException, UnsupportedEncodingException {

        String websiteString = "amazon";

        Website website = Website.valueOf(websiteString.toUpperCase());

        IListProcessor listProcessor = WebsiteProcessorFactory.getListProcessor(website);

        List<ListProduct> iphoneList = listProcessor.getProductSetByKeyword("Samsung Galaxy Star Pro", 13);

        System.out.println(iphoneList);

    }

}
