package hasoffer.admin.controller;

import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created on 2016/5/4.
 */
@RequestMapping(value = "hijack")
public class HijackController {

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView buy(HttpServletRequest request,
                            @RequestParam(required = false) String website,
                            @RequestParam(defaultValue = "") String startTime,
                            @RequestParam(defaultValue = "") String endTime,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "50") int size) {
        ModelAndView mav = new ModelAndView("hijack/list");

        final String DATE_PATTERN_FROM_WEB = "MM/dd/yyyy";

        Date startDate = null;
        Date endDate = null;
        if (StringUtils.isEmpty(startTime)) {
            startDate = new Date(TimeUtils.today());
            endDate = new Date();
            startTime = TimeUtils.parse(startDate, DATE_PATTERN_FROM_WEB);
            endTime = startTime;
        } else {
            startDate = TimeUtils.stringToDate(startTime, DATE_PATTERN_FROM_WEB);
            endDate = TimeUtils.addDay(TimeUtils.stringToDate(endTime, DATE_PATTERN_FROM_WEB), 1);
        }

//        PageableResult<UrmDeviceBuyLog> logs = deviceService.findUrmDeviceBuyLog(fromWebsite, toWebsite, startDate, endDate, page, size, "createTime", 0);
//
//
//        List<DeviceBuyLogVo> deviceBuyLogVos = getBuyLogs(logs.getData());
//
//        mav.addObject("requestLogs", deviceBuyLogVos);
//        mav.addObject("page", PageHelper.getPageModel(request, logs));

        mav.addObject("startTime", startTime);
        mav.addObject("endTime", endTime);

        return mav;
    }

}
