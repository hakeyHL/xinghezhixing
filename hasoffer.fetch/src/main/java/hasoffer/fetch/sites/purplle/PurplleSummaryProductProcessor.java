package hasoffer.fetch.sites.purplle;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.core.ISummaryProductProcessor;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.model.OriFetchedProduct;
import org.htmlcleaner.TagNode;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;

/**
 * Created by wing on 2016/3/1.
 */
public class PurplleSummaryProductProcessor implements ISummaryProductProcessor {

    private static final String XPATH_TITLE = "//h1[@class='fm f24 brb1 bc-e8 pdb10']";
    private static final String XPATH_PRICE = "//div[@class='price']/p";
    private static final String XPATH_PRODUCTID = "//input[@id='product_id']";
    private static final String XPATH_PRODUCT_IMAGE = "//div[@class='zoomcontainer']/div";

    @Override
    public OriFetchedProduct getSummaryProductByUrl(String url) throws HttpFetchException, ContentParseException {

        OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        TagNode titleNode = getSubNodeByXPath(root, XPATH_TITLE, new ContentParseException("title not found"));
        String title = titleNode.getText().toString().trim();

        TagNode priceNode = getSubNodeByXPath(root, XPATH_PRICE, new ContentParseException("price not found"));
        String priceString = priceNode.getText().toString().trim();
        float price = Float.parseFloat(priceString);

        TagNode sourceIdNode = getSubNodeByXPath(root, XPATH_PRODUCTID, new ContentParseException("sourceId not found"));
        String sourceId = sourceIdNode.getAttributeByName("value").toString().trim();

        TagNode imageNode = getSubNodeByXPath(root, XPATH_PRODUCT_IMAGE, new ContentParseException("image not found"));
        String imageUrl = imageNode.getAttributeByName("src");

        oriFetchedProduct.setImageUrl(imageUrl);
        oriFetchedProduct.setPrice(price);
        oriFetchedProduct.setProductStatus(ProductStatus.ONSALE);
        oriFetchedProduct.setTitle(title);
        oriFetchedProduct.setUrl(url);
        oriFetchedProduct.setWebsite(Website.PURPLLE);
        oriFetchedProduct.setSourceSid(sourceId);

        return oriFetchedProduct;
    }
}
