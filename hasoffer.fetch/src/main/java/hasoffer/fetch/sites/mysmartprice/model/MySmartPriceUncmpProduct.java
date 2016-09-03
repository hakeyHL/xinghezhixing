package hasoffer.fetch.sites.mysmartprice.model;

import hasoffer.base.model.Website;

public class MySmartPriceUncmpProduct {
	private String title;
	private String imgUrl;
	private String url;
	private float price;
	private long categoryId;
	private Website site;
	private String offerUrl;

	public MySmartPriceUncmpProduct(String title, String imgUrl, String url, float price, long categoryId) {
		this.title = title;
		this.imgUrl = imgUrl;
		this.url = url;
		this.price = price;
		this.categoryId = categoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public Website getSite() {
		return site;
	}

	public void setSite(Website site) {
		this.site = site;
	}

	public String getOfferUrl() {
		return offerUrl;
	}

	public void setOfferUrl(String offerUrl) {
		this.offerUrl = offerUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (!(o instanceof MySmartPriceUncmpProduct)) { return false; }

		MySmartPriceUncmpProduct that = (MySmartPriceUncmpProduct) o;

		if (categoryId != that.categoryId) { return false; }
		if (Float.compare(that.price, price) != 0) { return false; }
		if (imgUrl != null ? !imgUrl.equals(that.imgUrl) : that.imgUrl != null) { return false; }
		if (site != that.site) { return false; }
		if (!title.equals(that.title)) { return false; }
		if (!url.equals(that.url)) { return false; }

		return true;
	}

	@Override
	public int hashCode() {
		int result = title.hashCode();
		result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
		result = 31 * result + url.hashCode();
		result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
		result = 31 * result + (int) (categoryId ^ (categoryId >>> 32));
		result = 31 * result + (site != null ? site.hashCode() : 0);
		return result;
	}
}
