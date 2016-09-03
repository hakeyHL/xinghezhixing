package hasoffer.core.bo.user;

/**
 * Date : 2016/2/23
 * Function :
 */
public class DeviceEventBo {

    private String event;

    private String info;

    public DeviceEventBo(String event, String info) {
        this.event = event;
        this.info = info;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
