package hasoffer.core.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by hs on 2016/7/4.
 */
public class Httphelper {
    public static void main(String[] args) {
        try {
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("Accept", "application/json, text/javascript, */*;");
//            map.put("request-token", "290D25B9FC9EC9928CD715CCDBE93959   requestversion  1");
//            map.put("deviceinfo", "{     \"deviceId\": \"dfecc858243a616a\",     \"imeiId\": \"359786058646838\",     \"deviceName\": \"samsung SM-N9006 h3gzc\",     \"brand\": \"samsung\",     \"osVersion\": \"5.0\",     \"serial\": \"b29597d8\",     \"appVersion\": \"11\",     \"screen\": \"1920x1080\",     \"curShopApp\": \"FLIPKART\",     \"screenSize\": \"5.6939884085129915\",     \"ramSize\": \"2972520448\",     \"curNetState\": \"wifi\",     \"appType\": \"APP\",     \"marketChannel\": \"GOOGLEPLAY\" }");
//            String result = Httphelper.doGetWithHeaer("http://api.hasoffer.com/app/productsList?type=0", map);
//            System.out.println(result);
            Map<String, String> map = new HashMap<String, String>();
            map.put("Authorization", "key=AIzaSyCZrHjOkZ57j3Dvq_TpvYW8Mt38Ej1dzQA");
            String result = Httphelper.doPostJsonWithHeader("https://gcm-http.googleapis.com/gcm/send", "{\n" +
                    "    \"to\": \"c4gUXbiRD2Y:APA91bFJd4v3ktjpEZtBBhryPLABiFR2jRbimm-onYfP6Vf_tToJibMfqFpgcHzTr99epCbVhyWkMe9mOdXg7IavkHsBe_TGKl8lmnMOVBIiItkXlvJ2uzUj7dWHr2zBufrCT7mXlPP-\",\n" +
                    "    \"data\": {\n" +
                    "        \"score\": \"5x1\",\n" +
                    "        \"time\": \"15:10\",\n" +
                    "        \"message\": {\n" +
                    "            \"display\": {\n" +
                    "                \"outTitle\": \"accessibility2\",\n" +
                    "                \"title\": \"accessibility2\",\n" +
                    "                \"content\": \"accessibility2\"\n" +
                    "            },\n" +
                    "            \"click\": {\n" +
                    "                \"type\": \"PRODUCT\",\n" +
                    "                \"url\": \"3198\",\n" +
                    "                \"packageName\": \"com.flipkart.android\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}", map);
            System.out.println(result);

        } catch (Exception e) {
            System.out.println("e");
        }
    }

    public static String doPost(String url, List<NameValuePair> nvps) throws Exception {
        String responseText = null;
        CloseableHttpClient closeableHttpClient = createHttpsClient();
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        HttpResponse httpResponse = closeableHttpClient.execute(httppost);
        HttpEntity httpEntity2 = httpResponse.getEntity();
        System.out.println("httpResponse.getStatusLine().getStatusCode():" + httpResponse.getStatusLine().getStatusCode());
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(httpEntity2);
            responseText = result;
        } else {
            String result = EntityUtils.toString(httpEntity2);
            responseText = result;
        }
        closeableHttpClient.close();
        return responseText;
    }

    public static String doPost(String url, String json) throws Exception {
        String responseText = null;
        CloseableHttpClient closeableHttpClient = createHttpsClient();
        HttpPost method = new HttpPost(url);
        StringEntity entity = new StringEntity(json, "utf-8");// 解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        method.setEntity(entity);
        HttpResponse httpResponse = closeableHttpClient.execute(method);
        HttpEntity httpEntity2 = httpResponse.getEntity();
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(httpEntity2);
            responseText = result;
        }
        closeableHttpClient.close();
        return responseText;
    }

    public static String doPostJsonWithHeader(String url, String json, Map<String, String> headers) throws Exception {
        String responseText = null;
        CloseableHttpClient closeableHttpClient = createHttpsClient();
        HttpPost method = new HttpPost(url);
        Httphelper.addHeaders2Meethod(method, headers);
        StringEntity entity = new StringEntity(json, "utf-8");// 解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        method.setEntity(entity);
        HttpResponse httpResponse = closeableHttpClient.execute(method);
        HttpEntity httpEntity2 = httpResponse.getEntity();
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(httpEntity2);
            responseText = result;
        }
        closeableHttpClient.close();
        return responseText;
    }

    public static String doGetWithHeaer(String url, Map<String, String> headers) throws Exception {
        String responseText = null;
        CloseableHttpClient closeableHttpClient = createHttpsClient();
        HttpGet method = new HttpGet(url);
        Httphelper.addHeaders2Meethod(method, headers);
        HttpResponse httpResponse = closeableHttpClient.execute(method);
        HttpEntity httpEntity2 = httpResponse.getEntity();
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(httpEntity2);
            responseText = result;
        }
        closeableHttpClient.close();
        return responseText;
    }


    public static CloseableHttpClient createHttpsClient() throws Exception {
        X509TrustManager x509mgr = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] xcs, String string) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] xcs, String string) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{x509mgr}, new java.security.SecureRandom());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

    public static void addHeaders2Meethod(HttpRequestBase httpRequestBase, Map<String, String> headers) {
        Set<Map.Entry<String, String>> set = headers.entrySet();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            httpRequestBase.addHeader(entry.getKey(), entry.getValue());
        }
    }
}
