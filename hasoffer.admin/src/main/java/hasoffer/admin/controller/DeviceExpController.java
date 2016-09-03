package hasoffer.admin.controller;

import hasoffer.base.enums.AppType;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.urm.UrmDevice;
import jodd.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by chevy on 2016/6/21.
 */
@Controller
@RequestMapping(value = "/clientexp")
public class DeviceExpController {

    @Resource
    IDataBaseManager dbm;

    private Logger logger = LoggerFactory.getLogger(DeviceExpController.class);

    private String Q_DEVICE = "SELECT t from UrmDevice t where t.createTime >= ?0 and t.createTime < ?1 ";

    private String PATH_DIR = "/home/hasoffer/devices/";

    @RequestMapping(value = "/from/{symd}/to/{eymd}", method = RequestMethod.GET)
    public
    @ResponseBody
    String expDevice(@PathVariable String symd,
                     @PathVariable String eymd) {

        List<String> ymds = new ArrayList<String>();

        TimeUtils.fillDays(ymds, symd, eymd, TimeUtils.PATTERN_YMD);

        for (String ymd : ymds) {
            try {
                expDevice(ymd);
            } catch (Exception e) {
                logger.debug(e.getMessage());
                continue;
            }
        }

        return "ok";
    }

    private void expDevice(String ymd) {
        File file = makeFileAndDir(ymd);
        if (file == null) {
            logger.debug("file is null");
            return;
        }
        logger.debug("create file : " + file.getAbsolutePath());

        Date startTime = TimeUtils.stringToDate(ymd, TimeUtils.PATTERN_YMD);
        Date endTime = TimeUtils.addDay(startTime, 1);

        int pageSize = 2000;
        int page = 1;

        PageableResult<UrmDevice> pagedDevices = dbm.queryPage(Q_DEVICE, page, pageSize, Arrays.asList(startTime, endTime));
        long totalPage = pagedDevices.getTotalPage();

        while (page <= totalPage) {
            logger.debug("exp page = " + page);
            List<UrmDevice> devices = null;

            if (page == 1) {
                devices = pagedDevices.getData();
                String title = "id,createTime,updateTime,brand,mac,deviceId,imeiId,serial,deviceName,osVersion,screen,appVersion,shopApp,otherApp,appType,marketChannel,screenSize,ramSize,appCount,gcmToken\n";
                try {
                    FileUtil.appendString(file, title);
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                }
            } else {
                devices = dbm.query(Q_DEVICE, page, pageSize, Arrays.asList(startTime, endTime));
            }

            if (ArrayUtils.hasObjs(devices)) {
                for (UrmDevice device : devices) {
//                    logger.debug(device.getId());

                    StringBuffer sb = new StringBuffer(device.getId());

                    if (device.getMarketChannel() == null) {
                        device.setMarketChannel(MarketChannel.OFFICIAL);
                    }
                    if (device.getAppType() == null) {
                        device.setAppType(AppType.APP);
                    }

                    append(sb, TimeUtils.parse(device.getCreateTime(), "yyyy/MM/dd HH:mm:ss"));
                    append(sb, TimeUtils.parse(device.getUpdateTime(), "yyyy/MM/dd HH:mm:ss"));
                    append(sb, device.getBrand());
                    append(sb, device.getMac());
                    append(sb, device.getDeviceId());
                    append(sb, device.getImeiId());
                    append(sb, device.getSerial());
                    append(sb, device.getDeviceName());
                    append(sb, device.getOsVersion());
                    append(sb, device.getScreen());
                    append(sb, device.getAppVersion());
                    append(sb, device.getShopApp());
                    append(sb, device.getOtherApp());
                    append(sb, device.getAppType().name());
                    append(sb, device.getMarketChannel().name());
                    append(sb, device.getScreenSize());
                    append(sb, device.getRamSize());
                    append(sb, String.valueOf(device.getAppCount()));
                    append(sb, device.getGcmToken());

                    sb.append("\n");

                    try {
                        FileUtil.appendString(file, sb.toString());
                    } catch (Exception e) {
                        continue;
                    }
                }
            }

            page++;
        }

    }

    private void append(StringBuffer sb, String s) {
        sb.append(",");
        if (StringUtils.isEmpty(s) || "null".equalsIgnoreCase(s)) {
            sb.append("");
        } else {
            if (s.contains(",")) {
                sb.append(s.replaceAll(",", "_"));
            } else {
                sb.append(s);
            }
        }
    }

    private File makeFileAndDir(String ymd) {
        String ym = ymd.substring(0, 6);
        String fileDir = PATH_DIR + ym;
        File file = new File(fileDir + "/" + ymd + ".csv");
        try {
            FileUtil.mkdirs(new File(fileDir));

            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
        return file;
    }

}
