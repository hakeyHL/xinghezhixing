package hasoffer.fetch.sites.homeshop18;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2016/4/6.
 */
public class Homeshop18Helper {

    private static final Pattern PRODUCT_URL_SOURCEID_PATTERN = Pattern.compile(".*/product:([0-9]+)/.*");

    public static String getProductIdByUrl(String url){

        Matcher matcher = PRODUCT_URL_SOURCEID_PATTERN.matcher(url);
        if(matcher.matches()){
            return matcher.group(1);
        }
        return null;
    }

}
