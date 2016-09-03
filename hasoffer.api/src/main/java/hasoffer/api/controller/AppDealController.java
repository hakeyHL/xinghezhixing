package hasoffer.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import hasoffer.api.controller.vo.DealVo;
import hasoffer.api.controller.vo.DeviceInfoVo;
import hasoffer.api.helper.Httphelper;
import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.core.admin.IDealService;
import hasoffer.core.persistence.po.app.AppDeal;
import hasoffer.core.product.solr.DealIndexServiceImpl;
import hasoffer.core.product.solr.DealModel;
import hasoffer.core.system.IAppService;
import hasoffer.core.utils.JsonHelper;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.webcommon.context.Context;
import hasoffer.webcommon.context.StaticContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by hs on 2016/7/25.
 * 专用于Deal的Controller
 */
@Controller
@RequestMapping("deal")
public class AppDealController {
    Logger logger = LoggerFactory.getLogger(AppDealController.class);
    @Resource
    IDealService dealService;
    @Resource
    IAppService appService;
    @Resource
    DealIndexServiceImpl indexService;

    /**
     * 获取商品相关deal列表
     *
     * @return modelAndView
     */
    @RequestMapping("product")
    public ModelAndView getDealsByProductTitle(@RequestParam(defaultValue = "") String title,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "20") int pageSize,
                                               HttpServletResponse response) {
        //TODO 从Solr搜索Deal列表
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorCode", "00000");
        jsonObject.put("msg", "ok");
        String deviceId = (String) Context.currentContext().get(StaticContext.DEVICE_ID);
        DeviceInfoVo deviceInfo = (DeviceInfoVo) Context.currentContext().get(Context.DEVICE_INFO);
        List<DealVo> deals = new ArrayList<DealVo>();
        PropertyFilter propertyFilter = JsonHelper.filterProperty(new String[]{"exp", "extra", "link", "priceDescription", "image"});
        //先展示与浏览商品同类的deal
        List<DealModel> dealModels = indexService.simpleSearch(title, page, pageSize);
        System.out.println("search from solr dealModels  :" + dealModels.size());
        if (dealModels != null && dealModels.size() > 0) {
            for (DealModel dealModel : dealModels) {
                if (dealModel.getExpireTime().compareTo(new Date()) != 1 && dealModel.isDisplay()) {
                    DealVo dealVo = new DealVo();
                    dealVo.setLogoUrl(dealModel.getWebsite() == null ? "" : WebsiteHelper.getBiggerLogoUrl(Website.valueOf(dealModel.getWebsite())));
                    dealVo.setTitle(dealModel.getTitle());
                    dealVo.setWebsite(Website.valueOf(dealModel.getWebsite()));
                    dealVo.setId(dealModel.getId());
                    dealVo.setDiscount(dealModel.getDiscount());
                    dealVo.setDeepLink(dealModel.getLinkUrl() == null ? "" : WebsiteHelper.getDealUrlWithAff(Website.valueOf(dealModel.getWebsite()), dealModel.getLinkUrl(), new String[]{deviceInfo.getMarketChannel().name(), deviceId}));
                    deals.add(dealVo);
                }
            }
            System.out.println("from solr get   :" + deals.size());
        }
        //再展示手机类deal id或parentid 为 5 level小于等于3
        PageableResult pageableResult = appService.getDeals(page + 0l, pageSize + 0l);
        if (pageableResult != null && pageableResult.getData() != null && pageableResult.getData().size() > 0) {
            List<AppDeal> list = pageableResult.getData();
            System.out.println("search from mysql get   :" + list.size());
            List<DealVo> mobileDeals = new ArrayList<DealVo>();
            Iterator<AppDeal> dealIterator = list.iterator();
            while (dealIterator.hasNext()) {
                AppDeal appDeal = dealIterator.next();
                if (appDeal.getDealCategoryId() == 5) {
                    DealVo dealVo = new DealVo();
                    dealVo.setLogoUrl(appDeal.getWebsite() == null ? "" : WebsiteHelper.getBiggerLogoUrl(appDeal.getWebsite()));
                    dealVo.setTitle(appDeal.getTitle());
                    dealVo.setWebsite(appDeal.getWebsite());
                    dealVo.setId(appDeal.getId());
                    dealVo.setDiscount(appDeal.getDiscount());
                    dealVo.setDeepLink(appDeal.getLinkUrl() == null ? "" : WebsiteHelper.getDealUrlWithAff(appDeal.getWebsite(), appDeal.getLinkUrl(), new String[]{deviceInfo.getMarketChannel().name(), deviceId}));
                    mobileDeals.add(dealVo);
                    dealIterator.remove();
                }
            }
            System.out.println("mobile  get   :" + mobileDeals.size());
            deals.addAll(mobileDeals);
            System.out.println("current size   :" + deals.size());
            //其他deal按照点击次数排序
            Collections.sort(list, new Comparator<AppDeal>() {
                @Override
                public int compare(AppDeal o1, AppDeal o2) {
                    if (o1.getDealClickCount() > o2.getDealClickCount()) {
                        return -1;
                    } else if (o1.getDealClickCount() < o2.getDealClickCount()) {
                        return 1;
                    }
                    return 0;
                }
            });
            System.out.println("last  list size   :" + list.size());
            for (AppDeal appDeal : list) {
                DealVo dealVo = new DealVo();
                dealVo.setLogoUrl(appDeal.getWebsite() == null ? "" : WebsiteHelper.getBiggerLogoUrl(appDeal.getWebsite()));
                dealVo.setTitle(appDeal.getTitle());
                dealVo.setWebsite(appDeal.getWebsite());
                dealVo.setId(appDeal.getId());
                dealVo.setDiscount(appDeal.getDiscount());
                dealVo.setDeepLink(appDeal.getLinkUrl() == null ? "" : WebsiteHelper.getDealUrlWithAff(appDeal.getWebsite(), appDeal.getLinkUrl(), new String[]{deviceInfo.getMarketChannel().name(), deviceId}));
                deals.add(dealVo);
            }
            System.out.println("current  deals size   :" + deals.size());
        }
        Map map = new HashMap();
        map.put("deals", deals);
        jsonObject.put("data", JSONObject.toJSON(map));
        Httphelper.sendJsonMessage(JSON.toJSONString(jsonObject, propertyFilter), response);
        return null;
    }

    @RequestMapping("info")
    public String getDealById(@RequestParam(defaultValue = "0") Long id, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        AppDeal appDeal = dealService.getDealById(id);
        Map hashMap = new HashMap<>();
        jsonObject.put("errorCode", "00000");
        jsonObject.put("msg", "ok");
        hashMap.put("provisions", "• Taxs are applicable.\n• This offer cannot be clubbed with any other ongoing offer.\n• Offer cannot be redeemed for cash.\n• No coupon code required.\n• Company has the right to end this offer without prior notice.\n");
        if (appDeal != null) {
            logger.info("has this deal " + id);
            hashMap.put("description", appDeal.getDescription());
        }
        jsonObject.put("data", hashMap);
        Httphelper.sendJsonMessage(JSON.toJSONString(jsonObject), response);
        return null;
    }
}
