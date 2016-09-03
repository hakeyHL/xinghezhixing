package hasoffer.core.persistence.po.ptm;

import hasoffer.core.persistence.dbm.osql.Identifiable;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * 商品类别信息表
 * Created on 2014/7/25.
 */
@Entity
public class PtmCategory3 implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long parentId;
    private int level;

    private String name;

    private long skuid;

    @ColumnDefault(value = "0")
    private long hasofferCateogryId;

    public PtmCategory3() {
    }

    public PtmCategory3(long parentId, String name) {
        this.parentId = parentId;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getSkuid() {
        return skuid;
    }

    public void setSkuid(long skuid) {
        this.skuid = skuid;
    }

    public long getHasofferCateogryId() {
        return hasofferCateogryId;
    }

    public void setHasofferCateogryId(long hasofferCateogryId) {
        this.hasofferCateogryId = hasofferCateogryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PtmCategory3 that = (PtmCategory3) o;

        if (parentId != that.parentId) return false;
        if (level != that.level) return false;
        if (skuid != that.skuid) return false;
        if (hasofferCateogryId != that.hasofferCateogryId) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (parentId ^ (parentId >>> 32));
        result = 31 * result + level;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (skuid ^ (skuid >>> 32));
        result = 31 * result + (int) (hasofferCateogryId ^ (hasofferCateogryId >>> 32));
        return result;
    }
}
