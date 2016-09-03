package hasoffer.core.worker;

import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import hasoffer.core.user.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/4/22
 * Function :
 */
public class ListDeviceWorker implements Runnable {

    IDeviceService deviceService;
    ListProcessWorkerStatus ws;
    List<String> ymds;
    private Logger logger = LoggerFactory.getLogger(ListDeviceWorker.class);

    public ListDeviceWorker(IDeviceService deviceService, ListProcessWorkerStatus ws, List<String> ymds) {
        this.deviceService = deviceService;
        this.ws = ws;
        this.ymds = ymds;
    }

    @Override
    public void run() {

        for (String ymd : ymds) {
            listByDay(ymd);
        }

        ws.setListWorkFinished(true);
    }

    void listByDay(String ymd) {
        int page = 1, PAGE_SIZE = 3000;

        Date startDate = TimeUtils.stringToDate(ymd, TimeUtils.PATTERN_YMD);
        Date endDate = TimeUtils.addDay(startDate, 1);

        PageableResult<UrmDevice> pagedDevices = deviceService.findPagedDevices(startDate, endDate, page, PAGE_SIZE);//deviceService.findPagedDevicesAsc(page, PAGE_SIZE);
        List<UrmDevice> devices = pagedDevices.getData();

        long TOTAL_PAGE = pagedDevices.getTotalPage();

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
                pagedDevices = deviceService.findPagedDevices(startDate, endDate, page, PAGE_SIZE);//deviceService.findPagedDevicesAsc(page, PAGE_SIZE);
                devices = pagedDevices.getData();
            }

            if (ArrayUtils.hasObjs(devices)) {
                ws.getSdQueue().addAll(devices);
            }

            page++;
        }
    }

}
