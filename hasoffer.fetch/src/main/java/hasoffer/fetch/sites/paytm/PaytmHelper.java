package hasoffer.fetch.sites.paytm;

import hasoffer.base.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date : 2016/2/29
 * Function :
 */
public class PaytmHelper {

    private static final Pattern PRODUCT_URL_ID_PATTERN = Pattern.compile(".+-([A-Z0-9]+)?.*");

    public static String getDeeplink(String url) {
        return "paytmmp://product?url=" + url;
    }

    public static String getProductIdByUrl(String pageUrl) {
        Matcher matcher = PRODUCT_URL_ID_PATTERN.matcher(pageUrl);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }

    public static void main(String[] args) {

        String url1 = "https://paytm.com/shop/p/intex-aqua-r2-grey-black-CMPLXMOBINTEX-AQUA-RDUMM14171E22580";

        String url2 = "http://tracking.vcommission.com/aff_c?offer_id=1022&aff_id=48424&url=https%3A%2F%2Fpaytm.com%2Fshop%2Fp%2Fintex-aqua-r2-grey-black-CMPLXMOBINTEX-AQUA-RDUMM14171E22580%3Futm_source%3DAffiliates%26utm_medium%3DVCOMM%26utm_campaign%3DVCOMM%26utm_term%3D%7Baffiliate_id%7D";

        System.out.println(url2.equals(getUrlWithAff(url1)));
    }

    public static String getUrlWithAff(String url) {

        String url1 = url + "?utm_source=Affiliates&utm_medium=VCOMM&utm_campaign=VCOMM&utm_term={affiliate_id}";

        String url2 = "http://tracking.vcommission.com/aff_c?offer_id=1022&aff_id=48424&url=";

        return url2 + StringUtils.urlEncode(url1);
    }

    public static String getCleanUrl(String oriUrl) {

        if(oriUrl==null) {
            return "";
        }

        if(oriUrl.contains("?src=")){
            String[] subStr = oriUrl.split("\\?src=");
            return subStr[0];
        }

        if(oriUrl.contains("?utm=")){
            String[] subStr1 = oriUrl.split("\\?utm=");
            return subStr1[0];
        }


        return oriUrl;
    }

    //https://paytm.com/shop/p/i-kall-k11-blue-black-MOBI-KALL-K11-BNIKO105609588C3C5B
    //http://tracking.vcommission.com/aff_c?offer_id=1022&aff_id=48424&url=https%3A%2F%2Fpaytm.com%2Fshop%2Fp%2Fi-kall-k11-blue-black-MOBI-KALL-K11-BNIKO105609588C3C5B%3Futm_source%3DAffiliates%26utm_medium%3DVCOMM%26utm_campaign%3DVCOMM%26utm_term%3D%7Baffiliate_id%7D
    //https://paytm.com/shop/p/intex-aqua-r2-grey-black-CMPLXMOBINTEX-AQUA-RDUMM14171E22580
    //http://tracking.vcommission.com/aff_c?offer_id=1022&aff_id=48424&url=https%3A%2F%2Fpaytm.com%2Fshop%2Fp%2Fintex-aqua-r2-grey-black-CMPLXMOBINTEX-AQUA-RDUMM14171E22580%3Futm_source%3DAffiliates%26utm_medium%3DVCOMM%26utm_campaign%3DVCOMM%26utm_term%3D%7Baffiliate_id%7D
}
