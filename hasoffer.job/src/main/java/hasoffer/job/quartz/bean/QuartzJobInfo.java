package hasoffer.job.quartz.bean;

import java.util.Date;

public class QuartzJobInfo {
    private String triggerName;
    private String triggerGroup;

    private String jobClass;
    private String jobName;
    private String jobGroup;

    /**
     * 运行状态
     */
    private String state;

    /**
     * 下一次运行时间
     */
    private Date nextFireTime;

    /**
     * 上一次运行时间
     */
    private Date lastFireTime;

    private String data;

    /**
     * @return the triggerName
     */
    public String getTriggerName() {
        return triggerName;
    }

    /**
     * @param triggerName
     *            the triggerName to set
     */
    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    /**
     * @return the triggerGroup
     */
    public String getTriggerGroup() {
        return triggerGroup;
    }

    /**
     * @param triggerGroup
     *            the triggerGroup to set
     */
    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    /**
     * @return the jobClass
     */
    public String getJobClass() {
        return jobClass;
    }

    /**
     * @param jobClass
     *            the jobClass to set
     */
    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @param jobName
     *            the jobName to set
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * @return the jobGroup
     */
    public String getJobGroup() {
        return jobGroup;
    }

    /**
     * @param jobGroup
     *            the jobGroup to set
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the nextFireTime
     */
    public Date getNextFireTime() {
        return nextFireTime;
    }

    /**
     * @param nextFireTime
     *            the nextFireTime to set
     */
    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    /**
     * @return the lastFireTime
     */
    public Date getLastFireTime() {
        return lastFireTime;
    }

    /**
     * @param lastFireTime
     *            the lastFireTime to set
     */
    public void setLastFireTime(Date lastFireTime) {
        this.lastFireTime = lastFireTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "QuartzJobInfo [triggerName=" + triggerName + ", triggerGroup=" + triggerGroup + ", jobClass=" + jobClass
                + ", jobName=" + jobName + ", jobGroup=" + jobGroup + ", state=" + state + ", nextFireTime="
                + nextFireTime + ", lastFireTime=" + lastFireTime + ", data=" + data + "]";
    }

}
