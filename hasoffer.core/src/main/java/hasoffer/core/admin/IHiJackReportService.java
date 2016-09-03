package hasoffer.core.admin;

import hasoffer.base.model.PageableResult;
import hasoffer.core.persistence.po.admin.HiJackReportPo;

import java.util.Date;
import java.util.Map;

public interface IHiJackReportService {

    /**
     * 新增一条记录
     *
     * @param po
     * @return
     */
    int insert(HiJackReportPo po);

    /**
     * 做增量更新，将原有的记录中对应的字段加上该对象的值。
     *
     * @param po
     * @return
     */
    void update(HiJackReportPo po);

    /**
     * 根据站点和时间进行查询
     *
     * @param webSite
     * @param date
     * @return
     */
    HiJackReportPo selectByDateAndWebSit(String webSite, Date date);

    PageableResult<Map<String, Object>> selectPageableResult(String webSite, Date startYmd, Date endYmd, int page, int size);

    void countHiJack(Date startTime, Date endTime) throws Exception;
}
