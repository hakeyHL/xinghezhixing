package hasoffer.webcommon.interceptor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by glx on 2015/10/15.
 */
@Component
public class HttpExpiresInterceptor implements HandlerInterceptor {
    private static final Pattern EXPIRETIME = Pattern.compile("^\\d+(M|d|h|m|s)$");
    private static final Map<Character, Long> TIME_MAP = new HashMap<Character, Long>();
    private static final Pattern STATIC_PATTERN = Pattern.compile(".*\\.(css|png|jpg|jpeg|gif|js)$");
    private static final long STATIC_FILE_CACHE_TIME = 3600L*24;
    private static final String Last_Modified = "Last-Modified";
    private static final String Etag = "Etag";
    private static final String Expires = "Expires";
    private static final String Cache_Control = "Cache-Control";
    private static final String If_Modified_Since = "If-Modified-Since";
    private static final String If_None_Match = "If-None-Match";
    private static Logger logger = LoggerFactory.getLogger(HttpExpiresInterceptor.class);

    static {
        TIME_MAP.put('M', 30*24*3600L);
        TIME_MAP.put('d', 24*3600L);
        TIME_MAP.put('h', 3600L);
        TIME_MAP.put('m', 60L);
        TIME_MAP.put('s', 1L);
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        if (!this.isExpired(httpServletRequest, httpServletResponse)) {//文件没有过期，直接返回304
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
        if (this.isStaticFile(httpServletRequest, httpServletResponse) || handler instanceof ResourceHttpRequestHandler) {
            this.setCacheHeader(httpServletRequest, httpServletResponse, STATIC_FILE_CACHE_TIME);
        } else if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            HttpExpires httpExpires = null;
            Annotation[] annotations = handlerMethod.getMethod().getDeclaredAnnotations();
            if(annotations!=null) {
                for(Annotation annotation : annotations) {
                    if(annotation.annotationType().equals(HttpExpires.class)) {
                        httpExpires = (HttpExpires) annotation;
                        break;
                    }
                }
            }

            if (httpExpires != null) {
                String expireTime = StringUtils.trimToEmpty(httpExpires.value());
                if (EXPIRETIME.matcher(expireTime).matches()) {
                    long time = Long.parseLong(expireTime.substring(0, expireTime.length() - 1));
                    char unit = expireTime.charAt(expireTime.length()-1);
                    long timeAsSecond = time * TIME_MAP.get(unit);
                    this.setCacheHeader(httpServletRequest, httpServletResponse, timeAsSecond);
                } else {
                    logger.error("{}", "错误的httpexpire格式:" + expireTime);
                }
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {

    }

    private boolean isStaticFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String reqPath = httpServletRequest.getServletPath();
        reqPath = StringUtils.trimToEmpty(reqPath);

        if (STATIC_PATTERN.matcher(reqPath).matches()) {
            return true;
        }
        return false;
    }

    private boolean isExpired(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        long now = System.currentTimeMillis();
        long ifModifiedSince = httpServletRequest.getDateHeader(If_Modified_Since);
        long cacheExpireTime = ifModifiedSince + STATIC_FILE_CACHE_TIME * 1000L;
        String ifNoneMatch = httpServletRequest.getHeader(If_None_Match);

        if (now > cacheExpireTime) {//过期了
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return true;
        } else {//没有过期
            return false;
        }

        //这里忽略了ETag
    }

    private void setCacheHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, long cacheSecond) {
        long now = System.currentTimeMillis();
        String maxAgeDirective = "max-age=" + cacheSecond;
        httpServletResponse.setHeader(Cache_Control, maxAgeDirective);
        httpServletResponse.setDateHeader(Last_Modified, now);
        httpServletResponse.setDateHeader(Expires, now + cacheSecond * 1000);
        httpServletResponse.setHeader(Etag, Long.toString(now));
    }
}