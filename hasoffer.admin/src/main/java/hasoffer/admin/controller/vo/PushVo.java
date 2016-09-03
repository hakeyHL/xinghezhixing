package hasoffer.admin.controller.vo;

/**
 * Created by hs on 2016年08月11日.
 * Time 16:13
 */
public class PushVo {
    private String title;
    private String content;
    private String outline;
    private String[] website;
    private String[] version;
    private String[] channel;
    private String messageType;
    private String value;
    private int number;
    private String pushType;
    private String gcmToken;

    public String[] getChannel() {
        return channel;
    }

    public void setChannel(String[] channel) {
        this.channel = channel;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String[] getWebsite() {
        return website;
    }

    public void setWebsite(String[] website) {
        this.website = website;
    }

    public String[] getVersion() {
        return version;
    }

    public void setVersion(String[] version) {
        this.version = version;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }
}
