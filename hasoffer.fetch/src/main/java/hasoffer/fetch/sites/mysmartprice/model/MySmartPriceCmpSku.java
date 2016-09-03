package hasoffer.fetch.sites.mysmartprice.model;

import hasoffer.base.model.Website;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/12/6
 */
public class MySmartPriceCmpSku {

	private Website website;
	private String seller;// 考虑同一平台不同卖家的情况

	private String rating;

	private float price;

	private String url;

	private String color;
	private String size;

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
