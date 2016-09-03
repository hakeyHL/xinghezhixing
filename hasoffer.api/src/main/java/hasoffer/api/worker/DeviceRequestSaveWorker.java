package hasoffer.api.worker;

import hasoffer.api.controller.vo.DeviceEventVo;
import hasoffer.api.controller.vo.DeviceInfoVo;
import hasoffer.api.controller.vo.DeviceRequestVo;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.bo.user.DeviceEventBo;
import hasoffer.core.bo.user.DeviceInfoBo;
import hasoffer.core.bo.user.DeviceRequestBo;
import hasoffer.core.user.IDeviceService;

import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/1/14
 * Function :
 */
public class DeviceRequestSaveWorker implements Runnable {

    private IDeviceService deviceService;

    public DeviceRequestSaveWorker(IDeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public void run() {
        int sleep = 3;
        int sleepCount = 0;
        while (true) {
            try {
                DeviceRequestVo deviceRequestVo = DeviceRequestQueue.get();

                if (deviceRequestVo == null) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                        sleepCount++;
                    } catch (InterruptedException e) {
                    }
                    continue;
                }

                deviceService.saveDeviceRequest(getBo(deviceRequestVo));
            } catch (Exception e) {
                // 忽略异常
            }
        }
    }

    private DeviceRequestBo getBo(DeviceRequestVo deviceRequestVo) {

        DeviceInfoVo div = deviceRequestVo.getDeviceInfoVo();

        if (div == null) {
            return null;
        }

        DeviceInfoBo deviceInfoBo = new DeviceInfoBo(
                div.getMac(),
                div.getBrand(), div.getImeiId(),
                div.getDeviceId(), div.getSerial(),
                div.getDeviceName(), div.getOsVersion(),
                div.getAppVersion(), div.getScreen(),
                StringUtils.arrayToString(div.getShopApp(), ","),
                div.getAppType(), div.getMarketChannel(),
                div.getCurShopApp(),
                StringUtils.arrayToString(div.getOtherApp(), ","),
                div.getScreenSize(), div.getRamSize(),
                div.getCurNetState(), div.getAppCount(),
                div.getGcmToken()
        );

        DeviceRequestBo drb = new DeviceRequestBo(deviceInfoBo, deviceRequestVo.getRequestUri(), deviceRequestVo.getQuery());

        DeviceEventVo de = deviceRequestVo.getDeviceEvent();
        if (de != null) {
            DeviceEventBo debo = new DeviceEventBo(de.getEvent(), de.getInfo());
            drb.setDeviceEventBo(debo);
        }

        return drb;
    }
}
