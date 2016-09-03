package hasoffer.fetch.sites.Ali;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.model.PageModel;
import hasoffer.fetch.sites.Ali.model.AliSpu;
import org.apache.commons.lang3.math.NumberUtils;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static hasoffer.base.utils.http.XPathUtils.*;
/**
 * Created by chevy on 2015/11/20.
 */
public class AliexpressBandListProcessor {

	private static final Logger logger = LoggerFactory.getLogger(AliexpressBandListProcessor.class);

	private static final String XPATH_SEARCH_COUNT = "//strong[@class='search-count']";

	private static final String XPATH_PAGE_NODES = "//div[@id='pagination-bottom']/div[@class='ui-pagination-navi util-left']/a";

	private static final String XPATH_PRODUCT_URL = "//ul[@class='util-clearfix son-list']/li";

	private static final String XPATH_PIC = "//div[@class='pic']//img";

	private static final String XPATH_TITLE = "//div[@class='info']/h3/a";

	private static final String XPATH_PRICE = "//div[@class='info']/span[@class='ptm']/em";

	private static final String XPATH_ORDER_COUNT = "//div[@class='rate']/span[@class='order-num']/em";

	private static final int PAGE_COUNT = 36;

	public List<AliSpu> extractProducts(String pageUrl) {
		logger.debug(String.format("%s - %s", "AliexpressBandListProcessor", pageUrl));

		List<TagNode> productNodes = null;

		try {
			TagNode root = HtmlUtils.getUrlRootTagNode(pageUrl);
			productNodes = getSubNodesByXPath(root, XPATH_PRODUCT_URL, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (ArrayUtils.isNullOrEmpty(productNodes)) {
			return null;
		}

		List<AliSpu> spuList = new ArrayList<AliSpu>();

		for (TagNode productNode : productNodes) {
			try {
				String proId = productNode.getAttributeByName("qrdata");
				proId = StringUtils.filterAndTrim(proId, Arrays.asList("\\|"));

				TagNode imageNode = getSubNodeByXPath(productNode, XPATH_PIC, null);
				String imageUrl = imageNode.getAttributeByName("src");

				TagNode titleNode = getSubNodeByXPath(productNode, XPATH_TITLE, null);
				String title = titleNode.getText().toString();
				String nextUrl = titleNode.getAttributeByName("href");

				float price = 0.0F;
				String priceStr = getSubNodeStringByXPath(productNode, XPATH_PRICE, null);
				if (!StringUtils.isEmpty(priceStr)) {
					priceStr = StringUtils.filterAndTrim(priceStr, Arrays.asList("US", "\\$"));
					price = Float.valueOf(priceStr);
				}

				String orderCountStr = getSubNodeStringByXPath(productNode, XPATH_ORDER_COUNT, null);
				int orderCount = Integer.parseInt(orderCountStr);

				String proUrl = getAProUrl(nextUrl);

				AliSpu aliSpu = new AliSpu(proId, proUrl, imageUrl, orderCount, price, title);

				spuList.add(aliSpu);
//				logger.debug(aliSpu.toString());
			} catch (Exception e) {
				continue;
			}
		}

		return spuList;
	}

	private String getAProUrl(String nextUrl) {
		try {
			TagNode root = HtmlUtils.getUrlRootTagNode(nextUrl);

			////a[@class='history-item product ']
			List<TagNode> proNodes = getSubNodesByXPath(root, "//h3/a", null);

			if (ArrayUtils.isNullOrEmpty(proNodes)) {
				return "";
			} else {
				return proNodes.get(0).getAttributeByName("href");
			}

		} catch (Exception e) {
			logger.debug(e.getStackTrace().toString());
			logger.debug(nextUrl);
			return "";
		}
	}

	public PageModel getPageModel(String pageUrl) {
		PageModel pageModel = new PageModel();

		String pageUrlTemplate = pageUrl;
		if (pageUrlTemplate.indexOf("{page}") > 0) {
			pageUrl = pageUrlTemplate.replace("{page}", "1");
		}

		try {
			TagNode root = HtmlUtils.getUrlRootTagNode(pageUrl);

			String searchCountStr = getSubNodeStringByXPath(root, XPATH_SEARCH_COUNT, null);
			float searchCount = Float.parseFloat(StringUtils.filterAndTrim(searchCountStr, Arrays.asList(",")));

			int pageCount = (int) Math.ceil(searchCount / PAGE_COUNT);

			List<TagNode> pageNodes = getSubNodesByXPath(root, XPATH_PAGE_NODES, null);

			//http://www.aliexpress.com/spulist.html?catId=5090301&page=2
			if (ArrayUtils.isNullOrEmpty(pageNodes)) {
				pageModel.setPageCount(1);
				pageModel.setUrlTemplate(pageUrl);
			} else {
				int size = pageNodes.size();
				for (int i = size - 1; i >= 0; i--) {
					TagNode pageNode = pageNodes.get(i);
					String pageNum = pageNode.getText().toString();
					if (NumberUtils.isDigits(pageNum)) {
						String url = pageNode.getAttributeByName("href");
						url = url.substring(0, url.lastIndexOf("=") + 1) + "{page}";
						pageModel.setUrlTemplate(StringUtils.unescapeHtml(url));
						pageModel.setPageCount(pageCount);
						break;
					}
				}
			}

			return pageModel;
		} catch (HttpFetchException e) {
			return pageModel;
		} catch (ContentParseException e) {
			return pageModel;
		}
	}
}
