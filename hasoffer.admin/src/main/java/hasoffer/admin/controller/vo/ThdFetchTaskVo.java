package hasoffer.admin.controller.vo;

import java.util.Date;

/**
 * Created on 2016/2/24.
 */
public class ThdFetchTaskVo {

    private Long id;
    private long ptmCateId;
    private String categoryName;
    private String website;
    private int count;
    private Date lastProcessTime;
    private int priority;
    private String taskStatus;

    public ThdFetchTaskVo() {
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getPtmCateId() {
        return ptmCateId;
    }

    public void setPtmCateId(long ptmCateId) {
        this.ptmCateId = ptmCateId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getLastProcessTime() {
        return lastProcessTime;
    }

    public void setLastProcessTime(Date lastProcessTime) {
        this.lastProcessTime = lastProcessTime;
    }
}
