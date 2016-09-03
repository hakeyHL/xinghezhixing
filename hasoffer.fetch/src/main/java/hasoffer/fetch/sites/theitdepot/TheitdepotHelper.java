package hasoffer.fetch.sites.theitdepot;

import hasoffer.base.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2016/4/6.
 */
public class TheitdepotHelper {

    private static final Pattern PRODUCT_URL_SOURCEID_PATTERN = Pattern.compile(".*prodid=([0-9]+)$");

    public static String getSourceIdByUrl(String url){

        Matcher matcher = PRODUCT_URL_SOURCEID_PATTERN.matcher(url);

        if(matcher.matches()){
            return matcher.group(1);
        }

        return "";
    }

}
