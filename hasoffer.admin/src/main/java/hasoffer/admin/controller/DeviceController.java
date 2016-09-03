package hasoffer.admin.controller;

import hasoffer.admin.controller.vo.DayVisitVo;
import hasoffer.admin.controller.vo.DeviceBuyLogVo;
import hasoffer.admin.controller.vo.DeviceRequestLogVo;
import hasoffer.admin.controller.vo.DeviceVo;
import hasoffer.base.enums.AppType;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.PageModel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.bo.user.DayVisitBo;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.UrmDeviceBuyLog;
import hasoffer.core.persistence.mongo.UrmDeviceRequestLog;
import hasoffer.core.persistence.po.urm.UrmDayVisit;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.product.IProductService;
import hasoffer.core.system.IAppService;
import hasoffer.core.user.IDeviceService;
import hasoffer.webcommon.helper.PageHelper;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date : 2016/1/19
 * Function :
 */
@Controller
@RequestMapping(value = "/client")
public class DeviceController {

    @Resource
    IDeviceService deviceService;
    @Resource
    IAppService appService;
    @Resource
    IProductService productService;
    @Resource
    IDataBaseManager dbm;

    String[] FROMAPPS = {
            "FLIPKART",
            "SNAPDEAL",
            "PAYTM",
            "EBAY",
            "SHOPCLUES",
            "ASKME"
    };

    String[] TOAPPS = {
            "FLIPKART",
            "SNAPDEAL",
            "SHOPCLUES",
            "PAYTM",
            "AMAZON",
            "EBAY",
            "INFIBEAM",
            "INDIATIMES",
            "ASKMEBAZAAR",
            "BABYOYE",
            "ZOOMIN",
            "THEITDEPOT",
            "FIRSTCRY",
            "CROMARETAIL",
            "SAHOLIC",
            "HOMESHOP18",
            "MANIACSTORE",
            "NAAPTOL",
            "GADGETS360",
            "EDABBA",
            "PURPLLE",
            "SYBERPLACE",
            "SHOPMONK",
            "BAGITTODAY"
    };
    private Logger logger = LoggerFactory.logger(DeviceController.class);

    @RequestMapping(value = "/buy", method = RequestMethod.GET)
    public ModelAndView buy(HttpServletRequest request,
                            @RequestParam(required = false) String fromWebsite,
                            @RequestParam(required = false) String toWebsite,
                            @RequestParam(defaultValue = "") String startTime,
                            @RequestParam(defaultValue = "") String endTime,
                            @RequestParam(required = false) String orderByFieldName,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "50") int size) {
        ModelAndView mav = new ModelAndView("client/buylist");

        final String DATE_PATTERN_FROM_WEB = "MM/dd/yyyy";

        Date startDate = null;
        Date endDate = null;
        if (StringUtils.isEmpty(startTime)) {
            startDate = new Date(TimeUtils.today());
            endDate = new Date();
            startTime = TimeUtils.parse(startDate, DATE_PATTERN_FROM_WEB);
            endTime = startTime;
        } else {
            startDate = TimeUtils.stringToDate(startTime, DATE_PATTERN_FROM_WEB);
            endDate = TimeUtils.addDay(TimeUtils.stringToDate(endTime, DATE_PATTERN_FROM_WEB), 1);
        }

        if (!StringUtils.isEmpty(fromWebsite)) {
            fromWebsite = fromWebsite.toUpperCase();
        }

        if (!StringUtils.isEmpty(toWebsite)) {
            toWebsite = toWebsite.toUpperCase();
        }

        PageableResult<UrmDeviceBuyLog> logs = deviceService.findUrmDeviceBuyLog(fromWebsite, toWebsite, startDate, endDate, page, size, "createTime", 0);


        List<DeviceBuyLogVo> deviceBuyLogVos = getBuyLogs(logs.getData());

        mav.addObject("requestLogs", deviceBuyLogVos);
        mav.addObject("page", PageHelper.getPageModel(request, logs));

        if (StringUtils.isEmpty(fromWebsite)) {
            fromWebsite = "";
        }
        mav.addObject("fromWebsite", fromWebsite);

        if (StringUtils.isEmpty(toWebsite)) {
            toWebsite = "";
        }
        mav.addObject("toWebsite", toWebsite);

        mav.addObject("fromapps", FROMAPPS);
        mav.addObject("toapps", TOAPPS);

        mav.addObject("startTime", startTime);
        mav.addObject("endTime", endTime);

        return mav;
    }

    private List<DeviceBuyLogVo> getBuyLogs(List<UrmDeviceBuyLog> logs) {
        List<DeviceBuyLogVo> buyLogVos = new ArrayList<DeviceBuyLogVo>();

        if (ArrayUtils.isNullOrEmpty(logs)) {
            return buyLogVos;
        }

        for (UrmDeviceBuyLog log : logs) {

            AppType appType = log.getAppType();
            MarketChannel marketChannel = log.getMarketChannel();
            Date createTime = log.getCreateTime();
            String deviceId = log.getDeviceId();

            Website fromWebsite = log.getFromWebsite();
            Website toWebsite = log.getToWebsite();
            String id = log.getId();
            long ptmProductId = log.getPtmProductId();
            String title = log.getTitle();
            String shopApp = log.getShopApp();

            DeviceBuyLogVo urmDeviceBuyLog = new DeviceBuyLogVo(appType, createTime, deviceId, fromWebsite, marketChannel, ptmProductId, shopApp, title, toWebsite);
            buyLogVos.add(urmDeviceBuyLog);
        }
        return buyLogVos;
    }

    @RequestMapping(value = "/fixclients", method = RequestMethod.GET)
    public ModelAndView fixclients(HttpServletRequest request) {

        // 删除20160223 之前的设备及请求日志

        return new ModelAndView("system/ok");
    }

    @RequestMapping(value = "/fixshopapp", method = RequestMethod.GET)
    public ModelAndView detail(HttpServletRequest request) {

        int page = 1, size = 500;
        PageableResult pagedDevices = deviceService.findPagedDevices(page, size);

        long tPage = pagedDevices.getTotalPage();

        while (page <= tPage) {
            if (page > 1) {
                pagedDevices = deviceService.findPagedDevices(page, size);
            }

            List<UrmDevice> devices = pagedDevices.getData();

            if (ArrayUtils.hasObjs(devices)) {
                for (UrmDevice device : devices) {
                    if (StringUtils.isEmpty(device.getShopApp())) {
                        PageableResult<UrmDeviceRequestLog> pagedLogs = deviceService.findDeviceLogs(device.getId(), 1, 1);
                        List<UrmDeviceRequestLog> logs = pagedLogs.getData();
                        if (ArrayUtils.hasObjs(logs)) {
                            UrmDeviceRequestLog log = logs.get(0);
                            if (!StringUtils.isEmpty(log.getShopApp())) {
                                deviceService.updateDeviceShopApp(device.getId(), log.getShopApp());
                            }
                        }
                    }
                }
            }

            page++;
        }

        return new ModelAndView("system/ok");
    }

    @RequestMapping(value = "/detail/{deviceId}", method = RequestMethod.GET)
    public ModelAndView detail(HttpServletRequest request,
                               @PathVariable String deviceId,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "50") int size) {

        UrmDevice device = deviceService.findDevice(deviceId);

        PageableResult<UrmDeviceRequestLog> pagedLogs = deviceService.findDeviceLogs(deviceId, page, size);

        ModelAndView mav = new ModelAndView("client/detail");
        mav.addObject("device", device);
        mav.addObject("logs", pagedLogs.getData());
        mav.addObject("page", PageHelper.getPageModel(request, pagedLogs));

        return mav;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView catelist(HttpServletRequest request,
                                 @RequestParam(required = false) String deviceName,
                                 @RequestParam(defaultValue = "") String startTime,
                                 @RequestParam(defaultValue = "") String endTime,
                                 @RequestParam(required = false) String appTypeString,
                                 @RequestParam(required = false) String marketChannelString,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "50") int size) {

        final String DATE_PATTERN_FROM_WEB = "MM/dd/yyyy";

        Date startDate = null;
        Date endData = null;
        if (StringUtils.isEmpty(startTime)) {
            startDate = new Date(TimeUtils.today());
            endData = new Date();
            startTime = TimeUtils.parse(startDate, DATE_PATTERN_FROM_WEB);
            endTime = startTime;
        } else {
            startDate = TimeUtils.stringToDate(startTime, DATE_PATTERN_FROM_WEB);
            endData = TimeUtils.addDay(TimeUtils.stringToDate(endTime, DATE_PATTERN_FROM_WEB), 1);
        }

        AppType appType = null;
        if (!StringUtils.isEqual("0", appTypeString)) {
            if (!StringUtils.isEmpty(appTypeString)) {
                appType = AppType.valueOf(appTypeString);
            }
        }


        MarketChannel marketChannel = null;
        if (!StringUtils.isEqual("0", marketChannelString)) {
            if (!StringUtils.isEmpty(marketChannelString)) {
                marketChannel = MarketChannel.valueOf(marketChannelString);
            }
        }

        PageableResult<UrmDevice> devices = deviceService.findPagedDevices(deviceName, appType, marketChannel, startDate, endData, page, size, "createTime", 0);

        ModelAndView mav = new ModelAndView("client/list");
        mav.addObject("devices", devices.getData());
        mav.addObject("page", PageHelper.getPageModel(request, devices));

        if (StringUtils.isEmpty(deviceName)) {
            deviceName = "";
        }
        mav.addObject("deviceName", deviceName);

        if (StringUtils.isEmpty(appTypeString)) {
            appTypeString = "";
        }
        mav.addObject("appTypeString", appTypeString);

        if (StringUtils.isEmpty(marketChannelString)) {
            marketChannelString = "";
        }
        mav.addObject("marketChannelString", marketChannelString);
        mav.addObject("startTime", startTime);
        mav.addObject("endTime", endTime);

        return mav;
    }

    @RequestMapping(value = "/statnow", method = RequestMethod.GET)
    public ModelAndView statnow(HttpServletRequest request,
                                @RequestParam(defaultValue = "") final String ymd) {
        ModelAndView mav = new ModelAndView("system/ok");

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(new Runnable() {
            @Override
            public void run() {
                DayVisitBo dayVisitBo = deviceService.statDayVisit(ymd);
                deviceService.saveOrUpdate(dayVisitBo);
            }
        });

        return mav;
    }

    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    public ModelAndView stat(HttpServletRequest request) {

        List<UrmDayVisit> dayVisits = deviceService.listDayVisits();

        List<DayVisitVo> dayVisitVos = new ArrayList<DayVisitVo>();

        DayVisitVo totalVisitVo = null;

        if (ArrayUtils.hasObjs(dayVisits)) {
            for (UrmDayVisit dayVisit : dayVisits) {
                if (totalVisitVo == null) {
                    totalVisitVo = new DayVisitVo(dayVisit);
                } else {
                    totalVisitVo.setAliveDevice(totalVisitVo.getAliveDevice() + dayVisit.getVisitDevice());
                    totalVisitVo.setNewDevice(totalVisitVo.getNewDevice() + dayVisit.getNewDevice());
                    totalVisitVo.setNewDeviceWithShop(totalVisitVo.getNewDeviceWithShop() + dayVisit.getNewDeviceWithShop());
                    totalVisitVo.setVisitDevice(totalVisitVo.getVisitDevice() + dayVisit.getVisitDevice());
                    totalVisitVo.setVisitDeviceNew(totalVisitVo.getVisitDeviceNew() + dayVisit.getVisitDeviceNew());
                }
                dayVisitVos.add(new DayVisitVo(dayVisit));
            }
        }

        ModelAndView mav = new ModelAndView("client/stat");
        mav.addObject("dayVisits", dayVisitVos);
        mav.addObject("totalVisit", totalVisitVo);

        return mav;
    }

    @RequestMapping(value = "/requestlist", method = RequestMethod.GET)
    public ModelAndView requestlist(HttpServletRequest request,
                                    @RequestParam(defaultValue = "") String requestUri,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "20") int size) {
        ModelAndView mav = new ModelAndView("client/requestlist");

        PageableResult<UrmDeviceRequestLog> pagedRequestLogs = deviceService.findDeviceLogsByRequestUri(requestUri, page, size);

        PageModel pageModel = PageHelper.getPageModel(request, pagedRequestLogs);
        mav.addObject("requestLogs", getRequestLogs(pagedRequestLogs.getData()));
        mav.addObject("page", pageModel);

        return mav;
    }

    private List<DeviceRequestLogVo> getRequestLogs(List<UrmDeviceRequestLog> logs) {

        List<DeviceRequestLogVo> requestLogVos = new ArrayList<DeviceRequestLogVo>();

        Map<String, UrmDevice> deviceMap = new HashMap<String, UrmDevice>();

        if (ArrayUtils.hasObjs(logs)) {
            for (UrmDeviceRequestLog requestLog : logs) {
                String deviceId = requestLog.getDeviceId();
                UrmDevice device = deviceMap.get(deviceId);
                if (device == null) {
                    device = dbm.get(UrmDevice.class, deviceId);

                    if (device == null) {
                        logger.error(String.format(String.format("device[%s] is null.request log[%s]", deviceId, requestLog.getId())));
                        continue;
                    }

                    deviceMap.put(deviceId, device);
                }

                requestLogVos.add(new DeviceRequestLogVo(requestLog, new DeviceVo(device)));
            }
        }

        return requestLogVos;
    }

}