package hasoffer.core.persistence.po.ptm.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.ptm.PtmCmpSkuImage;

/**
 * Created on 2016/8/10.
 */
public class PtmCmpSkuImageUpdater extends Updater<Long, PtmCmpSkuImage> {
    public PtmCmpSkuImageUpdater(Long aLong) {
        super(PtmCmpSkuImage.class, aLong);
    }
}
