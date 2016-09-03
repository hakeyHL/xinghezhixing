package hasoffer.admin.controller;

import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.admin.IOrderStatsAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping(value = "/orderStats")
public class OrderController {
    private static Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Resource
    IOrderStatsAnalysisService orderStatsAnalysisService;

    @RequestMapping(value = "/updateOrderReport", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView listOrderStats(HttpServletRequest request, @RequestParam(defaultValue = "") String webSite) {


        try {

            Date todayTime = new Date();
            //头15天
            Date day15AgoTime = TimeUtils.addDay(todayTime, -15);
            //头三天
            Date day3AgoTime =  TimeUtils.addDay(todayTime, -3);
            //头两天
            Date day2AgoTime =  TimeUtils.addDay(todayTime, -2);
            //头一天
            Date day1AgoTime =  TimeUtils.addDay(todayTime, -1);
            //当天
            if (webSite == null || "".equals(webSite) || "ALL".equals(webSite) || Website.SNAPDEAL.toString().equals(webSite.toUpperCase())) {
                orderStatsAnalysisService.updateOrder(Website.SNAPDEAL.toString(), day15AgoTime, day15AgoTime);
                orderStatsAnalysisService.updateOrder(Website.SNAPDEAL.toString(), day3AgoTime, day3AgoTime);
                orderStatsAnalysisService.updateOrder(Website.SNAPDEAL.toString(), day2AgoTime, day2AgoTime);
                orderStatsAnalysisService.updateOrder(Website.SNAPDEAL.toString(), day1AgoTime, day1AgoTime);
                orderStatsAnalysisService.updateOrder(Website.SNAPDEAL.toString(), todayTime, todayTime);
            }
            if (webSite == null || "".equals(webSite) || "ALL".equals(webSite) || Website.FLIPKART.toString().equals(webSite.toUpperCase())) {
                orderStatsAnalysisService.updateOrder(Website.FLIPKART.toString(), day15AgoTime, day15AgoTime);
                orderStatsAnalysisService.updateOrder(Website.FLIPKART.toString(), day3AgoTime, day3AgoTime);
                orderStatsAnalysisService.updateOrder(Website.FLIPKART.toString(), day2AgoTime, day2AgoTime);
                orderStatsAnalysisService.updateOrder(Website.FLIPKART.toString(), day1AgoTime, day1AgoTime);
                orderStatsAnalysisService.updateOrder(Website.FLIPKART.toString(), todayTime, todayTime);
            }
        } catch (Exception e) {
            logger.debug("reportOrderStatistic:任务失败,   DATE:" + new Date() + ":具体如下");
            logger.debug(e.toString());
        }
        return new ModelAndView("showstat/listOrderReport");
    }

}
