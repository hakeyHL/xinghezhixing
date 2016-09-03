package hasoffer.core.worker;

import hasoffer.base.enums.MarketChannel;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.mongo.StatDevice;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import hasoffer.core.user.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/4/21
 * Function :
 */
public class StatDeviceWorker implements Runnable {

    IDeviceService deviceService;
    ListProcessWorkerStatus<UrmDevice> ws;
    List<String> ymds;

    private Logger logger = LoggerFactory.getLogger(StatDeviceWorker.class);

    public StatDeviceWorker(IDeviceService deviceService,
                            ListProcessWorkerStatus<UrmDevice> ws,
                            List<String> ymds) {
        this.deviceService = deviceService;
        this.ws = ws;
        this.ymds = ymds;
    }

    @Override
    public void run() {
        while (true) {
            UrmDevice device = ws.getSdQueue().poll();

            if (device == null) {
                if (ws.isListWorkFinished()) {
                    break;
                }
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    break;
                }
                continue;
            }

            try {
                stat(device);
            } catch (Exception e) {
                logger.debug(device.getId() + " stating but has exception." + e.getMessage());
            }
        }
    }

    private void stat(UrmDevice device) {
        if (!device.isStatAble()) {
            return;
        }

        Date startDate = device.getCreateTime();
        Date endDate = TimeUtils.nowDate();

        String startYmd = TimeUtils.parse(startDate, TimeUtils.PATTERN_YMD);
        String endYmd = TimeUtils.parse(endDate, TimeUtils.PATTERN_YMD);

        for (String ymd : ymds) {

            if (ymd.compareTo(startYmd) < 0) {
                // 这个设备ymd这天还没出生
                continue;
            }

            if (ymd.compareTo(endYmd) > 0) {
                // 这一天还没到
                return;
            }

            Map<MarketChannel, StatDevice> statResult = deviceService.statByLog(device, ymd);

            for (Map.Entry<MarketChannel, StatDevice> result : statResult.entrySet()) {
                StatDevice sd = result.getValue();
                deviceService.saveDeviceStatResult(sd);
            }
        }

        logger.debug("[OK] stat device : " + device.getId());
    }

}
