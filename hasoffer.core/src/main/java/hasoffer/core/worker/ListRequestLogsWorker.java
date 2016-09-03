package hasoffer.core.worker;

import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.mongo.UrmDeviceRequestLog;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import hasoffer.core.user.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/4/22
 * Function :
 */
public class ListRequestLogsWorker implements Runnable {

    IDeviceService deviceService;
    ListProcessWorkerStatus ws;
    String ymd;

    Set<String> deviceSet = new HashSet<String>();

    private Logger logger = LoggerFactory.getLogger(ListRequestLogsWorker.class);

    public ListRequestLogsWorker(IDeviceService deviceService,
                                 ListProcessWorkerStatus ws,
                                 String ymd) {
        this.deviceService = deviceService;
        this.ws = ws;
        this.ymd = ymd;
    }

    @Override
    public void run() {
        int page = 1, PAGE_SIZE = 1500;

        String YMD_PATTERN = "yyyyMMdd";
        Date startDate = TimeUtils.stringToDate(ymd, YMD_PATTERN);
        Date endDate = TimeUtils.addDay(startDate, 1);

        PageableResult<UrmDeviceRequestLog> pagedDeviceRequestLogs = deviceService.findRequestLogs(startDate, endDate, page, PAGE_SIZE);

        long TOTAL_PAGE = pagedDeviceRequestLogs.getTotalPage();

        List<UrmDeviceRequestLog> requestLogs = null;

        while (page <= TOTAL_PAGE) {
            if (ws.getSdQueue().size() > 1000) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                }
                continue;
            }

            logger.debug(String.format("stat [%d/%d]", page, TOTAL_PAGE));

            if (page > 1) {
                try {
                    pagedDeviceRequestLogs = deviceService.findRequestLogs(startDate, endDate, page, PAGE_SIZE);
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                    continue;
                }
            }

            requestLogs = pagedDeviceRequestLogs.getData();

            if (ArrayUtils.hasObjs(requestLogs)) {
                for (UrmDeviceRequestLog requestLog : requestLogs) {
                    String deviceId = requestLog.getDeviceId();
                    if (deviceSet.contains(deviceId)) {
                        continue;
                    } else {
                        UrmDevice device = deviceService.findDevice(deviceId);
                        if (device != null) {
                            ws.getSdQueue().add(device);
                        }
                        deviceSet.add(deviceId);
                    }
                }
            }

            page++;
        }

        ws.setListWorkFinished(true);
    }

}
