package hasoffer.fetch.sites.indiatimes;

import hasoffer.base.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2016/3/2.
 */
public class IndiatimesHelper {

    private static final Pattern PRODUCT_URL_ID_PATTERN = Pattern.compile(".+/p_B([0-9]+).*");

    public static String getProductIdByUrl(String pageUrl) {
        Matcher matcher = PRODUCT_URL_ID_PATTERN.matcher(pageUrl);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getUrlWithAff(String url) {
        String url0 = "http://tracking.vcommission.com/aff_c?offer_id=1060&aff_id=48424&url=";
        return url0 + StringUtils.urlEncode(url + "?utm_source=vcommission&utm_medium=affiliate");
    }

    public static void main(String[] args) {
        String url1 = "http://shopping.indiatimes.com/mobiles/reach-mobile/reach-zeal-r4001-black/44520/p_B5690981";
        String url2 = "http://tracking.vcommission.com/aff_c?offer_id=1060&aff_id=48424&url=http%3A%2F%2Fshopping.indiatimes.com%2Fmobiles%2Freach-mobile%2Freach-zeal-r4001-black%2F44520%2Fp_B5690981%3Futm_source%3Dvcommission%26utm_medium%3Daffiliate";

        System.out.println(StringUtils.urlDecode(url2));

        System.out.println(url2.equals(getUrlWithAff(url1)));
    }
}
