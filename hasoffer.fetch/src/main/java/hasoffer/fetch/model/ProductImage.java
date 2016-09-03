package hasoffer.fetch.model;

import hasoffer.base.utils.StringUtils;

import java.io.Serializable;

public class ProductImage implements Serializable {

	private String smallImageUrl;
	private String middleImageUrl;
	private String largeImageUrl;

	public ProductImage(String smallImageUrl, String middleImageUrl, String largeImageUrl) {
		this.smallImageUrl = smallImageUrl;
		this.middleImageUrl = middleImageUrl;
		this.largeImageUrl = largeImageUrl;
	}

	/**
	 * 按照默认顺序返回图片地址
	 *
	 * @return
	 */
	public String getDefaultImageUrl() {
		if (!StringUtils.isEmpty(largeImageUrl) && largeImageUrl.startsWith("http")) {
			return largeImageUrl;
		}
		if (!StringUtils.isEmpty(middleImageUrl) && middleImageUrl.startsWith("http")) {
			return middleImageUrl;
		}
		if (!StringUtils.isEmpty(smallImageUrl) && smallImageUrl.startsWith("http")) {
			return smallImageUrl;
		}
		return "";
	}

	public String getSmallImageUrl() {
		return smallImageUrl;
	}

	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}

	public String getMiddleImageUrl() {
		return middleImageUrl;
	}

	public void setMiddleImageUrl(String middleImageUrl) {
		this.middleImageUrl = middleImageUrl;
	}

	public String getLargeImageUrl() {
		return largeImageUrl;
	}

	public void setLargeImageUrl(String largeImageUrl) {
		this.largeImageUrl = largeImageUrl;
	}
}
