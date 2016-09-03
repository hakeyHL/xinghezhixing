package hasoffer.core.persistence.po.msp.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.msp.MspProductJob;

public class MspProductJobUpdater extends Updater<Long, MspProductJob> {
	public MspProductJobUpdater(Long aLong) {
		super(MspProductJob.class, aLong);
	}
}
