package hasoffer.job.bean.push;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.user.IPriceOffNoticeService;
import hasoffer.data.redis.IRedisListService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created on 2016/8/31.
 */
public class RePushPriceOffNoticeJobBean extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(RePushPriceOffNoticeJobBean.class);
    private static final String PUSH_FAIL_PRICEOFFNOTICE_ID = "PUSH_FAIL_PRICEOFFNOTICE_ID";

    @Resource
    IRedisListService redisListService;
    @Resource
    IPriceOffNoticeService priceOffNoticeService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        logger.info("RePushPriceOffNoticeJobBean will start at {}", new Date());
        //此处需要将缓存中的push失败的priceOffId重新push一遍
        Object pop = redisListService.pop(PUSH_FAIL_PRICEOFFNOTICE_ID);
        System.out.println("repush pop get null");
        while (pop != null) {
            long priceOffNoticeId = Long.parseLong((String) pop);
            System.out.println("send repush for " + priceOffNoticeId);

            //每天11,14,22点重发，最后一次失败不在缓存key
            if (TimeUtils.getHour() < 20) {
                priceOffNoticeService.pushFailRePush(priceOffNoticeId, true);
                System.out.println("repush cache fail true " + TimeUtils.nowDate());
            }
            {
                priceOffNoticeService.pushFailRePush(priceOffNoticeId, false);
                System.out.println("repush don't cache fail true " + TimeUtils.nowDate());
            }

        }

        logger.info("RePushPriceOffNoticeJobBean will stop at {}", new Date());
    }

}
