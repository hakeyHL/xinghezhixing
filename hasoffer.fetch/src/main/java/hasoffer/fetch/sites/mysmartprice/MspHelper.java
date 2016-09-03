package hasoffer.fetch.sites.mysmartprice;

import hasoffer.base.model.Website;
import hasoffer.fetch.helper.WebsiteHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chevy on 2015/12/4.
 * mysmartprice.com 是一个专注于印度市场的比价网站，made in india
 * 获取PriceTableUrl
 */
public class MspHelper {

    public final static String SITE_URL = "http://www.mysmartprice.com/";

    public final static String MSP_DEAL_URL = "http://www.mysmartprice.com/deals/";

    public final static String MSP_REDIRECT_DOMAIN = "mysmartprice.go2cloud.org";
    public final static String MSP_PRODUCTS_AJAX_URL =
            "http://www.mysmartprice.com/fashion/filters/filter_get_revamp?recent=0&q=filter%2F&subcategory=cases-covers&start=0&rows=200&page_name=";
    private final static String MSP_PRICE_TABLE_AJAXURL =
            "http://www.mysmartprice.com/mobile/filter_response.php?activetab=tab_price&storetype=online&mspid=%s";
    private final static Pattern PATTERN_URL_1 = Pattern.compile("http://www.mysmartprice.com/.+-ms[p|f](\\d+).*");

    private final static Pattern PATTERN_URL_2 = Pattern.compile(".+/(\\d+)");

    public static String getPriceTableUrl(String mspId) {
        return String.format(MSP_PRICE_TABLE_AJAXURL, mspId);
    }

    public static String getProductIdByUrl(String productUrl) {
        Matcher m = PATTERN_URL_1.matcher(productUrl);
        if (m.matches()) {
            return m.group(1);
        }

        m = PATTERN_URL_2.matcher(productUrl);
        if (m.matches()) {
            return m.group(1);
        }

        return null;
    }

    public static String getProductIdByUrl2(String productUrl) {
        String pid = productUrl.substring(productUrl.lastIndexOf("-"));
        Pattern pattern = Pattern.compile("\\d+");
        Matcher m = pattern.matcher(pid);
        if (m.find()) {
            return m.group(0);
        }
        return null;
    }

    public static String getRealUrl(final String url) {
        String realUrl = url;
        Website website = WebsiteHelper.getWebSite(realUrl);

        if (website == null) {
            return null;
        }

        int q = url.indexOf("?");
        if (q > 0) {
            realUrl = url.substring(0, q);
        }

        if (Website.SNAPDEAL.equals(website)) {
            if (url.indexOf("viewAllSellers") > 0) {
                realUrl = realUrl.replace("viewAllSellers/", "");
                return realUrl;
            }
        }

        return "";
    }
}
