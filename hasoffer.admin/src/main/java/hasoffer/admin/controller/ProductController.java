package hasoffer.admin.controller;

import hasoffer.admin.common.chart.ChartHelper;
import hasoffer.admin.controller.vo.CategoryVo;
import hasoffer.admin.controller.vo.CmpSkuVo;
import hasoffer.admin.controller.vo.ProductVo;
import hasoffer.base.model.PageModel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.JSONUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.mongo.PtmCmpSkuLog;
import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.sys.SysAdmin;
import hasoffer.core.product.ICategoryService;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.product.IFetchService;
import hasoffer.core.product.IProductService;
import hasoffer.core.product.exception.ProductNotFoundException;
import hasoffer.core.product.solr.CmpskuIndexServiceImpl;
import hasoffer.core.product.solr.ProductIndexServiceImpl;
import hasoffer.core.redis.ICacheService;
import hasoffer.core.search.ISearchService;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.webcommon.context.Context;
import hasoffer.webcommon.context.StaticContext;
import hasoffer.webcommon.helper.PageHelper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created on 2015/12/22.
 */
@Controller
@RequestMapping(value = "/p")
public class ProductController {

    @Resource
    ISearchService searchService;
    @Resource
    ICategoryService categoryService;
    @Resource
    IProductService productService;
    @Resource
    ProductIndexServiceImpl productIndexService;
    @Resource
    CmpskuIndexServiceImpl cmpskuIndexService;
    @Resource
    ICmpSkuService cmpSkuService;
    @Resource
    IFetchService fetchService;
    @Resource
    ICacheService cacheServiceImpl;

    @RequestMapping(value = "/cmp/del/{id}", method = RequestMethod.GET)
    public ModelAndView delCompare(@PathVariable long id) {

        PtmCmpSku cmpSku = cmpSkuService.getCmpSkuById(id);
        if (cmpSku == null) {
            cmpskuIndexService.remove(String.valueOf(id));
        } else {
            cmpSkuService.deleteCmpSku(id);

            productService.reimport2Solr(cmpSku.getProductId());
        }

        ModelAndView mav = new ModelAndView();
        return mav;
    }

    @RequestMapping(value = "/cmp/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Map updateCompare(@PathVariable long id) {

        Map<String, String> map = new HashMap<String, String>();

        PtmCmpSku ptmCmpSku = cmpSkuService.getCmpSkuById(id);

        String url = ptmCmpSku.getUrl();

        OriFetchedProduct oriFetchedProduct = null;
        try {
            oriFetchedProduct = fetchService.fetchSummaryProductByUrl(url);
            if (oriFetchedProduct != null) {
                cmpSkuService.updateCmpSkuByOriFetchedProduct(id, oriFetchedProduct);
                map.put("status", "success");
            } else {
                map.put("status", "fail");
            }
        } catch (Exception e) {
            map.put("status", "fail");
        }

        return map;
    }

    @RequestMapping(value = "/cmp/save", method = RequestMethod.POST)
    public ModelAndView saveCompare(HttpServletRequest request) {

        String id = request.getParameter("id");
        String productId = request.getParameter("productId");
        String url = request.getParameter("url");

        float price = 0.0f;
        String priceStr = request.getParameter("price");
        if (NumberUtils.isNumber(priceStr)) {
            price = Float.valueOf(priceStr);
        }

        String color = request.getParameter("color");
        String size = request.getParameter("size");

        if (!StringUtils.isEmpty(id)) {
            // 更新
            cmpSkuService.updateCmpSku(Long.valueOf(id), url, color, size, price);
        } else {
            // 创建
            cmpSkuService.createCmpSku(Long.valueOf(productId), url, color, size, price);
        }
        ModelAndView mav = new ModelAndView("redirect:/p/cmp/" + productId);
        return mav;
    }

    @RequestMapping(value = "/cmp/{id}", method = RequestMethod.GET)
    public ModelAndView listCompares(@PathVariable long id) throws ProductNotFoundException {
        ModelAndView mav = new ModelAndView("product/cmp");
        mav.addObject("pId", id);
        PtmProduct product = productService.getProduct(id);

        if (product == null) {
            throw new ProductNotFoundException(id + "");
        }
        PageableResult<PtmCmpSku> pagedCmpSkus = productService.listPagedCmpSkus(id, 1, Integer.MAX_VALUE);
        List<PtmCmpSku> cmpSkus = pagedCmpSkus.getData();

        Set<String> colors = new HashSet<String>();
        Set<String> sizes = new HashSet<String>();

        List<CmpSkuVo> cmpSkuVos = new ArrayList<CmpSkuVo>();
        Map<PtmCmpSku, List<PtmCmpSkuLog>> priceLogMap = new HashMap<PtmCmpSku, List<PtmCmpSkuLog>>();

        if (ArrayUtils.hasObjs(cmpSkus)) {
            for (PtmCmpSku cmpSku : cmpSkus) {
                cmpSkuVos.add(new CmpSkuVo(cmpSku));

                if (!StringUtils.isEmpty(cmpSku.getColor())) {
                    colors.add(cmpSku.getColor());
                }
                if (!StringUtils.isEmpty(cmpSku.getSize())) {
                    sizes.add(cmpSku.getSize());
                }

                List<PtmCmpSkuLog> logs = null;//cmpSkuService.listByPcsId(cmpSku.getId());
                if (ArrayUtils.hasObjs(logs)) {
                    priceLogMap.put(cmpSku, logs);
                }
            }
        }
        mav.addObject("cmpSkus", cmpSkuVos);
        mav.addObject("product", getProductVo(product));

        mav.addObject("skuColors", JSONUtil.toJSON(colors));
        mav.addObject("skuSizes", JSONUtil.toJSON(sizes));

        List<String> days = new ArrayList<String>();
        Map<Website, List<Float>> priceMap = new HashMap<Website, List<Float>>();
        getPriceLogs(priceLogMap, days, priceMap);
        mav.addObject("priceMap", JSONUtil.toJSON(ChartHelper.getChartData(priceMap)));
        mav.addObject("priceDays", JSONUtil.toJSON(days));
        mav.addObject("showCharts", ArrayUtils.hasObjs(days));

        return mav;
    }

    private void getPriceLogs(Map<PtmCmpSku, List<PtmCmpSkuLog>> priceLogMap, List<String> days, Map<Website, List<Float>> priceMap) {

        if (priceLogMap == null || priceLogMap.size() <= 0) {
            return;
        }

        // 先分析价格的日期区间
        final String DATE_PATTERN = "yyyyMMdd";
        String startDay = "30000000", endDay = "00000000";

        Map<Website, Map<String, Float>> priceLogMap2 = new HashMap<Website, Map<String, Float>>();

        for (Map.Entry<PtmCmpSku, List<PtmCmpSkuLog>> kv : priceLogMap.entrySet()) {
            PtmCmpSku cmpSku = kv.getKey();
            List<PtmCmpSkuLog> logs = kv.getValue();

            Map<String, Float> subMap = new HashMap<String, Float>();

            for (PtmCmpSkuLog log : logs) {
                String ymd = TimeUtils.parse(log.getPriceTime(), DATE_PATTERN);
                subMap.put(ymd, log.getPrice());

                if (startDay.compareTo(ymd) > 0) {
                    startDay = ymd;
                }
                if (endDay.compareTo(ymd) < 0) {
                    endDay = ymd;
                }
            }

            String ymd = TimeUtils.parse(cmpSku.getUpdateTime(), DATE_PATTERN);
            if (!subMap.containsKey(ymd)) {
                subMap.put(ymd, cmpSku.getPrice());

                if (endDay.compareTo(ymd) < 0) {
                    endDay = ymd;
                }
            }

            priceLogMap2.put(cmpSku.getWebsite(), subMap);
        }

        TimeUtils.fillDays(days, startDay, endDay, DATE_PATTERN);
        fillPriceMap(priceLogMap2, priceMap, days);
    }

    private void fillPriceMap(Map<Website, Map<String, Float>> priceLogMap, Map<Website, List<Float>> priceMap, List<String> days) {
        for (Map.Entry<Website, Map<String, Float>> kv : priceLogMap.entrySet()) {
            Website website = kv.getKey();

            if (website == null) {
                continue;
            }

            Map<String, Float> logMap = kv.getValue();
            List<Float> prices = new ArrayList<Float>();

            for (String ymd : days) {
                Float price = logMap.get(ymd);
                if (price == null) {
                    price = 0.0F;
                }
                prices.add(price);
            }
            priceMap.put(website, prices);
        }
    }


    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ModelAndView listProducts(@PathVariable long id) throws ProductNotFoundException {

        ModelAndView mav = new ModelAndView("product/detail");

        PtmProduct product = productService.getProduct(id);
        if (product == null) {
            throw new ProductNotFoundException();
        }

        mav.addObject("product", getProductVo(product));
        mav.addObject("imageUrls", productService.getProductImageUrls(id));
        mav.addObject("features", new ArrayList<>());
        mav.addObject("basicAttributes", new ArrayList<>());

        return mav;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView listProducts(HttpServletRequest request,
                                     @RequestParam(required = false) String title,
                                     @RequestParam(defaultValue = "0") int category1,
                                     @RequestParam(defaultValue = "0") int category2,
                                     @RequestParam(defaultValue = "0") int category3,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "20") int size) {

        ModelAndView mav = new ModelAndView("product/list");

        PageableResult pagedResults = null;
        List<PtmProduct> products = null;
        PageModel pageModel = null;

        pagedResults = productIndexService.searchPro(category1, category2, category3, title, page, size);

        products = productService.getProducts(pagedResults.getData());

        pageModel = PageHelper.getPageModel(request, pagedResults);

        mav.addObject("products", getProductVos(products));
        mav.addObject("page", pageModel);

        return mav;
    }

    @RequestMapping(value = "/updateTag", method = RequestMethod.POST)
    public ModelAndView updateTag(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("product/list");

        String proId = request.getParameter("id");
        String tag = request.getParameter("tag");

        productService.updateProductTag(proId, tag);

        mav.addObject("result", "ok");

        return mav;
    }

    private List<ProductVo> getProductVos(List<PtmProduct> products) {
        List<ProductVo> productVos = new ArrayList<ProductVo>();
        if (ArrayUtils.isNullOrEmpty(products)) {
            return productVos;
        }

        for (PtmProduct product : products) {
            productVos.add(getProductVo(product));
        }
        return productVos;
    }

    private ProductVo getProductVo(PtmProduct p) {
        ProductVo vo = new ProductVo();

        vo.setId(p.getId());
        vo.setCreateTime(p.getCreateTime());

        vo.setCategories(getCategories(p.getCategoryId()));

        vo.setTitle(p.getTitle());
        vo.setTag(p.getTag());
        vo.setPrice(p.getPrice());

        vo.setMasterImageUrl(productService.getProductMasterImageUrl(p.getId()));
        vo.setDescription(p.getDescription());

        vo.setColor(p.getColor());
        vo.setSize(p.getSize());
        vo.setRating(p.getRating());

        vo.setSourceSite(p.getSourceSite());
        vo.setSourceId(p.getSourceId());

        return vo;
    }

    private List<CategoryVo> getCategories(long categoryId) {
        List<CategoryVo> categoryVos = new ArrayList<CategoryVo>();
        List<PtmCategory> ptmCategories = categoryService.getRouterCategoryList(categoryId);

        if (ArrayUtils.hasObjs(ptmCategories)) {
            for (PtmCategory category : ptmCategories) {
                categoryVos.add(new CategoryVo(category));
            }
        }

        return categoryVos;
    }

    /**
     * 新增PtmProduct
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public PtmProduct createProduct(HttpServletRequest request, @RequestParam(defaultValue = "0") int category3) throws UnsupportedEncodingException {

        long catagoryId = category3;
        String data = request.getParameter("data");
        //表单数据是经过serialize的，所以传上来的数据需要经过decode使用，待测
        data = URLDecoder.decode(data);
        String[] subStrs1 = data.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String str : subStrs1) {
            String[] subStrs2 = str.split("=");
            if (subStrs2.length == 1) {
                map.put(subStrs2[0], "");
            } else {
                map.put(subStrs2[0], subStrs2[1]);
            }
        }
        String url = map.get("url");
        String title = map.get("title");
        String priceString = map.get("price");
        float price = 0.0f;
        if (NumberUtils.isNumber(priceString)) {
            price = Float.parseFloat(priceString);
        }
        String description = map.get("description");
        String colors = map.get("skus");
        String sizes = map.get("sANs");
        String website = map.get("website");
        String sourceId = map.get("sourceId");
        String color = "";
        String size = "";

        //创建PtmProduct
        PtmProduct product = productService.createProduct(catagoryId, title, price, description, colors, sizes, 0, website, sourceId);

        //newProoduct的日志
        SysAdmin admin = (SysAdmin) Context.currentContext().get(StaticContext.USER);

        //添加cmpSku
        PtmCmpSku cmpSku = cmpSkuService.createCmpSku(product.getId(), url, color, size, price);

        return product;
    }

    @RequestMapping(value = "/batchDelete", method = RequestMethod.GET)
    @ResponseBody
    public boolean batchDelete(@RequestParam(value = "ids[]") Long[] ids) {
//        cmpSkuService.batchDeleteCmpSku(ids);

        long productId = 0L;

        for (long id : ids) {
            PtmCmpSku cmpSku = cmpSkuService.getCmpSkuById(id);
            if (cmpSku == null) {
                cmpskuIndexService.remove(String.valueOf(id));
            } else {
                cmpSkuService.deleteCmpSku(id);
            }

            if (productId <= 0) {
                productId = cmpSku.getProductId();
            }
        }

        productService.reimport2Solr(productId);

        ModelAndView mav = new ModelAndView();

        return true;
    }

    @RequestMapping(value = "/removeCache/{productId}", method = RequestMethod.POST)
    @ResponseBody
    public Map removeCache(@PathVariable Long productId) {

        Map<String, String> statusMap = new HashMap<>();

        try {

            //清除商品缓存
            cacheServiceImpl.del("PRODUCT_" + productId);
            //清除sku缓存        PRODUCT__listPagedCmpSkus_3198_1_10
            Set<String> keys = cacheServiceImpl.keys("PRODUCT__listPagedCmpSkus_" + productId + "_*");

            for (String key : keys) {
                cacheServiceImpl.del(key);
            }

            statusMap.put("status", "success");

        } catch (Exception e) {
            statusMap.put("status", "fail");
        }

        return statusMap;
    }
}
