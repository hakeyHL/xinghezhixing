package hasoffer.core.bo.push;

/**
 * Date : 2016/4/27
 * Function :
 */
public class AppMsgDisplay {

    private String outTitle;

    private String title;

    private String content;

    public AppMsgDisplay(String outTitle, String title, String content) {
        this.outTitle = outTitle;
        this.title = title;
        this.content = content;
    }

    public String getOutTitle() {
        return outTitle;
    }

    public void setOutTitle(String outTitle) {
        this.outTitle = outTitle;
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
}
