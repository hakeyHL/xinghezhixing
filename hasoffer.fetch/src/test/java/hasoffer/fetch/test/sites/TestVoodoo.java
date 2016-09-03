package hasoffer.fetch.test.sites;

import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.utils.http.HttpUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Date : 2016/5/3
 * Function :
 */
public class TestVoodoo {

    public static void main(String[] strings) {

        String json = "{\"cache\":true,\"currentPrice\":\"Rs. 13,590\",\"merchant\":\"snapdeal\",\"originalPrice\":\"Rs. 13,590\",\"pid\":\"\",\"title\":\"UNBOXED OnePlus X (16 GB-Onyx\"}";

        Map<String, String> header = new HashMap<String, String>();
        header.put("key", a(a(a(String.valueOf(json.length())))));
//        header.put("user-id", "b595aca2c906f99");
//        header.put("voodoo-version-code", "134");
//        header.put("voodoo-version-name", " 0.0.13");
//        header.put("User-Agent", "okhttp/2.4.0");
        header.put("Content-Type", "application/json; charset=UTF-8");

        HttpResponseModel responseModel = HttpUtils.postByRaw("http://api.getvoodoo.in/rest/sherlock/similar-products", json, header);

        System.out.println(responseModel.getBodyString());

        /*String json = "{\"cache\":true,\"currentPrice\":\"Rs. 13,590\",\"merchant\":\"snapdeal\",\"originalPrice\":\"Rs. 13,590\",\"pid\":\"\",\"title\":\"UNBOXED OnePlus X (16 GB-Onyx)\"}";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.getvoodoo.in/rest/sherlock/similar-products")
                .post(body)
                .addHeader("key", a(a(a(String.valueOf(143)))))
                .addHeader("user-id", "b595aca2c906f99")
                .addHeader("voodoo-version-code", "134")
                .addHeader("voodoo-version-name", " 0.0.13").build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.print(response.body().string());
            } else {
                System.out.print(response + "error");
            }
        } catch (Exception e) {
            System.out.print(e.getLocalizedMessage());
        }*/
    }

    public static String a(String paramString) {
        try {
            String str;
            for (paramString = new BigInteger(1, MessageDigest.getInstance("MD5").digest(paramString.getBytes())).toString(16); ; paramString = "0" + paramString) {
                str = paramString;
                if (paramString.length() >= 32) {
                    break;
                }
            }
            return str;
        } catch (NoSuchAlgorithmException s) {
            return null;
        }
    }

}
