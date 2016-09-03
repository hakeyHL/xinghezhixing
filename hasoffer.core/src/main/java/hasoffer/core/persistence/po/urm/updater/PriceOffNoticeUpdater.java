package hasoffer.core.persistence.po.urm.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.urm.PriceOffNotice;

/**
 * Created on 2016/8/30.
 */
public class PriceOffNoticeUpdater extends Updater<Long, PriceOffNotice> {
    public PriceOffNoticeUpdater(Long id) {
        super(PriceOffNotice.class, id);
    }
}
