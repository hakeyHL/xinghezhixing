package hasoffer.core.bo.push;

/**
 * Date : 2016/4/27
 * Function :
 */
public class AppPushBo {

    private String score;

    private String time;

    private AppPushMessage message;

    public AppPushBo(String score, String time, AppPushMessage message) {
        this.score = score;
        this.time = time;
        this.message = message;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public AppPushMessage getMessage() {
        return message;
    }

    public void setMessage(AppPushMessage message) {
        this.message = message;
    }
}
