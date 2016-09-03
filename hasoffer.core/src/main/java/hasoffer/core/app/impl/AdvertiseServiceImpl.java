package hasoffer.core.app.impl;

import hasoffer.core.app.AdvertiseService;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.admin.Adt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hs on 2016年07月26日.
 * Time 13:05
 */
@Service
public class AdvertiseServiceImpl implements AdvertiseService {
    private static final String Q_AD_GET_CATEGORY =
            "SELECT t FROM Adt t " +
                    " where t.isShow=true ORDER BY t.startTime DESC";
    Logger logger = LoggerFactory.getLogger(AdvertiseServiceImpl.class);
    @Resource
    IDataBaseManager dbm;

    @Override
    public List<Adt> getAdByCategory() {
        return dbm.query(Q_AD_GET_CATEGORY);
    }
}
