package hasoffer.fetch.sites.shopclues;

import hasoffer.base.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date : 2016/3/28
 * Function :
 */
public class ShopcluesHelper {

    private static String SHOPCLUES_LOCAL_AFF_TEMP = "http://affiliateshopclues.com/?a=2892&c=22&p=r&s1=hasoffer&s2=%s&ckmrdr=%s";

    private static String SHOPCLUES_URL1 = "http://affiliateshopclues.com/?a=9&c=19&p=r&s1={affiliate_id}&s2={transaction_id}&ckmrdr=%s?utm_source=vcommission&utm_medium=CPS&s2=homepage";

    private static String SHOPCLUES_URL2 = "http://tracking.vcommission.com/aff_c?offer_id=122&aff_id=48424&url=";


    private static Pattern URL_PATTERN = Pattern.compile("http://www.shopclues.com/(.*).html.*");
    private static Pattern KEYWORD_PATTERN = Pattern.compile("(.*)-\\d+");

    /**
     * 使用了 第三方：vcommission 的联盟系统
     *
     * @param url
     * @return
     */
    public static String getUrlWithAff(String url) {
        String url1 = String.format(SHOPCLUES_URL1, url);

        return SHOPCLUES_URL2 + StringUtils.urlEncode(url1);
    }

    public static String getKeywordFromSkuUrl(String url) {
        Matcher m = URL_PATTERN.matcher(url);
        if (m.matches()) {
            String keyword = m.group(1);
            if (StringUtils.isEmpty(keyword)) {
                return "";
            }

            Matcher m2 = KEYWORD_PATTERN.matcher(keyword);
            if (m2.matches()) {
                keyword = m2.group(1);
            }

            return keyword.replace("-", " ");
        }
        return "";
    }

    public static String getCleanUrl(String oriUrl) {

        if (oriUrl.contains("?")) {
            return oriUrl.substring(0, oriUrl.indexOf("?"));
        }

        return oriUrl;
    }

    public static void main(String[] args) {
        String url = "http://www.shopclues.com/apple-iphone-5s-16gb-44.html";
        String target = "http://tracking.vcommission.com/aff_c?offer_id=122&aff_id=48424&url=http%3A%2F%2Faffiliateshopclues.com%2F%3Fa%3D9%26c%3D19%26p%3Dr%26s1%3D%7Baffiliate_id%7D%26s2%3D%7Btransaction_id%7D%26ckmrdr%3Dhttp%3A%2F%2Fwww.shopclues.com%2Fmtech-v4-black-16gb-java-enabled-preloaded-whats-app-mobile-phone.html%3Futm_source%3Dvcommission%26utm_medium%3DCPS%26s2%3Dhomepage";

        String deeplinkWithAff = getDeeplinkWithAff(url, new String[]{});
        System.out.println(deeplinkWithAff);
        System.out.println(getKeywordFromSkuUrl(url));
    }

    public static String getUrlWithAff(String url, String[] affs) {
        if (url.contains("affiliateshopclues")) {
            url = url.replace("affiliateshopclues", "www.shopclues");
        }
        String cleanUrl = getCleanUrl(url);
        return cleanUrl + "?ty=0&id=111438445&mcid=aff&utm_source=Hasoffer&OfferId=15";
        /*if (affs == null) {
            if (url.contains("affiliateshopclues")) {
                url = url.replace("affiliateshopclues", "www.shopclues");
            }
            String cleanUrl = getCleanUrl(url);
            return cleanUrl + "?ty=0&id=111438445&mcid=aff&utm_source=Hasoffer&OfferId=15";
        } else {
            //return getDeeplinkWithAff(url, affs);
            if (url.contains("affiliateshopclues")) {
                url = UrlUtils.getParam(url, "ckmrdr");
                url = StringUtils.urlDecode(url);
            }

            String market = "";
            if (affs != null && affs.length > 1) {
                market = affs[0];
            }

            return String.format(SHOPCLUES_LOCAL_AFF_TEMP, market, StringUtils.urlEncode(url));
        }*/

    }

    public static String getDeeplinkWithAff(String url, String[] affs) {
        return getUrlWithAff(getCleanUrl(url), affs);
        // return getCleanUrl(url) + "?ty=0&id=111438445&mcid=aff&utm_source=Hasoffer&OfferId=15";
    }
}
