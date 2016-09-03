package hasoffer.affiliate.affs.snapdeal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hasoffer.affiliate.affs.IAffiliateProcessor;
import hasoffer.affiliate.affs.snapdeal.model.SnapDealAffiliateOrder;
import hasoffer.affiliate.affs.snapdeal.model.SnapdealOrderReport;
import hasoffer.affiliate.exception.AffiliateAPIException;
import hasoffer.affiliate.model.AffiliateCategory;
import hasoffer.affiliate.model.AffiliateProduct;
import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.model.Website;
import hasoffer.base.utils.http.HttpUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;

/**
 * Created on 2016/3/7.
 */
public class SnapdealProductProcessor implements IAffiliateProcessor<SnapDealAffiliateOrder> {

    private static final String TRACKINGID = "82856";
    private static final String TOEKN = "09bf4a55fafe2ccc3c077e2ea48642";
    private static final String AFFILIATE_BASE_URL = "http://affiliate-feeds.snapdeal.com/feed/" + TRACKINGID + ".json";
    private static final String AFFILIATE_PRODUCTDETAIL_URL = "http://affiliate-feeds.snapdeal.com/feed/product?id=";

    public static final String R_START_DATE = "startDate";
    public static final String R_END_DATE = "endDate";
    public static final String R_ORDER_STATUS = "status";

    public static final String R_ORDER_STATUS_APPROVED = "approved";
    public static final String R_ORDER_STATUS_CANCELLED = "cancelled";
    //public static final String R_ORDER_STATUS_TENTATIVE = "approved";



    @Override
    public String getAffiliateToken() {
        return TOEKN;
    }

    @Override
    public List<AffiliateCategory> getProductDirectory() throws AffiliateAPIException, IllegalAccessException, InstantiationException {

        List<AffiliateCategory> categoryList = new ArrayList<AffiliateCategory>();

        String jsonData = sendRequest(AFFILIATE_BASE_URL, null, null);

        JSONObject obj = JSONObject.parseObject(jsonData);
        JSONObject listing = obj.getJSONObject("apiGroups").getJSONObject("Affiliate").getJSONObject("listingsAvailable");

        for (JSONObject.Entry<String, Object> category : listing.entrySet()) {

            AffiliateCategory affCategory = new AffiliateCategory();
            String category_name = category.getKey();
            JSONObject variants = listing.getJSONObject(category_name).getJSONObject("listingVersions");

            List<String> variant_keys = new ArrayList<String>();
            for (JSONObject.Entry<String, Object> categoryVersionNum : variants.entrySet()) {
                variant_keys.add(categoryVersionNum.getKey());
            }
            Collections.sort(variant_keys, Collections.reverseOrder());

            String category_url = variants.getJSONObject(variant_keys.get(0)).getString("get");

            affCategory.setUrl(category_url);
            affCategory.setName(category_name);
            affCategory.setParentId(0);

            categoryList.add(affCategory);
        }

        return categoryList;
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
    public AffiliateProduct getAffiliateProductBySourceId(String sourceId) throws AffiliateAPIException {

        Map<String, String> map = new HashMap<String, String>();
        map.put("Accept", "application/json");

        String jsonDate = sendRequest(AFFILIATE_PRODUCTDETAIL_URL + sourceId, map, null);

        JSONObject obj = JSON.parseObject(jsonDate);

        AffiliateProduct affiliateProduct = new AffiliateProduct();

        String productStatus = obj.getString("availability");
        if ("in stock".equals(productStatus)) {
            affiliateProduct.setProductStatus("true");
        } else {
            affiliateProduct.setProductStatus("false");
        }

        String priceString = obj.getString("effectivePrice");
        float price = Float.parseFloat(priceString);
        affiliateProduct.setPrice(price);

        String imageUrl = obj.getString("imageLink");
        affiliateProduct.setImageUrl(imageUrl);

        String title = obj.getString("title");
        affiliateProduct.setTitle(title);

        String url = obj.getString("link");
        affiliateProduct.setUrl(url);

        affiliateProduct.setSourceId(sourceId);
        affiliateProduct.setWebsite(Website.SNAPDEAL);
        return affiliateProduct;
    }

    @Override
    public String sendRequest(String urlString, Map<String, String> headerMap, Map<String, String> parameterMap) throws AffiliateAPIException {

        if (headerMap == null) {
            headerMap = new HashMap<String, String>();
        }

        HttpResponseModel responseModel = HttpUtils.get(urlString, headerMap, parameterMap);

        int status = responseModel.getStatusCode();

        switch (status) {

            case HttpURLConnection.HTTP_GONE:
                // The timestamp is expired.
                throw new AffiliateAPIException("URL expired");

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
    public List<SnapDealAffiliateOrder> getAffiliateOrderList(Map<String, String> headerMap,Map<String, String> parameterMap) {
        String url = "affiliate-feeds.snapdeal.com/feed/api/order";
        try {
            String respJson = sendRequest(url, headerMap, parameterMap);
            Gson gson = new GsonBuilder().setDateFormat("MM/dd/yyyy HH:mm:ss").create();
            SnapdealOrderReport report = gson.fromJson(respJson, SnapdealOrderReport.class);

            List<SnapDealAffiliateOrder> productDetails = report.getProductDetails();
            if(productDetails!=null) {
                for (SnapDealAffiliateOrder order : productDetails) {
                    if (order.getStatus() == null) {
                        order.setStatus(parameterMap.get(R_ORDER_STATUS));
                    }
                    //long x= TimeUtils.MILLISECONDS_OF_1_DAY*30;
                    //if (R_ORDER_STATUS_APPROVED.equals(order.getStatus()) && new Date().getTime() - order.getDateTime().getTime() < x) {
                    //    order.setStatus(R_ORDER_STATUS_APPROVED);
                    //}
                }
            }
            return productDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<SnapDealAffiliateOrder>();
        }
    }
}
