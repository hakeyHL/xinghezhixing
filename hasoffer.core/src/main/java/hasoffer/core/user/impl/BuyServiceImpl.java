package hasoffer.core.user.impl;

import hasoffer.base.model.PageableResult;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.stat.StatUserBuy;
import hasoffer.core.persistence.po.stat.updater.StatUserBuyUpdater;
import hasoffer.core.user.IBuyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created on 2016/4/11.
 */
@Service
public class BuyServiceImpl implements IBuyService {

    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;


    @Override
    public StatUserBuy getStatUerBuyById(long id) {
        return dbm.get(StatUserBuy.class, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatUserBuy(long id, StatUserBuy statUserBuy) {

        StatUserBuyUpdater updater = new StatUserBuyUpdater(id);

        updater.getPo().setLastBuyTime(statUserBuy.getLastBuyTime());

        updater.getPo().setCount(statUserBuy.getCount());

        dbm.update(updater);

    }

    @Override
    public PageableResult<StatUserBuy> listStatUserBuy(int page, int size) {
        return dbm.queryPage("SELECT t FROM StatUserBuy t ORDER BY t.count DESC", page, size);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StatUserBuy createStatUserBuy(StatUserBuy statUserBuy) {
        dbm.create(statUserBuy);
        return statUserBuy;
    }
}
