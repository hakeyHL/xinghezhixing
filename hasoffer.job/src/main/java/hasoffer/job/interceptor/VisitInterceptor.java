package hasoffer.job.interceptor;

import hasoffer.core.persistence.po.sys.SysAdmin;
import hasoffer.core.system.IAdminService;
import hasoffer.webcommon.context.Context;
import hasoffer.webcommon.context.StaticContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@Component
public class VisitInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(VisitInterceptor.class);

    @Resource
    IAdminService adminService;
    private Pattern PATTERN_URI_STATIC_RESOURCE = Pattern.compile("/static/.+");

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        String requestUri = httpServletRequest.getRequestURI();

        if (!PATTERN_URI_STATIC_RESOURCE.matcher(requestUri).matches()
                && !"/login".equals(requestUri)
                && !"/prelogin".equals(requestUri)) {

            // 验证登录, 初始化Context中的admin
            String ukey = Context.currentContext().getCookieAsString(StaticContext.USER_KEY);

            SysAdmin admin = adminService.findAdminByKey(ukey);

            if (admin == null) {
                httpServletRequest.getRequestDispatcher("/prelogin").forward(httpServletRequest, httpServletResponse);
            } else {
                // todo 增加一些其他验证
                // 比如绑定IP
                Context.currentContext().set(StaticContext.USER, admin);
            }
        }

        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("adminUser", Context.currentContext().get(StaticContext.USER));
        }
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex)
            throws Exception {

    }
}