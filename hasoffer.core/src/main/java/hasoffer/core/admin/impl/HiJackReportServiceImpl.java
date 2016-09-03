package hasoffer.core.admin.impl;

import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.admin.IHiJackReportService;
import hasoffer.core.persistence.dbm.HibernateDao;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.UrmDeviceRequestLog;
import hasoffer.core.persistence.po.admin.HiJackReportPo;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class HiJackReportServiceImpl implements IHiJackReportService {

    //SELECT webSite, DATE_FORMAT(calStartTime, '%y-%m-%d') AS calStartTime, SUM(pricelistCount) AS pricelistCount, SUM(rediToAffiliateUrlCount) AS rediToAffiliateUrlCount, SUM(captureMultipleSuccess) AS captureMultipleSuccess, SUM(captureSingleSuccess) as captureSingleSuccess, SUM(deeplinkCount) as deeplinkCount, SUM(deeplinkDoubleCount) as deeplinkDoubleCount, SUM(deeplinkNullCount) as deeplinkNullCount FROM report_hijack GROUP BY webSite, DATE_FORMAT(calStartTime, '%y-%m-%d')
    private static final String Q_BY_DATE_WEBSITE =
            "SELECT webSite, DATE_FORMAT(calStartTime, '%Y-%m-%d') AS calStartTime, SUM(cmpSkuCount) as cmpSkuCount, SUM(pricelistCount) AS pricelistCount, SUM(rediToAffiliateUrlCount) AS rediToAffiliateUrlCount, SUM(captureFail) as captureFail,SUM(captureMultipleSuccess) AS captureMultipleSuccess, SUM(captureSingleSuccess) as captureSingleSuccess, SUM(deeplinkCount) as deeplinkCount, SUM(deeplinkDoubleCount) as deeplinkDoubleCount, SUM(deeplinkNullCount) as deeplinkNullCount, SUM(deeplinkExceptionCount) as deeplinkExceptionCount FROM report_hijack  WHERE calStartTime >= ? and calEndTime<?  AND webSite = ?  GROUP BY webSite, DATE_FORMAT(calStartTime, '%Y-%m-%d')";

    private static final String Q_BY_DATE =
            "SELECT webSite, DATE_FORMAT(calStartTime, '%Y-%m-%d') AS calStartTime, SUM(cmpSkuCount) as cmpSkuCount, SUM(pricelistCount) AS pricelistCount, SUM(rediToAffiliateUrlCount) AS rediToAffiliateUrlCount,SUM(captureFail) as captureFail, SUM(captureMultipleSuccess) AS captureMultipleSuccess, SUM(captureSingleSuccess) as captureSingleSuccess, SUM(deeplinkCount) as deeplinkCount, SUM(deeplinkDoubleCount) as deeplinkDoubleCount, SUM(deeplinkNullCount) as deeplinkNullCount, SUM(deeplinkExceptionCount) as deeplinkExceptionCount FROM report_hijack  WHERE calStartTime >= ? and calEndTime<? GROUP BY webSite, DATE_FORMAT(calStartTime, '%Y-%m-%d')";

    @Resource
    IDataBaseManager dbm;

    @Resource
    HibernateDao hdao;

    @Resource
    IMongoDbManager mdm;

    @Override
    public int insert(HiJackReportPo po) {
        return dbm.create(po);
    }


    @Override
    public void update(HiJackReportPo po) {
        HiJackReportPo.HiJackReportPoUpdater updater = new HiJackReportPo.HiJackReportPoUpdater(po.getId());
        HiJackReportPo updaterPo = updater.getPo();
        updaterPo.setPricelistCount(po.getPricelistCount() + updaterPo.getPricelistCount());
        updaterPo.setRediToAffiliateUrlCount(po.getRediToAffiliateUrlCount() + updaterPo.getRediToAffiliateUrlCount());
        updaterPo.setDeeplinkCount(po.getDeeplinkCount() + updaterPo.getDeeplinkCount());
        updaterPo.setDeeplinkNullCount(po.getDeeplinkNullCount() + updaterPo.getDeeplinkNullCount());
        updaterPo.setDeeplinkDoubleCount(po.getDeeplinkDoubleCount() + updaterPo.getDeeplinkDoubleCount());
        updaterPo.setCaptureSingleSuccess(po.getCaptureSingleSuccess() + updaterPo.getCaptureSingleSuccess());
        updaterPo.setCaptureMultipleSuccess(po.getCaptureMultipleSuccess() + updaterPo.getCaptureMultipleSuccess());
        updaterPo.setCaptureFail(po.getCaptureFail() + updaterPo.getCaptureFail());
        updaterPo.setUpdateTime(new Date());
        dbm.update(updater);
    }

    @Override
    public HiJackReportPo selectByDateAndWebSit(String webSite, Date date) {
//        List<HiJackReportPo> resultList = dbm.query(Q_BY_DATE_WEBSITE, Arrays.asList(date, date, webSite));
//        if (ArrayUtils.hasObjs(resultList)) {
//            return resultList.get(0);
//        }
        return null;
    }

    @Override
    public PageableResult<Map<String, Object>> selectPageableResult(String webSite, Date startYmd, Date endYmd, int page, int size) {
        PageableResult<Map<String, Object>> resultMap;
        endYmd = TimeUtils.addDay(endYmd, 1);
        if (webSite == null || "ALL".equals(webSite) || "".equals(webSite)) {
            resultMap = hdao.findPageOfMapBySql(Q_BY_DATE, page, size, startYmd, endYmd);
        } else {
            resultMap = hdao.findPageOfMapBySql(Q_BY_DATE_WEBSITE, page, size, startYmd, endYmd, webSite);
        }

        return resultMap;
    }

    @Override
    public void countHiJack(Date startTime, Date endTime) throws Exception {
        /*try {


            Map<String, HiJackReportPo> jackReportMap = new HashMap<String, HiJackReportPo>();

            //0，查询比价请求次数
            countCmpSkuList(jackReportMap, startTime, endTime);

            //1，查询小球点击次数
            countPriceList(jackReportMap, startTime, endTime);

            //2，查询劫持成功次数
            countRediToAffiUrl(jackReportMap, startTime, endTime);

            //3, 查询劫持失败次数，未收录失败次数，重复失败次数
            countFail(jackReportMap, startTime, endTime);

            //4，统计当天劫持失败商品 抓取结果唯一且抓取成功数量，抓取结果有多个且抓取成功数量，抓取失败数量
            countCapture(jackReportMap, startTime, endTime);

            Collection<HiJackReportPo> reportPoList = jackReportMap.values();

            for (HiJackReportPo po : reportPoList) {
                po.setCalStartTime(startTime);
                po.setCalEndTime(endTime);
                insert(po);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }*/
    }

    private void countCmpSkuList(Map<String, HiJackReportPo> jackReportMap, Date startTime, Date endTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("createTime").gte(startTime).lt(endTime));
        query.addCriteria(Criteria.where("requestUri").is("/cmp/getcmpskus"));

        List<UrmDeviceRequestLog> resultList = mdm.query(UrmDeviceRequestLog.class, query);
        for (UrmDeviceRequestLog log : resultList) {
            String webSite = "NOTFOUND";
            Website curShopApp = log.getCurShopApp();
            if (curShopApp != null) {
                webSite = curShopApp.name();
            }
            HiJackReportPo temp = jackReportMap.get(webSite);
            if (temp == null) {
                temp = new HiJackReportPo();
                temp.setWebSite(webSite);
                jackReportMap.put(webSite, temp);
            }
            temp.setCmpSkuCount(temp.getCmpSkuCount() + 1);
        }
    }

    /**
     * 统计当天劫持失败商品 抓取结果唯一且抓取成功数量，抓取结果有多个且抓取成功数量，抓取失败数量
     */
    private void countCapture(Map<String, HiJackReportPo> jackReportMap, Date startTime, Date endTime) {

    }

    /**
     * 查询小球点击次数
     *
     * @param countPriceMap
     */
    private void countPriceList(Map<String, HiJackReportPo> countPriceMap, Date startTime, Date endTime) {

        Query query = new Query();
        query.addCriteria(Criteria.where("createTime").gte(startTime).lt(endTime));
        final String regex = "action=priceList*";
        query.addCriteria(Criteria.where("query").regex(regex));

        List<UrmDeviceRequestLog> resultList = mdm.query(UrmDeviceRequestLog.class, query);
        for (UrmDeviceRequestLog log : resultList) {
            String webSite = "NOTFOUND";
            Website curShopApp = log.getCurShopApp();
            if (curShopApp != null) {
                webSite = curShopApp.name();
            }
            HiJackReportPo temp = countPriceMap.get(webSite);
            if (temp == null) {
                temp = new HiJackReportPo();
                temp.setWebSite(webSite);
                countPriceMap.put(webSite, temp);
            }
            temp.setPricelistCount(temp.getPricelistCount() + 1);
        }

    }

    /**
     * 查询劫持成功次数
     */
    private void countRediToAffiUrl(Map<String, HiJackReportPo> countPriceMap, Date startTime, Date endTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("createTime").gte(startTime).lt(endTime));
        final String regex = "action=rediToAffiliateUrl*";
        query.addCriteria(Criteria.where("query").regex(regex));

        List<UrmDeviceRequestLog> resultList = mdm.query(UrmDeviceRequestLog.class, query);
        for (UrmDeviceRequestLog log : resultList) {
            String webSite = "NOTFOUND";
            Website curShopApp = log.getCurShopApp();
            if (curShopApp != null) {
                webSite = curShopApp.name();
            }
            HiJackReportPo temp = countPriceMap.get(webSite);
            if (temp == null) {
                temp = new HiJackReportPo();
                temp.setWebSite(webSite);
                countPriceMap.put(webSite, temp);
            }
            temp.setRediToAffiliateUrlCount(temp.getRediToAffiliateUrlCount() + 1);
        }

    }

    /**
     * 查询劫持失败次数，未收录失败次数，重复失败次数
     */
    /*private void countFail(Map<String, HiJackReportPo> countPriceMap, Date startTime, Date endTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("createTime").gte(startTime).lt(endTime));
        query.addCriteria(Criteria.where("errorMsg").in("no index.","different urls.","no url."));

        List<PtmCmpSkuIndexSearchLog> resultList = mdm.query(PtmCmpSkuIndexSearchLog.class, query);
        for (PtmCmpSkuIndexSearchLog log : resultList) {
            String webSite = "NOTFOUND";
            Website curShopApp = log.getWebsite();
            if (curShopApp != null) {
                webSite = curShopApp.name();
            }
            HiJackReportPo temp = countPriceMap.get(webSite);
            if (temp == null) {
                temp = new HiJackReportPo();
                temp.setWebSite(webSite);
                countPriceMap.put(webSite, temp);
            }
            if ("no index.".equals(log.getErrorMsg())) {
                temp.setDeeplinkNullCount(temp.getDeeplinkNullCount() + 1);
            } else if ("different urls.".equals(log.getErrorMsg())) {
                temp.setDeeplinkDoubleCount(temp.getDeeplinkDoubleCount() + 1);
            } else if("no url.".equals(log.getErrorMsg())) {
                temp.setDeeplinkExceptionCount(temp.getDeeplinkExceptionCount() + 1);
            }
            temp.setDeeplinkCount(temp.getDeeplinkCount() + 1);
        }
    }*/
}
