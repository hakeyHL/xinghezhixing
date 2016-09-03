package hasoffer.core.persistence.po.msp;

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by chevy on 2015/12/7.
 */
//@Entity
public class MspProduct implements Identifiable<Long> {
	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private long jobId;

	private long categoryId;

	private String title;
	private float price;

	@Column(length = 2048)
	private String feature;
	@Column(columnDefinition = "longtext")
	private String description;

	private String color;
	private String size;

	private int rating;

	private String url;

	public MspProduct(long jobId, long categoryId, String title, float price,
	                  String color, String size, int rating,
	                  String url, String description, String feature) {
		this.jobId = jobId;
		this.categoryId = categoryId;
		this.title = title;
		this.price = price;
		this.color = color;
		this.size = size;
		this.feature = feature;
		this.rating = rating;
		this.url = url;
		this.description = description;
	}

	public MspProduct() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
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

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		MspProduct that = (MspProduct) o;

		if (jobId != that.jobId) { return false; }
		if (categoryId != that.categoryId) { return false; }
		if (Float.compare(that.price, price) != 0) { return false; }
		if (rating != that.rating) { return false; }
		if (id != null ? !id.equals(that.id) : that.id != null) { return false; }
		if (title != null ? !title.equals(that.title) : that.title != null) { return false; }
		if (feature != null ? !feature.equals(that.feature) : that.feature != null) { return false; }
		if (description != null ? !description.equals(that.description) : that.description != null) { return false; }
		if (color != null ? !color.equals(that.color) : that.color != null) { return false; }
		if (size != null ? !size.equals(that.size) : that.size != null) { return false; }
		return !(url != null ? !url.equals(that.url) : that.url != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (int) (jobId ^ (jobId >>> 32));
		result = 31 * result + (int) (categoryId ^ (categoryId >>> 32));
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
		result = 31 * result + (feature != null ? feature.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (color != null ? color.hashCode() : 0);
		result = 31 * result + (size != null ? size.hashCode() : 0);
		result = 31 * result + rating;
		result = 31 * result + (url != null ? url.hashCode() : 0);
		return result;
	}
}
