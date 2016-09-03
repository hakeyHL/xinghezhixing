package hasoffer.core.persistence.po.admin;

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "report_ordersatas")
public class OrderStatsAnalysisPO implements Identifiable<Integer> {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String affID;

    @Column(length = 20, nullable = false)
    private String webSite;


    @Column(length = 20, nullable = false)
    private String channel;

    @Column(length = 100)
    private String userId;

    /**
     * 新用户（NEW）还是老用户（OLD）。
     */
    @Column(length = 10, nullable = false)
    private String userType;

    @Column(length = 20, nullable = false)
    private String orderId;

    private Date orderTime;

    /**
     * 比价订单或者流量劫持订单（SHOP/REDI）
     */
    @Column(length = 10)
    private String orderType;

    @Column(length = 20)
    private String orderStatus;

    private String title;

    private String productId;

    private String category;

    private BigDecimal saleAmount;

    private BigDecimal commissionRate;

    private BigDecimal tentativeAmount;

    private Date logTime = new Date();

    private String deviceId;

    private Date deviceRegTime;

    private String version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAffID() {
        return affID;
    }

    public void setAffID(String affID) {
        this.affID = affID;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public BigDecimal getTentativeAmount() {
        return tentativeAmount;
    }

    public void setTentativeAmount(BigDecimal tentativeAmount) {
        this.tentativeAmount = tentativeAmount;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getDeviceRegTime() {
        return deviceRegTime;
    }

    public void setDeviceRegTime(Date deviceRegTime) {
        this.deviceRegTime = deviceRegTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
