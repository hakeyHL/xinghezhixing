package hasoffer.fetch.sites.paytm.model;

import java.util.List;

/**
 * Created on 2016/3/15.
 */
public class PaytmProductListHelper {

    private String total_count;
    private List<PaytmProduct> grid_layout;

    public List<PaytmProduct> getGrid_layout() {
        return grid_layout;
    }

    public void setGrid_layout(List<PaytmProduct> grid_layout) {
        this.grid_layout = grid_layout;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }
}
