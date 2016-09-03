package hasoffer.fetch.sites.infibeam;

import hasoffer.base.utils.StringUtils;

/**
 * Date : 2016/3/28
 * Function :
 */
public class InfibeamHelper {

    public static String getCleanUrl(String url) {
        int index = url.indexOf("?");
        if (index > 0) {
            return url.substring(0, index);
        }

        return url;
    }

    public static String getUrlWithAff(String url, String[] affs) {
        url = getCleanUrl(url);

        StringBuffer sb = new StringBuffer(getCleanUrl(url));

        sb.append("?trackId=hasoffer");

        /*int index = 0;
        for (String aff : affs) {
            sb.append("&sub_id").append("index++").append("=").append(aff);
        }*/

        return sb.toString();
    }


    public static String getUrlWithAff_vcommission(String url) {
        String url0 = "http://tracking.vcommission.com/aff_c?offer_id=899&aff_id=48424&url=";

        return url0 + StringUtils.urlEncode(url + "?trackId=vcommission&subTrackId={affiliate_id}");
    }


    public static void main(String[] args) {
        String url1 = "http://www.infibeam.com/Mobiles/intex-aqua-y2-power/P-mobi-82334424571-cat-z.html";
        String url2 = "http://tracking.vcommission.com/aff_c?offer_id=899&aff_id=48424&url=http%3A%2F%2Fwww.infibeam.com%2FMobiles%2Fintex-aqua-y2-power%2FP-mobi-82334424571-cat-z.html%3FtrackId%3Dvcommission%26subTrackId%3D%7Baffiliate_id%7D";

        System.out.println(StringUtils.urlDecode(url2));
        System.out.println(getUrlWithAff(url1, new String[]{"1234", "456"}));
    }
}
