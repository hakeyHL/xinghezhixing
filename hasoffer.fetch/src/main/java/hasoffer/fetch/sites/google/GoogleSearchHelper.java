package hasoffer.fetch.sites.google;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.utils.http.HttpUtils;
import hasoffer.fetch.sites.google.model.GoogleSearchResult;

import java.io.IOException;
import java.util.List;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/25
 */
public class GoogleSearchHelper {

	private static final String GOOGLE_API_AJAX = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=QUERY_STRING";

	public static List<GoogleSearchResult> search(List<String> keywords) throws IOException {

		String url = getUrl(keywords);

		HttpResponseModel responseModel = HttpUtils.get(url, null);

		JSONObject jsonObj = null;
		List<GoogleSearchResult> searchResults = null;
		if (responseModel.isOk()) {
			jsonObj = JSON.parseObject(responseModel.getBodyString());

			int status = jsonObj.getInteger("responseStatus");

			if (status != 200) {
				System.out.println(responseModel.getBodyString());
				return searchResults;
			}

			JSONObject responseData = jsonObj.getJSONObject("responseData");
			if (responseData == null) {
				System.out.println("search none result ,status = " + status);
			}
			JSONArray results = responseData.getJSONArray("results");

			searchResults = JSON.parseArray(JSON.toJSONString(results), GoogleSearchResult.class);
		}

		return searchResults;
	}

	private static String getUrl(List<String> keywords) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0, len = keywords.size(); i < len; i++) {
			sb.append(keywords.get(i));
			if (i != len - 1) {
				sb.append("+");
			}
		}

		return GOOGLE_API_AJAX.replace("QUERY_STRING", sb.toString());
	}

}
