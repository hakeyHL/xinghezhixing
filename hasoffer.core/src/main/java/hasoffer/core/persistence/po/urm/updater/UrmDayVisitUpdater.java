package hasoffer.core.persistence.po.urm.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.urm.UrmDayVisit;

public class UrmDayVisitUpdater extends Updater<String, UrmDayVisit> {
    public UrmDayVisitUpdater(String id) {
        super(UrmDayVisit.class, id);
    }
}
