package hasoffer.job.quartz.spring;

import hasoffer.job.common.exception.BaseException;
import hasoffer.job.quartz.bean.QuartzJobInfo;
import org.quartz.*;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("schedulerServiceImpl")
public class SchedulerServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerServiceImpl.class);

    @Autowired
    @Qualifier("qrtz_scheduler")
    private Scheduler scheduler;


    public List<QuartzJobInfo> getQrtzTriggers() {
        List<QuartzJobInfo> infos = new ArrayList<QuartzJobInfo>();
        try {
            GroupMatcher group = GroupMatcher.anyJobGroup();
            Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(group);
            for (TriggerKey triggerKey : triggerKeys) {
                QuartzJobInfo info = new QuartzJobInfo();
                Trigger trigger = scheduler.getTrigger(triggerKey);
                if (trigger == null) {
                    continue;
                }
                info.setTriggerName(triggerKey.getName());
                info.setTriggerGroup(triggerKey.getGroup());
                info.setLastFireTime(trigger.getPreviousFireTime());
                info.setNextFireTime(trigger.getNextFireTime());
                JobKey jobKey = trigger.getJobKey();
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                if (jobDetail == null) {
                    continue;
                }
                info.setJobName(jobKey.getName());
                info.setJobGroup(jobKey.getGroup());
                info.setJobClass(jobDetail.getJobClass().getName());
                JobDataMap jobDataMap = jobDetail.getJobDataMap();
                StringBuilder builder = new StringBuilder();
                Set keySet = jobDataMap.keySet();
                for (Object object : keySet) {
                    builder.append(object).append(":").append(jobDataMap.get(object)).append("|");
                }
                info.setData(builder.toString());
                TriggerState triggerState = scheduler.getTriggerState(triggerKey);
                info.setState(triggerState.name());
                infos.add(info);
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return infos;
    }

    @Transactional
    public void pauseTrigger(String triggerName, String group) {
        try {
            TriggerKey key = new TriggerKey(triggerName, group);
            this.scheduler.pauseTrigger(key);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void resumeTrigger(String triggerName, String group) {
        try {
            TriggerKey key = new TriggerKey(triggerName, group);
            this.scheduler.resumeTrigger(key);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void runNow(String triggerName, String group, Map<String, Object> datas) {
        try {
            if (datas == null) {
                logger.error("datas 不能为空！");
                throw new BaseException(BaseException.ExceptionCategory.Illegal_Parameter, "datas不能为空！");
            }
            logger.info("job execute by hand: {}, {}, {}", triggerName, group, datas.toString());
            TriggerKey key = new TriggerKey(triggerName, group);
            Trigger trigger = this.scheduler.getTrigger(key);
            JobKey jobKey = trigger.getJobKey();

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);

            if (datas.size() > 0) {
                Set<String> keySet = datas.keySet();
                for (String key1 : keySet) {
                    jobDetail.getJobDataMap().put(key1, datas.get(key1));
                }
                scheduler.addJob(jobDetail, true);
            }

            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("Fail to execute job:{}", e);
            throw new RuntimeException(e);
        }
    }

}
