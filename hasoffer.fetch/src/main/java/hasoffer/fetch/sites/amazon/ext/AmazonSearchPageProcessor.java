package hasoffer.fetch.sites.amazon.ext;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.sites.amazon.ext.model.AmazonSearchPage;
import hasoffer.fetch.sites.amazon.ext.model.AmazonSimpleProduct;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static hasoffer.base.utils.http.XPathUtils.*;
/**
 * Created by chevy on 2015/11/12.
 */
public class AmazonSearchPageProcessor {

	private static final String XPATH_CATEGORY = "//select[@id='searchDropdownBox']/option[@selected]";
	private static final String XPATH_KEYWORD = "//*[@id='twotabsearchtextbox']";
	private static final String XPATH_PRODUCT = "//ul[@id='s-results-list-atf']/li";
	private static Logger logger = LoggerFactory.getLogger(AmazonSearchPageProcessor.class);

	public AmazonSearchPage parse(String searchUrl) throws HttpFetchException, ContentParseException {

		logger.debug(String.format("search url is [%s].", searchUrl));

		TagNode root = null;

		try {
			root = HtmlUtils.getUrlRootTagNode(searchUrl);
		} catch (HttpFetchException e) {
			logger.error(e.getMessage());
			return null;
		}

		AmazonSearchPage searchPage = new AmazonSearchPage();

		String category = getSubNodeStringByXPath(root, XPATH_CATEGORY, null);

		String keyword = "";
		TagNode keyNode = getSubNodeByXPath(root, XPATH_KEYWORD, null);
		if (keyNode != null) {
			keyword = keyNode.getAttributeByName("value");
		}

		List<AmazonSimpleProduct> simpleProducts = new ArrayList<AmazonSimpleProduct>();
		List<TagNode> proNodes = getSubNodesByXPath(root, XPATH_PRODUCT, null);
		if (!ArrayUtils.isNullOrEmpty(proNodes)) {
			int index = 0;
			for (TagNode proNode : proNodes) {
				simpleProducts.add(getSimpleProduct(proNode, index));
			}
		}

		searchPage.setCategory(category);
		searchPage.setKeyword(keyword);
		searchPage.setPageUrl(searchUrl);
		searchPage.setSimpleProducts(simpleProducts);

		return searchPage;
	}

	private AmazonSimpleProduct getSimpleProduct(TagNode proNode, int index) throws ContentParseException {
		String asin = proNode.getAttributeByName("data-asin");

		List<TagNode> tNodes = getSubNodesByXPath(proNode, "//a", null);

		String title = "";
		String url = "";
		for (TagNode tNode : tNodes) {
			if (tNode.hasAttribute("title") && tNode.hasAttribute("href")) {
				title = tNode.getAttributeByName("title");
				url = tNode.getAttributeByName("href");
			}
		}

		AmazonSimpleProduct simpleProduct = new AmazonSimpleProduct();

		simpleProduct.setASIN(asin);
		simpleProduct.setTitle(title);
		simpleProduct.setUrl(url);
		simpleProduct.setPage(1);
		simpleProduct.setRank(index);

		return simpleProduct;
	}
}
