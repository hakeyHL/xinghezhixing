package hasoffer.affiliate.affs.shopclues;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import hasoffer.affiliate.affs.IAffiliateProcessor;
import hasoffer.affiliate.affs.shopclues.model.ShopcluesOrder;
import hasoffer.affiliate.exception.AffiliateAPIException;
import hasoffer.affiliate.model.AffiliateCategory;
import hasoffer.affiliate.model.AffiliateProduct;
import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.http.HttpUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopcluesProductProcessor implements IAffiliateProcessor<ShopcluesOrder> {

    private static final String AFFILIATE_PRODUCTID_URL = "http://developer.shopclues.com/api/v1/product/";
    private static final String AUTHORIZATUIB = "Bearer bc1f461de4f193";
    private static final String CONTENT_TYPE = "application/json";
    private static String AffiliateID = "2892";
    private static String APIKey = "nVqV7Uh2Aj9Vw033EnmoXA";

    @Override
    public String sendRequest(String url, Map<String, String> headerMap, Map<String, String> parameterMap) throws AffiliateAPIException, IOException {
        if (headerMap == null) {
            headerMap = new HashMap<String, String>();
        }
        //headerMap.put("api_key", APIKey);
        //headerMap.put("affiliate_id", AffiliateID);
        HttpResponseModel responseModel = HttpUtils.get(url, headerMap, parameterMap);

        int status = responseModel.getStatusCode();

        switch (status) {

            case HttpURLConnection.HTTP_GONE:
                // The timestamp is expired.
                throw new AffiliateAPIException("URL expired " + url);

            case HttpURLConnection.HTTP_UNAUTHORIZED:
                // The API Token or the Tracking ID is invalid.
                throw new AffiliateAPIException("API Token or Affiliate Tracking ID invalid.");

            case HttpURLConnection.HTTP_FORBIDDEN:
                // Tampered URL, i.e., there is a signature mismatch.
                // The URL contents are modified from the originally returned value.
                throw new AffiliateAPIException("Tampered URL - The URL contents are modified from the originally returned value");

            case HttpURLConnection.HTTP_OK:
                return responseModel.getBodyString();

            default:
                throw new AffiliateAPIException("Connection error with the Affiliate API service: HTTP/" + status);
        }

    }

    @Override
    public List<ShopcluesOrder> getAffiliateOrderList(Map<String, String> headerMap,Map<String, String> parameterMap) {
        String requestUrl = "https://affiliatelogin.shopclues.com/affiliates/api/2/reports.asmx/OrderDetailChanges";
        try {
            sendRequest(requestUrl, headerMap, parameterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getOrderList(Date beginDate, Date endDate, int startRow, int rowLimit, SortField sortField, boolean sortDesc) throws AffiliateAPIException, IOException {
        String requestUrl = "https://affiliatelogin.shopclues.com/affiliates/api/2/reports.asmx/OrderDetailChanges";
        Map<String, String> parameterMap = new HashMap<String, String>();
        return sendRequest(requestUrl, null, parameterMap);
    }

    @Override
    public String getAffiliateToken() throws IOException {
        return null;
    }

    @Override
    public List<AffiliateCategory> getProductDirectory() throws AffiliateAPIException, IllegalAccessException, InstantiationException, IOException {
        return null;
    }

    @Override
    public List<AffiliateProduct> getProductList(String nextUrl) throws AffiliateAPIException, IOException {
        return null;
    }

    @Override
    public List<String> getProductNextUrlList(String productUrl) throws AffiliateAPIException, IOException {
        return null;
    }

    @Override
    public List<AffiliateProduct> getAffiliateProductByKeyword(String keyword, int resultNum) throws AffiliateAPIException, IOException {
        return null;
    }

    @Override
    public AffiliateProduct getAffiliateProductBySourceId(String sourceId) throws AffiliateAPIException, IOException {

        String queryString = AFFILIATE_PRODUCTID_URL + sourceId;

        Map<String,String> headerMap = new HashMap<String, String>();
        headerMap.put("Authorization", AUTHORIZATUIB);
        headerMap.put("Content-Type", CONTENT_TYPE);

        String jsonDate = sendRequest(queryString, headerMap, null);

        JSONObject obj = JSON.parseObject(jsonDate);

        JSONObject jsonProduct = obj.getJSONObject("productBaseInfoV1");

        String title = jsonProduct.getString("title");
        String url = jsonProduct.getString("productUrl");
        url = url.replace("http://dl.flipkart.com/dl/", "http://www.flipkart.com/");
        String productStatus = jsonProduct.getString("inStock");
        String priceString = jsonProduct.getJSONObject("flipkartSpecialPrice").getString("amount");
        float price = 0.0f;
        if (StringUtils.isEmpty(priceString)) {
            price = Float.parseFloat(priceString);
        }

        String imageUrl = jsonProduct.getJSONObject("imageUrls").getString("400x400");

        //filpkart联盟解析 instock状态返回false，暂认定是offsale状态
        //flipkart   instock返回true onsale    返回false outstock  2016-05-27 flipkart 1.0 api

        AffiliateProduct affiliateProduct = new AffiliateProduct();
        affiliateProduct.setProductStatus(productStatus);
        affiliateProduct.setPrice(price);
        affiliateProduct.setWebsite(Website.FLIPKART);
        affiliateProduct.setImageUrl(imageUrl);
        affiliateProduct.setSourceId(sourceId);
        affiliateProduct.setTitle(title);
        affiliateProduct.setUrl(url);

        return affiliateProduct;
    }

    public static enum SortField {
        conversion_id, order_id, sku_code
    }
}
