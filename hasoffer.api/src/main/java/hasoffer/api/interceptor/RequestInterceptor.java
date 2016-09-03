package hasoffer.api.interceptor;

import com.google.gson.Gson;
import hasoffer.api.controller.vo.DeviceEventVo;
import hasoffer.api.controller.vo.DeviceInfoVo;
import hasoffer.api.controller.vo.DeviceRequestVo;
import hasoffer.api.controller.vo.ResultVo;
import hasoffer.api.worker.DeviceRequestQueue;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.utils.DeviceUtils;
import hasoffer.core.persistence.po.urm.UrmUser;
import hasoffer.core.system.IAppService;
import hasoffer.core.user.IDeviceService;
import hasoffer.webcommon.context.Context;
import hasoffer.webcommon.context.StaticContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);
    @Resource
    IDeviceService deviceService;
    @Resource
    IAppService appService;

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Gson gson = new Gson();

        String deviceInfoStr = Context.currentContext().getHeader("deviceinfo");
        String userToken = Context.currentContext().getHeader("usertoken");
        String requestUri = httpServletRequest.getRequestURI();
        String queryStr = httpServletRequest.getQueryString();
        try {
            DeviceInfoVo deviceInfoVo = gson.fromJson(deviceInfoStr, DeviceInfoVo.class);

            if (deviceInfoVo.getMarketChannel() == null) {
                deviceInfoVo.setMarketChannel(MarketChannel.OFFICIAL);
            }

            DeviceRequestVo requestVo = new DeviceRequestVo(deviceInfoVo, requestUri, queryStr);

            recordClientEvent(requestVo, httpServletRequest);

            if (!requestUri.equals("/app/config")) {
                DeviceRequestQueue.addLog(requestVo);
            }
//            System.out.println("RequestInterceptor.time1=" + System.currentTimeMillis());
//            addLogToDB(requestVo.getBo());
//            System.out.println("RequestInterceptor.time2=" + System.currentTimeMillis());
            Context.currentContext().set(Context.DEVICE_INFO, deviceInfoVo);

            //比价的设备 setContext
            String deviceId = DeviceUtils.getDeviceId(deviceInfoVo.getDeviceId(), deviceInfoVo.getImeiId(), deviceInfoVo.getSerial());
            Context.currentContext().set(StaticContext.DEVICE_ID, deviceId);

            if (!StringUtils.isEmpty(userToken)) {
                Context.currentContext().set(StaticContext.USER_TOKEN, userToken);

                //todo add cache
                UrmUser user = appService.getUserByUserToken(userToken);
                if (user != null) {
                    Context.currentContext().set(StaticContext.USER_ID, user.getId());
                }
            }

        } catch (Exception e) {
            logger.error(String.format("RequestInterceptor Has Error: %s. request = [%s]. query = [%s] .device=[%s]",
                    e.getMessage(), requestUri, queryStr, deviceInfoStr));
            // + e.getMessage() + " , device = [" + deviceInfoStr + "]"
            return false;
        }

        return true;
    }

//    private void addLogToDB(DeviceRequestBo requestVo) {
//        ActorRef defaultActorRef = AkkaActorRef.getDefaultActorRef();
//        AkkaJobConfigMessage message = new AkkaJobConfigMessage(DeviceLogActor.class, 1);
//        defaultActorRef.tell(message, ActorRef.noSender());
//        AkkaJobMessage akkaJobMessage = new AkkaJobMessage(DeviceLogActor.class, requestVo);
//        defaultActorRef.tell(akkaJobMessage, ActorRef.noSender());
//    }

    private void recordClientEvent(DeviceRequestVo requestVo, HttpServletRequest request) {
        if (requestVo.getRequestUri().equals("/app/log")) {
            String event = request.getParameter("event");
            String info = request.getParameter("info");

            requestVo.setDeviceEvent(new DeviceEventVo(event, info));
        }
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
        //去除不必要的返回
        if (modelAndView != null && modelAndView.getModel().containsKey("searchCriteria")) {
            modelAndView.getModel().remove("searchCriteria");
        }
        if (modelAndView != null) {
            UrmUser urmUser = appService.getUserByUserToken((String) Context.currentContext().get(StaticContext.USER_TOKEN));
            if (urmUser == null) {
                modelAndView.addObject("result", new ResultVo("10010", "login expired"));
//            new StringBuilder().append("{\n" +
//                    "    \"errorCode\": \"10010\",\n" +
//                    "    \"msg\": \"login expired \"\n" +
//                    "}"));
            } else {
                modelAndView.addObject("result", new ResultVo("00000", "ok"));
//            modelAndView.addObject("result", new StringBuilder().append("{\n" +
//                    "    \"errorCode\": \"00000\",\n" +
//                    "    \"msg\": \"ok \"\n" +
//                    "}"));
            }
        }
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex)
            throws Exception {

    }
}