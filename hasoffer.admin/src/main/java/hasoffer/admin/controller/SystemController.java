package hasoffer.admin.controller;

import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.bo.product.SkuBo;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.enums.AdminType;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.sys.SysAdmin;
import hasoffer.core.product.IDataFixService;
import hasoffer.core.product.IProductService;
import hasoffer.core.system.IAdminService;
import hasoffer.core.user.IDeviceService;
import hasoffer.webcommon.context.Context;
import hasoffer.webcommon.context.StaticContext;
import jodd.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 2015/12/24.
 */
@Controller
@RequestMapping()
public class SystemController {

    private final static String Q_CMP_SKU =
            "SELECT t FROM PtmCmpSku t WHERE t.website IS NULL";
    private static final String Q_CMPSKU_THEITDEPOT_WEBSITE =
            "SELECT t FROM PtmCmpSku t WHERE website = 'THEITDEPOT' ";

    @Resource
    IAdminService adminService;
    @Resource
    IProductService productService;
    @Resource
    IDeviceService deviceService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    IDataFixService dataFixService;

    private Logger logger = LoggerFactory.getLogger(SystemController.class);

    @RequestMapping(value = "/fix/cmpskuurls", method = RequestMethod.GET)
    public String cmpskuurls() {
        List<PtmCmpSku> cmpSkus = dataFixService.findErrorCmpSkus();

        for (PtmCmpSku cmpSku : cmpSkus) {
            dataFixService.fixCmpskuUrl(cmpSku);
        }

        return "system/ok";
    }

    @RequestMapping(value = "/fix/skuerrorinprice", method = RequestMethod.GET)
    public String skuerrorinprice() throws IOException {

        List<Long> errorSkuList = new ArrayList<Long>();

        long maxProductId = dataFixService.getMaxProductId();

        File file = new File("C:/Users/wing/Desktop/error.txt");

        String head = "pid\ttitle\tskuid-price\n";

        FileUtil.appendString(file, head);

        for (int i = 1; i <= maxProductId; i++) {
            if (i % 400 == 0) {
                logger.debug("productId = " + i);
                logger.debug("errorIdList size = " + errorSkuList.size());
            }

            PtmProduct product = productService.getProduct(i);

            List<SkuBo> skuBoList = dataFixService.getErrorSkuInPriceByProductId(i);

            if (skuBoList == null) {
                continue;
            }

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(i + "\t" + product.getTitle() + "\t");
            for (SkuBo skuBo : skuBoList) {
                stringBuilder.append(skuBo.getId() + "\t" + skuBo.getPrice() + "\t");
            }

            stringBuilder.append("\n");

            FileUtil.appendString(file, stringBuilder.toString());

            errorSkuList.add(Long.valueOf(i));
        }
        return "system/ok";
    }

    @RequestMapping(value = "/fix/theitdepotUrl", method = RequestMethod.GET)
    public String theitdepotUrl() {

        PageableResult<PtmCmpSku> pageableResult = dbm.queryPage(Q_CMPSKU_THEITDEPOT_WEBSITE, 1, Integer.MAX_VALUE);

        List<PtmCmpSku> skus = pageableResult.getData();

        for (PtmCmpSku ptmCmpSku : skus) {
            dataFixService.fixtTheitdepotUrl(ptmCmpSku);
        }

        return "system/ok";
    }

    @RequestMapping(value = "/fix/distinctSkuWebsite", method = RequestMethod.GET)
    public String distinctSkuWebsite() {

        long maxProductId = dataFixService.getMaxProductId();

        for (int i = 1; i <= maxProductId; i++) {
            if (i % 400 == 0) {
                logger.debug(i + " distinctSkuWebsite ");
            }
            dataFixService.fixCmpskuWebsiteRepeat(i);
        }

        return "system/ok";
    }

    @RequestMapping(value = "/fix/cmpskuWebsite", method = RequestMethod.GET)
    public String cmpskuWebsite() {
        List<PtmCmpSku> cmpSkus = dataFixService.findErrorCmpSkus();

        for (PtmCmpSku cmpSku : cmpSkus) {
            dataFixService.fixCmpskuWebsite(cmpSku);
        }

        return "system/ok";
    }

    @RequestMapping(value = "/fix/restatvisit", method = RequestMethod.GET)
    public String reStatVisit() {
        String PATTERN_DATE = "yyyyMMdd";
        // 更新所有的设备信息
        String symd = "20160223";//20160118 - 20160223

        Date date = TimeUtils.stringToDate(symd, PATTERN_DATE);

        String endYmd = TimeUtils.parse(TimeUtils.today(), PATTERN_DATE);

        while (symd.compareTo(endYmd) <= 0) {
            deviceService.saveOrUpdate(deviceService.statDayVisit(symd));
            symd = TimeUtils.parse(TimeUtils.stringToDate(symd, PATTERN_DATE).getTime() + TimeUtils.MILLISECONDS_OF_1_DAY, PATTERN_DATE);
        }

        return "system/ok";
    }

    @RequestMapping(value = "/fix/website", method = RequestMethod.GET)
    public String fixwebsite() {

        Runnable re = new Runnable() {
            @Override
            public void run() {
                int PAGE_SIZE = 1000, pageNum = 1;

                PageableResult<PtmCmpSku> pCmpSkus = dbm.queryPage(Q_CMP_SKU, pageNum, PAGE_SIZE);

                int pageCount = (int) pCmpSkus.getTotalPage();
                List<PtmCmpSku> cmpSkus;
                while (pageNum <= pageCount) {
                    if (pageNum == 1) {
                        cmpSkus = pCmpSkus.getData();
                    } else {
                        cmpSkus = dbm.query(Q_CMP_SKU, pageNum, PAGE_SIZE);
                    }

                    logger.debug(String.format("fix website for page : %d / %d.", pageNum, pageCount));

                    if (ArrayUtils.isNullOrEmpty(cmpSkus)) {
                        break;
                    }

                    for (PtmCmpSku cmpSku : cmpSkus) {
                        String url = cmpSku.getUrl();

                        try {
                            URI uri = new URI(url);
                            String[] qs = uri.getQuery().split("&");

                            if (qs != null) {
                                for (String q : qs) {
                                    if (q.startsWith("ckmrdr=")) {
                                        url = q.substring(q.indexOf("=") + 1);
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }
                        productService.updateSku(cmpSku.getId(), url);
                    }

                    pageNum++;
                }
                logger.debug("finish fix.");
            }
        };

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(re);

        return "system/ok";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
        return "index";
    }

    @RequestMapping(value = "/sys/list", method = RequestMethod.GET)
    public String recreatesolrindex(HttpServletRequest request) {
        return "system/index";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public ModelAndView error(HttpServletRequest request,
                              @RequestParam(defaultValue = "") String _errorCode,
                              @RequestParam(defaultValue = "") String _message) {

        ModelAndView mav = new ModelAndView("system/error");

        mav.addObject("_errorCode", _errorCode);
        mav.addObject("_message", _message);

        return mav;
    }

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

            if (AdminType.TEMP_ADMIN.equals(admin.getType())) {
                mav.setViewName("redirect:/s2/list");
            } else {
                mav.setViewName("redirect:/index");
            }
            return mav;
        } catch (LoginException e) {
            mav.setViewName("system/login");
            mav.addObject("error", e.getMessage());
            return mav;
        }
    }

    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    public String regist(HttpServletRequest request) {
        return "system/pregist";
    }

    @RequestMapping(value = "/pregist", method = RequestMethod.GET)
    public String pregist(HttpServletRequest request) {
        return "system/regist";
    }

    @RequestMapping(value = "/dat/loadwebsites", method = RequestMethod.GET)
    public String loadwebsites(HttpServletRequest request) {
        adminService.createWebsites();
        return "system/ok";
    }
}
