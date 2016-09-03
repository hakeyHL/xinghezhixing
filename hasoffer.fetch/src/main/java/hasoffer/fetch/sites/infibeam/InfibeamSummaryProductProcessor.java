package hasoffer.fetch.sites.infibeam;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.core.ISummaryProductProcessor;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.model.OriFetchedProduct;
import org.htmlcleaner.TagNode;

import java.util.List;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Created on 2016/2/29.
 */
public class InfibeamSummaryProductProcessor implements ISummaryProductProcessor {

    private static final String XPATH_NOTFOUNF_PAGE = "//span[@class='error-img']";
    private static final String XPATH_TITLE = "//div[@id='title']/h1";
    private static final String XPATH_PRICE = "//div[@itemprop='offers']/div/div[@class='row']/div['price-after-discount']/span[@class='price']";
    private static final String XPATH_BUYNOW = "//input[@value='BUY NOW']";
    private static final String XPATH_PRODUCT_ID = "/input[@id='source']";
    private static final String XPATH_PRIDUCT_IMAGE = "//div[@id='product-images']/span";
    private static final String XPATH_PRIDUCT_IMAGE1 = "//div[@id='product-images']/img";
    private static final String XPATH_PRODUCT_STATUS = "//div[@id='product_overview']/div[@class='status soldout']";

    @Override
    public OriFetchedProduct getSummaryProductByUrl(String url) throws HttpFetchException, ContentParseException {

        OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        TagNode notFoundNode = getSubNodeByXPath(root, XPATH_NOTFOUNF_PAGE, null);
        if (notFoundNode != null) {
            //todo 404商品已经不存在
            oriFetchedProduct.setWebsite(Website.INFIBEAM);
            oriFetchedProduct.setUrl(url);
            oriFetchedProduct.setProductStatus(ProductStatus.OFFSALE);
            return oriFetchedProduct;
        }

        TagNode titleNode = getSubNodeByXPath(root, XPATH_TITLE, new ContentParseException("title not found"));
        String title = titleNode.getText().toString().trim();

        List<TagNode> priceNodes = getSubNodesByXPath(root, XPATH_PRICE, new ContentParseException("price not found"));
        String priceString = priceNodes.get(0).getText().toString();
        float price = Float.parseFloat(priceString.replace(",", ""));

        String sourceId = "";
        TagNode buyNowNode = getSubNodeByXPath(root, XPATH_BUYNOW, null);
        if (buyNowNode != null) {
            TagNode sourceIdNode = getSubNodeByXPath(buyNowNode.getParent(), XPATH_PRODUCT_ID, new ContentParseException("sourceId not found"));
            sourceId = sourceIdNode.getAttributeByName("value");
        }

        TagNode imageNode = null;
        List<TagNode> imageSpanNodes = getSubNodesByXPath(root, XPATH_PRIDUCT_IMAGE, null);
        if (imageSpanNodes.size() > 0) {
            TagNode imageSpanNode = imageSpanNodes.get(0);
            List<TagNode> imageNodes = getSubNodesByXPath(imageSpanNode, "/img", null);
            if (imageNodes.size() > 0) {
                imageNode = imageNodes.get(0);
            }
        }
        if (imageNode == null) {
            List<TagNode> imageNodes = getSubNodesByXPath(root, XPATH_PRIDUCT_IMAGE1, new ContentParseException("image not found"));
            if (imageNodes.size() > 0) {
                imageNode = imageNodes.get(0);
            }
        }
        String imageUrl = imageNode.getAttributeByName("src");

        TagNode statusNode = getSubNodeByXPath(root, XPATH_PRODUCT_STATUS, null);
        if (statusNode != null) {
            oriFetchedProduct.setProductStatus(ProductStatus.OUTSTOCK);
        }else{
            oriFetchedProduct.setProductStatus(ProductStatus.ONSALE);
        }

        oriFetchedProduct.setImageUrl(imageUrl);
        oriFetchedProduct.setPrice(price);
        oriFetchedProduct.setTitle(title);
        oriFetchedProduct.setUrl(url);
        oriFetchedProduct.setWebsite(Website.INFIBEAM);
        oriFetchedProduct.setSourceSid(sourceId);

        return oriFetchedProduct;
    }
}
