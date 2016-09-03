package hasoffer.admin.controller.vo;

import hasoffer.core.persistence.po.ptm.PtmCategory;

/**
 * Created on 2015/12/22.
 */
public class CategoryVo {

	private Long id;
	private long parentId;

	private String name;
	private String imageUrl;

	private String keyword;

	private long productCount;

	public CategoryVo(PtmCategory category) {
		this.id = category.getId();
		this.parentId = category.getParentId();
		this.name = category.getName();
		this.imageUrl = category.getImageUrl();
		this.keyword = category.getKeyword();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public long getProductCount() {
		return productCount;
	}

	public void setProductCount(long productCount) {
		this.productCount = productCount;
	}
}
