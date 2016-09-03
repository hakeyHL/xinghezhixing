package hasoffer.admin.controller;

import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.mongo.PtmCmpSkuLog;
import hasoffer.core.product.ICmpSkuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Date : 2016/1/22
 * Function :
 */
@Controller
@RequestMapping(value = "/stat")
public class StatController {

    @Resource
    ICmpSkuService cmpSkuService;
    @Resource
    IMongoDbManager mdm;

    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public ModelAndView skupriceupdateresult() {
        ModelAndView mav = new ModelAndView("system/monitor");

        mav.addObject("results", cmpSkuService.listUpdateResults());

        return mav;
    }


    @RequestMapping(value = "/cmpskupriceupdate/restat", method = RequestMethod.GET)
    public ModelAndView restat(HttpServletRequest request,
                               @RequestParam(defaultValue = "false") boolean all,
                               @RequestParam(defaultValue = "") String day) {

        if (all) {
            String symd = day;
            if (StringUtils.isEmpty(symd)) {
                PtmCmpSkuLog ptmCmpSkuLog = mdm.queryOne(PtmCmpSkuLog.class);
                Date date = ptmCmpSkuLog.getPriceTime();
                symd = TimeUtils.parse(date, TimeUtils.PATTERN_YMD);
            }

            String endYmd = TimeUtils.parse(TimeUtils.today(), TimeUtils.PATTERN_YMD);

            while (symd.compareTo(endYmd) < 0) {
                cmpSkuService.saveOrUpdateSkuPriceUpdateResult(cmpSkuService.countUpdate(symd));
                symd = TimeUtils.parse(TimeUtils.stringToDate(symd, TimeUtils.PATTERN_YMD).getTime() + TimeUtils.MILLISECONDS_OF_1_DAY, "yyyyMMdd");
            }
        } else {
            cmpSkuService.saveOrUpdateSkuPriceUpdateResult(cmpSkuService.countUpdate(day));
        }

        ModelAndView mav = new ModelAndView("system/ok");
        return mav;
    }

}
