package hasoffer.affiliate.test;

import hasoffer.affiliate.affs.amazon.SignedRequestsHelper;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Date : 2016/3/7
 * Function :
 */
public class Test {

    private static final String ENDPOINT = "webservices.amazon.com";
    private static final String AWS_ACCESS_KEY_ID = "AKIAIVSZWNDPM4NI34WQ";
    private static final String AWS_SECRET_KEY = "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";

    public static void main(String[] args) throws HttpFetchException {
        testItemLookUp();
    }

    public static void t() throws HttpFetchException {
        StringBuffer sb = new StringBuffer("http://webservices.amazon.com/onca/xml?");

        sb.append("Service=AWSECommerceService&");
        sb.append("AWSAccessKeyId=AKIAIVSZWNDPM4NI34WQ&");
        sb.append("AssociateTag=hasoffercom-21&");
        sb.append("Operation=ItemLookup&");
        sb.append("ItemId=B015J2S7GW&");
//        sb.append("ResponseGroup=OfferSummary&");
//        sb.append("Version=2013-08-01&");
        sb.append("Timestamp=2016-04-13T11:57:20Z&");
        sb.append("Signature=[Request Signature]");

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
        System.out.println(sdf.format(date));

        String requestUrl = sb.toString();

        HtmlUtils.getUrlRootTagNode(requestUrl);

    }


    /**
     * http://webservices.amazon.com/onca/xml?
     * Service=AWSECommerceService&
     * AWSAccessKeyId=[AWS Access Key ID]&
     * AssociateTag=[Associate Tag]&
     * Operation=ItemSearch&
     * Condition=All&
     * Availability=Available&
     * SearchIndex=Apparel&
     * Keywords=Shirt
     * &Timestamp=[YYYY-MM-DDThh:mm:ssZ]
     * &Signature=[Request Signature]
     */
    public static void testSearchByKeyword() {
        StringBuffer sb = new StringBuffer("http://webservices.amazon.com/onca/xml?");

        sb.append("Service=AWSECommerceService&");
        sb.append("AWSAccessKeyId=AKIAIFK23NBOOHJN6ZFA&");
        sb.append("AssociateTag=hasoffercom-21&");
        sb.append("Operation=ItemLookup&");
        sb.append("ItemId=B000A3UB2O&");
        sb.append("ResponseGroup=OfferSummary&");
        sb.append("Version=2013-08-01&");
        sb.append("Timestamp=2016-03-07T03:42:45+0800&");
        sb.append("Signature=[Request Signature]");

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd hh:mm:ssZ");
        System.out.println(sdf.format(date));
    }

    public static void testItemLookUp() {

        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Map<String, String> params = new HashMap<String, String>();

        params.put("Service", "AWSECommerceService");
        params.put("AWSAccessKeyId", "AKIAIFK23NBOOHJN6ZFA");
        params.put("AssociateTag", "hasoffercom-21");
        params.put("Operation", "ItemLookup");
        params.put("ItemId","B000A3UB2O");
        params.put("ResponseGroup","OfferSummary");
        params.put("Version","2013-08-01");
        params.put("Timestamp", "2016-03-07T03:42:45+0800");

        String requesUrl = helper.sign(params);

//        String responseString = ItemLookupSample.fetch(requesUrl);

//        System.out.println(responseString);

    }
}
