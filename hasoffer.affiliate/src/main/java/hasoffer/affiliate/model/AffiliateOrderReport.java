package hasoffer.affiliate.model;

import java.util.List;

public class AffiliateOrderReport {

    private String first;
    private String last;

    private String previous;
    private String next;

    private List<AffiliateOrder> orderList;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<AffiliateOrder> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<AffiliateOrder> orderList) {
        this.orderList = orderList;
    }
}
