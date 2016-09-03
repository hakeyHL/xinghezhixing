package hasoffer.core.admin.impl;

import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.admin.IOrderStatsAnalysisService;
import hasoffer.core.admin.ISnapdealAffiliateService;
import hasoffer.core.persistence.dbm.HibernateDao;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.admin.OrderStatsAnalysisPO;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderStatsAnalysisServiceImpl implements IOrderStatsAnalysisService {

    //private static final String Q_BASE = "sum(1) as sumCount, SUM(IF(userType='OLD',1,0)) as oldUserCount,SUM(IF(userType='NEW',1,0)) as newUserCount,SUM(IF(userType='NONE',1,0)) as noneUserCount,sum(if(channel='GOOGLEPLAY',1,0)) as googleChannel,sum(if(channel='SHANCHUAN' or channel='LeoMaster' ,1,0)) as shanchuanChannel,sum(if(channel='NINEAPPS',1,0)) as nineAppChannel,sum(if(channel='NONE',1,0)) as noneChannel from report_ordersatas";
    private static final String Q_BASE = "sum(1) AS sumCount, SUM(IF(userType = 'OLD', 1, 0)) AS oldUserCount, SUM(IF(userType = 'NEW', 1, 0)) AS newUserCount, SUM(IF(userType = 'NONE', 1, 0)) AS noneUserCount, sum( IF (channel = 'GOOGLEPLAY', 1, 0)) AS googleChannel, sum( IF ( channel = 'GOOGLEPLAY' AND userType = 'OLD', 1, 0 )) AS googleOldChannel, sum( IF ( channel = 'GOOGLEPLAY' AND userType = 'NEW', 1, 0 )) AS googleNewChannel, sum( IF ( channel = 'GOOGLEPLAY' AND userType = 'NONE', 1, 0 )) AS googleNoneChannel, sum( IF ( channel = 'SHANCHUAN' OR channel = 'LeoMaster', 1, 0 )) AS shanchuanChannel, sum( IF (( channel = 'SHANCHUAN' OR channel = 'LeoMaster' ) AND userType = 'OLD', 1, 0 )) AS shanchuanOldChannel, sum( IF (( channel = 'SHANCHUAN' OR channel = 'LeoMaster' ) AND userType = 'NEW', 1, 0 )) AS shanchuanNewChannel, sum( IF (( channel = 'SHANCHUAN' OR channel = 'LeoMaster' ) AND userType = 'NONE', 1, 0 )) AS shanchuanNoneChannel, sum(IF(channel = 'NINEAPPS', 1, 0)) AS nineAppChannel, sum( IF ( channel = 'NINEAPPS' AND userType = 'OLD', 1, 0 )) AS nineAppOldChannel, sum( IF ( channel = 'NINEAPPS' AND userType = 'NEW', 1, 0 )) AS nineAppNewChannel, sum( IF ( channel = 'NINEAPPS' AND userType = 'NONE', 1, 0 )) AS nineAppNoneChannel, sum(IF(channel = 'NONE', 1, 0)) AS noneChannel from report_ordersatas";

    private static final String D_BASE = "delete from report_ordersatas where webSite=? and orderTime>=DATE_FORMAT(?,'%Y-%m-%d %H:%i:%S') and orderTime<DATE_FORMAT(?,'%Y-%m-%d %H:%i:%S')";
    @Resource
    IDataBaseManager dbm;
    @Resource
    HibernateDao hdao;
    @Resource
    private FlipkartAffiliateServiceImpl flipkartAffiliateService;
    @Resource
    private ISnapdealAffiliateService snapdealAffiliateService;

    @Override
    public int insert(OrderStatsAnalysisPO po) {
        return dbm.create(po);
    }

    @Override
    public int delete(String webSite, Date startTime, Date endTime) {
        return hdao.deleteBySql(D_BASE, webSite, startTime, endTime);
    }

    @Override
    public void updateOrder(String webSite, Date startTime, Date endTime) {
        if (webSite == null || startTime == null) {
            return;
        }
        try {
            String formatStartTime = DateFormatUtils.format(startTime, "yyyy-MM-dd 00:00:00.000");
            startTime = DateUtils.parseDate(formatStartTime, "yyyy-MM-dd HH:mm:ss.SSS");
            String formatEndTime = DateFormatUtils.format(startTime, "yyyy-MM-dd 00:00:00.000");
            endTime = DateUtils.parseDate(formatEndTime, "yyyy-MM-dd HH:mm:ss.SSS");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date delEndTime = TimeUtils.addDay(endTime, 1);
        if (Website.FLIPKART.toString().equals(webSite)) {
            List<OrderStatsAnalysisPO> flipkartPOList = flipkartAffiliateService.countOrderList(startTime, endTime);
            if (flipkartPOList != null && flipkartPOList.size() > 0) {
                delete(Website.FLIPKART.toString(), startTime, delEndTime);
                for (OrderStatsAnalysisPO po : flipkartPOList) {
                    insert(po);
                }
            }
        }

        if (Website.SNAPDEAL.toString().equals(webSite)) {
            List<OrderStatsAnalysisPO> snapDealPoList = snapdealAffiliateService.countOrderList(startTime, endTime);
            if (snapDealPoList != null && snapDealPoList.size() > 0) {
                delete(Website.SNAPDEAL.toString(), startTime, delEndTime);
                for (OrderStatsAnalysisPO po : snapDealPoList) {
                    insert(po);
                }
            }
        }
    }

    @Override
    public PageableResult<Map<String, Object>> selectPageableResult(String webSite, String channel, String orderStatus, Date startYmd, Date endYmd, int page, int size) {
        List<Object> param = new ArrayList<Object>();
        endYmd = TimeUtils.addDay(endYmd, 1);
        StringBuilder groupSql = new StringBuilder(" group by DATE_FORMAT(orderTime,'%Y-%m-%d') ");
        StringBuilder whereSql = new StringBuilder(" where orderTime>=? and orderTime<? ");
        param.add(startYmd);
        param.add(endYmd);
        StringBuilder sql = new StringBuilder("select DATE_FORMAT(orderTime,'%Y-%m-%d') as dateTime, ");
        if (webSite != null && !"ALL".equals(webSite)) {
            whereSql.append(" and webSite=? ");
            param.add(webSite);
        }
        if (channel != null && !"".equals(channel) && !"ALL".equals(channel)) {
            whereSql.append(" and channel=? ");
            param.add(channel);
        }
        if (orderStatus != null && !"".equals(orderStatus) && "ALL".equals(orderStatus)) {
            whereSql.append(" and orderStatus=? ");
            param.add(orderStatus);
        }
        String execSql = sql.append(Q_BASE).append(whereSql).append(groupSql).append(" ORDER BY orderTime desc ").toString();
        System.out.println(execSql + ":" + param.toArray());
        return hdao.findPageOfMapBySql(execSql, page, size, param.toArray());
    }
}
