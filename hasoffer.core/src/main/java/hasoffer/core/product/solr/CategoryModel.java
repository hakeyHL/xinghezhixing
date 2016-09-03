package hasoffer.core.product.solr;

import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.data.solr.IIdentifiable;

/**
 * Date : 2016/1/18
 * Function :
 */
public class CategoryModel implements IIdentifiable<Long> {
    private Long id;

    private String name;

    private String tags;

    private int level;

    public CategoryModel() {
    }

    public CategoryModel(PtmCategory category) {
        this.id = category.getId();
        this.name = category.getName();
        this.tags = category.getKeyword();
        this.level = category.getLevel();
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
