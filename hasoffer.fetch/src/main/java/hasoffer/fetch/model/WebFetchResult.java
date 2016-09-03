package hasoffer.fetch.model;

import hasoffer.base.enums.TaskStatus;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebFetchResult implements Serializable {

    private TaskStatus taskStatus;

    private Date updateDate;

    private List<ListProduct> productList = new ArrayList<ListProduct>();

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateDateStr() {
        if (updateDate == null) {
            return "";
        }
        return DateFormatUtils.format(updateDate, "yyyy-MM-dd HH:mm:ss");
    }


    public Long getlUpdateDate() {
        if (updateDate == null) {
            return 0L;
        }
        return updateDate.getTime();
    }


    public List<ListProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<ListProduct> productList) {
        this.productList = productList;
    }

    @Override
    public String toString() {
        return "WebFetchResult{" +
                "updateDate=" + updateDate +
                ", productList.size=" + productList.size() +
                '}';
    }
}
