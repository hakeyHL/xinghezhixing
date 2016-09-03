package hasoffer.fetch.model;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/30
 */
public class SkuAttributeValue {

	private String id;

	private String attrName;

	private String strValue;
	private String imgValue;

	public SkuAttributeValue(String strValue) {
		this.strValue = strValue;
	}

	public SkuAttributeValue(String attrName, String strValue, String imgValue) {
		this.attrName = attrName;
		this.strValue = strValue;
		this.imgValue = imgValue;
	}

	public SkuAttributeValue(String id, String attrName, String strValue, String imgValue) {
		this.id = id;
		this.attrName = attrName;
		this.strValue = strValue;
		this.imgValue = imgValue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public String getImgValue() {
		return imgValue;
	}

	public void setImgValue(String imgValue) {
		this.imgValue = imgValue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		SkuAttributeValue that = (SkuAttributeValue) o;

		if (id != null ? !id.equals(that.id) : that.id != null) { return false; }
		if (attrName != null ? !attrName.equals(that.attrName) : that.attrName != null) { return false; }
		if (strValue != null ? !strValue.equals(that.strValue) : that.strValue != null) { return false; }
		return !(imgValue != null ? !imgValue.equals(that.imgValue) : that.imgValue != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (attrName != null ? attrName.hashCode() : 0);
		result = 31 * result + (strValue != null ? strValue.hashCode() : 0);
		result = 31 * result + (imgValue != null ? imgValue.hashCode() : 0);
		return result;
	}
}
