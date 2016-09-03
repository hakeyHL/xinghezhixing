package hasoffer.core.bo.push;

/**
 * Date : 2016/4/27
 * Function :
 */
public class AppPushMessage {

    private AppMsgDisplay display;

    private AppMsgClick click;

    public AppPushMessage(AppMsgDisplay display, AppMsgClick click) {
        this.display = display;
        this.click = click;
    }

    public AppMsgDisplay getDisplay() {
        return display;
    }

    public void setDisplay(AppMsgDisplay display) {
        this.display = display;
    }

    public AppMsgClick getClick() {
        return click;
    }

    public void setClick(AppMsgClick click) {
        this.click = click;
    }
}
