package hasoffer.fetch.model;

import java.util.Map;

public class Sku {

	private String sourceSkuId;
	private String sourceUrl;

	private float currentPrice;
	private float originalPrice;

	private Map<String, SkuAttributeValue> attributes;
	private boolean checked;

	public Sku(String sourceSkuId, String sourceUrl, boolean checked,
	           float currentPrice, float originalPrice,
	           Map<String, SkuAttributeValue> attributes) {
		this.sourceUrl = sourceUrl;
		this.checked = checked;
		this.sourceSkuId = sourceSkuId;
		this.currentPrice = currentPrice;
		this.originalPrice = originalPrice;
		this.attributes = attributes;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getSourceSkuId() {
		return sourceSkuId;
	}

	public void setSourceSkuId(String sourceSkuId) {
		this.sourceSkuId = sourceSkuId;
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

	public Map getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, SkuAttributeValue> attributes) {
		this.attributes = attributes;
	}
}
