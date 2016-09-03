package hasoffer.core.persistence.po.sys;

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;
import java.util.Date;

/**
 * Date : 2016/3/17
 * Function :
 */
@Entity
public class SysConfig implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createTime;

    private String name;

    private String val;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysConfig sysConfig = (SysConfig) o;

        if (id != null ? !id.equals(sysConfig.id) : sysConfig.id != null) return false;
        if (createTime != null ? !createTime.equals(sysConfig.createTime) : sysConfig.createTime != null) return false;
        if (name != null ? !name.equals(sysConfig.name) : sysConfig.name != null) return false;
        return !(val != null ? !val.equals(sysConfig.val) : sysConfig.val != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (val != null ? val.hashCode() : 0);
        return result;
    }
}
