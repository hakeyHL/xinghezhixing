package hasoffer.core.persistence.po.msp;

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;

/**
 * Created by chevy on 2015/12/7.
 */
@Entity
public class MspCategory implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long parentId;

    private String name;

    private String url;

    private String imageUrl;

    private String groupName;// 组名 - 把三级目录看成两级目录
    private int proCount;

    private long ptmCategoryId; // 对应的 PtmCategory id

    private boolean compared;

    public MspCategory() {
    }

    public MspCategory(long parentId, String name, String url, String imageUrl, String groupName) {
        this.parentId = parentId;
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
        this.groupName = groupName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getProCount() {
        return proCount;
    }

    public void setProCount(int proCount) {
        this.proCount = proCount;
    }

    public long getPtmCategoryId() {
        return ptmCategoryId;
    }

    public void setPtmCategoryId(long ptmCategoryId) {
        this.ptmCategoryId = ptmCategoryId;
    }

    public boolean isCompared() {
        return compared;
    }

    public void setCompared(boolean compared) {
        this.compared = compared;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MspCategory that = (MspCategory) o;

        if (parentId != that.parentId) return false;
        if (proCount != that.proCount) return false;
        if (ptmCategoryId != that.ptmCategoryId) return false;
        if (compared != that.compared) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) return false;
        return !(groupName != null ? !groupName.equals(that.groupName) : that.groupName != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (parentId ^ (parentId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
        result = 31 * result + proCount;
        result = 31 * result + (int) (ptmCategoryId ^ (ptmCategoryId >>> 32));
        result = 31 * result + (compared ? 1 : 0);
        return result;
    }
}
