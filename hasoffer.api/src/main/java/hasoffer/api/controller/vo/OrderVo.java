package hasoffer.api.controller.vo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hs on 2016/6/16.
 * 订单详情vo
 */
public class OrderVo {
    private  String channel;
    private  String orderId;
    private String status;
    private BigDecimal account;
    private BigDecimal total;
    private Date orderTime;
    private  String createTime;
    private String website;
    public OrderVo() {
    }

    public OrderVo(BigDecimal account, String channel, String orderId, BigDecimal rate, String status) {
        this.account = account;
        this.channel = channel;
        this.orderId = orderId;
        this.status = status;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCreateTime() {
        return new SimpleDateFormat("MM/dd/YYYY HH:mm:ss").format(this.orderTime);
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public BigDecimal getAccount() {
        return account;
    }

    public void setAccount(BigDecimal account) {
        this.account = account;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderVo{" +
                "account=" + account +
                ", channel='" + channel + '\'' +
                ", orderId='" + orderId + '\'' +
                ", status=" + status +
                '}';
    }

}
