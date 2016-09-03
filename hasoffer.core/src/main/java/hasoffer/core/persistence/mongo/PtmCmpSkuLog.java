package hasoffer.core.persistence.mongo;

import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created on 2016/1/4.
 */
@Document(collection = "PtmCmpSkuLog")
public class PtmCmpSkuLog {

	@Id
	private String id;

	private long pcsId; //ptmcmpsku # id

	private Date priceTime;

	private float price;
	private String rating;

	@PersistenceConstructor
	public PtmCmpSkuLog() {
	}

	public PtmCmpSkuLog(PtmCmpSku cmpSku) {
		this.pcsId = cmpSku.getId();
		this.price = cmpSku.getPrice();
		this.rating = cmpSku.getRating();
		this.priceTime = cmpSku.getUpdateTime();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getPcsId() {
		return pcsId;
	}

	public void setPcsId(long pcsId) {
		this.pcsId = pcsId;
	}

	public Date getPriceTime() {
		return priceTime;
	}

	public void setPriceTime(Date priceTime) {
		this.priceTime = priceTime;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PtmCmpSkuLog that = (PtmCmpSkuLog) o;

		if (pcsId != that.pcsId) return false;
		if (Double.compare(that.price, price) != 0) return false;
		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (priceTime != null ? !priceTime.equals(that.priceTime) : that.priceTime != null) return false;
		return !(rating != null ? !rating.equals(that.rating) : that.rating != null);

	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = id != null ? id.hashCode() : 0;
		result = 31 * result + (int) (pcsId ^ (pcsId >>> 32));
		result = 31 * result + (priceTime != null ? priceTime.hashCode() : 0);
		temp = Double.doubleToLongBits(price);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (rating != null ? rating.hashCode() : 0);
		return result;
	}
}
