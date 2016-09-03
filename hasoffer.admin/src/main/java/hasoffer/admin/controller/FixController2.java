package hasoffer.admin.controller;

import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.mongo.PriceNode;
import hasoffer.core.persistence.mongo.PtmCmpSkuLog;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.ptm.updater.PtmCmpSkuUpdater;
import hasoffer.core.persistence.po.ptm.updater.PtmProductUpdater;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.product.IProductService;
import hasoffer.core.task.ListProcessTask;
import hasoffer.core.task.worker.ILister;
import hasoffer.core.task.worker.IProcessor;
import hasoffer.fetch.helper.WebsiteHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Date : 2016/3/25
 * Function : 修复商品数据
 * 目前以手机类目开始，fix的项目有：cleanUrl更新、brand标记、sku非相同品牌的删除(修复完成后，更新solr)
 * 1cleanUrl-/fix2/clean_url_sku?cateId=5
 * 2brand1-/fix2/tag_brand?cateId=5
 * 3brand2-/fix2/tag_brand_man 手工处理品牌
 * +4price-/fix2/convert_price_log?start=20160801
 * 5del_sku-/fix2/del_sku_by_brand_mobile 删除品牌不正确的sku
 * 6tag_model - /fix2/tag_model?cateId=5
 */
@Controller
@RequestMapping(value = "/fix2")
public class FixController2 {

    private static Logger logger = LoggerFactory.getLogger(FixController2.class);
    private static Map<String, String> dataMap = new HashMap<>();

    @Resource
    IProductService productService;
    @Resource
    ICmpSkuService cmpSkuService;
    @Resource
    IMongoDbManager mdm;

    @RequestMapping(value = "/tag_model", method = RequestMethod.GET)
    @ResponseBody
    public String tag_model(@RequestParam final long cateId) {
        if (cateId != 5) {
            return "cate id must be 5!";
        }
        ListProcessTask<PtmProduct> ptmCmpSkuListProcessTask = new ListProcessTask<>(
                new ILister() {
                    @Override
                    public PageableResult getData(int page) {
                        return productService.listPagedProducts(cateId, page, 2000);
                    }

                    @Override
                    public boolean isRunForever() {
                        return false;
                    }

                    @Override
                    public void setRunForever(boolean runForever) {

                    }
                },
                new IProcessor<PtmProduct>() {
                    @Override
                    public void process(PtmProduct o) {
                        String pro_title = o.getTitle();
                        String pro_model = o.getModel();
                        if (StringUtils.isEmpty(pro_title)) {
//                            print("no title");
                            return;
                        }

//                        if (!StringUtils.isEmpty(pro_model)) {
////                            print("has model");
//                            return;
//                        }

                        pro_title = pro_title.toLowerCase();
                        String finalModel = "";

                        List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(o.getId());
                        if (StringUtils.isEmpty(pro_model)) {
                            Set<String> modelSet = new HashSet<>();

                            StringBuffer sb = new StringBuffer("===========" + o.getTitle() + "=============\n");

                            for (PtmCmpSku cmpSku : cmpSkus) {

                                String modelStr = cmpSku.getModel();
                                if (!StringUtils.isEmpty(modelStr)) {
                                    modelSet.addAll(Arrays.asList(modelStr.split(",")));
                                }

                                sb.append(String.format("%s : [Brand-%s], [Model-%s], [%s]\n", cmpSku.getWebsite().name(), cmpSku.getBrand(), cmpSku.getModel(), cmpSku.getTitle()));
                            }


                            for (String model : modelSet) {
                                model = model.trim();
                                if (!StringUtils.isEmpty(model) && pro_title.contains(model.toLowerCase())) {
                                    if (model.length() > finalModel.length()) {
                                        finalModel = model;
                                    }
                                }
                            }

//                        print(sb.toString());
                            if (finalModel.length() > 0) {
                                print(pro_title + "\t[" + finalModel + "]");
                                productService.updateProductBrandModel(o.getId(), o.getBrand(), finalModel);
                            }
                        } else {
                            finalModel = pro_model;
                        }

//                        for (PtmCmpSku cmpSku : cmpSkus) {
//                            String skuModel = cmpSku.getModel();
//                            String skuTitle = cmpSku.getTitle();
//                            if (!StringUtils.isEmpty(skuModel)) {
//                                String finalModelx = finalModel.toLowerCase().trim().replaceAll("-", "").replaceAll("\\s", "");
//                                String skuModelx = skuModel.toLowerCase().trim().replaceAll("-", "").replaceAll("\\s", "");
//                                if (finalModelx.equalsIgnoreCase(skuModelx)) {
//
//                                } else {
//                                    print(finalModel + " 1: " + cmpSku.getId() + "\t" + skuTitle + "\t" + skuModelx);
//                                }
//                            } else {
//                                if (!StringUtils.isEmpty(skuTitle)) {
//                                    String skuTitlex = skuTitle.toLowerCase().trim();
//                                    if (!skuTitlex.contains(finalModel.toLowerCase())) {
//                                        print(finalModel + " 2: " + cmpSku.getId() + "\t" + skuTitle);
//                                    }
//                                }
//                            }
//                        }
                    }
                }
        );

        ptmCmpSkuListProcessTask.setProcessorCount(20);
        ptmCmpSkuListProcessTask.setQueueMaxSize(2000);

        ptmCmpSkuListProcessTask.go();
        return "ok";
    }

    @RequestMapping(value = "/del_sku_by_brand_mobile", method = RequestMethod.GET)
    @ResponseBody
    public String del_sku_by_brand_mobile() {
        ListProcessTask<PtmProduct> ptmCmpSkuListProcessTask = new ListProcessTask<>(
                new ILister() {
                    @Override
                    public PageableResult getData(int page) {
                        return productService.listPagedProducts(5L, page, 2000);
                    }

                    @Override
                    public boolean isRunForever() {
                        return false;
                    }

                    @Override
                    public void setRunForever(boolean runForever) {

                    }
                },
                new IProcessor<PtmProduct>() {
                    @Override
                    public void process(PtmProduct o) {
                        String brand = o.getBrand();
                        if (StringUtils.isEmpty(brand)) {
                            return;
                        }

                        brand = brand.trim().toLowerCase();

                        List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(o.getId());

                        int skuSize = cmpSkus.size();

                        for (PtmCmpSku cmpSku : cmpSkus) {
                            boolean del = false;
                            String sTitle = cmpSku.getTitle();
                            if (!StringUtils.isEmpty(sTitle)) {
                                sTitle = sTitle.toLowerCase().trim();
                            }
                            String sBrand = cmpSku.getBrand();
                            if (!StringUtils.isEmpty(sBrand)) {
                                sBrand = sBrand.toLowerCase().trim();
                            }
                            String sModel = cmpSku.getModel();
                            if (!StringUtils.isEmpty(sModel)) {
                                sModel = sModel.toLowerCase().trim();
                            }

                            if (!StringUtils.isEmpty(sBrand)) {
                                if (!sameBrand(brand, sBrand)) {
                                    del = true;
//                                    print(brand + " -" + skuSize + "- " + sBrand);
                                }
                            } else {
                                if (StringUtils.isEmpty(sTitle)) {
                                    del = true;
//                                    print(cmpSku.getId() + " no title!!!");
                                } else {
                                    String ssTitle = sTitle.replaceAll("\\s+", "").replaceAll("-", "");
                                    if (!(sTitle.contains(brand) || ssTitle.contains(brand))) {
//                                        print(brand + " - " + skuSize + " - " + sTitle);
                                        del = true;
                                    }
                                }
                            }

                            if (del) {
                                cmpSkuService.deleteCmpSku(cmpSku.getId());
                            }
                        }

                        productService.importProduct2Solr2(o);
                    }
                }
        );

        ptmCmpSkuListProcessTask.setQueueMaxSize(3000);
        ptmCmpSkuListProcessTask.setProcessorCount(20);

        ptmCmpSkuListProcessTask.go();

        return "ok";
    }

    private boolean sameBrand(String brand, String sBrand) {
        if (brand.equalsIgnoreCase(sBrand) || sBrand.contains(brand) || brand.contains(sBrand)) {
            return true;
        }

        String brand2 = brand.replaceAll("\\s+", "").replaceAll("-", "");
        String sBrand2 = sBrand.replaceAll("\\s+", "").replaceAll("-", "");
        if (brand2.equalsIgnoreCase(sBrand2) || sBrand2.contains(brand2) || brand2.contains(sBrand2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 转换价格历史数据格式
     */
    @RequestMapping(value = "/convert_price_log", method = RequestMethod.GET)
    @ResponseBody
    public String convert_price_log(@RequestParam String start) {
        Date startD = TimeUtils.stringToDate(start, "yyyyMMdd");
        Date endD = TimeUtils.addDay(startD, 1);
        final ProcessDate pd = new ProcessDate(startD, endD);

        final Set<Long> idSet = new HashSet<>();
        final AtomicInteger count = new AtomicInteger(0);

        final ConcurrentHashMap<Long, ConcurrentHashMap<String, PriceNode>> historyPriceMap = new ConcurrentHashMap<>();

        ListProcessTask<PtmCmpSkuLog> listAndProcessTask2 = new ListProcessTask<>(
                new ILister<PtmCmpSkuLog>() {
                    @Override
                    public PageableResult<PtmCmpSkuLog> getData(int page) {
                        print("date=" + TimeUtils.parse(pd.startDate, "yyyyMMdd") + ", page=" + page + ", count=" + count.get() + ", id set=" + idSet.size());
                        Query query = new Query(Criteria.where("priceTime").gt(pd.getStartDate()).lte(pd.getEndDate()));
                        return mdm.queryPage(PtmCmpSkuLog.class, query, page, 2000);
                    }

                    @Override
                    public boolean isRunForever() {
                        return false;
                    }

                    @Override
                    public void setRunForever(boolean runForever) {

                    }
                },
                new IProcessor<PtmCmpSkuLog>() {
                    @Override
                    public void process(PtmCmpSkuLog o) {
                        count.addAndGet(1);

                        long sid = o.getPcsId();
                        Date priceTime = o.getPriceTime();
                        String ymd = TimeUtils.parse(priceTime, "yyyyMMdd");

                        ConcurrentHashMap<String, PriceNode> priceNodeMap = historyPriceMap.get(sid);
                        if (priceNodeMap == null) {
                            priceNodeMap = new ConcurrentHashMap<>();
                            historyPriceMap.put(sid, priceNodeMap);
                        }

                        PriceNode pn = priceNodeMap.get(ymd);
                        if (pn == null) {
                            pn = new PriceNode(priceTime, o.getPrice());
                            priceNodeMap.put(ymd, pn);
                        } else {
                            return;
                        }

                        idSet.add(sid);
                    }
                }
        );

        listAndProcessTask2.setQueueMaxSize(1500);
        listAndProcessTask2.setProcessorCount(10);

        while (!pd.isEnd()) {
            listAndProcessTask2.go();
            // save work
            print(String.format("[%s]save map : %d", TimeUtils.parse(pd.startDate, "yyyyMMdd"), historyPriceMap.size()));
            pd.save(historyPriceMap);

            historyPriceMap.clear();
            pd.addDay();
        }

        print("count=" + count.get() + ", id set=" + idSet.size());
        return "ok";
    }

    /**
     * 根据sku的品牌标记品牌 - 手工标记
     */
    @RequestMapping(value = "/tag_brand_man", method = RequestMethod.GET)
    @ResponseBody
    public String tag_brand_man() {
        for (Map.Entry<String, String> kv : dataMap.entrySet()) {
            long id = Long.valueOf(kv.getKey());
            String brand = kv.getValue();

            PtmProductUpdater ptmProductUpdater = new PtmProductUpdater(id);
            ptmProductUpdater.getPo().setBrand(brand);
            productService.updateProduct(ptmProductUpdater);
        }

        return "ok";
    }

    /**
     * 根据sku的品牌标记品牌
     * 规则：查询sku的品牌，如果都相同，同时商品的品牌为空，则取该品牌作为商品的品牌
     *
     * @param cateId
     */
    @RequestMapping(value = "/tag_brand", method = RequestMethod.GET)
    @ResponseBody
    public void tag_brand(@RequestParam final long cateId) {
        ListProcessTask<PtmProduct> productListAndProcessTask2 = new ListProcessTask<>(
                new ILister() {
                    @Override
                    public PageableResult getData(int page) {
                        return productService.listPagedProducts(cateId, page, 1000);
                    }

                    @Override
                    public boolean isRunForever() {
                        return false;
                    }

                    @Override
                    public void setRunForever(boolean runForever) {

                    }
                },
                new IProcessor<PtmProduct>() {
                    @Override
                    public void process(PtmProduct o) {
                        List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(o.getId());

                        if (ArrayUtils.hasObjs(cmpSkus)) {
                            String skuBrand = "";
                            for (PtmCmpSku cmpSku : cmpSkus) {
                                String skuBrand2 = cmpSku.getBrand();
                                if (!StringUtils.isEmpty(skuBrand2)) {
                                    skuBrand2 = skuBrand2.trim();

                                    if (StringUtils.isEmpty(skuBrand)) {
                                        skuBrand = skuBrand2;
                                        continue;
                                    } else {
                                        if (!skuBrand.equalsIgnoreCase(skuBrand2)) {
                                            print(o.getId() + "-diff sku brand," + skuBrand + "," + skuBrand2);
                                            return;
                                        }
                                    }
                                }
                            }

                            if (StringUtils.isEmpty(skuBrand)) {
                                print(o.getId() + "\t," + o.getBrand() + ",sku brand is null");
                                return;
                            }

                            String proBrand = o.getBrand();
                            if (!StringUtils.isEmpty(proBrand)) {
                                proBrand = proBrand.trim();
                                if (skuBrand.equalsIgnoreCase(proBrand)) {
                                    return;
                                } else {
                                    print(o.getId() + "," + o.getBrand() + "," + skuBrand + ",\tbrand diff!!!");
                                    return;
                                }
                            } else {
                                PtmProductUpdater ptmProductUpdater = new PtmProductUpdater(o.getId());
                                ptmProductUpdater.getPo().setBrand(skuBrand);
                                productService.updateProduct(ptmProductUpdater);
                            }

                        } else {
                            print(o.getId() + "\t no skus.");
                        }
                    }
                }
        );

        productListAndProcessTask2.setProcessorCount(10);
        productListAndProcessTask2.setQueueMaxSize(1500);

        productListAndProcessTask2.go();
    }

    /**
     * 1-将sku中
     *
     * @param cateId
     */
    @RequestMapping(value = "/clean_url_sku", method = RequestMethod.GET)
    @ResponseBody
    public void clean_url_sku(@RequestParam final long cateId) {

        final AtomicInteger delCount = new AtomicInteger(0);

        ListProcessTask<PtmProduct> productListAndProcessTask2 = new ListProcessTask<>(
                new ILister() {
                    @Override
                    public PageableResult getData(int page) {
                        return productService.listPagedProducts(cateId, page, 1000);
                    }

                    @Override
                    public boolean isRunForever() {
                        return false;
                    }

                    @Override
                    public void setRunForever(boolean runForever) {

                    }
                },
                new IProcessor<PtmProduct>() {
                    @Override
                    public void process(PtmProduct o) {
                        List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(o.getId());

                        for (PtmCmpSku cmpSku : cmpSkus) {
                            Set<String> urlSet = new HashSet<>();
                            boolean update = false;

                            PtmCmpSkuUpdater ptmCmpSkuUpdater = new PtmCmpSkuUpdater(cmpSku.getId());
                            if (cmpSku.getCategoryId() == null || cmpSku.getCategoryId() != 5) {
                                update = true;
                                ptmCmpSkuUpdater.getPo().setCategoryId(5L);
                            }

                            String cleanUrl = WebsiteHelper.getCleanUrl(cmpSku.getWebsite(), cmpSku.getUrl());
                            if (!cleanUrl.equalsIgnoreCase(cmpSku.getUrl())) {
                                update = true;
                                print(cmpSku.getUrl() + "\t" + cleanUrl);
                                ptmCmpSkuUpdater.getPo().setUrl(cleanUrl);
//                                cmpSkuService.updateCmpSku(cmpSku.getId(), cleanUrl, cmpSku.getColor(), cmpSku.getSize(), cmpSku.getPrice());
                            }

                            if (!urlSet.contains(cleanUrl)) {
                                urlSet.add(cleanUrl);
                                if (update) {
                                    cmpSkuService.updateCmpSku(ptmCmpSkuUpdater);
                                }
                            } else {
                                print("delete sku");
                                cmpSkuService.deleteCmpSku(cmpSku.getId());
                                delCount.addAndGet(1);
                            }
                        }
                    }
                }
        );

        productListAndProcessTask2.setProcessorCount(10);
        productListAndProcessTask2.setQueueMaxSize(1500);

        productListAndProcessTask2.go();
        print(String.format("delete sku count : %d", delCount.get()));

    }

    private void print(String str) {
        System.out.println(str);
    }

    class ProcessDate {

        private Date startDate;
        private Date endDate;

        public ProcessDate(Date startDate, Date endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public boolean isEnd() {
            print(String.format("%s", TimeUtils.parse(startDate, "yyyy-MM-dd")));
            return TimeUtils.today() < this.startDate.getTime();
        }

        public void addDay() {
            this.startDate = TimeUtils.addDay(startDate, 1);
            this.endDate = TimeUtils.addDay(endDate, 1);
        }

        public void save(ConcurrentHashMap<Long, ConcurrentHashMap<String, PriceNode>> historyPriceMap) {
            Iterator<Long> it = historyPriceMap.keySet().iterator();
            int total = historyPriceMap.size();
            int count = 0;
            while (it.hasNext()) {
                Long sid = it.next();
                ConcurrentHashMap<String, PriceNode> priceNodeMap = historyPriceMap.get(sid);
                List<PriceNode> priceNodes = new ArrayList<>();
                for (ConcurrentHashMap.Entry<String, PriceNode> priceNodeEntry : priceNodeMap.entrySet()) {
                    priceNodes.add(priceNodeEntry.getValue());
                }

                cmpSkuService.saveHistoryPrice(sid, priceNodes);
                count++;
                if (count % 400 == 0) {
                    print(String.format("save %d/%d", count, total));
                }
            }
        }
    }
}