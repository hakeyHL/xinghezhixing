package hasoffer.fetch.model;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/4
 */
public class PageModel {

	private String urlTemplate;

	private int pageCount;

	public PageModel() {
	}

	public PageModel(String urlTemplate, int pageCount) {
		this.urlTemplate = urlTemplate;
		this.pageCount = pageCount;
	}

	public String getUrlTemplate() {
		return urlTemplate;
	}

	public void setUrlTemplate(String urlTemplate) {
		this.urlTemplate = urlTemplate;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
}
