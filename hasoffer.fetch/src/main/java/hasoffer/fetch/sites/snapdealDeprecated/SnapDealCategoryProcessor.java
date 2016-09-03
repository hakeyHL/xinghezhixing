package hasoffer.fetch.sites.snapdealDeprecated;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.sites.snapdeal.SnapdealHelper;
import hasoffer.fetch.sites.snapdealDeprecated.model.SnapDealFetchCategory;
import hasoffer.fetch.sites.snapdealDeprecated.model.SnapDealFetchProduct;
import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Author:menghaiquan
 * Date:2016/1/14 2016/1/14
 */
public class SnapDealCategoryProcessor {
	//*[@id="leftNavMenuRevamp"]/div/div/ul/li[5]/a/span[3]
	private static final String XPATH_FIRSTPAGE_CATEGORY = "//li[@navindex]";
	private static final String XPATH_FIRST_CATENAME = "/a/span[last()]";
	private static final String XPATH_SECOND_CATEGORY = "/div/div/div/div/p/a/span[@class='headingText']";

	private static final String XPATH_THIRD_CATEGORY = "//div[@class='cat-nav-wrapper']/ul/li/ul/li[@class='sub-cat-list cat-list']";

	private static final String XPATH_THIRD_CATENAME = "/div[@class='sub-cat-name']";
	private static final String XPATH_THIRD_CATECOUNT = "/div[@class='sub-cat-count']";

	private static final String CATE_ID_PATH = "//div[@id='breadCrumbLabelIds']";

	private static final String AJAX_PRODUCT_QUERY = "http://www.snapdealDeprecated.com/acors/json/product/get/search/%d/%d/%d?q=&sort=plrty&brandPageUrl=&keyword=&vc=&webpageName=categoryPage&campaignId=&brandName=&isMC=false&clickSrc=";

	private static final String PRODUCT_SECTION = "//section[@class='js-section clearfix']";
	private static final String PRODUCT_IMG_PATH = "/div[@class='product-tuple-image']/a/img";
	private static final String PRODUCT_NAME_PATH = "/div[@class='product-tuple-description']/div[@class]/a/p[@class='product-title']";
	private static final String PRODUCT_PRICE_PATH = "/div[@class='product-tuple-description']/div[@class='productPrice']/div/span[@class='product-price']";
	private static final String PRODUCT_DES_PRICE_PATH = "/div[@class='product-tuple-description']/div[@class='productPrice']/div/span[@class='product-desc-price strike']";

	public static SnapDealFetchCategory evaluateCate1FromScrum(String url) throws HttpFetchException, XPatherException {
		String scrumNode = "//div[@class='containerBreadcrumb']";
		TagNode root = HtmlUtils.getUrlRootTagNode(url);
		TagNode node = HtmlUtils.getFirstNodeByXPath(root, scrumNode);
		if (node != null){
			TagNode linkNode = node.findElementByName("a", false);
			if (linkNode != null){
				String href = linkNode.getAttributeByName("href");
				href = href.split("#")[0];

				String title = linkNode.getText().toString().trim();
				SnapDealFetchCategory category = new SnapDealFetchCategory();
				category.setName(title);
				category.setUrl(href);
				category.setDepth(1);
				return category;
			}
		}

		return null;
	}

	public static long parseCateId(String url) throws XPatherException, HttpFetchException {
		TagNode root = null;
		try {
			root = HtmlUtils.getUrlRootTagNode(url);
		} catch (HttpFetchException e) {
			if (e.getMessage().contains("500")){
				System.out.println(e.getMessage());
				return 0;
			} else
			{
				throw e;
			}
		}

		TagNode idNode = HtmlUtils.getFirstNodeByXPath(root, CATE_ID_PATH);
		String text = idNode.getText().toString();
		String[] subStr = text.trim().split(",");
		for (int i = subStr.length -1 ; i >= 0; i--){
			String idStr = subStr[i].trim();
			if (StringUtils.isNumeric(idStr)){
				return Long.parseLong(idStr);
			}
		}

		return 0;
	}

	public static boolean fetchProductsByCate(long cateId, long sourceCateId, long count, BlockingQueue<SnapDealFetchProduct> queue){
		ExecutorService service = Executors.newFixedThreadPool(15);
		ProductSummaryEvaluateWorker.setCountPerTime(8);
		ProductSummaryEvaluateWorker.setIndexPos(0);
		ProductSummaryEvaluateWorker.setSourceCateId(sourceCateId);
		ProductSummaryEvaluateWorker.setCateId(cateId);
		ProductSummaryEvaluateWorker.setTotal(count);
		ProductSummaryEvaluateWorker.setQueue(queue);
		for (int i = 0; i < 10; i++){
			service.execute(new ProductSummaryEvaluateWorker());
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

		return true;
	}

	public static boolean parseProducts(long cateId, long cateSourceId, long pos, long count, BlockingQueue<SnapDealFetchProduct> queue)
			throws HttpFetchException, XPatherException {
		String url = String.format(AJAX_PRODUCT_QUERY, cateSourceId, pos, count);
		System.out.println(url);
		TagNode root = HtmlUtils.getUrlRootTagNode(url);

		List<TagNode> secNodes = HtmlUtils.getSubNodesByXPath(root, PRODUCT_SECTION);
		if (secNodes != null && secNodes.size() > 0){
			for (TagNode node : secNodes){
				TagNode[] nodes = node.getAllElements(false);
				if (nodes != null &&nodes.length > 0){
					for (TagNode pnode : nodes){
						TagNode imgNode = HtmlUtils.getFirstNodeByXPath(pnode, PRODUCT_IMG_PATH);
						String imgUrl = "";

						if (imgNode != null){
							imgUrl = imgNode.getAttributeByName("src");
						} else {
							System.out.println("Img node not found!");
						}

						//String productUrl = imgNode.getParent().getAttributeByName("href");

						String name = "";
						String productUrl = "";
								TagNode nameNode = HtmlUtils.getFirstNodeByXPath(pnode, PRODUCT_NAME_PATH);
						if (nameNode == null){
							System.out.println("name node null");
						} else {
							name = nameNode.getText().toString().trim();
							productUrl = nameNode.getParent().getAttributeByName("href");
						}

						TagNode priceNode = HtmlUtils.getFirstNodeByXPath(pnode, PRODUCT_PRICE_PATH);
						String priceStr = priceNode.getText().toString();
						priceStr = priceStr.replace("Rs.", "").trim();
						priceStr = priceStr.replace(",", "");
						SnapDealFetchProduct pro = new SnapDealFetchProduct(name, imgUrl,productUrl, Float.parseFloat(priceStr), cateId);
						queue.add(pro);
					}

				}
			}
		}

		return true;
	}

	public Set<SnapDealFetchCategory> parseCategories() throws ContentParseException, XPatherException, HttpFetchException {
		Set<String> seedUrls = new HashSet<String>();
		try {
			TagNode root = HtmlUtils.getUrlRootTagNode(SnapdealHelper.SITE_URL);
			List<TagNode> category0Nodes = getSubNodesByXPath(root, XPATH_FIRSTPAGE_CATEGORY, null);
			for (int i = 0; i < category0Nodes.size(); i++){
				int index = Integer.parseInt(category0Nodes.get(i).getAttributeByName("navindex"));
				if (index == 1 || index == category0Nodes.size()){
					continue;
				} else {
					evaluateSeedsForCates(category0Nodes.get(i), seedUrls);
				}
			}
		} catch (HttpFetchException e) {
			e.printStackTrace();
		}

		Set<SnapDealFetchCategory> cate1Set = evalueCate1s(seedUrls);

		evaluateCate2s(cate1Set);

		return cate1Set;
	}

	public SnapDealFetchCategory parseLevel0CateNode(TagNode cate0Node) throws XPatherException, ContentParseException {
		TagNode level0NameNode = HtmlUtils.getFirstNodeByXPath(cate0Node, XPATH_FIRST_CATENAME);
		String cate0Name = level0NameNode.getText().toString();
		SnapDealFetchCategory cate0 = new SnapDealFetchCategory();
		cate0.setName(cate0Name);
		cate0.setDepth(1);
		cate0.setSubCates(new LinkedList<SnapDealFetchCategory>());

		List<TagNode> level2Nodes = getSubNodesByXPath(cate0Node, XPATH_SECOND_CATEGORY, null);
		if (level2Nodes != null && level2Nodes.size() > 0){
			for (TagNode node : level2Nodes){
				TagNode parent = node.getParent();
				String href = parent.getAttributeByName("href");
				if (StringUtils.isBlank(href)){
					continue;
				}

				if (href.toLowerCase().contains("javascript:void(0)")){
					continue;
				}

				String cateName = node.getText().toString();
				SnapDealFetchCategory cate1 = new SnapDealFetchCategory();
				cate1.setName(cateName);
				cate1.setUrl(href);
				cate1.setDepth(2);
				cate0.getSubCates().add(cate1);
			}
		}

		return cate0;
	}

	public void evaluateSeedsForCates(TagNode cate0Node, Set<String> urls) throws ContentParseException {
		List<TagNode> level2Nodes = getSubNodesByXPath(cate0Node, XPATH_SECOND_CATEGORY, null);
		if (level2Nodes != null && level2Nodes.size() > 0){
			for (TagNode node : level2Nodes){
				TagNode parent = node.getParent();
				String href = parent.getAttributeByName("href");
				if (StringUtils.isBlank(href)){
					continue;
				}

				if (href.toLowerCase().contains("javascript:void(0)")){
					continue;
				}

				href = href.trim();
				href = href.split("\\?")[0];
				urls.add(href);
			}
		}
	}

	public Set<SnapDealFetchCategory> evalueCate1s(Set<String> seedsSet) throws HttpFetchException, XPatherException {
		Set<SnapDealFetchCategory> cate1Set = new HashSet<SnapDealFetchCategory>();
		List<String> seeds = new LinkedList<String>();
		String[] seedArr = seedsSet.toArray(new String[0]);
		for (String seed : seedArr){
			seeds.add(seed);
		}
		/*for (String url : seedsSet){
			SnapDealFetchCategory category = evaluateCate1FromScrum(url);
			if (category != null){
				cate1Set.add(category);
			}
		}*/
		ExecutorService service = Executors.newFixedThreadPool(15);
		for (int i = 0; i < 15; i++){
			service.execute(new CategoryEvaluateWorker(cate1Set, seeds));
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

		return cate1Set;
	}

	public void evaluateCate2s(Set<SnapDealFetchCategory> cate1Set) throws ContentParseException, XPatherException, HttpFetchException {
		/*Iterator<SnapDealFetchCategory> it = cate1Set.iterator();
		while (it.hasNext()){
			SnapDealFetchCategory cate = it.next();
			evaluateCate2(cate);
		}*/
		for (SnapDealFetchCategory category : cate1Set){
			evaluateCate2(category);
		}
	}

	public void evaluateCate2(SnapDealFetchCategory cate1) throws HttpFetchException, ContentParseException, XPatherException {
		cate1.setSubCates(new LinkedList<SnapDealFetchCategory>());
		String url = cate1.getUrl() + "?sort=plrty";
		TagNode pageRoot = HtmlUtils.getUrlRootTagNode(url);
		List<TagNode> cate2Nodes = getSubNodesByXPath(pageRoot, XPATH_THIRD_CATEGORY, null);
		for (TagNode cate2Node : cate2Nodes){
			TagNode linkNode = cate2Node.getElementsByName("a", false)[0];
			String href = linkNode.getAttributeByName("href");
			href = href.split("#")[0];
			href = SnapdealHelper.SITE_URL + href;
			TagNode nameNode = HtmlUtils.getFirstNodeByXPath(linkNode, XPATH_THIRD_CATENAME);
			String name = nameNode.getText().toString();

			TagNode countNode = HtmlUtils.getFirstNodeByXPath(linkNode, XPATH_THIRD_CATECOUNT);
			int count = Integer.parseInt(countNode.getText().toString());

			SnapDealFetchCategory cate2 = new SnapDealFetchCategory();
			cate2.setUrl(href);
			cate2.setName(name);
			cate2.setProCount(count);
			cate2.setDepth(2);
			cate1.getSubCates().add(cate2);
		}
	}
}
