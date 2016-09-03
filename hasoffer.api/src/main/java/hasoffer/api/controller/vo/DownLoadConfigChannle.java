package hasoffer.api.controller.vo;

/**
 * Created by hs on 2016年08月18日.
 * Time 17:22
 */
public class DownLoadConfigChannle {
    private String channel;
    private String deepLink;

    public DownLoadConfigChannle() {
    }

    public DownLoadConfigChannle(String channel, String deepLink) {
        this.channel = channel;
        this.deepLink = deepLink;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }
}
