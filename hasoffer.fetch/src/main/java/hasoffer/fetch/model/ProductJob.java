package hasoffer.fetch.model;


import hasoffer.base.model.Website;

import java.io.Serializable;

public class ProductJob implements Serializable {
	private Website website;

	private String sourceId;
	private String sourceUrl;
	private long categoryId;

	private Product product;

	private boolean error;
	private String errMessage;

	public ProductJob(Website website, String url) {
		this.website = website;
		this.sourceUrl = url;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		ProductJob that = (ProductJob) o;

		if (categoryId != that.categoryId) { return false; }
		if (error != that.error) { return false; }
		if (website != that.website) { return false; }
		if (sourceId != null ? !sourceId.equals(that.sourceId) : that.sourceId != null) { return false; }
		if (sourceUrl != null ? !sourceUrl.equals(that.sourceUrl) : that.sourceUrl != null) { return false; }
		if (product != null ? !product.equals(that.product) : that.product != null) { return false; }
		return !(errMessage != null ? !errMessage.equals(that.errMessage) : that.errMessage != null);

	}

	@Override
	public int hashCode() {
		int result = website != null ? website.hashCode() : 0;
		result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
		result = 31 * result + (sourceUrl != null ? sourceUrl.hashCode() : 0);
		result = 31 * result + (int) (categoryId ^ (categoryId >>> 32));
		result = 31 * result + (product != null ? product.hashCode() : 0);
		result = 31 * result + (error ? 1 : 0);
		result = 31 * result + (errMessage != null ? errMessage.hashCode() : 0);
		return result;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
