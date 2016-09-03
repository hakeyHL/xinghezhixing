package hasoffer.job.common.admin.controller;

import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.po.sys.SysAdmin;
import hasoffer.core.system.IAdminService;
import hasoffer.webcommon.context.Context;
import hasoffer.webcommon.context.StaticContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    IAdminService adminService;
    @RequestMapping(value = "/prelogin", method = RequestMethod.GET)
    public String prelogin(HttpServletRequest request) {
        logger.debug("prelogin");
        return "system/login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SysAdmin admin = (SysAdmin) Context.currentContext().get(StaticContext.USER);

        if (admin != null) {
            adminService.logout(admin);
        }

        return "system/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();

        String uname = request.getParameter("name");
        String password = request.getParameter("password");

        logger.debug(uname + "\t" + password);

        if (StringUtils.isEmpty(uname) || StringUtils.isEmpty(password)) {
            mav.setViewName("system/login");
            mav.addObject("error", "用户名/密码不能为空");
            return mav;
        }

        try {
            SysAdmin admin = adminService.login(uname, password);

            response.addCookie(new Cookie(StaticContext.USER_KEY, admin.getUkey()));
            logger.debug(uname + " login success.");

            //if (AdminType.TEMP_ADMIN.equals(admin.getType())) {
            //    mav.setViewName("redirect:/s2/list");
            //} else {
            //    mav.setViewName("redirect:/index");
            //}
            mav.setViewName("redirect:/layout/showIndex");
            return mav;
        } catch (LoginException e) {
            mav.setViewName("system/login");
            mav.addObject("error", e.getMessage());
            return mav;
        }
    }
}
