package hasoffer.job.bean.push;

import hasoffer.core.user.IPriceOffNoticeService;
import hasoffer.data.redis.IRedisListService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/8/31.
 */
public class PriceOffNoticeJobBean extends QuartzJobBean {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(PriceOffNoticeJobBean.class);
    private static final String PRICEOFF_NOTICE_SKUID_QUEUE = "PRICEOFF_NOTICE_SKUID_QUEUE";

    @Resource
    IRedisListService redisListService;
    @Resource
    IPriceOffNoticeService priceOffNoticeService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        Long size = redisListService.size(PRICEOFF_NOTICE_SKUID_QUEUE);
        System.out.println("PRICEOFF_NOTICE_SKUID_QUEUE size = " + size);

        if (size > 0) {

            logger.info("PriceOffNoticeJobBean is run at {}", new Date());
            logger.info("Need push " + size + "sku");

            for (int i = 0; i < size; i++) {

                Long skuid = Long.parseLong((String) redisListService.pop(PRICEOFF_NOTICE_SKUID_QUEUE));
                logger.info("price off push for " + skuid);

                priceOffNoticeService.priceOffCheck(skuid);

                //每条sku推送，间隔5s
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        logger.info("PriceOffNoticeJobBean will stop at {}", new Date());
    }

}