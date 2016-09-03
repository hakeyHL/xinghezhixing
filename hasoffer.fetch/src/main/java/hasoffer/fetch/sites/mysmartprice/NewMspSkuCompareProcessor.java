package hasoffer.fetch.sites.mysmartprice;

import com.alibaba.fastjson.JSON;
import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.UrlUtils;
import hasoffer.base.utils.http.HttpUtils;
import hasoffer.fetch.exception.ProductTitleNotFoundException;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.sites.mysmartprice.model.MySmartPriceCmpSku;
import hasoffer.fetch.sites.mysmartprice.model.MySmartPricePriceTable;
import hasoffer.fetch.sites.mysmartprice.model.MySmartPriceProduct;
import org.apache.commons.lang3.math.NumberUtils;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hasoffer.base.utils.http.XPathUtils.*;

/**
 * Created by chevy on 2015/12/3.
 */
public class NewMspSkuCompareProcessor {

    private static final Logger logger = LoggerFactory.getLogger(NewMspSkuCompareProcessor.class);

    private static final String XPATH_TITLE = "//h1[@class='prdct-dtl__ttl']";
    private static final String XPATH_TITLE_2 = "//h1[@id='mspSingleTitle']";
    private static final String XPATH_RATE = "//span[@class='view_review_count']/span[@itemprop='reviewCount']";
    private static final String XPATH_PRICE = "//span[@class='prdct-dtl__slr-prc-rcmnd-val']";
    private static final String XPATH_PRICE1 = "//div[@class='smart_price']";
    private static final String XPATH_IMAGE = "//img[@class='prdct-dtl__img']";
    private static final String XPATH_IMAGE1 = "//img[@id='mspSingleImg']";
    private static final String XPATH_IMAGE_SMALL = "//img[@class='prdct-dtl__thmbnl-img']";
    private static final String XPATH_IMAGE_SMALL1 = "//a[@class='product-thumb']";
    private static final String XPATH_COLORS = "//div[@class='avlbl-clrs']/label";
    private static final String XPATH_SIZES = "//div[@class='avlbl-sizes']/div/span";
    private static final String XPATH_DETAILS = "//div[@class='sctn__inr sctn__inr--spec algn-left']";
    private static final String XPATH_FEATURES_SHOW = "//ul[@class='prdct-dtl__spfctn-wrpr']/li/span";
    private static final String XPATH_FEATURES_HIDE = "//ul[@class='prdct-dtl__spfctn-more-wrpr']/li/span";
    private static final String XPATH_DESCRIPTION = "//div[@data-id='product-details']//p";

    private static final String XPATH_SKU_DIV = "//div[@id='pricetable']/div";

    public MySmartPriceProduct parse(String url) throws HttpFetchException, ContentParseException {

		/*if (StringUtils.isEmpty(sourceId)) {

		}*/
        String sourceId = MspHelper.getProductIdByUrl(url);

        if (StringUtils.isEmpty(sourceId)) {
            logger.error("source id is null. url : " + url);
            return null;
        }

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        String title = getSubNodeStringByXPath(root, XPATH_TITLE, null);
        if (StringUtils.isEmpty(title)) {
            title = getSubNodeStringByXPath(root, XPATH_TITLE_2, new ProductTitleNotFoundException(url));
        }

        String ratingStr = getSubNodeStringByXPath(root, XPATH_RATE, null);
        int rating = 0;
        if (!StringUtils.isEmpty(ratingStr)) {
            rating = Integer.parseInt(ratingStr);
        }

        String priceStr = getSubNodeStringByXPath(root, XPATH_PRICE, null);
        if (StringUtils.isEmpty(priceStr)) {
            priceStr = getSubNodeStringByXPath(root, XPATH_PRICE1, null);
        }


        float price = 0.0f;
        priceStr = StringUtils.filterAndTrim(priceStr, Arrays.asList("&#x20B9;", ","));
        if (NumberUtils.isNumber(priceStr)) {
            price = Float.valueOf(priceStr);
        }

        List<String> imageUrls = new ArrayList<String>();

        TagNode mainImageNode = getSubNodeByXPath(root, XPATH_IMAGE, null);
        if (mainImageNode != null) {
            imageUrls.add(mainImageNode.getAttributeByName("src"));
        } else {
            mainImageNode = getSubNodeByXPath(root, XPATH_IMAGE1, null);
            imageUrls.add(mainImageNode.getAttributeByName("src"));
        }

        List<TagNode> imageNodes = getSubNodesByXPath(root, XPATH_IMAGE_SMALL, null);
        if (!ArrayUtils.hasObjs(imageNodes)) {
            imageNodes = getSubNodesByXPath(root, XPATH_IMAGE_SMALL1, null);

        }
        for (TagNode imageNode : imageNodes) {
            String imageUrl = imageNode.getAttributeByName("src");
            if (!StringUtils.isEmpty(imageUrl) && imageUrl.startsWith("http")) {
                imageUrls.add(imageUrl);
            }
        }

        List<TagNode> colorNodes = getSubNodesByXPath(root, XPATH_COLORS, null);
        List<String> colors = new ArrayList<String>();
        if (ArrayUtils.hasObjs(colorNodes)) {
            for (TagNode color : colorNodes) {
                String colorStr = color.getAttributeByName("data-tooltip");
                if (StringUtils.isEmpty(colorStr)) {
                    continue;
                }
                colors.add(colorStr);
            }
        }

        List<String> sizes = getSubNodesStringsByXPath(root, XPATH_SIZES, null);

        // features
        List<String> features_show = getSubNodesStringsByXPath(root, XPATH_FEATURES_SHOW, null);
        List<String> features_hide = getSubNodesStringsByXPath(root, XPATH_FEATURES_HIDE, null);

        List<String> features = new ArrayList<String>();
        if (ArrayUtils.hasObjs(features_show)) {
            for (String f : features_show) {
                if (f.toLowerCase().contains("view more")) {
                    continue;
                }
                features.add(f);
            }
        }
        if (ArrayUtils.hasObjs(features_hide)) {
            for (String f : features_hide) {
                if (f.toLowerCase().contains("view less")) {
                    continue;
                }
                features.add(f);
            }
        }

        String description = "";
        List<String> descriptions = getSubNodesStringsByXPath(root, XPATH_DESCRIPTION, null);
        if (!ArrayUtils.isNullOrEmpty(descriptions)) {
            StringBuffer sb = new StringBuffer();
            for (String desc : descriptions) {
                sb.append("<p>").append(desc).append("</p>");
            }
            description = sb.toString();
        }

        Map<String, Map<String, String>> detailsMap = getDetails(url);

        List<MySmartPriceCmpSku> mySmartPriceCmpSkus = new ArrayList<MySmartPriceCmpSku>();
        getCmpSkus(root, mySmartPriceCmpSkus, sourceId);

        MySmartPriceProduct mySmartPriceProduct = new MySmartPriceProduct();
        mySmartPriceProduct.setMspId(sourceId);
        mySmartPriceProduct.setTitle(title);
        mySmartPriceProduct.setPrice(price);
        mySmartPriceProduct.setUrl(url);
        mySmartPriceProduct.setColors(colors);
        mySmartPriceProduct.setSizes(sizes);
        mySmartPriceProduct.setRating(rating);

        mySmartPriceProduct.setImageUrls(imageUrls);//productid, imageurl
        mySmartPriceProduct.setBaseAttrs(detailsMap);//group, key,value

        mySmartPriceProduct.setFeatures(features);
        mySmartPriceProduct.setDescription(description);

        mySmartPriceProduct.setCmpSkus(mySmartPriceCmpSkus);//

        return mySmartPriceProduct;
    }

    private void getCmpSkus(TagNode pageRoot, List<MySmartPriceCmpSku> mySmartPriceCmpSkus, String sourceId)
            throws HttpFetchException, ContentParseException {

        MySmartPricePriceTable priceTable = null;
        String url = MspHelper.getPriceTableUrl(sourceId);

        HttpResponseModel responseModel = HttpUtils.get(url, null);
        if (responseModel.getStatusCode() == 200) {
            priceTable = JSON.parseObject(responseModel.getBodyString(), MySmartPricePriceTable.class);
        }

        if (priceTable == null) {
            throw new HttpFetchException(url);
        }

        TagNode root = HtmlUtils.getTagNode(priceTable.getPricetable());

        List<TagNode> pros = getSubNodesByXPath(root, XPATH_SKU_DIV, null);

        if (ArrayUtils.isNullOrEmpty(pros)) {
            pros = getSubNodesByXPath(pageRoot, XPATH_SKU_DIV, null);
        }

        for (TagNode pro : pros) {
            try {
                getPro(mySmartPriceCmpSkus, pro);
            } catch (Exception e) {
                continue;
            }
        }
    }

    private String getPrice(String priceStr) {
        return StringUtils.filterAndTrim(StringUtils.unescapeHtml(priceStr), Arrays.asList("₹", ","));
    }

    public void getPro(List<MySmartPriceCmpSku> mySmartPriceCmpSkus, TagNode pro)
            throws HttpFetchException, ContentParseException {
        MySmartPriceCmpSku mySmartPriceCmpSku = new MySmartPriceCmpSku();

        String storeSite = pro.getAttributeByName("data-storename");

        if (StringUtils.isEmpty(storeSite)) {
            return;
        }

        String storeName = getSubNodeStringByXPath(pro, "//div[@class='store_pricetable_main']/div[@class='store_info']/div[@class='store_seller']", null);
        if (!StringUtils.isEmpty(storeName)) {
            storeName = StringUtils.filterAndTrim(storeName, Arrays.asList("Seller:"));
        }

        String color = getSubNodeStringByXPath(pro, "//span[@class='colour_name']", null);
        String size = getSubNodeStringByXPath(pro, "//span[@class='size_name']", null);

        String priceStr = pro.getAttributeByName("data-pricerank");
        float price = Float.valueOf(priceStr);

        TagNode priceInfoNode = getSubNodeByXPath(pro, "//div[@class='store_price_info']", null);
        TagNode[] pis = priceInfoNode.getChildTags();

        String url = null;
        for (TagNode t : pis) {
            if (t.getAttributeByName("class").contains("store_gostore")) {
                TagNode urlNode = t.getChildTags()[0];
                url = urlNode.getAttributeByName("data-url");
            }
        }

        TagNode root = HtmlUtils.getUrlRootTagNode(url);
        TagNode linkNode = getSubNodeByXPath(root, "//a[@class='store-link']", null);

        String realUrl = linkNode.getAttributeByName("href");
        realUrl = StringUtils.urlDecode(realUrl);

        String _realUrl = UrlUtils.getParam(realUrl, "url");

        if (StringUtils.isEmpty(_realUrl)) {
            _realUrl = WebsiteHelper.getRealUrl(realUrl);
        } else {
            _realUrl = WebsiteHelper.getRealUrl(_realUrl);
        }
        if (!StringUtils.isEmpty(_realUrl)) {
            realUrl = _realUrl;
        }

        mySmartPriceCmpSku.setUrl(realUrl);
        mySmartPriceCmpSku.setPrice(price);
        mySmartPriceCmpSku.setRating("0");
        mySmartPriceCmpSku.setWebsite(WebsiteHelper.getWebSite(realUrl));
        mySmartPriceCmpSku.setSeller(storeSite);
        mySmartPriceCmpSku.setColor(color);
        mySmartPriceCmpSku.setSize(size);

        mySmartPriceCmpSkus.add(mySmartPriceCmpSku);
    }

    private Map<String, Map<String, String>> getDetails(String url) throws HttpFetchException, ContentParseException {

        Map<String, Map<String, String>> detailMap = new LinkedHashMap<String, Map<String, String>>();

        String newUrl = "http://www.mysmartprice.com/product";

        Pattern pattern = Pattern.compile("-(ms[p|f])\\d+");
        Matcher m = pattern.matcher(url);
        if (m.find()) {
            int s = m.start(1);
            int e = m.end(1);
            String u1 = url.substring(0, s);
            String u2 = url.substring(e);
            StringBuffer sb = new StringBuffer(u1.replace("http://www.mysmartprice.com", newUrl));
            newUrl = sb.append("mst").append(u2).append("-spec").toString();
        }

        TagNode root = HtmlUtils.getUrlRootTagNode(newUrl);
        TagNode detailTableNode = getSubNodeByXPath(root, XPATH_DETAILS, null);

        if (detailTableNode == null) {
            return detailMap;
        }

        TagNode[] trs = detailTableNode.getChildTags();

        if (trs == null || trs.length <= 0) {
            return detailMap;
        }
        /**
         * tchncl-spcftn__ctgry
         *
         * tchncl-spcftn__item
         * tchncl-spcftn__item-key
         * tchncl-spcftn__item-val
         */
        String key = "";
        Map<String, String> attrs = null;
        for (TagNode tr : trs) {

            String style = tr.getAttributeByName("class");
            if ("tchncl-spcftn__ctgry".equalsIgnoreCase(style)) {
                key = tr.getText().toString();
                attrs = new LinkedHashMap<String, String>();
                detailMap.put(key, attrs);
            } else if ("tchncl-spcftn__item".equalsIgnoreCase(style)) {
                List<TagNode> kvs = tr.getChildTagList();
                if (ArrayUtils.isNullOrEmpty(kvs) || kvs.size() != 2) {
                    continue;
                }
                if (attrs == null) {
                    attrs = detailMap.get("default");
                    if (attrs == null) {
                        attrs = new LinkedHashMap<String, String>();
                        detailMap.put("default", attrs);
                    }
                }
                String k = getSubNodeStringByXPath(tr, "//div[@class='tchncl-spcftn__item-key']", null);
                String v = getSubNodeStringByXPath(tr, "//div[@class='tchncl-spcftn__item-val']", null);
                if (StringUtils.isEmpty(k) || StringUtils.isEmpty(v)) {
                    continue;
                }
                attrs.put(k, v);
            }
        }

        return detailMap;
    }
}
