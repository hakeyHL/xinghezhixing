package hasoffer.fetch.sites.google.model;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/25
 */
public class GoogleSearchResult {

	/**
	 *  "GsearchResultClass": "GwebSearch",
	 "unescapedUrl": "http://www.tinydeal.com/oukitel-u8-si-3879.html",
	 "url": "http://www.tinydeal.com/oukitel-u8-si-3879.html",
	 "visibleUrl": "www.tinydeal.com",
	 "cacheUrl": "http://www.google.com/search?q003dcache:2YuKso_vUaUJ:www.tinydeal.com",
	 "title": "003cb003eOUKITEL U8003c/b003e",
	 "titleNoFormatting": "OUKITEL U8",
	 "content": "003cb003eOUKITEL003c/b003e Universe Tap 003cb003eU8003c/b003e 5.50026quot; MTK6735P Quad-core 64-bit 4G LTE Phone. \n$135.41. 003cb003eOUKITEL003c/b003e Universe Tap 003cb003eU8003c/b003e 5.50026quot; MTK6735P Quad-core 64-bit 4G LTE ..."
	 */

	private String GsearchResultClass;
	private String unescapedUrl;
	private String url;
	private String visibleUrl;
	private String cacheUrl;
	private String title;
	private String titleNoFormatting;
	private String content;

	public String getGsearchResultClass() {
		return GsearchResultClass;
	}

	public void setGsearchResultClass(String gsearchResultClass) {
		GsearchResultClass = gsearchResultClass;
	}

	public String getUnescapedUrl() {
		return unescapedUrl;
	}

	public void setUnescapedUrl(String unescapedUrl) {
		this.unescapedUrl = unescapedUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVisibleUrl() {
		return visibleUrl;
	}

	public void setVisibleUrl(String visibleUrl) {
		this.visibleUrl = visibleUrl;
	}

	public String getCacheUrl() {
		return cacheUrl;
	}

	public void setCacheUrl(String cacheUrl) {
		this.cacheUrl = cacheUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleNoFormatting() {
		return titleNoFormatting;
	}

	public void setTitleNoFormatting(String titleNoFormatting) {
		this.titleNoFormatting = titleNoFormatting;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
