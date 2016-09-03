package hasoffer.core.persistence.po.app;

import hasoffer.core.persistence.dbm.osql.Identifiable;
import hasoffer.core.persistence.enums.BannerFrom;

import javax.persistence.*;
import java.util.Date;

/**
 * Created on 2016/6/20.
 */
@Entity
public class AppBanner implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;//图片路径
    @Column(columnDefinition = "text")
    private String linkUrl;//跳转路径

    private Date createTime;//创建时间
    private Date deadline;//最大的有效期 默认值：创建时间+7天

    @Enumerated(EnumType.STRING)
    private BannerFrom bannerFrom;//banner来源
    @Column(nullable = false)
    private String sourceId;//用来记录banner来源的id

    @Column(nullable = false)
    private long rank;//用于手工调整该条banner的优先级

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public BannerFrom getBannerFrom() {
        return bannerFrom;
    }

    public void setBannerFrom(BannerFrom bannerFrom) {
        this.bannerFrom = bannerFrom;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppBanner appBanner = (AppBanner) o;

        if (rank != appBanner.rank) return false;
        if (id != null ? !id.equals(appBanner.id) : appBanner.id != null) return false;
        if (imageUrl != null ? !imageUrl.equals(appBanner.imageUrl) : appBanner.imageUrl != null) return false;
        if (linkUrl != null ? !linkUrl.equals(appBanner.linkUrl) : appBanner.linkUrl != null) return false;
        if (createTime != null ? !createTime.equals(appBanner.createTime) : appBanner.createTime != null) return false;
        if (deadline != null ? !deadline.equals(appBanner.deadline) : appBanner.deadline != null) return false;
        if (bannerFrom != appBanner.bannerFrom) return false;
        if (sourceId != null ? !sourceId.equals(appBanner.sourceId) : appBanner.sourceId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (linkUrl != null ? linkUrl.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (deadline != null ? deadline.hashCode() : 0);
        result = 31 * result + (bannerFrom != null ? bannerFrom.hashCode() : 0);
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        result = 31 * result + (int) (rank ^ (rank >>> 32));
        return result;
    }
}
