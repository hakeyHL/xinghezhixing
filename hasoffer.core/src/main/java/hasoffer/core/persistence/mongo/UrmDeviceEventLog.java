package hasoffer.core.persistence.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Date : 2016/1/15
 * Function :
 */
@Document(collection = "UrmDeviceEventLog")
public class UrmDeviceEventLog {

    @Id
    private String id;

    private String requestId;
    private String deviceId;

    private String event;

    private String info;

    @PersistenceConstructor
    UrmDeviceEventLog() {
    }

    public UrmDeviceEventLog(String requestId, String deviceId, String event, String info) {
        this.requestId = requestId;
        this.event = event;
        this.info = info;
        this.deviceId = deviceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UrmDeviceEventLog that = (UrmDeviceEventLog) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) return false;
        if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) return false;
        if (event != null ? !event.equals(that.event) : that.event != null) return false;
        return !(info != null ? !info.equals(that.info) : that.info != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (requestId != null ? requestId.hashCode() : 0);
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + (event != null ? event.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }
}
