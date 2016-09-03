package hasoffer.fetch.sites.tinydeal;

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
 * Date : 2015/11/02
 */
public class TinydealProductProcessor implements IProductProcessor {
	public static final HtmlCleaner CLEANER = new HtmlCleaner();
	private static final Logger logger = LoggerFactory.getLogger(TinydealProductProcessor.class);

	private static final Pattern PATTERN_PRICE = Pattern.compile("^\\$(\\d+.\\d+)");

	private static final String XPATH_PRODUCT_NAME = "//h1[@id='productName']";
	private static final String XPATH_CATEGORY = "//div[@id='navBreadCrumb']/a";

	private static final String XPATH_PRICE = "//h3[@id='productPrices']/span[@class='site-ptm']";

	private static final String XPATH_DESCRIPTION = "//div[@id='productDescription']";

	private static final String XPATH_SMALL_IMAGES = "//ul[@class='product_list_li_ul']/li[@class='border_r']/img";

	/*private static final String XPATH_BRAND = "//ul[@id='productDetailsList']/li[1]/strong[@class='pd-model']";
	private static final String XPATH_MODEL = "//ul[@id='productDetailsList']/li[2]/a";*/

	private static final String XPATH_SOLD_COUNT = "//*[@id='review_num']//span[@itemprop='count']";

	private static final String XPATH_ATTRIBUTE_0 = "//ul[@id='productDetailsList']/li";
	private static final String XPATH_ATTRIBUTE = "//ul[@class='specifications_ul']/table[@class='ui-table']/tbody/tr";

	private static final String XPATH_SKUID = "//div[@class='good_main']/ul[@class='info']/li[@class='sku']/b/span";
	private static final String XPATH_SKU_ATTRIBUTES = "//div[@id='productAttributes']/dl[@class='stock clearfix  houseinfo']";

	public TinydealProductProcessor() {
	}

	@Override
	public String getUrlByProductId(String productId) {
		return TinydealHelper.getUrlByProductId(productId);
	}

	@Override
	public String getProductIdByUrl(String pageUrl) {
		return TinydealHelper.getProductIdByUrl(pageUrl);
	}

	@Override
	public Product parseProduct(String sourceUrl) throws HttpFetchException, XPatherException, ContentParseException {

		TagNode root = HtmlUtils.getUrlRootTagNode(sourceUrl);

		String sourceProductId = getProductIdByUrl(sourceUrl);

		// product name
		String productName = getSubNodeStringByXPath(root, XPATH_PRODUCT_NAME,
		                                             new ProductTitleNotFoundException(sourceUrl));
		String subTitle = "";

		String reviewStr = getSubNodeStringByXPath(root, XPATH_SOLD_COUNT, null);
		int reviews = StringUtils.getInt(reviewStr);//StringUtils.getInt(reviewStr);

		// brand - model
		String brandName = "", model = "";
		Map<String, String> attrMap = getDetails(root);
		if (StringUtils.isEmpty(model)) {
			if (attrMap.containsKey("Model")) {
				model = attrMap.get("Model");
			}
		}
		if (StringUtils.isEmpty(brandName)) {
			if (attrMap.containsKey("Brand")) {
				brandName = attrMap.get("Brand");
			}
		}

		// prices
		float[] prices = getPrice(root);
		float currentPrice = prices[0];
		float originalPrice = prices[1];

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

		LinkedHashMap<String, LinkedHashMap<String, String>> attributes = getBasicAttribute(root);
		//specifications
		if (StringUtils.isEmpty(brandName) && attributes.get("specifications") != null) {
			brandName = attributes.get("specifications").get("Brand");
		}

		List<String> saleAttributeNames = new ArrayList<String>();
		List<Sku> skus = new ArrayList<Sku>();
		getSkus(root, sourceProductId, skus, saleAttributeNames, currentPrice, originalPrice, sourceUrl);

		Product product = new Product(Website.TINYDEAL, productName, sourceProductId, sourceUrl,
		                              brandName, model, currentPrice, originalPrice,
		                              description, attributes, skus, saleAttributeNames, images, subTitle, reviews);

		return product;
	}

	private LinkedHashMap<String, String> getDetails(TagNode root) throws ContentParseException {
		List<TagNode> attrNodes = getSubNodesByXPath(root, XPATH_ATTRIBUTE_0, null);

		LinkedHashMap<String, String> attrs = new LinkedHashMap<String, String>();

		if (!ArrayUtils.isNullOrEmpty(attrNodes)) {
			for (TagNode attrNode : attrNodes) {
				String key = getSubNodeStringByXPath(attrNode, "span", null);
				String val = getSubNodeStringByXPath(attrNode, "strong", null);

				if (val == null) {
					val = getSubNodeStringByXPath(attrNode, "a", null);
				}

				if (StringUtils.isEmpty(key) || StringUtils.isEmpty(val)) {
					continue;
				}

				key = StringUtils.filterAndTrim(key, Arrays.asList(":"));
				val = StringUtils.filterAndTrim(val, Arrays.asList(""));
				attrs.put(key, val);
			}
		}

		return attrs;
	}

	private LinkedHashMap<String, LinkedHashMap<String, String>> getBasicAttribute(TagNode root) throws ContentParseException {
		LinkedHashMap<String, LinkedHashMap<String, String>> attrMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		List<TagNode> attrNodes = getSubNodesByXPath(root, XPATH_ATTRIBUTE, null);

		LinkedHashMap<String, String> attrs = new LinkedHashMap<String, String>();

		for (TagNode attrNode : attrNodes) {
			List<TagNode> tds = getSubNodesByXPath(attrNode, "//td", null);
			if (ArrayUtils.isNullOrEmpty(tds) || tds.size() != 2) {
				continue;
			}
			String name = getSubNodeStringByXPath(tds.get(0), "//strong", null);
			String val = tds.get(1).getText().toString();

			name = StringUtils.filterAndTrim(name, Arrays.asList(":"));
			attrs.put(name, val);
		}

		attrMap.put("specifications", attrs);

		LinkedHashMap<String, String> attrs2 = new LinkedHashMap<String, String>();

		List<TagNode> packageContentNodes = getSubNodesByXPath(root, "//ul[@class='package_content_ul']/table/tbody/tr/td", null);
		for (TagNode pcn : packageContentNodes) {
			attrs2.put(pcn.getText().toString(), "");
		}

		attrMap.put("Package Content", attrs2);

		return attrMap;
	}

	private float[] getPrice(TagNode root) throws ContentParseException {
		float curPrice = 0.0f;
		TagNode priceNode = getSubNodeByXPath(root, XPATH_PRICE, new PriceNotFoundException(""));

		String curPriceStr = getSubNodeStringByXPath(priceNode, "//span[@class='productSpecialPrice fl sprice']", null);
		String oriPriceStr = getSubNodeStringByXPath(priceNode, "//div[@class='nor-ptm']/span[@class='normalprice cen']/strong", null);

		curPriceStr = StringUtils.filterAndTrim(curPriceStr, Arrays.asList(""));

		Matcher pm = PATTERN_PRICE.matcher(curPriceStr);
		if (pm.matches()) {
			curPrice = Float.parseFloat(pm.group(1));
		}

		if (StringUtils.isEmpty(oriPriceStr)) {
			return new float[]{curPrice, curPrice};
		}

		float oriPrice = 0.0f;
		oriPriceStr = StringUtils.filterAndTrim(oriPriceStr, Arrays.asList(""));
		pm = PATTERN_PRICE.matcher(oriPriceStr);
		if (pm.matches()) {
			oriPrice = Float.parseFloat(pm.group(1));
		} else {
			oriPrice = curPrice;
		}

		return new float[]{curPrice, oriPrice};
	}

	private void getSkus(TagNode root, String productId, List<Sku> skus, List<String> saleAttributeNames,
	                     float currentPrice, float originalPrice, String sourceUrl) throws ContentParseException {

		String skuId = getSubNodeStringByXPath(root, XPATH_SKUID, null);

		TagNode skuAttrNode = getSubNodeByXPath(root, XPATH_SKU_ATTRIBUTES, null);

		if (skuAttrNode == null) {
			// 商品本身就是sku
			return;
		}

		String skuAttrName = getSubNodeStringByXPath(skuAttrNode, "//dt", null);
		skuAttrName = StringUtils.filterAndTrim(skuAttrName, Arrays.asList(":"));
		saleAttributeNames.add(skuAttrName);

		List<SkuAttributeValue> savs = new ArrayList<SkuAttributeValue>();
		List<TagNode> skuAttrNodes = getSubNodesByXPath(skuAttrNode, "//dd/div", null);

		// skuNodes
		int rank = 0;
		for (TagNode node : skuAttrNodes) {
			boolean selected = "selected".equalsIgnoreCase(node.getAttributeByName("class"));

			TagNode attrNode = getSubNodeByXPath(node, "//a", null);

			String id = attrNode.getAttributeByName("id");
			String value = attrNode.getText().toString();

			Map attrMap = new HashMap();
			SkuAttributeValue sav = new SkuAttributeValue(id, skuAttrName, value, "");
			attrMap.put(skuAttrName, sav);

			Sku sku = new Sku(id, sourceUrl, selected, currentPrice, originalPrice, attrMap);
			skus.add(sku);
		}
	}
}
