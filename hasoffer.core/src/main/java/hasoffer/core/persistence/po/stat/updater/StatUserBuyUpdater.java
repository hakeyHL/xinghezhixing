package hasoffer.core.persistence.po.stat.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.stat.StatUserBuy;

/**
 * Created on 2016/4/11.
 */
public class StatUserBuyUpdater extends Updater<Long, StatUserBuy> {
    public StatUserBuyUpdater(Long aLong) {
        super(StatUserBuy.class, aLong);
    }
}