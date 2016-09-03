package hasoffer.core.admin.impl;

import hasoffer.base.model.PageableResult;
import hasoffer.core.admin.ITopSellingService;
import hasoffer.core.bo.enums.TopSellStatus;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmTopSelling;
import hasoffer.core.persistence.po.ptm.updater.PtmTopSellingUpdater;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created on 2016/7/6.
 */
@Service
public class TopSellingServiceImpl implements ITopSellingService {

    private static final String Q_TOPSELLINGLIST_BYSTATUS = "SELECT t FROM PtmTopSelling t WHERE  t.status = ?0 order by t.lUpdateTime desc , t.count desc ";

    @Resource
    IDataBaseManager dbm;

    @Override
    public PageableResult<PtmTopSelling> findTopSellingList(TopSellStatus status, int page, int size) {
        PageableResult<PtmTopSelling> pageableResult = dbm.queryPage(Q_TOPSELLINGLIST_BYSTATUS, page, size, Arrays.asList(status));
        return pageableResult;
    }

    @Override
    public PtmTopSelling findTopSellingById(long id) {
        return dbm.get(PtmTopSelling.class, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTopSellingStatus(long topSellingId, TopSellStatus status) {

        PtmTopSellingUpdater updater = new PtmTopSellingUpdater(topSellingId);

        updater.getPo().setStatus(status);

        dbm.update(updater);
    }
}
