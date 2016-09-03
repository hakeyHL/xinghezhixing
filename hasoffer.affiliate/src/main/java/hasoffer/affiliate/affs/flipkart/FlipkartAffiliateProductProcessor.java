package hasoffer.affiliate.affs.flipkart;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import hasoffer.affiliate.affs.IAffiliateProcessor;
import hasoffer.affiliate.exception.AffiliateAPIException;
import hasoffer.affiliate.model.*;
import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/3/7.
 */
public class FlipkartAffiliateProductProcessor implements IAffiliateProcessor<AffiliateOrder> {

    public static final String R_START_DATE = "startDate";
    public static final String R_END_DATE = "endDate";
    public static final String R_OFFSET = "offset";
    public static final String R_ORDER_STATUS = "status";
    public static final String R_ORDER_STATUS_TENTATIVE = "tentative";
    public static final String R_ORDER_STATUS_APPROVED = "approved";
    public static final String R_ORDER_STATUS_CANCELLED = "cancelled";
    public static final String R_ORDER_STATUS_DISAPPROVED = "disapproved";
    private static final String TRACKINGID = "affiliate357";
    private static final String AFFILIATE_BASE_URL = "https://affiliate-api.flipkart.net/affiliate/api/" + TRACKINGID + ".json";
    private static final String AFFILIATE_KEYWORDQUERY_URL = "https://affiliate-api.flipkart.net/affiliate/search/json";
    private static final String AFFILIATE_PRODUCTID_URL = "https://affiliate-api.flipkart.net/affiliate/1.0/product.json?id=";
    private static Logger logger = LoggerFactory.getLogger(FlipkartAffiliateProductProcessor.class);
    //    private static final String TOKEN_URL = "https://affiliate.flipkart.com/api/a_generateToken";
    private static String TOKEN = "56e46c994b92488c91e43fad138d5c71";

    public static void main(String[] args) throws AffiliateAPIException, IOException {

        String url = "https://affiliate-api.flipkart.net/affiliate/report/orders/detail/json?startDate=2016-05-25&endDate=2016-05-25&status=tentative&offset=1";

        FlipkartAffiliateProductProcessor processor = new FlipkartAffiliateProductProcessor();

        String jsonString = processor.sendRequest(url, null, null);

        System.out.println(jsonString);
    }

    public FlipkartSkuInfo getSkuInfo(String sourceId) throws Exception {
        String queryString = AFFILIATE_PRODUCTID_URL + sourceId;

        String jsonString = sendRequest(queryString, getRequestToken(), null);
        JSONObject obj = JSON.parseObject(jsonString);

        JSONObject jsonProduct = obj.getJSONObject("productBaseInfoV1");

        FlipkartSkuInfo fsi = new FlipkartSkuInfo();

        // other skus
        // productFamily.get()
        JSONArray productFamily = (JSONArray) jsonProduct.get("productFamily");
        int pfSize = productFamily.size();
        String[] pfs = new String[pfSize];
        for (int i = 0; i < pfSize; i++) {
            pfs[i] = productFamily.getString(i).trim();
        }

        // image urls
        FlipkartImageUrls imageUrls = new FlipkartImageUrls();
        JSONObject imageUrlsJson = jsonProduct.getJSONObject("imageUrls");
        imageUrls.setImg200(imageUrlsJson.getString("200x200"));
        imageUrls.setImg400(imageUrlsJson.getString("400x400"));
        imageUrls.setImg800(imageUrlsJson.getString("800x800"));
        imageUrls.setImguk(imageUrlsJson.getString("unknown"));

        // set values
        fsi.setProductId(jsonProduct.getString("productId"));
        fsi.setTitle(jsonProduct.getString("title"));
        fsi.setProductBrand(jsonProduct.getString("productBrand"));
        fsi.setInStock(jsonProduct.getBoolean("inStock"));
        fsi.setProductUrl(jsonProduct.getString("productUrl"));
        fsi.setDesc(jsonProduct.getString("productDescription"));

        fsi.setCategoryPath(jsonProduct.getString("categoryPath"));

        fsi.setProductFamily(pfs);
        fsi.setImageUrls(imageUrls);
        fsi.setFlipkartSellingPrice(jsonProduct.getObject("flipkartSellingPrice", FlipkartPrice.class));
        fsi.setAttributes(jsonProduct.getObject("attributes", FlipkartAttribute.class));

        String modelNum = "", modelName = "", modelId = "";
        JSONObject categorySpecificInfoV1 = obj.getJSONObject("categorySpecificInfoV1");
        if (categorySpecificInfoV1 != null) {
            JSONArray ja = categorySpecificInfoV1.getJSONArray("specificationList");
            int len = ja.size();
            for (int i = 0; i < len; i++) {
                JSONObject oo = ja.getJSONObject(i);
                String key = oo.getString("key");
                if ("GENERAL FEATURES".equalsIgnoreCase(key) || "General".equalsIgnoreCase(key)) {
                    JSONArray oos = oo.getJSONArray("values");
                    int len2 = oos.size();
                    for (int j = 0; j < len2; j++) {
                        JSONObject ooso = (JSONObject) oos.get(j);
                        String oosokey = ooso.getString("key");
                        if ("Model Number".equalsIgnoreCase(oosokey)) {
                            modelNum = StringUtils.arrayToString(ooso.getJSONArray("value").toArray(new String[0]), "");
                        }

                        if ("Model Name".equalsIgnoreCase(oosokey)) {
                            modelName = StringUtils.arrayToString(ooso.getJSONArray("value").toArray(new String[0]), "");
                        }

                        if ("Model ID".equalsIgnoreCase(oosokey)) {
                            modelId = StringUtils.arrayToString(ooso.getJSONArray("value").toArray(new String[0]), "");
                        }
                    }
                }
            }
        }
        fsi.setModelNum(modelNum);
        fsi.setModelName(modelName);
        fsi.setModelId(modelId);

        return fsi;
    }

    /**
     * 获取一个动态的token值
     *
     * @return
     */
    public String getAffiliateToken() {
        return TOKEN;
    }

    @Override
    public List<AffiliateOrder> getAffiliateOrderList(Map<String, String> headerMap, Map<String, String> parameterMap) {
        String url = "https://affiliate-api.flipkart.net/affiliate/report/orders/detail/json";

        //headerMap.put("Fk-Affiliate-Token", getAffiliateToken());
        //headerMap.put("Fk-Affiliate-Id", TRACKINGID);

        try {
            String respJson = sendRequest(url, headerMap, parameterMap);
            Gson gson = new Gson();
            AffiliateOrderReport report = gson.fromJson(respJson, AffiliateOrderReport.class);
            List<AffiliateOrder> orderList = new ArrayList<AffiliateOrder>();
            if (report.getOrderList() != null) {
                orderList.addAll(report.getOrderList());
            }
            while (true) {
                if (!"".equals(report.getNext()) && parameterMap.get(R_OFFSET) != null) {
                    parameterMap.put(R_OFFSET, String.valueOf(Integer.valueOf(parameterMap.get(R_OFFSET)) + 500));
                    TimeUnit.SECONDS.sleep(5);
                    respJson = sendRequest(url, headerMap, parameterMap);
                    report = gson.fromJson(respJson, AffiliateOrderReport.class);
                    if (report.getOrderList() != null) {
                        orderList.addAll(report.getOrderList());
                    }
                }
                if ("".equals(report.getNext())) {
                    break;
                }

            }

            for (AffiliateOrder order : orderList) {
                order.setAffID(headerMap.get("Fk-Affiliate-Id"));
                logger.info("order.affId={}, order.title={})", headerMap.get("Fk-Affiliate-Id"), order.getTitle().length());
                if (order.getStatus() == null) {
                    order.setStatus(parameterMap.get(R_ORDER_STATUS));
                }
            }
            return orderList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    /**
     * 访问URLString获得json响应数据的方法，适用于访问categoryUrl、productUrl
     *
     * @param urlString
     * @return
     * @throws AffiliateAPIException
     */
    @Override
    public String sendRequest(String urlString, Map<String, String> headerMap, Map<String, String> paramMap) throws AffiliateAPIException, IOException {

        if (headerMap == null) {
            headerMap = new HashMap<String, String>();
        }

        HttpResponseModel responseModel = HttpUtils.get(urlString, headerMap, paramMap);

        int status = responseModel.getStatusCode();

        switch (status) {

            case HttpURLConnection.HTTP_GONE:
                // The timestamp is expired.
                throw new AffiliateAPIException("URL expired " + urlString);

            case HttpURLConnection.HTTP_UNAUTHORIZED:
                // The API Token or the Tracking ID is invalid.
                throw new AffiliateAPIException("API Token:" + headerMap.get("Fk-Affiliate-Token") + " or Affiliate Tracking ID:" + headerMap.get("Fk-Affiliate-Id") + " invalid.");

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

    /**
     * 获取分类集合
     *
     * @return
     */
    public List<AffiliateCategory> getProductDirectory() throws
            AffiliateAPIException, IllegalAccessException, InstantiationException, IOException {

        List<AffiliateCategory> categoryList = new ArrayList<AffiliateCategory>();

        // Query the API service and get back the result.
        String jsonData = sendRequest(AFFILIATE_BASE_URL, null, null);

        // Bookkeep the retrieved data in a local productDirectory Map.
        JSONObject obj = JSONObject.parseObject(jsonData);
        JSONObject listing = obj.getJSONObject("apiGroups").getJSONObject("affiliate").getJSONObject("apiListings");

        for (JSONObject.Entry<String, Object> category : listing.entrySet()) {

            AffiliateCategory affCategory = new AffiliateCategory();
            String category_name = category.getKey();
            JSONObject variants = listing.getJSONObject(category_name).getJSONObject("availableVariants");

            // Sort the variants and get the latest version
            List<String> variant_keys = new ArrayList<String>();
            for (JSONObject.Entry<String, Object> categoryInfo : variants.entrySet()) {
                variant_keys.add(categoryInfo.getKey());
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

        List<AffiliateProduct> thdProductList = new ArrayList<AffiliateProduct>();

        while (nextUrl != null && !nextUrl.isEmpty()) {
            String jsonData = sendRequest(nextUrl, null, null);

            JSONObject obj = JSON.parseObject(jsonData);
            JSONArray productInfoArray = obj.getJSONArray("productInfoList");

            for (int i = 0; i < productInfoArray.size(); i++) {

                JSONObject productInfo = productInfoArray.getJSONObject(i);

                String sourceId = (String) productInfo.getJSONObject("productBaseInfo").getJSONObject("productIdentifier").get("productId");

                JSONObject productAttributes = productInfo.getJSONObject("productBaseInfo").getJSONObject("productAttributes");
                String title = (String) productAttributes.get("title");

                JSONObject imageUrlObj = productAttributes.getJSONObject("imageUrls");
                String imageUrl = (String) imageUrlObj.get("400x400");

                float price = productAttributes.getJSONObject("sellingPrice").getFloatValue("amount");

                String url = (String) productAttributes.get("productUrl");

                AffiliateProduct affiliateProduct = new AffiliateProduct();
                affiliateProduct.setWebsite(Website.FLIPKART);
                affiliateProduct.setImageUrl(imageUrl);
                affiliateProduct.setSourceId(sourceId);
                affiliateProduct.setPrice(price);
                affiliateProduct.setUrl(url);
                affiliateProduct.setTitle(title);

                thdProductList.add(affiliateProduct);
                logger.debug("product add success " + affiliateProduct.getSourceId());
            }
        }

        return thdProductList;
    }

    @Override
    public List<String> getProductNextUrlList(String productUrl) throws AffiliateAPIException, IOException {

        List<String> nextUrlList = new ArrayList<String>();
        getProductNextUrl(productUrl, nextUrlList);

        return nextUrlList;
    }

    /**
     * @param keyword
     * @param resultNum api-每次搜索最多返回10个商品
     * @return
     * @throws AffiliateAPIException
     * @throws IOException
     */
    @Override
    public List<AffiliateProduct> getAffiliateProductByKeyword(String keyword, int resultNum) throws AffiliateAPIException, IOException {
//https://affiliate-api.flipkart.net/affiliate/search/json
        String queryString = AFFILIATE_KEYWORDQUERY_URL + "?query=" + StringUtils.urlEncode(keyword) + "&resultCount=" + resultNum;

        String jsonData = sendRequest(queryString, getRequestToken(), null);

        JSONObject jsonObject = JSONObject.parseObject(jsonData);

        JSONArray productArray = jsonObject.getJSONArray("productInfoList");

        List<AffiliateProduct> affiliateProductList = new ArrayList<AffiliateProduct>();

        for (int i = 0; i < productArray.size(); i++) {//size = 1

            AffiliateProduct affiliateProduct = new AffiliateProduct();
            String sourceId = productArray.getJSONObject(i).getJSONObject("productBaseInfo").getJSONObject("productIdentifier").getString("productId");

            JSONObject attributes = productArray.getJSONObject(i).getJSONObject("productBaseInfo").getJSONObject("productAttributes");

            String title = attributes.getString("title");

            JSONObject imageUrlsObject = attributes.getJSONObject("imageUrls");
            String imageUrl = imageUrlsObject.getString("400x400");

            float price = 0.0f;
            JSONObject priceObject = attributes.getJSONObject("sellingPrice");
            if (priceObject != null) {
                String priceString = priceObject.getString("amount");
                price = Float.parseFloat(priceString);
            }


            String url = attributes.getString("productUrl");
            url = url.replace("http://dl.flipkart.com/dl/", "http://www.flipkart.com/");

            affiliateProduct.setUrl(url);
            affiliateProduct.setWebsite(Website.FLIPKART);
            affiliateProduct.setImageUrl(imageUrl);
            affiliateProduct.setSourceId(sourceId);
            affiliateProduct.setPrice(price);
            affiliateProduct.setTitle(title);

            affiliateProductList.add(affiliateProduct);
        }

        return affiliateProductList;
    }

    private Map<String, String> getRequestToken() {
        Map<String, String> headerMap = new HashMap<String, String>();

        headerMap.put("Fk-Affiliate-Token", getAffiliateToken());
        headerMap.put("Fk-Affiliate-Id", TRACKINGID);

        return headerMap;
    }

    // 测试结果：价格采用flipkartSpecialPrice，有可能返回结果为null，此时商品为offsale状态
    @Override
    public AffiliateProduct getAffiliateProductBySourceId(String sourceId) throws AffiliateAPIException, IOException {

        String queryString = AFFILIATE_PRODUCTID_URL + sourceId;

        Map<String, String> headerMap = new HashMap<String, String>();

        headerMap.put("Fk-Affiliate-Token", getAffiliateToken());
        headerMap.put("Fk-Affiliate-Id", TRACKINGID);

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

    private void getProductNextUrl(String productUrl, List<String> nextUrlList) throws AffiliateAPIException, IOException {

        String jsonData = sendRequest(productUrl, null, null);
        JSONObject obj = JSON.parseObject(jsonData);
        String nextUrl = obj.getString("nextUrl");

        if (nextUrl == null) {
            return;
        }

        nextUrlList.add(nextUrl);
        logger.debug("add nextUrl success " + nextUrl);
        getProductNextUrl(nextUrl, nextUrlList);
    }

    public String getCatePath(String sourceId) throws IOException, AffiliateAPIException {

        String queryString = AFFILIATE_PRODUCTID_URL + sourceId;

        Map<String, String> headerMap = new HashMap<String, String>();

        headerMap.put("Fk-Affiliate-Token", getAffiliateToken());
        headerMap.put("Fk-Affiliate-Id", TRACKINGID);

        String jsonDate = sendRequest(queryString, headerMap, null);

        JSONObject obj = JSON.parseObject(jsonDate);

        JSONObject jsonProduct = obj.getJSONObject("productBaseInfoV1");

        String path = jsonProduct.getString("categoryPath");

        return path;
    }
}



