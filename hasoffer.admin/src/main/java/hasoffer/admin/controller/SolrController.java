package hasoffer.admin.controller;

import hasoffer.affiliate.affs.flipkart.FlipkartAffiliateProductProcessor;
import hasoffer.affiliate.model.FlipkartSkuInfo;
import hasoffer.base.model.PageableResult;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.product.ICategoryService;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.product.IProductService;
import hasoffer.core.task.ListProcessTask;
import hasoffer.core.task.worker.ILister;
import hasoffer.core.task.worker.IProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping(value = "/solr")
public class SolrController {

    private static final String Q_PRODUCT =
            "SELECT t FROM PtmProduct t where t.id > 726884";
    @Resource
    IProductService productService;
    @Resource
    ICategoryService categoryService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    ICmpSkuService cmpSkuService;

    @RequestMapping(value = "/product/reimport111", method = RequestMethod.GET)
    public void reimport111() {

        String Q_PRO_BRAND = "SELECT COUNT(t.id),t.brand FROM PtmProduct t WHERE t.brand LIKE '%mobile%' GROUP BY t.brand";
        String Q_PRO_BRAND_1 = "SELECT t from PtmProduct t where t.brand=?0 ";
        List<Object[]> datas = dbm.query(Q_PRO_BRAND);

        for (Object[] data : datas) {
            String brand = (String) data[1];
            List<PtmProduct> products = dbm.query(Q_PRO_BRAND_1, Arrays.asList(brand));
            for (PtmProduct product : products) {
                product.setBrand("");
                productService.importProduct2Solr2(product);
            }
        }

        System.out.println(datas.size());

    }

    //1973863
    @RequestMapping(value = "/product/importbycategory3", method = RequestMethod.GET)
    public void importbycategory3(@RequestParam final long cate) {
        ListProcessTask<PtmProduct> listAndProcessTask2 = new ListProcessTask<>(
                new ILister() {
                    @Override
                    public PageableResult<PtmProduct> getData(int page) {
                        System.out.println(String.format("importbycategory3, cate=%d page=%d", cate, page));
                        return productService.listPagedProducts(cate, page, 2000);
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
                        try {
                            import2Solr(o);
                        } catch (Exception e) {
                            System.out.println("ERROR " + o.getId() + "\t" + e.getMessage());
                        }
                    }
                }
        );

        listAndProcessTask2.setProcessorCount(10);
        listAndProcessTask2.setQueueMaxSize(200);

        listAndProcessTask2.go();
    }

    //1973863
    @RequestMapping(value = "/product/importbycategory2", method = RequestMethod.GET)
    public void importNewAllProducts(@RequestParam final long minProId) {
        final String Q_PRO = "SELECT t FROM PtmProduct t where t.id > ?0";
        ListProcessTask<PtmProduct> listAndProcessTask2 = new ListProcessTask<>(
                new ILister() {
                    @Override
                    public PageableResult getData(int page) {
                        System.out.println("importNewAllProducts page = " + page);
                        return dbm.queryPage(Q_PRO, page, 2000, Arrays.asList(minProId));
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
                        try {
                            import2Solr(o);
                        } catch (Exception e) {
                            System.out.println("ERROR " + o.getId() + "\t" + e.getMessage());
                        }
                    }
                }
        );

        listAndProcessTask2.setProcessorCount(10);
        listAndProcessTask2.setQueueMaxSize(200);

        listAndProcessTask2.go();
    }

    private void import2Solr(PtmProduct o) {
        List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(o.getId(), SkuStatus.ONSALE);

        long cateId = o.getCategoryId();
        String brand = o.getBrand();
        String model = o.getModel();


        Set<Long> cateSet = new HashSet<>();
        Set<String> brandSet = new HashSet<>();
        Set<String> modelSet = new HashSet<>();

        PtmCmpSku flipSku = null;

        float minPrice = -1f, maxPrice = -1f;

        for (PtmCmpSku cmpSku : cmpSkus) {
            float skuPrice = cmpSku.getPrice();
            if (skuPrice <= 0 || cmpSku.getStatus() == SkuStatus.OFFSALE) {
                continue;
            }

            if (minPrice <= 0) {
                minPrice = skuPrice;
                maxPrice = minPrice;
                continue;
            }

            if (minPrice > skuPrice) {
                minPrice = skuPrice;
            }
            if (maxPrice < skuPrice) {
                maxPrice = skuPrice;
            }

            // ....

            if (cmpSku.getCategoryId() != null && cmpSku.getCategoryId() > 0) {
                cateSet.add(cmpSku.getCategoryId());
            }
            if (StringUtils.isEmpty(cmpSku.getBrand())) {
                brandSet.add(cmpSku.getBrand());
            }
            if (StringUtils.isEmpty(cmpSku.getModel())) {
                modelSet.add(cmpSku.getModel());
            }

            if (flipSku == null && Website.FLIPKART == cmpSku.getWebsite()) {
                flipSku = cmpSku;
            }
        }

        if (minPrice < 0) {
            return;
        }

        String info = String.format("[Product]%d, Cate Set[%d], Brand Set[%d], Model Set[%d].", o.getId(), cateSet.size(), brandSet.size(), modelSet.size());
        System.out.println(info);

        if (StringUtils.isEmpty(brand)) {
            if (flipSku != null) {
                if (StringUtils.isEmpty(flipSku.getBrand())) {
                    FlipkartAffiliateProductProcessor fapp = new FlipkartAffiliateProductProcessor();
                    FlipkartSkuInfo skuInfo = null;
                    try {
                        skuInfo = fapp.getSkuInfo(flipSku.getSourceSid());
                        if (skuInfo != null) {
                            brand = skuInfo.getProductBrand();
                            model = skuInfo.getModelName();
                            // 更新商品brand和model， solr
                            cmpSkuService.updateCmpSkuBrandModel(flipSku.getId(), brand, model);
                        }
                    } catch (Exception e) {
                        System.out.println(String.format("Error : [%s]. Info : [%s]", e.getMessage(), flipSku.getSourceSid()));
                    }

                } else {
                    brand = flipSku.getBrand();
                    model = flipSku.getModel();
                }
            } else {
                if (brandSet.size() == 1) {
                    brand = brandSet.iterator().next();
                }
            }

            productService.updateProductBrandModel(o.getId(), brand, model);
            o.setBrand(brand);
            o.setModel(model);
        }

        productService.importProduct2Solr2(o, cmpSkus);
    }

    @RequestMapping(value = "/product/importbycategory", method = RequestMethod.GET)
    public void importNewAll(@RequestParam long minCateId) {
        final String Q_SKU = "select t from PtmCmpSku t where t.website=?0 and t.categoryId=?1 and t.sourceSid is not null";

        final ProcessCate pc = new ProcessCate();

        ListProcessTask<PtmCmpSku> listAndProcessTask2 = new ListProcessTask<>(
                new ILister() {
                    @Override
                    public PageableResult getData(int page) {
                        PageableResult result = dbm.queryPage(Q_SKU, page, 500, Arrays.asList(Website.FLIPKART, pc.getCateId()));
                        System.out.println(String.format("Import Solr: Category[%d], Page[%d/%d].", pc.getCateId(), page, result.getTotalPage()));
                        return result;
                    }

                    @Override
                    public boolean isRunForever() {
                        return false;
                    }

                    @Override
                    public void setRunForever(boolean runForever) {

                    }
                },
                new IProcessor<PtmCmpSku>() {
                    @Override
                    public void process(PtmCmpSku o) {
                        if (StringUtils.isEmpty(o.getSourceSid())) {
                            return;
                        }

                        String brand = o.getBrand();
                        String model = o.getModel();
                        try {

                            if (StringUtils.isEmpty(brand)) {
                                FlipkartAffiliateProductProcessor fapp = new FlipkartAffiliateProductProcessor();
                                FlipkartSkuInfo skuInfo = fapp.getSkuInfo(o.getSourceSid());
                                brand = skuInfo.getProductBrand();
                                model = skuInfo.getModelName();
                                // 更新商品brand和model， solr
                                cmpSkuService.updateCmpSkuBrandModel(o.getId(), brand, model);
                            }

                            long proId = o.getProductId();
                            // update product brand
                            productService.updateProductBrandModel(proId, brand, model);

                            productService.importProduct2Solr2(proId);

                        } catch (Exception e) {
                            System.out.println(String.format("Error : [%s]. Info : [%s]", e.getMessage(), o.getSourceSid()));
                        }
                    }
                }
        );

        listAndProcessTask2.setProcessorCount(10);
        listAndProcessTask2.setQueueMaxSize(200);

        // cate list for each
        List<PtmCategory> cates = categoryService.listCates();
        for (PtmCategory cate : cates) {
            if (cate.getId() <= minCateId) {
                continue;
            }
            pc.setCateId(cate.getId());
            listAndProcessTask2.go();
        }
    }

    @RequestMapping(value = "/product/reimportnew", method = RequestMethod.GET)
    public void reimportnew() {
        ListProcessTask<PtmProduct> listAndProcessTask2 = new ListProcessTask<>(
                new ILister() {
                    @Override
                    public PageableResult getData(int page) {
                        return dbm.queryPage(Q_PRODUCT, page, 2000);
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
                        productService.importProduct2Solr2(o);
                    }
                }
        );

        listAndProcessTask2.go();
    }

    @RequestMapping(value = "/category/reimport", method = RequestMethod.GET)
    public ModelAndView reimportCategory(HttpServletRequest request) {
        categoryService.reimportCategoryIndex();

        ModelAndView mav = new ModelAndView();
        mav.addObject("result", "ok");
        return mav;
    }

    @RequestMapping(value = "/product/updateall", method = RequestMethod.GET)
    public ModelAndView updateall(HttpServletRequest request) {

        Runnable re = new Runnable() {
            @Override
            public void run() {
                productService.reimport2Solr(false);
            }
        };

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(re);

        ModelAndView mav = new ModelAndView();
        mav.addObject("result", "ok");
        return mav;
    }

    @RequestMapping(value = "/product/reimportsolrindexbycategory", method = RequestMethod.GET)
    public
    @ResponseBody
    String reimportsolrindexbycategory(@RequestParam long cateId) {

        List<PtmProduct> products = productService.listProducts(cateId, 1, Integer.MAX_VALUE);
        int size = products.size();
        int count = 0;
        for (PtmProduct product : products) {
            count++;
            productService.importProduct2Solr(product);

            if (count % 10 == 0) {
                System.out.println("[reimportsolrindexbycategory] - " + count + " / " + size);
            }
        }

        return "ok";
    }

    @RequestMapping(value = "/product/reimport", method = RequestMethod.GET)
    public ModelAndView recreatesolrindex(HttpServletRequest request) {

        Runnable re = new Runnable() {
            @Override
            public void run() {
                productService.reimport2Solr(true);
            }
        };

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(re);

        ModelAndView mav = new ModelAndView();
        mav.addObject("result", "ok");
        return mav;
    }

    @RequestMapping(value = "/product/append", method = RequestMethod.GET)
    public ModelAndView append(HttpServletRequest request) {

        Runnable re = new Runnable() {
            @Override
            public void run() {
                productService.append2Solr();
            }
        };

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(re);

        ModelAndView mav = new ModelAndView();
        mav.addObject("result", "ok");
        return mav;
    }

    class ProcessCate {
        long cateId;

        public long getCateId() {
            return cateId;
        }

        public void setCateId(long cateId) {
            this.cateId = cateId;
        }
    }

}
