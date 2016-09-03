package hasoffer.fetch.sites.cromaretail;

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
 * Created on 2016/3/1.
 */
public class CromaretailSummaryProductProcessor implements ISummaryProductProcessor {

    private static final String WEBSITEURL = "http://www.cromaretail.com/";

    private static final String XPATH_TITLE = "//h1[@class='mobileHide']";
    private static final String XPATH_PRICE = "//section[@class='product']/div[@class='pDesc']/div[@class='cta']/h2";
    private static final String XPATH_PRODUCT_ID = "//input[@id='hfProductID']";
    private static final String XPATH_PRODUCT_IMAGE = "//div[@class='pImage']/div[@class='productImage']";
    private static final String XPATH_PRODUCT_STATUS = "//div[@id='outof-stock']";

    @Override
    public OriFetchedProduct
    getSummaryProductByUrl(String url) throws HttpFetchException, ContentParseException {

        OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        TagNode sourceIdNode = getSubNodeByXPath(root, XPATH_PRODUCT_ID, new ContentParseException("sourceId not found"));
        String sourceId = sourceIdNode.getAttributeByName("value").toString().trim();

        TagNode titleNode = getSubNodeByXPath(root, XPATH_TITLE, new ContentParseException("titlt not found"));
        String title = titleNode.getText().toString().trim();

        float price = 0.0f;
        TagNode statusNode = getSubNodeByXPath(root, XPATH_PRODUCT_STATUS, null);
        if (statusNode != null) {
            oriFetchedProduct.setProductStatus(ProductStatus.OFFSALE);
        } else {
            TagNode priceNode = getSubNodeByXPath(root, XPATH_PRICE, new ContentParseException("price not found"));
            String priceString = priceNode.getText().toString().trim();
            String[] subStrs1 = priceString.split("Rs.");
            price = Float.parseFloat(subStrs1[1].trim().replace(",", ""));
        }


        TagNode imageNode = getSubNodeByXPath(root, XPATH_PRODUCT_IMAGE, new ContentParseException("image not found"));
        String imageUrl = imageNode.getAttributeByName("src");
        if (imageUrl == null) {
            List<TagNode> imageNodeList = getSubNodesByXPath(imageNode, "/img", new ContentParseException("image not found"));
            if (imageNodeList.size() > 0) {
                imageNode = imageNodeList.get(0);
                imageUrl = WEBSITEURL + imageNode.getAttributeByName("src");
            }
        }

        oriFetchedProduct.setImageUrl(imageUrl);
        oriFetchedProduct.setPrice(price);
        oriFetchedProduct.setProductStatus(ProductStatus.ONSALE);
        oriFetchedProduct.setTitle(title);
        oriFetchedProduct.setUrl(url);
        oriFetchedProduct.setWebsite(Website.CROMARETAIL);
        oriFetchedProduct.setSourceSid(sourceId);

        return oriFetchedProduct;
    }
}
