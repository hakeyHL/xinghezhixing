package hasoffer.core.persistence.po.ptm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by chevy on 2016/8/26.
 */
@Entity
public class PtmCateTag {

    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    private String tag;// , 隔开各个tag,这个tag将会影响solr索引，进而影响搜索结果

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PtmCateTag that = (PtmCateTag) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(tag != null ? !tag.equals(that.tag) : that.tag != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }
}
