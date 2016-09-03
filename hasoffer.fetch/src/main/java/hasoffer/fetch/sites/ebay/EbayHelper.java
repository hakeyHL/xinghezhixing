package hasoffer.fetch.sites.ebay;

import hasoffer.base.config.AppConfig;
import hasoffer.base.enums.HasofferRegion;
import hasoffer.base.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2016/3/2.
 */
public class EbayHelper {

    private static final Pattern PRODUCT_URL_ID_PATTERN = Pattern.compile(".+/([0-9]+).*");

    private static String EBAY_US_URL_TEMP = "http://rover.ebay.com/rover/1/711-53200-19255-0/1?" +
            "icep_ff3=2&pub=5575193158&toolid=10001&campid=5337909688&customid=&icep_item=%s&ipn=psmain&icep_vectorid=229466&kwid=902099&mtid=824&kw=lg";

    public static String getProductIdByUrl(String pageUrl) {
        Matcher matcher = PRODUCT_URL_ID_PATTERN.matcher(pageUrl);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return "";
    }

    private static String getUrlWithAff_IN(String url) {
        String url0 = "http://tracking.vcommission.com/aff_c?offer_id=1018&aff_id=48424&url=";

        String url1 = "http://rover.ebay.com/rover/1/4686-203594-43235-14/4?mpre=%s?aff_source=vcom";

        return url0 + StringUtils.urlEncode(String.format(url1, url));
    }

    public static String getUrlWithAff(String url) {
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
        String proId = getProductIdByUrl(url);
        return String.format(EBAY_US_URL_TEMP, proId);
    }

    public static void main(String[] args) {
        String url1 = "http://www.ebay.in/itm/Micromax-Canvas-Pace-4G-Q416-Black-/131671935192";
        String url2 = "http://tracking.vcommission.com/aff_c?offer_id=1018&aff_id=48424&url=http%3A%2F%2Frover.ebay.com%2Frover%2F1%2F4686-203594-43235-14%2F4%3Fmpre%3Dhttp%3A%2F%2Fwww.ebay.in%2Fitm%2FMicromax-Canvas-Pace-4G-Q416-Black-%2F131671935192%3Faff_source%3Dvcom";

        System.out.println(StringUtils.urlDecode(url2));

        System.out.println(url2.equals(getUrlWithAff(url1)));
    }

    public static String getCleanUrl(String url) {
        int index = url.indexOf("?");
        if (index > 0) {
            return url.substring(0, index);

        }
        return url;
    }

    // http://www.ebay.in/itm/Brand-New-HTC-Desire-310-1-Year-HTC-India-Mfg-Warranty-White-/121308227574
    // http://tracking.vcommission.com/aff_c?offer_id=1018&aff_id=48424&url=http%3A%2F%2Frover.ebay.com%2Frover%2F1%2F4686-203594-43235-14%2F4%3Fmpre%3Dhttp%3A%2F%2Fwww.ebay.in%2Fitm%2FBrand-New-HTC-Desire-310-1-Year-HTC-India-Mfg-Warranty-White-%2F121308227574%3Faff_source%3Dvcom
}
