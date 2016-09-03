package hasoffer.fetch.sites.askmebazaar;

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
public class AskmebazaarSummaryProductProcessor implements ISummaryProductProcessor {

    private static final String XPATH_TITLE = "//h1[@class='productname']";
    private static final String XPATH_PRICE = "//span[@itemprop='offers']";
    private static final String XPATH_IMAGE = "//div[@id='slider_pdp']/ul/li/a/img";
    private static final String XPATH_IMAGE1 = "//div[@id='slider_pdp']/div/ul[@class='slides']/li[@class='flex-active-slide']/a/img";
    private static final String XPATH_IMAGE2 = "//div[@id='slider_pdp']/ul/li";


    @Override
    public OriFetchedProduct getSummaryProductByUrl(String url) throws HttpFetchException, ContentParseException {

        OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        TagNode titleNode = getSubNodeByXPath(root, XPATH_TITLE, new ContentParseException("title not found"));
        String[] subStrs1 = titleNode.getText().toString().split("Sold By");
        String title = subStrs1[0].trim();

        TagNode priceNode = getSubNodeByXPath(root, XPATH_PRICE, new ContentParseException("price not found"));
        String priceString = priceNode.getText().toString();
        String[] subStrs2 = priceString.split("&#8377;");
        float price = Float.parseFloat(subStrs2[1].trim().replace(",", ""));

        String imageUrl = null;
        TagNode imageNode = getSubNodeByXPath(root, XPATH_IMAGE, null);
        if (imageNode == null) {
            imageNode = getSubNodeByXPath(root, XPATH_IMAGE1, null);
        }
        if (imageNode == null) {
            List<TagNode> imageNodes = getSubNodesByXPath(root, XPATH_IMAGE2, new ContentParseException("image not found"));
            if (imageNodes != null && imageNodes.size() > 0) {
                imageNode =getSubNodeByXPath(imageNodes.get(0), "/a/img", new ContentParseException("image not found"));
            }
        }

        if(imageNode!=null){
            imageUrl = imageNode.getAttributeByName("src");
        }

        oriFetchedProduct.setImageUrl(imageUrl);
        oriFetchedProduct.setTitle(title);
        oriFetchedProduct.setProductStatus(ProductStatus.ONSALE);
        oriFetchedProduct.setUrl(url);
        oriFetchedProduct.setPrice(price);
        oriFetchedProduct.setWebsite(Website.ASKMEBAZAAR);
        oriFetchedProduct.setSourceSid(AskmebazaarHelper.getProductIdByUrl(url));

        return oriFetchedProduct;
    }


}
