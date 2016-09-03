package hasoffer.api.controller.vo;

/**
 * Created by hs on 2016年09月01日.
 * Time 11:
 * //价格曲线X轴、Y轴显示的Vo对象
 */
public class PriceCurveXYVo {
    private String positionX;
    private Long positionY;
    private int distanceX;

    public PriceCurveXYVo() {
    }

    public PriceCurveXYVo(String positionX, Long positionY, int distanceX) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.distanceX = distanceX;
    }

    public String getPositionX() {
        return positionX;
    }

    public void setPositionX(String positionX) {
        this.positionX = positionX;
    }

    public Long getPositionY() {
        return positionY;
    }

    public void setPositionY(Long positionY) {
        this.positionY = positionY;
    }

    public int getDistanceX() {
        return distanceX;
    }

    public void setDistanceX(int distanceX) {
        this.distanceX = distanceX;
    }
}
