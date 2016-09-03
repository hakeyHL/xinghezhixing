package hasoffer.fetch.sites.flipkart;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.CurrencyCode;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.exception.ImagesNotFoundException;
import hasoffer.fetch.exception.PriceNotFoundException;
import hasoffer.fetch.exception.ProductTitleNotFoundException;
import hasoffer.fetch.helper.SkuHelper;
import hasoffer.fetch.model.*;
import org.apache.commons.collections.ListUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.util.*;

import static hasoffer.base.utils.http.XPathUtils.*;

public class FlipkartProductProcessor implements IProductProcessor {
    private static final String XPATH_PRODUCT_NAME = "//div[@class='product-details line']/div/h1[@class='title']";
    private static final String XPATH_PRODUCT_SUBTITLE = "//div[@class='product-details line']/div/span[@class='subtitle']";
    private static final String XPATH_PRODUCT_SUBTITLE1 = "//h1";
    private static final String XPATH_PRODUoriPriceNodeCT_SUBTITLE = "//div[@class='product-details line']/div/span[@class='subtitle']";
    private static final String XPATH_PRICE_ORIGINAL = "//div[@class='prices']/span[@class='price']";
    private static final String XPATH_PRICE_NOW = "//div[@class='prices']/div/span[@class='selling-price omniture-field']";
    private static final String XPATH_PRICE_NOW2 = "//li[@id='tab-0']/a/div/span";
    private static final String XPATH_DETAILS = "//div[@class='detailssubbox']";
    //private static final String XPATH_IMGS = "//div[@class='baseSliderPager']/div[@class='bx-wrapper']/div[@class='bx-viewport']/div[@id='bx-pager-left-image-panel']";
    private static final String XPATH_IMAGES = "//div[@class='productImages']/div[@class='innerPanel']/div[@class='carouselContainer leftDisabled']/ul/li/div/div";
    private static final String XPATH_REVIEWS = "//a[@class='review']/span";

    private static final String XPATH_SALE_ATTR = "//div[@class='multiSelectionWrapper section line']";

    private static final String XPATH_BASIC_ATTR = "//div[@class='productSpecs specSection']";

    private static final String XPATH_DESCRIPTION = "//div[@class='detailssubbox']/p";

    @Override
    public String getUrlByProductId(String productId) {
        return FlipkartHelper.getProductIdByUrl(productId);
    }

    @Override
    public String getProductIdByUrl(String pageUrl) {
        return null;
    }

    @Override
    public Product parseProduct(String sourceUrl) throws ConnectTimeoutException, HttpFetchException, XPatherException, ContentParseException {
        String sourceProductId = getUrlByProductId(sourceUrl);

        TagNode root = HtmlUtils.getUrlRootTagNode(sourceUrl);
        String productName = getSubNodeStringByXPath(root, XPATH_PRODUCT_NAME,
                new ProductTitleNotFoundException(sourceUrl));
        String subTitle = getSubNodeStringByXPath(root, XPATH_PRODUCT_SUBTITLE, null);
        if (subTitle != null) {
            if (subTitle.indexOf("(") > -1 && subTitle.indexOf(")") > -1) {
                subTitle = subTitle.substring(subTitle.indexOf("(") + 1, subTitle.indexOf(")"));
            }
        } else {
            TagNode titleNode = getSubNodeByXPath(root, XPATH_PRODUCT_SUBTITLE1, null);
            subTitle = titleNode.getText().toString().trim();
        }

        String priceString = "";
        try {
            priceString = getSubNodeStringByXPath(root, XPATH_PRICE_NOW,
                    new ProductTitleNotFoundException(sourceUrl));
        } catch (ProductTitleNotFoundException e) {
            priceString = getSubNodeStringByXPath(root, XPATH_PRICE_NOW2, new ProductTitleNotFoundException(sourceUrl));
        }

        TagNode oriPriceNode = HtmlUtils.getFirstNodeByXPath(root, XPATH_PRICE_ORIGINAL);
        String originalPriceString = null;
        if (oriPriceNode != null) {
            originalPriceString = oriPriceNode.getText().toString();
            originalPriceString = originalPriceString.replace("Rs.", "").trim();
        }

        priceString = priceString.replace("Rs.", "").replace(",", "").trim();

        float currentPrice = Float.parseFloat(priceString.replace(",", ""));
        float originalPrice = 0.0f;
        if (originalPriceString != null) {
            originalPrice = Float.parseFloat(originalPriceString.replace(",", ""));
        }

        List<Sku> skus = new ArrayList<Sku>();
        List<String> saleAttributeNames = new ArrayList<String>();
        TagNode skuNode = HtmlUtils.getFirstNodeByXPath(root, XPATH_SALE_ATTR);
        getSkus(skuNode, skus, saleAttributeNames, currentPrice, originalPrice);

        List<ProductImage> images = new ArrayList<ProductImage>();
        List<TagNode> imageNodes = getSubNodesByXPath(root, XPATH_IMAGES,
                new ImagesNotFoundException(sourceUrl));
        for (TagNode imageNode : imageNodes) {
            String imageStyle = imageNode.getAttributeByName("style");
            int lIndex = imageStyle.indexOf("(");
            int rIndex = imageStyle.indexOf(")");
            String imageUrl = imageStyle.substring(lIndex + 1, rIndex);
            images.add(new ProductImage(imageUrl, imageUrl, imageUrl));
        }

        String description = "";

        TagNode basicNode = HtmlUtils.getFirstNodeByXPath(root, XPATH_BASIC_ATTR);
        LinkedHashMap<String, LinkedHashMap<String, String>> attrMap = getBasicAttributes(basicNode);
        Map<String, String> generalMap = attrMap.get("GENERAL FEATURES");
        if (generalMap == null) {
            generalMap = attrMap.get("GENERAL");
        }

        String brandName = "";
        String model = "";
        if (generalMap != null) {
            brandName = generalMap.get("Brand");
            model = generalMap.get("Model Name");
        }

		/*
        通过ajax获取的评论数，请求如：http://www.flipkart.com/lc/p/pv1/spotList1/spot1/actionBar?pid=MOBECC4UZTSGKWWZ&abData=5ca1092cfc1b9824d49cc034202b06fc&abbuckets=BABAAAdABB&loadDeferred=true&__FK=V2ae482e0a032048e9f323f4e91a6a23f0s2e38bD9EPz8MP1Q0UB8jPz9OP5DGjveyhD0TCxATUPFqWzCEEsYQK7XmrIRygMf%2B5pBDapdtccekGmCfjrB%2B%2FZw0O4wQzZ%2B04s8m5ZWjIERq1%2FzOvNzWWF59ryUS%2BZrzj%2BIFvNnSOkVNqCq0vPEUIeBg4vBqGnw1MbQOxL5hjVsGzDtmgK%2F0NSck7QX9P7Iqfg9iYLU4iUcBwkeqccm%2BHkKeuZ2mcj7X1TMooDwD69KaJl1Mg%2FDTp53tFU5Jk9ktbRTPk7GTUy0TRIQMI1%2B%2BANn7sRTx2LIcRzXJ2l1F6itLkH%2BUYEJrM6zkZwx0IffHjDlOMF7KvPjdMpoBKs%2BBtv0b8DyLVZFtLsxMJTOk%2BcLEQrSO6JO%2BqsW%2FXOOG5SPEX7lL
		String reviewStr = getSubNodeStringByXPath(root, XPATH_REVIEWS, null);
		int reviews = Integer.parseInt(reviewStr);
		*/
        int reviews = 0;
        Product product = new Product(Website.FLIPKART, productName, sourceProductId, sourceUrl,
                brandName, model, currentPrice, originalPrice,
                description, attrMap, skus, saleAttributeNames, images, subTitle, reviews);
        return product;
    }

    public Price getPirce(String url) throws HttpFetchException, ContentParseException {

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        String priceString = getSubNodeStringByXPath(root, XPATH_PRICE_NOW,
                new PriceNotFoundException(url));

        priceString = priceString.replace("Rs.", "").trim();
        priceString = priceString.replace(",", "");

        return new Price(CurrencyCode.INR, Float.valueOf(priceString));
    }

    private void getSkus(TagNode skuNode, List<Sku> skus, List<String> saleAttributeNames,
                         float currentPrice, float originalPrice)
            throws ContentParseException {
        if (skuNode == null) {
            return;
        }

        //String saleAttrPath = "//div[@class='multiSelectionWidget*']";
        //String saleAttrPath = "//div[contains(@class, 'multiSelectionWidget')]";
        try {
            //List<TagNode> nodes = HtmlUtils.getSubNodesByXPath(skuNode, saleAttrPath);
            TagNode[] nodes = skuNode.getElementsByName("div", false);
            //Map<String, List<String>> saleAttrMap = new HashMap<String, List<String>>();
            Map<String, List<SkuAttributeValue>> skuAttributeDefMap = new HashMap<String, List<SkuAttributeValue>>();
            String attrTitle = "//div[@class='multiSelectionWidget-title']";
            String attrs = "//div[@class='multiSelectionWidget-selectors-wrap']";
            if (nodes != null && nodes.length > 0) {
                for (TagNode node : nodes) {
                    String classText = node.getAttributeByName("class");
                    if (classText == null || !classText.startsWith("multiSelectionWidget")) {
                        continue;
                    }

                    String attrName = getSubNodeStringByXPath(node, attrTitle, null);
                    attrName = attrName.replace("Select", "").trim().toLowerCase();
                    saleAttributeNames.add(attrName);

                    TagNode attrisNode = HtmlUtils.getFirstNodeByXPath(node, attrs);

                    //TagNode[] attrValNodes = attrisNode.getAllElements(false);
                    //List<TagNode> attrValNodes = HtmlUtils.getSubNodesByXPath(attrisNode, "//div[@class='multiSelectionWidget-selector']");
                    //List<TagNode> attrValNodes = HtmlUtils.getSubNodesByXPath(attrisNode, "//div[@class][@title]|//a/div[@class][@title]");
                    List<TagNode> attrValNodesSelected = HtmlUtils.getSubNodesByXPath(attrisNode, "/div[@class]");
                    List<TagNode> attrValNodesUnSelected = HtmlUtils.getSubNodesByXPath(attrisNode, "/a/div[@class]");
                    List<TagNode> attrValNodes = ListUtils.sum(attrValNodesSelected, attrValNodesUnSelected);

                    if (attrValNodes != null && attrValNodes.size() > 0) {
                        List<SkuAttributeValue> valList = new LinkedList<SkuAttributeValue>();
                        for (TagNode attrNode : attrValNodes) {
                            String attrVal = attrNode.getAttributeByName("title");
                            if (org.apache.commons.lang.StringUtils.isNotBlank(attrVal)) {

                            } else {
                                attrVal = attrNode.getText().toString();

                            }

                            valList.add(new SkuAttributeValue(attrVal));
                        }

                        //saleAttrMap.put(attrName, valList);
                        skuAttributeDefMap.put(attrName, valList);
                    }
                }

                List<Map<String, SkuAttributeValue>> skuAttributes = SkuHelper.transformSkus(skuAttributeDefMap, saleAttributeNames);
                for (Map<String, SkuAttributeValue> attrMap : skuAttributes) {
                    skus.add(new Sku("", "", false,
                            currentPrice, originalPrice,
                            attrMap));
                }

            }

        } catch (XPatherException e) {
            e.printStackTrace();
        }
    }

    private LinkedHashMap<String, LinkedHashMap<String, String>> getBasicAttributes(TagNode basicNode)
            throws ContentParseException, XPatherException {
        LinkedHashMap<String, LinkedHashMap<String, String>> attrMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        String basicNodeCates = "//table/tbody";
        List<TagNode> nodes = HtmlUtils.getSubNodesByXPath(basicNode, basicNodeCates);
        if (nodes != null && nodes.size() > 0) {
            String basicNodeCatePath = "//tr";
            for (TagNode node : nodes) {
                try {
                    List<TagNode> trs = HtmlUtils.getSubNodesByXPath(node, basicNodeCatePath);
                    String tagName = trs.get(0).getText().toString().trim();
                    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                    for (int i = 1; i < trs.size(); i++) {
                        TagNode trNode = trs.get(i);
                        TagNode[] tdNodes = trNode.getElementsByName("td", false);
                        //如果规格页中的参数为一条的话
                        String attrName = "";
                        String attrValue = "";
                        if (tdNodes.length == 1) {
                            attrName = tdNodes[0].getText().toString().trim();
                            attrValue = attrName;
                        } else {
                            attrName = tdNodes[0].getText().toString().trim();
                            attrValue = tdNodes[1].getText().toString().trim();
                        }
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
