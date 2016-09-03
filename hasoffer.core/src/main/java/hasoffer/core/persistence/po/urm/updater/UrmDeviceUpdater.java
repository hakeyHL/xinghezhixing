package hasoffer.core.persistence.po.urm.updater;

import hasoffer.core.persistence.dbm.osql.Updater;
import hasoffer.core.persistence.po.urm.UrmDevice;

public class UrmDeviceUpdater extends Updater<String, UrmDevice> {
    public UrmDeviceUpdater(String id) {
        super(UrmDevice.class, id);
    }
}
