package hasoffer.fetch.sites.amazon;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.ISummaryProductProcessor;
import hasoffer.fetch.model.OriFetchedProduct;
import org.apache.commons.lang3.math.NumberUtils;
import org.htmlcleaner.TagNode;

import java.util.Arrays;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;

/**
 * Created by Administrator on 2016/9/2.
 */
public class UsaAmazonSummaryProductProcessor implements ISummaryProductProcessor {

    @Override
    public OriFetchedProduct getSummaryProductByUrl(String url) throws HttpFetchException, ContentParseException {

        OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        TagNode titleNode = getSubNodeByXPath(root, "//span[@id='productTitle']", new ContentParseException("title not found"));

        String title = StringUtils.filterAndTrim(titleNode.getText().toString(), null);

        String imageUrl = "";

        TagNode imageNode = getSubNodeByXPath(root, "//img[@id='landingImage']", null);

        if (imageNode != null) {
            imageUrl = imageNode.getAttributeByName("data-old-hires");
            if(StringUtils.isEmpty(imageUrl)){
                imageUrl = imageNode.getAttributeByName("data-a-dynamic-image");
            }
            if(!StringUtils.isEmpty(imageUrl)){
                imageUrl = imageUrl.substring(imageUrl.indexOf("http"), imageUrl.indexOf(".jpg") + 4);
            }
        }

        float price = 0.0f;

        TagNode priceNode = getSubNodeByXPath(root, "//span[@id='priceblock_dealprice']", null);

        if (priceNode == null) {

            priceNode = getSubNodeByXPath(root, "//span[@id='priceblock_ourprice']", new ContentParseException("price node not found"));

        }

        String priceString = StringUtils.filterAndTrim(priceNode.getText().toString(), Arrays.asList(",", "$"));
        if (NumberUtils.isNumber(priceString)) {
            price = Float.parseFloat(priceString);
        } else {
            System.out.println("priceString is " + priceString + " parse fail");
        }

        oriFetchedProduct.setTitle(title);
        oriFetchedProduct.setImageUrl(imageUrl);
        oriFetchedProduct.setPrice(price);

        return oriFetchedProduct;
    }
}
