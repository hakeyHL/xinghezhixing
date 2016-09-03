package hasoffer.core.persistence.po.search.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.search.SrmSearchLog;

public class SrmSearchLogUpdater extends Updater<String, SrmSearchLog> {
    public SrmSearchLogUpdater(String id) {
        super(SrmSearchLog.class, id);
    }
}
