package hasoffer.api.controller;

import hasoffer.base.utils.JSONUtil;
import hasoffer.base.utils.UrlUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.mongo.UrmFbLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Date : 2016/3/29
 * Function :
 */

@Controller
@RequestMapping(value = "/appexc")
public class AppxController {

    @Resource
    IMongoDbManager mdm;
    private Logger logger = LoggerFactory.getLogger(AppxController.class);

    //www.allbuy.com/api/callback/adjust/intall
    // ?idfa={idfa}&android_id={android_id}&deviceName={device_name}&osName={os_name}&osVersion={os_version}&country={country}&language={language}&timezone={timezone}&userAgent={user_agent}&deviceIp={ip_address}&appName={app_name}
    // &appVersion={app_version}&trackerId={tracker}&trackerName={tracker_name}&eventTime={created_at}
    // &campaign_name={campaign_name}&adgroup_name={adgroup_name}
    // &fb_campaign_group_name={fb_campaign_group_name}&fb_campaign_group_id={fb_campaign_group_id}&fb_campaign_name={fb_campaign_name}&fb_campaign_id={fb_campaign_id}&fb_adgroup_name={fb_adgroup_name}&fb_adgroup_id={fb_adgroup_id}

    @RequestMapping(value = "/callback/adjust", method = RequestMethod.GET)
    public void callback_adjust(HttpServletRequest request,
                                @RequestParam(defaultValue = "") String idfa,
                                @RequestParam(defaultValue = "") String android_id,
                                @RequestParam(defaultValue = "") String deviceName,
                                @RequestParam(defaultValue = "") String osName,
                                @RequestParam(defaultValue = "") String osVersion,
                                @RequestParam(defaultValue = "") String country,
                                @RequestParam(defaultValue = "") String language,
                                @RequestParam(defaultValue = "") String timezone,
                                @RequestParam(defaultValue = "") String userAgent,
                                @RequestParam(defaultValue = "") String deviceIp,
                                @RequestParam(defaultValue = "") String appName,
                                @RequestParam(defaultValue = "") String appVersion,
                                @RequestParam(defaultValue = "") String trackerId,
                                @RequestParam(defaultValue = "") String trackerName,
                                @RequestParam(defaultValue = "") String eventTime,
                                @RequestParam(defaultValue = "") String campaign_name,
                                @RequestParam(defaultValue = "") String adgroup_name,
                                @RequestParam(defaultValue = "") String fb_campaign_group_name,
                                @RequestParam(defaultValue = "") String fb_campaign_group_id,
                                @RequestParam(defaultValue = "") String fb_campaign_name,
                                @RequestParam(defaultValue = "") String fb_campaign_id,
                                @RequestParam(defaultValue = "") String fb_adgroup_name,
                                @RequestParam(defaultValue = "") String fb_adgroup_id) {

        String url = request.getRequestURL().toString();
        String queryJson = JSONUtil.toJSON(UrlUtils.getParams(url));

        UrmFbLog fbLog = new UrmFbLog(queryJson, idfa, android_id, deviceName,
                osName, osVersion, country, language, timezone,
                userAgent, deviceIp, appName, appVersion,
                trackerId, trackerName, eventTime,
                campaign_name, adgroup_name,
                fb_campaign_group_name,
                fb_campaign_group_id, fb_campaign_name, fb_campaign_id,
                fb_adgroup_name, fb_adgroup_id);

//        mdm.save(fbLog);

//        logger.debug("idfa \t" + idfa);
//        logger.debug("android_id \t" + android_id);
//        logger.debug("deviceName \t" + deviceName);
//        logger.debug("osName \t" + osName);
//        logger.debug("osVersion \t" + osVersion);
//        logger.debug("country \t" + country);
//        logger.debug("language \t" + language);
//        logger.debug("timezone \t" + timezone);
//        logger.debug("userAgent \t" + userAgent);
//        logger.debug("deviceIp \t" + deviceIp);
//        logger.debug("appName \t" + appName);
//        logger.debug("appVersion \t" + appVersion);
//        logger.debug("trackerId \t" + trackerId);
//        logger.debug("trackerName \t" + trackerName);
//        logger.debug("eventTime \t" + eventTime);
//        logger.debug("campaign_name \t" + campaign_name);
//        logger.debug("adgroup_name \t" + adgroup_name);
//        logger.debug("fb_campaign_group_name \t" + fb_campaign_group_name);
//        logger.debug("fb_campaign_group_id \t" + fb_campaign_group_id);
//        logger.debug("fb_campaign_name \t" + fb_campaign_name);
//        logger.debug("fb_campaign_id \t" + fb_campaign_id);
//        logger.debug("fb_adgroup_name \t" + fb_adgroup_name);
//        logger.debug("fb_adgroup_id \t" + fb_adgroup_id);

        return;
    }
}

