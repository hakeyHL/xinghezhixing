package hasoffer.affiliate.affs.snapdeal.model;

import java.math.BigDecimal;
import java.util.Date;

public class SnapDealAffiliateOrder {

    private String product;
    private String category;
    private String orderCode;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal sale;
    private BigDecimal commissionRate;
    private BigDecimal commissionEarned;
    private Date dateTime;

    /**
     * 渠道
     */
    private String affiliateSubId1;

    /**
     * 设备ID
     */
    private String affiliateSubId2;

    /**
     *  暂定用户ID
     */
    private String affiliateSubId3;

    /**
     * 订单状态
     */
    private String status;

    private String userType;
    private String deviceType;



    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSale() {
        return sale;
    }

    public void setSale(BigDecimal sale) {
        this.sale = sale;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public BigDecimal getCommissionEarned() {
        return commissionEarned;
    }

    public void setCommissionEarned(BigDecimal commissionEarned) {
        this.commissionEarned = commissionEarned;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getAffiliateSubId1() {
        return affiliateSubId1;
    }

    public void setAffiliateSubId1(String affiliateSubId1) {
        this.affiliateSubId1 = affiliateSubId1;
    }

    public String getAffiliateSubId2() {
        return affiliateSubId2;
    }

    public void setAffiliateSubId2(String affiliateSubId2) {
        this.affiliateSubId2 = affiliateSubId2;
    }

    public String getAffiliateSubId3() {
        return affiliateSubId3;
    }

    public void setAffiliateSubId3(String affiliateSubId3) {
        this.affiliateSubId3 = affiliateSubId3;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    }