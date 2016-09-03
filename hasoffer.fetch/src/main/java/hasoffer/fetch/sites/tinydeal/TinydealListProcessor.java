package hasoffer.fetch.sites.tinydeal;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.IListProcessor;
import hasoffer.fetch.model.ListJob;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.PageModel;
import hasoffer.fetch.model.ProductJob;
import hasoffer.fetch.sites.banggood.BanggoodListProcessor;
import org.apache.commons.lang3.math.NumberUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeStringByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/02
 */
public class TinydealListProcessor implements IListProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BanggoodListProcessor.class);
    private static final HtmlCleaner CLEANER = new HtmlCleaner();

    private static final String XPATH_PAGE_NAV = "//div[@id='pageDiv1']";
    private static final String XPATH_PAGE_NUM = "//div[@id='pageDiv1']/a[@class='pageNum']";

    //	private static final String XPATH_PRODUCT_URL = "//a[@class='a-link-normal s-access-detail-page  a-text-normal a-clickPredict']";
    private static final String XPATH_PRODUCT_URL =
            "//div[@id='productListing']/div[@class='p_box_wrapper']/li[@class='productListing-even']/a[@class='p_box_title']";

    private Set<String> existingProductIds;

    public TinydealListProcessor() {

    }

    @Override
    public void setExistingProductIds(Set<String> existingProductIds) {
        this.existingProductIds = existingProductIds;
    }

    @Override
    public PageModel getPageModel(String pageUrlTemplate) {

        String pageUrl = pageUrlTemplate;
        if (pageUrlTemplate.indexOf("{page}") > 0) {
            pageUrl = pageUrlTemplate.replace("{page}", "1");
        }

        List<TagNode> pageNodes = null;
        int pageCount = 0;

        try {
            TagNode root = HtmlUtils.getUrlRootTagNode(pageUrl);
            pageNodes = getSubNodesByXPath(root, XPATH_PAGE_NUM, null);
            pageCount = parsePageCount(root);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PageModel pageModel = new PageModel();

        if (pageCount > 1 && !pageUrlTemplate.contains("{page}")) {
            TagNode pageNode = pageNodes.get(0);
            String url = pageNode.getAttributeByName("href");
            pageUrlTemplate = url.replaceFirst("\\d+.html", "{page}.html");
        }

        //
        pageModel.setPageCount(pageCount);
        pageModel.setUrlTemplate(pageUrlTemplate);

        return pageModel;
    }

    @Override
    public List getProductByAjaxUrl(String ajaxUrl, Long ptmCateId) throws HttpFetchException, ContentParseException {
        return null;
    }

    @Override
    public List<ListProduct> getProductSetByKeyword(String keyword, int resultCount) {
        return null;
    }

    public int parsePageCount(TagNode root) {
        int pageCount = 1;
        try {
            String pageNav = getSubNodeStringByXPath(root, XPATH_PAGE_NAV, null);

            String[] ps = StringUtils.unescapeHtml(pageNav).trim().split("\\s+");

            int len = ps.length;
            for (int i = len - 1; i > 0; i--) {
                String tp = ps[i].trim();
                if (!StringUtils.isEmpty(tp)) {
                    if (NumberUtils.isNumber(tp)) {
                        pageCount = Integer.parseInt(tp);
                        break;
                    }
                }
            }

        } catch (ContentParseException e) {
            return -1;
        }

        return pageCount;
    }

    @Override
    public void extractProductJobs(ListJob job) {
        try {
            LinkedHashSet<ProductJob> productJobs = new LinkedHashSet<ProductJob>();

            int currentPage = 1;
            int pageCount = -1;
            // fetch first page
            String url = job.getListUrl();
            HttpResponseModel response = HtmlUtils.getResponse(url, 3);
            if (response.isHasException() || response.getStatusCode() != 200) {
                job.setError(true);
                job.setMessage("error fetch list page: " + url);
                return;
            }
            TagNode root = CLEANER.clean(response.getBodyString());

            List<TagNode> productLinkNodes;
            try {
                productLinkNodes = getSubNodesByXPath(root, XPATH_PRODUCT_URL, null);
            } catch (ContentParseException e) {
                job.setError(true);
                job.setMessage("error get product list: " + url);
                return;
            }
            for (TagNode productLinkNode : productLinkNodes) {
                String productUrl = StringUtils.notNullTrim(productLinkNode.getAttributeByName("href"));
                if (!productUrl.startsWith("http")) {
                    productUrl = TinydealHelper.SOURCE_SITE + productUrl;
                }
                if (productUrl.length() > 0) {
                    ProductJob productJob = new ProductJob(job.getWebsite(), productUrl);

                    if (productJobs.contains(productJob)) {
                        LOGGER.debug("Duplicated url: " + productUrl);
                    }
                    // check if existingProductIds contains sourceProductId
                    String productId = TinydealHelper.getProductIdByUrl(productUrl);
                    if (existingProductIds != null && existingProductIds.contains(productId)) {
                        LOGGER.debug(String.format("the product[%s] exists, url:%s", productId, productUrl));
                        continue;
                    }
                    productJobs.add(productJob);
                }
            }
            LOGGER.debug("Added product jobs: " + productJobs.size());
            job.setProductJobs(productJobs);
        } catch (Exception e) {
            job.setError(true);
            job.setMessage("Fail to process url: " + job.getListUrl() + "\n" + StringUtils.stackTraceAsString(e));
        }
    }
}
