package hasoffer.core.app;

import hasoffer.core.persistence.po.admin.Adt;

import java.util.List;

/**
 * Created by hs on 2016年07月26日.
 * Time 13:04
 */
public interface AdvertiseService {

    public List<Adt> getAdByCategory();

}
