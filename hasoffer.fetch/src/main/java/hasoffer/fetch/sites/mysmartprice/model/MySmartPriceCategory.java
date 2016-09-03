package hasoffer.fetch.sites.mysmartprice.model;

import java.util.List;

/**
 * Created by chevy on 2015/12/4.
 * category的model
 */
public class MySmartPriceCategory {

	private String name;

	private String url;

	private String imageUrl;

	private String groupName;// 组名 - 把三级目录看成两级目录

	private List<MySmartPriceCategory> subCategories;

	public MySmartPriceCategory(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public MySmartPriceCategory(String name, String url, String imageUrl, String groupName) {
		this.name = name;
		this.url = url;
		this.imageUrl = imageUrl;
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return name + "\t\t" + groupName + "\t\t" + url + "\t\t" + imageUrl + "\t\t";
	}

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

	public List<MySmartPriceCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<MySmartPriceCategory> subCategories) {
		this.subCategories = subCategories;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
