package hasoffer.fetch.model;

import hasoffer.base.model.Website;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

public class Product implements Serializable {
	private Website website;
	private String productTitle;
	private String subTitle;
	private long cmpCategoryId;// 比价类目中的ID
	private String sourceProductId;
	private String sourceUrl;
	private String brandName;
	private String model;
	private float currentPrice;
	private float originalPrice;
	private String description;
	private List<ProductImage> images;
	private LinkedHashMap<String, LinkedHashMap<String, String>> attributeMap;

	private List<Sku> skus;
	private List<String> saleAttributeNames;//sku 销售属性名称列表
	private int reviews;

	public Product() {
	}

	public Product(Website website, String productTitle, String sourceProductId, String sourceUrl,
	               String brandName, String model,
	               float currentPrice, float originalPrice,
	               String description, LinkedHashMap<String, LinkedHashMap<String, String>> attributeMap,
	               List<Sku> skus, List<String> saleAttributeNames,
	               List<ProductImage> images, String subTitle, int reviews) {
		this.website = website;
		this.productTitle = productTitle;
		this.sourceProductId = sourceProductId;
		this.brandName = brandName;
		this.model = model;
		this.sourceUrl = sourceUrl;
		this.currentPrice = currentPrice;
		this.originalPrice = originalPrice;
		this.description = description;
		this.attributeMap = attributeMap;
		this.skus = skus;
		this.images = images;
		this.saleAttributeNames = saleAttributeNames;
		this.subTitle = subTitle;
		this.reviews = reviews;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public long getCmpCategoryId() {
		return cmpCategoryId;
	}

	public void setCmpCategoryId(long cmpCategoryId) {
		this.cmpCategoryId = cmpCategoryId;
	}

	public List<Sku> getSkus() {
		return skus;
	}

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}

	public List<String> getSaleAttributeNames() {
		return saleAttributeNames;
	}

	public void setSaleAttributeNames(List<String> saleAttributeNames) {
		this.saleAttributeNames = saleAttributeNames;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public String getSourceProductId() {
		return sourceProductId;
	}

	public void setSourceProductId(String sourceProductId) {
		this.sourceProductId = sourceProductId;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public float getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(float currentPrice) {
		this.currentPrice = currentPrice;
	}

	public float getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(float originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LinkedHashMap<String, LinkedHashMap<String, String>> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(LinkedHashMap<String, LinkedHashMap<String, String>> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public List<ProductImage> getImages() {
		return images;
	}

	public void setImages(List<ProductImage> images) {
		this.images = images;
	}

	public int getReviews() {
		return reviews;
	}

	public void setReviews(int reviews) {
		this.reviews = reviews;
	}

	@Override
	public String toString() {
		return "Product{" +
				"website=" + website +
				", productTitle='" + productTitle + '\'' +
				", subTitle='" + subTitle + '\'' +
				", cmpCategoryId=" + cmpCategoryId +
				", sourceProductId='" + sourceProductId + '\'' +
				", sourceUrl='" + sourceUrl + '\'' +
				", brandName='" + brandName + '\'' +
				", model='" + model + '\'' +
				", currentPrice=" + currentPrice +
				", originalPrice=" + originalPrice +
				", description='" + description + '\'' +
				", images=" + images +
				", attributeMap=" + attributeMap +
				", skus=" + skus +
				", saleAttributeNames=" + saleAttributeNames +
				", reviews=" + reviews +
				'}';
	}
}

