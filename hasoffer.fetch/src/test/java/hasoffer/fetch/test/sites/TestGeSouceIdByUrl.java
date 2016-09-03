package hasoffer.fetch.test.sites;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.sites.cromaretail.CromaretailSummaryProductProcessor;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2016/3/2.
 */
public class TestGeSouceIdByUrl {

    @Test
    public void testGetSourceIdByUrl(){

        String url1 = "http://www.babyoye.com/h/Hair-Clips-&-Bands/NeedyBee-Floral-Hairband---Blue-(-0-4-Years-)/p_BP20114475?ref=block1";
        String url2 ="http://www.babyoye.com/h/Tees,-Tops-&-Shirts/Tops/Oye-Girls-Embroidery-Top/p_BP20120589?ref=block1";
        String url3 = "http://www.babyoye.com/h/Cloth-Diapers-&-Nappies/One-Size-Cloth-Diaper/Pom-Pom-Velcro-U-Shape-Nappy-Set-Of-5/p_BP10084770?ref=block1";
        String url4 ="http://www.babyoye.com/h/Maternity-Skin-&-Hair-Care/Skin-Care/Palmers-Cocoa-Butter-Formula-Skin-Therapy-Oil-150-ml/p_BPPLM00004?ref=block1";
        String url5 = "http://www.babyoye.com/h/Diaper-Bags/All-Diaper-Bags/Advance-Baby-Flower-Print-Diaper-Bag---Green/p_BP20108869?ref=block1";
        String url6 = "http://www.babyoye.com/h/Strollers-&-Prams/Strollers/Babyoye-Jingles-Stroller/p_BP20128528?ref=block1";

        String url ="http://www.askmebazaar.com/Dell-3558-Laptop-p2080197?store_front_id=9352&search=1&defSearch=0";

        Pattern pattern = Pattern.compile(".+-p([0-9]+).+");

        Matcher matcher = pattern.matcher(url);

        if(matcher.matches()){
            String sourceId = matcher.group(1);
            System.out.println(sourceId);
        }

    }

    @Test
    public void testCromaretailSGetProductIdByUrl() throws HttpFetchException, ContentParseException {

        String url1 = "http://www.cromaretail.com/Croma-CRAO0042-1500W-Low-Oil-Fryer-(Black)-pc-27886-816.aspx";
        String url2 = "http://www.cromaretail.com/Canon-EOS-750D-242-MP-Digital-SLR-Camera-(18-55-mm)-(Black)-pc-29370-133.aspx";
        String url = "http://www.cromaretail.com/Lenovo-Miix-2-(8)-8%22-Tablet-(Black)-pc-23595-902.aspx";
        CromaretailSummaryProductProcessor cromaretailSummaryProductProcessor = new CromaretailSummaryProductProcessor();
        OriFetchedProduct oriFetchedProduct = cromaretailSummaryProductProcessor.getSummaryProductByUrl(url);
        System.out.println(oriFetchedProduct);

    }

}
