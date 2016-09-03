package hasoffer.fetch.sites.flipkart;

import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.UrlUtils;
import hasoffer.fetch.helper.AffliIdHelper;

/**
 * Date:2015/12/30
 */
public class FlipkartHelper {

    public static String getProductIdByUrl(String pageUrl) {

        pageUrl = getCleanUrl(pageUrl);

        int index = pageUrl.indexOf("?");
        if (index > 0) {
            pageUrl = pageUrl.substring(0, index);
        }

        return pageUrl.substring(pageUrl.lastIndexOf("/") + 1);
    }

    public static String getSkuIdByUrl(String pageUrl) {

        pageUrl = getCleanUrl(pageUrl);

        return UrlUtils.getParam(pageUrl, "pid");
    }

    public static String getCleanUrl(String url) {
        int index = url.indexOf("?");

        if (index < 0) {
            return url;
        }

        String pid = UrlUtils.getParam(url, "pid");

        return url.substring(0, index) + "?pid=" + pid;
    }

    /**
     * #目标
     * http://dl.flipkart.com/dl/alcatel-onetouch-idol-X-6040d/p/itmdthghx79dtzwf?affid=affiliate357
     * <p/>
     * #可能的情况
     * http://www.flipkart.com/karbonn-k106s/p/itmeabrpjprkbmx5
     * http://dl.flipkart.com/dl/karbonn-k106s/p/itmeabrpjprkbmx5
     *
     * @param url
     * @return
     */
    public static String getDeeplink(final String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        String dl = getCleanUrl(url);

        if (dl.contains("www.flipkart.com")) {
            dl = dl.replace("www.flipkart.com", "dl.flipkart.com/dl");
        }

        return dl;
    }

    public static String getUrlByDeeplink(final String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        String dl = url;

        if (url.contains("dl.flipkart.com/dl")) {
            return dl.replace("dl.flipkart.com/dl", "www.flipkart.com");
        }

        return url;
    }

    public static String getUrlWithAff(String url, String[] affs) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        url = getCleanUrl(url);

        return appendAff(url, affs);
    }

    public static String getDealUrlWithAff(String url, String[] affs) {
        url = getDeeplink(url);
        return appendAff(url, affs);
    }

    private static String appendAff(String url, String[] affs) {
        StringBuffer sb = new StringBuffer(url);

        String affid = AffliIdHelper.getAffiIdByWebsite(Website.FLIPKART);

        if (sb.indexOf("?") > 0) {
            sb.append("&affid=").append(affid);
        } else {
            sb.append("?affid=").append(affid);
        }

        if (affs != null && affs.length >= 1) {
//            int i = 1;
//            for (String aff : affs) {
//                sb.append("&affExtParam").append(i++).append("=").append(aff);
//            }
            sb.append("&affExtParam1=").append(affs[0]);

            if (affs.length >= 2) {
                String deviceUser = affs[1];
                if (affs.length == 3) {
                    deviceUser += "_" + affs[2];
                }
                sb.append("&affExtParam2=").append(deviceUser);
            }
        }

        return sb.toString();
    }
}
