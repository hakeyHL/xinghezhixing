package hasoffer.fetch.test.sites.mysmartprice;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.sites.flipkart.FlipkartListProcessor;
import hasoffer.fetch.sites.flipkart.FlipkartProductProcessor;
import hasoffer.fetch.sites.flipkart.FlipkartSummaryProductProcessor;
import org.junit.Test;

import java.util.List;

/**
 * Created by chevy on 2015/12/14.
 */
public class FlipkartProcessorTest {

    @Test
    public void t() {
        String url =
                "http://www.flipkart.com/iball-prince-1-8g/p/itme95j9ydy5kzzs";

        FlipkartProductProcessor p = new FlipkartProductProcessor();
        try {
            p.getPirce(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void tt() {
        String url = "13.url：http://www.flipkart.com/samsung-55cm-22-full-hd-led-tv/p/itmdu7yczfng6br9?pid=TVSDU4SFKFNSRKXG&ref=L%3A7583683020864176584&srno=p_1&query=Samsung+55cm+%2822%29+Full+HD+LED+TV&otracker=from-search";

        FlipkartSummaryProductProcessor summaryProductProcessor = new FlipkartSummaryProductProcessor();

        try {
            OriFetchedProduct sp = summaryProductProcessor.getSummaryProductByUrl(url);
            System.out.println(sp.getPrice());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testFlipkartListProcessor() throws HttpFetchException, ContentParseException {

        FlipkartListProcessor processor = new FlipkartListProcessor();

        List<ListProduct> productList = processor.getProductSetByKeyword("FIRETALK YK-470 SHRINK EARHOOK NECKBAND EARPHONES Wired Headset (Pink)", 5);

        System.out.print(productList);

    }

}
