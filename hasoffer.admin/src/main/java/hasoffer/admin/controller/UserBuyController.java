package hasoffer.admin.controller;

import hasoffer.admin.controller.vo.UserBuyVo;
import hasoffer.admin.manager.SearchLogManager;
import hasoffer.base.model.PageModel;
import hasoffer.base.model.PageableResult;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.core.persistence.po.stat.StatUserBuy;
import hasoffer.core.product.ICategoryService;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.product.IProductService;
import hasoffer.core.search.ISearchService;
import hasoffer.core.user.IBuyService;
import hasoffer.webcommon.helper.PageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/4/11.
 */
@Controller
@RequestMapping("/userbuy")
public class UserBuyController {

    @Resource
    IBuyService buyService;
    @Resource
    IProductService productService;
    @Resource
    ICmpSkuService cmpskuService;
    @Resource
    ISearchService searchService;
    @Resource
    ICategoryService categoryService;
    @Resource
    SearchLogManager searchLogManager;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView listUserBuy(HttpServletRequest request,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "20") int size) {

        ModelAndView modelAndView = new ModelAndView("userbuy/listuserbuy");

        PageableResult<StatUserBuy> pagedResults = buyService.listStatUserBuy(page, size);

        List<StatUserBuy> statUserBuyList = pagedResults.getData();
        PageModel pageModel = PageHelper.getPageModel(request, pagedResults);

        List<UserBuyVo> userBuyVoList = getUserBuyVo(statUserBuyList);

        modelAndView.addObject("statUserBuyList", userBuyVoList);
        modelAndView.addObject("page", pageModel);

        return modelAndView;
    }

    private List<UserBuyVo> getUserBuyVo(List<StatUserBuy> statUserBuyList) {

        List<UserBuyVo> userBuyVoList = new ArrayList<UserBuyVo>();

        for (StatUserBuy statUserBuy : statUserBuyList) {

            PtmProduct product = productService.getProduct(statUserBuy.getId());

            if (product == null) {
                continue;
            }

            UserBuyVo userBuyVo = new UserBuyVo();
            userBuyVo.setId(statUserBuy.getId());
            userBuyVo.setCount(statUserBuy.getCount());
            userBuyVo.setUpdateTime(statUserBuy.getLastBuyTime());
            userBuyVo.setTitle(product.getTitle());

            userBuyVoList.add(userBuyVo);
        }

        return userBuyVoList;
    }


    @RequestMapping(value = "/getsrmlogs/{productId}", method = RequestMethod.GET)
    public ModelAndView getSearchLogs(HttpServletRequest request,
                                      @PathVariable long productId,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "20") int size) {

        ModelAndView modelAndView = new ModelAndView("userbuy/listsrmlogs");

        PageableResult<SrmSearchLog> pagedResults = searchService.listSearchLogsByProductId(productId, page, size);
        PageModel pageModel = PageHelper.getPageModel(request, pagedResults);

        modelAndView.addObject("searchLogs", searchLogManager.getSearchLogs(pagedResults.getData()));
        modelAndView.addObject("page", pageModel);

        return modelAndView;
    }


}
