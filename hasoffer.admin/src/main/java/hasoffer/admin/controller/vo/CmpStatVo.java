package hasoffer.admin.controller.vo;


import hasoffer.base.enums.MarketChannel;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 2015/12/22.
 */
public class CmpStatVo {

    private Long id;

    private String deviceId;

    private MarketChannel marketChannel;


    private String deviceYmd;

    private Set<String> useDaySet=new HashSet<String>();

    private int useDaySize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public MarketChannel getMarketChannel() {
        return marketChannel;
    }

    public void setMarketChannel(MarketChannel marketChannel) {
        this.marketChannel = marketChannel;
    }



    public String  getDeviceYmd() {
        return deviceYmd;
    }

    public void setDeviceYmd(String  deviceYmd) {
        this.deviceYmd = deviceYmd;
    }

    public Set<String> getUseDaySet() {
        return useDaySet;
    }

    public int getUseDaySize() {
        return useDaySet.size();
    }

}
