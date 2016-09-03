package hasoffer.fetch.sites.snapdeal;

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
 * Created on 2016/2/22.
 * 该类用来抓取Snapdeal的类目信息
 */
public class SnapDealCategoryProcessor implements ICategoryProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SnapDealCategoryProcessor.class);

    private static final String CATEGORY_MAIN_PAGE = "http://www.snapdeal.com";
    private static final String XPATH_CATEGORY_ROOT = "//*[@id='leftNavMenuRevamp']";
    private static final String WEBSITE_STRING = "http://www.snapdeal.com/";

    @Override
    public List<Category> parseCategories() throws HttpFetchException, ContentParseException {

        TagNode root = HtmlUtils.getUrlRootTagNode(CATEGORY_MAIN_PAGE);

        TagNode categoryRoot = getSubNodeByXPath(root, XPATH_CATEGORY_ROOT, new ContentParseException("category root not found."));

        //一级类目节点集合
        List<TagNode> cateNodes = getSubNodesByXPath(categoryRoot, "//li[@class='navlink smallNav']", new ContentParseException("no sub categoies."));

        //去除第一个和最后一个元素
        cateNodes.remove(0);
        cateNodes.remove(cateNodes.size()-1);

        List<Category> categories = new ArrayList<Category>();

        for (TagNode categoryNode : cateNodes) {
            getCategory(categories, categoryNode);
        }

        return categories;
    }

    /**
     * 按照目前snapdeal的结构抓取
     *
     * @param categories
     * @param categoryNode
     * @throws ContentParseException
     * @Date 2016.2.22
     */
    private void getCategory(List<Category> categories, TagNode categoryNode) throws ContentParseException, HttpFetchException {
        //获取二级列表中viewAll节点集合
        List<TagNode> firstCategoryNodes = getSubNodesByXPath(categoryNode, "div/div", new ContentParseException("category list url not found"));

        //从二级列表中选取，left，mid，right3组
        for (int i=0;i<3;i++){

            List<TagNode> viewAllNodes = getSubNodesByXPath(firstCategoryNodes.get(i), "//span[@class='viewText']", new ContentParseException("category list url not found"));

            int rank1 = 0;

            for(TagNode viewAllNode:viewAllNodes){

                String listUrl = viewAllNode.getParent().getAttributeByName("href").toString();
                //2.使用listurl中的title为categoryName
                String[] subStrs = listUrl.split("\\?");
                String[] subStrs2 = subStrs[0].split("/");
                String categoryName = subStrs2[subStrs2.length - 1];

                Category category = new Category();
                List<Category> subCates = new ArrayList<Category>();

                TagNode secCategoryRootTagNode = HtmlUtils.getUrlRootTagNode(listUrl);
                List<TagNode> secCategoryTagNode = getSubNodesByXPath(categoryNode, "//div[@class='sub-cat-name']", new ContentParseException("secCategoryNode not found"));
                int rank2 = 0;
                for (TagNode node : secCategoryTagNode) {
                    String secCategoryName = node.getText().toString().trim();
                    String[] subStrs3 = node.getParent().getAttributeByName("href").split("#");
                    String url = WEBSITE_STRING + subStrs3[0];
                    String[] subStrs4 = subStrs3[1].split(":");
                    String id = subStrs4[1];
                    subCates.add(new Category(id,secCategoryName, url, rank2++));
                }

                category.setSubCates(subCates);
                category.setName(categoryName);
                category.setUrl(listUrl);
                category.setRank(rank1++);

                categories.add(category);
            }
        }
    }

    /**
     * 如有必要再行补充，由于snapdeal网站好多跳转故障，等确定后在修正
     * @param listUrl
     * @return
     */
    public Category getThirdCategoryByUrl(String listUrl){

        Category category = new Category();

        try {

            TagNode root = HtmlUtils.getUrlRootTagNode(listUrl);


        } catch (HttpFetchException e) {
            logger.debug(listUrl+"getThirdCategory fail");
        }

        return category;
    }
}
