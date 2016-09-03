package hasoffer.fetch.sites.mysmartprice;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.IListProcessor;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.model.ListJob;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.PageModel;
import hasoffer.fetch.model.ProductJob;
import org.apache.commons.lang3.math.NumberUtils;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hasoffer.base.utils.http.XPathUtils.*;

/**
 * Created by chevy on 2015/12/4.
 */
public class MspListProcessor implements IListProcessor {

    private final static String XPATH_PAGE_NUM = "//span[@class='pgntn__rslt-page'][2]";
    private final static String XPATH_PAGE = "//a[@class='pgntn__item']";
    private final static String XPATH_PRODUCTS = "//div[@class='prdct-item']";
    private final static String XPATH_PRODUCTS2 = "//div[@class='prdct-item prdct-item--spcftn-5']";

    private final static Pattern PATTERN_PAGE = Pattern.compile(
            "^http.+-(\\d+)\\.html$"
    );
    private static Logger logger = LoggerFactory.getLogger(MspListProcessor.class);
    private Set<String> existingProductIds;


    @Override
    public void setExistingProductIds(Set<String> existingProductIds) {
        this.existingProductIds = existingProductIds;
    }

    @Override
    public void extractProductJobs(ListJob job) {
        String url = job.getListUrl();
        if (job.getProductJobs() == null) {
            job.setProductJobs(new LinkedHashSet<ProductJob>());
        }

        try {
            TagNode root = HtmlUtils.getUrlRootTagNode(url);

            List<TagNode> proNodes = getSubNodesByXPath(root, XPATH_PRODUCTS, null);

            if (ArrayUtils.isNullOrEmpty(proNodes)) {
                proNodes = getSubNodesByXPath(root, XPATH_PRODUCTS2, null);
                if (ArrayUtils.isNullOrEmpty(proNodes)) {
                    return;
                }
            }

            getProductJobs(job, proNodes);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void getProductJobs(ListJob job, List<TagNode> proNodes) throws ContentParseException {
        for (TagNode proNode : proNodes) {
            String productId = proNode.getAttributeByName("data-mspid");

            TagNode tNode = getSubNodeByXPath(proNode, "//a[@class='prdct-item__name']", null);//"class" -> "prdct-item__name"
            String productUrl = tNode.getAttributeByName("href");

            if (productUrl.length() > 0) {
                ProductJob productJob = new ProductJob(job.getWebsite(), productUrl);
                productJob.setSourceId(productId);

                if(existingProductIds != null){

                    if (existingProductIds.contains(productId)) {
                        logger.debug(String.format("the product[%s] exists, url:%s", productId, productUrl));
                        continue;
                    }

                    existingProductIds.add(productId);
                }

                productJob.setCategoryId(Long.valueOf(StringUtils.isEmpty(job.getCategoryId()) ? "0" : job.getCategoryId()));

                job.getProductJobs().add(productJob);
            }
        }
    }

    @Override
    public PageModel getPageModel(String pageUrl) {

        List<TagNode> pageNodes = null;
        int pageCount = 1;

        try {
            TagNode root = HtmlUtils.getUrlRootTagNode(pageUrl);

            String pageCountStr = getSubNodeStringByXPath(root, XPATH_PAGE_NUM, null);
            if (NumberUtils.isDigits(pageCountStr)) {
                pageCount = Integer.parseInt(pageCountStr);
            }

            if (pageCount == 1) {
                return new PageModel(pageUrl, pageCount);
            } else {
                pageNodes = getSubNodesByXPath(root, XPATH_PAGE, null);
                for (TagNode pageNode : pageNodes) {
                    String url = pageNode.getAttributeByName("href");
                    Matcher m = PATTERN_PAGE.matcher(url);
                    if (m.matches()) {
                        String template = url.replace(m.group(1) + ".html", "{page}.html");
                        return new PageModel(template, pageCount);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new PageModel(pageUrl, pageCount);
    }

    @Override
    public List getProductByAjaxUrl(String ajaxUrl, Long ptmCateId) throws HttpFetchException, ContentParseException {
        return null;
    }

    /**
     * resultCount不要超过50
     *
     * @param keyword
     * @param resultCount
     * @return
     * @throws HttpFetchException
     * @throws ContentParseException
     */
    @Override
    public List<ListProduct> getProductSetByKeyword(String keyword, int resultCount) throws HttpFetchException, ContentParseException {

        //http://www.mysmartprice.com/msp/search/msp_search_new.php?subcategory=undefined&s=iphone
        //http://www.mysmartprice.com/msp/search/search.php?#s=iphone

        String queryString = WebsiteHelper.getSearchUrl(Website.MYSMARTPRICE, keyword);
        queryString = queryString.replace("search/search", "search/msp_search_new");
        queryString = queryString.replace("#", "");

        TagNode root = HtmlUtils.getUrlRootTagNode(queryString);

        List<TagNode> productListNodes = getSubNodesByXPath(root, XPATH_PRODUCTS, null);

        // 如果没有匹配节点，说明没有商品
        if (productListNodes == null) {
            return new ArrayList<ListProduct>();
        }

        //如果要求结果的数量大于返回数，已返回的最大值为准
        if (resultCount > productListNodes.size()) {
            resultCount = productListNodes.size();
        }

        final String XPATH_PRODUCT_IMAGEURL = "/a[@class='prdct-item__img-wrpr']/img";
        final String XPATH_PRODUCT_DETAIL = "/div[@class='prdct-item__dtls']";


        List<ListProduct> thdProductModelList = new ArrayList<ListProduct>();

        for (int i = 0; i < resultCount; i++) {

            TagNode productNode = productListNodes.get(i);
            String sourceId = productNode.getAttributeByName("data-mspid");

            TagNode imageNode = getSubNodeByXPath(productNode, XPATH_PRODUCT_IMAGEURL, new ContentParseException("image not found"));
            String imageUrl = imageNode.getAttributeByName("src");

            TagNode detailNode = getSubNodeByXPath(productNode, XPATH_PRODUCT_DETAIL, new ContentParseException("detail for product not found"));

            TagNode urlNode = getSubNodeByXPath(detailNode, "/a", new ContentParseException("url not found"));

            String url = urlNode.getAttributeByName("href");
            String title = urlNode.getText().toString().trim();

            TagNode priceNode = getSubNodeByXPath(detailNode, "/div[@class='prdct-item__prc']/span[@class='prdct-item__prc-val']", new ContentParseException("price not found"));
            String priceString = StringUtils.filterAndTrim(priceNode.getText().toString(), Arrays.asList(" ", ","));
            float price = Float.parseFloat(priceString);

            ListProduct listProduct = new ListProduct();
            listProduct.setWebsite(Website.MYSMARTPRICE);
            listProduct.setImageUrl(imageUrl);
            listProduct.setSourceId(sourceId);
            listProduct.setPrice(price);
            listProduct.setTitle(title);
            listProduct.setUrl(url);

            thdProductModelList.add(listProduct);
        }

        return thdProductModelList;
    }
}
