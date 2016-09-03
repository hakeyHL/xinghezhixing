package hasoffer.fetch.sites.snapdealDeprecated;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.CurrencyCode;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.http.XPathUtils;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.exception.ImagesNotFoundException;
import hasoffer.fetch.exception.ProductTitleNotFoundException;
import hasoffer.fetch.model.*;
import hasoffer.fetch.sites.snapdeal.SnapdealHelper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeStringByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Created on 2015/12/9.
 */
public class SnapdealProductProcessor implements IProductProcessor {
    private static final String XPATH_PRODUCT_NAME = "//h1[@itemprop='name']";
    private static final String XPATH_PRICE_ORIGINAL = "//span[@class='pdpCutPrice']";
    private static final String XPATH_PRICE_NOW = "//span[@class='payBlkBig']";
    private static final String XPATH_DETAILS = "//div[@class='detailssubbox']";
    //private static final String XPATH_IMGS = "//div[@class='baseSliderPager']/div[@class='bx-wrapper']/div[@class='bx-viewport']/div[@id='bx-pager-left-image-panel']";
    private static final String XPATH_IMAGES = "//div[@id='bx-pager-left-image-panel']/a/img";
    private static final String XPATH_REVIEWS = "//div[@class='review-wrapper productWriteReview']/a";

    private static final String XPATH_SALE_ATTR = "//div[@id='product-attr-options']";

    private static final String XPATH_BASIC_ATTR = "//div[@class='spec-body']/div[@class='detailssubbox']/table[last()]/tbody";

    private static final String XPATH_DESCRIPTION = "//div[@class='detailssubbox']/p";

    @Override
    public String getUrlByProductId(String productId) {
        return SnapdealHelper.getUrlByProductId(productId);
    }

    @Override
    public String getProductIdByUrl(String pageUrl) {
        return SnapdealHelper.getProductIdByUrl(pageUrl);
    }

    public String getNodeContent(TagNode parentNode, String xpath, ContentParseException exceptionToThrow) {
        try {
            TagNode node = XPathUtils.getSubNodeByXPath(parentNode, xpath, exceptionToThrow);
            return HtmlUtils.getHtml(node);
        } catch (ContentParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Product parseProduct(String sourceUrl)
            throws ConnectTimeoutException, HttpFetchException, XPatherException, ContentParseException {

        String sourceProductId = getProductIdByUrl(sourceUrl);

        TagNode root = HtmlUtils.getUrlRootTagNode(sourceUrl);

        String productName = getSubNodeStringByXPath(root, XPATH_PRODUCT_NAME,
                new ProductTitleNotFoundException(sourceUrl));
        String subTitle = ""; // 没有

        String priceString = getSubNodeStringByXPath(root, XPATH_PRICE_NOW,
                new ProductTitleNotFoundException(sourceUrl));

        String originalPriceString = getSubNodeStringByXPath(root, XPATH_PRICE_ORIGINAL,
                new ProductTitleNotFoundException(sourceUrl));

        if (originalPriceString.indexOf(";") > -1) {
            originalPriceString = originalPriceString.substring(originalPriceString.indexOf(";") + 1);
        }

        float currentPrice = Float.parseFloat(priceString.replace(",", ""));
        float originalPrice = Float.parseFloat(originalPriceString.replace(",", ""));

		/*String details = this.getNodeContent(root, XPATH_DETAILS, null);
        String brandPattern = "Brand</td>\\s*<td>(.+?)</td>";
		String modelPattern = "Model</td>\\s*<td>(.+?)</td>";
		Pattern brandP = Pattern.compile(brandPattern);
		Pattern modelP = Pattern.compile(modelPattern);

		String brandName = "";
		String model = "";

		Matcher mBrand = brandP.matcher(details);
		if (mBrand.matches()){
			brandName = mBrand.group(1);
		}

		Matcher mModel = modelP.matcher(details);
		if (mModel.matches()){
			model = mModel.group(1);
		}
*/
        // TODO skus
        List<Sku> skus = new ArrayList<Sku>();
        List<String> saleAttributeNames = new ArrayList<String>();
        TagNode skuNode = HtmlUtils.getFirstNodeByXPath(root, XPATH_SALE_ATTR);
        getSkus(skuNode, skus, saleAttributeNames, currentPrice, originalPrice);

        List<ProductImage> images = new ArrayList<ProductImage>();
        List<TagNode> imageNodes = getSubNodesByXPath(root, XPATH_IMAGES,
                new ImagesNotFoundException(sourceUrl));
        for (TagNode imageNode : imageNodes) {
            String imageUrl = imageNode.getAttributeByName("src");
            if (imageUrl == null) {
                imageUrl = imageNode.getAttributeByName("lazysrc");
            }

            images.add(new ProductImage(imageUrl, imageUrl, imageUrl));
        }

        String description = "";
        List<TagNode> descNodes = getSubNodesByXPath(root, XPATH_DESCRIPTION, null);
        StringBuffer sb = new StringBuffer();
        for (TagNode node : descNodes) {
            sb.append(HtmlUtils.getInnerHTML(node));
        }

        description = StringUtils.filterAndTrim(sb.toString(), Arrays.asList("\r", "\n", "\t"));

        TagNode basicNode = HtmlUtils.getFirstNodeByXPath(root, XPATH_BASIC_ATTR);
        LinkedHashMap<String, LinkedHashMap<String, String>> attrMap = getBasicAttributes(basicNode);
        Map<String, String> generalMap = attrMap.get("General");
        String brandName = generalMap.get("Brand");
        String model = generalMap.get("Model");

        String reviewStr = getSubNodeStringByXPath(root, XPATH_REVIEWS, null);
        int reviews = SnapdealHelper.getReviewCountFromStr(reviewStr);

        Product product = new Product(Website.SNAPDEAL, productName, sourceProductId, sourceUrl,
                brandName, model, currentPrice, originalPrice,
                description, attrMap, skus, saleAttributeNames, images, subTitle, reviews);
        return product;
    }

    public Price getPirce(String url) throws HttpFetchException, ContentParseException {

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        String price = getSubNodeStringByXPath(root, "//span[@class='payBlkBig']", null);
        price = StringUtils.filterAndTrim(StringUtils.unescapeHtml(price), Arrays.asList(""));

        return new Price(CurrencyCode.INR, Float.valueOf(price));
    }

    private void getSkus(TagNode skuNode, List<Sku> skus, List<String> saleAttributeNames,
                         float currentPrice, float originalPrice)
            throws ContentParseException {
        if (skuNode == null) {
            return;
        }

        String saleAttrPath = "//span/ul";
        try {
            List<TagNode> nodes = HtmlUtils.getSubNodesByXPath(skuNode, saleAttrPath);
            Map<String, List<String>> saleAttrMap = new HashMap<String, List<String>>();
            if (nodes != null && nodes.size() > 0) {
                for (TagNode node : nodes) {
                    String attrName = node.getAttributeByName("data-attrname");
                    saleAttributeNames.add(attrName);

                    List<TagNode> attrValNodes = getSubNodesByXPath(node, "//li", null);
                    if (attrValNodes != null && attrValNodes.size() > 0) {
                        List<String> valList = new LinkedList<String>();
                        for (TagNode attrNode : attrValNodes) {
                            String attrVal = attrNode.getAttributeByName("attrid");
                            valList.add(attrVal);
                        }

                        saleAttrMap.put(attrName, valList);
                    }
                }

		/*		int skuCount = 1;
				for (int i = 0; i < saleAttributeNames.size(); i++){  //计算sku数量
					skuCount *= saleAttrMap.get(saleAttributeNames.get(i)).size();
				}*/

                List<Map<String, String>> mapList = new LinkedList<Map<String, String>>();
                for (String attrName : saleAttributeNames) {
                    expandAttriByMultiply(mapList, attrName, saleAttrMap.get(attrName));
                }

                for (Map<String, String> map : mapList) {
                    Map<String, SkuAttributeValue> attrMap = new HashMap<String, SkuAttributeValue>();
                    for (String key : map.keySet()) {
                        attrMap.put(key, new SkuAttributeValue(map.get(key)));
                    }

                    skus.add(new Sku("", "", false,
                            currentPrice, originalPrice,
                            attrMap));
                }
				/*for (int i = 0; i < skuCount; i++){  //建立足够的map容器存储sku属性
					mapList.add(new HashMap<String, String>());
				}*/


				/*for (int i = 0; i < saleAttributeNames.size(); i++){
					int len = saleAttrMap.get(saleAttributeNames.get(i)).size();
				}*/

            }

        } catch (XPatherException e) {
            e.printStackTrace();
        }
    }

    private void expandAttriByMultiply(List<Map<String, String>> attrs, String attrName, List<String> attrValues) {

        if (attrs.size() > 0) {
            int inputCount = attrs.size();
            for (int i = 0; i < attrValues.size(); i++) {
                if (i == attrValues.size() - 1) {
                    for (int j = 0; j < inputCount; j++) {
                        attrs.get(j).put(attrName, attrValues.get(i));
                    }
                } else {
                    for (int j = 0; j < inputCount; j++) {
                        try {
                            Map cloneMap = (Map) BeanUtils.cloneBean(attrs.get(j));
                            cloneMap.put(attrName, attrValues.get(i));
                            attrs.add(cloneMap);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } else { // 第一个属性向量
            for (String value : attrValues) {
                Map<String, String> attr = new HashMap<String, String>();
                attr.put(attrName, value);
                attrs.add(attr);
            }
        }
    }

    private LinkedHashMap<String, LinkedHashMap<String, String>> getBasicAttributes(TagNode basicNode) throws ContentParseException {
        LinkedHashMap<String, LinkedHashMap<String, String>> attrMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        String basicNodeCatePath = "//tbody/tr";
        TagNode[] nodes = basicNode.getAllElements(false);
        if (nodes != null && nodes.length > 0) {
            for (TagNode node : nodes) {
                try {
                    List<TagNode> trs = HtmlUtils.getSubNodesByXPath(node, basicNodeCatePath);
                    String tagName = trs.get(0).getText().toString();
                    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                    for (int i = 1; i < trs.size(); i++) {
                        TagNode trNode = trs.get(i);
                        TagNode[] tdNodes = trNode.getElementsByName("td", false);
                        String attrName = tdNodes[0].getText().toString();
                        String attrValue = tdNodes[1].getText().toString();
                        map.put(attrName, attrValue);
                    }

                    attrMap.put(tagName, map);
                } catch (XPatherException e) {
                    e.printStackTrace();
                }

            }
        }

        return attrMap;
    }
}
