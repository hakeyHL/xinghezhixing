package hasoffer.fetch.sites.flipkart;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.IListProcessor;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.model.ListJob;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.PageModel;
import hasoffer.fetch.model.ProductStatus;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Date : 2016/2/23
 * Function :
 */
public class FlipkartListProcessor implements IListProcessor {

    private static final String XPATH_NOPRODUCT = "//div[@class='noResults fk-text-center tmargin20 bmargin20 fk-uppercase omniture-field']";
    private static final String XPATH_PRODUCT_QUERTRESULT = "//div[@id='searchCount']/span[@class='items']";
    private static final String XPATH_PRODUCT_SECTION = "//div[@class='old-grid']";
    private static final String WEBSITE_PREFIX = "http://www.flipkart.com";

    private Logger logger = LoggerFactory.getLogger(FlipkartListProcessor.class);


    @Override
    public void setExistingProductIds(Set existingProductIds) {

    }

    @Override
    public void extractProductJobs(ListJob job) {

    }

    @Override
    public PageModel getPageModel(String pageUrl) {
        return null;
    }

    /**
     * 将ajax请求的结果，默认20条，分为5组，每组4条，flipkart ajaxUrl访问返回结果
     *
     * @param ajaxUrl
     * @return
     * @throws HttpFetchException
     * @throws ContentParseException ajaxUrl ex:http://www.flipkart.com/lc/pr/pv1/spotList1/spot1/productList?sid=tyy%2Cjgu%2Cl7d%2Cblf&start=0
     */
    @Override
    public List<ListProduct> getProductByAjaxUrl(String ajaxUrl, Long cateId) throws HttpFetchException, ContentParseException {

        final String XPATH_PRODUCTS = "//div[@class='gd-row browse-grid-row']";

        List<ListProduct> products = new ArrayList<ListProduct>();

        TagNode root = HtmlUtils.getUrlRootTagNode(ajaxUrl);

        List<TagNode> productsNodes = getSubNodesByXPath(root, XPATH_PRODUCTS, new ContentParseException("product section not found"));

        for (TagNode productsNode : productsNodes) {//size=5

            List<ListProduct> flipkartProducts = getProductsByProductsNode(productsNode, cateId);

            products.addAll(flipkartProducts);

        }


        return products;
    }

    @Override
    public List<ListProduct> getProductSetByKeyword(String keyword, int resultCount) throws HttpFetchException, ContentParseException {

        String queryString = WebsiteHelper.getSearchUrl(Website.FLIPKART, keyword);

        TagNode root = HtmlUtils.getUrlRootTagNode(queryString);

        //没有匹配结果
        TagNode noProductNode = getSubNodeByXPath(root, XPATH_NOPRODUCT, null);
        if (noProductNode != null) {
            return new ArrayList<ListProduct>();
        }

        TagNode productNumNode = getSubNodeByXPath(root, XPATH_PRODUCT_QUERTRESULT, null);

        String productNumString = productNumNode.getText().toString();

        productNumString = StringUtils.filterAndTrim(productNumString, Arrays.asList(","));

        int result = Integer.parseInt(productNumString);

        if (resultCount > result) {
            resultCount = result;
        }

        List<TagNode> productNodeList = getSubNodesByXPath(root, "//div[@data-ctrl='ProductUnitController']", new ContentParseException("productList not found for [FLIPKART] keyword [" + keyword + "]"));
        int maxRestult = productNodeList.size();

        if (resultCount > maxRestult) {
            resultCount = maxRestult;
        }

        final String XPATH_PRODUCT_IMAGEURL = "/div[@class='pu-visual-section']/a[1]";
        final String XPATH_PRODUCT_TITLE = "/div[@class='pu-details lastUnit']/div[@class='pu-title fk-font-12']/a";
        final String XPATH_PRODUCT_TITLE1 = "/div[@class='pu-details lastUnit']/div[@class='pu-title fk-font-13']/a";
        final String XPATH_PRODUCT_PRICE = "/div[@class='pu-details lastUnit']/div[@class='pu-price']/div/div[@class='pu-final']/span";
        final String XPATH_PRODUCT_PRICE1 = "/div[@class='pu-details lastUnit']/div[@class='pu-price']/div[@class='pu-final font-dark-color fk-inline-block']/span";

        List<ListProduct> thdProductModelList = new ArrayList<ListProduct>();
        for (int i = 0; i < resultCount; i++) {

            TagNode prodcutDetailNode = productNodeList.get(i);

            String title = "";
            String imageUrl = "";
            String sourceId = "";
            float price = 0.0f;
            String url = "";

            sourceId = prodcutDetailNode.getAttributeByName("data-pid");

            TagNode urlAndImageNode = getSubNodeByXPath(prodcutDetailNode, XPATH_PRODUCT_IMAGEURL, new ContentParseException("url and image not found for [FLIPKART] keyword [" + keyword + "]"));
            String hrefString = urlAndImageNode.getAttributeByName("href");
            String[] subStr = hrefString.split("\\?");
            url = WEBSITE_PREFIX + subStr[0] + "?pid=" + sourceId;

            TagNode imageNode = getSubNodeByXPath(urlAndImageNode, "/img", new ContentParseException("imageUrl not found for [FLIPKART] keyword [" + keyword + "]"));
            imageUrl = imageNode.getAttributeByName("data-src");

            TagNode titleNode = getSubNodeByXPath(prodcutDetailNode, XPATH_PRODUCT_TITLE, null);
            if (titleNode == null) {
                titleNode = getSubNodeByXPath(prodcutDetailNode, XPATH_PRODUCT_TITLE1, new ContentParseException("title not found"));
            }
            title = titleNode.getAttributeByName("title").trim();


            TagNode priceNode = getSubNodeByXPath(prodcutDetailNode, XPATH_PRODUCT_PRICE, null);
            if (priceNode == null) {
                priceNode = getSubNodeByXPath(prodcutDetailNode, XPATH_PRODUCT_PRICE1, new ContentParseException("price not found"));
            }
            String priceString = StringUtils.filterAndTrim(priceNode.getText().toString(), Arrays.asList("Rs.", " ", ","));
            price = Float.parseFloat(priceString);

            ListProduct thdProductModel = new ListProduct();
            thdProductModel.setTitle(title);
            thdProductModel.setWebsite(Website.FLIPKART);
            thdProductModel.setImageUrl(imageUrl);
            thdProductModel.setSourceId(sourceId);
            thdProductModel.setPrice(price);
            thdProductModel.setUrl(url);
            thdProductModel.setStatus(ProductStatus.ONSALE);

            thdProductModelList.add(thdProductModel);
        }

        return thdProductModelList;
    }

    /**
     * 根据每组，获取每个product节点
     *
     * @param productsNode
     * @return
     * @throws ContentParseException
     */
    private List<ListProduct> getProductsByProductsNode(TagNode productsNode, Long cateId) throws ContentParseException {

        final String XPATH_PRODUCT = "//div[@class='gd-col gu3']";

        List<ListProduct> flipkartProducts = new ArrayList<ListProduct>();

        List<TagNode> productNodes = getSubNodesByXPath(productsNode, XPATH_PRODUCT, new ContentParseException("product field not found"));

        for (TagNode productNode : productNodes) {
            ListProduct product = getProductByProductNode(productNode, cateId);
            flipkartProducts.add(product);
        }

        return flipkartProducts;

    }

    /**
     * 根据product节点获取商品信息
     *
     * @param productNode
     * @return
     * @throws ContentParseException
     */
    private ListProduct getProductByProductNode(TagNode productNode, Long cateId) throws ContentParseException {

        final String XPATH_SOURCEID = "/div";
        final String XPATH_IMAGEURL = "/div[@class='pu-visual-section']/a/img";
        final String XPATH_TITLEANDPRICE = "/div[@class='pu-details lastUnit']";
        final String XPATH_TITLE = "/div[@class='pu-title fk-font-13']/a";
        final String XPATH_PRICE = "/div[@class='pu-price']/div/div[@class='pu-final']/span";

        TagNode sourceIdNode = getSubNodeByXPath(productNode, XPATH_SOURCEID, new ContentParseException("sourceId not found"));
//        String sourceId = sourceIdNode.getAttributeByName("data-pid");

        TagNode imgNode = getSubNodeByXPath(sourceIdNode, XPATH_IMAGEURL, new ContentParseException("image node not found"));
        String imageUrl = imgNode.getAttributeByName("data-src");

        String url = imgNode.getParent().getAttributeByName("href");
        String[] subStrs1 = url.split("\\?pid");
        url = "http://www.flipkart.com" + subStrs1[0];

        String[] subStrs2 = url.split("/");
        String sourceId = subStrs2[subStrs2.length - 1];
        sourceId = sourceId.substring(sourceId.indexOf('m') + 1);

        TagNode titleAndPriceNode = getSubNodeByXPath(sourceIdNode, XPATH_TITLEANDPRICE, new ContentParseException("titleAndPriceNode not found"));

        TagNode titleNode = getSubNodeByXPath(titleAndPriceNode, XPATH_TITLE, new ContentParseException("title not found"));
        String title = titleNode.getText().toString().trim();


        TagNode priceNode = getSubNodeByXPath(titleAndPriceNode, XPATH_PRICE, new ContentParseException("price not found"));
        String priceString = priceNode.getText().toString().replace("Rs.", "").replace(",", "").trim();
        float price = 0.0f;
        try {
            price = Float.parseFloat(priceString);
        } catch (Exception e) {
            logger.debug(sourceId + " sold out");
        }


        ListProduct product = new ListProduct();

        product.setSourceId(sourceId);
        product.setUrl(url);
        product.setImageUrl(imageUrl);
        product.setTitle(title);
        product.setPrice(price);
        product.setCategoryId(cateId);
        product.setWebsite(Website.FLIPKART);

        return product;
    }

    /**
     * 通过url获取categoryId
     *
     * @param ajaxUrl
     * @return todo 获取的id可能需要解码
     */
    private String getCategoryIdByAjaxUrl(String ajaxUrl) {

        String[] subStrs1 = ajaxUrl.split("\\?");
        String[] subStrs2 = subStrs1[1].split("&");
        String[] subStrs3 = subStrs2[0].split("=");

        String categoryId = subStrs3[1];

        return categoryId;
    }
}
