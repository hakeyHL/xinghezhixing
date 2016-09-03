package hasoffer.api.controller.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hs on 2016年08月29日.
 * Time 18:16
 * 价格曲线vo对象
 */
public class PriceCurveVo {
    List<Long> showY;
    List<PriceCurveXYVo> priceXY = new ArrayList<PriceCurveXYVo>();
    private List<String> showX;
    private Long startPoint;//最低点 ,起始数据点
    private Long endPoint;//终止数据点
    private int distanceX2X;

    public PriceCurveVo(List<String> showX, List<Long> showY, List<PriceCurveXYVo> priceXY, Long startPoint, Long endPoint) {
        this.showX = showX;
        this.showY = showY;
        this.priceXY = priceXY;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public PriceCurveVo() {
    }

    public Long getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Long startPoint) {
        this.startPoint = startPoint;
    }

    public Long getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Long endPoint) {
        this.endPoint = endPoint;
    }

    public List<Long> getShowY() {
        return showY;
    }

    public void setShowY(List<Long> showY) {
        this.showY = showY;
    }

    public List<String> getShowX() {
        return showX;
    }

    public void setShowX(List<String> showX) {
        this.showX = showX;
    }

    public List<PriceCurveXYVo> getPriceXY() {
        return priceXY;
    }

    public void setPriceXY(List<PriceCurveXYVo> priceXY) {
        this.priceXY = priceXY;
    }

    public int getDistanceX2X() {
        return distanceX2X;
    }

    public void setDistanceX2X(int distanceX2X) {
        this.distanceX2X = distanceX2X;
    }
}
