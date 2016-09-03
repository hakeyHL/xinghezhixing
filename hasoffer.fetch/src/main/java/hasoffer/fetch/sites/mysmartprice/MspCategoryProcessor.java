package hasoffer.fetch.sites.mysmartprice;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.sites.mysmartprice.model.MySmartPriceCategory;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static hasoffer.base.utils.http.XPathUtils.*;
/**
 * Created by chevy on 2015/12/3.
 *	该类用来抓取出category的list集合
 */
public class MspCategoryProcessor {

	private static final Logger logger = LoggerFactory.getLogger(MspCategoryProcessor.class);

	private static final String XPATH_FIRSTPAGE_CATEGORY = "//div[@class='ctgry-list']/div/a";

	private static final String X_PRO_DIV = "//div[@class='store_pricetable_main']";

	public List<MySmartPriceCategory> parseCategories() {

		List<MySmartPriceCategory> categories = new ArrayList<MySmartPriceCategory>();

		try {
			TagNode root = HtmlUtils.getUrlRootTagNode(MspHelper.SITE_URL);

			List<TagNode> category0Nodes = getSubNodesByXPath(root, XPATH_FIRSTPAGE_CATEGORY, null);

			for (TagNode cateNode : category0Nodes) {
				MySmartPriceCategory category = getCategory(cateNode);
				categories.add(category);
			}

		} catch (HttpFetchException fetchException) {
			System.err.println(fetchException);
			return categories;
		} catch (ContentParseException cpe) {
			System.err.println(cpe);
			return categories;
		}

		return categories;
	}

	private MySmartPriceCategory getCategory(TagNode cateNode) throws HttpFetchException, ContentParseException {

		String url = cateNode.getAttributeByName("href");
		String name = getSubNodeStringByXPath(cateNode, "//span[@class='ctgry-item__text']", null);

		url = StringUtils.filterAndTrim(StringUtils.unescapeHtml(url), null);
		name = StringUtils.filterAndTrim(StringUtils.unescapeHtml(name), null);

		MySmartPriceCategory mspCategory = new MySmartPriceCategory(name, url);

		if (url.equalsIgnoreCase(MspHelper.MSP_DEAL_URL)) {
			return mspCategory;
		}

		TagNode root = HtmlUtils.getUrlRootTagNode(url);
		List<TagNode> cateNodes = getSubNodesByXPath(root, "//div[@class='sctn item-grid']", null);

		List<MySmartPriceCategory> subCates = new ArrayList<MySmartPriceCategory>();

		for (TagNode cn : cateNodes) {
			String groupName = cn.getAttributeByName("data-id");
			List<TagNode> subCateNodes = getSubNodesByXPath(cn, "//div[@class='item-grid-item']", null);
			for (TagNode subCateNode : subCateNodes) {

				TagNode nameNode = getSubNodeByXPath(subCateNode, "//a[@class='item-grid-item__ttl']", null);

				String subCateUrl = nameNode.getAttributeByName("href");
				if (subCateUrl.startsWith("/")) {
					subCateUrl = "http://www.mysmartprice.com" + subCateUrl;
				}
				String subCateName = nameNode.getText().toString();

				TagNode imageNode = getSubNodeByXPath(subCateNode, "//img", null);
				String imageUrl = imageNode.getAttributeByName("src");

				MySmartPriceCategory subCate = new MySmartPriceCategory(subCateName, subCateUrl, imageUrl, groupName);
				subCates.add(subCate);
			}
		}

		mspCategory.setSubCategories(subCates);

		return mspCategory;
	}
}
