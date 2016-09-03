package hasoffer.core.admin;

import hasoffer.base.model.PageableResult;
import hasoffer.core.bo.enums.TopSellStatus;
import hasoffer.core.persistence.po.ptm.PtmTopSelling;

/**
 * Created on 2016/7/6.
 */
public interface ITopSellingService {


    PageableResult<PtmTopSelling> findTopSellingList(TopSellStatus status, int page, int size);

    PtmTopSelling findTopSellingById(long id);

    void updateTopSellingStatus(long topSellingId, TopSellStatus status);

}
