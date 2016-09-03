package hasoffer.core.persistence.po.ptm.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.ptm.PtmCmpSku2;

/**
 * Created on 2016/8/1.
 */
public class PtmCmpSku2Updater extends Updater<Long, PtmCmpSku2> {
    public PtmCmpSku2Updater(Long aLong) {
        super(PtmCmpSku2.class, aLong);
    }
}
