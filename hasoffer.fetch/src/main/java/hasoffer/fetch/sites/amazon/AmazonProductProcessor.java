package hasoffer.fetch.sites.amazon;


import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.exception.ImagesNotFoundException;
import hasoffer.fetch.model.Product;
import hasoffer.fetch.model.ProductImage;
import hasoffer.fetch.model.Sku;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hasoffer.base.utils.http.XPathUtils.*;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/26
 */
public class AmazonProductProcessor implements IProductProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AmazonProductProcessor.class);

    private static final Pattern PATTERN_ORIGINAL_PRICE = Pattern.compile("^\\$(\\d+.\\d+)");

    private static final String XPATH_PRODUCT_NAME = "//*[@id='title']";
    private static final String XPATH_PRODUCT_NAME1 = "//div[@id='olpProductDetails']/h1";
    private static final String XPATH_BRAND = "//*[@id='brand']";

    private static final String XPATH_PRICE_ORIGINAL =
            "//div[@id='ptm']/table[@class='a-lineitem']/tbody/tr[1]/td[@class='a-span12 a-color-secondary a-size-base a-text-strike']";

    private static final String XPATH_DESCRIPTION = "//div[@id='productDescription']";
    private static final String XPATH_SMALL_IMAGES = "//*[@class='a-button-text']/img";

    private static final String XPATH_PRODUCT_DETAIL = "//div[@id='prodDetails']";

    private static final String XPATH_SKU_DIV = "//div[@id='twisterContainer']";

    private static final String ATTRIBUTE_ID_LABEL = "ASIN";
    private static final String ATTRIBUTE_MODEL_LABEL = "Item model number";
    private static final String XPATH_SOLD_COUNT = "//span[@id='acrCustomerReviewText']";

    public AmazonProductProcessor() {
    }

    @Override
    public String getUrlByProductId(String productId) {
        return AmazonHelper.getUrlByProductId(productId);
    }

    @Override
    public String getProductIdByUrl(String pageUrl) {
        return AmazonHelper.getProductIdByUrl(pageUrl);
    }

    @Override
    public Product parseProduct(String sourceUrl) throws HttpFetchException, XPatherException, ContentParseException {
        String sourceProductId = getProductIdByUrl(sourceUrl);

        TagNode root = HtmlUtils.getUrlRootTagNode(sourceUrl);

        // product name
        TagNode productNameNode = getSubNodeByXPath(root, XPATH_PRODUCT_NAME, null);
        if (productNameNode == null) {
            productNameNode = getSubNodeByXPath(root, XPATH_PRODUCT_NAME1, null);
        }
        String productName = productNameNode.getText().toString().replace("New offers for","").trim();

        // prices
        String priceString = AmazonHelper.getCurrentPriceString(root);
        String originalPriceString = getOriginalPriceString(root, priceString);

        float currentPrice = Float.parseFloat(priceString.replace(",", ""));
        float originalPrice = Float.parseFloat(originalPriceString.replace(",", ""));

        // discount product
//		BigDecimal discount = new BigDecimal((originalPrice - currentPrice) / originalPrice);

        // brandName
        String brandName = getSubNodeStringByXPath(root, XPATH_BRAND, null);

        // category
        // TODO

        // TODO skus
        List<Sku> skus = new ArrayList<Sku>();
        List<String> saleAttributeNames = new ArrayList<String>();
        getSkus(root, currentPrice, originalPrice);

        // attributeMap
        LinkedHashMap<String, LinkedHashMap<String, String>> attributes = null;
        LinkedHashMap<String, String> attributeMap = getProductAttributes(root);
        if (attributeMap.containsKey(ATTRIBUTE_ID_LABEL)) {
            String sourcePID = attributeMap.get(ATTRIBUTE_ID_LABEL);
            if (StringUtils.isEmpty(sourceProductId)) {
                sourceProductId = sourcePID;
            }
        }

        String model = "";
        if (attributeMap.containsKey(ATTRIBUTE_MODEL_LABEL)) {
            model = attributeMap.get(ATTRIBUTE_MODEL_LABEL);
        }

        // description
        List<TagNode> descriptionNodeList = getSubNodesByXPath(root, XPATH_DESCRIPTION, null);
        StringBuffer sb = new StringBuffer();
        for (TagNode node : descriptionNodeList) {
            sb.append(HtmlUtils.getInnerHTML(node));
        }
        String description = StringUtils.filterAndTrim(sb.toString(),
                Arrays.asList("\r", "\n", "\t", "<table.+</table>", "<script>.+</script>"));

        // images
        List<ProductImage> images = new ArrayList<ProductImage>();
        List<TagNode> imageNodes = getSubNodesByXPath(root,
                XPATH_SMALL_IMAGES,
                new ImagesNotFoundException(sourceUrl));
        for (TagNode imageNode : imageNodes) {
            String imageUrl = imageNode.getAttributeByName("src");
            //http://ecx.images-amazon.com/images/I/41zvK71x0BL._SS40_.jpg
            Pattern p_x = Pattern.compile("\\._.+_");
            Matcher urlMatcher = p_x.matcher(imageUrl);
            imageUrl = urlMatcher.replaceAll("");
            if (imageUrl.startsWith("//")) {
                imageUrl = "http:" + imageUrl;
            }
            images.add(new ProductImage(imageUrl, imageUrl, imageUrl));
        }

        String subTitle = "";//getSubNodeStringByXPath(root, XPATH_SUBTITLE, null);

        String reviewStr = getSubNodeStringByXPath(root, XPATH_SOLD_COUNT, null);

        int reviews = 0;
        if (reviewStr != null) {
            Matcher m = Pattern.compile("\\d+").matcher(reviewStr);
            if (m.find()) {
                reviewStr = m.group(0);
            }
            reviews = StringUtils.getInt(reviewStr);
        }

        Product product = new Product(Website.AMAZON, productName, sourceProductId, sourceUrl,
                brandName, model, currentPrice, originalPrice,
                description, attributes, skus, saleAttributeNames, images, subTitle, reviews);
        return product;
    }

    private LinkedHashMap<String, String> getProductAttributes(TagNode root) throws ContentParseException {
        LinkedHashMap<String, String> attributeMap = new LinkedHashMap<String, String>();
        TagNode productDetail = getSubNodeByXPath(root, XPATH_PRODUCT_DETAIL, null);

        List<TagNode> attrNodes = getSubNodesByXPath(root, "//div[@class='pdTab']/table/tbody/tr", null);

        for (TagNode attrNode : attrNodes) {
            String label = getSubNodeStringByXPath(attrNode, "//td[@class='label']", null);
            String value = getSubNodeStringByXPath(attrNode, "//td[@class='value']", null);

            if (StringUtils.isEmpty(label) ||
                    StringUtils.isEmpty(value) ||
                    label.equalsIgnoreCase("null") ||
                    value.equalsIgnoreCase("null")) {
                continue;
            }

            label = label.trim().replaceAll("\\n", "");
            value = value.trim().replaceAll("\\n", "");

            attributeMap.put(label, value);
        }

        return attributeMap;
    }

    private String getOriginalPriceString(TagNode root, String priceString) throws ContentParseException {
        String originalPriceString = getSubNodeStringByXPath(root, XPATH_PRICE_ORIGINAL, null);
        if (StringUtils.isEmpty(originalPriceString)) {
            originalPriceString = priceString;
        }
        Matcher priceMatcher = PATTERN_ORIGINAL_PRICE.matcher(originalPriceString);
        if (priceMatcher.matches()) {
            originalPriceString = priceMatcher.group(1);
        }
        return originalPriceString;
    }

    private void getSkus(TagNode root, float currentPrice, float originalPrice) throws ContentParseException {
        /*TagNode skuListNode = getSubNodeByXPath(root, XPATH_SKU_DIV, null);

		if (skuListNode == null) {
			return;
		}

		List<TagNode> skuAttrNodes = getSubNodesByXPath(skuListNode, "//form/div", null);

		if (skuAttrNodes == null || skuAttrNodes.isEmpty()) {
			// 商品本身只有一个sku
			return SkuUtils.initDefaultSku(currentPrice, originalPrice);
		}

		int rank = 0;
		for (TagNode tagNode : skuAttrNodes) {
			List<SkuNameValuePair> valuePairs = new ArrayList<SkuNameValuePair>();

			String name = getSubNodeStringByXPath(tagNode, "//div/label", null);

			String attr = "alt";
			List<TagNode> valueTags =
					getSubNodesByXPath(tagNode, "//button/div", null);///div[@class='twisterTextDiv text']/span // /div/img

			int valueRank = 0, size = valueTags.size();
			for (TagNode skuAttrTag : valueTags) {

				TagNode valueTag = getSubNodeByXPath(skuAttrTag, "//div/img", null);
				if (valueTag == null) {
					valueTag = getSubNodeByXPath(skuAttrTag, "//div[@class='twisterTextDiv text']/span", null);
					attr = "";
				}

				String value = valueTag.getAttributeByName(attr);
				if ("".equals(attr)) {
					value = valueTag.getText().toString();
				}

				String skuPriceStr = getSubNodeStringByXPath(skuAttrTag, "//div[@class='twisterSlotDiv']/span", null);
				if (StringUtils.isNotBlankOrNull(skuPriceStr)) {
					Matcher priceMather = PATTERN_CURRENT_PRICE.matcher(skuPriceStr);
					if (priceMather.matches()) {
						skuPriceStr = priceMather.group(1);
//						skuPriceMap.put(value, skuPriceStr);
					}
				}

				valuePairs.add(new SkuNameValuePair(name, value, rank, valueRank++, skuPriceStr));
			}
			rank++;
			skuNameValues.add(valuePairs);
		}*/
    }
}
