package hasoffer.fetch.test.sites.mysmartprice;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.fetch.model.ListJob;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.PageModel;
import hasoffer.fetch.sites.mysmartprice.MspCategoryProcessor;
import hasoffer.fetch.sites.mysmartprice.MspHelper;
import hasoffer.fetch.sites.mysmartprice.MspListProcessor;
import hasoffer.fetch.sites.mysmartprice.NewMspSkuCompareProcessor;
import hasoffer.fetch.sites.mysmartprice.model.MySmartPriceCategory;
import hasoffer.fetch.sites.mysmartprice.model.MySmartPriceCmpSku;
import hasoffer.fetch.sites.mysmartprice.model.MySmartPriceProduct;
import org.junit.Test;

import java.util.List;

/**
 * Created by chevy on 2015/12/3.
 */
public class MySmartPriceProcessorTest {

    @Test
    public void f0() {
        String url = "http://www.mysmartprice.com/mobile/apple-iphone-5s-msp3216";
        NewMspSkuCompareProcessor processor = new NewMspSkuCompareProcessor();
        try {
            MySmartPriceProduct p = processor.parse(url);
            List<MySmartPriceCmpSku> cmpSkus = p.getCmpSkus();
            for(MySmartPriceCmpSku cmpSku : cmpSkus){
                System.out.println(cmpSku.getWebsite() + " - " + cmpSku.getUrl());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void f1() {
//		String url = "http://www.mysmartprice.com/accessories_nc/cases-covers";
        String url = "http://www.mysmartprice.com/mobile/pricelist/mobile-price-list-in-india.html";
        MspListProcessor listProcessor = new MspListProcessor();
        try {
            ListJob lj = new ListJob(null, url);
            PageModel pm = listProcessor.getPageModel(url);
            listProcessor.extractProductJobs(lj);

            System.out.println(lj.getProductJobs().size());
        } catch (Exception e) {

        }
    }

    @Test
    public void f2() {
        MspCategoryProcessor categoryProcessor = new MspCategoryProcessor();
        List<MySmartPriceCategory> categories = categoryProcessor.parseCategories();

        for (MySmartPriceCategory category : categories) {
            System.out.println(category);
            List<MySmartPriceCategory> subs = category.getSubCategories();
            if (ArrayUtils.isNullOrEmpty(subs)) {
                continue;
            }
            for (MySmartPriceCategory sub : subs) {
                System.out.println(sub);
            }
        }
    }

    @Test
    public void testUrl() {
        String url = "/accessories_nc/products/61752688";
        System.out.println(MspHelper.getProductIdByUrl(url));
        ///(\\d+)

    }

    @Test
    public void testGetProductByKeyword() throws HttpFetchException, ContentParseException {

        MspListProcessor listProcessor = new MspListProcessor();
        List<ListProduct> iphoneProduct = listProcessor.getProductSetByKeyword("iphone", 5);
        System.out.println(iphoneProduct);

    }
}
