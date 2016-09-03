package hasoffer.fetch.sites.snapdealDeprecated.model;
import java.util.List;

/**
 * Author:menghaiquan
 * Date:2016/1/14 2016/1/14
 */
public class SnapDealFetchCategory {
	private String name;

	private String url;

	private String imageUrl;

	private int proCount;

	private int depth;

	List<SnapDealFetchCategory> subCates;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getProCount() {
		return proCount;
	}

	public void setProCount(int proCount) {
		this.proCount = proCount;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public List<SnapDealFetchCategory> getSubCates() {
		return subCates;
	}

	public void setSubCates(List<SnapDealFetchCategory> subCates) {
		this.subCates = subCates;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (!(o instanceof SnapDealFetchCategory)) { return false; }

		SnapDealFetchCategory category = (SnapDealFetchCategory) o;

		if (!name.equals(category.name)) { return false; }
		if (!url.equals(category.url)) { return false; }

		return true;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + url.hashCode();
		return result;
	}
}
