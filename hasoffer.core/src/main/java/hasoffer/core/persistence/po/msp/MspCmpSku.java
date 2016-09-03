package hasoffer.core.persistence.po.msp;

import hasoffer.base.model.Website;
import hasoffer.core.persistence.dbm.osql.Identifiable;
import hasoffer.fetch.sites.mysmartprice.model.MySmartPriceCmpSku;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/12/6
 */
//@Entity
public class MspCmpSku implements Identifiable<Long> {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private long productId; // MspProduct # id

	private Website website;
	private String seller;

	private String rating;

	private float price;

	@Column(columnDefinition = "text")
	private String url;

	private String color;
	private String size;

	public MspCmpSku() {
	}

	public MspCmpSku(long productId, MySmartPriceCmpSku cmpSku) {
		this.productId = productId;
		this.website = cmpSku.getWebsite();
		this.rating = cmpSku.getRating();
		this.price = cmpSku.getPrice();
		this.url = cmpSku.getUrl();
		this.color = cmpSku.getColor();
		this.size = cmpSku.getSize();
		this.seller = cmpSku.getSeller();
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
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

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		MspCmpSku mspCmpSku = (MspCmpSku) o;

		if (productId != mspCmpSku.productId) { return false; }
		if (Float.compare(mspCmpSku.price, price) != 0) { return false; }
		if (id != null ? !id.equals(mspCmpSku.id) : mspCmpSku.id != null) { return false; }
		if (website != mspCmpSku.website) { return false; }
		if (seller != null ? !seller.equals(mspCmpSku.seller) : mspCmpSku.seller != null) { return false; }
		if (rating != null ? !rating.equals(mspCmpSku.rating) : mspCmpSku.rating != null) { return false; }
		if (url != null ? !url.equals(mspCmpSku.url) : mspCmpSku.url != null) { return false; }
		if (color != null ? !color.equals(mspCmpSku.color) : mspCmpSku.color != null) { return false; }
		return !(size != null ? !size.equals(mspCmpSku.size) : mspCmpSku.size != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (int) (productId ^ (productId >>> 32));
		result = 31 * result + (website != null ? website.hashCode() : 0);
		result = 31 * result + (seller != null ? seller.hashCode() : 0);
		result = 31 * result + (rating != null ? rating.hashCode() : 0);
		result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
		result = 31 * result + (url != null ? url.hashCode() : 0);
		result = 31 * result + (color != null ? color.hashCode() : 0);
		result = 31 * result + (size != null ? size.hashCode() : 0);
		return result;
	}
}
