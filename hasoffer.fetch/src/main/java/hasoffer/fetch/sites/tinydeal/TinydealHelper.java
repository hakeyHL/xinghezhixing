package hasoffer.fetch.sites.tinydeal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/30
 */
public class TinydealHelper {

	public static final String SOURCE_SITE = "tinydeal";

	private static final String SITE_URL = "http://www.tinydeal.com";

	private static final Pattern PRODUCT_URL_ID_PATTERN = Pattern.compile(
			".+-(\\d+)\\.html"
	);

	public static String getUrlByProductId(String productId) {
		return String.format("", productId);
	}

	public static String getProductIdByUrl(String pageUrl) {
		Matcher matcher = PRODUCT_URL_ID_PATTERN.matcher(pageUrl);
		if (matcher.matches()) {
			return matcher.group(1);
		}

		return null;
	}

}
