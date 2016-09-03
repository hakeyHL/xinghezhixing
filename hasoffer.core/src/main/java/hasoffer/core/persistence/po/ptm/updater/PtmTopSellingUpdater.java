package hasoffer.core.persistence.po.ptm.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.ptm.PtmTopSelling;

public class PtmTopSellingUpdater extends Updater<Long, PtmTopSelling> {
    public PtmTopSellingUpdater(Long aLong) {
        super(PtmTopSelling.class, aLong);
    }
}
