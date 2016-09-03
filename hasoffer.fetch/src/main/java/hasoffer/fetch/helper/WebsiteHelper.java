package hasoffer.fetch.helper;

import hasoffer.base.config.AppConfig;
import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.UrlUtils;
import hasoffer.fetch.sites.amazon.AmazonHelper;
import hasoffer.fetch.sites.ebay.EbayHelper;
import hasoffer.fetch.sites.flipkart.FlipkartHelper;
import hasoffer.fetch.sites.indiatimes.IndiatimesHelper;
import hasoffer.fetch.sites.infibeam.InfibeamHelper;
import hasoffer.fetch.sites.paytm.PaytmHelper;
import hasoffer.fetch.sites.shopclues.ShopcluesHelper;
import hasoffer.fetch.sites.snapdeal.SnapdealHelper;
import hasoffer.fetch.sites.theitdepot.TheitdepotHelper;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date : 2015/12/21
 * Function :
 */
public class WebsiteHelper {

    private static final String MGSVC_KEY = "6f4922f45568161a8cdf4ad2299f6d23";
    private static final String MGSVC_URL_TEMP = "http://r.brandreward.com/?key=6f4922f45568161a8cdf4ad2299f6d23&id=%s&url=%s";

    public static Map<Website, String> packageMap = new HashMap();

    public static List<Website> DEFAULT_WEBSITES = new ArrayList<Website>();

    static {
        packageMap.put(Website.FLIPKART, "com.flipkart.android");
        packageMap.put(Website.SNAPDEAL, "com.snapdeal.main");
        packageMap.put(Website.PAYTM, "net.one97.paytm");
        packageMap.put(Website.AMAZON, "in.amazon.mShop.android.shopping");
        packageMap.put(Website.SHOPCLUES, "com.shopclues");
        packageMap.put(Website.EBAY, "com.ebay.mobile");
        packageMap.put(Website.HOMESHOP18, "com.homeshop18.activity");
        packageMap.put(Website.INDIATIMES, "com.shopping");
        packageMap.put(Website.MYNTRA, "com.myntra.android");
        packageMap.put(Website.JABONG, "com.jabong.android");
        packageMap.put(Website.PURPLLE, "com.manash.purplle");

        DEFAULT_WEBSITES.add(Website.FLIPKART);
        DEFAULT_WEBSITES.add(Website.SNAPDEAL);
        DEFAULT_WEBSITES.add(Website.SHOPCLUES);
        DEFAULT_WEBSITES.add(Website.PAYTM);
        DEFAULT_WEBSITES.add(Website.EBAY);
        DEFAULT_WEBSITES.add(Website.AMAZON);
        DEFAULT_WEBSITES.add(Website.INFIBEAM);
    }

    public static Website getWebSite(String url) {
        try {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                URL u = new URL(url);
                String[] ss = u.getHost().split("\\.");
                return Website.valueOf(ss[ss.length - 2].toUpperCase());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static String getPackage(Website website) {
        return packageMap.get(website);
    }

    private static String getMgsvcUrl(String urlId, String dl) {
        String encodeUrl = StringUtils.urlEncode(dl);
        return String.format(MGSVC_URL_TEMP, urlId, encodeUrl);
    }

//    public static String getSiteUrl(String urlId, Website website, String url) {
//        return getMgsvcUrl(urlId, url);
//    }
//
//    public static String getDeeplink2(String urlId, Website website, String url) {
//        String dl = getDeeplink(website, url);
//        return getMgsvcUrl(urlId, dl);
//    }

    public static String getUrlWithAff(String url) {
        Website website = getWebSite(url);

        String[] affs = new String[0];

        switch (website) {
            case FLIPKART:
                return FlipkartHelper.getUrlWithAff(url, affs);
            case PAYTM:
                return PaytmHelper.getUrlWithAff(url);
            case AMAZON:
                return AmazonHelper.getUrlWithAff(url);
            case SNAPDEAL:
//                url = SnapdealHelper.getCleanUrl(url);
//                return getMgsvcUrl(String.valueOf(skuId), url);
                return SnapdealHelper.appendAff(SnapdealHelper.getDeeplink(url), affs);
            case SHOPCLUES:
                return ShopcluesHelper.getUrlWithAff(url, affs);
            case EBAY:
                return EbayHelper.getUrlWithAff(url);
            case INFIBEAM:
                return InfibeamHelper.getUrlWithAff(url, affs);
            case INDIATIMES:
                return IndiatimesHelper.getUrlWithAff(url);
            case CROMARETAIL:
            case THEITDEPOT:
            case ASKMEBAZAAR:
                break;
            default:
                break;
        }

        return url;
    }

    public static String getDealUrlWithAff(Website website, String url, String[] affs) {

        if (StringUtils.isEmpty(url)) {
            return StringUtils.EMPTY_STRING;
        }

        if (website == null) {
            website = getWebSite(url);
        }

        switch (website) {
            case FLIPKART:
                return FlipkartHelper.getDealUrlWithAff(url, affs);
            case PAYTM:
                return url;
            case AMAZON:
                return url;
            case SNAPDEAL:
                return getUrlWithAff(website, url, affs);
            case SHOPCLUES:
                return getUrlWithAff(website, url, affs);
            case EBAY:
                return url;
            case INFIBEAM:
                return url;
            case INDIATIMES:
                return url;
            case CROMARETAIL:
                return url;
            case THEITDEPOT:
                return url;
            case ASKMEBAZAAR:
                return url;
            default:
                return url;
        }
    }

    public static String getUrlWithAff(Website website, String url, String[] affs) {

        if (StringUtils.isEmpty(url)) {
            return StringUtils.EMPTY_STRING;
        }

        if (website == null) {
            website = getWebSite(url);
        }

        switch (website) {
            case FLIPKART:
                return FlipkartHelper.getUrlWithAff(url, affs);
            case PAYTM:
                return PaytmHelper.getUrlWithAff(url);
            case AMAZON:
                return AmazonHelper.getUrlWithAff(url);
            case SNAPDEAL:
//                url = SnapdealHelper.getCleanUrl(url);
//                return getMgsvcUrl(String.valueOf(skuId), url);
                return SnapdealHelper.appendAff(SnapdealHelper.getDeeplink(url), affs);
            case SHOPCLUES:
                return ShopcluesHelper.getUrlWithAff(url, affs);
            case EBAY:
                return EbayHelper.getUrlWithAff(url);
            case INFIBEAM:
                return InfibeamHelper.getUrlWithAff(url, affs);
            case INDIATIMES:
                return IndiatimesHelper.getUrlWithAff(url);
            case CROMARETAIL:
            case THEITDEPOT:
            case ASKMEBAZAAR:
                break;
            default:
                break;
        }

        return url;
    }

    public static String getDeeplinkWithAff(Website website, String url, String[] affs) {
        if (website == null) {
            website = getWebSite(url);
        }

        switch (website) {
            case FLIPKART:
                return FlipkartHelper.getUrlWithAff(FlipkartHelper.getDeeplink(url), affs);
            case PAYTM:
                break;
//                return PaytmHelper.getDeeplink(url);
            case AMAZON:
                return getUrlWithAff(website, url, affs);
                /*String amazonDl = AmazonHelper.getDeeplink(url);
                if (StringUtils.isEmpty(amazonDl)) {
                    break;
                }
                return amazonDl;*/
            case SNAPDEAL:
//                url = SnapdealHelper.getCleanUrl(url);
//                return getMgsvcUrl(String.valueOf(skuId), url);
                return SnapdealHelper.appendAff(SnapdealHelper.getDeeplink(url), affs); // local aff of snapdeal
//                return getUrlWithAff(website, url, 0);
//                return SnapdealHelper.getDeeplink(url);
            case SHOPCLUES:
                return ShopcluesHelper.getDeeplinkWithAff(url, affs);
            case EBAY:
            case INFIBEAM:
            case ASKMEBAZAAR:
            case INDIATIMES:
            case CROMARETAIL:
            case THEITDEPOT:
            default:
                break;
        }
        // 这里返回空，即如果没有deeplink，返回空字符串
        return StringUtils.EMPTY_STRING;
    }

    public static String getLogoUrl(Website website) {
        String path = "";
        if (website == null) {
            path = path + "NULL.png";
        } else {
            path = path + website.name() + ".jpg";
        }
        return AppConfig.get(AppConfig.IMAGE_SITELOGO_PATH) + path;
    }

    public static String getBiggerLogoUrl(Website website) {
        String path = "";
        if (website == null) {
            path = path + "NULL.png";
        } else {
            path = path + website.name() + "_b.jpg";
        }
        return AppConfig.get(AppConfig.IMAGE_SITELOGO_PATH) + path;
    }

    public static String getRealUrl(String oriUrl) {
        String url = oriUrl;

        if (!StringUtils.isEmpty(url)) {
            if (url.startsWith("http://track.in.omgpm.com/")) {
                String rUrl = UrlUtils.getParam(url, "r");
                url = StringUtils.urlDecode(rUrl);
            } else if (url.startsWith("http://mysmartprice.go2cloud.org")) {
                String rUrl = UrlUtils.getParam(url, "url");
                url = StringUtils.urlDecode(rUrl);
            } else if (url.startsWith("http://affiliateshopclues.com")) {
                String rUrl = UrlUtils.getParam(url, "ckmrdr");
                url = StringUtils.urlDecode(rUrl);
            } else if (url.contains("Frover.ebay.com")) {
                if (url.startsWith("http://")) {
                    url = StringUtils.urlDecode(UrlUtils.getParam(url, "mpre"));
                } else {
                    url = StringUtils.urlDecode(UrlUtils.getParam(StringUtils.urlDecode(url), "mpre"));
                }
            }

            int endIndex = url.indexOf("?");

            String suffix = getSuffix(url);

            if (endIndex > 0) {
                url = url.substring(0, endIndex);
            }

            url += suffix;
        }
        return url;
    }

    private static String getSuffix(String url) {

        Website webSite = getWebSite(url);

        if (Website.FLIPKART.equals(webSite)) {
            String sourceId = FlipkartHelper.getProductIdByUrl(url);
            return "?pid=" + sourceId;
        } else if (Website.THEITDEPOT.equals(webSite)) {
            String sourceId = TheitdepotHelper.getSourceIdByUrl(url);
            return "?prodid=" + sourceId;
        }

        return "";
    }

    public static String getSearchUrl(Website website, String keyword) {
        if (website == null) {
            return "";
        }

        String url = "";
        keyword = URLEncoder.encode(keyword);

        switch (website) {
            case FLIPKART:
                url = "http://www.flipkart.com/search?otracker=start&as-show=on&as=off&q=";
                return url + keyword;
            case PAYTM:
//                url = "https://paytm.com/shop/search/?from=organic&q=";
                url = "https://paytm.com/search/?page_count=1&items_per_page=30&resolution=960x720&quality=high&curated=1&cat_tree=1&userQuery=" + URLDecoder.decode(keyword) + "&from=organic&callback=angular.callbacks._c&channel=web&version=2";
                return url;
            case AMAZON:
                url = "http://www.amazon.in/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=";
                return url + keyword;
            case SNAPDEAL:
                url = "http://www.snapdeal.com/search?keyword=";
                return url + URLDecoder.decode(keyword);
            case SHOPCLUES:
                url = "http://search.shopclues.com/?dispatch=products.search&q=";
                return url + keyword;
            case EBAY:
                url = "http://www.ebay.in/sch/i.html?_nkw=";
                return url + keyword;
            case INFIBEAM:
                url = "http://www.infibeam.com/search?us=nbc&q=";
                return url + keyword;
            case ASKMEBAZAAR:
                url = "http://www.askmebazaar.com/index.php?search_query=";
                return url + keyword;
            case INDIATIMES:
                url = "http://shopping.indiatimes.com/mtkeywordsearch?SEARCH_STRING=";
                return url + keyword;
            case MYSMARTPRICE:
                url = "http://www.mysmartprice.com/msp/search/search.php?#s=";
                return url + keyword;
            default:
                break;
        }

        return null;
    }

    public static String getCleanUrl(Website website, String url) {
        switch (website) {
            case FLIPKART:
                return FlipkartHelper.getCleanUrl(url);
            case SNAPDEAL:
                return SnapdealHelper.getCleanUrl(url);
            case SHOPCLUES:
                return ShopcluesHelper.getCleanUrl(url);
            case PAYTM:
                return PaytmHelper.getCleanUrl(url);
            case EBAY:
                return EbayHelper.getCleanUrl(url);
            case AMAZON:
                return AmazonHelper.getCleanUrl(url);
            default:
                return url;
        }
    }

    public static String getProductIdFromUrl(Website website, String url) {
        switch (website) {
            case FLIPKART:
                return FlipkartHelper.getProductIdByUrl(url);
            default:
                return "";
        }
    }

    public static String getSkuIdFromUrl(Website website, String url) {
        switch (website) {
            case FLIPKART:
                return FlipkartHelper.getSkuIdByUrl(url);
            case AMAZON:
                return AmazonHelper.getProductIdByUrl(url);
            case SNAPDEAL:
                return SnapdealHelper.getProductIdByUrl(url);
            default:
                return "";
        }
    }

    public static String getAdtUrlByWebSite(Website website, String url) {
        switch (website) {
            case FLIPKART:
                if (url.contains("?")) {
                    url += "&affid=" + AffliIdHelper.getAffiIdByWebsite(website);
                } else {
                    url += "?affid=" + AffliIdHelper.getAffiIdByWebsite(website);
                }
                return url;
//            case SNAPDEAL:
//                return AmazonHelper.getProductIdByUrl(url);
//            case SHOPCLUES:
//                return SnapdealHelper.getProductIdByUrl(url);
            default:
                return url;
        }
    }
}
