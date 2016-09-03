package hasoffer.core.persistence.po.ptm.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.ptm.PtmProduct;

/**
 * Created by glx on 2015/10/13.
 */
public class PtmProductUpdater extends Updater<Long, PtmProduct> {
	public PtmProductUpdater(Long aLong) {
		super(PtmProduct.class, aLong);
	}
}
