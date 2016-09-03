package hasoffer.fetch.sites.amazon.ext.model;

import java.util.List;

/**
 * Created by chevy on 2015/11/12.
 */
public class AmazonSearchPage {

	private String pageUrl;

	private List<AmazonSimpleProduct> simpleProducts;

	private String category;

	private String keyword;

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public List<AmazonSimpleProduct> getSimpleProducts() {
		return simpleProducts;
	}

	public void setSimpleProducts(List<AmazonSimpleProduct> simpleProducts) {
		this.simpleProducts = simpleProducts;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
