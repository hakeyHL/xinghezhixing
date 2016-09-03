package hasoffer.admin.interceptor;

import hasoffer.core.exception.PermissionDeniedException;
import hasoffer.core.persistence.enums.AdminType;
import hasoffer.core.persistence.po.sys.SysAdmin;
import hasoffer.webcommon.context.Context;
import hasoffer.webcommon.context.StaticContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class Visit2Interceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(Visit2Interceptor.class);

    private static List<String> tempUserUris = new ArrayList<String>();

    static {
        tempUserUris.add("/s2/list");
        tempUserUris.add("/s/reSearchByLogKeyword");
        tempUserUris.add("/s/toManualCorrelation");
        tempUserUris.add("/s/getProductInfo");
        tempUserUris.add("/s/getProduct");
        tempUserUris.add("/cate/list");
        tempUserUris.add("/p/create");
        tempUserUris.add("/s/addCmpSkus");
        tempUserUris.add("/s/getCmpSku");
        tempUserUris.add("/p/cmp/");
        tempUserUris.add("/s/checkWebsite");
    }

    private Pattern PATTERN_URI_STATIC_RESOURCE = Pattern.compile("/static/.+");

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        String requestUri = httpServletRequest.getRequestURI();

        if (PATTERN_URI_STATIC_RESOURCE.matcher(requestUri).matches()
                || requestUri.equals("/")
                || requestUri.equals("/prelogin")
                || requestUri.equals("/login")
                || requestUri.equals("/logout")) {
            return true;
        }

        SysAdmin admin = (SysAdmin) Context.currentContext().get(StaticContext.USER);

        try {
            if (admin == null) {
                throw new PermissionDeniedException("禁止访问");
            }

            if (admin.getType().equals(AdminType.SUPER_ADMIN)
                    || admin.getType().equals(AdminType.GENERAL_ADMIN)) {
                return true;
            }

            if (admin.getType().equals(AdminType.TEMP_ADMIN)) {
                for (String uri : tempUserUris) {
                    if (requestUri.startsWith(uri)) {
                        return true;
                    }
                }

                throw new PermissionDeniedException("禁止访问");
            }
        } catch (PermissionDeniedException e) {
            return false;
//            httpServletRequest.getRequestDispatcher("/error?_message=禁止访问");
        }

        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {

        }
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex)
            throws Exception {

    }
}