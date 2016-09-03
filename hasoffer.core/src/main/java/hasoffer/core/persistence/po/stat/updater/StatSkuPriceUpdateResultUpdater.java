package hasoffer.core.persistence.po.stat.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.stat.StatSkuPriceUpdateResult;

public class StatSkuPriceUpdateResultUpdater extends Updater<String, StatSkuPriceUpdateResult> {
    public StatSkuPriceUpdateResultUpdater(String aLong) {
        super(StatSkuPriceUpdateResult.class, aLong);
    }
}
