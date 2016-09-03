package hasoffer.job.quartz.bean;

import java.util.Date;


public class JobExeHistory {

    /**
     * 当前时间
     */
    private Date curTime;

    /**
     * 执行时间
     */
    private long exeTime;

    /**
     * job名称
     */
    private String jobName;

    /**
     * 执行结果
     */
    private String result;

    /**
     * 附加数据
     */
    private String data;

    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @param jobName the jobName to set
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the curTime
     */
    public Date getCurTime() {
        return curTime;
    }

    /**
     * @param curTime the curTime to set
     */
    public void setCurTime(Date curTime) {
        this.curTime = curTime;
    }

    /**
     * @return the exeTime
     */
    public long getExeTime() {
        return exeTime;
    }

    /**
     * @param exeTime the exeTime to set
     */
    public void setExeTime(long exeTime) {
        this.exeTime = exeTime;
    }

}
