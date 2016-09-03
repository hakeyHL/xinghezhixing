package hasoffer.core.persistence.po.msp;


import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;
import java.util.Date;

/**
 * Created on 2015/12/7.
 */
@Entity
public class MspProductJob implements Identifiable<Long> {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private long categoryId; // MspCategory # id

	@Column(unique = true, nullable = false)
	private String sourceId;
	private String url;

	private long ptmProductId; // 如果抓取成功，则对应ptmproduct 的哪个商品
	private Date processTime = TimeUtils.nowDate();
	private Date createTime = TimeUtils.nowDate();

	public MspProductJob() {
	}

	public MspProductJob(long categoryId, String sourceId, String url) {
		this.categoryId = categoryId;
		this.sourceId = sourceId;
		this.url = url;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getPtmProductId() {
		return ptmProductId;
	}

	public void setPtmProductId(long ptmProductId) {
		this.ptmProductId = ptmProductId;
	}

	public Date getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		MspProductJob that = (MspProductJob) o;

		if (categoryId != that.categoryId) { return false; }
		if (ptmProductId != that.ptmProductId) { return false; }
		if (id != null ? !id.equals(that.id) : that.id != null) { return false; }
		if (sourceId != null ? !sourceId.equals(that.sourceId) : that.sourceId != null) { return false; }
		if (url != null ? !url.equals(that.url) : that.url != null) { return false; }
		if (processTime != null ? !processTime.equals(that.processTime) : that.processTime != null) { return false; }
		return !(createTime != null ? !createTime.equals(that.createTime) : that.createTime != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (int) (categoryId ^ (categoryId >>> 32));
		result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
		result = 31 * result + (url != null ? url.hashCode() : 0);
		result = 31 * result + (int) (ptmProductId ^ (ptmProductId >>> 32));
		result = 31 * result + (processTime != null ? processTime.hashCode() : 0);
		result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
		return result;
	}
}
