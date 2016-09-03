package hasoffer.core.persistence.po.ptm.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.ptm.PtmCmpSkuIndex2;

/**
 * Created on 2016/5/26.
 */
public class PtmCmpSkuIndex2Updater extends Updater<Long, PtmCmpSkuIndex2> {
    public PtmCmpSkuIndex2Updater(Long aLong) {
        super(PtmCmpSkuIndex2.class, aLong);
    }
}