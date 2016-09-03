package hasoffer.dubbo.api.listener;

import hasoffer.base.thread.HasofferThreadFactory;
import hasoffer.dubbo.api.fetch.task.FetchKeywordWorker;
import hasoffer.dubbo.api.fetch.task.FetchUrlWorker;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchListener extends ContextLoaderListener {

    //获取spring注入的bean对象
    private WebApplicationContext springContext;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());

        initThread();
    }

    private void initThread() {

        HasofferThreadFactory factory = new HasofferThreadFactory("FetchKeywordWorker");
        ExecutorService es = Executors.newCachedThreadPool(factory);
        for (int i = 0; i < 20; i++) {
            es.execute(new FetchKeywordWorker(springContext));
        }

        factory = new HasofferThreadFactory("FetchUrlWorker");
        es = Executors.newCachedThreadPool(factory);
        for (int i = 0; i < 20; i++) {
            es.execute(new FetchUrlWorker(springContext));
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}