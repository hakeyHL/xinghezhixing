package hasoffer.fetch.sites.paytm;

import com.google.gson.Gson;
import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.core.IListProcessor;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.model.ListJob;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.PageModel;
import hasoffer.fetch.sites.paytm.model.PaytmProduct;
import hasoffer.fetch.sites.paytm.model.PaytmProductListHelper;
import org.htmlcleaner.TagNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created on 2016/3/15.
 */
public class PaytmListProcessor implements IListProcessor {

    private static final String PAYTM_WEBSITEURL = "https://paytm.com";
    private static final String XPATH_PRODUCT_QUERTRESULT = "//div[@class='Ecart']/h1";
    private static final String XPATH_PRODUCT_QUERTRESULT_NUM = "//span[@ng-if='products.total_count']";
    private static final String XPATH_PRODUCT_SECTION = "//div[@id='productCat']/div";

    @Override
    public void setExistingProductIds(Set<String> existingProductIds) {

    }

    @Override
    public void extractProductJobs(ListJob job) {

    }

    @Override
    public PageModel getPageModel(String pageUrl) {
        return null;
    }

    @Override
    public List getProductByAjaxUrl(String ajaxUrl, Long ptmCateId) throws HttpFetchException, ContentParseException {
        return null;
    }


    /**
     * paytm暂时支持30条，默认请求大小
     *
     * @param keyword
     * @param resultCount
     * @return
     * @throws HttpFetchException
     * @throws ContentParseException
     */
    @Override
    public List<ListProduct> getProductSetByKeyword(String keyword, int resultCount) throws HttpFetchException, ContentParseException {

        keyword = StringUtils.filterAndTrim(keyword, Arrays.asList("(", ")"));

        String queryString = WebsiteHelper.getSearchUrl(Website.PAYTM, StringUtils.urlEncode(keyword));

        queryString = queryString.replace("paytm", "search.paytm");

        TagNode root = HtmlUtils.getUrlRootTagNode(queryString);

        String json = root.getText().toString();

        json = json.replace("typeof angular.callbacks._c === \"function\" && angular.callbacks._c(", "");
        json = json.substring(0, json.length() - 3);
        json = json.trim();

        Gson gson = new Gson();

        PaytmProductListHelper paytmProductListHelper = gson.fromJson(json, PaytmProductListHelper.class);
        String totalString = paytmProductListHelper.getTotal_count();

        int total = Integer.parseInt(totalString);

        ArrayList<ListProduct> thdProductModelList = new ArrayList<ListProduct>();

        if (total == 0) {
            return thdProductModelList;
        }
        if (total < resultCount) {
            resultCount = total;
        }

        List<PaytmProduct> productList = paytmProductListHelper.getGrid_layout();

        for (int i = 0; i < resultCount; i++) {
            ListProduct thdProductModel = new ListProduct();
            PaytmProduct paytmProduct = productList.get(i);
            thdProductModel.setImageUrl(paytmProduct.getImage_url());
            thdProductModel.setWebsite(Website.PAYTM);
            thdProductModel.setUrl(paytmProduct.getUrl().replace("//catalog.paytm.com/v1/", "//paytm.com/shop/"));
            thdProductModel.setSourceId(paytmProduct.getProduct_id());
            thdProductModel.setPrice(Float.parseFloat(paytmProduct.getOffer_price()));
            thdProductModel.setTitle(paytmProduct.getName());
            thdProductModelList.add(thdProductModel);
        }
        return thdProductModelList;
    }


}
