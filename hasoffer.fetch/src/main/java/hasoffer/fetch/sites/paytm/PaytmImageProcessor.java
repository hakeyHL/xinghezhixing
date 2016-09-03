package hasoffer.fetch.sites.paytm;

import com.google.gson.Gson;
import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.fetch.core.IImageProcessor;
import hasoffer.fetch.sites.paytm.model.FetchedProductHelper;
import org.htmlcleaner.TagNode;

/**
 * Created on 2016/7/21.
 */
public class PaytmImageProcessor implements IImageProcessor {

    @Override
    public String getWebsiteImageUrl(String url) throws HttpFetchException, ContentParseException {

        String[] subStrs1 = url.split("\\?");
        url = subStrs1[0];
        //将url更新为返回json的url
        String jsonUrl = url.replace("paytm", "catalog.paytm").replace("shop", "v1");
        TagNode root = HtmlUtils.getUrlRootTagNode(jsonUrl);
        String json = root.getText().toString();

        Gson gson = new Gson();

        FetchedProductHelper summaryProductHelper = gson.fromJson(json, FetchedProductHelper.class);

        return summaryProductHelper.getImage_url();
    }
}
