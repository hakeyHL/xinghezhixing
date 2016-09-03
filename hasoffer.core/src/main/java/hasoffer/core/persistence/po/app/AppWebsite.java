package hasoffer.core.persistence.po.app;

import hasoffer.base.model.Website;
import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;

/**
 * Created on 2015/12/31.
 */
@Entity
public class AppWebsite implements Identifiable<Long> {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(unique = true, nullable = false)
	private Website website;

	private String logoPath;//logo图片路径

	// 该网站 android app 包名
	private String appPackage;

	// display
	private boolean display;

	private int rebate;//返利额度

	public AppWebsite() {
	}

	public AppWebsite(Website website, String appPackage) {
		this.website = website;
		this.display = true;
		this.appPackage = appPackage;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public int getRebate() {
		return rebate;
	}

	public void setRebate(int rebate) {
		this.rebate = rebate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AppWebsite that = (AppWebsite) o;

		if (display != that.display) return false;
		if (rebate != that.rebate) return false;
		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (website != that.website) return false;
		if (logoPath != null ? !logoPath.equals(that.logoPath) : that.logoPath != null) return false;
		return !(appPackage != null ? !appPackage.equals(that.appPackage) : that.appPackage != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (website != null ? website.hashCode() : 0);
		result = 31 * result + (logoPath != null ? logoPath.hashCode() : 0);
		result = 31 * result + (appPackage != null ? appPackage.hashCode() : 0);
		result = 31 * result + (display ? 1 : 0);
		result = 31 * result + rebate;
		return result;
	}
}
