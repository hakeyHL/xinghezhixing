package hasoffer.core.cache;

import hasoffer.base.model.PageableResult;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.JSONUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.ptm.PtmTopSelling;
import hasoffer.core.product.IProductService;
import hasoffer.core.redis.ICacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Date : 2016/5/7
 * Function :
 */
@Component
public class ProductCacheManager {

    private static final Class CACHE_CLASS = PtmProduct.class;
    private static final String CACHE_KEY_PRE = "PRODUCT_";
    private static final long CACHE_EXPIRE_TIME = TimeUtils.SECONDS_OF_1_DAY;

    @Resource
    ICacheService<PtmProduct> cacheService;
    @Resource
    IProductService productService;
    Logger logger = LoggerFactory.getLogger(ProductCacheManager.class);

    /**
     * 根据商品ID查询商品
     *
     * @param id
     * @return
     */
    public PtmProduct getProduct(long id) {
        String key = CACHE_KEY_PRE + id;

        PtmProduct product = cacheService.get(CACHE_CLASS, key, 0);

        if (product == null) {
            product = productService.getProduct(id);
            if (product != null) {
                cacheService.add(key, product, CACHE_EXPIRE_TIME);
            }
        }

        return product;
    }

    /**
     * 根据商品ID查询商品主图
     *
     * @param id
     * @return
     */
    public String getProductMasterImageUrl(long id) {
        String key = CACHE_KEY_PRE + "_getProductMasterImageUrl_" + id;

        String imageUrl = cacheService.get(key, 0);
        if (imageUrl == null) {
            imageUrl = productService.getProductMasterImageUrl(id);
            if (imageUrl != null) {
                cacheService.add(key, imageUrl, CACHE_EXPIRE_TIME);
            }
        }

        return imageUrl;
    }

    /**
     * 根据商品ID，分页查询关联的比价sku
     * todo sku有价格更新时，需要更新其关联的比价列表
     *
     * @param proId
     * @param page
     * @param size
     * @return
     */
    public PageableResult<PtmCmpSku> listPagedCmpSkus(long proId, int page, int size) {
        String key = CACHE_KEY_PRE + "_listPagedCmpSkus_" + String.valueOf(proId) + "_" + page + "_" + size;
        String cmpSkusJson = cacheService.get(key, 0);
        PageableResult<PtmCmpSku> pagedCmpskus = null;
        try {
            if (StringUtils.isEmpty(cmpSkusJson)) {
                pagedCmpskus = productService.listOnsaleCmpSkus(proId, page, size);
                cacheService.add(key, JSONUtil.toJSON(pagedCmpskus), TimeUtils.SECONDS_OF_1_HOUR * 2);
            } else {
                PageableResult datas = (PageableResult<Map>) JSONUtil.toObject(cmpSkusJson, PageableResult.class);

                List<PtmCmpSku> cmpSkus = new ArrayList<PtmCmpSku>();
                List<Map> data = datas.getData();

                for (Map<String, Object> map : data) {
                    PtmCmpSku cmpSku = new PtmCmpSku();
                    String website = (String) map.get("website");
                    Double price = (Double) map.get("price");
                    Double cashBack = (Double) map.get("cashBack");
                    Double shipping = (Double) map.get("shipping");
                    if (StringUtils.isEmpty(website) || price == null) {
                        continue;
                    }

                    cmpSku.setId(Long.valueOf(map.get("id") + ""));
                    cmpSku.setProductId(Long.valueOf(map.get("productId") + ""));
                    cmpSku.setWebsite(Website.valueOf(website));
                    cmpSku.setSeller((String) map.get("seller"));
                    cmpSku.setSkuTitle((String) map.get("skuTitle"));
                    cmpSku.setTitle((String) map.get("title"));
                    cmpSku.setSupportPayMethod((String) map.get("supportPayMethod"));
                    cmpSku.setPrice(price.floatValue());
                    cmpSku.setCashBack(cashBack.floatValue());
                    cmpSku.setShipping(shipping.floatValue());
                    cmpSku.setRatings((Integer) map.get("ratings"));
                    cmpSku.setReturnDays((Integer) map.get("returnDays"));
                    cmpSku.setCommentsNumber(Long.valueOf(map.get("commentsNumber") + ""));
                    cmpSku.setRating((String) map.get("rating"));
                    cmpSku.setImagePath((String) map.get("imagePath"));
                    cmpSku.setSmallImagePath((String) map.get("smallImagePath"));
                    cmpSku.setOriImageUrl((String) map.get("oriImageUrl"));
                    cmpSku.setDeeplink((String) map.get("deeplink"));
                    cmpSku.setUrl((String) map.get("url"));
                    cmpSku.setOriUrl((String) map.get("oriUrl"));
                    cmpSku.setColor((String) map.get("color"));
                    cmpSku.setSize((String) map.get("size"));
                    cmpSku.setUpdateTime(new Date((Long) map.get("updateTime")));
                    cmpSku.setChecked((Boolean) map.get("checked"));
                    cmpSku.setSourcePid((String) map.get("sourcePid"));
                    cmpSku.setSourceSid((String) map.get("sourceSid"));
                    cmpSku.setStatus(SkuStatus.valueOf((String) map.get("status")));
                    cmpSkus.add(cmpSku);
                }
                pagedCmpskus = new PageableResult<PtmCmpSku>(cmpSkus, datas.getNumFund(), datas.getCurrentPage(), datas.getPageSize());
            }
        } catch (Exception e) {
            logger.error(" deal skus from cache error " + e.getMessage());
            return null;
        }
        return pagedCmpskus;
    }


    public List<PtmProduct> getTopSellins(int page, int size) {
        String key = CACHE_KEY_PRE + "_listPagedCmpSkus_TopSelling" + "_" + page + "_" + size;
        String ptmProductJson = cacheService.get(key, 0);
        List<PtmProduct> products = new ArrayList<PtmProduct>();
        try {
            if (StringUtils.isEmpty(ptmProductJson)) {
                Calendar calendar = Calendar.getInstance();
                Date date = new Date();
                calendar.setTime(date);
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 00, 00, 00);
                long todayStart = calendar.getTimeInMillis();
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1, 00, 00, 00);
                long yesterdayStart = calendar.getTimeInMillis();
                List<PtmTopSelling> ptmTopSellings = productService.getTopSellings(yesterdayStart, todayStart, page, size);
                for (PtmTopSelling ptmTopSelling : ptmTopSellings) {
                    PageableResult<PtmCmpSku> pageableResult = listPagedCmpSkus(ptmTopSelling.getId(), 0, 20);
                    if (pageableResult != null && pageableResult.getData() != null && pageableResult.getData().size() > 0) {
                        PtmProduct product = productService.getProduct(ptmTopSelling.getId());
                        if (product != null && product.getPrice() > 0) {
                            products.add(product);
                        }
                    }
                }
                if (products != null && products.size() > 0) {
                    cacheService.add(key, JSONUtil.toJSON(products), TimeUtils.SECONDS_OF_1_HOUR * 8);
                }
            } else {
                List<Map> datas = JSONUtil.toObject(ptmProductJson, List.class);
                for (Map map : datas) {
                    PtmProduct ptmProduct = new PtmProduct();
                    String website = (String) map.get("sourceSite");
                    Double price = (Double) map.get("price");
                    if (StringUtils.isEmpty(website) || price == null) {
                        continue;
                    }
                    ptmProduct.setId(Long.valueOf(map.get("id") + ""));
                    ptmProduct.setTitle((String) map.get("title"));
                    ptmProduct.setPrice(price.floatValue());
                    ptmProduct.setRating((Integer) map.get("rating"));
                    ptmProduct.setColor((String) map.get("color"));
                    ptmProduct.setSize((String) map.get("size"));
                    ptmProduct.setSourceUrl((String) map.get("sourceUrl"));
                    ptmProduct.setSourceSite((String) map.get("sourceSite"));
                    ptmProduct.setCategoryId(((Integer) map.get("categoryId")).longValue());
                    ptmProduct.setTag((String) map.get("tag"));
                    products.add(ptmProduct);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return products;
    }

    public PageableResult<PtmCmpSku> listCmpSkus(long proId, int page, int size) {
        String key = CACHE_KEY_PRE + "_listCmpSkus_" + String.valueOf(proId) + "_" + page + "_" + size;
        String cmpSkusJson = cacheService.get(key, 0);
        PageableResult<PtmCmpSku> pagedCmpskus = null;
        try {
            if (StringUtils.isEmpty(cmpSkusJson)) {
                pagedCmpskus = productService.listNotOffSaleCmpSkus(proId, page, size);
                cacheService.add(key, JSONUtil.toJSON(pagedCmpskus), TimeUtils.SECONDS_OF_1_HOUR * 2);
            } else {
                PageableResult datas = (PageableResult<Map>) JSONUtil.toObject(cmpSkusJson, PageableResult.class);

                List<PtmCmpSku> cmpSkus = new ArrayList<PtmCmpSku>();
                List<Map> data = datas.getData();

                for (Map<String, Object> map : data) {
                    PtmCmpSku cmpSku = new PtmCmpSku();
                    String website = (String) map.get("website");
                    Double price = (Double) map.get("price");
                    if (StringUtils.isEmpty(website) || price == null) {
                        continue;
                    }
                    cmpSku.setId(Long.valueOf(map.get("id") + ""));
                    cmpSku.setProductId(Long.valueOf(map.get("productId") + ""));
                    cmpSku.setWebsite(Website.valueOf(website));
                    cmpSku.setSeller((String) map.get("seller"));
                    cmpSku.setSkuTitle((String) map.get("skuTitle"));
                    cmpSku.setTitle((String) map.get("title"));
                    cmpSku.setPrice(price.floatValue());
                    cmpSku.setRating((String) map.get("rating"));
                    cmpSku.setImagePath((String) map.get("imagePath"));
                    cmpSku.setOriImageUrl((String) map.get("oriImageUrl"));
                    cmpSku.setDeeplink((String) map.get("deeplink"));
                    cmpSku.setSmallImagePath((String) map.get("smallImagePath"));
                    cmpSku.setUrl((String) map.get("url"));
                    cmpSku.setOriUrl((String) map.get("oriUrl"));
                    cmpSku.setColor((String) map.get("color"));
                    cmpSku.setSize((String) map.get("size"));
                    cmpSku.setUpdateTime(new Date((Long) map.get("updateTime")));
                    cmpSku.setChecked((Boolean) map.get("checked"));
                    cmpSku.setSourcePid((String) map.get("sourcePid"));
                    cmpSku.setSourceSid((String) map.get("sourceSid"));
                    cmpSku.setStatus(SkuStatus.valueOf((String) map.get("status")));
                    cmpSkus.add(cmpSku);
                }
                pagedCmpskus = new PageableResult<PtmCmpSku>(cmpSkus, datas.getNumFund(), datas.getCurrentPage(), datas.getPageSize());
            }
        } catch (Exception e) {
            logger.error(" deal skus from cache error " + e.getMessage());
            return null;
        }
        return pagedCmpskus;
    }
}
