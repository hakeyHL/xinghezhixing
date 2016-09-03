package hasoffer.fetch.sites.homeshop18;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.ISummaryProductProcessor;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.model.OriFetchedProduct;
import org.htmlcleaner.TagNode;

import java.util.Arrays;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;

/**
 * Created on 2016/4/6.
 */
public class Homeshop18SummaryProductProdcessor implements ISummaryProductProcessor {

    public static final String XPATH_PRODUCT_STATUS = "//strong[@class='out_stock']";
    public static final String XPATH_PRODUCT_TITLE = "//span[@id='productTitleInPDP']";
    public static final String XPATH_PRODUCT_IMAGE = "//img[@id='pbilimage1tag']";
    public static final String XPATH_PRODUCT_PRICE = "//span[@id='hs18Price']";

    @Override
    public OriFetchedProduct getSummaryProductByUrl(String url) throws HttpFetchException, ContentParseException {

        OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();

        oriFetchedProduct.setUrl(url);
        oriFetchedProduct.setWebsite(Website.HOMESHOP18);

        String sourceId = Homeshop18Helper.getProductIdByUrl(url);
        oriFetchedProduct.setSourceSid(sourceId);

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        TagNode statusNode = getSubNodeByXPath(root, XPATH_PRODUCT_STATUS, null);
        if (statusNode != null) {
            String titleString = statusNode.getParent().getText().toString();
            String[] subStrs = titleString.split("is");
            String title = subStrs[0].trim();
            oriFetchedProduct.setTitle(title);
            oriFetchedProduct.setProductStatus(ProductStatus.OUTSTOCK);
            return oriFetchedProduct;
        }

        TagNode titleNode = getSubNodeByXPath(root, XPATH_PRODUCT_TITLE, new ContentParseException("[title] not found for [" + url + "]"));
        String title = titleNode.getText().toString().trim();
        oriFetchedProduct.setTitle(title);

        TagNode imageNode = getSubNodeByXPath(root, XPATH_PRODUCT_IMAGE, new ContentParseException("[image] not found for [" + url + "]"));
        String imageUrl = imageNode.getAttributeByName("src");
        oriFetchedProduct.setImageUrl("http:"+imageUrl);

        TagNode priceNode = getSubNodeByXPath(root, XPATH_PRODUCT_PRICE, new ContentParseException("price not found for [" + url + "]"));
        String priceString = StringUtils.filterAndTrim(priceNode.getText().toString(), Arrays.asList("Rs.", "&nbsp;"));
        float price = Float.parseFloat(priceString);
        oriFetchedProduct.setPrice(price);

        oriFetchedProduct.setProductStatus(ProductStatus.ONSALE);
        return oriFetchedProduct;
    }
}
