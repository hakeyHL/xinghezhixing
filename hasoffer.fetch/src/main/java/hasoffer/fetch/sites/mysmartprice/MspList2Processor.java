package hasoffer.fetch.sites.mysmartprice;

import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.sites.mysmartprice.model.MySmartPriceUncmpProduct;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeStringByXPath;

/**
 * Created on 2015/12/4.
 */
@Deprecated
public class MspList2Processor {
    private static final String PRODUCT_IMG_PATH = "/a[@class='image-container']/div[@class='image-wrapper']/img";
    private static final String PRODUCT_NAME_PATH = "/a[@class='info']/div[@class='title']";
    private static final String PRODUCT_OFFER_PATH = "/a[@class='price-info']/div[@class='offer-info']";
    private static final String PRODUCT_PRICE_PATH = "/a[@class='price-info']/span";
    private static final String TITLE_IN_DETAIL = "//div[@class='singlePageTitle']";
    private static final String BUY_NOW_BUTTON = "//div[@class='buyNowButton']/a";
    private static String AJAX_PRODUCTS_QUERY = "http://www.mysmartprice.com/fashion/filters/filter_get_revamp?recent=0&q=filter%2F&subcategory=$cateIdentifier&start=$start&rows=$rowCount&page_name=";
    private static String PRODUCT_SECTION = "//div[@class='grid-item product']";
    private static Logger logger = LoggerFactory.getLogger(MspList2Processor.class);

    private Set<String> existingProductIds;

    @Deprecated
    public static boolean fetchProductsByCate(long cateId, String categoryIdentity, long count, BlockingQueue<MySmartPriceUncmpProduct> queue) {
        ExecutorService service = Executors.newFixedThreadPool(15);
        UncmpProductEvaluateWorker.setTotal(count);
        UncmpProductEvaluateWorker.setCateId(cateId);
        UncmpProductEvaluateWorker.setIndexPos(0);
        UncmpProductEvaluateWorker.setCountPerTime(1);  // 目前此值只支持1
        UncmpProductEvaluateWorker.setCategoryIdentity(categoryIdentity);
        UncmpProductEvaluateWorker.setQueue(queue);
        for (int i = 0; i < 25; i++) {
            service.execute(new UncmpProductEvaluateWorker());
        }

        service.shutdown();
        while (true) {
            if (service.isTerminated()) {
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

    @Deprecated
    public static boolean parseProducts(long cateId, String categoryIdentity, long pos, long count, BlockingQueue<MySmartPriceUncmpProduct> queue)
            throws HttpFetchException, XPatherException {
        //String url = String.format(AJAX_PRODUCTS_QUERY, categoryIdentity, pos, count);
        String url = AJAX_PRODUCTS_QUERY.replace("$cateIdentifier", categoryIdentity).replace("$start", pos + "").replace("$rowCount", count + "");
        System.out.println(url);
        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        List<TagNode> secNodes = HtmlUtils.getSubNodesByXPath(root, PRODUCT_SECTION);
        if (secNodes != null && secNodes.size() > 0) {
            for (TagNode node : secNodes) {
                // TagNode[] nodes = node.getAllElements(false);
                // if (nodes != null &&nodes.length > 0){
                //   for (TagNode pnode : nodes){
                TagNode imgNode = HtmlUtils.getFirstNodeByXPath(node, PRODUCT_IMG_PATH);
                String imgUrl = "";

                if (imgNode != null) {
                    imgUrl = imgNode.getAttributeByName("src");
                } else {
                    System.out.println("Img node not found!");
                }

                //String productUrl = imgNode.getParent().getAttributeByName("href");

                String name = "";
                String productUrl = "";
                String offerUrl = "";
                TagNode nameNode = HtmlUtils.getFirstNodeByXPath(node, PRODUCT_NAME_PATH);
                if (nameNode == null) {
                    System.out.println("name node null");
                } else {
                    name = nameNode.getText().toString().trim();
                    productUrl = nameNode.getParent().getAttributeByName("href");
                    if (!productUrl.contains("//")) {
                        productUrl = "http://www.mysmartprice.com/" + productUrl;
                    }

                    TagNode detailRoot = HtmlUtils.getUrlRootTagNode(productUrl);
                    TagNode titleNode = HtmlUtils.getFirstNodeByXPath(detailRoot, TITLE_IN_DETAIL);
                    name = titleNode.getText().toString().trim();
                    if (name.length() > 254) {
                        System.out.println(name);
                    }

                    TagNode buyNowNode = HtmlUtils.getFirstNodeByXPath(detailRoot, BUY_NOW_BUTTON);
                    offerUrl = buyNowNode.getAttributeByName("href");
                }

                TagNode priceNode = HtmlUtils.getFirstNodeByXPath(node, PRODUCT_PRICE_PATH);
                String priceStr = priceNode.getText().toString();
                priceStr = priceStr.replace("Rs.", "").trim();
                priceStr = priceStr.replace(",", "");

                Website site = null;
                TagNode offerNode = HtmlUtils.getFirstNodeByXPath(node, PRODUCT_OFFER_PATH);
                if (offerNode != null) {
                    String offer = offerNode.getText().toString();
                    site = Website.valueOf(offer.toUpperCase());
                }

                MySmartPriceUncmpProduct pro = new MySmartPriceUncmpProduct(name, imgUrl, productUrl, Float.parseFloat(priceStr), cateId);
                pro.setSite(site);
                pro.setOfferUrl(offerUrl);
                queue.add(pro);
                //  }

                //}
            }
        }

        return true;
    }

    /**
     * 根据tagNode节点解析页面得到商品信息
     *
     * @param tagNode
     * @return
     */
    public static MySmartPriceUncmpProduct parseUncmpProductByTagNode(TagNode tagNode,long categoryId) throws XPatherException, HttpFetchException {

        String imgUrl = "";
        String name = "";
        String productUrl = "";
        String offerUrl = "";
        Website site = null;

        TagNode imgNode = HtmlUtils.getFirstNodeByXPath(tagNode, PRODUCT_IMG_PATH);
        if (imgNode != null) {
            imgUrl = imgNode.getAttributeByName("src");
        } else {
            System.out.println("Img node not found!");
        }

        TagNode nameNode = HtmlUtils.getFirstNodeByXPath(tagNode, PRODUCT_NAME_PATH);
        if (nameNode == null) {
            System.out.println("name node null");
        } else {
            name = nameNode.getText().toString().trim();
            offerUrl = nameNode.getParent().getAttributeByName("href");
            if (!offerUrl.contains("//")) {
                offerUrl = "http://www.mysmartprice.com/" + offerUrl;
            }

            TagNode detailRoot = HtmlUtils.getUrlRootTagNode(offerUrl);
            TagNode titleNode = HtmlUtils.getFirstNodeByXPath(detailRoot, TITLE_IN_DETAIL);
            name = titleNode.getText().toString().trim();
            if (name.length() > 254) {
                System.out.println(name);
            }

            TagNode buyNowNode = HtmlUtils.getFirstNodeByXPath(detailRoot, BUY_NOW_BUTTON);
            productUrl = buyNowNode.getAttributeByName("href");
            //ex:http://www.mysmartprice.com/out/sendtostore.php?item_id=62242722&url=http://www.flipkart.com/magpie-back-cover-samsung-galaxy-s6/p/itme5nedr62pe49g?pid=ACCE5N98BUFMAUKN&store=flipkart&access_point=desktop&l1=nc&top_category=fashion
            //第一次按照“url=”切分
            //ex:http://www.flipkart.com/magpie-back-cover-samsung-galaxy-s6/p/itme5nedr62pe49g?pid=ACCE5N98BUFMAUKN&store=flipkart&access_point=desktop&l1=nc&top_category=fashion
            //第二次按照“？”号切分
            //ex:http://www.flipkart.com/magpie-back-cover-samsung-galaxy-s6/p/itme5nedr62pe49g
            String[] subStrs = productUrl.split("url=");
            String[] subStrs2 = subStrs[1].split("\\?");
            String[] subStrs3 = subStrs2[0].split("&");
            productUrl = subStrs3[0];
        }

        TagNode priceNode = HtmlUtils.getFirstNodeByXPath(tagNode, PRODUCT_PRICE_PATH);
        String priceStr = priceNode.getText().toString();
        priceStr = priceStr.replace("Rs.", "").trim();
        priceStr = priceStr.replace(",", "");


        TagNode offerNode = HtmlUtils.getFirstNodeByXPath(tagNode, PRODUCT_OFFER_PATH);
        if (offerNode != null) {
            String offer = offerNode.getText().toString();
            site = Website.valueOf(offer.toUpperCase());
        }

        MySmartPriceUncmpProduct pro = new MySmartPriceUncmpProduct(name, imgUrl, productUrl, Float.parseFloat(priceStr), categoryId);
        pro.setSite(site);
        pro.setOfferUrl(offerUrl);

        return pro;
    }


    public static String getTitleFromDetailPage(String url) throws HttpFetchException, XPatherException {
        TagNode root = HtmlUtils.getUrlRootTagNode(url);
        TagNode titleNode = HtmlUtils.getFirstNodeByXPath(root, TITLE_IN_DETAIL);
        return titleNode.getText().toString().trim();
    }

    public static int getProductCount(String pageUrl) {

        List<TagNode> pageNodes = null;
        int productCount = 0;

        try {
            TagNode root = HtmlUtils.getUrlRootTagNode(pageUrl);

            String proCountStr = getSubNodeStringByXPath(root, "//span[@class='num-products']", null);
            if (!StringUtils.isEmpty(proCountStr)) {
                productCount = Integer.parseInt(proCountStr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return productCount;
    }

    public void setExistingProductIds(Set<String> existingProductIds) {
        this.existingProductIds = existingProductIds;
    }
}
