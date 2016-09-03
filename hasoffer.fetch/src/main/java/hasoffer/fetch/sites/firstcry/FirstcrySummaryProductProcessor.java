package hasoffer.fetch.sites.firstcry;

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
 * Created on 2016/3/1.
 */
public class FirstcrySummaryProductProcessor implements ISummaryProductProcessor {

    private static final String XPATH_TITLE = "//h1[@itemprop='name']";
    private static final String XPATH_PRICE = "//span[@itemprop='price']";
    private static final String XPATH_IMAGE = "//div[@id='inZoom']/img";
    private static final String XPATH_IMAGE1 = "//div[@class='pr_1']/img";

    @Override
    public OriFetchedProduct getSummaryProductByUrl(String url) throws HttpFetchException, ContentParseException {

        OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();
        String sourceId = FirstcryHelper.getProductIdByUrl(url);

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        TagNode titleNode = getSubNodeByXPath(root, XPATH_TITLE, new ContentParseException("title not found"));
        String title = titleNode.getText().toString().trim();

        TagNode priceNode = getSubNodeByXPath(root, XPATH_PRICE, new ContentParseException("price not found"));
        float price = Float.parseFloat(priceNode.getText().toString());

        TagNode imageNode = getSubNodeByXPath(root, XPATH_IMAGE, new ContentParseException("image not found"));
        if (imageNode == null) {
            imageNode = getSubNodeByXPath(root, XPATH_IMAGE1, new ContentParseException("image not found"));
        }
        String imageUrl = imageNode.getAttributeByName("src");

        oriFetchedProduct.setImageUrl(imageUrl);
        oriFetchedProduct.setPrice(price);
        oriFetchedProduct.setProductStatus(ProductStatus.ONSALE);
        oriFetchedProduct.setTitle(title);
        oriFetchedProduct.setUrl(url);
        oriFetchedProduct.setWebsite(Website.FIRSTCRY);
        oriFetchedProduct.setSourceSid(sourceId);

        return oriFetchedProduct;
    }
}
