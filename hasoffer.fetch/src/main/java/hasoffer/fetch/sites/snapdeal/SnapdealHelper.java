package hasoffer.fetch.sites.snapdeal;

import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.helper.AffliIdHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chevy on 2015/12/9.
 */
public class SnapdealHelper {
    public final static String SITE_URL = "http://www.snapdeal.com";

    private static final Pattern PRODUCT_URL_ID_PATTERN = Pattern.compile(".*/([0-9]+).*");

    /*public static String getProductIdByUrl(String productUrl) {
        Matcher matcher = PRODUCT_URL_ID_PATTERN.matcher(productUrl);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }*/

    public static String getUrlByProductId(String productId) {
        return null;
    }

    public static String getProductIdByUrl(String pageUrl) {

        if (pageUrl.contains("?")) {
            pageUrl = pageUrl.substring(0, pageUrl.indexOf("?"));
        }

        return pageUrl.substring(pageUrl.lastIndexOf("/") + 1);
    }

    public static int getReviewCountFromStr(String reviewStr) {
        Pattern p = Pattern.compile("^\\s*(\\d+)[\\s,\\S]*$");
        Matcher m = p.matcher(reviewStr);
        if (m.matches()) {
            return Integer.parseInt(m.group(1));
        }

        return 0;
    }

    public static String getDeeplink(String url) {
        // url = getCleanUrl(url);
        if (url.contains("www.snapdeal.com")) {
            url = url.replace("www.snapdeal.com", "m.snapdeal.com");
        }
        return url;
    }

    public static String appendAff(String url, String[] affs) {
        String aff_query = "?aff_id=" + AffliIdHelper.getAffiIdByWebsite(Website.SNAPDEAL) + "&utm_source=aff_prog&utm_campaign=afts&offer_id=17";
//        final String aff_query = "?utm_source=aff_prog&utm_campaign=afts&offer_id=17&aff_id=82856&aff_sub=613e3e809daad9e";

        StringBuffer sb = new StringBuffer(url);
        sb.append(aff_query);

        if (affs != null && affs.length > 0) {
//            int index = 1;
//            for (String aff : affs) {
//                sb.append("&aff_sub");
//                if (index != 1) {
//                    sb.append(index);
//                }
//                sb.append("=").append(aff);
//                index++;
//            }
            if (affs != null && affs.length >= 1) {
                sb.append("&aff_sub=").append(affs[0]);

                if (affs.length >= 2) {
                    String deviceUser = affs[1];
                    if (affs.length == 3) {
                        deviceUser += "_" + affs[2];
                    }
                    sb.append("&aff_sub2=").append(deviceUser);
                }
            }
        }

        return sb.toString();
    }

    public static String getCleanUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        if (url.startsWith("http://")) {
            url = url.replace("http://", "https://");
        }

        int bcrumbIndex = url.indexOf("#bcrumbSearch");
        if (bcrumbIndex > 0) {
            url = url.substring(0, bcrumbIndex);
        }

        if (url.contains("m.snapdeal.com")) {
            url = url.replace("m.snapdeal.com", "www.snapdeal.com");
        }

        url = url.replace("viewAllSellers/", "");

        int win = url.indexOf("?");
        if (win > 0) {
            url = url.substring(0, win);
        }

        return url;
    }

    public static String getUrlWithAff(String url) {

        if (StringUtils.isEmpty(url)) {
            return null;
        }

        return appendAff(getCleanUrl(url), new String[]{});
    }

    public static String getSkuIdByUrl(String url) {
        return getProductIdByUrl(url);
    }
}
