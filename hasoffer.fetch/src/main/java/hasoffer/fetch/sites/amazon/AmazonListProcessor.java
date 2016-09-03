package hasoffer.fetch.sites.amazon;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.IListProcessor;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.model.ListJob;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.PageModel;
import org.htmlcleaner.TagNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Created on 2016/4/11.
 */
public class AmazonListProcessor implements IListProcessor {

    private static final String XPATH_NOPRODUCT = "//h1[@id='noResultsTitle']";
    private static final String XPATH_PRODUCT_QUERTRESULT = "//h2[@id='s-result-count']";

    @Override
    public void setExistingProductIds(Set<String> existingProductIds) {

    }

    @Override
    public void extractProductJobs(ListJob job) {

    }

    @Override
    public PageModel getPageModel(String pageUrl) {
        return null;
    }

    @Override
    public List getProductByAjaxUrl(String ajaxUrl, Long ptmCateId) throws HttpFetchException, ContentParseException {
        return null;
    }

    @Override
    public List<ListProduct> getProductSetByKeyword(String keyword, int resultCount) throws HttpFetchException, ContentParseException {

        String queryString = WebsiteHelper.getSearchUrl(Website.AMAZON, keyword);

        TagNode root = HtmlUtils.getUrlRootTagNode(queryString);

//        //没有匹配结果
//        TagNode noProductNode = getSubNodeByXPath(root, XPATH_NOPRODUCT, null);
//        if (noProductNode != null) {
//            return new ArrayList<ListProduct>();
//        }

        List<TagNode> productNodeList = getSubNodesByXPath(root, "//div[@class='s-item-container']", new ContentParseException("product list not found for [AMAZON] keyword [" + keyword + "]"));

        final String XPATH_PRODUCT_IMAGEURL = "/div/div/div[@class='a-fixed-left-grid-col a-col-left']/div/div/a";
        final String XPATH_PRODUCT_IMAGEURL1 = "/div[@class='a-row a-spacing-base']/div/div[@class='a-section a-spacing-none a-inline-block s-position-relative']/a/img";
        final String XPATH_PRODUCT_TITLE = "/div/div/div[@class='a-fixed-left-grid-col a-col-right']/div[@class='a-row a-spacing-small']/a";
        final String XPATH_PRODUCT_TITLE1 = "/div[@class='a-row a-spacing-mini']/div[@class='a-row a-spacing-none']/a/h2";
        final String XPATH_PRODUCT_PRICE = "//span[@class='a-size-base a-color-price s-price a-text-bold']";

        List<ListProduct> thdProductModelList = new ArrayList<ListProduct>();

        resultCount = resultCount > productNodeList.size() ? productNodeList.size() : resultCount;

        for (int i = 0; i < resultCount; i++) {

            TagNode prodcutDetailNode = productNodeList.get(i);

            String title = "";
            String imageUrl = "";
            String sourceId = "";
            float price = Float.MAX_VALUE;
            String url = "";

            sourceId = prodcutDetailNode.getParent().getAttributeByName("data-asin");

            TagNode urlAndImageNode = getSubNodeByXPath(prodcutDetailNode, XPATH_PRODUCT_IMAGEURL, null);
            if (urlAndImageNode != null) {
                String hrefString = urlAndImageNode.getAttributeByName("href");
                String[] subStr = hrefString.split("/ref");
                url = subStr[0];

                TagNode imageNode = getSubNodeByXPath(urlAndImageNode, "/img", new ContentParseException("imageUrl not found for [AMAZON] keyword [" + keyword + "]"));
                imageUrl = imageNode.getAttributeByName("src");
            } else {
                urlAndImageNode = getSubNodeByXPath(prodcutDetailNode, XPATH_PRODUCT_IMAGEURL1, new ContentParseException("imageUrl not found"));
                imageUrl = urlAndImageNode.getAttributeByName("src");
                String hrefString = urlAndImageNode.getParent().getAttributeByName("href");
                String[] subStr = hrefString.split("/ref");
                url = subStr[0];
            }


            TagNode titleNode = getSubNodeByXPath(prodcutDetailNode, XPATH_PRODUCT_TITLE, null);
            if (titleNode == null) {
                titleNode = getSubNodeByXPath(prodcutDetailNode, XPATH_PRODUCT_TITLE1, new ContentParseException("title not fpund"));
                title = (String) titleNode.getText().toString();
            } else {
                title = titleNode.getAttributeByName("title");
            }


            List<TagNode> priceNodes = getSubNodesByXPath(prodcutDetailNode, XPATH_PRODUCT_PRICE, new ContentParseException("price not found for for [AMAZON] keyword [" + keyword + "]"));
            for (TagNode node : priceNodes) {
                String priceString = node.getText().toString();
                //example:1615.00 - 1735.00
                if (priceString.contains("-")) {
                    String[] subStr3 = priceString.split("-");
                    priceString = StringUtils.filterAndTrim(subStr3[0], Arrays.asList(","));
                }
                priceString = StringUtils.filterAndTrim(priceString, Arrays.asList("&nbsp;", ","));
                float tempPrice = Float.parseFloat(priceString);
                if (tempPrice < price) {
                    price = tempPrice;
                }
            }

            ListProduct thdProductModel = new ListProduct();
            thdProductModel.setTitle(title);
            thdProductModel.setWebsite(Website.AMAZON);
            thdProductModel.setImageUrl(imageUrl);
            thdProductModel.setSourceId(sourceId);
            thdProductModel.setPrice(price);
            if (StringUtils.isEmpty(sourceId)) {
                thdProductModel.setUrl(url);
            } else {
                thdProductModel.setUrl("http://www.amazon.in/dp/" + sourceId);
            }


            thdProductModelList.add(thdProductModel);
        }

        return thdProductModelList;
    }
}
