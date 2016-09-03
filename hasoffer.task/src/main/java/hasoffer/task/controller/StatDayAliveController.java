package hasoffer.task.controller;

import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.StatDayAlive;
import hasoffer.core.persistence.mongo.StatDevice;
import hasoffer.core.user.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Date : 2016/4/22
 * Function : 统计设备日活等数据
 * perdeviceperday - 第一步，按照每设备每天发的请求统计一条数据
 * updatefirstbindassist - 第二步，根据第一步的统计结果，计算设备第一次打开辅助功能的时间
 * statdayalive - 第三步，根据1、2步统计结果，统计整日的各纬度（客户端品牌、渠道、系统版本）统计日活
 * <p>
 * http://192.168.1.202:8021/statdayalive/start?start=20160223&end=20160501
 */
@Controller
@RequestMapping(value = "/statdayalive")
public class StatDayAliveController {

    @Resource
    IDeviceService deviceService;
    @Resource
    IDataBaseManager dbm;

    private Logger logger = LoggerFactory.getLogger(StatDayAliveController.class);

    @RequestMapping(value = "/{start}/{end}", method = RequestMethod.GET)
    public
    @ResponseBody
    void statdayalive(@PathVariable String start,
                      @PathVariable String end) {

        List<String> ymds = new ArrayList<String>();
        TimeUtils.fillDays(ymds, start, end, TimeUtils.PATTERN_YMD);

        for (String ymd : ymds) {
            logger.debug(ymd);
            statByYMD(ymd);
        }
    }

    private void statByYMD(String ymd) {
        Map<String, StatDayAlive> dayAliveMap = new HashMap<String, StatDayAlive>();//getInitMap(ymd, brandSet, osSet, mcSet);

        int page = 1, PAGE_SIZE = 2000;

        PageableResult<StatDevice> pagedStatDevices = deviceService.listPagedStatDevice(ymd, page, PAGE_SIZE);
        List<StatDevice> statDevices = pagedStatDevices.getData();

        long totalPage = pagedStatDevices.getTotalPage();

        int total = 0;
        AtomicInteger passCount = new AtomicInteger(0);

        while (page <= totalPage) {

            logger.debug("page = " + page + "/" + totalPage);

            if (page > 1) {
                statDevices = deviceService.listStatDevice(ymd, page, PAGE_SIZE);
            }

            for (StatDevice sd : statDevices) {
                total++;
                statOneDevice(dayAliveMap, sd, passCount);

                if (total % 500 == 0) {
                    logger.debug(String.format("stat %d devices. %d passed.", total, passCount.get()));
                }
            }

            page++;
        }

        for (Map.Entry<String, StatDayAlive> kv : dayAliveMap.entrySet()) {
            deviceService.saveDayAlive(kv.getValue());
        }

        logger.debug("[OK] Save day alive : " + dayAliveMap.size());
    }

    private void statOneDevice(Map<String, StatDayAlive> dayAliveMap, StatDevice sd,
                               AtomicInteger passCount) {
        String os = sd.getOsVersion();
        String brand = sd.getBrand();
        MarketChannel mc = sd.getMarketChannel();

        if (mc == null) {
            mc = MarketChannel.OFFICIAL;
        }

        StatDayAlive[] sdas = new StatDayAlive[]{
                new StatDayAlive(sd.getYmd(), os, brand, mc.name(), "", ""),

                new StatDayAlive(sd.getYmd(), os, brand, "ALL", "", ""),
                new StatDayAlive(sd.getYmd(), os, "ALL", mc.name(), "", ""),
                new StatDayAlive(sd.getYmd(), "ALL", brand, mc.name(), "", ""),

                new StatDayAlive(sd.getYmd(), "ALL", "ALL", mc.name(), "", ""),
                new StatDayAlive(sd.getYmd(), "ALL", brand, "ALL", "", ""),
                new StatDayAlive(sd.getYmd(), os, "ALL", "ALL", "", ""),

                new StatDayAlive(sd.getYmd(), "ALL", "ALL", "ALL", "", "")
        };

        for (StatDayAlive sda : sdas) {

            getOneStat(sda, sd);

            StatDayAlive sdaInMap = dayAliveMap.get(sda.getId());

            if (sdaInMap == null) {
                dayAliveMap.put(sda.getId(), sda);
            } else {
                calSum(sdaInMap, sda);
            }
        }
    }

    private void getOneStat(StatDayAlive sda, StatDevice sd) {
        sda.setAllAlive(1);

        if (sd.getDeviceYmd().equals(sd.getYmd())) {
            sda.setNewAlive(1);

            if (sd.getShowBall() > 0) {
                sda.setShowIconNew(1);
            }

            if (sd.getClickBall() > 0) {
                sda.setClickIconNew(1);
            }

            if (sd.getShop() > 0) {
                sda.setClickShopNew(1);
            }

            if (!StringUtils.isEmpty(sd.getShopApp())) {
                sda.seteCommerceNew(1);
            }

            if (sd.getCmpPrice() > 0) {
                sda.setCmpNew(1);
            }

            if (sd.getWakeUp() > 0) {
                sda.setWakeUpNew(1);
            }
        }

        if (sd.getBindAssist() > 0) {
            sda.setBindAssist(1);
            if (sd.getYmd().equals(sd.getFirstBindAssistYmd())) {
                sda.setAssistIsFirst(1);
            } else {
                sda.setAssistNotFirst(1);
            }
        }

        if (sd.getWakeUp() > 0) {
            sda.setWakeUpAll(1);
        }

        if (sd.getShowBall() > 0) {
            sda.setShowIconAll(1);
        }

        if (sd.getClickBall() > 0) {
            sda.setClickIconAll(1);
        }

        if (sd.getShop() > 0) {
            sda.setClickShopAll(1);
        }

        if (!StringUtils.isEmpty(sd.getShopApp())) {
            sda.seteCommerceAll(1);
        }

        if (sd.getCmpPrice() > 0) {
            sda.setCmpAll(1);
        }
    }

    private void calSum(StatDayAlive sdaAll, StatDayAlive sda) {

        sdaAll.setAllAlive(sdaAll.getAllAlive() + sda.getAllAlive());
        sdaAll.setNewAlive(sdaAll.getNewAlive() + sda.getNewAlive());

        sdaAll.setAssistNotFirst(sdaAll.getAssistNotFirst() + sda.getAssistNotFirst());
        sdaAll.setAssistIsFirst(sdaAll.getAssistIsFirst() + sda.getAssistIsFirst());

        sdaAll.setBindAssist(sdaAll.getBindAssist() + sda.getBindAssist());

        sdaAll.setWakeUpAll(sdaAll.getWakeUpAll() + sda.getWakeUpAll());
        sdaAll.setWakeUpNew(sdaAll.getWakeUpNew() + sda.getWakeUpNew());

        sdaAll.setShowIconAll(sdaAll.getShowIconAll() + sda.getShowIconAll());
        sdaAll.setShowIconNew(sdaAll.getShowIconNew() + sda.getShowIconNew());

        sdaAll.setClickShopAll(sdaAll.getClickShopAll() + sda.getClickShopAll());
        sdaAll.setClickShopNew(sdaAll.getClickShopNew() + sda.getClickShopNew());

        sdaAll.setClickIconAll(sdaAll.getClickIconAll() + sda.getClickIconAll());
        sdaAll.setClickIconNew(sdaAll.getClickIconNew() + sda.getClickIconNew());

        sdaAll.seteCommerceAll(sdaAll.geteCommerceAll() + sda.geteCommerceAll());
        sdaAll.seteCommerceNew(sdaAll.geteCommerceNew() + sda.geteCommerceNew());

        sdaAll.setCmpAll(sdaAll.getCmpAll() + sda.getCmpAll());
        sdaAll.setCmpNew(sdaAll.getCmpNew() + sda.getCmpNew());
    }

}
