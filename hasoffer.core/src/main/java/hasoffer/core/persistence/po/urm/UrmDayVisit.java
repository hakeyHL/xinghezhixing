package hasoffer.core.persistence.po.urm;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Date : 2016/1/21
 * Function :
 */
@Entity
public class UrmDayVisit implements Identifiable<String> {

    @Id
    @Column(unique = true, nullable = false)
    private String id; // 使用ymd 作为主键

    private long aliveDevice; // 活着的设备
    private long aliveDeviceWithShop; // 活着的设备中装有电商APP的

    private long newDevice;// 新增设备
    private long newDeviceWithShop;//新增设备中装有电商APP的

    private long visitDevice;// 当日访问过的设备 - 一定有电商app
    private long visitDeviceNew;// 当日访问过的设备中 新设备数量

    private Date updateTime = TimeUtils.nowDate();

    public UrmDayVisit(String id, long aliveDevice, long aliveDeviceWithShop,
                       long newDevice, long newDeviceWithShop,
                       long visitDevice, long visitDeviceNew) {
        this.id = id;
        this.aliveDevice = aliveDevice;
        this.aliveDeviceWithShop = aliveDeviceWithShop;
        this.newDevice = newDevice;
        this.newDeviceWithShop = newDeviceWithShop;
        this.visitDevice = visitDevice;
        this.visitDeviceNew = visitDeviceNew;
    }

    public UrmDayVisit() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public long getAliveDevice() {
        return aliveDevice;
    }

    public void setAliveDevice(long aliveDevice) {
        this.aliveDevice = aliveDevice;
    }

    public long getAliveDeviceWithShop() {
        return aliveDeviceWithShop;
    }

    public void setAliveDeviceWithShop(long aliveDeviceWithShop) {
        this.aliveDeviceWithShop = aliveDeviceWithShop;
    }

    public long getNewDevice() {
        return newDevice;
    }

    public void setNewDevice(long newDevice) {
        this.newDevice = newDevice;
    }

    public long getNewDeviceWithShop() {
        return newDeviceWithShop;
    }

    public void setNewDeviceWithShop(long newDeviceWithShop) {
        this.newDeviceWithShop = newDeviceWithShop;
    }

    public long getVisitDevice() {
        return visitDevice;
    }

    public void setVisitDevice(long visitDevice) {
        this.visitDevice = visitDevice;
    }

    public long getVisitDeviceNew() {
        return visitDeviceNew;
    }

    public void setVisitDeviceNew(long visitDeviceNew) {
        this.visitDeviceNew = visitDeviceNew;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrmDayVisit dayVisit = (UrmDayVisit) o;

        if (aliveDevice != dayVisit.aliveDevice) return false;
        if (aliveDeviceWithShop != dayVisit.aliveDeviceWithShop) return false;
        if (newDevice != dayVisit.newDevice) return false;
        if (newDeviceWithShop != dayVisit.newDeviceWithShop) return false;
        if (visitDevice != dayVisit.visitDevice) return false;
        if (visitDeviceNew != dayVisit.visitDeviceNew) return false;
        if (id != null ? !id.equals(dayVisit.id) : dayVisit.id != null) return false;
        return !(updateTime != null ? !updateTime.equals(dayVisit.updateTime) : dayVisit.updateTime != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (aliveDevice ^ (aliveDevice >>> 32));
        result = 31 * result + (int) (aliveDeviceWithShop ^ (aliveDeviceWithShop >>> 32));
        result = 31 * result + (int) (newDevice ^ (newDevice >>> 32));
        result = 31 * result + (int) (newDeviceWithShop ^ (newDeviceWithShop >>> 32));
        result = 31 * result + (int) (visitDevice ^ (visitDevice >>> 32));
        result = 31 * result + (int) (visitDeviceNew ^ (visitDeviceNew >>> 32));
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }
}
