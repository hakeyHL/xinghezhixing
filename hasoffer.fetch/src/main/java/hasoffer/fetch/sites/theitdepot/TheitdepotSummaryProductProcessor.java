package hasoffer.fetch.sites.theitdepot;

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
import java.util.List;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Created on 2016/4/6.
 */
public class TheitdepotSummaryProductProcessor implements ISummaryProductProcessor {


    private static final String XPATH_PRODUCT_STATUS = "//div[@class='breadcrumb-inner']/ul/li[@class='active']";
    private static final String XPATH_PRODUCT_IMAGE = "//div[@id='slide1']/a/img";
    private static final String XPATH_PRODUCT_PRICE = "//div[@class='price-box']";

    @Override
    public OriFetchedProduct getSummaryProductByUrl(String url) throws HttpFetchException, ContentParseException {

        OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();

        oriFetchedProduct.setWebsite(Website.THEITDEPOT);
        oriFetchedProduct.setUrl(url);

        String sourceId = TheitdepotHelper.getSourceIdByUrl(url);
        oriFetchedProduct.setSourceSid(sourceId);

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        TagNode statusNode = getSubNodeByXPath(root, XPATH_PRODUCT_STATUS, new ContentParseException("statusNode not found for [" + url + "]"));
        List<TagNode> statusNodeList = getSubNodesByXPath(statusNode, "/a", null);
        if (statusNodeList != null) {
            oriFetchedProduct.setProductStatus(ProductStatus.OFFSALE);
            oriFetchedProduct.setTitle("url expired");
            return oriFetchedProduct;
        }

        TagNode titleNode = getSubNodeByXPath(statusNode, "/span", new ContentParseException("title not found for [" + url + "]"));
        String title = titleNode.getText().toString().trim();
        oriFetchedProduct.setTitle(title);

        TagNode imageNode = getSubNodeByXPath(root, XPATH_PRODUCT_IMAGE, new ContentParseException("image not found for [" + url + "]"));
        String imageUrl = imageNode.getAttributeByName("src");
        oriFetchedProduct.setImageUrl(imageUrl);

        float price = 0.0f;
        List<TagNode> priceNodeList = getSubNodesByXPath(root, XPATH_PRODUCT_PRICE, new ContentParseException("priceBoxNode not found for [" + url + "]"));
        if (priceNodeList != null && priceNodeList.size() > 0) {
            TagNode priceBoxNode = priceNodeList.get(0);
            TagNode priceNode = getSubNodeByXPath(priceBoxNode, "/span", new ContentParseException("priceNode not found for [" + url + "]"));
            String priceString = priceNode.getText().toString();
            priceString = StringUtils.filterAndTrim(priceString, Arrays.asList("Rs.", "/", "-"));
            price = Float.parseFloat(priceString);
        }
        oriFetchedProduct.setPrice(price);

        oriFetchedProduct.setProductStatus(ProductStatus.ONSALE);

        return oriFetchedProduct;
    }
}
