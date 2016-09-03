package hasoffer.fetch.sites.mysmartprice.model;

import java.util.List;
import java.util.Map;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/12/6
 */
public class MySmartPriceProduct {

	private String mspId;

	private String url;

	private String title;

	private float price;

	private List<String> imageUrls;

	private int rating;

	private Map<String, Map<String, String>> baseAttrs;

	private List<String> colors;

	private List<String> sizes;

	private List<MySmartPriceCmpSku> cmpSkus;

	private List<String> features;

	private String description;

	public String getMspId() {
		return mspId;
	}

	public void setMspId(String mspId) {
		this.mspId = mspId;
	}

	public List<MySmartPriceCmpSku> getCmpSkus() {
		return cmpSkus;
	}

	public void setCmpSkus(List<MySmartPriceCmpSku> cmpSkus) {
		this.cmpSkus = cmpSkus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Map<String, Map<String, String>> getBaseAttrs() {
		return baseAttrs;
	}

	public void setBaseAttrs(Map<String, Map<String, String>> baseAttrs) {
		this.baseAttrs = baseAttrs;
	}

	public List<String> getColors() {
		return colors;
	}

	public void setColors(List<String> colors) {
		this.colors = colors;
	}

	public List<String> getSizes() {
		return sizes;
	}

	public void setSizes(List<String> sizes) {
		this.sizes = sizes;
	}
}
