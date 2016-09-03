package hasoffer.core.persistence.dbm.osql;

import java.io.Serializable;

/**
 * Created by glx on 2015/5/20.
 */
public interface Identifiable<ID extends Serializable> extends Serializable{
	ID getId();
	void setId(ID id);
}