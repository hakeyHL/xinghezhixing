package hasoffer.api.interceptor;

import hasoffer.api.controller.vo.DeviceInfoVo;
import hasoffer.api.helper.ClientHelper;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.UrlUtils;
import hasoffer.core.user.IDeviceService;
import hasoffer.webcommon.context.Context;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class ClientRequestInterceptor implements HandlerInterceptor {

    @Resource
    IDeviceService deviceService;

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        String requestVersion = Context.currentContext().getHeader("request-version");

        // todo 暂时没有request-version的请求都通过，后期升级客户端后，禁止访问
        if (StringUtils.isEmpty(requestVersion)) {
            return true;
        }

        if (!NumberUtils.isDigits(requestVersion)) {
            return false;
        } else {
            int version = Integer.valueOf(requestVersion);
            if (version < 1) {
                return false;
            }
        }

        String requestToken = Context.currentContext().getHeader("request-token");

        if (StringUtils.isEmpty(requestToken) || requestToken.length() != 32) {
            return false;
        }

        DeviceInfoVo deviceInfoVo = (DeviceInfoVo) Context.currentContext().get(Context.DEVICE_INFO);

        String uri = httpServletRequest.getRequestURI();

        Map<String, String> params = UrlUtils.getParamsByQuery(httpServletRequest.getQueryString());

        String client_key = ClientHelper.getRequestKey(deviceInfoVo, params, uri);
        if (!requestToken.equals(client_key)) {
            return false;
        }

        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex)
            throws Exception {

    }
}