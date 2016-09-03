package hasoffer.fetch.helper;

import hasoffer.base.model.Website;
import hasoffer.fetch.model.FetchSetting;
import hasoffer.fetch.model.FetchSettingParameter;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/3
 */
public class JobConfigHelper {

	public static List<FetchSetting> getJobs() throws Exception {
		List<FetchSetting> fetchSettings = new ArrayList<FetchSetting>();

		SAXReader reader = new SAXReader();
//		File file = new File("D:\\allbuy_projects\\allbuy_compare\\com.allbuy.compare.fetch\\src\\main\\resources\\fetchsettings.xml");
//		File file = new File("/fetchsettings.xml");
		Document document = reader.read(ClassLoader.getSystemResourceAsStream("fetchsettings.xml"));

		Element root = document.getRootElement();
		List<Element> childElements = root.elements("job");
		for (Element element : childElements) {
			String id = element.attributeValue("id");
			boolean valid = Boolean.valueOf(element.attributeValue("valid"));
			boolean daily = Boolean.valueOf(element.attributeValue("daily"));

			List<Element> params = element.elements("parameter");

			Map<String, String> paraMap = new HashMap<String, String>();
			for (Element param : params) {
				paraMap.put(element.attributeValue("key"), element.attributeValue("value"));
			}

			fetchSettings.add(getFetchSetting(id, valid, daily, paraMap));
		}

		return fetchSettings;
	}

	private static FetchSetting getFetchSetting(String id, boolean valid, boolean daily, Map<String, String> paraMap) {
		String website = paraMap.get("website");
		String byPage = paraMap.get("byPage");
		String pageUrl = paraMap.get("pageUrl");
		String pageCount = paraMap.get("pageCount");
		String pageFrom = paraMap.get("pageFrom");
		String listThreadCountStr = paraMap.get("listThreadCount");
		String analysisThreadCountStr = paraMap.get("analysisThreadCount");
		String saveThreadCountStr = paraMap.get("saveThreadCount");

		FetchSettingParameter parameters = new FetchSettingParameter();

		parameters.setAnalysisThreadCount(Integer.parseInt(analysisThreadCountStr));
		parameters.setListThreadCount(Integer.parseInt(listThreadCountStr));
		parameters.setSaveThreadCount(Integer.parseInt(saveThreadCountStr));

		parameters.setPageCount(Integer.parseInt(pageCount));
		parameters.setPageFrom(Integer.parseInt(pageFrom));
		parameters.setWebsite(Website.valueOf(website));
		parameters.setPageUrlTemplate(pageUrl);
		parameters.setByPageUrl(Boolean.valueOf(byPage));

		return new FetchSetting(Integer.parseInt(id), valid, daily, parameters);
	}

	public static void main(String[] args) {
		try {
			getJobs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
