package hasoffer.fetch.sites.babyoye;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2016/3/2.
 */
public class BabyoyeHelper {

    private static final Pattern PRODUCT_URL_ID_PATTERN = Pattern.compile(".+/p_([A-Z0-9]+).+");

    public static String getProductIdByUrl(String pageUrl) {
        Matcher matcher = PRODUCT_URL_ID_PATTERN.matcher(pageUrl);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        return null;
    }

}
