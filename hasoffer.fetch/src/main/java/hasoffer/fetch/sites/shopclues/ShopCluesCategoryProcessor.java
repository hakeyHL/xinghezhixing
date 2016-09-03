package hasoffer.fetch.sites.shopclues;

import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.sites.shopclues.model.ShopCluesFetchCategory;
import hasoffer.fetch.sites.shopclues.model.ShopCluesFetchProduct;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author:menghaiquan
 * Date:2016/1/21 2016/1/21
 */
public class ShopCluesCategoryProcessor {
	private static final String CATES_URL = "http://www.shopclues.com/categories";
	private static final String CATE_BLOCK = "//div[@class='cat_icon_text_nl']";
	private static final String CATE1_PATH = "/div[@class='nav_topsubmenu_label_new']/a";
	private static final String DEEP_CATES_PATH = "/div/div/a";

	private static final String CATE_COUNT_PATH = "//div[@class='products-header']/span";
	private static final String CATE_ID = "//input[@id='category_id']";

	private static final String AJAX_PRODUCT_QUERY = "http://www.shopclues.com/index.php?dispatch=categories.view&category_id=%d&isis=1&page=%d&undefined";

	private static final String PRODUCT_GRID = "//div[@class='products-grid']/div[@class='grid-product special  category_grid_4']";
	private static final String PRODUCT_IMG = "/a/img";
	private static final String PRODUCT_TITLE = "/div[@class='details']/a";
	private static final String PRODUCT_PRICE = "/div[@class='details']/div[@class='product-price']/span[@class='price']";

	public static Set<ShopCluesFetchCategory> parseCategories() throws HttpFetchException, XPatherException {
		Set<ShopCluesFetchCategory> set = new HashSet<ShopCluesFetchCategory>();
		TagNode root = HtmlUtils.getUrlRootTagNode(CATES_URL);
		List<TagNode> cateBlocks = HtmlUtils.getSubNodesByXPath(root, CATE_BLOCK);
		if (cateBlocks != null &&cateBlocks.size() > 0){
			for (TagNode catesNode : cateBlocks){
				TagNode cate1 = HtmlUtils.getFirstNodeByXPath(catesNode, CATE1_PATH);
				String title = cate1.getText().toString().trim();
				String url = cate1.getAttributeByName("href");
				if (!url.contains("//")){
					url = "http://www.shopclues.com/" + url;
				}

				ShopCluesFetchCategory category1 = new ShopCluesFetchCategory();
				category1.setDepth(1);
				category1.setUrl(url);
				category1.setName(title);
				category1.setSubCates(new LinkedList<ShopCluesFetchCategory>());
				set.add(category1);

				List<TagNode> deepCates = HtmlUtils.getSubNodesByXPath(catesNode, DEEP_CATES_PATH);
				if (deepCates != null && deepCates.size() > 0){
					for (TagNode deepCate : deepCates){
						String cateUrl = deepCate.getAttributeByName("href");
						String[] urlParts = cateUrl.split("/");
						if (urlParts.length == 3){
							String cate2Title = deepCate.getText().toString().trim();
							ShopCluesFetchCategory category2 = new ShopCluesFetchCategory();
							category2.setDepth(2);
							category2.setUrl(cateUrl);
							category2.setName(cate2Title);
							category1.getSubCates().add(category2);
						}
					}
				}
			}
		}

		return set;
	}

	public static String getCateInfo(String pageUrl) throws HttpFetchException, XPatherException {
		TagNode root = HtmlUtils.getUrlRootTagNode(pageUrl);
		TagNode countNode = HtmlUtils.getFirstNodeByXPath(root, CATE_COUNT_PATH);
		String countStr = countNode.getText().toString();
		Pattern p = Pattern.compile("\\D*(\\d+)\\D*");
		Matcher m = p.matcher(countStr);

		if (m.matches()){
			countStr = m.group(1);
		}

		TagNode cateIdNode = HtmlUtils.getFirstNodeByXPath(root, CATE_ID);
		String cateIdV = cateIdNode.getAttributeByName("value");

		return countStr + "," + cateIdV;
	}

	public static void fetchProductsByCate(long cateId, long sourceCateId, long count, BlockingQueue<ShopCluesFetchProduct> queue){
		ExecutorService service = Executors.newFixedThreadPool(15);
		ProductSummaryEvaluateWorker.setCateId(cateId);
		ProductSummaryEvaluateWorker.setTotal(count);
		ProductSummaryEvaluateWorker.setSourceCateId(sourceCateId);
		ProductSummaryEvaluateWorker.setPageNum(1);
		ProductSummaryEvaluateWorker.setCurrentCount(0);

		for (int i = 0; i < 10; i++){
			service.execute(new hasoffer.fetch.sites.snapdealDeprecated.ProductSummaryEvaluateWorker());
		}

		service.shutdown();
		while (true){
			if (service.isTerminated()){
				break;
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static int parseProducts(long cateId, long sourceCateId, long page, BlockingQueue<ShopCluesFetchProduct> queue)
			throws HttpFetchException, XPatherException {
		String url = String.format(AJAX_PRODUCT_QUERY, sourceCateId, page);
		TagNode root = HtmlUtils.getUrlRootTagNode(url);
		List<TagNode> nodes = HtmlUtils.getSubNodesByXPath(root, PRODUCT_GRID);
		int count = 0;
		if (nodes != null && nodes.size()>0){
			count = nodes.size();
			for (TagNode node : nodes){
				TagNode imgNode = HtmlUtils.getFirstNodeByXPath(node, PRODUCT_IMG);
				String imgUrl = "";
				if (imgNode != null){
					imgUrl = imgNode.getAttributeByName("src2");
				}

				TagNode titleNode = HtmlUtils.getFirstNodeByXPath(node, PRODUCT_TITLE);
				String title = titleNode.getText().toString();
				String href = titleNode.getAttributeByName("href");

				TagNode priceNode = HtmlUtils.getFirstNodeByXPath(node, PRODUCT_PRICE);
				String priceStr = priceNode.getText().toString().replace(",", "");
				float price = Float.parseFloat(priceStr);

				ShopCluesFetchProduct product = new ShopCluesFetchProduct(title, imgUrl, href, price, cateId);
				queue.add(product);
			}
		}

		return count;
	}
}
