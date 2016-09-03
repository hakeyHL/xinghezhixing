package hasoffer.affiliate.model;

public class AffiliateOrder {
    private String affID;
    private String price;
    private String category;
    private String title;
    private String productId;
    private String status;
    private String affiliateOrderItemId;
    private String orderDate;
    private String commissionRate;
    private String affExtParam1;
    private String affExtParam2;
    private String affExtParam3;
    private String salesChannel;
    private String customerType;

    private Sale sales;

    private TentativeCommission tentativeCommission;

    public String getAffID() {
        return affID;
    }

    public void setAffID(String affID) {
        this.affID = affID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Sale getSales() {
        return sales;
    }

    public void setSales(Sale sales) {
        this.sales = sales;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAffiliateOrderItemId() {
        return affiliateOrderItemId;
    }

    public void setAffiliateOrderItemId(String affiliateOrderItemId) {
        this.affiliateOrderItemId = affiliateOrderItemId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(String commissionRate) {
        this.commissionRate = commissionRate;
    }

    public TentativeCommission getTentativeCommission() {
        return tentativeCommission;
    }

    public void setTentativeCommission(TentativeCommission tentativeCommission) {
        this.tentativeCommission = tentativeCommission;
    }

    public String getAffExtParam1() {
        return affExtParam1;
    }

    public void setAffExtParam1(String affExtParam1) {
        this.affExtParam1 = affExtParam1;
    }

    public String getAffExtParam2() {
        return affExtParam2;
    }

    public void setAffExtParam2(String affExtParam2) {
        this.affExtParam2 = affExtParam2;
    }

    public String getAffExtParam3() {
        return affExtParam3;
    }

    public void setAffExtParam3(String affExtParam3) {
        this.affExtParam3 = affExtParam3;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getTentativeAmount() {
        if(tentativeCommission==null){
            return null;
        }
        return tentativeCommission.getAmount();
    }


    public String getTentativeCurrency() {
        if(tentativeCommission==null){
            return null;
        }
        return tentativeCommission.getCurrency();
    }

    public String getSaleAmount() {
        if(sales==null){
            return null;
        }
        return sales.getAmount();
    }

    public String getSaleCurrency() {
        if(sales==null){
            return null;
        }
        return sales.getCurrency();
    }

    private class Sale {
        private String amount;
        private String currency;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    private class TentativeCommission {
        private String amount;
        private String currency;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }


}
