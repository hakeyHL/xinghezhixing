package hasoffer.api.controller;

import hasoffer.api.helper.Httphelper;
import hasoffer.core.third.impl.ThirdServiceImple;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hs on 2016/7/4.
 */
@Controller
@RequestMapping(value = "/third")
public class ThirdPartyController {
    @Resource
    ThirdServiceImple thridPartyService;
    private Logger logger = LoggerFactory.logger(ThirdPartyController.class);

    /**
     * provide API to get deals for Gmobi
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/deals", method = RequestMethod.POST)
    public String config(HttpServletRequest request, HttpServletResponse response) {
        String acceptjson = Httphelper.getJsonFromRequest(request);
        logger.info("accept content is " + acceptjson);
        String result = thridPartyService.getDeals(acceptjson);
        Httphelper.sendJsonMessage(result, response);
        return null;
    }
}
