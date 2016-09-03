package hasoffer.fetch.sites.shopclues;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.IListProcessor;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.model.*;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Created on 2016/3/3.
 */
public class ShopcluesListProcessor implements IListProcessor {

    private static final String XPATH_PRODUCT_QUERTRESULT = "//div[@class='products_list']/div/div[2]";
    private static final String XPATH_PRODUCT_SECTION = "//div[@class='products_list']";
    private static final Pattern TITLE_FROM_URL_PATTERN = Pattern.compile("http://www\\.shopclues\\.com/([0-9a-z-]+).html$]");
    private static Logger logger = LoggerFactory.getLogger(ShopcluesListProcessor.class);

    @Override
    public void setExistingProductIds(Set existingProductIds) {

    }

    @Override
    public void extractProductJobs(ListJob job) {

    }

    @Override
    public PageModel getPageModel(String pageUrl) {
        return null;
    }

    @Override
    public List<ListProduct> getProductByAjaxUrl(String ajaxUrl, Long cateId) throws HttpFetchException, ContentParseException {

        final String XPATH_PRODUCTSECTION = "//div[@class='products-grid']";
        final String XPATH_PRODUCTNODE = "/div";

        List<ListProduct> shopCluesProducts = new ArrayList<ListProduct>();

        TagNode root = HtmlUtils.getUrlRootTagNode(ajaxUrl);

        TagNode productSection = getSubNodeByXPath(root, XPATH_PRODUCTSECTION, new ContentParseException("productSection not found"));

        List<TagNode> productNodes = getSubNodesByXPath(productSection, XPATH_PRODUCTNODE, new ContentParseException("productNode not found"));

        String url = "";
        String sourceId = "";
        String imageUrl = "";
        String title = "";
        float price = 0.0f;

        for (TagNode productNode : productNodes) {
            try {
                TagNode urlAndIdNode = getSubNodeByXPath(productNode, "/a", new ContentParseException("url and sourceId not found"));
                url = urlAndIdNode.getAttributeByName("href");
                sourceId = urlAndIdNode.getAttributeByName("id");

                TagNode imageNode = getSubNodeByXPath(urlAndIdNode, "/img", new ContentParseException("imageUrl not found"));
                imageUrl = imageNode.getAttributeByName("src2");

                TagNode titleAndPriceNode = getSubNodeByXPath(productNode, "/div[@class='details']", new ContentParseException("title not found"));
                TagNode titleNode = getSubNodeByXPath(titleAndPriceNode, "/a[@class='name']", new ContentParseException("title not found"));
                title = titleNode.getAttributeByName("alt");
                TagNode priceNode = getSubNodeByXPath(titleAndPriceNode, "/div[@class='product-price']/span[@class='price']", new ContentParseException("price not found"));
                String priceString = priceNode.getText().toString().replace(",", "");
                if (priceString.contains("-")) {
                    String[] subStrs1 = priceString.split("-");
                    priceString = subStrs1[1];
                }
                price = Float.parseFloat(priceString);


                ListProduct product = new ListProduct();
                product.setPrice(price);
                product.setSourceId(sourceId);
                product.setTitle(title);
                product.setImageUrl(imageUrl);
                product.setUrl(url);
                product.setCategoryId(cateId);
                product.setWebsite(Website.SHOPCLUES);

                shopCluesProducts.add(product);
            } catch (ContentParseException e) {
                logger.debug("contentParse fail" + e.toString());
                continue;
            } catch (NumberFormatException e) {
                logger.debug("numberFormat fail" + e.toString() + ajaxUrl + title);
                continue;
            }
        }

        return shopCluesProducts;
    }

    /**
     * shuopclues页面结构中，在商品显示区，是由products_list构成，每个list中有12个元素，如果没有翻页，默认返回结果24
     * 页面获取不到sourceId
     *
     * @param keyword
     * @param resultCount
     * @return
     * @throws HttpFetchException
     * @throws ContentParseException
     */
    @Override
    public List<ListProduct> getProductSetByKeyword(String keyword, int resultCount) throws HttpFetchException, ContentParseException {

        keyword = StringUtils.filterAndTrim(keyword, Arrays.asList("(", ")", ","));

        String queryString = WebsiteHelper.getSearchUrl(Website.SHOPCLUES, StringUtils.urlEncode(keyword));

        TagNode root = HtmlUtils.getUrlRootTagNode(queryString);

        List<ListProduct> thdProductModelList = new ArrayList<ListProduct>();

        TagNode productNumNode = getSubNodeByXPath(root, XPATH_PRODUCT_QUERTRESULT, null);

        //如果没有商品数量，认为没有商品，一般在首页没跳转
        if (productNumNode == null) {
            return thdProductModelList;
        }

        String resultString = productNumNode.getText().toString();
        resultString = StringUtils.filterAndTrim(resultString, Arrays.asList("(", ")", "Products", " ", "Found"));
        int result = Integer.parseInt(resultString);

        if (result == 0) {
            return new ArrayList<ListProduct>();
        }
        if (resultCount > result) {
            resultCount = result;
        }
        if (resultCount > 24) {
            resultCount = 24;
        }

        //计算分页数据
        int pageNum = (resultCount + 11) / 12;
        boolean flag1 = true;
        boolean flag2 = true;

        List<TagNode> productNodes = getSubNodesByXPath(root, XPATH_PRODUCT_SECTION, new ContentParseException("productSection not found"));
        final String XPATH_PRODUCT = "//ul/li";
        final String XPATH_PRODUCT_IMAGEURL = "/a/img";
        final String XPATH_PRODUCT_URLANDTITLE = "/h5/a";
        final String XPATH_PRODUCT_PRICE = "/div[@class='pricing_area']/div[@class='p_price']";


        for (int i = 0; i < productNodes.size() && flag1; i++) {
            List<ListProduct> thdProductModelSetList = new ArrayList<ListProduct>();
            List<TagNode> productSetNodes = getSubNodesByXPath(productNodes.get(i), XPATH_PRODUCT, new ContentParseException("productSet not found"));
            for (int j = 0; j < productSetNodes.size() && flag2; j++) {
                String imageUrl = "";
                String title = "";
                String url = "";
                float price = 0.0f;
                String sourceId = "";
                ListProduct thdProductModel = new ListProduct();

                TagNode productNode = productSetNodes.get(j);

                String outOfStackClassString = productNode.getAttributeByName("class");

                if ("out-of-stock".equals(outOfStackClassString)) {
                    thdProductModel.setStatus(ProductStatus.OUTSTOCK);
                } else {
                    thdProductModel.setStatus(ProductStatus.ONSALE);
                }

                TagNode imageNode = getSubNodeByXPath(productNode, XPATH_PRODUCT_IMAGEURL, new ContentParseException("imageUrl not found"));
                imageUrl = imageNode.getAttributeByName("src");

                TagNode urlAndTitleNode = getSubNodeByXPath(productNode, XPATH_PRODUCT_URLANDTITLE, new ContentParseException("url not found"));
                url = urlAndTitleNode.getAttributeByName("href");

                try {
                    ShopCluesSummaryProductProcessor processor = new ShopCluesSummaryProductProcessor();
                    OriFetchedProduct product = processor.getSummaryProductByUrl(url);
                    if (product != null) {
                        title = product.getTitle();
                        sourceId = product.getSourceSid();
                    }
                } catch (Exception e) {
                    logger.debug("fetch product exception");
                    continue;
                }


                TagNode priceNode = getSubNodeByXPath(productNode, XPATH_PRODUCT_PRICE, new ContentParseException("price not found"));
                String priceString = priceNode.getText().toString().trim();
                price = Float.parseFloat(priceString);


                thdProductModel.setTitle(title);
                thdProductModel.setWebsite(Website.SHOPCLUES);
                thdProductModel.setImageUrl(imageUrl);
                thdProductModel.setPrice(price);
                thdProductModel.setUrl(url);
                thdProductModel.setSourceId(sourceId);

                thdProductModelSetList.add(thdProductModel);

                if (j == resultCount - 12 * i - 1) {
                    flag2 = false;
                }
            }
            thdProductModelList.addAll(thdProductModelSetList);

            if (i == pageNum - 1) {
                flag1 = false;
            }
        }
        return thdProductModelList;
    }
}
