package hasoffer.webcommon.advice;

import hasoffer.core.exception.BaseException;
import hasoffer.core.exception.UnknownException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glx on 2015/10/15.
 */

//@ControllerAdvice
//不使用，异常统一使用ExceptionFilter处理
public class ExceptionHandlerAdvice {
    @ExceptionHandler(value = Exception.class)
    public ModelAndView exception(Exception ex, WebRequest request) {
        BaseException baseException = null;

        if (ex instanceof org.springframework.web.util.NestedServletException) {
            ex = (Exception) ex.getCause();
        }

        if (ex instanceof BaseException) {
            baseException = (BaseException) ex;
        } else {
            ex.printStackTrace();
            baseException = new UnknownException(ex.getMessage());
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("_errorCode", baseException.getErrorCode());
        map.put("_message", baseException.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addAllObjects(map);
        modelAndView.setViewName("error");

        return modelAndView;
    }
}