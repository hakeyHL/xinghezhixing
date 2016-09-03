package hasoffer.core.persistence.po.ptm;

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.*;

@Entity
public class PtmStdSkuValue implements Identifiable<Long> {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long stdSkuId; // PtmStdSku @ id

    private String stdDefId; // ptm_std_def id

    private String stdName;

    private String stdValue;

    private PtmStdSkuValue() {
    }

    public PtmStdSkuValue(long stdSkuId, String stdDefId, String stdName, String stdValue) {
        this();
        this.stdSkuId = stdSkuId;
        this.stdDefId = stdDefId;
        this.stdName = stdName;
        this.stdValue = stdValue;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public long getStdSkuId() {
        return stdSkuId;
    }

    public void setStdSkuId(long stdSkuId) {
        this.stdSkuId = stdSkuId;
    }

    public String getStdDefId() {
        return stdDefId;
    }

    public void setStdDefId(String stdDefId) {
        this.stdDefId = stdDefId;
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName;
    }

    public String getStdValue() {
        return stdValue;
    }

    public void setStdValue(String stdValue) {
        this.stdValue = stdValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PtmStdSkuValue that = (PtmStdSkuValue) o;

        if (stdSkuId != that.stdSkuId) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (stdDefId != null ? !stdDefId.equals(that.stdDefId) : that.stdDefId != null) return false;
        if (stdName != null ? !stdName.equals(that.stdName) : that.stdName != null) return false;
        return !(stdValue != null ? !stdValue.equals(that.stdValue) : that.stdValue != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (stdSkuId ^ (stdSkuId >>> 32));
        result = 31 * result + (stdDefId != null ? stdDefId.hashCode() : 0);
        result = 31 * result + (stdName != null ? stdName.hashCode() : 0);
        result = 31 * result + (stdValue != null ? stdValue.hashCode() : 0);
        return result;
    }
}
