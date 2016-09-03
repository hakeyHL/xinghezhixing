package hasoffer.fetch.sites.shopclues;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.core.IImageProcessor;
import org.htmlcleaner.TagNode;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;

/**
 * Created on 2016/7/21.
 */
public class ShopcluesImageProcessor implements IImageProcessor {

    private static final String XPATH_PRODUCT_IMAGE = "//div[@class='slide']/a[1]";

    @Override
    public String getWebsiteImageUrl(String url) throws HttpFetchException, ContentParseException {

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        TagNode imageNode = getSubNodeByXPath(root, XPATH_PRODUCT_IMAGE, new ContentParseException("image not found"));

        return imageNode.getAttributeByName("href");
    }
}
