package hasoffer.fetch.sites.gearbest;

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

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/26
 */
public class GearbestListProcessor implements IListProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(GearbestListProcessor.class);
	private static final HtmlCleaner CLEANER = new HtmlCleaner();

	private static final String XPATH_PAGE_NAV = "//div[@class='pages tr fr']/p[@class='listspan']/*";

	private static final String XPATH_PRODUCT_URL = "//ul[@id='catePageList']/li/div[@class='pro_inner']/p[@class='all_proNam']/a";

	private static final String XPATH_PRODUCT_URL_FOR_SEARCHRESULT = "//div[@class='pro_inner']/p[@class='all_proNam']/a";

	private static final Pattern PRODUCT_URL_ID_PATTERN = Pattern.compile(
			".+/pp_([\\d]+).html?"
	);

	private Set<String> existingProductIds;

	public GearbestListProcessor() {
	}

	@Override
	public void setExistingProductIds(Set<String> existingProductIds) {
		this.existingProductIds = existingProductIds;
	}

	@Override
	public PageModel getPageModel(String pageUrlTemplate) {

		PageModel pageModel = new PageModel();

		String pageUrl = pageUrlTemplate;
		if (pageUrlTemplate.contains("{page}")) {
			pageUrl = pageUrl.replace("{page}", "1");
		} else {
			if (!pageUrlTemplate.endsWith("/")) {
				pageUrlTemplate += "/";
			}
			pageUrlTemplate = pageUrlTemplate + "{page}.html";
		}

		int pageCount = parsePageCount(pageUrl);

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

	public int parsePageCount(String url) {
		HttpResponseModel response = HtmlUtils.getResponse(url, 3);
		if (response.isHasException() || response.getStatusCode() != 200) {
			LOGGER.error("error fetch list page: " + url);
			return -1;
		}
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode root = cleaner.clean(response.getBodyString());

		List<TagNode> pageNodes;
		try {
			pageNodes = getSubNodesByXPath(root, XPATH_PAGE_NAV, null);
		} catch (ContentParseException e) {
			return -1;
		}
		if (pageNodes == null || pageNodes.size() < 3) {
			return 1;
		}
		String pageCountStr = pageNodes.get(pageNodes.size() - 2).getText().toString();

		return Integer.parseInt(pageCountStr);
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

			List<TagNode> productLinkNodes = getSubNodesByXPath(root, XPATH_PRODUCT_URL, null);

			if (productLinkNodes == null || productLinkNodes.isEmpty()) {
				productLinkNodes = getSubNodesByXPath(root, XPATH_PRODUCT_URL_FOR_SEARCHRESULT, null);
			}

			for (TagNode productLinkNode : productLinkNodes) {
				String productUrl = StringUtils.filterAndTrim(productLinkNode.getAttributeByName("href"), Arrays.<String>asList());
				if (productUrl.length() > 0) {
					if (productJobs.contains(new ProductJob(job.getWebsite(), productUrl))) {
						LOGGER.debug("Duplicated url: " + productUrl);
					}
					// check if existingProductIds contains sourceProductId
					String productId = GearbestHelper.getProductIdByUrl(productUrl);
					if (existingProductIds.contains(productId)) {
						LOGGER.debug(String.format("the product[%s] exists, url:%s", productId, productUrl));
						continue;
					}
					productJobs.add(new ProductJob(job.getWebsite(), productUrl));
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
