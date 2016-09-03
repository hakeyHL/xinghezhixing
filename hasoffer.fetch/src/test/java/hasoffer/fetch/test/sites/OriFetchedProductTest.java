package hasoffer.fetch.test.sites;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.fetch.core.ISummaryProductProcessor;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.helper.WebsiteSummaryProductProcessorFactory;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.sites.flipkart.FlipkartSummaryProductProcessor;
import org.junit.Test;

import java.net.URLDecoder;
import java.util.List;

/**
 * Created on 2016/3/1.
 */
public class OriFetchedProductTest {

    @Test
    public void testSummaryProdcut() {

        //AMAZON
        String urlTemplate1 = "http://www.amazon.in/Red-Label-Leaf-Carton-250g/dp/B015DVV4NM/ref=pd_rhf_dp_s_cp_4?ie=UTF8&dpID=519bKFkm5cL&dpSrc=sims&preST=_SL500_SR97%2C135_&refRID=0DBFEVD5CF8WMXN74HFA";
        ////FLIPKART（待测）
        String urlTemplate2 = "http://www.flipkart.com/lenovo-s8-50f/p/itmecgmeggyepnpg?pid=TABECGMDBCQ8UEMW&al=Y2Xid%2B79DGgNG6HPrslXn8ldugMWZuE7wkNiXfq8GiQlQOoCaMyHizIQQ1kwLBfjI2%2F8ZG5Honk%3D&ref=L%3A7708051946735223490&srno=b_2";
        //SNAPDEAL
        String urlTemplate3 = "http://www.snapdeal.com/product/chrome-azzaro-cologne-for-men/1790321502#bcrumbLabelId:728";
        //PAYTM
        String urlTemplate4 = "https://paytm.com/shop/p/oppo-neo-7-4g-black-MOBOPPO-NEO-7-4THE-3403010CD2312?src=grid&tracker=%7C%7C%7C%7C%2Fg%2Fmobile-accessories%2Fmobiles%2Fmobile-store%2Fsimple-tms-new%7C1";
        //INDIATIMES
        String urlTemplate5 = "http://shopping.indiatimes.com/fashion/t-shirts/top-notch-orange-cotton-men-polo-t-shirt-rcpt-do/43963/p_B2965459";
        //INFIBEAM
        String urlTemplate6 = "http://www.infibeam.com/Portable_Electronics/intex-pb11k-11000-mah-power-bank-with-3/P-poel-97192471423-cat-z.html#variantId=P-poel-25584906209";
        //SAHOLIC
        String urlTemplate7 = "http://www.saholic.com/cameras/nikon-coolpix-l320-1006586";
        //SYBERPLACE
        String urlTemplate8 = "https://www.syberplace.com/lenovo-k3-note-music-4g-black.html";
        //SHOPCLUES
        String urlTemplate9 = "http://www.shopclues.com/janasya-womens-pink-color-chiffon-sareee.html";
        //ASKMEBAZAAR
        String urlTemplate10 = "http://www.askmebazaar.com/Floral-Imported-Jacquard-Sleeveless-A-line-Skater-Dress-Blue-p4730173?defSearch=1";
        //BABYOYE
        String urlTemplate11 = "http://www.babyoye.com/h/Breast-Feeding-Accessories/Feeding-Pillows-&-Cushions/Bright-Starts-Mombo-Feeding-Pillow---Yellow/p_BPBRS00026?ref=block1";
        //CROMARETAIL
        String urlTemplate12 = "http://www.cromaretail.com/Braun-Series-7-799cc-Wet--Dry-Mens-Shaver-(Black)-pc-30780-748.aspx";
        //EBAY
        String urlTemplate13 = "http://www.ebay.in/itm/331786098311?_trkparms=clkid%3D3684280102211142729&_qi=RTM2306625";
        //FIRSTCRY
        String urlTemplate14 = "http://www.firstcry.com/lilliput-kids/lilliput-kids-half-sleeves-t-shirt-numeric-65-print-yellow/766019/product-detail";
        //MANIACSTORE
        String urlTemplate15 = "http://www.maniacstore.com/Petrafab-Faux-Georgette-and-Heavy-Dani-Georgette-Sarees-Combo-Deal-Buy-1-Get-2Nd-Free-Petrafabcombo_41.html";
        //NAAPTOL
        String urlTemplate16 = "http://www.naaptol.com/tablet-carry-cases/3-in-1-multipurpose-pillow/p/12478996.html";
        //PURPLLE
        String urlTemplate17 = "http://purplle.com/product/vaadi-herbals-elbow-foot-knee-scrub-soap-with-almond-and-walnut-scrub-75gm?ref=tm_bs_4";
        //SHOPMONK
        String urlTemplate18 = "https://shopmonk.com/product/onyx-studio-2";

        String urlTemplate = "http://www.snapdeal.com/product/vecom-black-plain-polyester-casual/1253304625";

        urlTemplate = URLDecoder.decode(urlTemplate);

        Website webSite = WebsiteHelper.getWebSite(urlTemplate);

        ISummaryProductProcessor summaryProductProcessor = WebsiteSummaryProductProcessorFactory.getSummaryProductProcessor(webSite);

        OriFetchedProduct product = null;
        try {

            product = summaryProductProcessor.getSummaryProductByUrl(urlTemplate);
            ProductStatus productStatus = product.getProductStatus();
            if (ProductStatus.OFFSALE.equals(productStatus)) {
                String url = product.getUrl();
                System.out.println("url=" + url);
                System.out.println("status=" + productStatus.name());
                String website = product.getWebsite().name();
                System.out.println("website=" + website);
            } else {
                float price = product.getPrice();
                String title = product.getTitle();
                String url = product.getUrl();
                String sourceId = product.getSourceSid();
                String website = product.getWebsite().name();
                String imageUrl = product.getImageUrl();
                System.out.println("status=" + productStatus.name());
                System.out.println("title=" + title);
                System.out.println("price=" + price);
                System.out.println("url=" + url);
                System.out.println("sourceId=" + sourceId);
                System.out.println("website=" + website);
                System.out.println("imageUrl=" + imageUrl);
            }

        } catch (HttpFetchException e) {

            String message = e.getMessage();
            if(message.contains("302")||message.contains("404")){
                product = new OriFetchedProduct();
                product.setProductStatus(ProductStatus.OFFSALE);
                product.setWebsite(webSite);
                product.setUrl(urlTemplate);
            }

        } catch (ContentParseException e) {
            e.printStackTrace();
        }

    }



    @Test
    public void testSkuSummaryProduct() throws HttpFetchException, ContentParseException {

        String urlTemplate = "http://www.flipkart.com/apple-iphone-5s/p/itme8ra4f4twtsva";

        Website webSite = WebsiteHelper.getWebSite(urlTemplate);

        FlipkartSummaryProductProcessor flipkartSummaryProductProcessor = new FlipkartSummaryProductProcessor();

        List<OriFetchedProduct> productList = flipkartSummaryProductProcessor.getSkuSummaryProductByUrl(urlTemplate);

        System.out.println(productList);
    }

}
