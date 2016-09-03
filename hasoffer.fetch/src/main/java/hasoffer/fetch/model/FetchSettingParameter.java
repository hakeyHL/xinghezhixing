package hasoffer.fetch.model;

import hasoffer.base.model.Website;

import java.util.List;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/28
 */
public class FetchSettingParameter {

	private Website website;

	private boolean byPageUrl;//true : 根据pageUrl抓取，false : 根据sourceProductIds抓取

	private String pageUrlTemplate; // 抓取的页面地址
	private int pageFrom;//如果按照页面抓取，从第几页开始
	private int pageCount;// 如果按照页面抓取，抓取的页数

	private List<String> sourceProductIds;//源站productId的列表

	private long categoryId;

	private int listThreadCount;//解析商品列表的线程数

	private int analysisThreadCount;//解析商品详情页的线程数

	private int saveThreadCount; //保存任务的线程数

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public boolean isByPageUrl() {
		return byPageUrl;
	}

	public void setByPageUrl(boolean byPageUrl) {
		this.byPageUrl = byPageUrl;
	}

	public String getPageUrlTemplate() {
		return pageUrlTemplate;
	}

	public void setPageUrlTemplate(String pageUrlTemplate) {
		this.pageUrlTemplate = pageUrlTemplate;
	}

	public int getPageFrom() {
		return pageFrom;
	}

	public void setPageFrom(int pageFrom) {
		this.pageFrom = pageFrom;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<String> getSourceProductIds() {
		return sourceProductIds;
	}

	public void setSourceProductIds(List<String> sourceProductIds) {
		this.sourceProductIds = sourceProductIds;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public int getListThreadCount() {
		return listThreadCount;
	}

	public void setListThreadCount(int listThreadCount) {
		this.listThreadCount = listThreadCount;
	}

	public int getAnalysisThreadCount() {
		return analysisThreadCount;
	}

	public void setAnalysisThreadCount(int analysisThreadCount) {
		this.analysisThreadCount = analysisThreadCount;
	}

	public int getSaveThreadCount() {
		return saveThreadCount;
	}

	public void setSaveThreadCount(int saveThreadCount) {
		this.saveThreadCount = saveThreadCount;
	}
}
