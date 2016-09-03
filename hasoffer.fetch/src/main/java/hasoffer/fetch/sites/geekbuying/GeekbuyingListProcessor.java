package hasoffer.fetch.sites.geekbuying;

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
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeStringByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/26
 */
public class GeekbuyingListProcessor implements IListProcessor{

	private static final Logger logger = LoggerFactory.getLogger(GeekbuyingListProcessor.class);
	private static final HtmlCleaner CLEANER = new HtmlCleaner();

	private static final String XPATH_PAGE_COUNT = "//div[@id='simple-pagination']";
	private static final String XPATH_PRODUCT_URL = "//li[@class='searchResultItem']/div[@class='name']/a";
	private static final String XPATH_PAGE_NUM = "//div[@id='pagination']/a[@class='pagenumber']";

	private static final Pattern PATTERN_PAGE_COUNT = Pattern.compile("\\d+/(\\d+)");

	private Set<String> existingProductIds;

	public GeekbuyingListProcessor() {
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

		if (!pageUrlTemplate.contains("{page}") && pageCount > 1) {
			TagNode pageNode = pageNodes.get(0);
			String url = pageNode.getAttributeByName("href");
			pageUrlTemplate = getPageUrlTemplate(url);
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

	public String getPageUrlTemplate(String url) {
		//http://www.geekbuying.com/category/Wearable-Devices-1549/11-40-3-0-0-0-grid.html
		StringBuffer urlTemplateBuf = new StringBuffer();
		String s1 = url.substring(0, url.lastIndexOf("/"));
		String s2 = url.substring(url.lastIndexOf("/"));
		s2 = s2.substring(s2.indexOf("-"));
		return s1 + "/{page}" + s2;
	}

	private int parsePageCount(TagNode root) {
		int pageCount = 1;
		try {
			//Page:2/2
			String pageNav = getSubNodeStringByXPath(root, XPATH_PAGE_COUNT, null);

			Matcher matcher = PATTERN_PAGE_COUNT.matcher(pageNav);
			if (matcher.matches()) {
				String pageCountStr = matcher.group(1);
				pageCount = Integer.parseInt(pageCountStr);
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
					productUrl = GeekbuyingHelper.SOURCE_SITE + productUrl;
				}
				if (productUrl.length() > 0) {
					ProductJob productJob = new ProductJob(job.getWebsite(), productUrl);

					if (productJobs.contains(productJob)) {
						logger.debug("Duplicated url: " + productUrl);
					}
					// check if existingProductIds contains sourceProductId
					String productId = GeekbuyingHelper.getProductIdByUrl(productUrl);
					if (existingProductIds != null && existingProductIds.contains(productId)) {
						logger.debug(String.format("the product[%s] exists, url:%s", productId, productUrl));
						continue;
					}
					productJobs.add(productJob);
				}
			}
			logger.debug("Added product jobs: " + productJobs.size());
			job.setProductJobs(productJobs);
		} catch (Exception e) {
			job.setError(true);
			job.setMessage("Fail to process url: " + job.getListUrl() + "\n" + StringUtils.stackTraceAsString(e));
		}
	}
}
