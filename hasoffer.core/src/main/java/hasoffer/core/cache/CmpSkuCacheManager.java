package hasoffer.core.cache;

import hasoffer.base.model.Website;
import hasoffer.base.utils.JSONUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.bo.cache.DeviceFlowControllRecord;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmCmpSkuIndex2;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.redis.ICacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Date : 2016/5/7
 * Function :
 */
@Component
public class CmpSkuCacheManager {

    private static final String CACHE_KEY_PRE = "CmpSku_";
    private static final long CACHE_EXPIRE_TIME = TimeUtils.SECONDS_OF_1_HOUR;
    @Resource
    ICacheService<PtmCmpSku> cacheService;
    @Resource
    ICmpSkuService cmpSkuService;

    private Logger logger = LoggerFactory.getLogger(CmpSkuCacheManager.class);

    /**
     * cmpsku 的 缓存
     * todo 每次更新cmpsku的时候要更新缓存
     *
     * @param id
     * @return
     */
    public PtmCmpSku getCmpSkuById(long id) {

        String key = CACHE_KEY_PRE + "_getCmpSkuById_" + id;

        PtmCmpSku cmpSku = cacheService.get(PtmCmpSku.class, key, 0);

        if (cmpSku == null) {
            cmpSku = cmpSkuService.getCmpSkuById(id);
            if (cmpSku != null) {
                cacheService.add(key, cmpSku, CACHE_EXPIRE_TIME);
            }
        }

        return cmpSku;
    }

    /**
     * cmpsku的索引
     * todo 更新sku时要更新
     *
     * @param cliSite
     * @param sourceId
     * @param keyword
     * @return
     */
    public PtmCmpSkuIndex2 getCmpSkuIndex2(String deviceId, Website cliSite, String sourceId, String keyword) {
        boolean isCtrled = isFlowControlled(deviceId, cliSite);
        if (isCtrled) {
            return null;
        }

        String key = CACHE_KEY_PRE + "_getCmpSkuIndex_" + cliSite.name() + "_" + sourceId + "_" + keyword;

        String cmpSkuIndexJson = cacheService.get(key, 0);

        PtmCmpSkuIndex2 cmpSkuIndex = null;
        try {

            if (StringUtils.isEmpty(cmpSkuIndexJson)) {
                cmpSkuIndex = cmpSkuService.getCmpSkuIndex2(cliSite, sourceId, keyword);
                if (cmpSkuIndex == null) {
                    cmpSkuIndex = new PtmCmpSkuIndex2();
                }
                cacheService.add(key, JSONUtil.toJSON(cmpSkuIndex), CACHE_EXPIRE_TIME);
                return cmpSkuIndex;
            } else {
                cmpSkuIndex = JSONUtil.toObject(cmpSkuIndexJson, PtmCmpSkuIndex2.class);
                return cmpSkuIndex;
            }

        } catch (Exception e) {
            logger.debug(e.getMessage());
            cmpSkuIndex = null;
        }

        return cmpSkuIndex;
    }

    /**
     * @param deviceId
     * @param cliSite
     * @return
     */
    public boolean isFlowControlled(String deviceId, Website cliSite) {

        String key = CACHE_KEY_PRE + "flow_controlled_" + deviceId + "_" + cliSite.name();

        String deviceFlowControllRecord = cacheService.get(key, 0);

        if (StringUtils.isEmpty(deviceFlowControllRecord)) {
            return false;
        } else {
            return true;
        }
    }

    public void recordFlowControll(String deviceId, Website cliSite) {

        String key = CACHE_KEY_PRE + "flow_controlled_" + deviceId + "_" + cliSite.name();

        String deviceFlowControllRecord = cacheService.get(key, 0);

        if (StringUtils.isEmpty(deviceFlowControllRecord)) {
            cacheService.add(key, JSONUtil.toJSON(new DeviceFlowControllRecord(deviceId, cliSite.name(), true)), TimeUtils.SECONDS_OF_1_DAY);
        }
    }
}
