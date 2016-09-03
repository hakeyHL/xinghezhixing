package hasoffer.fetch.model;

import java.util.List;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/27
 */
public class Category {

	private String	id;
	private String name;
	private String url;

	private int rank;

	private List<Category> subCates;

	public Category() {
	}

	public Category(String name, String url, int rank) {
		this.name = name;
		this.url = url;
		this.rank = rank;
	}

	public Category(String id, String name, String url, int rank) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.rank = rank;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public List<Category> getSubCates() {
		return subCates;
	}

	public void setSubCates(List<Category> subCates) {
		this.subCates = subCates;
	}
}
