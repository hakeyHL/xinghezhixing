package hasoffer.admin.controller.vo;

import java.util.Date;

/**
 * Created on 2016/4/11.
 */
public class UserBuyVo {

    private long id;

    private String title;

    private int count;

    private Date updateTime;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
