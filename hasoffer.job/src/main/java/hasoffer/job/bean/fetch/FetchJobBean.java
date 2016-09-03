package hasoffer.job.bean.fetch;

import hasoffer.job.service.IWebSiteFetchService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Date;

public class FetchJobBean extends QuartzJobBean {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(FetchJobBean.class);

    @Resource
    private IWebSiteFetchService webSiteFetchService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("FetchJobBean is run at {}" ,new Date());
        //System.out.println(new Date()+":任务执行。");
        if (logger.isDebugEnabled()) {
            logger.debug("executeInternal(JobExecutionContext context) - start");
        }

        webSiteFetchService.fetchProduct2Mongodb();

        if (logger.isDebugEnabled()) {
            logger.debug("executeInternal(JobExecutionContext context={}) - end", context);
        }
    }

}
