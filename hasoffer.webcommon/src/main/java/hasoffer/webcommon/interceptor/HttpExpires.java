package hasoffer.webcommon.interceptor;

import java.lang.annotation.*;

/**
 * Created by glx on 2015/6/14.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpExpires {
    String  value() default "10m";//xxM, xxd, xxh, xxm, xxs
}
