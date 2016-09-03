package hasoffer.fetch.sites.snapdealDeprecated.model;

/**
 * Author:menghaiquan
 * Date:2016/1/18 2016/1/18
 */
public class SnapDealFetchProduct {
	private String name;
	private String imgUrl;
	private String url;
	private float price;
	private long categoryId;

	public SnapDealFetchProduct(String name, String imgUrl, String url, float price, long categoryId) {
		this.name = name;
		this.imgUrl = imgUrl;
		this.url = url;
		this.price = price;
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (!(o instanceof SnapDealFetchProduct)) { return false; }

		SnapDealFetchProduct that = (SnapDealFetchProduct) o;

		if (categoryId != that.categoryId) { return false; }
		if (Float.compare(that.price, price) != 0) { return false; }
		if (imgUrl != null ? !imgUrl.equals(that.imgUrl) : that.imgUrl != null) { return false; }
		if (!name.equals(that.name)) { return false; }
		if (!url.equals(that.url)) { return false; }

		return true;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
		result = 31 * result + url.hashCode();
		result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
		result = 31 * result + (int) (categoryId ^ (categoryId >>> 32));
		return result;
	}
}
