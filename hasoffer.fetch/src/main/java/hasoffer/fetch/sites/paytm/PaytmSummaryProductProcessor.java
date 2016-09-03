package hasoffer.fetch.sites.paytm;

import com.google.gson.Gson;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.core.ISummaryProductProcessor;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.sites.paytm.model.FetchedProductHelper;
import org.htmlcleaner.TagNode;

/**
 * Created on 2016/2/29.
 */
public class PaytmSummaryProductProcessor implements ISummaryProductProcessor {

    @Override
    public OriFetchedProduct getSummaryProductByUrl(String url) throws HttpFetchException {

        OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();

        String[] subStrs1 = url.split("\\?");
        url = subStrs1[0];
        //将url更新为返回json的url
        String jsonUrl = url.replace("paytm", "catalog.paytm").replace("shop", "v1");
        TagNode root = HtmlUtils.getUrlRootTagNode(jsonUrl);
        String json = root.getText().toString();

        Gson gson = new Gson();

        FetchedProductHelper summaryProductHelper = gson.fromJson(json, FetchedProductHelper.class);
        float price = Float.parseFloat(summaryProductHelper.getOffer_price().trim());

        oriFetchedProduct.setImageUrl(summaryProductHelper.getImage_url());
        oriFetchedProduct.setPrice(price);
        oriFetchedProduct.setProductStatus(summaryProductHelper.getInstock() == "true" ? ProductStatus.ONSALE : ProductStatus.OUTSTOCK);
        oriFetchedProduct.setTitle(summaryProductHelper.getName());
        oriFetchedProduct.setSourceSid(summaryProductHelper.getParent_id());
        oriFetchedProduct.setWebsite(WebsiteHelper.getWebSite(url));
        oriFetchedProduct.setUrl(url);
        oriFetchedProduct.setWebsite(Website.PAYTM);

        return oriFetchedProduct;
    }

    private float getPriceByPriceString(String priceString) {

        String[] subStrs1 = priceString.split("Rs");
        String priceStr = subStrs1[1].replace(",", "").replace(" ", "");
        float price = Float.parseFloat(priceStr);

        return price;
    }
}
