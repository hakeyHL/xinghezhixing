package hasoffer.core.persistence.po.urm;

import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Date : 2016/4/7
 * Function :
 */
@Entity
public class UrmDeviceConfig implements Identifiable<String> {

    @Id
    @Column(unique = true, nullable = false)
    private String id;//device id

    private boolean showToast;

    private boolean showPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isShowToast() {
        return showToast;
    }

    public void setShowToast(boolean showToast) {
        this.showToast = showToast;
    }

    public boolean isShowPrice() {
        return showPrice;
    }

    public void setShowPrice(boolean showPrice) {
        this.showPrice = showPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrmDeviceConfig that = (UrmDeviceConfig) o;

        if (showToast != that.showToast) return false;
        if (showPrice != that.showPrice) return false;
        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (showToast ? 1 : 0);
        result = 31 * result + (showPrice ? 1 : 0);
        return result;
    }
}
