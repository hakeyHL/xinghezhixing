package hasoffer.webcommon.listener;

import hasoffer.base.config.AppConfig;
import hasoffer.base.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicLong;

public class ServerSessionListener implements HttpSessionListener {
    private Logger logger = LoggerFactory.getLogger(ServerSessionListener.class);

    private final static int SESSION_DISPLAY_COUNT = Integer.valueOf(AppConfig.get("SESSION_DISPLAY_COUNT"));

    private AtomicLong curCount = new AtomicLong(0);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().setMaxInactiveInterval(20);
        curCount.addAndGet(1);
        show();
    }

    private void show() {
        long count = curCount.get();
        if (count % SESSION_DISPLAY_COUNT == 0) {
            System.out.println(String.format("[%s] - current session count : %d.", TimeUtils.parse(TimeUtils.nowDate(), "yyyy/MM/dd HH:mm:ss"), count));
            logger.info("{} - current session count : {}.", TimeUtils.parse(TimeUtils.nowDate(), "yyyy/MM/dd HH:mm:ss"), count);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        curCount.addAndGet(-1);
        show();
    }
}
