package hasoffer.admin.controller.vo;

import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;

import java.util.Date;
import java.util.List;

/**
 * Created on 2015/12/22.
 */
public class ProductVo {

	private long id;

	private Date createTime = TimeUtils.nowDate();

	private List<CategoryVo> categories;

	private String title;// 标题
	private String tag;
	private float price;

	private String masterImageUrl;
	private String description;

	private String color;
	private String size;

	private int rating;

	private String sourceSite;
	private String sourceId;
	private Website website;
	private String flag;

	private float minPrice;
	private float maxPrice;
	private int skuCount;

	public float getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(float minPrice) {
		this.minPrice = minPrice;
	}

	public float getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(float maxPrice) {
		this.maxPrice = maxPrice;
	}

	public int getSkuCount() {
		return skuCount;
	}

	public void setSkuCount(int skuCount) {
		this.skuCount = skuCount;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getMasterImageUrl() {
		return masterImageUrl;
	}

	public void setMasterImageUrl(String masterImageUrl) {
		this.masterImageUrl = masterImageUrl;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<CategoryVo> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryVo> categories) {
		this.categories = categories;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getSourceSite() {
		return sourceSite;
	}

	public void setSourceSite(String sourceSite) {
		this.sourceSite = sourceSite;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
}
