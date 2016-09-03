package hasoffer.fetch.sites.amazon;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.core.ICategoryProcessor;
import hasoffer.fetch.model.Category;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/26
 */
public class AmazonCategoryProcessor implements ICategoryProcessor {
	private static final String CATEGORY_MAIN_PAGE = "http://www.amazon.com/gp/site-directory/ref=nav_shopall_btn";
	private static final String XPATH_CATEGORY_ROOT = "//*[@id='shopAllLinks']";

	private static Logger logger = LoggerFactory.getLogger(AmazonCategoryProcessor.class);

	@Override
	public List<Category> parseCategories() throws HttpFetchException, ContentParseException {

		TagNode root = HtmlUtils.getUrlRootTagNode(CATEGORY_MAIN_PAGE);

		TagNode categoryRoot = getSubNodeByXPath(root, XPATH_CATEGORY_ROOT, new ContentParseException("category root not found."));

		List<TagNode> cateNodes =
				getSubNodesByXPath(categoryRoot, "//div[@class='popover-grouping']", new ContentParseException("no sub categoies."));

		List<Category> categories = new ArrayList<Category>();
		/**
		 * Amazon 是两级分类
		 */
		for (TagNode cateNode : cateNodes) {
			getCategory(categories, cateNode);
		}

		return categories;
	}

	private void getCategory(List<Category> categories, TagNode cate) throws ContentParseException {
		TagNode tagNode = getSubNodeByXPath(cate, "//h2", null);

		String tagNodeName = tagNode.getText().toString().trim();
		Category category = new Category(tagNodeName, null, 0);

		List<Category> subCates = new ArrayList<Category>();
		List<TagNode> nodes = getSubNodesByXPath(cate, "//ul/li/a", null);

		int rank = 0;
		for (TagNode node : nodes) {
			String url = node.getAttributeByName("href");
			if (!url.startsWith("http")) {
				url = "http://www.amazon.com" + url;
			}
			String name = node.getText().toString().trim();
			subCates.add(new Category(name, url, rank++));
		}

		category.setSubCates(subCates);

		categories.add(category);
	}
}
