package hasoffer.admin.controller;

import hasoffer.admin.controller.vo.TopSellingVo;
import hasoffer.base.model.PageModel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.IDUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.admin.ITopSellingService;
import hasoffer.core.bo.enums.TopSellStatus;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmImage;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.ptm.PtmTopSelling;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.core.product.IImageService;
import hasoffer.core.product.IProductService;
import hasoffer.core.redis.ICacheService;
import hasoffer.core.utils.ImageUtil;
import hasoffer.webcommon.helper.PageHelper;
import jodd.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created on 2016/7/6.
 */
@Controller
@RequestMapping(value = "/topselling")
public class TopSellingController {

    private static final String Q_COUNT_SKU = "SELECT COUNT(*) FROM PtmCmpSku t WHERE t.productId = ?0 ";
    private static final String Q_SRMSEARCHLOG_BYPRODUCTID = "SELECT t FROM SrmSearchLog t WHERE t.ptmProductId = ?0 ";
    private static final String Q_PTMCMPSKU_BYPRODUCTID = "SELECT t FROM PtmCmpSku t WHERE t.productId = ?0 ";

    @Resource
    ITopSellingService topSellingService;
    @Resource
    IProductService productService;
    @Resource
    IImageService imageService;
    @Resource
    ICacheService cacheService;
    @Resource
    IDataBaseManager dbm;
//    @Resource
//    @Qualifier
//    IFetchDubboService fetchDubboService;

    private Logger logger = LoggerFactory.getLogger(TopSellingController.class);

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView list(HttpServletRequest request,
                             @RequestParam(defaultValue = "") String topSellingStatusString,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "20") int size) {
        ModelAndView modelAndView = new ModelAndView("operate/topselling/list");

        List<TopSellingVo> topSellingVoList = new ArrayList<TopSellingVo>();

        if (StringUtils.isEmpty(topSellingStatusString)) {
            topSellingStatusString = TopSellStatus.ONLINE.toString();
            modelAndView.addObject("selectstatus", "");
        }

        TopSellStatus selectstatus = TopSellStatus.valueOf(topSellingStatusString);
        Calendar calendar = Calendar.getInstance();
        PageableResult<PtmTopSelling> pageableResult = topSellingService.findTopSellingList(selectstatus, page, size);

        List<PtmTopSelling> ptmTopSellingList = pageableResult.getData();

        for (PtmTopSelling ptmTopSelling : ptmTopSellingList) {

            TopSellingVo topSellingVo = new TopSellingVo();

            long productId = ptmTopSelling.getId();

            PtmProduct ptmProduct = productService.getProduct(productId);
            if (ptmProduct == null) {
                continue;
            }

            List<SrmSearchLog> logList = dbm.query(Q_SRMSEARCHLOG_BYPRODUCTID, Arrays.asList(ptmProduct.getId()));

            String logId = "";
            if (logList == null || logList.size() == 0) {

            } else {
                logId = logList.get(0).getId();
            }

            long skuNumber = dbm.querySingle(Q_COUNT_SKU, Arrays.asList(productId));

            topSellingVo.setId(ptmTopSelling.getId());
            topSellingVo.setName(ptmProduct.getTitle());
            topSellingVo.setImageurl(productService.getProductMasterImageUrl(productId));
            topSellingVo.setSkuNumber(skuNumber);
            topSellingVo.setLogid(logId);
            topSellingVo.setCount(ptmTopSelling.getCount());

            topSellingVoList.add(topSellingVo);
        }

        PageModel pageModel = PageHelper.getPageModel(request, pageableResult);
        modelAndView.addObject("page", pageModel);
        modelAndView.addObject("topSellingVoList", topSellingVoList);

        List<TopSellStatus> statusList = Arrays.asList(TopSellStatus.values());
        modelAndView.addObject("statusList", statusList);

        modelAndView.addObject("selectstatus", selectstatus);

        return modelAndView;
    }

    @RequestMapping(value = "detail/{topSellingId}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable long topSellingId) {

        ModelAndView modelAndView = new ModelAndView("operate/topselling/edit");

        PtmProduct ptmProduct = productService.getProduct(topSellingId);

        String title = ptmProduct.getTitle();
        modelAndView.addObject("title", title);

        PtmImage ptmImage = productService.getProductMasterImage(topSellingId);

        if (ptmImage == null) {
            return new ModelAndView("system/error");
        }

        String oriImageUrl = productService.getProductMasterImageUrl(topSellingId);
        modelAndView.addObject("oriImageUrl", oriImageUrl);
        modelAndView.addObject("topSellingId", topSellingId);

        return modelAndView;
    }


    @RequestMapping(value = "/edit/{topSellingId}", method = RequestMethod.POST)
    public ModelAndView edit(@PathVariable long topSellingId, MultipartFile file) {

        try {

            File imageFile = FileUtil.createTempFile(IDUtil.uuid(), ".jpg", null);
            FileUtil.writeBytes(imageFile, file.getBytes());
            String imagePath = ImageUtil.uploadImage(imageFile);

            //此处topsellingid就是productid
            imageService.updatePtmProductImagePath(topSellingId, imagePath);
//            //更新后需要更新topselling状态
//            topSellingService.updateTopSellingStatus(topSellingId, TopSellStatus.ONLINE);

            //编辑的时候注意更新图片清除缓存
            String PTMPRODUCT_IMAGE_CACHE_KEY = "PRODUCT__getProductMasterImageUrl_" + topSellingId;

            cacheService.del(PTMPRODUCT_IMAGE_CACHE_KEY);
        } catch (Exception e) {
            logger.error("image upload fail");
        }

        return new ModelAndView("redirect:/topselling/list");
    }

    @RequestMapping(value = "/delete/{topsellingid}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable long topsellingid) {

        topSellingService.updateTopSellingStatus(topsellingid, TopSellStatus.OFFLINE);

        return new ModelAndView("redirect:/topselling/list");
    }

    @RequestMapping(value = "/changeStatus/{topsellingid}", method = RequestMethod.GET)
    public void changeStatus(@PathVariable long topsellingid) {

        PtmTopSelling topSelling = topSellingService.findTopSellingById(topsellingid);

        TopSellStatus status = topSelling.getStatus();

        //清除缓存
        cacheService.del("PRODUCT__listPagedCmpSkus_TopSelling_0_20");
//                        PRODUCT__listPagedCmpSkus_TopSelling_0_20
        if (TopSellStatus.WAIT.equals(status)) {
            topSellingService.updateTopSellingStatus(topsellingid, TopSellStatus.ONLINE);
        } else {
            topSellingService.updateTopSellingStatus(topsellingid, TopSellStatus.WAIT);
        }
    }

//    @RequestMapping(value = "/sendFetchRequest/{productid}", method = RequestMethod.GET)
//    @ResponseBody
//    public String sendFetchRequest(@PathVariable long productid) {
//
//        List<PtmCmpSku> skuList = dbm.query(Q_PTMCMPSKU_BYPRODUCTID, Arrays.asList(productid));
//
//        for (PtmCmpSku sku : skuList) {
//            //判断，如果该sku 当天更新过价格, 直接跳过
//            Date updateTime = sku.getUpdateTime();
//            if (updateTime != null) {
//                if (updateTime.compareTo(TimeUtils.toDate(TimeUtils.today())) > 0) {
//                    continue;
//                }
//            }
//
//            //更新商品的信息，写入多图数据，写入描述/参数
//            try {
//                fetchDubboService.getProductsByUrl(sku.getId(), sku.getWebsite(), sku.getUrl());
//            } catch (Exception e) {
//
//            }
//        }
//
//        return "ok";
//    }
}
