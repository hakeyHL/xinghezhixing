package hasoffer.core.persistence.po.ptm;

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by chevy on 2016/8/12.
 */
@Entity
public class PtmStdDef implements Identifiable<String> {

    @Id
    @Column(unique = true, nullable = false)
    private String id;

    private String stdName; // 规格名称

    private String unitName; // 单位,计量单位

    private PtmStdDef() {
    }

    public PtmStdDef(String stdName) {
        this();
        this.stdName = stdName;
        this.id = getId(stdName);
    }

    public PtmStdDef(String stdName, String unitName) {
        this(stdName);
        this.unitName = unitName;
    }

    private String getId(String stdName) {
        return stdName.toLowerCase().trim();// 小写，去两端空格
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PtmStdDef stdDef = (PtmStdDef) o;

        if (id != null ? !id.equals(stdDef.id) : stdDef.id != null) return false;
        if (stdName != null ? !stdName.equals(stdDef.stdName) : stdDef.stdName != null) return false;
        return !(unitName != null ? !unitName.equals(stdDef.unitName) : stdDef.unitName != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (stdName != null ? stdName.hashCode() : 0);
        result = 31 * result + (unitName != null ? unitName.hashCode() : 0);
        return result;
    }
}
