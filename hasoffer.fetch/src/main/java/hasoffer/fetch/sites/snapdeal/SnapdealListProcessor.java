package hasoffer.fetch.sites.snapdeal;

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
import hasoffer.fetch.model.ProductStatus;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Created on 2016/2/23.
 */
public class SnapdealListProcessor implements IListProcessor {

    //snapdeal网站，根据关键字搜索商品的url，默认排序按照流行及plrty
    private static final String XPATH_PRODUCT_QUERTRESULT = "//div[@class='search-result-text']";
    private static final String XPATH_PRODUCT_QUERTRESULT_NUM = "//span[@class='search-result-num']";
    private static final String XPATH_PRODUCT_SECTION = "//div[@id='products']/section";
    private static Logger logger = LoggerFactory.getLogger(SnapdealListProcessor.class);
    private Set<String> existingProductIds;


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

    /***
     * @param ajaxUrl
     * @return
     * @throws HttpFetchException
     * @throws ContentParseException ajaxUrl ex:http://www.snapdeal.com/acors/json/product/get/search/175/0/4?sort=plrty
     *                               注意，pagesize为4，且排序为plrty
     */
    public List<ListProduct> getProductByAjaxUrl(String ajaxUrl, Long cateId) throws HttpFetchException, ContentParseException {

        final String PRODUCT_COUNT = "/body/div[@class='jsNumberFound hidden']";
        final String XPATH_PRODUCTS = "//section[@class='js-section clearfix']";

        List<ListProduct> products = new ArrayList<ListProduct>();

        TagNode root = HtmlUtils.getUrlRootTagNode(ajaxUrl);

        TagNode flagNode = getSubNodeByXPath(root, PRODUCT_COUNT, new ContentParseException("js Number not Found"));
        if (flagNode.getText().toString().equals("0")) {
            throw new ContentParseException("no more paroduct");
        }

        List<TagNode> productNodes = getSubNodesByXPath(root, XPATH_PRODUCTS, new ContentParseException("no productSection found by ajax"));

        for (TagNode tagNode : productNodes) {
            String sourceId = "";
            String imageUrl = "";
            String url = "";
            String title = "";
            float price = 0.0f;
            try {
                sourceId = tagNode.getAttributeByName("id");
                TagNode imgNode = getSubNodeByXPath(tagNode, "/div[@class='product-tuple-image']/a/img", new ContentParseException("img path not found"));
                imageUrl = imgNode.getAttributeByName("src");
                url = imgNode.getParent().getAttributeByName("href");
                //titleNode有以下俩种情况的clas
                TagNode titleNode = getSubNodeByXPath(tagNode, "/div[@class='product-tuple-description']/div/a/p", new ContentParseException("title not found"));
//                TagNode titleNode = getSubNodeByXPath(tagNode, "/div[@class='product-tuple-description']/div[@class='product-desc-rating title-section-collapse']/a/p", new ContentParseException("title not found"));

                title = titleNode.getText().toString().trim();
                //*[@id="671292696356"]/div[3]/div[2]/div[1]/span[2]
                TagNode priceNode = getSubNodeByXPath(tagNode, "/div[@class='product-tuple-description']/div[@class='productPrice']/div[@class='product-price-row']/span[@class='product-price']", new ContentParseException("price not found maybe sold out"));
                String priceString = priceNode.getText().toString();
                price = getPriceByPriceString(priceString);

                ListProduct product = new ListProduct(cateId, sourceId, url, imageUrl, title, price, Website.SNAPDEAL, ProductStatus.ONSALE);

                products.add(product);

            } catch (Exception e) {
                logger.debug(ajaxUrl + " parse fail " + e.getMessage());
            }
        }
        return products;
    }

    /**
     * snapdeal 商品详情4个一组，根据resultCount自行分页
     * 针对snapdeal网站该方法目前支持最大数量48
     *
     * @param keyword
     * @param resultCount
     * @return
     */
    @Override
    public List<ListProduct> getProductSetByKeyword(String keyword, int resultCount) throws HttpFetchException, ContentParseException {

        String queryString = WebsiteHelper.getSearchUrl(Website.SNAPDEAL, StringUtils.urlEncode(keyword));

//        queryString = "http://www.snapdeal.com/search?keyword=Adidas%20AY5111%2015%20L%20Medium%20Backpack%20(Black)&santizedKeyword=Adidas+AY5111+15+L+Medium+Backpack+Black&catId=0&categoryId=0&suggested=false&vertical=&noOfResults=48&clickSrc=go_header&lastKeyword=&prodCatId=&changeBackToAll=false&foundInAll=false&categoryIdSearched=&cityPageUrl=&url=&utmContent=&dealDetail=&sort=rlvncy";
        TagNode root = HtmlUtils.getUrlRootTagNode(queryString);

//        TagNode productNumNode = getSubNodeByXPath(root, XPATH_PRODUCT_QUERTRESULT, null);
//
//        String resultString = productNumNode.getText().toString();
//
//        String[] subStr = resultString.split("result");
//        resultString = subStr[0];
//
//        if (resultString.contains("no")) {
//            return new ArrayList<ListProduct>();
//        }

        List<TagNode> productNumNode = getSubNodesByXPath(root, "//div[@class='col-xs-6  product-tuple-listing js-tuple']", new ContentParseException("product not found"));
//        TagNode productFoundNum = getSubNodeByXPath(root, XPATH_PRODUCT_QUERTRESULT_NUM, new ContentParseException("result product count not found"));
//        String productNumString = StringUtils.filterAndTrim(productFoundNum.getText().toString(), Arrays.asList("+"));
        int result = productNumNode.size();

        if (resultCount > result) {
            resultCount = result;
        }

        if (resultCount > 48) {
            resultCount = 48;
        }

        //计算分页数据
        int pageNum = (resultCount + 3) / 4;
        boolean flag1 = true;
        boolean flag2 = true;

        List<ListProduct> thdProductModelList = new ArrayList<ListProduct>();
        List<TagNode> productNodes = getSubNodesByXPath(root, XPATH_PRODUCT_SECTION, new ContentParseException("productSection not found"));
        final String XPATH_PRODUCT = "/div";
        final String XPATH_PRODUCT_IMAGEURL = "/div[2]/a/img";
        final String XPATH_PRODUCT_URL = "/div[3]/div[1]/a";
//        final String XPATH_PRODUCT_TITLE = "/div[3]/div[1]/a/p";
        final String XPATH_PRODUCT_PRICE = "/div[3]/div[2]/div/span[@class='product-price']";


        for (int i = 0; i < productNodes.size() && flag1; i++) {
            List<ListProduct> thdProductModelSetList = new ArrayList<ListProduct>();
            List<TagNode> productSetNodes = getSubNodesByXPath(productNodes.get(i), XPATH_PRODUCT, new ContentParseException("productSet not found"));
            for (int j = 0; j < productSetNodes.size() && flag2; j++) {
                String title = "";
                String imageUrl = "";
                String sourceId = "";
                float price = 0.0f;
                String url = "";

                TagNode productNode = productSetNodes.get(j);
                sourceId = productNode.getAttributeByName("id");

                TagNode urlNode = getSubNodeByXPath(productNode, XPATH_PRODUCT_URL, new ContentParseException("url not found"));
                url = urlNode.getAttributeByName("href");

                TagNode imageNode = getSubNodeByXPath(productNode, XPATH_PRODUCT_IMAGEURL, new ContentParseException("imageUrl not found"));

                if (i == 0) {
                    imageUrl = imageNode.getAttributeByName("src");
                } else {
                    imageUrl = imageNode.getAttributeByName("data-src");
                }

                title = imageNode.getAttributeByName("title");

                TagNode priceNode = getSubNodeByXPath(productNode, XPATH_PRODUCT_PRICE, new ContentParseException("price not found"));
                String priceString = StringUtils.filterAndTrim(priceNode.getText().toString(), Arrays.asList("Rs.", " ", ",", "+"));
                price = Float.parseFloat(priceString);

                ListProduct thdProductModel = new ListProduct();
                thdProductModel.setTitle(title);
                thdProductModel.setWebsite(Website.SNAPDEAL);
                thdProductModel.setImageUrl(imageUrl);
                thdProductModel.setSourceId(sourceId);
                thdProductModel.setPrice(price);
                thdProductModel.setUrl(url);

                thdProductModelSetList.add(thdProductModel);


                if (j == resultCount - 4 * i - 1) {
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

    /**
     * 根据页面返回的价格字符串，获取价格
     *
     * @param priceString
     * @return
     */
    private float getPriceByPriceString(String priceString) {
        priceString = priceString.replace("Rs.", "");
        priceString = priceString.replace(",", "");
        return Float.parseFloat(priceString.trim());
    }

    /**
     * 根据ajaxUrl获取商品的categoryId
     *
     * @param ajaxUrl
     * @return
     */
    private Long getCategoryIdByAjaxUrl(String ajaxUrl) {

        String[] subStrs1 = ajaxUrl.split("\\?");
        String[] subStrs2 = subStrs1[0].split("/");
        String categoryId = subStrs2[subStrs2.length - 3];
        return Long.parseLong(categoryId);
    }
}
