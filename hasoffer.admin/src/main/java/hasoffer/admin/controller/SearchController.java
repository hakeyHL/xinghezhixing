package hasoffer.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import hasoffer.admin.controller.vo.CategoryVo;
import hasoffer.admin.controller.vo.CmpSkuVo;
import hasoffer.admin.controller.vo.ProductVo;
import hasoffer.admin.controller.vo.SearchResultCountVo;
import hasoffer.admin.manager.SearchLogManager;
import hasoffer.base.model.PageModel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.cache.SearchLogCacheManager;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.mongo.SrmAutoSearchResult;
import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.search.SrmProductSearchStat;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.core.persistence.po.sys.SysAdmin;
import hasoffer.core.product.ICategoryService;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.product.IFetchService;
import hasoffer.core.product.IProductService;
import hasoffer.core.product.solr.ProductIndexServiceImpl;
import hasoffer.core.search.ISearchService;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.core.ISummaryProductProcessor;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.helper.WebsiteProcessorFactory;
import hasoffer.fetch.helper.WebsiteSummaryProductProcessorFactory;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.model.Product;
import hasoffer.webcommon.context.Context;
import hasoffer.webcommon.context.StaticContext;
import hasoffer.webcommon.helper.PageHelper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date : 2016/1/8
 * Function :
 */
@Controller
@RequestMapping(value = "/s")
public class SearchController {

    @Resource
    IProductService productService;
    @Resource
    ProductIndexServiceImpl productIndexService;
    @Resource
    ISearchService searchService;
    @Resource
    ICategoryService categoryService;
    @Resource
    SearchLogManager searchLogManager;
    @Resource
    IFetchService fetchService;
    @Resource
    ICmpSkuService cmpSkuService;
    @Resource
    SearchLogCacheManager logCacheManager;
    @Resource
    IMongoDbManager mdm;

    @RequestMapping(value = "/rematch/{logId}", method = RequestMethod.GET)
    public ModelAndView rematch(@PathVariable String logId) {
        ModelAndView mav = new ModelAndView();

        try {

            SrmAutoSearchResult autoSearchResult = mdm.queryOne(SrmAutoSearchResult.class, logId);

            searchService.analysisAndRelate(autoSearchResult);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mav.addObject("result", "ok");

        return mav;
    }

    @RequestMapping(value = "/showmatch/{logId}", method = RequestMethod.GET)
    public ModelAndView showmatch(@PathVariable String logId) {
        ModelAndView mav = new ModelAndView("search/match_result");

        SrmAutoSearchResult autoSearchResult = mdm.queryOne(SrmAutoSearchResult.class, logId);

        mav.addObject("result", autoSearchResult);

        return mav;
    }

    @RequestMapping(value = "/showstat", method = RequestMethod.GET)
    public ModelAndView showstat() {
        ModelAndView mav = new ModelAndView("showstat/cmpresult");

        List<SrmProductSearchStat> searchStats = searchService.findSearchCountStats();

        List<SearchResultCountVo> searchResultCountVos = new ArrayList<SearchResultCountVo>();
        for (SrmProductSearchStat searchStat : searchStats) {
            searchResultCountVos.add(new SearchResultCountVo(searchStat));
        }

        mav.addObject("stats", searchResultCountVos);

        return mav;
    }

    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    public
    @ResponseBody
    String statSearchCount(@RequestParam String ymd) {
        if (StringUtils.isEmpty(ymd)) {
            ymd = TimeUtils.parse(TimeUtils.yesterday(), "yyyyMMdd");
        }

        searchService.saveSearchCount(ymd);

        productService.expTopSellingsFromSearchCount(ymd);

        searchService.statSearchCount(ymd);

        return "ok";
    }

    @RequestMapping(value = "/stat2", method = RequestMethod.GET)
    public
    @ResponseBody
    String statSearchCount2(@RequestParam String ymd) {
        if (StringUtils.isEmpty(ymd)) {
            ymd = TimeUtils.parse(TimeUtils.yesterday(), "yyyyMMdd");
        }

        searchService.statSearchCount(ymd);

        return "ok";
    }

    @RequestMapping(value = "/result/ok", method = RequestMethod.POST)
    public ModelAndView resultOk(HttpServletRequest request) {

        String logId = request.getParameter("logId");
        String cmpSkuIdStr = request.getParameter("cmpSkuId");
        String skuUrl = request.getParameter("skuUrl");

        long cmpSkuId = Long.valueOf(cmpSkuIdStr);
        if (cmpSkuId == 0 && StringUtils.isEmpty(skuUrl)) {
            throw new InvalidParameterException();
        }

        searchService.setResultPreciseOk(logId, cmpSkuId, skuUrl);

        ModelAndView mav = new ModelAndView();

        //更新状态


        return mav;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request,
                             @RequestParam(defaultValue = "0") int queryType,
                             @RequestParam(defaultValue = "time_desc") String sort,
                             @RequestParam(defaultValue = "") String createDate,
                             @RequestParam(defaultValue = "ALL") String precise,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "20") int size) {
        boolean byDay = false;

        ModelAndView mav = new ModelAndView("search/list");

        final String DATE_PATTERN_FROM_WEB = "MM/dd/yyyy";

        Date startTime, endTime;
        if (StringUtils.isEmpty(createDate)) {
            startTime = TimeUtils.stringToDate("01/18/2016", DATE_PATTERN_FROM_WEB);// 2016-1-18 第一条搜索记录
            endTime = new Date();
        } else {
            startTime = TimeUtils.stringToDate(createDate, DATE_PATTERN_FROM_WEB);
            endTime = TimeUtils.addDay(startTime, 1);
            byDay = true;
        }

        PageableResult<SrmSearchLog> pagedSearchLogs = searchService.listSearchLogs(queryType, sort, startTime, endTime, precise, page, size);

        PageModel pageModel = PageHelper.getPageModel(request, pagedSearchLogs);

        mav.addObject("searchLogs", searchLogManager.getSearchLogs(pagedSearchLogs.getData()));
        mav.addObject("page", pageModel);
        mav.addObject("byDay", byDay);

        if (byDay) {
            mav.addObject("date", TimeUtils.parse(startTime, DATE_PATTERN_FROM_WEB));
        } else {
            mav.addObject("date", TimeUtils.parse(TimeUtils.today(), DATE_PATTERN_FROM_WEB));
        }

        return mav;
    }

    /*private List<SearchLogVo> getSearchLogs(List<SrmSearchLog> logs) {
        List<SearchLogVo> searchLogVos = new ArrayList<SearchLogVo>();

        if (ArrayUtils.hasObjs(logs)) {
            for (SrmSearchLog srmSearchLog : logs) {
                String title = "";
                if (srmSearchLog.getPtmProductId() > 0) {
                    PtmProduct ptmProduct = dbm.get(PtmProduct.class, srmSearchLog.getPtmProductId());
                    if (ptmProduct != null) {
                        title = ptmProduct.getTitle();
                    }
                }

                List<PtmCategory> categories = categoryService.getRouterCategoryList(srmSearchLog.getCategory());

                searchLogVos.add(new SearchLogVo(srmSearchLog, title, CategoryHelper.getCategoryVos(categories)));
            }
        }

        return searchLogVos;
    }*/


    /**
     * 跳转到手动添加页面
     */
    @RequestMapping(value = "/reSearchByLogKeyword/{id}", method = RequestMethod.GET)
    public ModelAndView reSearchByLogKeyword(HttpServletRequest request,
                                             @PathVariable String id,
                                             @RequestParam(required = false) String title,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "20") int size) {

        ModelAndView mav = null;

        PageableResult pagedResults = null;
        List<PtmProduct> products = new ArrayList<PtmProduct>();
        PageModel pageModel = null;
        String website = null;
        boolean hasRelateProduct = false;

        SrmSearchLog srmSearchLog = searchService.findSrmSearchLogById(id);

        //拿到已匹配的商品
        long ptmProductId = srmSearchLog.getPtmProductId();
        PtmProduct firstProduct = productService.getProduct(ptmProductId);
        if (firstProduct != null) {
            products.add(firstProduct);
            hasRelateProduct = true;
        }

        //根据传过来的keyword再查询
        if (StringUtils.isEmpty(title)) {
            title = srmSearchLog.getKeyword();
        }
        pagedResults = productIndexService.searchPro(0, 0, 0, title, page, size);
        List<PtmProduct> indexProducts = productService.getProducts(pagedResults.getData());
        indexProducts.remove(firstProduct);

        products.addAll(indexProducts);

        mav = new ModelAndView("search/listByKeyword");

        pageModel = PageHelper.getPageModel(request, pagedResults);

        website = srmSearchLog.getSite();

        mav.addObject("products", getProductVos(products));
        mav.addObject("page", pageModel);
        mav.addObject("keyword", title);
        mav.addObject("clientPrice", srmSearchLog.getPrice());
        mav.addObject("website", website);
        mav.addObject("srmSearchLogId", id);
        mav.addObject("srmSearchLogPrice", srmSearchLog.getPrice());
        mav.addObject("hasRelateProduct", hasRelateProduct);

        return mav;
    }

    private List<ProductVo> getProductVos(List<PtmProduct> products) {
        List<ProductVo> productVos = new ArrayList<ProductVo>();
        if (ArrayUtils.isNullOrEmpty(products)) {
            return productVos;
        }

        for (PtmProduct product : products) {
            List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(product.getId());

            if (ArrayUtils.isNullOrEmpty(cmpSkus)) {
                System.out.println(product.getId());
                productVos.add(getProductVo(product, 0, 0, 0));
                continue;
            }

            int skuCount = cmpSkus.size();
            float min = cmpSkus.get(0).getPrice(), max = min;

            for (PtmCmpSku cmpSku : cmpSkus) {
                float price = cmpSku.getPrice();
                if (price < min) {
                    min = price;
                }
                if (price > max) {
                    max = price;
                }
            }
            productVos.add(getProductVo(product, min, max, skuCount));
        }
        return productVos;
    }

    private ProductVo getProductVo(PtmProduct p, float min, float max, int skuCount) {
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

        vo.setMinPrice(min);
        vo.setMaxPrice(max);

        vo.setSkuCount(skuCount);
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
     * ajax请求，通过url抓取商品信息
     *
     * @param request
     * @return 返回商品的json格式数据
     */
    @RequestMapping(value = "/getProduct", method = RequestMethod.POST)
    @ResponseBody
    public List<ProductVo> getProduct(HttpServletRequest request) {

        String parameters = request.getParameter("parameters");
        JSONArray jsonArray = JSON.parseArray(parameters);
        List<ProductVo> productList = new ArrayList<ProductVo>();

        //都传上来的新增比价的url进行website去重复
        List<Website> websiteList = new ArrayList<Website>();

        for (int i = 0; i < jsonArray.size(); i++) {
            String urlWithRef = (String) jsonArray.get(i);

            if (urlWithRef == null || urlWithRef.length() == 0) {
                continue;
            }

            String url = "";

            if (urlWithRef.contains("&ref")) {
                url = urlWithRef.split("&ref")[0];
            } else if (urlWithRef.contains("/ref")) {
                url = urlWithRef.split("/ref")[0];
            } else {
                url = urlWithRef;
            }

            //调用服务，抓取product
            Website webSite = WebsiteHelper.getWebSite(url);
            //将解析出来的website放入websit集合，如果存在，跳过，不存在，新增
            if (websiteList.contains(webSite)) {
                continue;
            } else {
                websiteList.add(webSite);
            }
            ISummaryProductProcessor summaryProductProcessor = WebsiteSummaryProductProcessorFactory.getSummaryProductProcessor(webSite);
            if (summaryProductProcessor != null && url != null) {
                OriFetchedProduct oriFetchedProduct = null;
                ProductVo productVo = new ProductVo();
                try {
                    oriFetchedProduct = summaryProductProcessor.getSummaryProductByUrl(url);
                } catch (Exception e) {
                    productVo.setFlag("false");
                    productVo.setWebsite(webSite);
                    productVo.setPrice(0.0f);
                    productList.add(productVo);
                    continue;
                }

                productVo.setFlag("true");
                productVo.setSourceId(oriFetchedProduct.getSourceSid());
                productVo.setPrice(oriFetchedProduct.getPrice());
                productVo.setTitle(oriFetchedProduct.getTitle());
                productVo.setSourceSite(oriFetchedProduct.getUrl());
                productVo.setWebsite(webSite);
                productList.add(productVo);
            }
        }
        return productList;
    }


    /**
     * 手动匹配方式
     * 1.根据SrmSearchLogId更新当前记录的Precise为Manualset
     *
     * @return
     */
    @RequestMapping(value = "/getCmpSku", method = RequestMethod.POST)
    @ResponseBody
    public List<CmpSkuVo> getCmpSku(HttpServletRequest request) {

        long productId = Long.parseLong(request.getParameter("productId"));
        String srmSearchLogId = request.getParameter("srmSearchLogId");
        SrmSearchLog srmSearchLog = searchService.findSrmSearchLogById(srmSearchLogId);
        String keywords = srmSearchLog.getKeyword();
        List<CmpSkuVo> cmpSkuVoList = new ArrayList<CmpSkuVo>();

        //获取比较列表
        PageableResult<PtmCmpSku> pagedCmpSkus = productService.listPagedCmpSkus(productId, 1, Integer.MAX_VALUE);
        //给定一个默认的比价列表集合,如果修改注意修改listByKeyword页面中js读取数组大小的值
        List<Website> websiteList = new ArrayList<Website>();
        websiteList.add(Website.FLIPKART);
        websiteList.add(Website.SNAPDEAL);
        websiteList.add(Website.AMAZON);
        websiteList.add(Website.EBAY);
        websiteList.add(Website.SHOPCLUES);
        websiteList.add(Website.PAYTM);

        //返回比较列表中的数据，同时与主流网站进行对比排除操作
        List<PtmCmpSku> cmpSkus = pagedCmpSkus.getData();
        for (PtmCmpSku ptmCmpSku : cmpSkus) {
            Website website = ptmCmpSku.getWebsite();
            if (website == null) {
                continue;
            }
            CmpSkuVo cmpSkuVo = new CmpSkuVo();
            if (websiteList.contains(website)) {
                cmpSkuVo.setWebsite(website);
                cmpSkuVo.setFlag("exists");
                cmpSkuVo.setUrl(ptmCmpSku.getOriUrl());
                websiteList.remove(website);
            } else {
                cmpSkuVo.setWebsite(website);
                cmpSkuVo.setFlag("exists");
                cmpSkuVo.setUrl(ptmCmpSku.getOriUrl());
            }
            cmpSkuVoList.add(cmpSkuVo);
        }

        //返回主流网站的剩余信息
        if (websiteList.size() > 0) {
            for (Website website : websiteList) {
                String searchUrl = WebsiteHelper.getSearchUrl(website, keywords);
                CmpSkuVo cmpSkuVo = new CmpSkuVo();
                cmpSkuVo.setFlag("none");
                cmpSkuVo.setWebsite(website);
                cmpSkuVo.setUrl(searchUrl);
                cmpSkuVoList.add(cmpSkuVo);
            }
        }

        return cmpSkuVoList;
    }


    /**
     * 在手动匹配页面判断过程中新增一条或多条cmpSku记录
     *
     * @param request
     */
    @RequestMapping(value = "/addCmpSkus", method = RequestMethod.POST)
    public ModelAndView addCmpSku(HttpServletRequest request) {

        //获取productId
        String newPtmProductId = request.getParameter("productId");
        long ptmProductId = Long.parseLong(newPtmProductId);
        SysAdmin admin = (SysAdmin) Context.currentContext().get(StaticContext.USER);

        //如果有srmSearchLogId，更新srmSearchLog
        String willUpdateSrmSearchLogId = request.getParameter("srmSearchLogId");
        if (willUpdateSrmSearchLogId != null && !StringUtils.isEmpty(willUpdateSrmSearchLogId)) {

            SrmSearchLog srmSearchLog = searchService.findSrmSearchLogById(willUpdateSrmSearchLogId);
            long oldValue = srmSearchLog.getPtmProductId();
            //修改日志的关联状态
            searchService.setResultPreciseManualset(willUpdateSrmSearchLogId);
            //如果新值和旧的值一样，认为只是添加比价列表，不进行添加更新log的日志记录
            if (oldValue != ptmProductId) {
                searchService.setPtmProductId(willUpdateSrmSearchLogId, ptmProductId);
            }
        }

        //新增比较信息
        String[] websites = request.getParameterValues("website");
        String[] prices = request.getParameterValues("price");
        String[] urls = request.getParameterValues("url");

        if (websites != null && prices != null && urls != null) {
            if (websites.length != 0 && prices.length != 0 && urls.length != 0) {

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < websites.length; i++) {
                    float price = 0.0f;
                    if (NumberUtils.isNumber(prices[i])) {
                        price = Float.valueOf(prices[i]);
                    }
                    //todo  size，color
                    String color = "";
                    String size = "";

                    //新增CmpSku
                    PtmCmpSku cmpSku = cmpSkuService.createCmpSku(ptmProductId, urls[i], color, size, price);
                    if (i != websites.length - 1) {
                        stringBuilder.append(cmpSku.getId() + ",");
                    } else {
                        stringBuilder.append(cmpSku.getId());
                    }

                }
            }
        }


        return new ModelAndView("redirect:/p/cmp/" + ptmProductId);
    }

    /**
     * 跳转到手动新增的页面
     *
     * @return
     */
    @RequestMapping(value = "/toManualCorrelation/{srmSearchLogId}", method = RequestMethod.GET)
    public ModelAndView toManualCorrelation(@PathVariable String srmSearchLogId) {
        ModelAndView modelAndView = new ModelAndView("search/manualCorrelation");

        SrmSearchLog srmSearchLog = searchService.findSrmSearchLogById(srmSearchLogId);
        String keyword = srmSearchLog.getKeyword();

        String flipkartSearchUrl = WebsiteHelper.getSearchUrl(Website.FLIPKART, keyword);
        String snapdealSearchUrl = WebsiteHelper.getSearchUrl(Website.SNAPDEAL, keyword);
        String amazonSearchUrl = WebsiteHelper.getSearchUrl(Website.AMAZON, keyword);
        String ebaySearchUrl = WebsiteHelper.getSearchUrl(Website.EBAY, keyword);
        String shopcluesSearchUrl = WebsiteHelper.getSearchUrl(Website.SHOPCLUES, keyword);
        String paytmearchUrl = WebsiteHelper.getSearchUrl(Website.PAYTM, keyword);

        modelAndView.addObject("flipkartSearchUrl", flipkartSearchUrl);
        modelAndView.addObject("snapdealSearchUrl", snapdealSearchUrl);
        modelAndView.addObject("amazonSearchUrl", amazonSearchUrl);
        modelAndView.addObject("ebaySearchUrl", ebaySearchUrl);
        modelAndView.addObject("shopcluesSearchUrl", shopcluesSearchUrl);
        modelAndView.addObject("paytmearchUrl", paytmearchUrl);
        modelAndView.addObject("srmSearchLogId", srmSearchLogId);
        modelAndView.addObject("keywords", keyword);

        return modelAndView;
    }

    /**
     * 检查抓取的网站是否支持
     * 存在以下情况
     * 1.网址格式不对
     * 2.网址不支持
     * 3.网址支持
     *
     * @return
     */
    @RequestMapping(value = "/checkWebsite", method = RequestMethod.POST)
    @ResponseBody
    public HashMap<String, String> checkWebsite(HttpServletRequest request) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        String url = request.getParameter("urlString");
        final String regex = "(https?|http):\\/\\/([A-z0-9]+[_\\-]?[A-z0-9]+\\.)*[A-z0-9]+\\-?[A-z0-9]+\\.[A-z]{2,}(\\/.*)*\\/?";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.matches()) {

            Website webSite = WebsiteHelper.getWebSite(url);

            if (WebsiteProcessorFactory.webSiteListProcessorMap.containsKey(webSite)) {
                hashMap.put("website", "success");
            } else if (WebsiteSummaryProductProcessorFactory.websiteSummaryProcessor.containsKey(webSite)) {
                hashMap.put("website", "success");
            } else {
                hashMap.put("website", "该url对应的网站暂不支持解析");
            }
        } else {
            //网站格式不对
            hashMap.put("website", "请输入正确的网站格式");
        }

        return hashMap;
    }

    @RequestMapping(value = "/getProductInfo", method = RequestMethod.POST)
    @ResponseBody
    public ProductVo getProductInfo(HttpServletRequest request) {

        String url = request.getParameter("parameter");

        Website website = WebsiteHelper.getWebSite(url);

        IProductProcessor productProcessor = WebsiteProcessorFactory.getProductProcessor(website);

        ProductVo productVo = new ProductVo();
        productVo.setFlag("false");
        productVo.setWebsite(website);
        productVo.setSourceSite(url);
        productVo.setPrice(0.0f);
        if (productProcessor != null) {
            Product product = null;
            try {
                product = productProcessor.parseProduct(url);
            } catch (Exception e) {
                return productVo;
            }

            productVo.setFlag("true");
            productVo.setTitle(product.getProductTitle());
            productVo.setPrice(product.getCurrentPrice());
            productVo.setMasterImageUrl(product.getImages().get(0).getDefaultImageUrl());
            productVo.setDescription(product.getDescription());
        }
        return productVo;
    }
}
