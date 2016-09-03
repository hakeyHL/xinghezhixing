package hasoffer.api.controller.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hs on 2016/6/16.
 * 查看返利vo
 */
public class BackDetailVo {
    private BigDecimal PendingCoins;
    private  BigDecimal   verifiedCoins;
    private List<OrderVo> transcations=new ArrayList<OrderVo>();

    public BigDecimal getPendingCoins() {
        return PendingCoins;
    }

    public void setPendingCoins(BigDecimal pendingCoins) {
        PendingCoins = pendingCoins;
    }

    public BigDecimal getVericiedCoins() {
        return verifiedCoins;
    }

    public void setVericiedCoins(BigDecimal vericiedCoins) {
        this.verifiedCoins = vericiedCoins;
    }

    public List<OrderVo> getTranscations() {
        return transcations;
    }

    public void setTranscations(List<OrderVo> transcations) {
        this.transcations = transcations;
    }
}
