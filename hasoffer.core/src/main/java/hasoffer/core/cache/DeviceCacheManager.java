package hasoffer.core.cache;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.po.urm.UrmDevice;
import hasoffer.core.redis.ICacheService;
import hasoffer.core.user.IDeviceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Date : 2016/5/7
 * Function :
 */
@Component
public class DeviceCacheManager {

    private static final String CACHE_KEY_PRE = "DEVICE_";
    private static final long CACHE_EXPIRE_TIME = TimeUtils.SECONDS_OF_1_HOUR * 2;
    @Resource
    ICacheService<UrmDevice> cacheService;

    @Resource
    IDeviceService deviceService;

    /**
     * 根据设备ID查询设备
     * @param deviceId
     * @return
     */
    public UrmDevice findDeviceById(String deviceId) {

        String key = CACHE_KEY_PRE + deviceId;

        UrmDevice device = cacheService.get(UrmDevice.class, key, CACHE_EXPIRE_TIME);

        if (device == null) {
            device = deviceService.findDevice(deviceId);
        }

        if (device != null) {
            cacheService.add(key, device, CACHE_EXPIRE_TIME);
        }

        return device;
    }

    public void updateDeviceById(UrmDevice di) {

        if (di == null) {
            return;
        }

        String key = CACHE_KEY_PRE + di.getDeviceId();
        cacheService.add(key, di, CACHE_EXPIRE_TIME);

    }
}
