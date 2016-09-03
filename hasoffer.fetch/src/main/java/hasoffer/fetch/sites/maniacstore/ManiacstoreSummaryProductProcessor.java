package hasoffer.fetch.sites.maniacstore;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.core.ISummaryProductProcessor;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.model.OriFetchedProduct;
import org.htmlcleaner.TagNode;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Created on 2016/3/1.
 */
public class ManiacstoreSummaryProductProcessor implements ISummaryProductProcessor {

    private static final String XPATH_TITLE = "//div[@class='details']/h1";
    private static final String XPATH_PRICE = "//*[@id='product_price']";
    private static final String XPATH_PRODUCTID = "//input[@name='productid']";
    private static final String XPATH_PRODUCT_IMAGE = "//img[@id='product_thumbnail']";

    @Override
    public OriFetchedProduct getSummaryProductByUrl(String url) throws HttpFetchException, ContentParseException {

        OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        TagNode titleNode = getSubNodeByXPath(root, XPATH_TITLE, new ContentParseException("title not found"));
        String title = titleNode.getText().toString().trim();

        TagNode priceNode = getSubNodeByXPath(root, XPATH_PRICE, new ContentParseException("price not found"));
        String priceString = priceNode.getText().toString().replace(",", "").trim();
        float price = Float.parseFloat(priceString);

        TagNode sourceIdNode = getSubNodesByXPath(root, XPATH_PRODUCTID, new ContentParseException("productid not found")).get(1);
        String sourceId = sourceIdNode.getAttributeByName("value");

        TagNode imageNode = getSubNodeByXPath(root, XPATH_PRODUCT_IMAGE, new ContentParseException("image not found"));
        String imageUrl = imageNode.getAttributeByName("src");

        oriFetchedProduct.setImageUrl(imageUrl);
        oriFetchedProduct.setSourceSid(sourceId);
        oriFetchedProduct.setPrice(price);
        oriFetchedProduct.setProductStatus(ProductStatus.ONSALE);
        oriFetchedProduct.setTitle(title);
        oriFetchedProduct.setUrl(url);
        oriFetchedProduct.setWebsite(Website.MANIACSTORE);

        return oriFetchedProduct;
    }
}

