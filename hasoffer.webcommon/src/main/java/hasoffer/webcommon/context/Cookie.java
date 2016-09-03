package hasoffer.webcommon.context;

/**
 * Created by glx on 2015/4/8.
 */
public class Cookie {
	public static final String TOKEN = "TOKEN";
	public static int DEFAULT_MAX_AG = 3600*24*10000;

	private String name;
	private String value;
	private int maxAge = DEFAULT_MAX_AG;

	public Cookie(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public Cookie(String name, String value, int maxAge) {
		this.name = name;
		this.value = value;
		this.maxAge = maxAge;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}
}
