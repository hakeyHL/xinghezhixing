package hasoffer.fetch.sites.amazon;

import hasoffer.base.config.AppConfig;
import hasoffer.base.enums.HasofferRegion;
import hasoffer.base.exception.ContentParseException;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.exception.PriceNotFoundException;
import org.htmlcleaner.TagNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeStringByXPath;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/28
 */
public class AmazonHelper {

    private static final String XPATH_PRICE_CURRENT = "//*[@id='priceblock_ourprice']";
    private static final String XPATH_PRICE_CURRENT1 = "//*[@id='priceblock_dealprice']";
    private static final String XPATH_PRICE_CURRENT2 = "//span[@id='priceblock_saleprice']";

    private static final Pattern PRODUCT_URL_ID_PATTERN = Pattern.compile(
            ".+/dp/([A-Z0-9]+).*"
    );

    private static final Pattern PRODUCT_URL_ID_PATTERN2 = Pattern.compile(
            ".+/gp/product/([A-Z0-9]+)/*.*$"
    );

    private static final Pattern PRODUCT_URL_ID_PATTERN3 = Pattern.compile(
            ".+/gp/offer-listing/([A-Z0-9]+)/*.*$"
    );

    private static final Pattern PATTERN_CURRENT_PRICE = Pattern.compile("(\\d+.\\d+)");

    public static String getUrlByProductId(String productId) {
        return String.format("http://www.amazon.com/gp/product/%s", productId);
    }

    public static String getProductIdByUrl(String pageUrl) {
        Matcher matcher = PRODUCT_URL_ID_PATTERN.matcher(pageUrl);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        matcher = PRODUCT_URL_ID_PATTERN2.matcher(pageUrl);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        matcher = PRODUCT_URL_ID_PATTERN3.matcher(pageUrl);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        return null;
    }

    public static String getCurrentPriceString(TagNode root) throws ContentParseException {
        String priceNodeString = getSubNodeStringByXPath(root, XPATH_PRICE_CURRENT, null);
        if (StringUtils.isEmpty(priceNodeString)) {
            priceNodeString = getSubNodeStringByXPath(root, XPATH_PRICE_CURRENT1, null);
            if (StringUtils.isEmpty(priceNodeString)) {
                priceNodeString = getSubNodeStringByXPath(root, XPATH_PRICE_CURRENT2, null);
                if (StringUtils.isEmpty(priceNodeString)) {
                    throw new PriceNotFoundException("");
                }
            }
        }

        String priceString = priceNodeString;

        Matcher priceMatcher = PATTERN_CURRENT_PRICE.matcher(priceNodeString);
        if (priceMatcher.find()) {
            priceString = priceMatcher.group(1);
        }

        return priceString;
    }

    public static String getDeeplink(String url) {
        // todo AMAZON deep-link
        return "";
        /*String pid = AmazonHelper.getProductIdByUrl(url);
        if (StringUtils.isEmpty(pid)) {
            return "";
        }
        StringBuffer sb = new StringBuffer("intent:/azon.in/products/");
        sb.append(pid)
                .append("?dl_sid=277-3391083-6615335#Intent;")
                .append("scheme=com.amazon.mobile.shopping;")
                .append("package=in.amazon.mShop.android.shopping;")
                .append("S.browser_fallback_url=https://play.google.com/store/apps/details?id=com.amazon.mShop.android.shopping;")
                .append("end");
        return sb.toString();*/
    }

    public static String getUrlWithAff(String url) {
        int win = url.indexOf("?");
        if (win > 0) {
            url = url.substring(0, win);
        }

        String region = AppConfig.get(AppConfig.SER_REGION);

        HasofferRegion hr = HasofferRegion.valueOf(region);

        switch (hr) {
            case INDIA:
                return getUrlWithAff_IN(url);
            case USA:
                return getUrlWithAff_US(url);
            default:
                return url;
        }
    }

    private static String getUrlWithAff_US(String url) {
        return url + "?tag=hasoffer02-20";
    }

    private static String getUrlWithAff_IN(String url) {
        String pid = AmazonHelper.getProductIdByUrl(url);
        if (StringUtils.isEmpty(pid)) {
            return "";
        }

        if (!url.endsWith("/")) {
            url = url + "/";
        }

        url = url.replace("offer-listing", "product");

        StringBuffer sb = new StringBuffer(url);
        sb.append(String.format("ref=as_li_tl?ie=UTF8&camp=3626&creative=24790&creativeASIN=%s&linkCode=as2&tag=hasoffer0c-21", pid));

        return sb.toString();
    }

    public static String getCleanUrl(String url) {
        return url;
    }
    //http://www.amazon.in/gp/product/9350946661/ref=as_li_tl?ie=UTF8&camp=3626&creative=24790&creativeASIN=9350946661&linkCode=as2&tag=
}
