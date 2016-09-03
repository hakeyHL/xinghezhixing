package hasoffer.fetch.sites.ebay;

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
 * Created on 2016/4/8.
 */
public class EbayListProcessor implements IListProcessor {

    private static final String XPATH_PRODUCT_QUERTRESULT = "//span[@class='listingscnt']";

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

        String queryString = WebsiteHelper.getSearchUrl(Website.EBAY, keyword);

        TagNode root = HtmlUtils.getUrlRootTagNode(queryString);

        TagNode productSectionNode = getSubNodeByXPath(root, "//ul[@id='ListViewInner']", new ContentParseException("productSectionNode not found for [EBAY] keyword [" + keyword + "]"));
        List<TagNode> productNodeList = getSubNodesByXPath(productSectionNode, "/li", new ContentParseException("productList not found for [EBAY] keyword [" + keyword + "]"));

        resultCount = resultCount > productNodeList.size() ? productNodeList.size() : resultCount;

        final String XPATH_PRODUCT_IMAGEURL = "/div[@class='pu-visual-section']/a[1]";
        final String XPATH_PRODUCT_TITLE = "/div[@class='pu-details lastUnit']/div[@class='pu-title fk-font-13']/a";
        final String XPATH_PRODUCT_PRICE = "/div[@class='pu-details lastUnit']/div[@class='pu-price']/div/div[@class='pu-final']/span";

        List<ListProduct> thdProductModelList = new ArrayList<ListProduct>();
        for (int i = 0; i < resultCount; i++) {

            TagNode prodcutDetailNode = productNodeList.get(i);


            String title = "";
            String imageUrl = "";
            String sourceId = "";
            float price = 0.0f;
            String url = "";

            sourceId = prodcutDetailNode.getAttributeByName("listingId");
            if (StringUtils.isEmpty(sourceId)) {
                continue;
            }

            TagNode urlAndImageNode = getSubNodeByXPath(prodcutDetailNode, "/h3/a", new ContentParseException("url and image not found for [EBAY] keyword [" + keyword + "]"));
            url = urlAndImageNode.getAttributeByName("href");

            TagNode imageNode = getSubNodeByXPath(prodcutDetailNode, "/div[@class='lvpic pic img left']/div/a/img", new ContentParseException("imageUrl not found for [EBAY] keyword [" + keyword + "]"));
            imageUrl = imageNode.getAttributeByName("src");

            title = urlAndImageNode.getText().toString().trim();

            TagNode priceNode = getSubNodeByXPath(prodcutDetailNode, "/ul[@class='lvprices left space-zero']/li[@class='lvprice prc']/span", new ContentParseException("price not found for [EBAY] keyword [" + keyword + "]"));
            String priceString = priceNode.getText().toString();
            if (priceString.contains("to")) {
                String[] subStr = priceString.split("to");
                priceString = subStr[0];
            }

            priceString = StringUtils.filterAndTrim(priceString, Arrays.asList("Rs.", " ", ","));
            price = Float.parseFloat(priceString);

            ListProduct thdProductModel = new ListProduct();
            thdProductModel.setTitle(title);
            thdProductModel.setWebsite(Website.EBAY);
            thdProductModel.setImageUrl(imageUrl);
            thdProductModel.setSourceId(sourceId);
            thdProductModel.setPrice(price);
            thdProductModel.setUrl(url);

            thdProductModelList.add(thdProductModel);
        }

        return thdProductModelList;
    }
}
