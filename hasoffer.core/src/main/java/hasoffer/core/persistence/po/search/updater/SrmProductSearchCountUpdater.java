package hasoffer.core.persistence.po.search.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.search.SrmProductSearchCount;

public class SrmProductSearchCountUpdater extends Updater<Long, SrmProductSearchCount> {
    public SrmProductSearchCountUpdater(Long id) {
        super(SrmProductSearchCount.class, id);
    }
}
