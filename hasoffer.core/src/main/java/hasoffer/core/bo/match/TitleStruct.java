package hasoffer.core.bo.match;

import java.util.ArrayList;
import java.util.List;

/**
 * Date : 2016/6/16
 * Function :
 */
public class TitleStruct {

    String title;

    List<String> brandTag; // 品牌
    List<String> modelTag; // 型号
    List<String> cateTag; // 类别
    List<String> colorTag; // 颜色
    List<String> sizeTag; // 大小

    public TitleStruct(String title) {
        this.title = title;
        this.brandTag = new ArrayList<String>();
        this.modelTag = new ArrayList<String>();
        this.cateTag = new ArrayList<String>();
        this.colorTag = new ArrayList<String>();
        this.sizeTag = new ArrayList<String>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getBrandTag() {
        return brandTag;
    }

    public void setBrandTag(List<String> brandTag) {
        this.brandTag = brandTag;
    }

    public List<String> getModelTag() {
        return modelTag;
    }

    public void setModelTag(List<String> modelTag) {
        this.modelTag = modelTag;
    }

    public List<String> getCateTag() {
        return cateTag;
    }

    public void setCateTag(List<String> cateTag) {
        this.cateTag = cateTag;
    }

    public List<String> getColorTag() {
        return colorTag;
    }

    public void setColorTag(List<String> colorTag) {
        this.colorTag = colorTag;
    }

    public List<String> getSizeTag() {
        return sizeTag;
    }

    public void setSizeTag(List<String> sizeTag) {
        this.sizeTag = sizeTag;
    }
}
