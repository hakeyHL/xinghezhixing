package hasoffer.webcommon.context;

/**
 * Created by glx on 2015/4/8.
 */
public interface ICookieListener {
	void afterSetCookie(String name, String value, long maxAge);
}
