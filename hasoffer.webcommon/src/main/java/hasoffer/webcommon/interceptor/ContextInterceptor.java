package hasoffer.webcommon.interceptor;

import hasoffer.core.exception.BaseException;
import hasoffer.core.exception.UnknownException;
import hasoffer.core.product.ICategoryService;
import hasoffer.webcommon.context.Context;
import hasoffer.webcommon.context.ICookieListener;
import hasoffer.webcommon.context.ISessionListener;
import hasoffer.webcommon.context.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by glx on 2015/10/15.
 */
@Component
public class ContextInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(ContextInterceptor.class);

    @Resource
    private ICategoryService categoryService;

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //以下init顺序不能变
        Context.currentContext().init();

        this.initHeaders(httpServletRequest);
        this.initCookie(httpServletRequest, httpServletResponse);
        this.initSession(httpServletRequest);
        this.initToken(httpServletRequest);
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            /*if (modelAndView.getModel().get("categories") == null) {
//                List<CategoryBo> categoryBos = categoryService.listCategories();
//                List<CategoryVo> categoryVos = this.buildCatVo(categoryBos, "/");
//                modelAndView.getModel().put("categories", categoryVos);
			}

			if (modelAndView.getModel().get("_queryString") == null) {
				modelAndView.getModel().put("_queryString", httpServletRequest.getQueryString());
			}

			if (modelAndView.getModel().get("_queryStringWithoutPage") == null) {
				String queryString = StringUtils.trimToEmpty(httpServletRequest.getQueryString());
				String[] params = queryString.split("&");
				StringBuffer sb = new StringBuffer();
				if (params != null) {
					for (String param : params) {
						if (!param.startsWith("pn=") && !param.startsWith("ps=")) {
							sb.append("&");
							sb.append(param);
						}
					}
				}
				String str = sb.toString();
				modelAndView.getModel().put("_queryStringWithoutPage", str.startsWith("&") ? str.substring(1) : "");
			}

			if (modelAndView.getModel().get("_path") == null) {
				modelAndView.getModel().put("_path", httpServletRequest.getServletPath());
			}

			if (modelAndView.getModel().get("_parameterMap") == null) {
				modelAndView.getModel().put("_parameterMap", httpServletRequest.getParameterMap());
			}

			if (modelAndView.getModel().get("_errorCode") == null) {
				modelAndView.getModel().put("_errorCode", 0);
			}

			if (modelAndView.getModel().get("_message") == null) {
				modelAndView.getModel().put("_message", "");
			}*/
        }
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex)
            throws Exception {
        if (ex != null) {
            BaseException baseException = null;

            if (ex instanceof org.springframework.web.util.NestedServletException) {
                ex = (Exception) ex.getCause();
            }

            if (ex instanceof BaseException) {
                baseException = (BaseException) ex;
            } else {
                logger.error("{}", ex);
                baseException = new UnknownException(ex.getMessage());
            }

            String forward = "/error?_errorCode=" + baseException.getErrorCode().ordinal() + "&_message=" + baseException.getMessage();
            logger.error(ex.getMessage());
//            httpServletRequest.getRequestDispatcher(forward).forward(httpServletRequest, httpServletResponse);
        }
    }

    /*private List<CategoryVo> buildCatVo(List<CategoryBo> categoryBos, String parentPath) {
        parentPath = StringUtils.trimToEmpty(parentPath);
        if (!parentPath.endsWith("/")) {
            parentPath = parentPath + "/";
        }

        Pattern pattern = Pattern.compile("^" + parentPath + "\\d+/?$");


        List<CategoryVo> categoryVos = new ArrayList<CategoryVo>();
        for (CategoryBo categoryBo : categoryBos) {
            if (pattern.matcher(categoryBo.getPath()).matches()) {
                List<CategoryVo> subCat = this.buildCatVo(categoryBos, categoryBo.getPath());
                CategoryVo categoryVo = new CategoryVo(categoryBo.getId(),
                        categoryBo.getName(),
                        ImageServerUtil.getUrl(categoryBo.getImagePath()),
                        ImageServerUtil.getUrl(categoryBo.getMobImagePath()),
                        subCat);
                categoryVos.add(categoryVo);
            }
        }

        return categoryVos;
    }*/

    private void initToken(HttpServletRequest httpRequest) {

    }

    private void initHeaders(HttpServletRequest httpServletRequest) {
        Map<String, String> headerMap = new HashMap<String, String>();
        Enumeration enumeration = httpServletRequest.getHeaderNames();
        Object e = enumeration.nextElement();
        while (e != null) {
            String hs = httpServletRequest.getHeader(e.toString());
            headerMap.put(e.toString(), hs);
            e = enumeration.nextElement();
        }

        Context.currentContext().setHeaders(headerMap);
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