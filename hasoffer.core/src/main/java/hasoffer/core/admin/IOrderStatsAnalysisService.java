package hasoffer.core.admin;

import hasoffer.base.model.PageableResult;
import hasoffer.core.persistence.po.admin.OrderStatsAnalysisPO;

import java.util.Date;
import java.util.Map;

public interface IOrderStatsAnalysisService {

    int insert(OrderStatsAnalysisPO po);

    int delete(String webSite, Date startTime, Date endTime);

    void updateOrder(String webSite, Date startTime, Date endTime);

    PageableResult<Map<String, Object>> selectPageableResult(String webSite, String channel, String orderStatus, Date startYmd, Date endYmd, int page, int size);
}
