package hasoffer.fetch.model;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/3
 */
public class FetchSetting {

	private int id;

	private boolean valid;

	private boolean daily;

	private FetchSettingParameter settingParameter;

	public FetchSetting(int id, boolean valid, boolean daily, FetchSettingParameter settingParameter) {
		this.id = id;
		this.valid = valid;
		this.daily = daily;
		this.settingParameter = settingParameter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isDaily() {
		return daily;
	}

	public void setDaily(boolean daily) {
		this.daily = daily;
	}

	public FetchSettingParameter getSettingParameter() {
		return settingParameter;
	}

	public void setSettingParameter(FetchSettingParameter settingParameter) {
		this.settingParameter = settingParameter;
	}
}
