package hasoffer.core.bo.product;

import java.util.List;

/**
 * 商品类目VO
 * Created by hs on 2016/6/20.
 */
public class CategoryVo {

    private String  name;
    private  String image;
    private Long id;
    private int hasChildren;
    private  Long  parentId;
    private  int rank;
    private  int  level;

    List<CategoryVo> categorys;

    public List<CategoryVo> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<CategoryVo> categorys) {
        this.categorys = categorys;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(int hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
