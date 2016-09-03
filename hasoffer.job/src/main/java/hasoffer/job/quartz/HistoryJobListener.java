package hasoffer.job.quartz;

import hasoffer.job.quartz.bean.JobExeHistory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class HistoryJobListener extends JobListenerSupport {
    private static final Logger logger = LoggerFactory.getLogger(HistoryJobListener.class);

    @Override
    public String getName() {
        return "HistoryTriggerListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        logger.info("== Job will run: {}", context.getJobDetail().getKey().getName());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        super.jobExecutionVetoed(context);
        logger.warn("== Job will be vetoed: {}", context.getJobDetail().getKey());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

        JobExeHistory history = new JobExeHistory();
        history.setExeTime(context.getJobRunTime());
        history.setCurTime(context.getFireTime());
        history.setJobName(context.getJobDetail().getKey().getName());

        StringBuilder bufBuilder = new StringBuilder();
        Set keySet = context.getMergedJobDataMap().keySet();
        for (Object object : keySet) {
            bufBuilder.append(object).append(":").append(context.getMergedJobDataMap().get(object)).append("|");
        }
        history.setData(bufBuilder.toString());

        if (jobException != null) {
            history.setResult(jobException.getMessage());
            logger.error("== Job completed with exception: {},runtime:{},data:{}, result:{}", context.getJobDetail().getKey().getName(), context.getJobRunTime(), bufBuilder.toString(), jobException);
        } else {
            logger.info("== Job completed: {},runtime:{},data:{}", context.getJobDetail().getKey().getName(), context.getJobRunTime(), bufBuilder.toString());
        }

    }
}
