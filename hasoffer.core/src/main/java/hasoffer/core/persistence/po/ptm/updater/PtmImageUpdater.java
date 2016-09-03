package hasoffer.core.persistence.po.ptm.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.ptm.PtmImage;

public class PtmImageUpdater extends Updater<Long, PtmImage> {
    public PtmImageUpdater(Long aLong) {
        super(PtmImage.class, aLong);
    }
}
