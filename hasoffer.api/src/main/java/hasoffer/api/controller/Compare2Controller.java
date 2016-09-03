package hasoffer.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import hasoffer.api.controller.vo.*;
import hasoffer.api.helper.ClientHelper;
import hasoffer.api.helper.Httphelper;
import hasoffer.api.helper.SearchHelper;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.AppDisplayMode;
import hasoffer.base.model.PageableResult;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.HexDigestUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.cache.CmpSkuCacheManager;
import hasoffer.core.cache.ProductCacheManager;
import hasoffer.core.cache.SearchLogCacheManager;
import hasoffer.core.exception.ERROR_CODE;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.enums.SearchPrecise;
import hasoffer.core.persistence.mongo.PtmCmpSkuDescription;
import hasoffer.core.persistence.mongo.PtmProductDescription;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmCmpSkuIndex2;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.core.persistence.po.urm.PriceOffNotice;
import hasoffer.core.persistence.po.urm.UrmUser;
import hasoffer.core.product.impl.ProductServiceImpl;
import hasoffer.core.product.solr.CategoryIndexServiceImpl;
import hasoffer.core.product.solr.CmpSkuModel;
import hasoffer.core.product.solr.CmpskuIndexServiceImpl;
import hasoffer.core.product.solr.ProductIndexServiceImpl;
import hasoffer.core.search.ISearchService;
import hasoffer.core.search.exception.NonMatchedProductException;
import hasoffer.core.system.impl.AppServiceImpl;
import hasoffer.core.user.IPriceOffNoticeService;
import hasoffer.core.utils.JsonHelper;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.webcommon.context.Context;
import hasoffer.webcommon.context.StaticContext;
import hasoffer.webcommon.helper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created on 2015/12/21.
 */
@Controller
@RequestMapping(value = "/cmp")
public class Compare2Controller {
    @Resource
    CmpskuIndexServiceImpl cmpskuIndexService;
    @Resource
    ProductIndexServiceImpl productIndexService;
    @Resource
    CategoryIndexServiceImpl categoryIndexService;
    @Resource
    ProductCacheManager productCacheManager;
    @Resource
    CmpSkuCacheManager cmpSkuCacheManager;
    @Resource
    SearchLogCacheManager searchLogCacheManager;
    @Resource
    ISearchService searchService;
    @Resource
    ProductServiceImpl productService;
    @Resource
    IMongoDbManager mongoDbManager;
    @Resource
    AppServiceImpl appService;
    @Resource
    IPriceOffNoticeService iPriceOffNoticeService;
    private Logger logger = LoggerFactory.getLogger(Compare2Controller.class);

    public static void main(String[] args) {
        String dealUrlWithAff = WebsiteHelper.getDealUrlWithAff(Website.SHOPCLUES, "http://www.shopclues.com/rupa-jon-sleeveless-vests-set-of-5.html", new String[]{MarketChannel.GOOGLEPLAY.name(), "dfecc858243a616a"});
        System.out.println(dealUrlWithAff);
//        String urlWithAff = WebsiteHelper.getUrlWithAff(Website.SHOPCLUES, "http://www.shopclues.com/reach-allure-speed.html", new String[]{MarketChannel.GOOGLEPLAY.name(), "asd123gfd654"});
//        System.out.println(urlWithAff);
//        String flipkart = WebsiteHelper.getDealUrlWithAff(Website.FLIPKART, "https://www.flipkart.com/apple-iphone-6s-silver-16-gb/p/itmebysgupjepunx", new String[]{MarketChannel.GOOGLEPLAY.name(), "asd123gfd654"});
//        System.out.println(flipkart);
//        String deeplinkWithAff = WebsiteHelper.getDeeplinkWithAff(Website.SHOPCLUES, "http://www.shopclues.com/reach-allure-speed.html", new String[]{MarketChannel.GOOGLEPLAY.name(), "asd123gfd654"});
//        System.out.println(deeplinkWithAff);
//        Map<Long, Integer> map = new HashMap<Long, Integer>();
//        map.put(1l, 4);
//        map.put(3l, 100);
//        map.put(2l, 3);
//        Set<Long> longs = map.keySet();
//        int t = 0;
//        int t1 = 0;
//        Iterator<Long> iterator = longs.iterator();
//        while (iterator.hasNext()) {
//            Long next = iterator.next();
//            t += map.get(next);
//            t1 += map.get(next) * next;
//        }
//        System.out.println(t1);
//        System.out.println(t);
//        System.out.println(BigDecimal.valueOf(t).divide(BigDecimal.valueOf(10), 1, BigDecimal.ROUND_HALF_UP));
//        BigDecimal s = BigDecimal.ZERO;
//        Set<Long> long2 = map.keySet();
//        Iterator<Long> iterator1 = long2.iterator();
//        while (iterator1.hasNext()) {
//            Long next = iterator1.next();
//            BigDecimal ss = BigDecimal.valueOf(map.get(next)).divide(BigDecimal.valueOf(t), 1, BigDecimal.ROUND_HALF_UP);
//            s = s.add(ss.multiply(BigDecimal.valueOf(next)));
//        }
//        System.out.println(s.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_UP));
//        System.out.println(305 % 10);
//        String price = "Rs. 17,511";
//        if (!StringUtils.isEmpty(price)) {
//            //如果price不为空
//            if (price.contains(",")) {
//                System.out.println(price + "    contains , ");
//                price = price.replaceAll(",", " ");
//            }
//            if (price.contains("Rs.")) {
//                System.out.println(price + "    contains Rs. ");
//                price = price.replaceAll("Rs.", " ");
//            }
//            price = price.replaceAll(" ", "");
//            System.out.println(" price is " + price);
//        }
        if ("Samsung Tizen Z3 (8GB,Tizen OS)".equalsIgnoreCase("sumsung tizen z3 (8GB,Tizen OS)")) {
            System.out.println("dd");
        }

    }

    // @Cacheable(value = "compare", key = "'getcmpskus_'+#q+'_'+#site+'_'+#price+'_'+#page+'_'+#size")
    // Model And View 不是可序列化的 会抛出  java.io.NotSerializableException 异常
    @RequestMapping(value = "/getcmpskus", method = RequestMethod.GET)
    public ModelAndView getcmpskus(HttpServletRequest request,
                                   @RequestParam(defaultValue = "") final String q,
                                   @RequestParam(defaultValue = "") final String brand,
                                   @RequestParam(defaultValue = "") final String sourceId,
                                   @RequestParam(defaultValue = "") String site,
                                   @RequestParam(defaultValue = "0") String price,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        String deviceId = (String) Context.currentContext().get(StaticContext.DEVICE_ID);
        DeviceInfoVo deviceInfo = (DeviceInfoVo) Context.currentContext().get(Context.DEVICE_INFO);

        SearchIO sio = new SearchIO(sourceId, q, brand, site, price, deviceInfo.getMarketChannel(), deviceId, page, size);
        CmpResult cr = null;

        PtmCmpSkuIndex2 cmpSkuIndex = null;

        try {
            // 先去匹配sku
            cmpSkuIndex = cmpSkuCacheManager.getCmpSkuIndex2(sio.getDeviceId(), sio.getCliSite(), sio.getCliSourceId(), sio.getCliQ());
            getSioBySearch(sio);
            cr = getCmpResult(sio, cmpSkuIndex);
        } catch (Exception e) {
            if (sio.getHsProId() > 0) {
                PtmProduct ptmProduct = productService.getProduct(sio.getHsProId());
                if (ptmProduct == null) {
                    productService.deleteProduct(sio.getHsProId());
                } else {
                    //logger.info(ptmProduct.toString());
                }

            }
            //logger.error(e.getMessage());
            //  logger.error(String.format("[NonMatchedProductException]:query=[%s].site=[%s].price=[%s].page=[%d, %d]", q, site, price, page, size));

            cr = getDefaultCmpResult(sio, cmpSkuIndex);
        }
        // 速度优化
        SearchHelper.addToLog(sio);

        ModelAndView mav = new ModelAndView();

        mav.addObject("priceOff", cr.getPriceOff());
        mav.addObject("product", cr.getProductVo());
        mav.addObject("skus", cr.getPagedComparedSkuVos().getData());
        mav.addObject("page", PageHelper.getPageModel(request, cr.getPagedComparedSkuVos()));
        mav.addObject("newLayout", false);

//        logger.info(sio.toString());

        return mav;
    }

    /**
     * 根据商品获取比价的sku列表
     *
     * @return
     */
    @RequestMapping("sdk/cmpskus")
    public String cmpSkus(@RequestParam(defaultValue = "") final String q,
                          @RequestParam(defaultValue = "") final String brand,
                          @RequestParam(defaultValue = "") final String sourceId,
                          @RequestParam(defaultValue = "") String site,
                          @RequestParam(defaultValue = "0") String price,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "20") int pageSize,
                          HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorCode", "00000");
        jsonObject.put("msg", "ok");
        PropertyFilter propertyFilter = JsonHelper.filterProperty(new String[]{"ratingNum", "bestPrice", "backRate", "support", "price", "returnGuarantee", "freight"});
        //初始化sio对象
        String deviceId = (String) Context.currentContext().get(StaticContext.DEVICE_ID);
        DeviceInfoVo deviceInfo = (DeviceInfoVo) Context.currentContext().get(Context.DEVICE_INFO);
        CmpResult cr = null;
        PtmProduct ptmProduct = null;
        if (!StringUtils.isEmpty(price)) {
            //如果price不为空
            if (price.contains(",")) {
                System.out.println(price + "    contains , ");
                price = price.replaceAll(",", " ");
            }
            if (price.contains("Rs.")) {
                System.out.println(price + "    contains Rs. ");
                price = price.replaceAll("Rs.", " ");
            }
            if (price.contains("₹")) {
                System.out.println(price + "    contains ₹ ");
                price = price.replaceAll("₹", " ");
            }
            price = price.replaceAll(" ", "");
            System.out.println(" price is " + price);
        }
        SearchIO sio = new SearchIO(sourceId, q, brand, site, price, deviceInfo.getMarketChannel(), deviceId, page, pageSize);
        try {
            //匹配sku
            PtmCmpSkuIndex2 cmpSkuIndex = cmpSkuCacheManager.getCmpSkuIndex2(sio.getDeviceId(), sio.getCliSite(), sio.getCliSourceId(), sio.getCliQ());
            //根据title匹配到商品
            getSioBySearch(sio);
            logger.info("get product from solr or searchLog ");
            if (sio.getHsProId() > 0) {
                ptmProduct = productService.getProduct(sio.getHsProId());
                //若此时匹配到的商品实际库中不存在则删除此匹配记录,下次重新匹配
                if (ptmProduct == null) {
                    logger.info("product id" + sio.getHsProId() + " is not exist ");
                    productService.deleteProduct(sio.getHsProId());
                    //未匹配,结束操作
                } else {
                    cr = getCmpProducts(cmpSkuIndex, sio);
                    if (cr != null && cr.getPriceList().size() > 0 && cr.getPriceList().size() > 0) {
                        cr.setPriceOff(cr.getPriceList().get(0).getSaved());
                    }
//                    if (cr == null) {
//                        cr = new CmpResult();
//                        cr.setProductVo(new ProductVo(sio.getHsProId(), sio.getCliQ(), productCacheManager.getProductMasterImageUrl(ptmProduct.getId()), 0.0f, WebsiteHelper.getDeeplinkWithAff(Website.valueOf(ptmProduct.getSourceSite()), ptmProduct.getSourceUrl(), new String[]{sio.getMarketChannel().name(), sio.getDeviceId()})));
//                    }
                    cr.setProductId(sio.getHsProId());
                    //cr.setCopywriting(ptmProduct != null && ptmProduct.isStd() ? "Searched across Flipkart,Snapdeal,Paytm & 6 other apps to get the best deals for you." : "Looked around Myntre,Jabong & 5 other apps,thought you might like these items as well..");
                    cr.setCopywriting("Searched across Flipkart,Snapdeal,Paytm & 6 other apps to get the best deals for you.");
                    //暂时屏蔽标品非标品
                    // cr.setDisplayMode(ptmProduct != null && ptmProduct.isStd() ? AppDisplayMode.NONE : AppDisplayMode.WATERFALL);
                    // cr.setStd(ptmProduct.isStd());
                    cr.setDisplayMode(AppDisplayMode.NONE);
                    cr.setStd(true);
                    jsonObject.put("data", JSONObject.toJSON(cr));
                    Httphelper.sendJsonMessage(JSON.toJSONString(jsonObject, propertyFilter), response);
                    return null;
                }
            } else {
                //小于等于0,直接返回
                logger.info("productid is " + sio.getHsProId() + " ls than zero");
                jsonObject.put("data", JSONObject.toJSON(cr));
                Httphelper.sendJsonMessage(JSON.toJSONString(jsonObject, propertyFilter), response);
                return null;
            }
        } catch (Exception e) {
            logger.error(String.format("sdk_cmp_  [NonMatchedProductException]:query=[%s].site=[%s].price=[%s].page=[%d, %d]", q, site, price, page, pageSize));
            jsonObject.put("data", JSONObject.toJSON(cr));
            Httphelper.sendJsonMessage(JSON.toJSONString(jsonObject, propertyFilter), response);
            return null;
        }
        Httphelper.sendJsonMessage(JSON.toJSONString(jsonObject, propertyFilter), response);
        return null;
    }

    @RequestMapping(value = "/cmpsku", method = RequestMethod.GET)
    public ModelAndView cmpsku(@RequestParam(defaultValue = "0") final String id,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "20") int pageSize,
                               HttpServletResponse response,
                               HttpServletRequest request
    ) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorCode", "00000");
        jsonObject.put("msg", "ok");
        PropertyFilter propertyFilter = JsonHelper.filterProperty(new String[]{"skuPrice", "deepLink", "saved", "priceOff", "productVo", "pagedComparedSkuVos", "copywriting", "displayMode", "std", "cashBack"});
        CmpResult cr = null;
        PtmProduct product = productService.getProduct(Long.valueOf(id));
        String userToken = Context.currentContext().getHeader("usertoken");
        if (product != null) {
            System.out.println("product is exist in our system " + product.getId());
            String deviceId = (String) Context.currentContext().get(StaticContext.DEVICE_ID);
            DeviceInfoVo deviceInfo = (DeviceInfoVo) Context.currentContext().get(Context.DEVICE_INFO);
            SearchIO sio = new SearchIO(product.getSourceId(), product.getTitle(), "", product.getSourceSite(), product.getPrice() + "", deviceInfo.getMarketChannel(), deviceId, page, pageSize);
            try {
//                cr = getCmpProducts(sio, product);
                cr = getCmpProducts(sio, product, userToken);
                jsonObject.put("page", JSONObject.toJSON(PageHelper.getPageModel(request, cr.getPagedComparedSkuVos())));
            } catch (Exception e) {
                logger.error(String.format("[NonMatchedProductException]:query=[%s].site=[%s].price=[%s].page=[%d, %d]", product.getTitle(), product.getSourceSite(), product.getPrice(), page, pageSize));
                //if exception occured ,get default cmpResult
                jsonObject.put("data", JSONObject.toJSON(cr));
                Httphelper.sendJsonMessage(JSON.toJSONString(jsonObject, propertyFilter), response);
                return null;
            }
            // 速度优化
            SearchHelper.addToLog(sio);
            logger.debug(sio.toString());
            jsonObject.put("data", JSONObject.toJSON(cr));
            Httphelper.sendJsonMessage(JSON.toJSONString(jsonObject, propertyFilter), response);
            return null;
        }
        jsonObject.put("data", JSONObject.toJSON(cr));
        Httphelper.sendJsonMessage(JSON.toJSONString(jsonObject, propertyFilter), response);
        return null;
    }

    private CmpResult getDefaultCmpResult(SearchIO sio, PtmCmpSkuIndex2 cmpSkuIndex) {
        String currentDeeplink = "";
        if (cmpSkuIndex != null && cmpSkuIndex.getId() != null && cmpSkuIndex.getId() > 0) {
            PtmCmpSku cmpSku = cmpSkuCacheManager.getCmpSkuById(cmpSkuIndex.getId());

            if (cmpSku != null && cmpSku.getWebsite().equals(sio.getCliSite())) {
                currentDeeplink = WebsiteHelper.getDeeplinkWithAff(cmpSku.getWebsite(), cmpSku.getUrl(), new String[]{sio.getMarketChannel().name(), sio.getDeviceId()});
            }
        }

        List<ComparedSkuVo> comparedSkuVos = new ArrayList<ComparedSkuVo>();
        comparedSkuVos.add(new ComparedSkuVo(sio.getCliSite(), sio.getCliQ(), sio.getCliPrice()));

        return new CmpResult(0,
                new ProductVo(0L, sio.getCliQ(), "", sio.getCliPrice(), currentDeeplink),
                new PageableResult<ComparedSkuVo>(comparedSkuVos, 0, 1, 10)
        );
    }

    /**
     * 从solr中搜索
     *
     * @param sio
     */
    private void searchForResult_old(SearchIO sio) {
        String _q = StringUtils.getSearchKey(sio.getCliQ());

        long cateId = 0L;
        int level = 0, index_for;

        // 搜索商品
        PageableResult<Long> pagedProIds = productIndexService.searchPro(cateId, level, StringUtils.toLowerCase(_q), 1, 5);

        List<Long> proIds = pagedProIds.getData();

        if (ArrayUtils.isNullOrEmpty(proIds)) {
            throw new NonMatchedProductException(ERROR_CODE.UNKNOWN, _q, "", 0);
        }

        long proId = proIds.get(0);
        PtmProduct product = productCacheManager.getProduct(proId); //productService.getProduct(proId);

        float mc = StringUtils.wordMatchD(StringUtils.toLowerCase(product.getTitle()), _q);
        if (!StringUtils.isEmpty(product.getTag())) {
            mc = (mc + StringUtils.wordMatchD(StringUtils.toLowerCase(product.getTag()), _q) * 2) / 2;
        }

        // 匹配度如果小于40%, 则认为不匹配
        if (mc <= 0.4) {
            throw new NonMatchedProductException(ERROR_CODE.UNKNOWN, _q, product.getTitle(), mc);
        }

        sio.set(cateId, proId, 0L);
    }

    /**
     * 从solr中搜索
     *
     * @param sio
     */
    private void searchForResult(SearchIO sio) throws NonMatchedProductException {
        String _q = StringUtils.getSearchKey(sio.getCliQ());

        // 搜索SKU
        PageableResult<CmpSkuModel> pagedCmpskuModels = cmpskuIndexService.searchSku(_q, 1, 5);
        List<CmpSkuModel> skuModels = pagedCmpskuModels.getData();
        System.out.println("skuModels   " + skuModels.size());

        if (ArrayUtils.isNullOrEmpty(skuModels)) {
            throw new NonMatchedProductException(ERROR_CODE.UNKNOWN, _q, "", 0);
        }

        CmpSkuModel skuModel = null;
        for (CmpSkuModel cmpSkuModel : skuModels) {
            if (cmpSkuModel.getProductId() == 0) {
                continue;
            }
            skuModel = cmpSkuModel;
            break;
        }

        if (skuModel == null) {
            throw new NonMatchedProductException(ERROR_CODE.UNKNOWN, _q, "", 0);
        }

        String title = skuModel.getTitle();

        float mc = StringUtils.wordMatchD(StringUtils.toLowerCase(title), _q);
        System.out.println(" mc " + mc);
        // 匹配度如果小于40%, 则认为不匹配
        if (mc <= 0.4) {
            throw new NonMatchedProductException(ERROR_CODE.UNKNOWN, _q, title, mc);
        }

        long cateId = 0L;
        System.out.println("skuModel.getProductId()  " + skuModel.getProductId());
        sio.set(cateId, skuModel.getProductId(), skuModel.getId());
    }

    private void getSioBySearch(SearchIO sio) {

        String q = sio.getCliQ();

        String logId = HexDigestUtil.md5(q + "-" + sio.getCliSite().name()); // 这个值作为log表的id

        SrmSearchLog srmSearchLog = searchLogCacheManager.findSrmSearchLog(logId, true);

        if (srmSearchLog != null
                && (srmSearchLog.getPrecise() == SearchPrecise.TIMERSET2
                || srmSearchLog.getPrecise() == SearchPrecise.MANUALSET)) {

            if (srmSearchLog.getPtmProductId() <= 0) {
                logger.error("Found search log. but product id is 0 .");
                throw new NonMatchedProductException(ERROR_CODE.UNKNOWN, sio.getCliQ(), "", 0);
            }

            sio.set(srmSearchLog.getCategory(), srmSearchLog.getPtmProductId(), srmSearchLog.getPtmCmpSkuId());
            System.out.println("getHsProId  :" + sio.getHsProId());
        } else {
            if (srmSearchLog == null) {
                System.out.println("srmSearchLog is null");
                sio.setFirstSearch(true);
                System.out.println("setFirstSearch is true");
            }

            try {
                System.out.println("searchForResult  ");
                searchForResult(sio);
                System.out.println(" searchForResult result  " + sio.getHsProId());
            } catch (NonMatchedProductException e) {
                System.out.println("searchForResult_old  ");
                searchForResult_old(sio);
                System.out.println(" searchForResult_old result  " + sio.getHsProId());
            }
        }

        /*if (srmSearchLog == null) {
            sio.setFirstSearch(true);

            searchForResult(sio);
        } else {
            if (srmSearchLog.getPrecise() == SearchPrecise.TIMERSET2
                    || srmSearchLog.getPrecise() == SearchPrecise.MANUALSET) {

                if (srmSearchLog.getPtmProductId() <= 0) {
                    logger.debug("Found search log. but product id is 0 .");
                    throw new NonMatchedProductException(ERROR_CODE.UNKNOWN, sio.getCliQ(), "", 0);
                }

                sio.set(srmSearchLog.getCategory(), srmSearchLog.getPtmProductId(), srmSearchLog.getPtmCmpSkuId());
            } else {
                searchForResult(sio);
            }
        }*/
    }

    private CmpResult getCmpResult(SearchIO sio, PtmCmpSkuIndex2 cmpSkuIndex) {

        List<ComparedSkuVo> comparedSkuVos = new ArrayList<ComparedSkuVo>();

        /**
         * 如果匹配到商品，proId是匹配到的商品ID，cmpSkuId是比价列表中对应该网站的skuId
         * 如果没有匹配到，或比价列表不含该网站，则相应变量值为0
         */
        long cmpSkuId = 0L;
        float minPrice = sio.getCliPrice(), maxPrice = sio.getCliPrice();

        PageableResult<PtmCmpSku> pagedCmpskus = productCacheManager.listPagedCmpSkus(sio.getHsProId(), sio.getPage(), sio.getSize());
        List<PtmCmpSku> cmpSkus = pagedCmpskus.getData();
        PtmCmpSku clientCmpSku = null;

        float cliPrice = sio.getCliPrice(), priceOff = 0.0f;
        if (ArrayUtils.hasObjs(cmpSkus)) {

            for (PtmCmpSku cmpSku : cmpSkus) {
                if (sio.getCliSite().equals(cmpSku.getWebsite())) {
                    clientCmpSku = cmpSku;
                    break;
                }
            }

            if (clientCmpSku != null) {
                cmpSkuId = clientCmpSku.getId();
                if (cliPrice <= 0) {
                    cliPrice = clientCmpSku.getPrice();
                    minPrice = cliPrice;
                    maxPrice = cliPrice;
                } else {
                    clientCmpSku.setPrice(cliPrice);
                }
            } else {
                // 如果比价列表中没有找到该网站的 sku， 则把客户端传上来的商品返回
                addVo(comparedSkuVos, new ComparedSkuVo(sio.getCliSite(), sio.getCliQ(), sio.getCliPrice()));
            }

            // 获取vo list
            for (PtmCmpSku cmpSku : cmpSkus) {

                if (cmpSku.getWebsite() == null
                        || cmpSku.getPrice() <= 0
                        || cmpSku.getStatus() != SkuStatus.ONSALE) { // 临时过滤掉不能更新价格的商品
                    logger.error(cmpSku.getId() + ", price=" + cmpSku.getPrice() + ", status=" + cmpSku.getStatus());
                    continue;
                }
                if (minPrice <= 0 || minPrice > cmpSku.getPrice()) {
                    minPrice = cmpSku.getPrice();
                }
                if (maxPrice <= 0 || maxPrice < cmpSku.getPrice()) {
                    maxPrice = cmpSku.getPrice();
                }

                // 忽略前台返回的价格
                List<String> affs = getAffs(sio);
                ComparedSkuVo csv = new ComparedSkuVo(cmpSku, affs.toArray(new String[0]));
                csv.setPriceOff(cliPrice - cmpSku.getPrice());

                addVo(comparedSkuVos, csv);
            }

            if (ArrayUtils.isNullOrEmpty(comparedSkuVos)) {
                logger.error("Compared SKU VO IS EMPTY");
                throw new NonMatchedProductException(ERROR_CODE.UNKNOWN, "", sio.getCliQ(), sio.getCliPrice());
            }

            float standPrice = maxPrice;
            if (cliPrice <= 0) {
                // 取一个标准价格，如果client sku 为null，则取maxPrice
                if (clientCmpSku != null) {
                    standPrice = clientCmpSku.getPrice();
                }
                for (ComparedSkuVo skuVo : comparedSkuVos) {
                    skuVo.setPriceOff(standPrice - skuVo.getPrice());
                }
            } else {
                standPrice = cliPrice;
            }
            // you can save ...
            priceOff = standPrice - minPrice;

            //根据价格排序
            Collections.sort(comparedSkuVos, new Comparator<ComparedSkuVo>() {
                @Override
                public int compare(ComparedSkuVo o1, ComparedSkuVo o2) {
                    if (o1.getPrice() > o2.getPrice()) {
                        return 1;
                    } else if (o1.getPrice() < o2.getPrice()) {
                        return -1;
                    }
                    return 0;
                }
            });

        } else {
            // logger.error("Found skus size is 0 .");
            throw new NonMatchedProductException(ERROR_CODE.UNKNOWN, sio.getCliQ(), sio.getKeyword(), 0.0f);
        }

        sio.setHsSkuId(cmpSkuId);
        String currentDeeplink = "";
        try {
            if (cmpSkuIndex != null && cmpSkuIndex.getId() > 0) {
                if (cmpSkuIndex.getWebsite().equals(sio.getCliSite())) {
                    currentDeeplink = WebsiteHelper.getDeeplinkWithAff(cmpSkuIndex.getWebsite(), cmpSkuIndex.getUrl(), new String[]{sio.getMarketChannel().name(), sio.getDeviceId()});
                }
                /*PtmCmpSku cmpSku = cmpSkuCacheManager.getCmpSkuById(cmpSkuIndex.getId());
                if (cmpSku.getWebsite().equals(sio.getCliSite())) {
                    currentDeeplink = WebsiteHelper.getDeeplinkWithAff(cmpSku.getWebsite(), cmpSku.getUrl(), new String[]{sio.getMarketChannel().name(), sio.getDeviceId()});
                }*/
            } else if (clientCmpSku != null) {
                if (!cmpSkuCacheManager.isFlowControlled(sio.getDeviceId(), sio.getCliSite())) {
                    if (StringUtils.isEqual(clientCmpSku.getSkuTitle(), sio.getCliQ()) && clientCmpSku.getPrice() == cliPrice) {
                        currentDeeplink = WebsiteHelper.getDeeplinkWithAff(clientCmpSku.getWebsite(), clientCmpSku.getUrl(), new String[]{sio.getMarketChannel().name(), sio.getDeviceId()});
                    }
                }
            }
        } catch (Exception e) {
            // logger.error(e.getMessage());
        }

        String imageUrl = productCacheManager.getProductMasterImageUrl(sio.getHsProId());//productService.getProductMasterImageUrl(sio.getHsProId());
        ProductVo productVo = new ProductVo(sio.getHsProId(), sio.getCliQ(), imageUrl, minPrice, currentDeeplink);

        return new CmpResult(priceOff, productVo, new PageableResult<ComparedSkuVo>(comparedSkuVos, pagedCmpskus.getNumFund(), pagedCmpskus.getCurrentPage(), pagedCmpskus.getPageSize()));
    }

    private List<String> getAffs(SearchIO sio) {
        List<String> affs = new ArrayList<String>();
        affs.add(sio.getMarketChannel().name());
        affs.add(sio.getDeviceId());
        UrmUser urmUser = appService.getUserByUserToken((String) Context.currentContext().get(StaticContext.USER_TOKEN));
        if (urmUser != null) {
            affs.add(urmUser.getId().toString());
        }
        return affs;
    }

    /**
     * get cmp product results
     *
     * @param sio
     * @return
     */
    private CmpResult getCmpProducts(SearchIO sio, PtmProduct product, String userToken) {
        //初始化一个空的用于存放比价商品列表的List
        List<CmpProductListVo> comparedSkuVos = new ArrayList<CmpProductListVo>();
        CmpResult cmpResult = new CmpResult();
        //从ptmCmpSku表获取 productId为指定值、且状态为ONSALE 按照价格升序排列
        PageableResult<PtmCmpSku> pagedCmpskus = productCacheManager.listPagedCmpSkus(product.getId(), sio.getPage(), sio.getSize());
        if (pagedCmpskus != null && pagedCmpskus.getData() != null && pagedCmpskus.getData().size() > 0) {
            System.out.println("get skus size is " + pagedCmpskus.getData().size());
            List<PtmCmpSku> cmpSkus = pagedCmpskus.getData();
            System.out.println(" cmpskus size is " + cmpSkus.size());
            //评论数按照加权平均值展示
            Long tempTotalComments = Long.valueOf(0);
            //统计site
            Set<Website> websiteSet = new HashSet<Website>();
            //统计site
//            Set<Website> websiteSet = new HashSet<Website>();
            //初始化price为客户端传输的price
            if (ArrayUtils.hasObjs(cmpSkus)) {
                // 获取vo list
                for (PtmCmpSku cmpSku : cmpSkus) {
                    if (cmpSku.getWebsite() == null
                            || cmpSku.getPrice() <= 0
                            ) {
                        continue;
                    }
                    if (cmpSku.getWebsite() != null) {
                        websiteSet.add(cmpSku.getWebsite());
                    }
                    // 忽略前台返回的价格
                    System.out.println(" Enter cmpProductListVO set area ");
                    System.out.println("sku smallImagePath is " + cmpSku.getSmallImagePath());
                    CmpProductListVo cplv = new CmpProductListVo(cmpSku, WebsiteHelper.getLogoUrl(cmpSku.getWebsite()));
                    System.out.println("after set , imageUrl is  " + cplv.getImageUrl());
                    System.out.println("set properteis over l");
                    cplv.setDeepLinkUrl(WebsiteHelper.getDealUrlWithAff(cmpSku.getWebsite(), cmpSku.getUrl(), new String[]{sio.getMarketChannel().name()}));
                    cplv.setDeepLink(WebsiteHelper.getDeeplinkWithAff(cmpSku.getWebsite(), cmpSku.getUrl(), new String[]{sio.getMarketChannel().name()}));
                    cplv.setIsAlert(isPriceOffAlert(userToken, cplv.getId()));
                    comparedSkuVos.add(cplv);
                }
                if (ArrayUtils.isNullOrEmpty(comparedSkuVos)) {
                    throw new NonMatchedProductException(ERROR_CODE.UNKNOWN, "", product.getTitle(), product.getPrice());
                }
                //根据价格排序
                Collections.sort(comparedSkuVos, new Comparator<CmpProductListVo>() {
                    @Override
                    public int compare(CmpProductListVo o1, CmpProductListVo o2) {
                        if (o1.getPrice() > o2.getPrice()) {
                            return 1;
                        } else if (o1.getPrice() < o2.getPrice()) {
                            return -1;
                        }
                        return 0;
                    }
                });

            } else {
                logger.debug("Found skus size is 0 .");
                throw new NonMatchedProductException(ERROR_CODE.UNKNOWN, sio.getCliQ(), sio.getKeyword(), 0.0f);
            }
            List<CmpProductListVo> tempCmpProductListVos = new ArrayList<CmpProductListVo>();
            //计算评论数*星级的总和
            int sum = 0;
            System.out.println("iterator  comparedSkuVos , and  it is size is " + comparedSkuVos.size());
            for (CmpProductListVo cmpProductListVo : comparedSkuVos) {
                if (websiteSet.size() <= 0) {
                    break;
                }
                if (websiteSet.contains(cmpProductListVo.getWebsite())) {
                    websiteSet.remove(cmpProductListVo.getWebsite());
                    //去除列表中除此之外的其他此site的数据
                    if (!cmpProductListVo.getWebsite().equals(Website.EBAY)) {
                        System.out.println("not ebay ");
                        //评论数*星级 累加 除以评论数和
                        sum += cmpProductListVo.getTotalRatingsNum() * cmpProductListVo.getRatingNum();
                        tempTotalComments += cmpProductListVo.getTotalRatingsNum();
                    }
                    //获取offers
                    System.out.println(" get offers from mongoDb ");
                    System.out.println(" cmpProductListVo " + cmpProductListVo.getId() + "  : price : " + cmpProductListVo.getPrice());
                    PtmCmpSkuDescription ptmCmpSkuDescription = mongoDbManager.queryOne(PtmCmpSkuDescription.class, cmpProductListVo.getId());
                    if (ptmCmpSkuDescription != null) {
                        System.out.println(" aha  aha  aha ");
                        String offers = ptmCmpSkuDescription.getOffers();
                        System.out.println(" got it ,and offers is " + offers);
                        if (!StringUtils.isEmpty(offers)) {
                            List<String> offer = new ArrayList<>();
                            String[] temps = offers.split(",");
                            for (String str : temps) {
                                offer.add(str);
                            }
                            cmpProductListVo.setOffers(offer);
                        }
                    }
                    tempCmpProductListVos.add(cmpProductListVo);
                }
            }
            //移除之前加进列表的所有的sku列表
            comparedSkuVos = null;
            comparedSkuVos = new ArrayList<>();
            //将新的加入的放入到列表中
            comparedSkuVos.addAll(tempCmpProductListVos);
            String imageUrl = productCacheManager.getProductMasterImageUrl(product.getId());
            cmpResult.setImage(imageUrl);
            cmpResult.setName(product.getTitle());
            PageableResult<CmpProductListVo> priceList = new PageableResult<CmpProductListVo>(comparedSkuVos, pagedCmpskus.getNumFund(), pagedCmpskus.getCurrentPage(), pagedCmpskus.getPageSize());
            cmpResult.setBestPrice(priceList.getData().get(0).getPrice());
            cmpResult.setPriceList(priceList.getData());
            int rating = ClientHelper.returnNumberBetween0And5(BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(tempTotalComments == 0 ? 1 : tempTotalComments), 0, BigDecimal.ROUND_HALF_UP).longValue());
            cmpResult.setRatingNum(rating <= 0 ? 90 : rating);
            PtmProductDescription ptmProductDescription = mongoDbManager.queryOne(PtmProductDescription.class, product.getId());
            String specs = "";
            if (ptmProductDescription != null) {
                specs = ptmProductDescription.getJsonDescription();
            }
            cmpResult.setPagedComparedSkuVos(priceList);
            cmpResult.setSpecs(specs);
            //cmpResult.setTotalRatingsNum(WeightedAverage.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_UP).longValue());
            cmpResult.setTotalRatingsNum(tempTotalComments);
            return cmpResult;
        }
        return cmpResult;
    }

    private CmpResult getCmpProducts(PtmCmpSkuIndex2 ptmCmpSkuIndex2, SearchIO sio) {
        System.out.println(" ptmCmpSkuIndex2  " + ptmCmpSkuIndex2);
        long cmpSkuId = 0L;
        //初始化一个空的用于存放比价商品列表的List
        List<CmpProductListVo> comparedSkuVos = new ArrayList<CmpProductListVo>();
        CmpResult cmpResult = new CmpResult();
        // 1. 查询此商品对应的sku列表 状态为ONSALE/OUTSTOCK
        PageableResult<PtmCmpSku> pagedCmpskus = productCacheManager.listCmpSkus(sio.getHsProId(), sio.getPage(), sio.getSize());
        PtmCmpSku clientCmpSku = null;
        float cliPrice = sio.getCliPrice();
        if (pagedCmpskus != null && pagedCmpskus.getData() != null && pagedCmpskus.getData().size() > 0) {
            List<PtmCmpSku> cmpSkus = pagedCmpskus.getData();
            //统计site
            Set<Website> websiteSet = new HashSet<Website>();
            if (ArrayUtils.hasObjs(cmpSkus)) {
                for (PtmCmpSku cmpSku : cmpSkus) {
                    if (sio.getCliSite().equals(cmpSku.getWebsite())) {
                        clientCmpSku = cmpSku;
                        break;
                    }
                }

                if (clientCmpSku != null) {
                    cmpSkuId = clientCmpSku.getId();
                    if (cliPrice <= 0) {
                        cliPrice = clientCmpSku.getPrice();
                    } else {
                        clientCmpSku.setPrice(cliPrice);
                    }
                } else {
                    // 如果比价列表中没有找到该网站的 sku， 则把客户端传上来的商品返回
                    CmpProductListVo cplv = new CmpProductListVo();
                    cplv.setTitle(sio.getCliQ());
                    cplv.setPrice(Math.round(sio.getCliPrice()));
                    cplv.setWebsite(sio.getCliSite());
                    comparedSkuVos.add(cplv);
                }
                // 获取vo list
                for (PtmCmpSku cmpSku : cmpSkus) {
                    if (cmpSku.getWebsite() == null
                            || cmpSku.getPrice() <= 0) { // 临时过滤掉不能更新价格的商品
                        continue;
                    }
                    if (cmpSku.getWebsite() != null) {
                        websiteSet.add(cmpSku.getWebsite());
                    }
                    System.out.println("id :  " + cmpSku.getId() + " imagePath " + cmpSku.getSmallImagePath());
                    CmpProductListVo cplv = new CmpProductListVo(cmpSku, sio.getCliPrice());
                    cplv.setDeepLink(WebsiteHelper.getDealUrlWithAff(cmpSku.getWebsite(), cmpSku.getUrl(), new String[]{sio.getMarketChannel().name()}));
                    comparedSkuVos.add(cplv);
                }
                if (ArrayUtils.isNullOrEmpty(comparedSkuVos)) {
                    throw new NonMatchedProductException(ERROR_CODE.UNKNOWN, sio.getCliQ(), "productid_" + sio.getHsProId(), sio.getCliPrice());
                }
                //根据价格排序
                Collections.sort(comparedSkuVos, new Comparator<CmpProductListVo>() {
                    @Override
                    public int compare(CmpProductListVo o1, CmpProductListVo o2) {
                        if (o1.getPrice() > o2.getPrice()) {
                            return 1;
                        } else if (o1.getPrice() < o2.getPrice()) {
                            return -1;
                        }
                        return 0;
                    }
                });
                //如果客户端传价格无法解析则重新计算save
                if (sio.getCliPrice() <= 0) {
                    int maxPrice = comparedSkuVos.get(comparedSkuVos.size() - 1).getPrice();
                    System.out.println(" can not analysis client's price ,use maxPrice instead of it " + maxPrice);
                    Iterator<CmpProductListVo> iterator = comparedSkuVos.iterator();
                    while (iterator.hasNext()) {
                        CmpProductListVo next = iterator.next();
                        next.setSaved(Math.round(cliPrice - maxPrice));
                    }
                }
            } else {
                logger.debug("Found skus size is 0 .");
                throw new NonMatchedProductException(ERROR_CODE.UNKNOWN, sio.getCliQ(), sio.getKeyword(), 0.0f);
            }
            sio.setHsSkuId(cmpSkuId);
            List<CmpProductListVo> tempCmpProductListVos = new ArrayList<CmpProductListVo>();
            //每个site只保留一个且为最低价
            System.out.println("websiteSet :" + websiteSet.size());
            long startTime = System.nanoTime();   //获取开始时间
            for (CmpProductListVo cmpProductListVo : comparedSkuVos) {
                if (websiteSet.size() <= 0) {
                    break;
                }
                if (websiteSet.contains(cmpProductListVo.getWebsite())) {
                    websiteSet.remove(cmpProductListVo.getWebsite());
                    //去除列表中除此之外的其他此site的数据
                    tempCmpProductListVos.add(cmpProductListVo);
                }
            }
            //移除之前加进列表的所有的sku列表
            comparedSkuVos = null;
            comparedSkuVos = new ArrayList<>();
            //将新的加入的放入到列表中
            System.out.println("tempCmpProductListVos" + tempCmpProductListVos.size());
            comparedSkuVos.addAll(tempCmpProductListVos);
            long endTime = System.nanoTime(); //获取结束时间
            System.out.println("total time is " + (endTime - startTime) / 1000000 + "");
        }
        String currentDeeplink = "";
        try {
            if (ptmCmpSkuIndex2 != null && ptmCmpSkuIndex2.getId() > 0) {
                if (ptmCmpSkuIndex2.getWebsite().equals(sio.getCliSite())) {
                    System.out.println(" enter ptmCmpSkuIndex2 get deepLink ");
                    currentDeeplink = WebsiteHelper.getDeeplinkWithAff(ptmCmpSkuIndex2.getWebsite(), ptmCmpSkuIndex2.getUrl(), new String[]{sio.getMarketChannel().name(), sio.getDeviceId()});
                    System.out.println("currentDeeplink1  " + currentDeeplink);
                }
            } else if (clientCmpSku != null) {
                if (!cmpSkuCacheManager.isFlowControlled(sio.getDeviceId(), sio.getCliSite())) {
                    System.out.println(" enter clientCmpSku get deepLink ");
                    System.out.println(" sku id is " + clientCmpSku.getId());
                    System.out.println("  clientCmpSku.getSkuTitle()  : " + clientCmpSku.getSkuTitle());
                    System.out.println("   sio.getCliQ() : " + sio.getCliQ());
                    System.out.println(" clientCmpSku.getPrice() :   " + clientCmpSku.getPrice());
                    System.out.println("  cliPrice  " + cliPrice);
                    if (clientCmpSku.getSkuTitle().equalsIgnoreCase(sio.getCliQ()) && clientCmpSku.getPrice() == cliPrice) {
                        currentDeeplink = WebsiteHelper.getDeeplinkWithAff(clientCmpSku.getWebsite(), clientCmpSku.getUrl(), new String[]{sio.getMarketChannel().name(), sio.getDeviceId()});
                        System.out.println("currentDeeplink2  " + currentDeeplink);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("get deepLink failed ");
        }
        //取与客户端所传商品同一个site的sku作为sku匹配sku
        cmpResult.setProductVo(new ProductVo(sio.getHsProId(), sio.getCliQ(), productCacheManager.getProductMasterImageUrl(sio.getHsProId()), 0.0f, currentDeeplink));
        System.out.println("comparedSkuVos" + comparedSkuVos.size());
        cmpResult.setPriceList(comparedSkuVos);
        cmpResult.setCopywriting("Searched across Flipkart,Snapdeal,Paytm & 6 other apps to get the best deals for you.");
        return cmpResult;
    }

    private void addVo(List<ComparedSkuVo> comparedSkuVos, ComparedSkuVo comparedSkuVo) {
        if (comparedSkuVo == null || comparedSkuVo.getPrice() <= 0) {
            return;
        }
        comparedSkuVos.add(comparedSkuVo);
    }

    public boolean isPriceOffAlert(String userToken, Long skuId) {
        if (!StringUtils.isEmpty(userToken)) {
            System.out.println("userToken is :" + userToken);
            UrmUser urmUser = appService.getUserByUserToken(userToken);
            if (urmUser != null) {
                System.out.println("this userToken has user ");
                PriceOffNotice priceOffNotice = iPriceOffNoticeService.getPriceOffNotice(urmUser.getId() + "", skuId);
                if (priceOffNotice != null) {
                    return true;
                }
            }
        }
        return false;
    }
}