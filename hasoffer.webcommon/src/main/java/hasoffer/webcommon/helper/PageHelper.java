package hasoffer.webcommon.helper;

import hasoffer.base.model.PageModel;
import hasoffer.base.model.PageableResult;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class PageHelper {

	public static PageModel getPageModel(HttpServletRequest request, PageableResult pageableResult) {
		PageModel pageModel = new PageModel();
		if (pageableResult != null) {
			pageModel.set(pageableResult);
		}
		pageModel.setRequestUrl(request.getRequestURI());

		Map pageParams = new HashMap<String, Object>();
		Map<String, String[]> params = request.getParameterMap();
		for (String key : params.keySet()) {
			if (key.equals("page") || key.equals("size")) {
				continue;
			}
			String[] ss = params.get(key);
			if (ss == null || ss.length != 1) {
				continue;
			}
			pageParams.put(key, ss[0]);
		}
		pageModel.setPageParams(pageParams);
		return pageModel;
	}

}
