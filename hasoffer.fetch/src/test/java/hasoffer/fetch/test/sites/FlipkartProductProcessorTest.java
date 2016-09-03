package hasoffer.fetch.test.sites;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.IListProcessor;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.sites.flipkart.FlipkartListProcessor;
import hasoffer.fetch.sites.flipkart.FlipkartProductProcessor;
import hasoffer.fetch.sites.flipkart.FlipkartSummaryProductProcessor;
import org.apache.http.conn.ConnectTimeoutException;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

public class FlipkartProductProcessorTest {
    @Test
    public void testCategory() throws Exception {
        String url = "http://www.flipkart.com/casio-ed371-edifice-analog-watch-men/p/itmdj5fcg9yy3bsh?pid=WATDJ5YXMTWKHZAD";

        final String CATE_PATH = "//div[@data-tracking-id='product_breadCrumbs']/ul/li";

        TagNode root = HtmlUtils.getUrlRootTagNode(url);
//        HttpResponseModel responseModel = MyHttpUtils.getByProxy(url, new HttpHost("127.0.0.1", 10080));
//        TagNode root = HtmlUtils.getTagNode(responseModel.getBodyString());

        //获取导航栏
        List<TagNode> catePathList = getSubNodesByXPath(root, CATE_PATH, null);

        //获取导航栏中catePath的长度，取前5位或者更小
        int cateSize = catePathList.size();
        cateSize = cateSize > 6 ? 6 : cateSize;
        if (catePathList.size() == cateSize) {
            cateSize = cateSize - 1;
        }

        long parentId = 0;

        for (int i = 0; i < cateSize; i++) {

            if (i == 0) {//排除导航中的第一个home
                continue;
            }

            try {

                TagNode pathNode = getSubNodeByXPath(catePathList.get(i), "/a", new ContentParseException("path not found"));

                //获取类目名称
                String pathString = StringUtils.filterAndTrim(pathNode.getText().toString(), null);

                System.out.println(String.format("pathString=%s, i=%d", pathString, i));

            } catch (ContentParseException exception) {
                break;
            }

        }
    }

    @Test
    public void testSp() throws ContentParseException, HttpFetchException, ConnectTimeoutException, XPatherException {
        String url = "http://www.flipkart.com/t-edger-ii-rechargeable-cordless-travel-grooming-kit-d3-trimmer-men/p/itme5ypvsc8zeghq";
        FlipkartSummaryProductProcessor processor = new FlipkartSummaryProductProcessor();
        try {
            OriFetchedProduct p = processor.getSummaryProductByUrl(url);
            System.out.println(p.getImageUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void anylizeFlipKart() throws ContentParseException, HttpFetchException, ConnectTimeoutException, XPatherException {
        String url = "http://www.flipkart.com/letv-le-1s/p/itmefe3sztdzw42g";
        FlipkartProductProcessor processor = new FlipkartProductProcessor();
        processor.parseProduct(url);
    }

    @Test
    public void anylizeFlipKartPrice() throws ContentParseException, HttpFetchException, ConnectTimeoutException, XPatherException {
        String url = "http://www.flipkart.com/moto-g-turbo/p/itmecc4uhbue7ve6?pid=MOBECC4UZTSGKWWZ&al=Nej5dgs5x28oxN39aE0jE8ldugMWZuE7Qdj0IGOOVqvJj75%2FXJ058eAccvDWH4GCXVK%2Fbjori2g%3D&ref=L%3A6663500155670581208&srno=b_1&findingMethod=hp_mod";
        FlipkartProductProcessor processor = new FlipkartProductProcessor();
        processor.getPirce(url);
    }

    @Test
    public void TestReg() {
        String pageUrl = "http://www.flipkart.com/moto-g-turbo/p/itmecc4uhbue7ve6?pid=MOBECC4UZTSGKWWZ&al=Nej5dgs5x28oxN39aE0jE8ldugMWZuE7Qdj0IGOOVqvJj75%2FXJ058eAccvDWH4GCXVK%2Fbjori2g%3D&ref=L%3A6663500155670581208&srno=b_1&findingMethod=hp_mod";
        Pattern p = Pattern.compile(".*pid=(\\w+?)(?:&.+)?$");
        //Pattern p = Pattern.compile(".*pid=(\\w+?)&\\w+$");
        Matcher m = p.matcher(pageUrl);
        if (m.matches()) {
            System.out.println(m.group(1));
            System.out.print("success");
        } else {
            System.out.print("failed");
        }
    }

    @Test
    public void testFlipkartAjaxProcessor() throws HttpFetchException, ContentParseException {

        String ajaxUrl = "http://www.flipkart.com/lc/pr/pv1/spotList1/spot1/productList?sid=tyy,4io&start=0";

        IListProcessor listProcessor = new FlipkartListProcessor();

        List<ListProduct> product = listProcessor.getProductByAjaxUrl(ajaxUrl, 45L);

        System.out.println(product.size());
    }
}
