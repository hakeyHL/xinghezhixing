package hasoffer.fetch.sites.flipkart;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.ISummaryProductProcessor;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.model.OriFetchedProduct;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;


/**
 * Created on 2016/2/29.
 */
public class FlipkartSummaryProductProcessor implements ISummaryProductProcessor {

    private static final String WEBSITE_URL = "http://www.flipkart.com";
    private static final String XPATH_TITLE = "//h1[@itemprop='name']";
    private static final String XPATH_SUBTITLE = "//span[@class='subtitle']";
    private static final String XPATH_PRICE1 = "//span[@class='selling-price omniture-field']";
    //    private static final String XPATH_PRICE2 = "//*[@id=\"tab-0-content\"]/div/div[@class='section-wrap line section']/div[@class='left-section-wrap size1of2 unit']/div/div[@clas='pricing']/div/span[@class='selling-price omniture-field']";
//    private static final String XPATH_PRICE3 = "//ul[@class='tab-header-wrap']/li[@id='tab-0']/a/span[@class='selling-price']";
    private static final String XPATH_IMAGE = "//div[@class='imgWrapper']/img[1]";
    private static final String XPATH_STATUS = "//div[@class='out-of-stock-status']";
    private static final String XPATH_STATUS_COMING = "//div[@class='coming-soon-status']";
    private static final String XPATH_SOURCEID = "//div[@id='reco-module-wrapper']";
    private static final String XPATH_SKUSNODE = "//div[@class='multiSelectionWrapper section line']";


    @Override
    public OriFetchedProduct getSummaryProductByUrl(String url) throws HttpFetchException, ContentParseException {

        OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();

        if (url != null && url.contains("dl.flipkart.com/dl/")) {
            url = FlipkartHelper.getUrlByDeeplink(url);
        }

        String pageHtml = HtmlUtils.getUrlHtml(url);

        TagNode root = new HtmlCleaner().clean(pageHtml);


        String sourceId = FlipkartHelper.getProductIdByUrl(url);
        if (sourceId == null) {
            TagNode sourceIdNode = getSubNodeByXPath(root, XPATH_SOURCEID, new ContentParseException("sourceId not found"));
            sourceId = sourceIdNode.getAttributeByName("data-pid");
            if (!url.contains("?pid=")) {
                url += "?pid=" + sourceId;
            }
        }

        TagNode titleNode = getSubNodeByXPath(root, XPATH_TITLE, new ContentParseException("title not found"));
        String title = titleNode.getText().toString().trim();

        String subTitle = "";
        TagNode subTitleNode = getSubNodeByXPath(root, XPATH_SUBTITLE, null);
        if (subTitleNode != null) {
            subTitle = subTitleNode.getText().toString();
        }

        TagNode priceNode = null;
        float price = 0.0f;

        try {
            priceNode = getSubNodeByXPath(root, XPATH_PRICE1, new ContentParseException("price not found by XPATH_PRICE1"));
        } catch (ContentParseException e1) {
            List<TagNode> priceNodes = getSubNodesByXPath(root, XPATH_PRICE1, new ContentParseException("price not found by XPATH_PRICE1"));
            if (priceNodes.size() != 0) {
                priceNode = priceNodes.get(priceNodes.size() - 1);
            }
        }
        if (priceNode != null) {
            String priceString = StringUtils.filterAndTrim(priceNode.getText().toString(), Arrays.asList("Rs.", " ", ","));
            price = Float.parseFloat(priceString);
        } else {
            price = 0.0f;
        }


        TagNode imageNode = getSubNodeByXPath(root, XPATH_IMAGE, new ContentParseException("image not found"));
        String imageUrl = imageNode.getAttributeByName("data-src");

        TagNode comingStatusNode = getSubNodeByXPath(root, XPATH_STATUS_COMING, null);
        if (comingStatusNode != null) {
            oriFetchedProduct.setProductStatus(ProductStatus.OUTSTOCK);
            oriFetchedProduct.setWebsite(Website.FLIPKART);
            oriFetchedProduct.setTitle(title);
            oriFetchedProduct.setImageUrl(imageUrl);
            oriFetchedProduct.setUrl(url);
            oriFetchedProduct.setPrice(price);
            oriFetchedProduct.setSourceSid(sourceId);
            return oriFetchedProduct;
        }

        TagNode statusNode = getSubNodeByXPath(root, XPATH_STATUS, null);
        if (statusNode != null) {
            oriFetchedProduct.setProductStatus(ProductStatus.OUTSTOCK);
        } else {
            oriFetchedProduct.setProductStatus(ProductStatus.ONSALE);
        }

        oriFetchedProduct.setImageUrl(imageUrl);
        oriFetchedProduct.setPrice(price);
        oriFetchedProduct.setTitle(title);
        oriFetchedProduct.setUrl(FlipkartHelper.getCleanUrl(url));
        oriFetchedProduct.setWebsite(Website.FLIPKART);
        oriFetchedProduct.setSourceSid(sourceId);
        oriFetchedProduct.setSourcePid(FlipkartHelper.getProductIdByUrl(url));
        oriFetchedProduct.setSubTitle(subTitle);
//        summaryProduct.setPageHtml(pageHtml);

        return oriFetchedProduct;
    }

    /**
     * 暂时支持最多俩个sku属性的商品
     * 该方法用来解析多skuUrl的商品详情，有bug，如要使用，需要修改
     *
     * @return
     */
    @Deprecated
    public List<OriFetchedProduct> getSkuSummaryProductByUrl(String url) throws HttpFetchException, ContentParseException {

        List<OriFetchedProduct> oriFetchedProductList = new ArrayList<OriFetchedProduct>();

        //获取sku属性的集合，判断是一个sku属性还是俩个还是没有sku属性
        TagNode root = HtmlUtils.getUrlRootTagNode(url);
        TagNode skusNode = getSubNodeByXPath(root, XPATH_SKUSNODE, null);

        //如果为null说明没有skuNode，使用summaryProduct解析
        if (skusNode == null) {
            OriFetchedProduct oriFetchedProduct = getSummaryProductByUrl(url);
            oriFetchedProductList.add(oriFetchedProduct);
            return oriFetchedProductList;
        } else {//不为null，判断个数

            List<TagNode> skuListNode = getSubNodesByXPath(skusNode, "/div", null);
            //skuListNode-sku的类型节点
            if (skuListNode != null && skuListNode.size() > 0) {
                //移除第一个hidden，sku属性介绍的div
                skuListNode.remove(0);
                //首先解析默认选中的sku节点对应的商品
                TagNode firstNode = skuListNode.get(0);

                if (skuListNode.size() > 1) {//如果size大于1，说明有多个sku属性，遍历

                    //首先解析当前sku的信息
                    OriFetchedProduct oriFetchedProduct = getSummaryProductByUrl(url);
                    oriFetchedProductList.add(oriFetchedProduct);
                    //当前第一级sku，匹配所有二级属性
                    TagNode anotherSkuNodes = skuListNode.get(1);
                    List<TagNode> anotherSkuNodeList = getSubNodesByXPath(anotherSkuNodes, "/div[@class='multiSelectionWidget-selectors-wrap']/a", new ContentParseException("anotherSkuNodeList fetch fail"));
                    if (anotherSkuNodeList != null && anotherSkuNodeList.size() > 0) {
                        for (TagNode anotherSkuNode : anotherSkuNodeList) {
                            String skuUrl = anotherSkuNode.getAttributeByName("href");
                            skuUrl = WEBSITE_URL + skuUrl;
                            OriFetchedProduct skuOriFetchedProduct = getSummaryProductByUrl(skuUrl);
                            oriFetchedProductList.add(skuOriFetchedProduct);
                        }
                    }

                    //获取第一级sku其他类型的数据
                    List<TagNode> otherSameSkuList = getSubNodesByXPath(firstNode, "/div[@class='multiSelectionWidget-selectors-wrap']/a", new ContentParseException("firstNode otherSameSku fetch fail"));
                    if (otherSameSkuList != null && otherSameSkuList.size() > 0) {
                        for (TagNode otherSameSku : otherSameSkuList) {
                            String otherSameSkuUrl = otherSameSku.getAttributeByName("href");
                            otherSameSkuUrl = WEBSITE_URL + otherSameSkuUrl;

                            OriFetchedProduct otherSameSkuOriFetchedProduct = getSummaryProductByUrl(otherSameSkuUrl);
                            oriFetchedProductList.add(otherSameSkuOriFetchedProduct);

                            TagNode otherSameSkuRootNode = HtmlUtils.getUrlRootTagNode(otherSameSkuUrl);

                            //该xpath直接定位到第二种sku，获取所有的第二级sku和第一级的进行匹配
                            List<TagNode> anotherSkuList = getSubNodesByXPath(otherSameSkuRootNode, "//div[@class='multiSelectionWrapper section line']/div[3]/div[@class='multiSelectionWidget-selectors-wrap']/a", new ContentParseException("anotherSkuList fetch fail"));
                            if (anotherSkuList != null && anotherSkuList.size() > 0) {
                                for (TagNode anotherSkuNode : anotherSkuList) {
                                    String anotherSkuUrl = anotherSkuNode.getAttributeByName("href");
                                    anotherSkuUrl = WEBSITE_URL + anotherSkuUrl;
                                    OriFetchedProduct anotherSkuOriFetchedProduct = getSummaryProductByUrl(anotherSkuUrl);
                                    oriFetchedProductList.add(anotherSkuOriFetchedProduct);
                                }
                            }
                        }
                    }

                } else {//如果只有一个sku属性，获取其他同类sku属性，解析

                    //首先解析当前sku的信息
                    OriFetchedProduct oriFetchedProduct = getSummaryProductByUrl(url);
                    oriFetchedProductList.add(oriFetchedProduct);
                    //获取第一个sku的全部同类属性
                    List<TagNode> otherSameSkuNodeList = getSubNodesByXPath(firstNode, "/div[@class='multiSelectionWidget-selectors-wrap']/a", null);
                    if (otherSameSkuNodeList != null && otherSameSkuNodeList.size() > 0) {
                        for (TagNode otherSameSkuNode : otherSameSkuNodeList) {
                            String skuUrl = otherSameSkuNode.getAttributeByName("href");
                            skuUrl = WEBSITE_URL + skuUrl;
                            OriFetchedProduct skuOriFetchedProduct = getSummaryProductByUrl(skuUrl);
                            oriFetchedProductList.add(skuOriFetchedProduct);
                        }
                    }

                }
            }
        }

        return oriFetchedProductList;
    }
}
