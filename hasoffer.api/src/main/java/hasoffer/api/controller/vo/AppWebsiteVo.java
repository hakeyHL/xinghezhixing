package hasoffer.api.controller.vo;

import hasoffer.base.model.Website;

/**
 * Created on 2015/12/31.
 */
public class AppWebsiteVo {

	private Website website;
	private String appPackage;
	private String logoUrl;

	public AppWebsiteVo(Website website, String appPackage, String logoUrl) {
		this.website = website;
		this.appPackage = appPackage;
		this.logoUrl = logoUrl;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
}
