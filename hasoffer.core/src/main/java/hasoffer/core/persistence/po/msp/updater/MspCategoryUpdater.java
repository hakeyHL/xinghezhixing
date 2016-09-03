package hasoffer.core.persistence.po.msp.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.msp.MspCategory;

public class MspCategoryUpdater extends Updater<Long, MspCategory> {
	public MspCategoryUpdater(Long aLong) {
		super(MspCategory.class, aLong);
	}
}
