package hasoffer.fetch.sites.geekbuying;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/2
 */
public class GeekbuyingHelper {

	public static final String SOURCE_SITE = "geekbuying";

	public static final String SITE_URL = "http://www.geekbuying.com";

	private static final Pattern PATTERN_PRODUCT_URL_ID = Pattern.compile(
			".+-(\\d+)\\.html"
	);

	public static String getUrlByProductId(String productId) {
		return "http://www.geekbuying.com/item/" + productId; //343327
	}

	public static String getProductIdByUrl(String pageUrl) {
		Matcher matcher = PATTERN_PRODUCT_URL_ID.matcher(pageUrl);
		if (matcher.matches()) {
			return matcher.group(1);
		}

		return null;
	}

}
