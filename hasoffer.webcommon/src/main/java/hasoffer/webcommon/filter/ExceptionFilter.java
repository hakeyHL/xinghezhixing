package hasoffer.webcommon.filter;

import hasoffer.core.exception.BaseException;
import hasoffer.core.exception.UnknownException;

import javax.servlet.*;
import java.io.IOException;

/**
 * Servlet Filter implementation class InitContextFilter
 */

public class ExceptionFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Throwable ex) {
            BaseException baseException = null;
            if (ex instanceof org.springframework.web.util.NestedServletException) {
                ex = ex.getCause();
            }

            if (ex instanceof BaseException) {
                baseException = (BaseException) ex;
            } else {
                ex.printStackTrace();
                baseException = new UnknownException(ex.getMessage());
            }

            String target = "/error?_errorCode=" + baseException.getErrorCode().ordinal() + "&_message=" + baseException.getMessage();

//            request.getRequestDispatcher(target).forward(request, response);
//            ((HttpServletResponse) response).sendRedirect(target);
            return;
        }
    }

    public void destroy() {

    }
}
