package hasoffer.fetch.sites.Ali.model;

/**
 * Created by chevy on 2015/11/20.
 * 从aliexpress 网站获取的商品model
 */
public class AliSpu {

	private String proId;

	private String proUrl;

	private String imageUrl;

	private int orderCount;

	private float price;

	private String title;

	public AliSpu(String sourceId, String proUrl, String imageUrl,
	              int orderCount, float price, String title) {
		this.proId = sourceId;
		this.proUrl = proUrl;
		this.imageUrl = imageUrl;
		this.orderCount = orderCount;
		this.price = price;
		this.title = title;
	}

	@Override
	public String toString() {
		return "AliSpu{" +
		       "proId='" + proId + '\'' +
		       ", title='" + title + '\'' +
		       '}';
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getProUrl() {
		return proUrl;
	}

	public void setProUrl(String proUrl) {
		this.proUrl = proUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
