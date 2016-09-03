package hasoffer.api.controller;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.app.AdvertiseService;
import hasoffer.core.persistence.po.admin.Adt;
import hasoffer.fetch.helper.WebsiteHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hs on 2016/7/25.
 * 专用于广告相关的Controller
 */
@Controller
@RequestMapping("ad")
public class AppAdController {
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

    Logger logger = LoggerFactory.getLogger(AppAdController.class);
    @Resource
    AdvertiseService advertiseService;

    public static void main(String[] args) {
        try {
            Sender sender = new Sender("AIzaSyCZrHjOkZ57j3Dvq_TpvYW8Mt38Ej1dzQA");
            String userMessage = "{\n" +
                    "    \"display\": {\n" +
                    "        \"outTitle\": \" Sony 16GB USB Pen Drive  , 25% OFF at Rs.270.Hurry up! \",\n" +
                    "        \"title\": \"Sony 16GB USB Pen Drive \",\n" +
                    "        \"content\": \"25% OFF at Rs.270.Hurry up!\n \"\n" +
                    "    },\n" +
                    "    \"click\": {\n" +
                    "        \"type\": \"DEAL\",\n" +
                    "        \"url\": \"99000238\",\n" +
                    "        \"packageName\": \"com.flipkart.android\"\n" +
                    "    }\n" +
                    "}";
            Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true).addData("message", userMessage).build();
//            Result result = sender.send(message, "e1lvEUbO4wc:APA91bHBsxTiXXSo3SQdvPB7tTqWrGIbez2H3yyqr1y6gTfohYAB98HjYICFK35c4_UwScQwI0J7m634r_Qzdo1bRtvHf71ZjcUHytDH4VPmwCfdlEu62ErQMfX4fYXcWlxUNQILqbkd", 2);
            MulticastResult result = sender.send(message, Arrays.asList("cWkpvtsRBd4:APA91bFxIK8S3M_ZRzkEBrm6fx2aSk183GdG4nF5U9CkuBpxp4mlyoKYISI1uqbs-H8r-_oHiLdrrnYVgcviUf4T-J9G4HxtLnWbD2whRAaqBoos-I8jp48Ye6z0cJ0rXk6MAARZMVaH", "e1lvEUbO4wc:APA91bHBsxTiXXSo3SQdvPB7tTqWrGIbez2H3yyqr1y6gTfohYAB98HjYICFK35c4_UwScQwI0J7m634r_Qzdo1bRtvHf71ZjcUHytDH4VPmwCfdlEu62ErQMfX4fYXcWlxUNQILqbkd"), 1);
            System.out.println("123");
//            Result result = sender.sendNoRetry(message, "cWkpvtsRBd4:APA91bFxIK8S3M_ZRzkEBrm6fx2aSk183GdG4nF5U9CkuBpxp4mlyoKYISI1uqbs-H8r-_oHiLdrrnYVgcviUf4T-J9G4HxtLnWbD2whRAaqBoos-I8jp48Ye6z0cJ0rXk6MAARZMVaH");
//            String errorCodeName = result.getErrorCodeName();
//            System.out.println(errorCodeName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" get exception ");
            System.out.println(e.getMessage());
        }
    }

    /**
     * 根据商品id获取category
     * 根据category匹配广告
     *
     * @return
     */
    @RequestMapping("product")
    public ModelAndView getAdsByProductId(@RequestParam(defaultValue = "0") Long productId) {
        ModelAndView modelAndView = new ModelAndView();
        logger.info(" get advertisement ");
        Map map = new HashMap<>();
        modelAndView.addObject("errorCode", "00000");
        modelAndView.addObject("msg", "ok");
        List<Adt> adt = advertiseService.getAdByCategory();
        if (adt != null && adt.size() > 0) {
            System.out.println(" get  index 0");
            Adt adt1 = adt.get(0);
            if (adt1 != null) {
                if (!StringUtils.isEmpty(adt1.getAderName())) {
                    adt1.setPackageName(packageMap.get(Website.valueOf(adt1.getAderName())));
                }
                if (!StringUtils.isEmpty(adt1.getAdLink())) {
                    adt1.setAdLink(WebsiteHelper.getAdtUrlByWebSite(Website.valueOf(adt1.getAderName()), adt1.getAdLink()));
                }
            }
            map.put("ads", Arrays.asList(adt1));
            modelAndView.addObject("data", map);
        }
        return modelAndView;
    }

    @RequestMapping("tPush")
    public ModelAndView tt() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Sender sender = new Sender("key=AIzaSyCZrHjOkZ57j3Dvq_TpvYW8Mt38Ej1dzQA");
        String userMessage = "{\"errorCode\":\"00000\"}";
        Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true).addData("m", userMessage).build();
//        String regId="e1lvEUbO4wc:APA91bHBsxTiXXSo3SQdvPB7tTqWrGIbez2H3yyqr1y6gTfohYAB98HjYICFK35c4_UwScQwI0J7m634r_Qzdo1bRtvHf71ZjcUHytDH4VPmwCfdlEu62ErQMfX4fYXcWlxUNQILqbkd";
//        Result result  = sender.send(message, regId, 1);
        Result result = sender.send(message, "e1lvEUbO4wc:APA91bHBsxTiXXSo3SQdvPB7tTqWrGIbez2H3yyqr1y6gTfohYAB98HjYICFK35c4_UwScQwI0J7m634r_Qzdo1bRtvHf71ZjcUHytDH4VPmwCfdlEu62ErQMfX4fYXcWlxUNQILqbkd", 2);
        String errorCodeName = result.getErrorCodeName();
        System.out.println(errorCodeName);
        return modelAndView;
    }
}
