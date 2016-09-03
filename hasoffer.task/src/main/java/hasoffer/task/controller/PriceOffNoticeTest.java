package hasoffer.task.controller;

import hasoffer.core.user.IPriceOffNoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created on 2016/8/31.
 */
@Controller
@RequestMapping(value = "/PriceOffNoticeTest")
public class PriceOffNoticeTest {

    @Resource
    IPriceOffNoticeService priceOffNoticeService;

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    @ResponseBody
    public String start() {

        priceOffNoticeService.priceOffCheck(979);

        return "ok";
    }

}
