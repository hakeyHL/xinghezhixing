package hasoffer.job.common.admin.controller;

import hasoffer.job.quartz.bean.QuartzJobInfo;
import hasoffer.job.quartz.spring.SchedulerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Controller
@RequestMapping("/layout")
public class ShowHomeController {

    private static final String GROUP = "DEFAULT";
    @Autowired
    private SchedulerServiceImpl schedulerService;

    @RequestMapping("/showHome")
    public ModelAndView showHome() {
        List<QuartzJobInfo> infos = schedulerService.getQrtzTriggers();
        ModelAndView mv = new ModelAndView("layout/mainFrame");
        mv.addObject("infos", infos);
        return mv;
    }

    @RequestMapping("/showIndex")
    public ModelAndView showMain() {
        return new ModelAndView("layout/index");
    }


    @RequestMapping("/pause")
    public String pauseTrigger(@RequestParam(defaultValue = "") String triggerName) {
        schedulerService.pauseTrigger(triggerName, GROUP);
        return "redirect:/layout/showHome";
    }

    @RequestMapping("/resumeTrigger")
    public String resumeTrigger(@RequestParam(defaultValue = "") String triggerName) {
        schedulerService.resumeTrigger(triggerName, GROUP);
        return "redirect:/layout/showHome";
    }

    //@RequestMapping("/shutDownNow")
    //public String breakTrigger(@RequestParam(defaultValue = "") String triggerName) {
    //    if(triggerName.contains("fetchTrigger")){
    //        webSiteFetchService.shutDown();
    //    }
    //    return "redirect:/layout/showHome";
    //}
    //
    //
    @RequestMapping("/runNow")
    public String runNow(@RequestParam(defaultValue = "") String triggerName, @RequestParam(defaultValue = "") String targetBizDate, @RequestParam(defaultValue = "") String officeId) {
        Map<String, Object> data = new HashMap<String, Object>();
        if (targetBizDate != null && targetBizDate.trim().length() > 0) {
            data.put("targetBizDate", targetBizDate);
        }
        if (officeId != null && officeId.trim().length() > 0) {
            data.put("officeId", officeId);
        }

        try {
            schedulerService.runNow(triggerName, GROUP, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/layout/showHome";
    }

}
