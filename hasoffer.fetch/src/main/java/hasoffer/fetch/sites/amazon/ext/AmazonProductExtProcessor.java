package hasoffer.fetch.sites.amazon.ext;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.exception.ProductTitleNotFoundException;
import hasoffer.fetch.sites.amazon.AmazonHelper;
import hasoffer.fetch.sites.amazon.ext.model.AmazonProduct;
import hasoffer.fetch.sites.amazon.ext.model.AmazonProductReview;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static hasoffer.base.utils.http.XPathUtils.*;

/**
 * Created by chevy on 2015/11/12.
 */
public class AmazonProductExtProcessor {

    private static final String XPATH_PRODUCT_NAME = "//*[@id='title']";
    private static final String XPATH_PRICE_CURRENT = "//*[@id='priceblock_ourprice']";
    private static final String XPATH_MERCHANT_INFO = "//div[@id='merchant-info']";
    private static final String XPATH_OTHER_SELLERS = "//span[@class='olp-padding-right']";
    private static final String XPATH_PRODUCT_SELLER_RANK = "//*[@id='SalesRank']";
    private static final String XPATH_REVIEW = "//";

    private static Logger logger = LoggerFactory.getLogger(AmazonProductExtProcessor.class);

    public AmazonProduct parse(String url) throws HttpFetchException, ContentParseException {
        AmazonProduct amazonProduct = new AmazonProduct();

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        String title = getSubNodeStringByXPath(root, XPATH_PRODUCT_NAME, new ProductTitleNotFoundException(url));

        // 通过url获取
        String ASIN = AmazonHelper.getProductIdByUrl(url);

        String priceStr = AmazonHelper.getCurrentPriceString(root);
        float price = Float.valueOf(priceStr);

        String merchantInfo = getSubNodeStringByXPath(root, XPATH_MERCHANT_INFO, null);
        merchantInfo = StringUtils.filterAndTrim(merchantInfo, Arrays.asList("\r", "\n", "\\s\\s"));

        List<String> others = getSubNodesStringsByXPath(root, XPATH_OTHER_SELLERS, null);

        String sellerRank = getSellerRank(root);

        amazonProduct.setUrl(url);
        amazonProduct.setTitle(title);
        amazonProduct.setASIN(ASIN);
        amazonProduct.setHasGift(false);

        amazonProduct.setPrice(price);
        /*amazonProduct.setShipping(shipping);

		amazonProduct.setFulfilledBy(amazon);
		amazonProduct.setSoldBy(amazon);
		amazonProduct.setOtherSellers(other);*/

        amazonProduct.setProductReview(getProductReview(root));
        amazonProduct.setSellersRank(sellerRank);
        return amazonProduct;
    }

    private AmazonProductReview getProductReview(TagNode root) {

        return null;
    }

    private String getSellerRank(TagNode root) throws ContentParseException {
        TagNode sellerRank = getSubNodeByXPath(root, XPATH_PRODUCT_SELLER_RANK, null);

        if (sellerRank == null) {
            return "";
        }

        String text = sellerRank.getText().toString();

        text = StringUtils.filterAndTrim(StringUtils.unescapeHtml(text), Arrays.asList("\r", "\n", "\t"));

        return text;
    }
}
