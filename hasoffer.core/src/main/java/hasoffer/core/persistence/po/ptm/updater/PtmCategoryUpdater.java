package hasoffer.core.persistence.po.ptm.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.ptm.PtmCategory;

/**
 * Created by glx on 2015/10/13.
 */
public class PtmCategoryUpdater extends Updater<Long, PtmCategory> {
	public PtmCategoryUpdater(Long aLong) {
		super(PtmCategory.class, aLong);
	}
}
