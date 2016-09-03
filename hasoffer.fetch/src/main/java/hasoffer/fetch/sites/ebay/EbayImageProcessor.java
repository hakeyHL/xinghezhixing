package hasoffer.fetch.sites.ebay;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.core.IImageProcessor;
import hasoffer.fetch.sites.flipkart.FlipkartHelper;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.util.List;

import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Created on 2016/7/18.
 */
public class EbayImageProcessor implements IImageProcessor {

    private static final String XPATH_IMAGE = "//div[@id='mainImgHldr']/img";

    @Override
    public String getWebsiteImageUrl(String url) throws HttpFetchException, ContentParseException {
        if (url != null && url.contains("dl.flipkart.com/dl/")) {
            url = FlipkartHelper.getUrlByDeeplink(url);
        }

        String pageHtml = HtmlUtils.getUrlHtml(url);

        TagNode root = new HtmlCleaner().clean(pageHtml);

        TagNode imageNode = null;
        List<TagNode> imageNodes = getSubNodesByXPath(root, XPATH_IMAGE, new ContentParseException("image not found"));
        if (imageNodes.size() > 0) {
            imageNode = imageNodes.get(imageNodes.size() - 1);
        }

        return imageNode.getAttributeByName("src");
    }
}
