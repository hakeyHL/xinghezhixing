package hasoffer.core.persistence.po.sys.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.sys.SysAdmin;

public class SysAdminUpdater extends Updater<Long, SysAdmin> {
	public SysAdminUpdater(Long aLong) {
		super(SysAdmin.class, aLong);
	}
}
