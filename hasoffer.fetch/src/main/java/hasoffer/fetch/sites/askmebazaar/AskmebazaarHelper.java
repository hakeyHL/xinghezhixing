package hasoffer.fetch.sites.askmebazaar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date : 2016/3/2
 */
public class AskmebazaarHelper {

	private static final Pattern PRODUCT_URL_ID_PATTERN = Pattern.compile(".+-p([0-9]+).+");


	public static String getProductIdByUrl(String pageUrl) {
		Matcher matcher = PRODUCT_URL_ID_PATTERN.matcher(pageUrl);
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return null;
	}

}
