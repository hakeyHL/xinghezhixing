package hasoffer.core.worker;

import hasoffer.core.persistence.mongo.StatDevice;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import hasoffer.core.user.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/4/22
 * Function :
 */
public class UpdateBindAssistYmdWorker implements Runnable {

    IDeviceService deviceService;
    ListProcessWorkerStatus ws;
    private Logger logger = LoggerFactory.getLogger(UpdateBindAssistYmdWorker.class);

    public UpdateBindAssistYmdWorker(IDeviceService deviceService, ListProcessWorkerStatus ws) {
        this.deviceService = deviceService;
        this.ws = ws;
    }

    @Override
    public void run() {

        while (true) {
            StatDevice sd = (StatDevice) ws.getSdQueue().poll();

            if (sd == null) {
                if (ws.isListWorkFinished()) {
                    break;
                } else {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
            }

            try {
                deviceService.updateFirstBindAssistYmd(sd.getDeviceId());
            } catch (Exception e) {
                logger.debug(e.getStackTrace().toString());
            }
        }
    }
}
