package hasoffer.fetch.sites.gearbest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.fetch.core.ICategoryProcessor;
import hasoffer.fetch.model.Category;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/26
 */
public class GearbestCategoryProcessor implements ICategoryProcessor {
	private static final String CATEGORY_MAIN_PAGE = "http://www.gearbest.com/";
	private static final String XPATH_CATEGORY_ROOT = "//div[@id='js_nav_list']";
	private static final String SUB_CATEGORY_REQUEST_URL =
			"http://www.gearbest.com/index.php?m=public&a=getNav&is_index=1&type=0&callback=callback_#time#&_=#time#";
	private static Logger logger = LoggerFactory.getLogger(GearbestCategoryProcessor.class);

	@Override
	public List<Category> parseCategories() throws HttpFetchException, ContentParseException {

		TagNode root = HtmlUtils.getUrlRootTagNode(CATEGORY_MAIN_PAGE);

		TagNode categoryRoot = getSubNodeByXPath(root, XPATH_CATEGORY_ROOT, new ContentParseException("category root not found."));

		List<TagNode> cateNodes = getSubNodesByXPath(categoryRoot, "//ul/li", new ContentParseException("no sub categoies."));

		// 通过请求json获取子类目
		String timeStamp = String.valueOf(TimeUtils.now());
		HttpResponseModel response = HtmlUtils.getResponse(SUB_CATEGORY_REQUEST_URL.replaceAll("#time#", timeStamp), 3);
		String subCatesJsonString = response.getBodyString().replace("callback_" + timeStamp, "").trim();
		JSONObject subCatesJson = JSON.parseObject(subCatesJsonString.substring(1, subCatesJsonString.length() - 1));
		JSONArray subCatesH = (JSONArray) subCatesJson.get("data");

		if (cateNodes.size() != subCatesH.size()) {
			logger.debug("类目数据不一致");
		}

		int index = 0;
		for (TagNode cateNode : cateNodes) {
			Object object = subCatesH.get(index);
			TagNode subCateNode = HtmlUtils.getTagNode(object.toString());

			getCategory(cateNode, subCateNode);

			index++;
		}

		// TODO 分析入库

		logger.debug(categoryRoot.toString());
		return null;
	}

	private void getCategory(TagNode cate, TagNode subCate) throws ContentParseException {
		TagNode tagNode = getSubNodeByXPath(cate, "//a", null);

		List<TagNode> tagNodes = getSubNodesByXPath(subCate, "//dd", null);

		logger.debug(tagNode.toString());
	}
}
