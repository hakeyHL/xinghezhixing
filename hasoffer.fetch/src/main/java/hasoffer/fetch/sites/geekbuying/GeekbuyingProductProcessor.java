package hasoffer.fetch.sites.geekbuying;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hasoffer.base.utils.http.XPathUtils.*;
/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/02
 */
public class GeekbuyingProductProcessor implements IProductProcessor {
	private static final Logger logger = LoggerFactory.getLogger(GeekbuyingProductProcessor.class);

	private static final Pattern PATTERN_PRICE_ORIGINAL = Pattern.compile("\\w+\\s+(\\d+\\.\\d+)");

	private static final String XPATH_PRODUCT_NAME = "//h1[@id='productName']";
	private static final String XPATH_PRICE_CURRENT = "//span[@id='saleprice']";
	private static final String XPATH_PRICE_ORIGINAL = "//span[@id='regprice']";

	private static final String XPATH_SMALL_IMAGES = "//*[@id='thumbnail']/li/a/img";

	private static final String XPATH_DESCRIPTION = "//div[@id='description']";
	private static final String XPATH_ATTR_TABLE = "//div[@id='DESCRIPTION_HTML']/table[@class='jbEidtTable']";

	private static final String XPATH_SKU_ATTRIBUTE = "//*[@id='colorProperty2014']";
	private static final String XPATH_SOLD_COUNT = "//div[@id='simpleReviewSummary']/span/a";

	public GeekbuyingProductProcessor() {
	}

	@Override
	public String getUrlByProductId(String productId) {
		return GeekbuyingHelper.getUrlByProductId(productId);
	}

	@Override
	public String getProductIdByUrl(String pageUrl) {
		return GeekbuyingHelper.getProductIdByUrl(pageUrl);
	}

	@Override
	public Product parseProduct(String sourceUrl)
			throws HttpFetchException, XPatherException, ContentParseException {

		String sourceProductId = getProductIdByUrl(sourceUrl);

		TagNode root = HtmlUtils.getUrlRootTagNode(sourceUrl);

		// product name
		String productName = getSubNodeStringByXPath(root, XPATH_PRODUCT_NAME,
		                                             new ProductTitleNotFoundException(sourceUrl));

		// images
		String smallImageFlag = "make_pic", bigImageFlag = "ggo_pic";
		List<ProductImage> images = new ArrayList<ProductImage>();
		List<TagNode> imageNodes = getSubNodesByXPath(root, XPATH_SMALL_IMAGES,
		                                              new ImagesNotFoundException(sourceUrl));
		for (TagNode imageNode : imageNodes) {
			String smallImageUrl = imageNode.getAttributeByName("src");
			String bigImageUrl = smallImageUrl.replaceAll(smallImageFlag, bigImageFlag);
			bigImageUrl = bigImageUrl.substring(0, bigImageUrl.lastIndexOf("1")) + ".jpg";
			images.add(new ProductImage(smallImageUrl, bigImageUrl, bigImageUrl));
		}

		// prices
		float currentPrice, originalPrice;
		String priceStr = getSubNodeStringByXPath(root, XPATH_PRICE_CURRENT, new PriceNotFoundException(sourceUrl));
		currentPrice = Float.parseFloat(priceStr);

		String priceOriginalString = getSubNodeStringByXPath(root, XPATH_PRICE_ORIGINAL,
		                                                     new PriceNotFoundException(sourceUrl));
		Matcher matcher = PATTERN_PRICE_ORIGINAL.matcher(priceOriginalString);
		if (matcher.matches()) {
			originalPrice = Float.parseFloat(matcher.group(1));
		} else {
			originalPrice = currentPrice;
		}

		// description
		List<TagNode> descriptionNodeList = getSubNodesByXPath(root, XPATH_DESCRIPTION, null);
		StringBuffer sb = new StringBuffer();
		for (TagNode node : descriptionNodeList) {
			sb.append(HtmlUtils.getInnerHTML(node));
		}
		String description = StringUtils.filterAndTrim(sb.toString(),
		                                               Arrays.asList("\r", "\n", "\t", "<table.+</table>", "<script>.+</script>"));

		// skus
		List<String> saleAttributeNames = new ArrayList<String>();
		List<Sku> skus = new ArrayList<Sku>();
		getSkus(root, skus, saleAttributeNames, currentPrice, originalPrice);

		// attributes
		LinkedHashMap<String, LinkedHashMap<String, String>> attributes = getBasicAttrs(root);

		String brandName = "", model = "";
		if (attributes.containsKey("General")) {
			Map<String, String> genMap = attributes.get("General");
			//"Brand" -> "Oneplus"
			brandName = genMap.get("Brand");

			//"Model" -> "One"
			model = genMap.get("Model");
		}

		String subTitle = "";//getSubNodeStringByXPath(root, XPATH_SUBTITLE, null);

		String reviewStr = getSubNodeStringByXPath(root, XPATH_SOLD_COUNT, null);
		Matcher m = Pattern.compile("\\d+").matcher(reviewStr);
		if (m.find()) {
			reviewStr = m.group(0);
		}
		int reviews = StringUtils.getInt(reviewStr);

		Product product = new Product(Website.GEEKBUYING, productName, sourceProductId, sourceUrl,
		                              brandName, model, currentPrice, originalPrice,
		                              description, attributes, skus, saleAttributeNames, images, subTitle, reviews);

		return product;
	}

	private LinkedHashMap<String, LinkedHashMap<String, String>> getBasicAttrs(TagNode root) throws ContentParseException {
		LinkedHashMap<String, LinkedHashMap<String, String>> attrMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();

		TagNode attrTable = getSubNodeByXPath(root, XPATH_ATTR_TABLE, null);

		List<TagNode> trs = getSubNodesByXPath(root, "//tr", null);

		LinkedHashMap<String, String> attrs = new LinkedHashMap<String, String>();

		for (TagNode tr : trs) {
			List<TagNode> tds = getSubNodesByXPath(tr, "td", null);

			if (ArrayUtils.isNullOrEmpty(tds)) {
				String title = getSubNodeStringByXPath(tr, "//th", null);
				if (StringUtils.isEmpty(title)) {
					title = getSubNodeStringByXPath(tr, "//th/strong", null);
				}
				if (StringUtils.isEmpty(title)) {
					continue;
				}
				title = StringUtils.unescapeHtml(title);
				attrs = new LinkedHashMap<String, String>();
				attrMap.put(title, attrs);
			} else {
				int len = tds.size();
				switch (len) {
					case 1:
						String tdv = tds.get(0).getText().toString();
						tdv = StringUtils.unescapeHtml(tdv);
						attrs.put(tdv, "");
						break;
					case 2:
						String tdv1 = tds.get(0).getText().toString();
						String tdv2 = tds.get(1).getText().toString();
						tdv1 = StringUtils.unescapeHtml(tdv1);
						tdv2 = StringUtils.unescapeHtml(tdv2);
						attrs.put(tdv1, tdv2);
						break;
					default:
						break;
				}
			}
		}

		return attrMap;
	}

	private void getSkus(TagNode root, List<Sku> skus, List<String> saleAttributeNames,
	                     float currentPrice, float originalPrice) throws ContentParseException {
		TagNode attrNode = getSubNodeByXPath(root, XPATH_SKU_ATTRIBUTE, null);
		if (attrNode == null) {
			// 没有sku
			return;
		}

		List<TagNode> skuNodes = getSubNodesByXPath(attrNode, "//dd/ul/li", null);

		String attrName = "option";

		for (TagNode skuNode : skuNodes) {

			boolean selected = "active".equalsIgnoreCase(skuNode.getAttributeByName("class"));

			TagNode linkNode = getSubNodeByXPath(skuNode, "//a", null);

			String sourceUrl = linkNode.getAttributeByName("href");
			if (!sourceUrl.startsWith("http")) {
				sourceUrl = GeekbuyingHelper.SITE_URL + sourceUrl;
			}

			TagNode imageNode = getSubNodeByXPath(linkNode, "//img", null);
			String imageUrl = imageNode.getAttributeByName("src");

			SkuAttributeValue sav = new SkuAttributeValue(attrName, "", imageUrl);
			Map<String, SkuAttributeValue> attributes = new HashMap<String, SkuAttributeValue>();
			attributes.put(attrName, sav);

			Sku sku = new Sku(GeekbuyingHelper.getProductIdByUrl(sourceUrl), sourceUrl, selected,
			                  currentPrice, originalPrice, attributes);
			skus.add(sku);
		}

		return;
	}
}
