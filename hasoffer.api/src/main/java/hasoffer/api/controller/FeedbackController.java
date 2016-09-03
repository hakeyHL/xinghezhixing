package hasoffer.api.controller;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Date : 2016/5/17
 * Function :
 */
@Controller
@RequestMapping(value = "/feedback")
public class FeedbackController {
    private Logger logger = LoggerFactory.logger(FeedbackController.class);

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView feedback(HttpServletRequest request) {

        String type = request.getParameter("type");
        String pos = request.getParameter("pos");
        String content = request.getParameter("content");

        logger.debug(type + "\t" + pos);
        logger.debug(content);

        ModelAndView mav = new ModelAndView();

        mav.addObject("result", "OK");

        return mav;
    }

}
