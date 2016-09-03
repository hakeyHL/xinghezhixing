package hasoffer.fetch.sites.amazon.ext.model;

/**
 * Created by chevy on 2015/11/12.
 */
public class AmazonProduct {

	private String url;

	private String ASIN;// amazon product id

	private String title;

	private float price;

	private String soldBy; // 卖家

	private String fulfilledBy;// 由谁配送

	private boolean hasGift;// 是否有礼品包装

	private float shipping; // 运费

	private String otherSellers;// 其他相同商品

	private AmazonProductReview productReview;

	private String sellersRank;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getASIN() {
		return ASIN;
	}

	public void setASIN(String ASIN) {
		this.ASIN = ASIN;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getSoldBy() {
		return soldBy;
	}

	public void setSoldBy(String soldBy) {
		this.soldBy = soldBy;
	}

	public String getFulfilledBy() {
		return fulfilledBy;
	}

	public void setFulfilledBy(String fulfilledBy) {
		this.fulfilledBy = fulfilledBy;
	}

	public boolean isHasGift() {
		return hasGift;
	}

	public void setHasGift(boolean hasGift) {
		this.hasGift = hasGift;
	}

	public float getShipping() {
		return shipping;
	}

	public void setShipping(float shipping) {
		this.shipping = shipping;
	}

	public String getOtherSellers() {
		return otherSellers;
	}

	public void setOtherSellers(String otherSellers) {
		this.otherSellers = otherSellers;
	}

	public AmazonProductReview getProductReview() {
		return productReview;
	}

	public void setProductReview(AmazonProductReview productReview) {
		this.productReview = productReview;
	}

	public String getSellersRank() {
		return sellersRank;
	}

	public void setSellersRank(String sellersRank) {
		this.sellersRank = sellersRank;
	}
}
