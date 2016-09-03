package hasoffer.core.persistence.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created on 2016/8/1.
 */
@Document(collection = "PtmProductDescription")
public class PtmProductDescription {

    @Id
    private long id;

    private String jsonParam;//参数

    private String jsonDescription;

    public String getJsonDescription() {
        return jsonDescription;
    }

    public void setJsonDescription(String jsonDescription) {
        this.jsonDescription = jsonDescription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJsonParam() {
        return jsonParam;
    }

    public void setJsonParam(String jsonParam) {
        this.jsonParam = jsonParam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PtmProductDescription that = (PtmProductDescription) o;

        if (id != that.id) return false;
        if (jsonParam != null ? !jsonParam.equals(that.jsonParam) : that.jsonParam != null) return false;
        return !(jsonDescription != null ? !jsonDescription.equals(that.jsonDescription) : that.jsonDescription != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (jsonParam != null ? jsonParam.hashCode() : 0);
        result = 31 * result + (jsonDescription != null ? jsonDescription.hashCode() : 0);
        return result;
    }
}
