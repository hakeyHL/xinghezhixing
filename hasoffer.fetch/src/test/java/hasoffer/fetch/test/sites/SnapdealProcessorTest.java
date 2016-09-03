package hasoffer.fetch.test.sites;

/**
 * Date:2015/12/28 2015/12/28
 */

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.core.ICategoryProcessor;
import hasoffer.fetch.exception.ProductTitleNotFoundException;
import hasoffer.fetch.model.Category;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.sites.snapdeal.SnapDealCategoryProcessor;
import hasoffer.fetch.sites.snapdeal.SnapdealListProcessor;
import hasoffer.fetch.sites.snapdealDeprecated.SnapdealProductProcessor;
import org.apache.http.conn.ConnectTimeoutException;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.junit.Test;

import java.util.List;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;


public class SnapdealProcessorTest {
	@Test
	public void anylizeSnapDeal() throws ContentParseException, HttpFetchException, ConnectTimeoutException, XPatherException {
		String url = "http://www.snapdealDeprecated.com/product/apple-iphone-6-64-gb/131798351";
		SnapdealProductProcessor productProcessor = new SnapdealProductProcessor();
		productProcessor.parseProduct(url);
	}

	/**
	 * 测试xpath表达式，抓取分类名称
	 */
	@Test
	public void testXPathSnapdeal(){

		String url = "http://www.snapdeal.com/";

		String categoryXPath = "//*[@id=\"leftNavMenuRevamp\"]/div/div/ul/li[@class='navlink smallNav']/a/span[@class='menuWidthFst catText']";

		try {

			TagNode tagNode = HtmlUtils.getUrlRootTagNode(url);

			try {
				//String categoryName = getSubNodeStringByXPath(tagNode,categoryXPath,new ProductTitleNotFoundException(url));

				List<TagNode> subNodesList = getSubNodesByXPath(tagNode, categoryXPath, new ProductTitleNotFoundException(url));

				System.out.println(subNodesList.size());

				for(TagNode secTagNode:subNodesList){

					String catagoryName = secTagNode.getText().toString();

					System.out.println(catagoryName);

				}

			} catch (ContentParseException e) {
				e.printStackTrace();
			}

		} catch (HttpFetchException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试xpath表达式，ajax获取商品详情
	 */
	@Test
	public void testXPath2Snapdeal(){

		String ajaxRequest = "http://www.snapdeal.com/acors/json/product/get/search/175/startNum/pageSizeNum?q=&sort=plrty&brandPageUrl=&keyword=&vc=&webpageName=categoryPage&campaignId=&brandName=&isMC=false&clickSrc=";

		String ajaxProduct = "//*[@class='col-xs-6  product-tuple-listing js-tuple']";
		String offNumXPath = "//div[@class='product-tuple-image']/div[@class='product-disc']/span";
		String imgXPath = "//img[@class='product-image']";
		//商品名称后面有空格，注意去除
		String titleXPath = "//p[@class='product-title']";
		String priceXPath = "//span[@class='product-price']";

		try {

			ajaxRequest=ajaxRequest.replace("startNum", "0").replace("pageSizeNum", "5");

			TagNode root = HtmlUtils.getUrlRootTagNode(ajaxRequest);
			List<TagNode> tagNodes = getSubNodesByXPath(root, ajaxProduct, new ProductTitleNotFoundException(ajaxRequest));

			for (TagNode node:tagNodes){
				
				TagNode offNode = getSubNodeByXPath(node, offNumXPath, new ProductTitleNotFoundException(ajaxRequest));
				System.out.println(offNode.getText().toString());

				TagNode imgNode = getSubNodeByXPath(node, imgXPath, new ProductTitleNotFoundException(ajaxRequest));
				String imgPath = imgNode.getAttributeByName("src");
				System.out.println(imgPath);

				TagNode titleNode = getSubNodeByXPath(node, titleXPath, new ProductTitleNotFoundException(ajaxRequest));
				System.out.println(titleNode.getText().toString().trim());

				TagNode priceNode = getSubNodeByXPath(node, priceXPath, new ProductTitleNotFoundException(ajaxRequest));
				System.out.println(priceNode.getText().toString());

				System.out.println("---------------------------------------");
			}


		} catch (HttpFetchException e) {
			e.printStackTrace();
		} catch (ContentParseException e) {
			e.printStackTrace();
		}

	}

	/**
	 *  测试抓取分类数据
	 */
	@Test
	public void testSnapdealFetchCategory(){

		ICategoryProcessor snapdealCategoryProcessor = new SnapDealCategoryProcessor();

		try {
			//获取到79个分类
			List<Category> categories = snapdealCategoryProcessor.parseCategories();

		} catch (HttpFetchException e) {
			e.printStackTrace();
		} catch (ContentParseException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 测试抓取列表页数据
	 */
	@Test
	public void testSnapdealFetchList() throws XPatherException, ContentParseException, HttpFetchException {

		SnapdealListProcessor snapdealListProcessor = new SnapdealListProcessor();

		String ajaxUrl = "http://www.snapdeal.com/acors/json/product/get/search/175/0/4?sort=plrty";

		List<ListProduct> products = snapdealListProcessor.getProductByAjaxUrl(ajaxUrl,45L);

		System.out.println(products.size());

	}
}
