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
public class MspImage implements Identifiable<Long> {
	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private long productId; // #
	private String imageUrl;

	public MspImage(long proId, String imageUrl) {
		this.productId = proId;
		this.imageUrl = imageUrl;
	}

	public MspImage() {
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		MspImage mspImage = (MspImage) o;

		if (productId != mspImage.productId) { return false; }
		if (id != null ? !id.equals(mspImage.id) : mspImage.id != null) { return false; }
		return !(imageUrl != null ? !imageUrl.equals(mspImage.imageUrl) : mspImage.imageUrl != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (int) (productId ^ (productId >>> 32));
		result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
		return result;
	}
}
