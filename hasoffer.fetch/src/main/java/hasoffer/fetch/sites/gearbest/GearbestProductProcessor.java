package hasoffer.fetch.sites.gearbest;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.exception.ImagesNotFoundException;
import hasoffer.fetch.exception.PriceNotFoundException;
import hasoffer.fetch.exception.ProductTitleNotFoundException;
import hasoffer.fetch.model.Product;
import hasoffer.fetch.model.ProductImage;
import hasoffer.fetch.model.Sku;
import hasoffer.fetch.model.SkuAttributeValue;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

import static hasoffer.base.utils.http.XPathUtils.*;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/26
 */
public class GearbestProductProcessor implements IProductProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(GearbestProductProcessor.class);

	private static final Pattern ORIGINAL_PRICE_PATTERN = Pattern.compile(
			"US\\$([\\d\\.]+)"
	);
	private static final Pattern REVIEW_RATING_PATTERN = Pattern.compile(
			".*star([\\d]).*"
	);

	private static final String XPATH_PRODUCT_NAME = "//div[@class='goods_info_inner']/h1";
	private static final String XPATH_PRODUCT_SUBTITLE = "//div[@class='goods_info_inner']/p[@class='shortTitle']";

	private static final String XPATH_IMAGES = "//ul[@class='js_scrollableDiv']/li/img";

	private static final String XPATH_PRICE_CURRENT = "//span[@id='unit_price']";
	private static final String XPATH_PRICE_ORIGINAL = "//span[@id='market_price']";

	private static final String XPATH_DESCRIPTION = "//div[@class='js_showtable description clearfix']";

	private static final String XPATH_SKUS = "//p[@class='g_cateList clearfix']";

	private static final String XPATH_BRAND = "//a[@class='brand-name']";
	private static final String XPATH_BASIC_INFO = "//div[@class='product_pz_info product_pz_style1']/table/tbody/tr/td";
	private static final String XPATH_CATEGORY = "//p[@class='fl']/span/a/span";
	private static final String XPATH_SOLD_COUNT = "//a[@class='colorBlue  pr5']/strong";

	public GearbestProductProcessor() {
	}

	@Override
	public String getUrlByProductId(String productId) {
		return GearbestHelper.getUrlByProductId(productId);
	}

	@Override
	public String getProductIdByUrl(String pageUrl) {
		return GearbestHelper.getProductIdByUrl(pageUrl);
	}

	@Override
	public Product parseProduct(String sourceUrl) throws HttpFetchException, XPatherException, ContentParseException {
		TagNode root = HtmlUtils.getUrlRootTagNode(sourceUrl);

		String sourceProductId = GearbestHelper.getProductIdByUrl(sourceUrl);

		// product name
		String productName = getSubNodeStringByXPath(root, XPATH_PRODUCT_NAME,
		                                             new ProductTitleNotFoundException(sourceUrl));
		String subTitle = getSubNodeStringByXPath(root, XPATH_PRODUCT_SUBTITLE, null);

		// category
		List<String> categoryNames = getSubNodesStringsByXPath(root, XPATH_CATEGORY, null);

		// images
		List<ProductImage> images = new ArrayList<ProductImage>();
		List<TagNode> imageNodes = getSubNodesByXPath(root, XPATH_IMAGES,
		                                              new ImagesNotFoundException(sourceUrl));
		for (TagNode imageNode : imageNodes) {
			String imageUrl = imageNode.getAttributeByName("data-normal-img");
			images.add(new ProductImage(imageUrl, imageUrl, imageUrl));
		}

		// prices
		float currentPrice, originalPrice;
		String priceString = getSubNodeStringByXPath(root, XPATH_PRICE_CURRENT, new PriceNotFoundException(sourceUrl));
		// new PriceNotFoundException(this.sourceProductId, this.sourceUrl));
		String priceOriginalString = getSubNodeStringByXPath(root, XPATH_PRICE_ORIGINAL, null);

		if (StringUtils.isEmpty(priceOriginalString)) {
			priceOriginalString = priceString;
		}

		currentPrice = Float.parseFloat(priceString.replace("$", ""));
		originalPrice = Float.parseFloat(priceOriginalString.replace("$", ""));

		String description;
		List<TagNode> descNodes = getSubNodesByXPath(root, XPATH_DESCRIPTION, null);
		StringBuffer sb = new StringBuffer();
		for (TagNode node : descNodes) {
			sb.append(HtmlUtils.getInnerHTML(node));
		}
		description = StringUtils.filterAndTrim(sb.toString(), Arrays.asList("\r", "\n", "\t"));

		// brand
		String brandName = getSubNodeStringByXPath(root, XPATH_BRAND, null);
		String model = "";
		TagNode ns = getSubNodeByXPath(root, "//meta[@name='og:sku']", null);
		if (ns != null) {
			String og_sku = ns.getAttributeByName("content");
			// model
			if (!StringUtils.isEmpty(og_sku) && !StringUtils.isEmpty(brandName)) {
				model = StringUtils.filterAndTrim(og_sku, Arrays.asList(brandName));
			}
		}

		LinkedHashMap<String, LinkedHashMap<String, String>> attrMap = getBasicAttributes(root);
		if (StringUtils.isEmpty(brandName)) {
			if (attrMap.containsKey("Basic Information")) {
				LinkedHashMap<String, String> am = attrMap.get("Basic Information");
				if (am.containsKey("Brand")) {
					brandName = am.get("Brand");
				}
			}
		}
		if (StringUtils.isEmpty(model)) {
			if (attrMap.containsKey("Basic Information")) {
				LinkedHashMap<String, String> am = attrMap.get("Basic Information");
				if (am.containsKey("model")) {
					brandName = am.get("model");
				}
			}
		}

		List<Sku> skus = new ArrayList<Sku>();
		List<String> saleAttributeNames = new ArrayList<String>();
		TagNode skuNode = getSubNodeByXPath(root, XPATH_SKUS, null);
		getSkus(skuNode, skus, saleAttributeNames, currentPrice, originalPrice);

		String reviewStr = getSubNodeStringByXPath(root, XPATH_SOLD_COUNT, null);
		int reviews = StringUtils.getInt(reviewStr);

		Product product = new Product(Website.GEARBEST, productName, sourceProductId, sourceUrl,
		                              brandName, model, currentPrice, originalPrice,
		                              description, attrMap, skus, saleAttributeNames, images, subTitle, reviews);
		return product;
	}

	private void getSkus(TagNode skuNode, List<Sku> skus, List<String> saleAttributeNames,
	                     float currentPrice, float originalPrice)
			throws ContentParseException {
		if (skuNode == null) {
			return;
		}

		// attr name
		String attrName = getSubNodeStringByXPath(skuNode, "//label", null);
		attrName = attrName.replaceAll(":", "").trim().toUpperCase();
		saleAttributeNames.add(attrName);

		// attr values
		List<TagNode> attrValNodes = getSubNodesByXPath(skuNode, "//a", null);

		int rank = 0;
		for (TagNode attrValNode : attrValNodes) {
			String skuUrl = attrValNode.getAttributeByName("href");
			String attrVal = attrValNode.getText().toString();
			if (!skuUrl.startsWith("http")) {
				skuUrl = GearbestHelper.SITE_URL + skuUrl;
			}
			attrVal = attrVal.replaceAll("\\r", "").replaceAll("\\n", "").trim();
			rank++;

			String pStyle = attrValNode.getAttributeByName("class");
			boolean checked = "on".equalsIgnoreCase(pStyle);

			Map<String, SkuAttributeValue> attrMap = new HashMap<String, SkuAttributeValue>();
			attrMap.put(attrName, new SkuAttributeValue(attrVal));

			skus.add(new Sku(GearbestHelper.getProductIdByUrl(skuUrl), skuUrl, checked,
			                 currentPrice, originalPrice,
			                 attrMap));
		}
	}

	private LinkedHashMap<String, LinkedHashMap<String, String>> getBasicAttributes(TagNode root) throws ContentParseException {
		LinkedHashMap<String, LinkedHashMap<String, String>> attrMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();

		List<TagNode> basicInfoNodes = getSubNodesByXPath(root, XPATH_BASIC_INFO, null);
		if (ArrayUtils.isNullOrEmpty(basicInfoNodes)) {
			return attrMap;
		}

		for (TagNode basicInfoNode : basicInfoNodes) {
			LinkedHashMap<String, String> basicAttrMap = new LinkedHashMap<String, String>();

			String key0 = getSubNodeStringByXPath(basicInfoNode, "//div[@class='product_pz_img']/p", null);

			String val0 = getSubNodeStringByXPath(basicInfoNode, "p", null);

			String[] binfs = val0.split("\r");
			for (String binf : binfs) {
				binf = StringUtils.filterAndTrim(binf, Arrays.asList("\r", "\n"));
				if (!StringUtils.isEmpty(binf) && binf.contains(":")) {
					String[] kv = binf.split(":");
					String key = kv[0];
					String value = kv[1];
					basicAttrMap.put(key.trim(), value.trim());
				}
			}

			attrMap.put(key0, basicAttrMap);
		}

		return attrMap;
	}
}
