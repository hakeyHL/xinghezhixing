package hasoffer.analysis.api.listener;

import hasoffer.core.analysis.ITagService;
import hasoffer.core.analysis.impl.TagServiceImpl;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;

public class ApplicationListener extends ContextLoaderListener {

    //获取spring注入的bean对象
    private WebApplicationContext springContext;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());

        serverInitialized();
    }

    private void serverInitialized() {
        // 初始化词典
        ITagService tagService = springContext.getBean(TagServiceImpl.class);
        tagService.loadWordDicts();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
