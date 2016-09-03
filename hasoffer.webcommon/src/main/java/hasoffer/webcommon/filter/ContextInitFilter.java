package hasoffer.webcommon.filter;

import hasoffer.webcommon.context.Context;
import hasoffer.webcommon.context.ICookieListener;
import hasoffer.webcommon.context.ISessionListener;
import hasoffer.webcommon.context.Session;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Date : 2015/12/28
 * Function :
 */
public class ContextInitFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		Context.currentContext().init();

		this.initCookie((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
		this.initSession((HttpServletRequest) servletRequest);

		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}

	private void initCookie(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) {
		Cookie[] cookies = httpRequest.getCookies();
		if (cookies == null) {
			return;
		}
		for (Cookie cookie : cookies) {
			Context.currentContext().setCookie(cookie.getName(), cookie.getValue());
		}

		Context.currentContext().addCookieListener(new ICookieListener() {
			public void afterSetCookie(String name, String value, long maxAge) {
				Cookie cookie1 = new Cookie(name, value);
				cookie1.setPath("/");
				//cookie1.setDomain("localhost");
				cookie1.setMaxAge((int) maxAge);

				if (value == null) {
					cookie1.setMaxAge(0);
				}

				httpResponse.addCookie(cookie1);
			}
		});
	}

	private void initSession(HttpServletRequest httpRequest) {
		final HttpSession httpSession = httpRequest.getSession();
		Session session = new Session(httpSession.getId());
		session.setCreationTime(httpSession.getCreationTime());
		session.setLastAccessedTime(httpSession.getLastAccessedTime());

		Enumeration<String> attributeNames = httpSession.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String attributeName = attributeNames.nextElement();
			session.setAttribute(attributeName, httpSession.getAttribute(attributeName));
		}

		session.addListener(new ISessionListener() {
			public <T> void afterSetAttribute(String attributeName, T oldAttribute, T newAttribute) {
				httpSession.setAttribute(attributeName, newAttribute);
			}
		});

		Context.currentContext().setSession(session);
	}

}
