package hasoffer.core.test;

import hasoffer.affiliate.affs.flipkart.FlipkartAffiliateProductProcessor;
import hasoffer.affiliate.exception.AffiliateAPIException;
import hasoffer.affiliate.model.AffiliateProduct;
import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.core.product.impl.FetchServiceImpl;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.sites.paytm.PaytmListProcessor;
import hasoffer.fetch.sites.shopclues.ShopcluesListProcessor;
import hasoffer.fetch.sites.snapdeal.SnapdealListProcessor;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created on 2016/3/15.
 */
public class FetchProductByKeyword {

    @Test
    public void testFlipkart() {

        FlipkartAffiliateProductProcessor filpkartAffilicateProductProcessor = new FlipkartAffiliateProductProcessor();
        try {

            List<AffiliateProduct> keywordProduct = filpkartAffilicateProductProcessor.getAffiliateProductByKeyword("Badhiyadeal Back Cover for Le Eco LeTv Le 1s", 5);

            System.out.println(keywordProduct);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSnapdeal() {

        SnapdealListProcessor snapdealListProcessor = new SnapdealListProcessor();
        try {
            List<ListProduct> iphoneProduct = snapdealListProcessor.getProductSetByKeyword("infocus bingo 50", 5);
            System.out.println(iphoneProduct);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testShopclues() {

        ShopcluesListProcessor shopcluesListProcessor = new ShopcluesListProcessor();

        try {

            List<ListProduct> iphoneProduct = shopcluesListProcessor.getProductSetByKeyword("iphone", 5);

            System.out.println(iphoneProduct);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testPaytm() {

        PaytmListProcessor paytmListProcessor = new PaytmListProcessor();

        try {

            List<ListProduct> iphoneProduct = paytmListProcessor.getProductSetByKeyword("iphone", 5);

            System.out.println(iphoneProduct);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testFetchService() {
        String url = "http://www.amazon.in/gp/offer-listing/B00ZWNQDBA";

        FetchServiceImpl fetchService = new FetchServiceImpl();

        OriFetchedProduct oriFetchedProduct = null;

        try {
            oriFetchedProduct = fetchService.fetchSummaryProductByUrl(url);
        } catch (HttpFetchException e) {
            e.printStackTrace();
        } catch (ContentParseException e) {
            e.printStackTrace();
        } catch (AffiliateAPIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(oriFetchedProduct);

    }

}
