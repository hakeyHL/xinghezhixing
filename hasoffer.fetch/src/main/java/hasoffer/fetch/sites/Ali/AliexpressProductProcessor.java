package hasoffer.fetch.sites.Ali;

import com.alibaba.fastjson.JSON;
import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.exception.ProductTitleNotFoundException;
import hasoffer.fetch.model.Product;
import hasoffer.fetch.model.ProductImage;
import hasoffer.fetch.model.Sku;
import hasoffer.fetch.model.SkuAttributeValue;
import hasoffer.fetch.sites.Ali.model.AliSku;
import org.apache.http.conn.ConnectTimeoutException;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hasoffer.base.utils.http.XPathUtils.*;
/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/20
 */
public class AliexpressProductProcessor implements IProductProcessor {

	private static final Pattern PATTERN_SKU_ATTR_ID = Pattern.compile("sku-\\d-(\\d+)");

	private static final String XPATH_PRODUCT_NAME = "//h1[@class='product-name']";

	private static final String XPATH_PRICE_CURRENT = "//span[@id='sku-ptm']";

	private static final String XPATH_ORDER_COUNT = "//span[@class='orders-count']/b";

	private static final String XPATH_DESCRIPTION = "//div[@id='custom-description']/div[@class='ui-box-body']";

	private static final String XPATH_SMALL_IMAGES = "//ul[@class='image-nav util-clearfix']/li/span/img";

	private static final String XPATH_PRODUCT_ATTR = "//div[@class='ui-box-body']/dl";

	private static final String XPATH_ATTRS = "//dl[@class='ui-attr-list util-clearfix']";

	private static final String XPATH_SKU_ATTRS = "//div[@id='product-info-sku']/dl";
	private static final String XPATH_SOLD_COUNT = "//span[@class='orders-count']/b";

	@Override
	public String getUrlByProductId(String productId) {
		return AliexpressHelper.getUrlByProductId(productId);
	}

	@Override
	public String getProductIdByUrl(String pageUrl) {
		return AliexpressHelper.getProductIdByUrl(pageUrl);
	}

	@Override
	public Product parseProduct(String url) throws ConnectTimeoutException, HttpFetchException, XPatherException, ContentParseException {

		String sourceProductId = getProductIdByUrl(url);

		String html = HtmlUtils.getUrlHtml(url);
		TagNode root = HtmlUtils.getTagNode(html);

		List<AliSku> aliSkus = getSkuProducts(html);

		// product name
		String productName = getSubNodeStringByXPath(root, XPATH_PRODUCT_NAME,
		                                             new ProductTitleNotFoundException(url));

		// prices
		float currentPrice = 0.0f, originalPrice = 0.0f;

		// order count
		String orderCountString = getSubNodeStringByXPath(root, XPATH_ORDER_COUNT, null);
		int sourceOrderCount = Integer.parseInt(orderCountString);

		// description
		String description = "";
		TagNode descNode = getSubNodeByXPath(root, XPATH_DESCRIPTION, null);
		if (descNode != null) {
		}

		// images
		List<ProductImage> images = new ArrayList<ProductImage>();
		List<TagNode> imageNodes = getSubNodesByXPath(root, XPATH_SMALL_IMAGES, null);
		for (TagNode imageNode : imageNodes) {
			String smallImageUrl = StringUtils.notNullTrim(imageNode.getAttributeByName("src"));
			if (smallImageUrl.equals("about:blank")) {
				smallImageUrl = StringUtils.notNullTrim(imageNode.getAttributeByName("image-src"));
			}
			String originalImageUrl = smallImageUrl.replace("_50x50.jpg", "");
			images.add(new ProductImage(smallImageUrl, originalImageUrl, originalImageUrl));
		}

		// attributes
		LinkedHashMap<String, LinkedHashMap<String, String>> attributes = getBasicAttributes(root);

		String brandName = "", model = "";
		// skus
		// 当没有SKU时，初始化一个sku
		List<String> saleAttributeNames = new ArrayList<String>();
		List<Sku> skus = new ArrayList<Sku>();
		getSkus(root, aliSkus, url, saleAttributeNames, skus);

		String subTitle = "";//getSubNodeStringByXPath(root, XPATH_SUBTITLE, null);

		Product product = new Product(Website.ALIEXPRESS, productName, sourceProductId, url,
		                              brandName, model, currentPrice, originalPrice,
		                              description, attributes, skus, saleAttributeNames, images, subTitle, sourceOrderCount);

		return product;
	}

	private void getSkus(TagNode root, List<AliSku> aliSkus, String sourceUrl, List<String> saleAttributeNames, List<Sku> skus)
			throws ContentParseException {

		Map<String, Map<String, String>> attrValMap = getSkuAttrs(root);

		for (AliSku aliSku : aliSkus) {

			float currentPrice = Float.parseFloat(aliSku.getSkuVal().getSkuPrice());
			float originalPrice = currentPrice;
			if (!StringUtils.isEmpty(aliSku.getSkuVal().getActSkuCalPrice())) {
				currentPrice = Float.parseFloat(aliSku.getSkuVal().getActSkuCalPrice());
			}

			String[] propIds = StringUtils.splitAndTrim(aliSku.getSkuPropIds(), ",");

			Map<String, SkuAttributeValue> saleAttrs = new HashMap<String, SkuAttributeValue>();

			for (Map.Entry<String, Map<String, String>> kv : attrValMap.entrySet()) {
				String name = kv.getKey();
				if (!saleAttributeNames.contains(name)) {
					saleAttributeNames.add(name);
				}

				Map<String, String> attrs = kv.getValue();
				for (String propId : propIds) {
					if (attrs.containsKey(propId)) {
						String key = attrs.get(propId);
						saleAttrs.put(name, new SkuAttributeValue(key));
						break;
					}
				}
			}

			skus.add(new Sku(aliSku.getSkuAttr(), sourceUrl, false, currentPrice, originalPrice, saleAttrs));
		}

	}

	private Map<String, Map<String, String>> getSkuAttrs(TagNode root) throws ContentParseException {
		Map<String, Map<String, String>> attrValMap = new HashMap<String, Map<String, String>>();

		List<TagNode> skuAttrNodes = getSubNodesByXPath(root, XPATH_SKU_ATTRS, null);

		if (ArrayUtils.isNullOrEmpty(skuAttrNodes)) {
			return attrValMap;
		}

		for (TagNode skuAttrNode : skuAttrNodes) {
			String skuAttrName = getSubNodeStringByXPath(skuAttrNode, "dt", null);
			skuAttrName = StringUtils.filterAndTrim(skuAttrName, Arrays.asList(":"));

			TagNode skuAttrVal = getSubNodeByXPath(skuAttrNode, "dd/ul", null);

			List<TagNode> skuAttrValNodes = getSubNodesByXPath(skuAttrVal, "li/a", null);

			Map<String, String> valMap = new HashMap<String, String>();
			for (TagNode skuAttrValNode : skuAttrValNodes) {
				String skuAttrId = skuAttrValNode.getAttributeByName("id");
				Matcher m = PATTERN_SKU_ATTR_ID.matcher(skuAttrId);
				if (m.matches()) {
					skuAttrId = m.group(1);
				}

				String skuAttrValText = getSubNodeStringByXPath(skuAttrValNode, "span", null);
				valMap.put(skuAttrId, skuAttrValText);
			}

			attrValMap.put(skuAttrName, valMap);
		}

		return attrValMap;
	}

	private List<AliSku> getSkuProducts(String html) {
		int skuProsStart = html.indexOf("skuProducts=[");
		String st = html.substring(skuProsStart);
		int skuProsEnd = st.indexOf("]") + 1;

		String skuProductsJson = st.substring(0, skuProsEnd).replace("skuProducts=", "");

		List<AliSku> aliSkus = JSON.parseArray(skuProductsJson, AliSku.class);
		return aliSkus;
	}

	private LinkedHashMap<String, LinkedHashMap<String, String>> getBasicAttributes(TagNode root) throws ContentParseException {
		LinkedHashMap<String, LinkedHashMap<String, String>> am = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		LinkedHashMap<String, String> attrMap = new LinkedHashMap<String, String>();

		List<TagNode> attrNodes = getSubNodesByXPath(root, XPATH_ATTRS, null);

		if (!ArrayUtils.isNullOrEmpty(attrNodes)) {
			for (TagNode attrNode : attrNodes) {
				String k = getSubNodeStringByXPath(attrNode, "dt", null);
				String v = getSubNodeStringByXPath(attrNode, "dd", null);

				k = StringUtils.filterAndTrim(k, Arrays.asList(":"));

				if (StringUtils.isEmpty(k) || StringUtils.isEmpty(v)) {
					continue;
				}

				attrMap.put(k, v.trim());
			}
		}

		am.put("default", attrMap);
		return am;
	}
}
