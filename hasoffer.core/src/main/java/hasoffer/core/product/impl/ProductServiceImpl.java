package hasoffer.core.product.impl;

import hasoffer.base.model.PageableResult;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.bo.product.ProductBo;
import hasoffer.core.cache.CategoryCacheManager;
import hasoffer.core.cache.SearchLogCacheManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.*;
import hasoffer.core.persistence.po.ptm.updater.PtmCmpSkuUpdater;
import hasoffer.core.persistence.po.ptm.updater.PtmImageUpdater;
import hasoffer.core.persistence.po.ptm.updater.PtmProductUpdater;
import hasoffer.core.persistence.po.ptm.updater.PtmTopSellingUpdater;
import hasoffer.core.persistence.po.search.SrmProductSearchCount;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.core.product.ICategoryService;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.product.IProductService;
import hasoffer.core.product.solr.*;
import hasoffer.core.search.ISearchService;
import hasoffer.core.utils.ImageUtil;
import hasoffer.data.solr.*;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.model.ListProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ProductServiceImpl implements IProductService {
    private static final String Q_PRODUCT =
            "SELECT t FROM PtmProduct t";
    private static final String Q_PRODUCT_BY_CATEGORY =
            "SELECT t FROM PtmProduct t WHERE t.categoryId = ?0";
    private static final String Q_PRODUCT_BY_CMPID =
            "SELECT t FROM PtmProduct t " +
                    " WHERE t.id > ?0 ";
    private static final String Q_PRODUCT_BY_CREATETIME =
            "SELECT t FROM PtmProduct t " +
                    " WHERE t.createTime > ?0 " +
                    "   AND t.sourceSite <> 'MYSMARTPRICE' " +
                    " ORDER BY t.createTime ASC ";

    private static final String Q_PTM_CMPSKU =
            "SELECT t FROM PtmCmpSku t " +
                    " WHERE t.productId = ?0   " +
                    " ORDER BY t.price ASC ";

    //    private static final String Q_ONSALE_PTM_CMPSKU =
//            "SELECT t FROM PtmCmpSku t " +
//                    " WHERE t.productId = ?0  AND  t.price >?1  " +
//                    "   AND t.status = 'ONSALE' or  t.status = 'OUTSTOCK'  " +
//                    " ORDER BY t.price ASC ";
    private static final String Q_ONSALE_PTM_CMPSKU =
            "SELECT t FROM PtmCmpSku t " +
                    " WHERE t.productId = ?0  AND  t.price >?1  AND t.status <> 'OFFSALE'  ORDER BY t.price ASC  ";


    private static final String Q_NOTOFFSALE_PTM_CMPSKU =
            "SELECT t FROM PtmCmpSku t " +
                    " WHERE t.productId = ?0 " +
                    "   AND t.status != 'OFFSALE'  " +
                    " ORDER BY t.price ASC ";

    private static final String Q_PTM_IMAGE =
            "SELECT t FROM PtmImage t " +
                    " WHERE t.productId = ?0  ";
    private static final String Q_PTM_BASICATTRIBUTE =
            "SELECT t FROM PtmBasicAttribute t " +
                    " WHERE t.productId = ?0 ";

    private static final String Q_PTM_TOPSEELLING =
            "select t from PtmTopSelling t where   t.status='ONLINE'  order by t.lUpdateTime desc , t.count desc ";

    @Resource
    ISearchService searchService;

    @Resource
    ProductIndexServiceImpl productIndexService;
    @Resource
    ProductIndex2ServiceImpl productIndex2Service;
    @Resource
    ICategoryService categoryService;
    @Resource
    SearchLogCacheManager searchLogCacheManager;
    @Resource
    CmpskuIndexServiceImpl cmpskuIndexService;
    @Resource
    CategoryCacheManager categoryCacheManager;
    @Resource
    private IDataBaseManager dbm;
    @Resource
    private ICmpSkuService cmpSkuService;
    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductImage2(Long productId, String oriImageUrl) {
        // 更新 image url 2
        PtmImage image = getProductMasterImage(productId);
        if (image == null) {
            // 图片为null时，创建
            image = new PtmImage(productId, oriImageUrl, oriImageUrl);
            dbm.create(image);
            return;
        } else {
            PtmImageUpdater imageUpdater = new PtmImageUpdater(image.getId());
            imageUpdater.getPo().setImageUrl2(oriImageUrl);
            dbm.update(imageUpdater);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePtmProductPrice(long id) {

        List<PtmCmpSku> skus = dbm.query("SELECT t FROM PtmCmpSku t WHERE t.productId = ?0 ", Arrays.asList(id));

        float price = 0.0f;
        boolean flag = true;

        for (int i = 0; i < skus.size(); i++) {

            PtmCmpSku sku = skus.get(i);
            //status
            if (sku.getStatus() == SkuStatus.OFFSALE) {
                continue;
            }
            //price
            if (sku.getPrice() <= 0) {
                continue;
            }

            if (flag) {
                price = skus.get(i).getPrice();
                flag = false;
                continue;
            }

            if (skus.get(i).getPrice() < price) {
                price = skus.get(i).getPrice();
            }

        }

        if (price != 0) {

            PtmProductUpdater updater = new PtmProductUpdater(id);

            updater.getPo().setPrice(price);
            updater.getPo().setUpdateTime(TimeUtils.nowDate());

            dbm.update(updater);

            PtmProduct product = getProduct(id);
            if (product == null) {
                return;
            }

            product.setPrice(price);

            importProduct2Solr(product);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePtmProdcutWebsite(long id, Website website) {
        PtmProductUpdater updater = new PtmProductUpdater(id);

        updater.getPo().setSourceSite(website.name());

        dbm.update(updater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void expTopSellingsFromSearchCount(String ymd) {
        // 查询
        int page = 1, size = 40;
        PageableResult<SrmProductSearchCount> pagedSearchCounts = searchService.findSearchCountsByYmd(ymd, page, size);

        List<SrmProductSearchCount> searchCounts = pagedSearchCounts.getData();

        List<PtmTopSelling> topSellings = new ArrayList<PtmTopSelling>();
        for (SrmProductSearchCount searchCount : searchCounts) {
            Long productId = searchCount.getProductId();

            PtmTopSelling topSelling = dbm.get(PtmTopSelling.class, productId);

            if (topSelling != null) {
                // 更新
                PtmTopSellingUpdater topSellingUpdater = new PtmTopSellingUpdater(productId);
                topSellingUpdater.getPo().setlUpdateTime(TimeUtils.now());
                topSellingUpdater.getPo().setCount(searchCount.getCount());
                dbm.update(topSellingUpdater);
                continue;
            }

            // 创建
            topSellings.add(new PtmTopSelling(productId, searchCount.getCount()));
        }

        dbm.batchSave(topSellings);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePtmProductCategoryId(long ptmProductId, long categoryId) {

        PtmProductUpdater updater = new PtmProductUpdater(ptmProductId);

        updater.getPo().setCategoryId(categoryId);

        dbm.update(updater);
    }

    @Override
    public ProductBo getProductBo(long proId) {

        if (proId > 0) {

            PtmProduct product = getProduct(proId);

            if (product == null) {
                return null;
            }

            PtmImage image = getProductMasterImage(proId);

            return new ProductBo(product, cmpSkuService.listCmpSkus(proId), image);
        }

        return null;
    }

    @Override
    public PageableResult<PtmProduct> listProductsByCreateTime(Date fromDate, int page, int size) {
        return dbm.queryPage(Q_PRODUCT_BY_CREATETIME, page, size, Arrays.asList(fromDate));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductBo createProductByListProducts(Map<Website, ListProduct> listProductMap) {

        long cateId = 0L;
        ProductBo pro = null;

        for (Map.Entry<Website, ListProduct> kv : listProductMap.entrySet()) {
            Website website = kv.getKey();

            ListProduct listProduct = kv.getValue();

            if (pro == null) {
                pro = createProduct(cateId, listProduct.getTitle(), listProduct.getPrice(),
                        website, listProduct.getUrl(), listProduct.getSourceId(), listProduct.getImageUrl());
            } else {
                PtmCmpSku cmpSku = createCmpsku(pro.getId(), listProduct.getPrice(),
                        listProduct.getUrl(), listProduct.getTitle(), listProduct.getImageUrl());
                pro.getCmpSkus().add(cmpSku);
            }

        }

        return pro;
    }

    @Override
    public PtmCmpSku createCmpsku(long ptmProductId, float price, String url, String title, String imageUrl, String deeplink) {
        logger.debug(String.format("create cmp sku : %s", title));

        PtmCmpSku cmpSku = new PtmCmpSku(ptmProductId, price, url, title, imageUrl, deeplink);

        cmpSkuService.createCmpSku(cmpSku);

        return cmpSku;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PtmCmpSku createCmpsku(long productId, float price, String url, String title, String imageUrl) {
        logger.debug(String.format("create cmp sku : %s", title));

        PtmCmpSku cmpSku = new PtmCmpSku(productId, price, url, title, imageUrl);

        cmpSkuService.createCmpSku(cmpSku);

        return cmpSku;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductCategory(PtmProduct product, Long targetCate) {

        PtmProductUpdater productUpdater = new PtmProductUpdater(product.getId());
        productUpdater.getPo().setCategoryId(targetCate);
        dbm.update(productUpdater);

        product.setCategoryId(targetCate);
        importProduct2Solr(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductTag(String proId, String tag) {

        long proIdl = Long.valueOf(proId);

        PtmProduct product = dbm.get(PtmProduct.class, proIdl);

        if (product == null) {
            return false;
        }

        PtmProductUpdater ptmProductUpdater = new PtmProductUpdater(proIdl);
        ptmProductUpdater.getPo().setTag(tag);
        dbm.update(ptmProductUpdater);

        product.setTag(tag);
        importProduct2Solr(product);

        return true;
    }

    @Override
//    @Cacheable(value = CACHE_KEY, key = "#root.methodName + '_' + #root.args[0]")
    public PtmProduct getProduct(long proId) {
        return dbm.get(PtmProduct.class, proId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSku(long skuId, String url) {
        PtmCmpSkuUpdater ptmCmpSkuUpdater = new PtmCmpSkuUpdater(skuId);

        Website website = WebsiteHelper.getWebSite(url);
        ptmCmpSkuUpdater.getPo().setWebsite(website);
        ptmCmpSkuUpdater.getPo().setUrl(url);

        dbm.update(ptmCmpSkuUpdater);
    }

    @Override
    public List<String> getProductImageUrls(Long id) {
        List<PtmImage> images = dbm.query(Q_PTM_IMAGE, Arrays.asList(id));
        List<String> imageUrls = new ArrayList<String>();

        if (ArrayUtils.hasObjs(images)) {
            // 优先从服务器取图片
            for (PtmImage image : images) {
                imageUrls.add(ImageUtil.getImageUrl(image));
            }
        }

        return imageUrls;
    }

    @Override
    public PtmImage getProductMasterImage(Long id) {
        List<PtmImage> images = dbm.query(Q_PTM_IMAGE, Arrays.asList(id));
        return ArrayUtils.hasObjs(images) ? images.get(0) : null;
    }

    @Override
    public String getProductMasterImageUrl(Long id) {
        PtmImage image = getProductMasterImage(id);

        return ImageUtil.getImageUrl(image);
    }

    @Override
    public List<PtmProduct> getProducts(List<Long> proIds) {

        List<PtmProduct> products = new ArrayList<PtmProduct>();
        if (ArrayUtils.isNullOrEmpty(proIds)) {
            return products;
        }

        for (Long id : proIds) {
            if (id <= 0) {
                continue;
            }
            PtmProduct product = dbm.get(PtmProduct.class, id);
            if (product != null) {
                products.add(product);
            }
        }

        return products;
    }

    @Override
    public List<PtmTopSelling> getTopSellings(Long yesterdayStart, Long todayStart, int page, int size) {
        return dbm.query(Q_PTM_TOPSEELLING, page < 1 ? 1 : page, size == 0 ? 20 : size);
    }

    @Override
    public List<PtmProduct> listProducts(long cateId, int page, int size) {
        return dbm.query(Q_PRODUCT_BY_CATEGORY, page, size, Arrays.asList(cateId));
    }

    @Override
    public PageableResult<PtmProduct> listPagedProducts(int page, int size) {
        return dbm.queryPage(Q_PRODUCT, page, size);
    }

    @Override
    public PageableResult<PtmProduct> listPagedProducts(long cateId, int page, int size) {
        return dbm.queryPage(Q_PRODUCT_BY_CATEGORY, page, size, Arrays.asList(cateId));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public PtmProduct createProduct(long categoryId, String title, float price,
                                    String description, String colors, String sizes,
                                    int rating, String website, String sourceId) {
        PtmProduct ptmProduct = new PtmProduct(categoryId,
                title,
                price,
                description,
                colors,
                sizes,
                rating,
                website,
                sourceId);
        dbm.create(ptmProduct);
        return ptmProduct;
    }

    /**
     * 删除商品操作
     * 一般情况不要使用
     * 删除商品：删除ptmimage ptmcmpsku ptmproduct ， 目前不涉及 ptmsku
     *
     * @param ptmProductId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteProduct(long ptmProductId) {
        logger.warn("delete product : " + ptmProductId);
        System.out.println("delete product : " + ptmProductId);

        List<PtmCmpSku> cmpSkus = dbm.query("select t from PtmCmpSku t where t.productId = ?0", Arrays.asList(ptmProductId));

        List<PtmImage> images = dbm.query("select t from PtmImage t where t.productId = ?0", Arrays.asList(ptmProductId));

        if (ArrayUtils.hasObjs(cmpSkus)) {
            for (PtmCmpSku cmpSku : cmpSkus) {
//                dbm.delete(PtmCmpSku.class, cmpSku.getId());
                cmpSkuService.deleteCmpSku(cmpSku.getId());
            }
        } else {
            PageableResult<CmpSkuModel> skuModelPageableResult = cmpskuIndexService.search("productId", String.valueOf(ptmProductId), 1, Integer.MAX_VALUE);
            List<CmpSkuModel> skuModels = skuModelPageableResult.getData();
            if (ArrayUtils.hasObjs(skuModels)) {
                for (CmpSkuModel cmpSkuModel : skuModels) {
                    cmpskuIndexService.remove(String.valueOf(cmpSkuModel.getId()));
                }
            }
        }

        if (ArrayUtils.hasObjs(images)) {
            for (PtmImage image : images) {
                dbm.delete(PtmImage.class, image.getId());
            }
        }

        PtmProduct product = dbm.get(PtmProduct.class, ptmProductId);
        if (product != null) {
            dbm.delete(PtmProduct.class, ptmProductId);
        }

        productIndexService.remove(String.valueOf(ptmProductId));
        productIndex2Service.remove(String.valueOf(ptmProductId));

        // 删除searchlog 以及 缓存
        PageableResult<SrmSearchLog> pagedSearchLogs = searchService.listSearchLogsByProductId(ptmProductId, 1, Integer.MAX_VALUE);
        List<SrmSearchLog> searchLogs = pagedSearchLogs.getData();
        if (ArrayUtils.hasObjs(searchLogs)) {
            for (SrmSearchLog srmSearchLog : searchLogs) {
                dbm.delete(SrmSearchLog.class, srmSearchLog.getId());
                searchLogCacheManager.delCache(srmSearchLog.getId());
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ProductBo createProduct(long cateId, String title, float price,
                                   Website website, String sourceUrl, String sourceId, String imageUrl) {
        logger.debug(String.format("create product : %s", title));

        //创建 PtmProduct
        PtmProduct product = new PtmProduct(cateId, title, price, website.name(), sourceUrl, sourceId);
        dbm.create(product);

        //创建 图片
        PtmImage ptmImage = new PtmImage(product.getId(), imageUrl);
        dbm.create(ptmImage);

        //features
        //basic attrs

        //关联 cmp sku
//        PtmCmpSku cmpSku = new PtmCmpSku(product.getId(), price, sourceUrl, title, imageUrl);
//        cmpSku.setChecked(true);
//        cmpSkuService.createCmpSku(cmpSku);

//        List skuList = new ArrayList();
//        skuList.add(cmpSku);

        importProduct2Solr(product);

        return new ProductBo(product, new ArrayList<PtmCmpSku>(), ptmImage);
    }

    @Override
    public void reimport2Solr(boolean removeFirst) {
        updateProductIndex2Solr();
//        if (removeFirst) {
//            try {
//                productIndexService.removeAll();
//            } catch (SolrServerException e) {
//                logger.error("{}", e);
//            } catch (IOException e) {
//                logger.error("{}", e);
//            }
//    }
//
//        addOrUpdateSolr(Q_PRODUCT, null);

    }

    @Override
    public void importProduct2Solr2(long proId) {
        PtmProduct product = getProduct(proId);
        importProduct2Solr2(product);
    }

    @Override
    public void importProduct2Solr2(PtmProduct o, List<PtmCmpSku> cmpSkus) {
        ProductModel2 productModel2 = getProductModel2(o, cmpSkus);
        if (productModel2 != null) {
            productIndex2Service.createOrUpdate(productModel2);
        }
    }

    @Override
    @Transactional
    public void updateProduct(PtmProductUpdater ptmProductUpdater) {
        dbm.update(ptmProductUpdater);
    }

    @Override
    @Transactional
    public void updateProductBrandModel(long proId, String productBrand, String modelName) {
        PtmProductUpdater productUpdater = new PtmProductUpdater(proId);

        productUpdater.getPo().setModel(modelName);
        productUpdater.getPo().setBrand(productBrand);

        dbm.update(productUpdater);
    }

    @Override
    public void reimport2Solr(long productId) {
        PtmProduct ptmProduct = getProduct(productId);
        if (ptmProduct != null) {
            importProduct2Solr(ptmProduct);
        }
    }

    @Override
    public void import2Solr(ProductModel pm) {
        if (pm == null) {
            return;
        }
        productIndexService.createOrUpdate(pm);
    }

    @Override
    @Transactional
    public void updateProductStd(Long proId, boolean std) {
        PtmProductUpdater productUpdater = new PtmProductUpdater(proId);
        productUpdater.getPo().setStd(std);
        dbm.update(productUpdater);
    }

    @Override
    public PageableResult<PtmCmpSku> listNotOffSaleCmpSkus(long proId, int page, int size) {
        PageableResult<PtmCmpSku> pagedResult = dbm.queryPage(Q_NOTOFFSALE_PTM_CMPSKU, page, size, Arrays.asList(proId));
        return pagedResult;
    }

    @Override
    public ProductModel getProductModel(PtmProduct product) {
        if (product == null) {
            return null;
        }

        List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(product.getId());
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
        }

        if (minPrice < 0) {
            productIndexService.remove(String.valueOf(product.getId()));
            productIndex2Service.remove(String.valueOf(product.getId()));
            return null;
        }

//        PtmCategory category = dbm.get(PtmCategory.class, product.getCategoryId());
        List<PtmCategory> categories = categoryCacheManager.getRouterCategoryList(product.getCategoryId());

        long cate1 = 0L, cate2 = 0L, cate3 = 0L;
        String cate3name = "";

        // 目前仅支持3级类目
        if (ArrayUtils.hasObjs(categories)) {
            cate1 = categories.get(0).getId();

            int cateSize = categories.size();

            if (cateSize > 1) {
                cate2 = categories.get(1).getId();
            }

            if (cateSize > 2) {
                cate3 = categories.get(2).getId();
            }
        }

        long searchCount = 0;
        SrmProductSearchCount productSearchCount = searchService.findSearchCountByProductId(product.getId());
        if (productSearchCount != null) {
            searchCount = productSearchCount.getCount();
        }

        ProductModel productModel = new ProductModel(product.getId(),
                product.getTitle(),
                product.getTag(),
                cate3,
                cate3name,
                minPrice,
                product.getDescription(),
                product.getColor(),
                product.getSize(),
                product.getRating(),
                cate1,
                cate2,
                cate3,
                searchCount);

        return productModel;
    }

    @Override
    public ProductModel2 getProductModel2(PtmProduct product) {
        if (product == null) {
            return null;
        }

        List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(product.getId());

        return getProductModel2(product, cmpSkus);
    }

    private ProductModel2 getProductModel2(PtmProduct product, List<PtmCmpSku> cmpSkus) {
        if (product == null || ArrayUtils.isNullOrEmpty(cmpSkus)) {
            return null;
        }

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
        }

        if (minPrice < 0) {
            return null;
        }

        // 类目关键词
        long cate1 = 0L, cate2 = 0L, cate3 = 0L;
        String cate1name = "", cate2name = "", cate3name = "", cateTag = "";

        List<PtmCategory> categories = categoryCacheManager.getRouterCategoryList(product.getCategoryId());

        // 目前仅支持3级类目
        if (ArrayUtils.hasObjs(categories)) {
            PtmCategory cate = categories.get(0);

            cate1 = cate.getId();
            cate1name = cate.getName();

            int cateSize = categories.size();

            if (cateSize > 1) {
                cate = categories.get(1);
                cate2 = cate.getId();
                cate2name = cate.getName();
            }

            if (cateSize > 2) {
                cate = categories.get(2);
                cate3 = cate.getId();
                cate3name = cate.getName();
            }

            cateTag = categoryCacheManager.getCategoryTag(product.getCategoryId());
        }

        long searchCount = 0;
        SrmProductSearchCount productSearchCount = searchService.findSearchCountByProductId(product.getId());
        if (productSearchCount != null) {
            searchCount = productSearchCount.getCount();
        }

        ProductModel2 productModel = new ProductModel2(
                product.getId(),
                product.getTitle(),
                product.getTag(),
                cateTag,
                product.getBrand(),
                product.getModel(),
                cate1,
                cate2,
                cate3,
                cate1name,
                cate2name,
                cate3name,
                minPrice,
                maxPrice,
                product.getRating(),
                searchCount);

        return productModel;
    }

    @Override
    public void importProduct2Solr(PtmProduct product) {
        if (product == null) {
            return;
        }
        // new import
        importProduct2Solr2(product);

        ProductModel productModel = getProductModel(product);

        if (productModel != null) {
            productIndexService.createOrUpdate(productModel);
        } else {
            productIndexService.remove(String.valueOf(product.getId()));
        }
    }

    @Override
    public void importProduct2Solr2(PtmProduct product) {
        if (product == null) {
            return;
        }
        ProductModel2 productModel2 = getProductModel2(product);
        if (productModel2 != null) {
            productIndex2Service.createOrUpdate(productModel2);
        } else {
            productIndex2Service.remove(String.valueOf(product.getId()));
        }
    }

    private void addOrUpdateSolr(final String queryString, List params) {
        final int PAGE_SIZE = 500;
        int pageNum = 1;

        PageableResult<PtmProduct> pagedProducts = null;

        if (ArrayUtils.isNullOrEmpty(params)) {
            pagedProducts = dbm.queryPage(queryString, pageNum, PAGE_SIZE);
        } else {
            pagedProducts = dbm.queryPage(queryString, pageNum, PAGE_SIZE, params);
        }

        int pageCount = (int) pagedProducts.getTotalPage();
        List<PtmProduct> products = pagedProducts.getData();

        while (pageNum <= pageCount) {

            logger.debug(String.format("PAGE: %d / %d", pageNum, pageCount));

            if (pageNum > 1) {
                products = dbm.query(queryString, pageNum, PAGE_SIZE);
            }

            if (ArrayUtils.isNullOrEmpty(products)) {
                continue;
            } else {
                for (PtmProduct product : products) {
                    importProduct2Solr(product);
                }
            }

            pageNum++;
        }
    }

    private void updateProductIndex2Solr() {
        long id = 1L;

        final String Q_MAX_PROID = "SELECT MAX(t.id) FROM PtmProduct t";
        final long maxId = dbm.querySingle(Q_MAX_PROID);

        while (id <= maxId) {
            PtmProduct product = getProduct(id);

            if (id++ % 500 == 0) {
                System.out.println("addOrUpdateSolr2, id = " + id);
            }

            if (product == null || product.getPrice() <= 0) {
                productIndexService.remove(String.valueOf(id));
                continue;
            }

            List<PtmCmpSku> cmpSkus = cmpSkuService.listCmpSkus(id);
            int validCount = 0;
            for (PtmCmpSku cmpSku : cmpSkus) {
                if (cmpSku.getStatus() == SkuStatus.OFFSALE || cmpSku.getPrice() <= 0) {
                    continue;
                }
                validCount++;
            }

            if (validCount <= 0) {
                // remove from solr
                productIndexService.remove(String.valueOf(id));
            }

        }
    }

    @Override
    public void append2Solr() {
        FilterQuery[] fqs = null;
        Sort[] sorts = new Sort[]{
                new Sort("id", Order.DESC)
        };
        PivotFacet[] pivotFacets = null;

        SearchResult<Long> sr = productIndexService.search("*", fqs, sorts, pivotFacets, 1, 1);

        long maxId = sr.getResult().get(0);

        addOrUpdateSolr(Q_PRODUCT_BY_CMPID, Arrays.asList(maxId));
    }

    @Override
//    @Cacheable(value = CACHE_KEY, key = "#root.methodName + '_' + #root.args[0] + #root.args[1] + #root.args[2]")
    public PageableResult<PtmCmpSku> listPagedCmpSkus(long proId, int pageNum, int pageSize) {
        PageableResult<PtmCmpSku> pagedResult = dbm.queryPage(Q_PTM_CMPSKU, pageNum, pageSize, Arrays.asList(proId));
        return pagedResult;
    }

    @Override
    public PageableResult<PtmCmpSku> listOnsaleCmpSkus(long proId, int page, int size) {
        //outstock的sku也返回
        PageableResult<PtmCmpSku> pagedResult = dbm.queryPage(Q_ONSALE_PTM_CMPSKU, page, size, Arrays.asList(proId, 1.0f));
        return pagedResult;
    }
}