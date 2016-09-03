package hasoffer.api.listener;

import hasoffer.api.worker.DeviceRequestSaveWorker;
import hasoffer.base.utils.DaemonThreadFactory;
import hasoffer.core.user.IDeviceService;
import hasoffer.core.user.impl.DeviceServiceImpl;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationListener extends ContextLoaderListener {

    //获取spring注入的bean对象
    private WebApplicationContext springContext;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        serverInitialized();
    }

    private void serverInitialized() {
        ExecutorService es = Executors.newCachedThreadPool();

//        ISearchService searchService = springContext.getBean(SearchServiceImpl.class);
//        es.execute(DaemonThreadFactory.create(new SearchLogSaveWorker(searchService)));
//
        IDeviceService deviceService = springContext.getBean(DeviceServiceImpl.class);
        es.execute(DaemonThreadFactory.create(new DeviceRequestSaveWorker(deviceService)));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
