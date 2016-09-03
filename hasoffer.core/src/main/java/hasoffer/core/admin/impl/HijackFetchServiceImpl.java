package hasoffer.core.admin.impl;

import hasoffer.core.admin.IHijackFetchService;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.mongo.StatHijackFetchCount;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created on 2016/6/7.
 */
@Service
public class HijackFetchServiceImpl implements IHijackFetchService {

    @Resource
    IMongoDbManager mdm;


    @Override
    public StatHijackFetchCount findStatHijackCount(String id) {
        return mdm.queryOne(StatHijackFetchCount.class, id);
    }
}
