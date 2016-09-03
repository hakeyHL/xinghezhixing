package hasoffer.job.bean.stat;

import hasoffer.base.model.Website;
import hasoffer.base.utils.HexDigestUtil;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.mongo.StatHijackFetch;
import hasoffer.core.persistence.mongo.StatHijackFetchCount;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatHijackFetchJobBean extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(StatHijackFetchJobBean.class);

    @Resource
    IMongoDbManager mdm;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        try {

            //4.劫持失败数
            List<Website> websiteList = new ArrayList<Website>();
            websiteList.add(Website.FLIPKART);
            websiteList.add(Website.SNAPDEAL);
            websiteList.add(Website.SHOPCLUES);

            String todayString = TimeUtils.parse(TimeUtils.today(), "yyyyMMdd");//20160530

            for (Website website : websiteList) {

                String id = HexDigestUtil.md5(website.name() + todayString); //1.当日零点做为id

                long totalAmount = getTotalAmount(website); //2.StatHijackFetch中当天的数据的总数作为当体应劫持总数
                logger.debug("getTotalAmount");
                long statusSuccessAmount = getStatusSuccessAmount(website);//3.劫持成功数
                logger.debug("getStatusSuccessAmount");
                long noIndexAmount = getNoIndexAmount(website);  //6.未收录--no_index
                logger.debug("getNoIndexAmount");
                long differentUrlAmount = getDifferentUrlAmount(website);//5.因重名失败——different_url
                logger.debug("getDifferentUrlAmount");
                long noIndexSuccessAmount = getNoIndexSuccessAmount(website);//7.no_index   result:success
                logger.debug("getNoIndexSuccessAmount");
                long noIndexFailAmount = getNoIndexFailAmount(website); //8.no_index   result:fail
                logger.debug("getNoIndexFailAmount");

                StatHijackFetchCount countObject = new StatHijackFetchCount();

                countObject.setId(id);
                countObject.setWebsite(website);
                try {
                    Date date = TimeUtils.parse(TimeUtils.parse(TimeUtils.today(), "yyyy-MM-dd"));
                    countObject.setDate(date);
                } catch (ParseException e) {

                }
                countObject.setUpdateTime(TimeUtils.now());

                countObject.setTotalAmount(totalAmount);
                countObject.setStatusSuccessAmount(statusSuccessAmount);
                countObject.setNoIndexAmount(noIndexAmount);
                countObject.setDifferentUrlAmount(differentUrlAmount);
                countObject.setNoIndexSuccessAmount(noIndexSuccessAmount);
                countObject.setNoIndexFailAmount(noIndexFailAmount);
                countObject.setStatusFailAmount(differentUrlAmount + noIndexAmount);

                mdm.save(countObject);
            }

        } catch (Exception e) {
            logger.debug("StatHijackFetchCount:任务失败,   DATE:" + new Date() + ":具体如下");
            logger.info("error msg:{}",e);
        }
    }

    private long getTotalAmount(Website website) {
        Query query = new Query();
        query.addCriteria(Criteria.where("website").is(website.name()));
        query.addCriteria(Criteria.where("lCreateTime").gt(TimeUtils.getDayStart(TimeUtils.now())));
        return mdm.count(StatHijackFetch.class, query);
    }

    private long getStatusSuccessAmount(Website website) {
        Query query = new Query();
        query.addCriteria(Criteria.where("website").is(website.name()));
        query.addCriteria(Criteria.where("status").is("SUCCESS"));
        query.addCriteria(Criteria.where("lCreateTime").gt(TimeUtils.getDayStart(TimeUtils.now())));
        return mdm.count(StatHijackFetch.class, query);
    }

    private long getNoIndexAmount(Website website) {
        Query query = new Query();
        query.addCriteria(Criteria.where("website").is(website.name()));
        query.addCriteria(Criteria.where("status").is("NO_INDEX"));
        query.addCriteria(Criteria.where("lCreateTime").gt(TimeUtils.getDayStart(TimeUtils.now())));
        return mdm.count(StatHijackFetch.class, query);
    }

    private long getDifferentUrlAmount(Website website) {
        Query query = new Query();
        query.addCriteria(Criteria.where("website").is(website.name()));
        query.addCriteria(Criteria.where("status").is("DIFFERENT_URL"));
        query.addCriteria(Criteria.where("lCreateTime").gt(TimeUtils.getDayStart(TimeUtils.now())));
        return mdm.count(StatHijackFetch.class, query);
    }

    private long getNoIndexSuccessAmount(Website website) {
        Query query = new Query();
        query.addCriteria(Criteria.where("website").is(website.name()));
        query.addCriteria(Criteria.where("status").is("NO_INDEX"));
        query.addCriteria(Criteria.where("result").is("success"));
        query.addCriteria(Criteria.where("lCreateTime").gt(TimeUtils.getDayStart(TimeUtils.now())));
        return mdm.count(StatHijackFetch.class, query);
    }

    private long getNoIndexFailAmount(Website website) {
        Query query = new Query();
        query.addCriteria(Criteria.where("website").is(website.name()));
        query.addCriteria(Criteria.where("status").is("NO_INDEX"));
        query.addCriteria(Criteria.where("result").is("fail"));
        query.addCriteria(Criteria.where("lCreateTime").gt(TimeUtils.getDayStart(TimeUtils.now())));
        return mdm.count(StatHijackFetch.class, query);
    }
}
