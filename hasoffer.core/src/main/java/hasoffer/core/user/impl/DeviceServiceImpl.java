package hasoffer.core.user.impl;

import hasoffer.base.enums.AppType;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.DeviceUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.bo.user.DayVisitBo;
import hasoffer.core.bo.user.DeviceEventBo;
import hasoffer.core.bo.user.DeviceInfoBo;
import hasoffer.core.bo.user.DeviceRequestBo;
import hasoffer.core.cache.DeviceCacheManager;
import hasoffer.core.persistence.dbm.HibernateDao;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.*;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.urm.UrmDayVisit;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.persistence.po.urm.UrmDeviceConfig;
import hasoffer.core.persistence.po.urm.updater.UrmDayVisitUpdater;
import hasoffer.core.persistence.po.urm.updater.UrmDeviceUpdater;
import hasoffer.core.user.IDeviceService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date : 2016/1/15
 * Function :
 */
@Service
public class DeviceServiceImpl implements IDeviceService {

    private static final String Q_DEVICE = " SELECT t FROM UrmDevice t ORDER BY t.createTime DESC ";
    private static final String Q_DEVICE_ASC = " SELECT t FROM UrmDevice t ORDER BY t.createTime ASC ";
    private static final String Q_DEVICE_BY_TIME =
            " SELECT t FROM UrmDevice t " +
                    "  WHERE t.createTime > ?0 " +
                    "    AND t.createTime < ?1 " +
                    "  ORDER BY t.createTime ASC ";
    private static final String C_DEVICE =
            "SELECT COUNT(t.id) FROM UrmDevice t " +
                    "WHERE t.createTime >= ?0    " +
                    "  AND t.createTime < ?1     " +
                    "  AND t.osVersion >= '4.3'  " +
                    "  AND t.statAble = 1        ";
    private static final String Q_DEVICEBRAND =
            "SELECT t FROM UrmBrand t";
    private static final String Q_DEVICEVERSIONS =
            "SELECT t FROM UrmVersions t";
    private static final String Q_DEVICE_STATRESULT =
            "SELECT t FROM StatDevice t WHERE t.deviceId = ?0 ORDER BY t.ymd ASC ";
    private static final String Q_DEVICE_STATRESULT_BY_YMD =
            "SELECT t FROM StatDevice t WHERE t.ymd = ?0 ";
    private static final String Q_VISIT =
            "SELECT t FROM UrmDayVisit t ORDER BY t.id DESC";
    private static final String D_DEVICE_LOG = "delete FROM UrmDevice t where t.id in (:ids) ";
    private static final String Q_DEVICE_BY_IDS = "SELECT t FROM UrmDevice t where t.id in (:ids)";
    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;
    @Resource
    HibernateDao hdao;
    @Resource
    DeviceCacheManager deviceCM;
    private Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Override
    public PageableResult<StatDayAlive> findAliveStats(String startYmd, String endYmd, String marketChannel, String brand, String osVersion, int page, int size, String order) {

        Query query = Query.query(
                Criteria.where("ymd").gte(startYmd).lte(endYmd)
                        .andOperator(Criteria.where("marketChannel").is(marketChannel)
                                .andOperator(Criteria.where("brand").is(brand)
                                        .andOperator(Criteria.where("osVersion").is(osVersion))))
        );

        query.with(new Sort(Sort.Direction.DESC, "ymd"));

        return mdm.queryPage(StatDayAlive.class, query, page, size);
        /*
        final String sql
                = "SELECT t FROM StatDayAlive t " +
                "WHERE t.date>=?0 and t.date<=?1 " +
                "and t.marketChannel=?2 " +
                "and t.brand = ?3 " +
                "and t.osVersion = ?4 " +
                "order by t.date desc";

        return dbm.queryPage(sql, page, size, Arrays.asList(startYmd, endYmd, marketChannel, brand, osVersion));*/
    }

    @Override
    public List<StatDevice> findCmpskuStat(String marketChannel, int days, String startYmd, String endYmd) {
        //        {"marketChannel":"SHANCHUAN", "deviceYmd":{"$lt":"20160513"}, "cmpPrice":{"$gt":0},"ymd":{"$gt":"20160501"},"ymd":{"$lt":"20160510"}}
        Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
        try {
            Date date = DateUtils.parseDate(startYmd, "yyyyMMdd");
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, -days);//取当前日期的前一天.
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String beginYmd = DateFormatUtils.format(cal, "yyyyMMdd");
        Query query;
        if (marketChannel == null || "ALL".equals(marketChannel) || "".equals(marketChannel)) {
            query = Query.query(
                    Criteria.where("deviceYmd").lte(startYmd).gte(beginYmd)
                            .andOperator(Criteria.where("ymd").gte(startYmd).lte(endYmd).andOperator(Criteria.where("cmpPrice").gt(0)))
            );
        } else {
            query = Query.query(
                    Criteria.where("deviceYmd").lte(startYmd).gte(beginYmd)
                            .andOperator(Criteria.where("ymd").gte(startYmd).lte(endYmd).andOperator(Criteria.where("cmpPrice").gt(0).andOperator(Criteria.where("marketChannel").is(marketChannel))))
            );
        }
//        Criteria matchCriteria=Criteria.where("deviceYmd").lte(startYmd).gte(beginYmd).and("ymd").gte(startYmd).lte(endYmd).and("cmpPrice").gt(0).and("marketChannel").is(marketChannel);
//        MatchOperation match = Aggregation.match(matchCriteria);
//        GroupOperation groupOperation = Aggregation.group("deviceId", "deviceYmd").count().as("count");
//        GroupOperation groupOperation1 = Aggregation.group("deviceId").count().as("count");
//        TypedAggregation<StatDevice> aggregation=Aggregation.newAggregation(StatDevice.class, match, groupOperation, groupOperation1);
//        AggregationResults<StatDevice> aggregate = mdm.aggregate(StatDevice.class, aggregation);
        return mdm.query(StatDevice.class, query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFirstBindAssistYmd(String deviceId) {
        List<StatDevice> stats = listDeviceStats(deviceId);

        String ymd = "";

        for (StatDevice sd : stats) {
            if (sd.getBindAssist() > 0) {
                ymd = sd.getYmd();
                break;
            }
        }

        if (StringUtils.isEmpty(ymd)) {
            return;
        }

        // 更新 device
        UrmDeviceUpdater deviceUpdater = new UrmDeviceUpdater(deviceId);
        deviceUpdater.getPo().setFirstBindAssistYmd(ymd);
        dbm.update(deviceUpdater);

        for (StatDevice sd : stats) {
            sd.setFirstBindAssistYmd(ymd);
            mdm.save(sd);
//            StatDeviceUpdater statDeviceUpdater = new StatDeviceUpdater(sd.getId());
//            statDeviceUpdater.getPo().setFirstBindAssistYmd(ymd);
//            dbm.update(statDeviceUpdater);
        }
    }

    @Override
    public PageableResult<StatDevice> listPagedStatDevice(String ymd, int page, int size) {
        Query query = Query.query(Criteria.where("ymd").is(ymd));
        return mdm.queryPage(StatDevice.class, query, page, size);
//        return dbm.queryPage(Q_DEVICE_STATRESULT_BY_YMD, page, size, Arrays.asList(ymd));
    }

    @Override
    public List<StatDevice> listStatDevice(String ymd, int page, int size) {
        Query query = Query.query(Criteria.where("ymd").is(ymd));
        return mdm.query(StatDevice.class, query, page, size);
//        return dbm.query(Q_DEVICE_STATRESULT_BY_YMD, page, size, Arrays.asList(ymd));
    }

    @Override
    public List<StatDevice> listDeviceStats(String deviceId) {
        Query query = Query.query(Criteria.where("deviceId").is(deviceId));
        return mdm.query(StatDevice.class, query);
//        return dbm.query(Q_DEVICE_STATRESULT, Arrays.asList(deviceId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveDeviceStatResult(StatDevice sda) {
        mdm.save(sda);
//        StatDevice sda0 = mdm.queryOne(StatDevice.class, sda.getId());//dbm.get(StatDevice.class, sda.getId());


//        if (sda0 == null) {
//            mdm.save(sda);
//        } else {
//            StatDeviceUpdater updater = new StatDeviceUpdater(sda.getId());
//            updater.getPo().setWakeUp(sda.getWakeUp());
//            updater.getPo().setBindAssist(sda.getBindAssist());
//            updater.getPo().setCmpPrice(sda.getCmpPrice());
//            updater.getPo().setShowBall(sda.getShowBall());
//            updater.getPo().setClickBall(sda.getClickBall());
//            updater.getPo().setShop(sda.getShop());
//
//            dbm.update(updater);
//        }
    }

    @Override
    public Map<MarketChannel, StatDevice> statByLog(String deviceId, String ymd) {
        UrmDevice device = findDevice(deviceId);
        return statByLog(device, ymd);
    }

    /**
     * 不持久化
     */
    @Override
    public Map<MarketChannel, StatDevice> statByLog(UrmDevice device, String ymd) {

        if (device == null) {
            return null;
        }

        String deviceId = device.getId();

        Map<MarketChannel, StatDevice> mcDMap = new HashMap<MarketChannel, StatDevice>();

        Date sDate = TimeUtils.stringToDate(ymd, "yyyyMMdd");
        Date eDate = TimeUtils.addDay(sDate, 1);

        int page = 1;
        final int PAGE_SIZE = 500;

        PageableResult<UrmDeviceRequestLog> pagedRequestLogs = findDeviceLogs(deviceId, sDate, eDate, page, PAGE_SIZE);
        List<UrmDeviceRequestLog> requestLogs = pagedRequestLogs.getData();

        final long TOTAL_PAGE = pagedRequestLogs.getTotalPage();

        while (page <= TOTAL_PAGE) {
            if (page > 1) {
                pagedRequestLogs = findDeviceLogs(deviceId, sDate, eDate, page, PAGE_SIZE);
                requestLogs = pagedRequestLogs.getData();
            }

            if (ArrayUtils.hasObjs(requestLogs)) {
                for (UrmDeviceRequestLog log : requestLogs) {
                    // 取渠道，如果请求log中渠道为空，则使用device记录的渠道
                    MarketChannel mc = log.getMarketChannel();
                    if (mc == null) {
                        mc = device.getMarketChannel();
                    }

                    if (mc == null) {
                        mc = MarketChannel.OFFICIAL;
                    }

                    StatDevice sda = mcDMap.get(mc);
                    if (sda == null) {
                        sda = new StatDevice(
                                mc,
                                ymd,
                                TimeUtils.parse(device.getCreateTime(), TimeUtils.PATTERN_YMD),
                                device.getId(),
                                device.getDeviceName(),
                                device.getOsVersion(),
                                device.getCampaign(),
                                device.getAdSet(),
                                device.getBrand(),
                                device.getShopApp()
                        );
                        mcDMap.put(mc, sda);
                    }

                    String requestUri = log.getRequestUri();
                    String queryStr = log.getQuery();

                    if (requestUri.equals("/cmp/getcmpskus")) {
                        sda.setCmpPrice(sda.getCmpPrice() + 1);
                    } else if (requestUri.equals("/app/dot")) {

                        if (queryStr.contains("wakeUp")) {
                            sda.setWakeUp(sda.getWakeUp() + 1);
                        } else if (queryStr.contains("autoModifyAccessSuccess")) {
                            sda.setBindAssist(sda.getBindAssist() + 1);
                        } else if (queryStr.contains("shop") && !queryStr.contains("autoModifyAccessSuccess")) {
                            sda.setShop(sda.getShop() + 1);
                        } else if (queryStr.contains("showBall") || queryStr.contains("showIcon")) {
                            sda.setShowBall(sda.getShowBall() + 1);
                        } else if (queryStr.contains("priceList")) {
                            sda.setClickBall(sda.getClickBall() + 1);
                        } else {
                            sda.setOtherDot(sda.getOtherDot() + 1);
                        }

                    } else {
                        sda.setOtherDot(sda.getOtherDot() + 1);
                    }

                }
            }

            page++;
        }

        return mcDMap;
    }

    @Override
    public PageableResult<UrmDeviceRequestLog> findDeviceLogs(String deviceId, Date start, Date end, int page, int size) {

        Criteria c = Criteria.where("deviceId").is(deviceId);

        Query query = new Query(c.andOperator(Criteria.where("createTime").gte(start).lt(end)));

        query.with(new Sort(Sort.Direction.DESC, "createTime"));

        return mdm.queryPage(UrmDeviceRequestLog.class, query, page, size);
    }

    @Override
    public PageableResult<UrmDeviceRequestLog> findDeviceLogsByRequestUri(String requestUri, Date start, Date end, int page, int size) {
        Query query = null;

        Criteria c = Criteria.where("createTime").gte(start).lt(end);

        if (StringUtils.isEmpty(requestUri)) {
            query = new Query(c);
        } else {
            query = new Query(c.andOperator(Criteria.where("requestUri").is(requestUri)));
        }

        query.with(new Sort(Sort.Direction.DESC, "createTime"));

        return mdm.queryPage(UrmDeviceRequestLog.class, query, page, size);
    }

    @Override
    public PageableResult<UrmDeviceRequestLog> findDeviceLogsByRequestUri(String requestUri, int page, int size) {

        Query query = null;
        if (StringUtils.isEmpty(requestUri)) {
            query = new Query();
        } else {
            query = new Query(Criteria.where("requestUri").is(requestUri));
        }

        query.with(new Sort(Sort.Direction.DESC, "createTime"));

        return mdm.queryPage(UrmDeviceRequestLog.class, query, page, size);
    }

    @Override
    public PageableResult<UrmDeviceRequestLog> findDeviceLogsByUriAndQuery(String requestUri, String queryStr, Date start, Date end, int page, int size) {
        Query query = new Query(
                Criteria.where("requestUri").is(requestUri)
                        .andOperator(Criteria.where("createTime").gte(start).lt(end)
                                .andOperator(Criteria.where("query").regex(queryStr)))
        );

        query.with(new Sort(Sort.Direction.DESC, "createTime"));

        return mdm.queryPage(UrmDeviceRequestLog.class, query, page, size);
    }

    @Override
    public PageableResult<UrmDeviceRequestLog> findDeviceLogsByUriAndQuery(String requestUri, String query, boolean eq, Date start, Date end, int page, int size) {

        if (eq) {
            return findDeviceLogsByUriAndQuery(requestUri, query, start, end, page, size);
        } else {
            query = ".*" + query + ".*"; // 模糊查询
            return findDeviceLogsByUriAndQuery(requestUri, query, start, end, page, size);
        }

    }

    @Override
    public UrmDevice findDevice(String deviceId) {
        return dbm.get(UrmDevice.class, deviceId);
    }

    @Override
    public List<UrmDevice> findDeviceByIdList(List<String> deviceList) {
//        hdao.findBySql(Q_DEVICE_BY_IDS);
        if (deviceList == null || deviceList.size() == 0) {
            return new ArrayList<UrmDevice>();
        }
        return dbm.queryByIds(Q_DEVICE_BY_IDS, deviceList.toArray(new String[deviceList.size()]));
    }

    @Override
    public void deviceRequestLogsAnalysis(Date buyLogsMaxCreateTime) {

        Pattern pattern1 = Pattern.compile(".*(action=shop)$");
        Pattern pattern2 = Pattern.compile(".*(action=shop)&.*");
//        Pattern pattern3 = Pattern.compile(".*(action=rediToAffiliateUrl)$");
//        Pattern pattern4 = Pattern.compile(".*(action=rediToAffiliateUrl)&.*");

        List<String> websiteList = new ArrayList<String>();

        for (Website website : Website.values()) {
            websiteList.add(website.toString());
        }

        while (true) {
            Date endDate = TimeUtils.addDay(buyLogsMaxCreateTime, 1);
            PageableResult<UrmDeviceRequestLog> logs = findDeviceLogsByRequestUri("/app/dot", buyLogsMaxCreateTime, endDate, 1, Integer.MAX_VALUE);
            List<UrmDeviceRequestLog> urmDeviceRequestLogList = logs.getData();

            if (ArrayUtils.isNullOrEmpty(urmDeviceRequestLogList)) {
                continue;
            }

            for (UrmDeviceRequestLog urmDeviceRequestLog : urmDeviceRequestLogList) {
                String query = urmDeviceRequestLog.getQuery();
                if (StringUtils.isEmpty(query)) {
                    continue;
                } else {

                    Matcher matcher1 = pattern1.matcher(query);
                    Matcher matcher2 = pattern2.matcher(query);
//                    Matcher matcher3 = pattern3.matcher(query);
//                    Matcher matcher4 = pattern4.matcher(query);
                    boolean b1 = matcher1.matches();
                    boolean b2 = matcher2.matches();
//                    boolean b3 = matcher3.matches();
//                    boolean b4 = matcher4.matches();


                    //如果满足b1或者b2进行buylog转化，如果满足b3或者b4进行hijackLog转化
                    if (b1 || b2) {
                        parseBuyLog(websiteList, urmDeviceRequestLog, query);
                    }

//                    if (b3 || b4) {
//                        parseHijackLog(urmDeviceRequestLog);
//                    }

                }
            }

            if (endDate.getTime() > TimeUtils.nowDate().getTime()) {
                break;
            } else {
                buyLogsMaxCreateTime = TimeUtils.addDay(buyLogsMaxCreateTime, 1);
            }
        }
    }

    /**
     * 直接将requestlog中curApp拿出来存储进行转化
     * 该功能切换至分析日志
     * @param urmDeviceRequestLog
     */
//    private void parseHijackLog(UrmDeviceRequestLog urmDeviceRequestLog) {
//
//        //被劫持网站的名称
//        Website website = urmDeviceRequestLog.getCurShopApp();
//
//        HijackLog hijackLog = new HijackLog();
//        hijackLog.setId(urmDeviceRequestLog.getId());
//        hijackLog.setWebsite(website);
//        hijackLog.setCreateTime(urmDeviceRequestLog.getCreateTime());
//
//        mdm.save(hijackLog);
//    }

    private void parseBuyLog(List<String> websiteList, UrmDeviceRequestLog urmDeviceRequestLog, String query) {
        String[] params = query.split("&");

        if (params.length != 2) {
            return;
        }

        String value = "";
        for (int i = 0; i < 2; i++) {
            if (params[i].contains("value=")) {
                value = params[i].replace("value=", "");
            }
        }

        if (StringUtils.isEmpty(value)) {
            return;
        }

        // value 两种情况： proId_website | skuId
        long proId = 0L;
        Website website = null;

        int _index = value.indexOf("_");
        if (_index > 0) {
            String proIdStr = value.substring(0, _index);
            if (NumberUtils.isDigits(proIdStr)) {
                proId = Long.valueOf(proIdStr);
            }
            String websiteString = value.substring(_index + 1).toUpperCase();
            if (!websiteList.contains(websiteString)) {
                return;
            }
            website = Website.valueOf(websiteString);
        } else {
            String skuId = value;
            PtmCmpSku cmpSku = dbm.get(PtmCmpSku.class, Long.valueOf(skuId));
            if (cmpSku != null) {
                proId = cmpSku.getProductId();
                website = cmpSku.getWebsite();
            }
        }

        if (proId <= 0) {
            return;
        }

        PtmProduct product = dbm.get(PtmProduct.class, proId);
        if (product != null) {
            UrmDeviceBuyLog urmDeviceBuyLog = new UrmDeviceBuyLog();
            urmDeviceBuyLog.setAppType(urmDeviceRequestLog.getAppType());
            urmDeviceBuyLog.setMarketChannel(urmDeviceRequestLog.getMarketChannel());
            urmDeviceBuyLog.setCreateTime(urmDeviceRequestLog.getCreateTime());
            urmDeviceBuyLog.setDeviceId(urmDeviceRequestLog.getDeviceId());
            urmDeviceBuyLog.setFromWebsite(urmDeviceRequestLog.getCurShopApp());
            urmDeviceBuyLog.setToWebsite(website);
            urmDeviceBuyLog.setId(urmDeviceRequestLog.getId());
            urmDeviceBuyLog.setPtmProductId(proId);
            urmDeviceBuyLog.setTitle(product.getTitle());
            urmDeviceBuyLog.setShopApp(urmDeviceRequestLog.getShopApp());
            mdm.save(urmDeviceBuyLog);
        }
    }

    @Override
    public PageableResult<UrmDeviceBuyLog> findUrmDeviceBuyLog(String fromWebsite, String toWebsite, Date start, Date end, int page, int size, String orderFieldName, int order) {
        Query query = new Query();

        if (!StringUtils.isEmpty(fromWebsite)) {
            Criteria fromWebsiteCriteria = Criteria.where("fromWebsite").is(fromWebsite);
            query.addCriteria(fromWebsiteCriteria);
        }

        if (!StringUtils.isEmpty(toWebsite)) {
            Criteria toWebsiteCriteria = Criteria.where("toWebsite").is(toWebsite);
            query.addCriteria(toWebsiteCriteria);
        }

        if (start != null && end != null) {
            Criteria timeCriteria = Criteria.where("createTime").gte(start).lt(end);
            query.addCriteria(timeCriteria);
        }

        if (!StringUtils.isEmpty(orderFieldName)) {
            if (order == 0) {
                query.with(new Sort(Sort.Direction.DESC, orderFieldName));
            } else {
                query.with(new Sort(Sort.Direction.ASC, orderFieldName));
            }
        }

        PageableResult<UrmDeviceBuyLog> urmDeviceBuyLogPageableResult = mdm.queryPage(UrmDeviceBuyLog.class, query, page, size);

        return urmDeviceBuyLogPageableResult;
    }

    @Override
    public List<UrmDayVisit> listDayVisits() {
        return dbm.query(Q_VISIT);
    }

    @Override
    @Transactional
    public void updateDeviceShopApp(String id, String shopApp) {
        UrmDeviceUpdater urmDeviceUpdater = new UrmDeviceUpdater(id);
        urmDeviceUpdater.getPo().setShopApp(shopApp);
        dbm.update(urmDeviceUpdater);
    }

    @Override
    public boolean restatIfInHour() {
        String ymd = TimeUtils.parse(TimeUtils.today(), "yyyyMMdd");

        UrmDayVisit dayVisit = dbm.get(UrmDayVisit.class, ymd);

        if (dayVisit != null) {
            if (TimeUtils.now() - dayVisit.getUpdateTime().getTime() < TimeUtils.MILLISECONDS_OF_1_HOUR / 2) {
                // 1小时之内更新过，直接返回
                return true;
            }
        }

        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(DayVisitBo dvb) {

        UrmDayVisit udv = dbm.get(UrmDayVisit.class, dvb.getYmd());

        if (udv == null) {
            udv = new UrmDayVisit(dvb.getYmd(), dvb.getAliveDevice(), dvb.getAliveDeviceWithShop(),
                    dvb.getNewDevice(), dvb.getNewDeviceWithShop(), dvb.getVisitDevice(),
                    dvb.getVisitDeviceNew());
            dbm.create(udv);
        } else {
            UrmDayVisitUpdater urmDayVisitUpdater = new UrmDayVisitUpdater(udv.getId());

            urmDayVisitUpdater.getPo().setAliveDevice(dvb.getAliveDevice());
            urmDayVisitUpdater.getPo().setAliveDeviceWithShop(dvb.getAliveDeviceWithShop());
            urmDayVisitUpdater.getPo().setVisitDevice(dvb.getVisitDevice());
            urmDayVisitUpdater.getPo().setVisitDeviceNew(dvb.getVisitDeviceNew());
            urmDayVisitUpdater.getPo().setNewDeviceWithShop(dvb.getNewDeviceWithShop());
            urmDayVisitUpdater.getPo().setNewDevice(dvb.getNewDevice());

            urmDayVisitUpdater.getPo().setUpdateTime(TimeUtils.nowDate());
            dbm.update(urmDayVisitUpdater);
        }
    }

    @Override
    public PageableResult<UrmDeviceRequestLog> findRequestLogs(Date startDate, Date endDate, int page, int size) {
        Query query = new Query(Criteria.where("createTime").gte(startDate).lt(endDate));
        return mdm.queryPage(UrmDeviceRequestLog.class, query, page, size);
    }

    @Override
    public DayVisitBo statDayVisit(String ymd) {
        DayVisitBo dvb = new DayVisitBo(ymd);
        Date startDate = TimeUtils.toDate(TimeUtils.getDayStart(ymd, "yyyyMMdd"));
        Date endDate = TimeUtils.toDate(TimeUtils.getDayStart(ymd, "yyyyMMdd") + TimeUtils.MILLISECONDS_OF_1_DAY);

        logger.debug(String.format("stat day visit .day[%s]", ymd));

        int page = 1, size = 500;

        PageableResult<UrmDeviceRequestLog> pagedDeviceRequestLogs = findRequestLogs(startDate, endDate, page, size);

        List<UrmDeviceRequestLog> deviceRequestLogs = pagedDeviceRequestLogs.getData();
        final long totalPage = pagedDeviceRequestLogs.getTotalPage();

        Set<String> aliveDeviceIdSet = new HashSet<String>();
        Set<String> compareDeviceIdSet = new HashSet<String>();
        Set<String> aliveDeviceWithShopDeviceIdSet = new HashSet<String>();
        Map<String, UrmDevice> deviceMap = new HashMap<String, UrmDevice>();
        int visitNew = 0;

        while (page <= totalPage) {
            logger.debug(String.format("stat day visit[%d/%d]", page, totalPage));
            if (page > 1) {
                pagedDeviceRequestLogs = findRequestLogs(startDate, endDate, page, size);
                deviceRequestLogs = pagedDeviceRequestLogs.getData();
            }

            if (ArrayUtils.hasObjs(deviceRequestLogs)) {
                for (UrmDeviceRequestLog log : deviceRequestLogs) {

                    String deviceId = log.getDeviceId();
                    UrmDevice device = deviceMap.get(deviceId);
                    if (device == null) {
                        device = dbm.get(UrmDevice.class, deviceId);
                        deviceMap.put(deviceId, device);
                    }

                    if (device == null || !device.isStatAble()) {
                        continue;
                    }

                    if (compareDeviceIdSet.contains(deviceId)) {
                        continue;
                    }

                    if ("/cmp/getcmpskus".equals(log.getRequestUri())) {
                        compareDeviceIdSet.add(deviceId);
                        if (TimeUtils.parse(device.getCreateTime(), "yyyyMMdd").equals(ymd)) {
                            // 是当天的新设备
                            visitNew++;
                        }
                        aliveDeviceWithShopDeviceIdSet.add(deviceId);
                    } else {
                        if (!aliveDeviceIdSet.contains(deviceId)) {
                            if (!StringUtils.isEmpty(device.getShopApp())) {
                                aliveDeviceWithShopDeviceIdSet.add(deviceId);
                            }
                        }
                    }

                    aliveDeviceIdSet.add(deviceId);
                }
            }

            page++;
        }

        dvb.setAliveDevice(aliveDeviceIdSet.size());
        dvb.setAliveDeviceWithShop(aliveDeviceWithShopDeviceIdSet.size());
        dvb.setVisitDevice(compareDeviceIdSet.size());
        dvb.setVisitDeviceNew(visitNew);

        // 统计新增设备
        dvb.setNewDevice((Long) dbm.querySingle(C_DEVICE, Arrays.asList(startDate, endDate)));

        // 统计装有电商app的新增设备数量
        String sqlCountDeviceWithShop = C_DEVICE + " AND t.shopApp IS NOT NULL AND t.shopApp <> '' ";
        dvb.setNewDeviceWithShop((Long) dbm.querySingle(sqlCountDeviceWithShop, Arrays.asList(startDate, endDate)));

        return dvb;
    }

    @Override
    public PageableResult<UrmDeviceRequestLog> findDeviceLogs(String deviceId, int page, int size) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        query.with(new Sort(Sort.Direction.DESC, "createTime"));

        return mdm.queryPage(UrmDeviceRequestLog.class, query, page, size);
    }

    @Override
    public PageableResult<UrmDevice> findPagedDevicesAsc(int page, int size) {
        return dbm.queryPage(Q_DEVICE_ASC, page, size);
    }

    @Override
    public PageableResult<UrmDevice> findPagedDevices(Date sDate, Date eDate, int page, int size) {
        return dbm.queryPage(Q_DEVICE_BY_TIME, page, size, Arrays.asList(sDate, eDate));
    }

    @Override
    public PageableResult<UrmDevice> findPagedDevices(int page, int size) {

        return dbm.queryPage(Q_DEVICE, page, size);
    }

    @Override
    public PageableResult<UrmDevice> findPagedDevices(String deviceName, AppType appType, MarketChannel marketChannel, Date startDate, Date endDate, int page, int size, String orderByFieldName, int orderType) {

        StringBuilder query = new StringBuilder(" SELECT t FROM UrmDevice t where 1 = 1 ");
        List params = new ArrayList();
        int index = 0;

        if (!StringUtils.isEmpty(deviceName)) {
            query.append(" AND t.deviceName like ?" + index + " ");
            params.add("%" + deviceName + "%");
            index++;
        }

        if (appType != null) {
            query.append(" AND t.appType = ?" + index + " ");
            params.add(appType);
            index++;
        }

        if (marketChannel != null) {
            query.append(" AND t.marketChannel = ?" + index + " ");
            params.add(marketChannel);
            index++;
        }

        query.append(" AND t.createTime >= ?" + index + " ");
        params.add(startDate);
        index++;

        query.append(" AND t.createTime <= ?" + index + " ");
        params.add(endDate);
        index++;

        if (!StringUtils.isEmpty(orderByFieldName)) {
            query.append(" order by t.").append(orderByFieldName);
            if (orderType == 1) {
                query.append(" asc ");
            } else {
                query.append(" desc");
            }
        }

        return dbm.queryPage(query.toString(), page, size, params);
    }

    @Override
    public UrmDeviceConfig getDeviceConfig(String deviceId) {
        return dbm.get(UrmDeviceConfig.class, deviceId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveDeviceRequest(List<DeviceRequestBo> requestBoList) {
        Map<String, UrmDevice> insertMap = new HashMap<String, UrmDevice>();
        Set<String> delSet = new HashSet<String>();
        for (DeviceRequestBo requestBo : requestBoList) {
            if (requestBo == null) {
                return;
            }
            DeviceInfoBo di = requestBo.getDeviceInfoBo();
            String deviceId = DeviceUtils.getDeviceId(di.getDeviceId(), di.getImeiId(), di.getSerial());
            //查询 缓存中 deviceId 是否存在
            UrmDevice urmDevice = deviceCM.findDeviceById(deviceId);
            if (urmDevice == null) {
                //创建
                urmDevice = new UrmDevice(deviceId, di.getMac(), di.getBrand(), di.getImeiId(), di.getDeviceId(),
                        di.getSerial(), di.getDeviceName(), di.getOsVersion(), di.getAppVersion(), di.getScreen(),
                        di.getAppType(), di.getMarketChannel(), di.getShopApp(), di.getOtherApp(), di.getScreenSize(),
                        di.getRamSize(), di.getAppCount(), di.getGcmToken());
                insertMap.put(deviceId, urmDevice);

            } else {

                boolean doUpdate = false;

                if (!StringUtils.isEmpty(di.getGcmToken()) && !StringUtils.isEqual(urmDevice.getGcmToken(), di.getGcmToken())) {
                    urmDevice.setGcmToken(di.getGcmToken());
                    doUpdate = true;
                }

                if (StringUtils.isEmpty(urmDevice.getMac())) {
                    urmDevice.setMac(di.getMac());
                    doUpdate = true;
                }

                // 不变的
                if (!StringUtils.isEqual(urmDevice.getScreenSize(), di.getScreenSize())) {
                    urmDevice.setScreenSize(di.getScreenSize());
                    doUpdate = true;
                }

                if (!StringUtils.isEqual(urmDevice.getRamSize(), di.getRamSize())) {
                    urmDevice.setRamSize(di.getRamSize());
                    doUpdate = true;
                }

                // 可能会变的
                if (!StringUtils.isEqual(urmDevice.getShopApp(), di.getShopApp())) {
                    urmDevice.setShopApp(di.getShopApp());
                    doUpdate = true;
                }

                if (!StringUtils.isEqual(urmDevice.getOtherApp(), di.getOtherApp())) {
                    urmDevice.setOtherApp(di.getOtherApp());
                    doUpdate = true;
                }

                if (!StringUtils.isEqual(urmDevice.getAppType(), di.getAppType())) {
                    urmDevice.setAppType(di.getAppType());
                    doUpdate = true;
                }

                if (!StringUtils.isEqual(urmDevice.getMarketChannel(), di.getMarketChannel())) {
                    urmDevice.setMarketChannel(di.getMarketChannel());
                    doUpdate = true;
                }

                if (!StringUtils.isEqual(urmDevice.getAppVersion(), di.getAppVersion())) {
                    urmDevice.setAppVersion(di.getAppVersion());
                    doUpdate = true;
                }

                if (!StringUtils.isEqual(urmDevice.getOsVersion(), di.getOsVersion())) {
                    urmDevice.setOsVersion(di.getOsVersion());
                    doUpdate = true;
                }

                if (!StringUtils.isEqual(urmDevice.getAppCount(), di.getAppCount())) {
                    urmDevice.setAppCount(di.getAppCount());
                    doUpdate = true;
                }

                if (doUpdate) {
                    urmDevice.setUpdateTime(TimeUtils.nowDate());
                    delSet.add(urmDevice.getId());
                    insertMap.put(urmDevice.getId(), urmDevice);
                }
            }

            UrmDeviceRequestLog deviceRequestLog = new UrmDeviceRequestLog(deviceId, requestBo.getRequestUri(), requestBo.getQuery(),
                    di.getShopApp(), di.getAppType(), di.getMarketChannel(), di.getCurShopApp(), di.getAppVersion(), di.getCurNetState());
            // TODO 写入文件
            mdm.save(deviceRequestLog);

            DeviceEventBo deb = requestBo.getDeviceEventBo();
            if (deb != null) {
                UrmDeviceEventLog urmDeviceEventLog = new UrmDeviceEventLog(deviceRequestLog.getId(), deviceId, deb.getEvent(), deb.getInfo());
                mdm.save(urmDeviceEventLog);
            }
        }
        if (delSet.size() > 0) {
            dbm.batchDelete(D_DEVICE_LOG, delSet.toArray(new String[delSet.size()]));
        }
        if (insertMap.size() > 0) {
            dbm.batchSave(new ArrayList<Object>(insertMap.values()));
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDeviceRequest(DeviceRequestBo requestBo) {
        if (requestBo == null) {
            return;
        }

        DeviceInfoBo di = requestBo.getDeviceInfoBo();

        String deviceId = DeviceUtils.getDeviceId(di.getDeviceId(), di.getImeiId(), di.getSerial());

        //查询deviceId 是否存在
        UrmDevice urmDevice = deviceCM.findDeviceById(deviceId);
        if (urmDevice == null) {
            //创建
            urmDevice = new UrmDevice(deviceId, di.getMac(), di.getBrand(), di.getImeiId(), di.getDeviceId(),
                    di.getSerial(), di.getDeviceName(), di.getOsVersion(), di.getAppVersion(), di.getScreen(),
                    di.getAppType(), di.getMarketChannel(), di.getShopApp(), di.getOtherApp(), di.getScreenSize(),
                    di.getRamSize(), di.getAppCount(), di.getGcmToken());
            dbm.create(urmDevice);
        } else {
            boolean doUpdate = false;
            UrmDeviceUpdater urmDeviceUpdater = new UrmDeviceUpdater(urmDevice.getId());

            if (!StringUtils.isEmpty(di.getGcmToken()) && !StringUtils.isEqual(urmDevice.getGcmToken(), di.getGcmToken())) {
                urmDeviceUpdater.getPo().setGcmToken(di.getGcmToken());
                doUpdate = true;
            }

            if (StringUtils.isEmpty(urmDevice.getMac())) {
                urmDeviceUpdater.getPo().setMac(di.getMac());
                doUpdate = true;
            }

            // 不变的
            if (!StringUtils.isEqual(urmDevice.getScreenSize(), di.getScreenSize())) {
                urmDeviceUpdater.getPo().setScreenSize(di.getScreenSize());
                doUpdate = true;
            }

            if (!StringUtils.isEqual(urmDevice.getRamSize(), di.getRamSize())) {
                urmDeviceUpdater.getPo().setRamSize(di.getRamSize());
                doUpdate = true;
            }

            // 可能会变的
            if (!StringUtils.isEqual(urmDevice.getShopApp(), di.getShopApp())) {
                urmDeviceUpdater.getPo().setShopApp(di.getShopApp());
                doUpdate = true;
            }

            if (!StringUtils.isEqual(urmDevice.getOtherApp(), di.getOtherApp())) {
                urmDeviceUpdater.getPo().setOtherApp(di.getOtherApp());
                doUpdate = true;
            }

            if (!StringUtils.isEqual(urmDevice.getAppType(), di.getAppType())) {
                urmDeviceUpdater.getPo().setAppType(di.getAppType());
                doUpdate = true;
            }

            if (!StringUtils.isEqual(urmDevice.getMarketChannel(), di.getMarketChannel())) {
                urmDeviceUpdater.getPo().setMarketChannel(di.getMarketChannel());
                doUpdate = true;
            }

            if (!StringUtils.isEqual(urmDevice.getAppVersion(), di.getAppVersion())) {
                urmDeviceUpdater.getPo().setAppVersion(di.getAppVersion());
                doUpdate = true;
            }

            if (!StringUtils.isEqual(urmDevice.getOsVersion(), di.getOsVersion())) {
                urmDeviceUpdater.getPo().setOsVersion(di.getOsVersion());
                doUpdate = true;
            }

            if (!StringUtils.isEqual(urmDevice.getAppCount(), di.getAppCount())) {
                urmDeviceUpdater.getPo().setAppCount(di.getAppCount());
                doUpdate = true;
            }

            if (doUpdate) {
                urmDeviceUpdater.getPo().setUpdateTime(TimeUtils.nowDate());
                urmDevice = urmDeviceUpdater.getPo();
                dbm.update(urmDeviceUpdater);
            }
            deviceCM.updateDeviceById(urmDevice);
        }

        UrmDeviceRequestLog deviceRequestLog = new UrmDeviceRequestLog(deviceId, requestBo.getRequestUri(), requestBo.getQuery(),
                di.getShopApp(), di.getAppType(), di.getMarketChannel(), di.getCurShopApp(), di.getAppVersion(), di.getCurNetState());
        mdm.save(deviceRequestLog);

        DeviceEventBo deb = requestBo.getDeviceEventBo();
        if (deb != null) {
            UrmDeviceEventLog urmDeviceEventLog = new UrmDeviceEventLog(deviceRequestLog.getId(), deviceId, deb.getEvent(), deb.getInfo());
            mdm.save(urmDeviceEventLog);
        }
    }

    private Query getBaseQuery(String ymd, String deviceBrand, String osVersion, String channel, String deviceYmd) {
        Query baseQuery = Query.query(
                Criteria.where("brand").is(deviceBrand)
                        .andOperator(Criteria.where("osVersion").is(osVersion)
                                .andOperator(Criteria.where("marketChannel").is(channel)
                                        .andOperator(Criteria.where("ymd").is(ymd))))
        );

        if (StringUtils.isEmpty(deviceYmd)) {
            return baseQuery;
        }

        return baseQuery.addCriteria(Criteria.where("deviceYmd").is(deviceYmd));
    }

    @Override
    public StatDayAlive statDayAlive(String ymd, String deviceBrand, String osVersion, MarketChannel channel) {
        if (channel == null) {
            channel = MarketChannel.OFFICIAL;
        }

        String channelName = channel.name();

        long allAlive = 0, eCommerceAll = 0, assistNotFirst = 0, assistFirst = 0, showBallAll = 0, clickBallAll = 0, shopAll = 0, cmpAll = 0;
        long newAlive = 0, eCommerceNew = 0, showBallNew = 0, clickBallNew = 0, shopNew = 0, cmpNew = 0;

        // all alive
        allAlive = mdm.count(StatDevice.class, getBaseQuery(ymd, deviceBrand, osVersion, channelName, null));

        if (allAlive <= 0) {
            return null;
        }

        StatDayAlive sda = new StatDayAlive(ymd, osVersion, deviceBrand, channelName, "", "");

        // all
        Query Q_eCommerceAll = getBaseQuery(ymd, deviceBrand, osVersion, channelName, null).addCriteria(Criteria.where("shopApp").ne(""));
        eCommerceAll = mdm.count(StatDevice.class, Q_eCommerceAll);

//        Query Q_assistNotFirst = getBaseQuery(ymd, deviceBrand, osVersion, channelName, null).addCriteria(Criteria.where("firstBindAssistYmd").ne(ymd).andOperator(Criteria.where("bindAssist").gt(0)));
//                Query Q_assistNotFirst = getBaseQuery(ymd, deviceBrand, osVersion, channelName, null).addCriteria(Criteria.where("bindAssist").gt(0));
        Query Q_assistNotFirst = getBaseQuery(ymd, deviceBrand, osVersion, channelName, null).addCriteria(Criteria.where("firstBindAssistYmd").ne(ymd)).addCriteria(Criteria.where("bindAssist").gt(0));
        assistNotFirst = mdm.count(StatDevice.class, Q_assistNotFirst);

        Query Q_assistFirst = getBaseQuery(ymd, deviceBrand, osVersion, channelName, null).addCriteria(Criteria.where("firstBindAssistYmd").is(ymd)).addCriteria(Criteria.where("bindAssist").gt(0));
        assistFirst = mdm.count(StatDevice.class, Q_assistFirst);

        Query Q_showBallAll = getBaseQuery(ymd, deviceBrand, osVersion, channelName, null).addCriteria(Criteria.where("showBall").gt(0));
        showBallAll = mdm.count(StatDevice.class, Q_showBallAll);

        Query Q_cmpAll = getBaseQuery(ymd, deviceBrand, osVersion, channelName, null).addCriteria(Criteria.where("cmpPrice").gt(0));
        cmpAll = mdm.count(StatDevice.class, Q_cmpAll);

        Query Q_clickBallAll = getBaseQuery(ymd, deviceBrand, osVersion, channelName, null).addCriteria(Criteria.where("clickBall").gt(0));
        clickBallAll = mdm.count(StatDevice.class, Q_clickBallAll);

        Query Q_shopAll = getBaseQuery(ymd, deviceBrand, osVersion, channelName, null).addCriteria(Criteria.where("shop").gt(0));
        shopAll = mdm.count(StatDevice.class, Q_shopAll);

        // new
        Query newBaseQuery = getBaseQuery(ymd, deviceBrand, osVersion, channelName, ymd);
        newAlive = mdm.count(StatDevice.class, newBaseQuery);

        Query Q_eCommerceNew = getBaseQuery(ymd, deviceBrand, osVersion, channelName, ymd).addCriteria(Criteria.where("shopApp").ne(""));
        eCommerceNew = mdm.count(StatDevice.class, Q_eCommerceNew);

        Query Q_showBallNew = getBaseQuery(ymd, deviceBrand, osVersion, channelName, ymd).addCriteria(Criteria.where("showBall").gt(0));
        showBallNew = mdm.count(StatDevice.class, Q_showBallNew);

        Query Q_cmpNew = getBaseQuery(ymd, deviceBrand, osVersion, channelName, ymd).addCriteria(Criteria.where("cmpPrice").gt(0));
        cmpNew = mdm.count(StatDevice.class, Q_cmpNew);

        Query Q_clickBallNew = getBaseQuery(ymd, deviceBrand, osVersion, channelName, ymd).addCriteria(Criteria.where("clickBall").gt(0));
        clickBallNew = mdm.count(StatDevice.class, Q_clickBallNew);

        Query Q_shopNew = getBaseQuery(ymd, deviceBrand, osVersion, channelName, ymd).addCriteria(Criteria.where("shop").gt(0));
        shopNew = mdm.count(StatDevice.class, Q_shopNew);


        sda.setAllAlive(allAlive);
        sda.setNewAlive(newAlive);

        sda.seteCommerceAll(eCommerceAll);
        sda.seteCommerceNew(eCommerceNew);

        sda.setAssistIsFirst(assistFirst);
        sda.setAssistNotFirst(assistNotFirst);

        sda.setShowIconAll(showBallAll);
        sda.setShowIconNew(showBallNew);

        sda.setCmpAll(cmpAll);
        sda.setCmpNew(cmpNew);

        sda.setClickIconAll(clickBallAll);
        sda.setClickIconNew(clickBallNew);

        sda.setClickShopAll(shopAll);
        sda.setClickShopNew(shopNew);

        return sda;
    }

    /*@Override
    public StatDayAlive statDayAlive(String ymd, String deviceBrand, String osVersion, MarketChannel channel) {

        String mcStr = channel == null ? "" : channel.name();

        StatDayAlive sda = new StatDayAlive(ymd, osVersion, deviceBrand, mcStr, "", "");

        final String BASE_SQL = "SELECT COUNT(t.id) FROM StatDevice t WHERE t.brand = ?0 AND t.osVersion=?1 AND t.marketChannel=?2 AND t.ymd=?3 ";
        Object[] params = new Object[]{
                deviceBrand, osVersion, channel, ymd
        };
//        final int index = 4;

        long allAlive = 0, eCommerceAll = 0, assistNotFirst = 0, assistFirst = 0, showBallAll = 0, clickBallAll = 0, shopAll = 0;
        long newAlive = 0, eCommerceNew = 0, showBallNew = 0, clickBallNew = 0, shopNew = 0;

        String SQL_allAlive = BASE_SQL;
        allAlive = dbm.querySingle(SQL_allAlive, Arrays.asList(params));

        if (allAlive > 0) {
            String SQL_eCommerceAll = SQL_allAlive + " AND t.shopApp is not null AND t.shopApp <>'' ";
            eCommerceAll = dbm.querySingle(SQL_eCommerceAll, Arrays.asList(params));

            String SQL_assistNotFirst = SQL_allAlive + " AND t.ymd<>t.firstBindAssistYmd AND t.bindAssist > 0 ";
            assistNotFirst = dbm.querySingle(SQL_assistNotFirst, Arrays.asList(params));

            String SQL_assistFirst = SQL_allAlive + " AND t.ymd=t.firstBindAssistYmd AND t.bindAssist > 0 ";
            assistFirst = dbm.querySingle(SQL_assistFirst, Arrays.asList(params));

            String SQL_showBallAll = SQL_allAlive + " AND t.showBall > 0 ";
            showBallAll = dbm.querySingle(SQL_showBallAll, Arrays.asList(params));

            String SQL_clickBallAll = SQL_allAlive + " AND t.clickBall > 0 ";
            clickBallAll = dbm.querySingle(SQL_clickBallAll, Arrays.asList(params));

            String SQL_shopAll = SQL_allAlive + " AND t.shop > 0 ";
            shopAll = dbm.querySingle(SQL_shopAll, Arrays.asList(params));
        } else {
            return null;
        }

        String SQL_newAlive = BASE_SQL + " AND t.ymd=t.deviceYmd ";
        newAlive = dbm.querySingle(SQL_newAlive, Arrays.asList(params));
        if (newAlive > 0) {

            String SQL_eCommerceNew = SQL_newAlive + " AND t.shopApp is not null AND t.shopApp <>'' ";
            eCommerceNew = dbm.querySingle(SQL_eCommerceNew, Arrays.asList(params));

            String SQL_showBallNew = SQL_newAlive + " AND t.showBall > 0 ";
            showBallNew = dbm.querySingle(SQL_showBallNew, Arrays.asList(params));

            String SQL_clickBallNew = SQL_newAlive + " AND t.clickBall > 0 ";
            clickBallNew = dbm.querySingle(SQL_clickBallNew, Arrays.asList(params));

            String SQL_shopNew = SQL_newAlive + " AND t.shop > 0 ";
            shopNew = dbm.querySingle(SQL_shopNew, Arrays.asList(params));
        }

        sda.setAllAlive(allAlive);
        sda.setNewAlive(newAlive);

        sda.seteCommerceAll(eCommerceAll);
        sda.seteCommerceNew(eCommerceNew);

        sda.setAssistIsFirst(assistFirst);
        sda.setAssistNotFirst(assistNotFirst);

        sda.setShowIconAll(showBallAll);
        sda.setShowIconNew(showBallNew);

        sda.setClickIconAll(clickBallAll);
        sda.setClickIconNew(clickBallNew);

        sda.setClickShopAll(shopAll);
        sda.setClickShopNew(shopNew);

        return sda;
    }*/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDayAlive(StatDayAlive sda) {
        mdm.save(sda);
//        StatDayAlive sda0 = dbm.get(StatDayAlive.class, sda.getId());
//        if (sda0 == null) {
//            dbm.create(sda);
//        } else {
//            StatDayAliveUpdater statDayAliveUpdater = new StatDayAliveUpdater(sda.getId());
//
//            statDayAliveUpdater.getPo().setAllAlive(sda.getAllAlive());
//            statDayAliveUpdater.getPo().setNewAlive(sda.getNewAlive());
//
//            statDayAliveUpdater.getPo().seteCommerceAll(sda.geteCommerceAll());
//            statDayAliveUpdater.getPo().seteCommerceNew(sda.geteCommerceNew());
//
//            statDayAliveUpdater.getPo().setAssistIsFirst(sda.getAssistIsFirst());
//            statDayAliveUpdater.getPo().setAssistNotFirst(sda.getAssistNotFirst());
//
//            statDayAliveUpdater.getPo().setShowIconAll(sda.getShowIconAll());
//            statDayAliveUpdater.getPo().setShowIconNew(sda.getShowIconNew());
//
//            statDayAliveUpdater.getPo().setClickIconAll(sda.getClickIconAll());
//            statDayAliveUpdater.getPo().setClickIconNew(sda.getClickIconNew());
//
//            statDayAliveUpdater.getPo().setClickShopAll(sda.getClickShopAll());
//            statDayAliveUpdater.getPo().setClickShopNew(sda.getClickShopNew());
//            dbm.update(statDayAliveUpdater);
//        }
    }


}
