package hasoffer.admin.controller.vo;

/**
 * Created by chevy on 2016/7/18.
 */
public class TitleCountVo {

    private String title;

    private int count;

    public TitleCountVo(String title, int count) {
        this.title = title;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "TitleCountVo{" +
                "title='" + title + '\'' +
                ", count=" + count +
                '}';
    }
}
