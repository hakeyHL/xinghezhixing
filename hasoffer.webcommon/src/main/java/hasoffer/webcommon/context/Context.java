package hasoffer.webcommon.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by glx on 2015/5/27.
 */
public class Context {
	public static final String DEVICE_INFO = "DEVICE_INFO";
	private static final String ERROR = "ERROR";
	private static final String SESSION = "SESSION";
	private static final String TOKEN = "TOKEN";
	private static final String USER_ID = "USER_ID";
	private static final String SELLER_ID = "SELLER_ID";
	private static final String USER = "USER";
	private static final ThreadLocal<Context> threadLocal = new ThreadLocal<Context>();

	private Map<String, Object> map = new HashMap<String, Object>();
	private Map<String, String> headers = new HashMap<String, String>();
	private Map<String, Cookie> cookies = new HashMap<String, Cookie>();
	private List<ICookieListener> cookieListenerList = new ArrayList<ICookieListener>();


	private Context() {
	}

	public static Context currentContext() {
		if (threadLocal.get() == null) {
			Context context = new Context();
			threadLocal.set(context);
		}

		return threadLocal.get();
	}

	public void init() {
		map.clear();
		headers.clear();
		cookies.clear();
		cookieListenerList.clear();
	}

	public void set(String name, Object value) {
		map.put(name, value);
	}

	public Object get(String name) {
		return get(name, null);
	}

	public String getHeader(String name) {
		return getHeader(name, null);
	}

	public String getHeader(String name, String defaultValue) {
		String val = headers.get(name);
		if (val == null) {
			return defaultValue;
		}
		return val;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Object get(String name, Object defaultVal) {
		Object obj = map.get(name);
		return obj == null ? defaultVal : obj;
	}

	public String getError() {
		return (String) map.get(ERROR);
	}

	public void setError(String error) {
		map.put(ERROR, error);
	}

	public long getSellerId() {
		if (map.get(SELLER_ID) == null) {
			return 0;
		}
		return (Long) map.get(SELLER_ID);
	}

	public void setSellerId(long sellerId) {
		map.put(SELLER_ID, sellerId);
	}

	public long getUserId() {
		if (map.get(USER_ID) == null) {
			return 0;
		}
		return (Long) map.get(USER_ID);
	}

	public void setUserId(long userId) {
		map.put(USER_ID, userId);
	}

	public Session getSession() {
		Session session = (Session) map.get(SESSION);
		return session;
	}

	public void setSession(Session session) {
		map.put(SESSION, session);
	}

	public void addCookieListener(ICookieListener cookieListener) {
		this.cookieListenerList.add(cookieListener);
	}

	public void removeCookieListener(ICookieListener cookieListener) {
		this.cookieListenerList.remove(cookieListener);
	}

	public void setCookie(String name, String value) {
		if (value == null) {
			cookies.remove(name);
		} else {
			Cookie cookie = cookies.get(name);
			if (cookie != null) {
				cookie.setValue(value);
			} else {
				cookie = new Cookie(name, value);
				cookies.put(name, cookie);
			}
		}

		for (ICookieListener cookieListener : cookieListenerList) {
			cookieListener.afterSetCookie(name, value, Cookie.DEFAULT_MAX_AG);
		}
	}

	public String getCookieAsString(String name, String defaultValue) {
		Cookie cookie = cookies.get(name);
		if (cookie != null) {
			return cookie.getValue();
		} else {
			return defaultValue;
		}
	}

	public String getCookieAsString(String name) {
		return this.getCookieAsString(name, "");
	}

	public String getToken() {
		return (String) map.get(TOKEN);
	}

	public void setToken(String token) {
		map.put(TOKEN, token);
	}
}
