package hasoffer.core.persistence.po.ptm.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;

/**
 * Created by glx on 2015/10/13.
 */
public class PtmCmpSkuUpdater extends Updater<Long, PtmCmpSku> {
	public PtmCmpSkuUpdater(Long aLong) {
		super(PtmCmpSku.class, aLong);
	}
}
