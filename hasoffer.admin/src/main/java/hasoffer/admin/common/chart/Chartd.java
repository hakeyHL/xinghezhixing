package hasoffer.admin.common.chart;

import java.util.List;

/**
 * Date : 2016/1/22
 * Function :
 */
class Chartd {
	String name;
	List<Float> data;

	public Chartd(String name, List<Float> data) {
		this.name = name;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Float> getData() {
		return data;
	}

	public void setData(List<Float> data) {
		this.data = data;
	}
}
