package hasoffer.fetch.sites.banggood;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/30
 */
public class BanggoodHelper {

	public static final String SOURCE_SITE = "banggood";

	private static final String SITE_URL = "http://www.banggood.com";

	private static final Pattern PRODUCT_URL_ID_PATTERN = Pattern.compile(
			".+-(\\d+)\\.html"
	);

	private static final Map<String, String> currencyMap = new HashMap<String, String>();

	static {
		currencyMap.put("US$", "USD");
		currencyMap.put("€", "EUR");
		currencyMap.put("£", "GBP");
		currencyMap.put("AU$", "AUD");
		currencyMap.put("CA$", "CAD");
		currencyMap.put("руб.", "RUB");
		currencyMap.put("R$", "BRL");
		currencyMap.put("SFr", "CHF");
		currencyMap.put("Dkr", "DKK");
		currencyMap.put("₱", "PHP");
		currencyMap.put("S$", "SGD");
		currencyMap.put("Kč", "CZK");
		currencyMap.put("Ft", "HUF");
		currencyMap.put("Mex$", "MXN");
		currencyMap.put("Kr", "NOK");
		currencyMap.put("NZD$", "NZD");
		currencyMap.put("zł", "PLN");
		currencyMap.put("฿", "THB");
		currencyMap.put("HK$", "HKD");
		currencyMap.put("₪", "ILS");
		currencyMap.put("Kr", "SEK");
		currencyMap.put("₩", "KRW");
		currencyMap.put("$", "CLP");
		currencyMap.put("TRY", "TRY");
	}

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

	public static String getCurrencyCodeBySymbol(String currencySymbol) {
		return currencyMap.get(currencySymbol);
	}
}
