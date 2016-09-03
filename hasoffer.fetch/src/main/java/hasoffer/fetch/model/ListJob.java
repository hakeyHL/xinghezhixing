package hasoffer.fetch.model;

import hasoffer.base.model.Website;

import java.io.Serializable;
import java.util.LinkedHashSet;

/**
 * Author: Wesley Wu
 * Date: 2014/10/30 13:27
 */
public class ListJob implements Serializable {
	private Website website;
	private String listUrl;
	private boolean error;
	private String message;
	private LinkedHashSet<ProductJob> productJobs;
	private String categoryId;

	public ListJob(Website website, String listUrl, String categoryId) {
		this.website = website;
		this.listUrl = listUrl;
		this.categoryId = categoryId;
	}

	public ListJob(Website website, String listUrl) {
		this.website = website;
		this.listUrl = listUrl;
	}

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getListUrl() {
		return listUrl;
	}

	public void setListUrl(String listUrl) {
		this.listUrl = listUrl;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LinkedHashSet<ProductJob> getProductJobs() {
		return productJobs;
	}

	public void setProductJobs(LinkedHashSet<ProductJob> productJobs) {
		this.productJobs = productJobs;
	}
}
