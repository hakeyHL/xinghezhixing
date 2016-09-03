package hasoffer.fetch.sites.gearbest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/28
 */
public class GearbestHelper {
	public static final String SOURCE_SITE = "gearbest";

	public static final String SITE_URL = "http://www.gearbest.com";

	private static final Pattern PRODUCT_URL_ID_PATTERN = Pattern.compile(
			".+/pp_([\\d]+).html?"
	);

	public static String getUrlByProductId(String productId) {
		return "http://www.gearbest.com/p/pp_" + productId + ".html";
	}

	public static String getProductIdByUrl(String pageUrl) {
		Matcher matcher = PRODUCT_URL_ID_PATTERN.matcher(pageUrl);
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return null;
	}
}
