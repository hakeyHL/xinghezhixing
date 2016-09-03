package hasoffer.api.worker;

import hasoffer.api.controller.vo.DeviceRequestVo;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Date : 2016/1/14
 * Function :
 */
public class DeviceRequestQueue {

    private static DeviceRequestQueue queue = new DeviceRequestQueue();
    private LinkedBlockingQueue<DeviceRequestVo> deviceRequestVos;

    private DeviceRequestQueue() {
        deviceRequestVos = new LinkedBlockingQueue<DeviceRequestVo>();
    }

    public static void addLog(DeviceRequestVo deviceRequestVo) {
        queue.deviceRequestVos.add(deviceRequestVo);
    }

    public static DeviceRequestVo get() {
        return queue.deviceRequestVos.poll();
    }

    public static Object getCount() {
        return queue.deviceRequestVos.size();
    }
}
