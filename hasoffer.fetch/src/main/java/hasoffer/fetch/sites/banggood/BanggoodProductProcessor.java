package hasoffer.fetch.sites.banggood;

import com.alibaba.fastjson.JSON;
import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.ExchangeUtils;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.exception.ImagesNotFoundException;
import hasoffer.fetch.exception.PriceNotFoundException;
import hasoffer.fetch.exception.ProductTitleNotFoundException;
import hasoffer.fetch.helper.SkuHelper;
import hasoffer.fetch.model.Product;
import hasoffer.fetch.model.ProductImage;
import hasoffer.fetch.model.Sku;
import hasoffer.fetch.model.SkuAttributeValue;
import hasoffer.fetch.sites.banggood.model.StockMessageModel;
import org.apache.commons.lang3.math.NumberUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hasoffer.base.utils.http.XPathUtils.*;
/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/26
 */
public class BanggoodProductProcessor implements IProductProcessor {
	public static final HtmlCleaner CLEANER = new HtmlCleaner();
	private static final Logger logger = LoggerFactory.getLogger(BanggoodProductProcessor.class);

	private static final Pattern PATTERN_CURRENT_PRICE = Pattern.compile("^(\\d+.\\d+)");
	private static final Pattern PATTERN_ORIGINAL_PRICE = Pattern.compile("^US\\$(\\d+.\\d+)");
	private static final Pattern PATTERN_CARRIAGE_PRICE = Pattern.compile("^US\\$(\\d+.\\d+)");

	private static final String XPATH_PRODUCT_NAME = "//div[@class='good_main']/h1";
	private static final String XPATH_CATEGORY = "//ol[@class='inhere']/li/a";

	private static final String XPATH_CURRENCY = "//div[@class='hb hbactive']";
	private static final String XPATH_CURRENCY_SYMBOL = "//div[@class='hb hbactive']/b";
	private static final String XPATH_PRICE_CURRENT = "//div[@class='item_box ptm']/div[@class='item_con']/div[@class='now']";
	private static final String XPATH_PRICE_ORIGINAL = "//div[@class='item_box ptm']/div[@class='item_con']/div[@class='old']";

	private static final String XPATH_DESCRIPTION = "//div[@class='wrap_right']/div[@class='good_tabs_box']/div[@class='list']";
	private static final String XPATH_SMALL_IMAGES = "//div[@class='good_photo_min']/ul/li/a/img";

	private static final String XPATH_SOLD_COUNT = "//b[@id='sold_num']";

	private static final String XPATH_SKUID = "//div[@class='good_main']/ul[@class='info']/li[@class='sku']/b/span";
	private static final String XPATH_SKU_ATTRIBUTES = "//div[@class='item_box attr']";

	private static final String XPATH_BASIC_INFO = "//div[@class='list']//table/tbody/tr";
	//	private static final String XPATH_BASIC_INFO_1 = "//div[@id='specification']//table/tbody/tr";
	private static final String XPATH_BASIC_INFO_2 = "//div[@id='specification']/p[1]";
	/**
	 * http://www.banggood.com/index.php?com=product&t=stockMessage&sku=SKU253972&warehouse=CN&products_id=983462&value_ids[]=14928&value_ids[]=1492
	 * http://www.banggood.com/index.php?com=product&t=checkAttrStock&products_id=983462&warehouse=CN&option_ids[]=22&value_ids[]=14928&option_ids[]=359&value_ids[]=14930
	 * http://www.banggood.com/index.php?com=product&t=getShipments&warehouse=CN&products_id=983462&value_ids[]=14928&value_ids[]=14930
	 */
	private static final String URL_TEMPLATE_INIT_SHIPMENT =
			"http://www.banggood.com/index.php?com=product&t=initShipments&warehouse=CN&products_id={productId}&sku={skuId}&selPoa=&selAttrId=&utm_medium=&utm_source=&getCurWarehouse=1";
	private static final String URL_TEMPLATE_STOCKMESSAGE =
			"http://www.banggood.com/index.php?com=product&t=stockMessage&sku={skuId}&warehouse=CN&products_id={productId}";
	private static final String URL_TEMPLATE_PRODUCTID = "{productId}";
	private static final String URL_TEMPLATE_SKUID = "{skuId}";
	private static final String URL_TEMPLATE_VALUEIDS = "value_ids";

	private static final String[] VAL_TAG_STRS = new String[]{"//span", "//span/span", "//span/span/strong"};

	public BanggoodProductProcessor() {
	}

	@Override
	public String getUrlByProductId(String productId) {
		return BanggoodHelper.getUrlByProductId(productId);
	}

	@Override
	public String getProductIdByUrl(String pageUrl) {
		return BanggoodHelper.getProductIdByUrl(pageUrl);
	}

	@Override
	public Product parseProduct(String sourceUrl) throws HttpFetchException, XPatherException, ContentParseException {

		TagNode root = HtmlUtils.getUrlRootTagNode(sourceUrl);

		String sourceProductId = getProductIdByUrl(sourceUrl);

		// product name
		String productName = getSubNodeStringByXPath(root, XPATH_PRODUCT_NAME,
		                                             new ProductTitleNotFoundException(sourceUrl));

		// prices
		String currentPriceString = getCurrentPriceString(root);
		String originalPriceString = getOriginalPriceString(root, currentPriceString);

//		String currencySymbol = getSubNodeStringByXPath(root, XPATH_CURRENCY_SYMBOL, null);
		TagNode symbolNode = getSubNodeByXPath(root, XPATH_CURRENCY, null);
		String currencySymbol = getSubNodeStringByXPath(symbolNode, "b", null);
		/* 获取所有支持的货币
		List<TagNode> symbolNodes = getSubNodesByXPath(symbolNode, "ul/li", null);
		for (TagNode sn : symbolNodes) {
			String currencyCode = sn.getAttributeByName("sel");
			String symbol = getSubNodeStringByXPath(sn, "a/u", null);
			logger.debug(currencyCode + "\t" + symbol);
		}*/
		logger.debug(String.format("currencySymbol = %s, currentPrice = %s, originalPrice = %s.", currencySymbol, currentPriceString,
		                           originalPriceString));

		currentPriceString = StringUtils.filterAndTrim(currentPriceString, Arrays.asList(currencySymbol, ",", "\\s+"));
		originalPriceString = StringUtils.filterAndTrim(originalPriceString, Arrays.asList(currencySymbol, ",", "\\s+"));

		float currentPrice = Float.parseFloat(currentPriceString);
		float originalPrice = Float.parseFloat(originalPriceString);

		String currencyCode = BanggoodHelper.getCurrencyCodeBySymbol(currencySymbol);

		// rate exchange
		currentPrice = ExchangeUtils.getPrice(currentPrice, currencyCode);
		originalPrice = ExchangeUtils.getPrice(originalPrice, currencyCode);

		if (currentPrice <= 0) {
			throw new PriceNotFoundException(sourceUrl);
		}

		if (originalPrice <= 0) {
			originalPrice = currentPrice;
		}

		// category
		////ol[@class='inhere']/li/a
		List<String> categories = getSubNodesStringsByXPath(root, XPATH_CATEGORY, null);
		// 运费
//		double carriage = getCarriageStr(root, sourceProductId);

		// description
		List<TagNode> descriptionNodeList = getSubNodesByXPath(root, XPATH_DESCRIPTION, null);
		StringBuffer sb = new StringBuffer();
		for (TagNode node : descriptionNodeList) {
			sb.append(HtmlUtils.getInnerHTML(node));
		}
		String description =
				StringUtils.filterAndTrim(sb.toString(), Arrays.asList("\r", "\n", "\t", "<table.+</table>", "<script>.+</script>"));

		// images
		List<ProductImage> images = new ArrayList<ProductImage>();
		List<TagNode> imageNodes = getSubNodesByXPath(root,
		                                              XPATH_SMALL_IMAGES,
		                                              new ImagesNotFoundException(sourceUrl));
		for (TagNode imageNode : imageNodes) {
			String imageUrl = imageNode.getAttributeByName("src").replace("other_items", "large");
			images.add(new ProductImage(imageUrl, imageUrl, imageUrl));
		}

		String brandName = "", model = "";

		LinkedHashMap<String, LinkedHashMap<String, String>> attributes = getBasicAttributes(root);
		LinkedHashMap<String, String> attrMap = attributes.get("default");
		if (attrMap != null) {
			String attr_model = attrMap.get("Model");
			if (attr_model != null) {
				String[] attrs = attr_model.split("\\s");
				if (attrs.length >= 2) {
					brandName = attrs[0];
					model = attrs[1];
				}
			}

			// 验证
			TagNode keyNode = getSubNodeByXPath(root, "//meta[@name='keywords']", null);
			String[] keywords = keyNode.getAttributeByName("content").split(",");
			String[] bm = keywords[0].split("\\s");
			if (!(brandName.equalsIgnoreCase(bm[0]) && model.equalsIgnoreCase(bm[1]))) {
				if (StringUtils.isEmpty(brandName)) {
					brandName = bm[0];
				}
				if (StringUtils.isEmpty(model)) {
					model = bm[1];
				}
			}
		}

		List<String> saleAttributeNames = new ArrayList<String>();
		List<Sku> skus = new ArrayList<Sku>();

		getSkus(root, sourceProductId, skus, saleAttributeNames, currentPrice, originalPrice, sourceUrl);

		String subTitle = "";//getSubNodeStringByXPath(root, XPATH_SUBTITLE, null);

		String soldCountStr = getSubNodeStringByXPath(root, XPATH_SOLD_COUNT, null);
		int soldCount = StringUtils.getInt(soldCountStr);

		Product product = new Product(Website.BANGGOOD, productName, sourceProductId, sourceUrl,
		                              brandName, model, currentPrice, originalPrice,
		                              description, attributes, skus, saleAttributeNames, images, subTitle, soldCount);

		return product;
	}

	private LinkedHashMap<String, LinkedHashMap<String, String>> getBasicAttributes(TagNode root) throws ContentParseException {
		LinkedHashMap<String, LinkedHashMap<String, String>> attrMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();

		List<TagNode> basicInfoNodes = getSubNodesByXPath(root, XPATH_BASIC_INFO, null);

		LinkedHashMap<String, String> basicAttrMap = new LinkedHashMap<String, String>();
		attrMap.put("default", basicAttrMap);

		if (ArrayUtils.isNullOrEmpty(basicInfoNodes)) {
//			basicInfoNodes = getSubNodesByXPath(root, XPATH_BASIC_INFO_1, null);
//			if (ArrayUtils.isNullOrEmpty(basicInfoNodes)) {
			String basicInfoStr = getSubNodeStringByXPath(root, XPATH_BASIC_INFO_2, null);

			if (StringUtils.isEmpty(basicInfoStr)) {
				return attrMap;
			}

			String[] basicInfs = basicInfoStr.split("\n");

			for (String binf : basicInfs) {
				int pos = binf.indexOf(":");
				if (pos > 0 && pos < binf.length()) {
					String[] kv = binf.split(":");
					if (kv.length == 2) {
						String key = kv[0];
						String val = kv[1];

						key = StringUtils.filterAndTrim(key, Arrays.asList(""));
						val = StringUtils.filterAndTrim(val, Arrays.asList(""));
						basicAttrMap.put(key, val);
					}
				}
			}

			return attrMap;
//			}
		}

		for (TagNode basicInfoNode : basicInfoNodes) {
			List<TagNode> attrNodes = getSubNodesByXPath(basicInfoNode, "td", null);

			if (ArrayUtils.isNullOrEmpty(attrNodes) || attrNodes.size() != 2) {
				continue;
			}

			TagNode keyNode = attrNodes.get(0);
			String key = getStringVal(keyNode);

			TagNode valNode = attrNodes.get(1);
			String val = getStringVal(valNode);

			if (StringUtils.isEmpty(key) || StringUtils.isEmpty(val)) {
				continue;
			}

			basicAttrMap.put(key, val);
		}

		return attrMap;
	}

	private String getStringVal(TagNode valNode) throws ContentParseException {
		int size = VAL_TAG_STRS.length;
		int i = 0;

		String val = valNode.getText().toString();

		while (i < size && StringUtils.isEmpty(val)) {
			val = getSubNodeStringByXPath(valNode, VAL_TAG_STRS[i++], null);
		}

		return StringUtils.filterAndTrim(val, Arrays.asList("&nbsp;", "\n", "\\s\\s"));
	}

	private void getSkus(TagNode root, String productId, List<Sku> skus, List<String> saleAttributeNames,
	                     float currentPrice, float originalPrice, String sourceUrl) throws ContentParseException {

		String skuId = getSubNodeStringByXPath(root, XPATH_SKUID, null);

		List<TagNode> skuAttrNodes = getSubNodesByXPath(root, XPATH_SKU_ATTRIBUTES, null);

		if (ArrayUtils.isNullOrEmpty(skuAttrNodes)) {
			// 商品本身就是sku
			return;
		}

		// sold count
		int soldCount = 0;
		String soldCountStr = getSubNodeStringByXPath(root, XPATH_SOLD_COUNT, null);
		if (StringUtils.isEmpty(soldCountStr) && NumberUtils.isDigits(soldCountStr)) {
			soldCount = Integer.parseInt(soldCountStr);
		}

		Map<String, List<SkuAttributeValue>> skuAttributeDefMap = new HashMap<String, List<SkuAttributeValue>>();

		// skuNodes
		int rank = 0;
		for (TagNode skuAttrNode : skuAttrNodes) {
			String skuAttrName = getSubNodeStringByXPath(skuAttrNode, "//div[@class='item_name']", null);
			skuAttrName = StringUtils.filterAndTrim(skuAttrName, Arrays.asList(":"));
			saleAttributeNames.add(skuAttrName);

			List<TagNode> valueNodes = getSubNodesByXPath(skuAttrNode, "//a", null);

			skuAttributeDefMap.put(skuAttrName, new ArrayList<SkuAttributeValue>());

			for (TagNode valueNode : valueNodes) {
				String id = valueNode.getAttributeByName("value_id");
				String value = valueNode.getAttributeByName("ori_name");

				TagNode imageNode = getSubNodeByXPath(valueNode, "//img", null);

				String smallImageUrl = "";
				String largeImageUrl = "";
				if (imageNode != null) {
					smallImageUrl = imageNode.getAttributeByName("src");
					largeImageUrl = imageNode.getAttributeByName("largeimage");
				}

				skuAttributeDefMap.get(skuAttrName).add(new SkuAttributeValue(id, skuAttrName, value, largeImageUrl));
			}
		}
		List<Map<String, SkuAttributeValue>> skuAttributes = SkuHelper.transformSkus(skuAttributeDefMap, saleAttributeNames);

		generateSkus(skus, skuAttributes, productId, skuId, sourceUrl, currentPrice, originalPrice);
	}

	private List<Sku> generateSkus(List<Sku> skus, List<Map<String, SkuAttributeValue>> skuAttributes,
	                               String productId, String skuId, String sourceUrl,
	                               float currentPrice, float originalPrice) {
		for (Map<String, SkuAttributeValue> skuAttributeMap : skuAttributes) {
			List<String> valueIds = new ArrayList<String>();
			for (Map.Entry<String, SkuAttributeValue> kv : skuAttributeMap.entrySet()) {
				valueIds.add(kv.getValue().getId());
			}
			Sku sku = new Sku(skuId, sourceUrl, false, currentPrice, originalPrice, skuAttributeMap);
			getPrices(sku, productId, skuId, valueIds);
			skus.add(sku);
		}
		return skus;
	}

	//value_ids[]= 通过请求json获取sku价格
	private void getPrices(Sku sku, String productId, String skuId, List<String> valueIds) {
		String requestUrl = URL_TEMPLATE_STOCKMESSAGE.replace(URL_TEMPLATE_PRODUCTID, productId)
		                                             .replace(URL_TEMPLATE_SKUID, skuId);
		for (String valueId : valueIds) {
			requestUrl += "&" + URL_TEMPLATE_VALUEIDS + "=" + valueId;
		}
		HttpResponseModel responseModel = HtmlUtils.getResponse(requestUrl, 3);
		if (responseModel.isOk()) {
			String stockMessageModelJson = responseModel.getBodyString();
			StockMessageModel smm = JSON.parseObject(stockMessageModelJson, StockMessageModel.class);

			sku.setCurrentPrice(smm.getFinal_price());
			sku.setOriginalPrice(smm.getPrice());
		}
	}

	private double getCarriageStr(TagNode root, String sourceProductId) throws ContentParseException {

		/*String skuId = getSubNodeStringByXPath(root, XPATH_SKUID, null);

		String url = URL_TEMPLATE_INIT_SHIPMENT.replace(URL_TEMPLATE_PRODUCTID, sourceProductId).replace(URL_TEMPLATE_SKUID, skuId);

		HttpResponseModel responseModel = HttpFetcher.getResponse(url);

		if (responseModel.getStatusCode() == 200) {
			String shipJson = responseModel.getBodyString();
			ShipModel shipModel = JsonUtil.toObject(shipJson, ShipModel.class);
			TagNode tagNode = CLEANER.clean(shipModel.getShipmentBox());

			String shipStr = getSubNodeStringByXPath(tagNode, "//div[@class='item_con']/b", null);

			if (StringUtils.isNotBlankOrNull(shipStr)) {
				shipStr = shipStr.trim();
				if ("free shipping".equalsIgnoreCase(shipStr)) {
					return 0.00;
				} else {
					Matcher m = PATTERN_CARRIAGE_PRICE.matcher(shipStr);
					if (m.matches()) {
						return Double.parseDouble(m.group(1));
					}
				}
			}
		}

		return 1;*/
		return 1;
	}

	private String getOriginalPriceString(TagNode root, String priceString) throws ContentParseException {
		String originalPriceString = getSubNodeStringByXPath(root, XPATH_PRICE_ORIGINAL, null);
		if (StringUtils.isEmpty(originalPriceString)) {
			originalPriceString = priceString;
		}//
		Matcher priceMatcher = PATTERN_ORIGINAL_PRICE.matcher(originalPriceString);
		if (priceMatcher.matches()) {
			originalPriceString = priceMatcher.group(1);
		}
		return originalPriceString;
	}

	private String getCurrentPriceString(TagNode root) throws ContentParseException {
		String priceNodeString = getSubNodeStringByXPath(root, XPATH_PRICE_CURRENT, null);

		String priceString = priceNodeString;
		Matcher priceMatcher = PATTERN_CURRENT_PRICE.matcher(priceNodeString);
		if (priceMatcher.matches()) {
			priceString = priceMatcher.group(1);
		}
		return priceString;
	}
}
