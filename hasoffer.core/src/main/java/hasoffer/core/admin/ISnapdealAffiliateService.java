package hasoffer.core.admin;

import hasoffer.core.persistence.po.admin.OrderStatsAnalysisPO;

import java.util.Date;
import java.util.List;

public interface ISnapdealAffiliateService {

    /**
     * 按时间统计
     *
     * @param startTime
     * @param endTime
     */
    List<OrderStatsAnalysisPO> countOrderList(Date startTime, Date endTime);
}
