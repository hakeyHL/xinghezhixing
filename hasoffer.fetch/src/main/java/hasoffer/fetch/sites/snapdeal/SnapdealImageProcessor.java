package hasoffer.fetch.sites.snapdeal;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.core.IImageProcessor;
import hasoffer.fetch.sites.flipkart.FlipkartHelper;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;

/**
 * Created on 2016/7/18.
 */
public class SnapdealImageProcessor implements IImageProcessor {

    private static final String XPATH_PRODUCT_IMAGE = "//img[@itemprop='image']";

    @Override
    public String getWebsiteImageUrl(String url) throws HttpFetchException, ContentParseException {

        if (url != null && url.contains("dl.flipkart.com/dl/")) {
            url = FlipkartHelper.getUrlByDeeplink(url);
        }

        String pageHtml = HtmlUtils.getUrlHtml(url);

        TagNode root = new HtmlCleaner().clean(pageHtml);

        TagNode imageNode = getSubNodeByXPath(root, XPATH_PRODUCT_IMAGE, new ContentParseException("image not found"));

        return imageNode.getAttributeByName("src");
    }
}
