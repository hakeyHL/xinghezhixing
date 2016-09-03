package hasoffer.admin.controller;

import com.alibaba.fastjson.JSONObject;
import hasoffer.admin.controller.vo.PushVo;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.Website;
import hasoffer.core.bo.push.*;
import hasoffer.core.push.IPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2016/6/21 12:47
 */

@Controller
@RequestMapping(value = "/push")
public class PushController {
    static Map<Website, String> packageMap = new HashMap<Website, String>();

    static {
        packageMap.put(Website.SNAPDEAL, "com.snapdeal.main");
        packageMap.put(Website.FLIPKART, "com.flipkart.android");
        packageMap.put(Website.AMAZON, "in.amazon.mShop.android.shopping");
        packageMap.put(Website.PAYTM, "net.one97.paytm");
        packageMap.put(Website.EBAY, "com.ebay.mobile");
        packageMap.put(Website.SHOPCLUES, "com.shopclues");
        packageMap.put(Website.INFIBEAM, "com.infibeam.infibeamapp");
        packageMap.put(Website.MYNTRA, "com.myntra.android");
        packageMap.put(Website.JABONG, "com.jabong.android");
        packageMap.put(Website.VOONIK, "com.voonik.android");
    }

    @Resource
    IPushService pushService;
    private Logger logger = LoggerFactory.getLogger(PushController.class);

    @RequestMapping(value = "/pushIndex")
    public ModelAndView PushIndex() {
        ModelAndView mav = new ModelAndView("push/push");
        List<MarketChannel> channles = pushService.getAllMarketChannels();
        List<Website> websites = new ArrayList<>();
        Class classzz = Website.class;
        for (Object o : classzz.getEnumConstants()) {
            websites.add((Website) o);
        }
        //获得所有APP版本
        List<String> versions = pushService.getAllAppVersions();
        mav.addObject("channels", channles);
        mav.addObject("websites", websites);
        mav.addObject("versions", versions);
        return mav;
    }

    @RequestMapping(value = "/pushMessage")
    public ModelAndView PushMessage(PushVo pushVol) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("success", true);
        //1.推送类型
        switch (pushVol.getPushType()) {
            case "single":
                //2.1单
                AppPushMessage message = new AppPushMessage(
                        new AppMsgDisplay(pushVol.getOutline(), pushVol.getTitle(), pushVol.getContent()),
                        new AppMsgClick(AppMsgClickType.valueOf(pushVol.getMessageType()), pushVol.getValue(), packageMap.get(pushVol.getWebsite()))
                );
                mv.addObject("pushCount", 1);
                mv.addObject("pushType", "single");
                AppPushBo pushBo = new AppPushBo("5x1", "15:10", message);
//                //3.渠道
//                for (String channel : pushVol.getChannel()) {
//                    //4.版本列表
//                    for (String version : pushVol.getVersion()) {
//
//                        for (String website : pushVol.getWebsite()) {
//
//                        }
//                        //5.app列表
//                    }
//                }
                String pushResult = pushService.push(pushVol.getGcmToken(), pushBo);
                if (pushResult != null) {
                    JSONObject JsonObject = JSONObject.parseObject(pushResult);
                    if (JsonObject.getInteger("success") == 1) {
                        mv.addObject("successCount", 1);
                    } else {
                        mv.addObject("successCount", 0);
                        mv.addObject("failedCount", 1);
                    }
//                    JSONArray results = JsonObject.getJSONArray("results");
//                    JSONObject jsonObject = results.getJSONObject(0);
//                    String error = jsonObject.getString("error");
                } else {
                    mv.addObject("success", true);
                    mv.addObject("msg", "网络请求失败!");
                }
                break;
            case "group":
                //2.2群
                break;
            default:
                break;
        }
        return mv;
    }
}
