package hasoffer.core.search;

import hasoffer.affiliate.affs.flipkart.FlipkartAffiliateProductProcessor;
import hasoffer.affiliate.model.AffiliateProduct;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.analysis.ProductAnalysisService;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.mongo.SrmAutoSearchResult;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.product.IProductService;
import hasoffer.fetch.core.IListProcessor;
import hasoffer.fetch.helper.WebsiteProcessorFactory;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.sites.mysmartprice.MspListProcessor;
import hasoffer.fetch.sites.mysmartprice.NewMspSkuCompareProcessor;
import hasoffer.fetch.sites.mysmartprice.model.MySmartPriceCmpSku;
import hasoffer.fetch.sites.mysmartprice.model.MySmartPriceProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Date : 2016/3/16
 * Function :
 */
@Service
public class SearchProductService {
    private static Logger logger = LoggerFactory.getLogger(SearchProductService.class);
    private static List<Website> websites = Arrays.asList(Website.FLIPKART, Website.SNAPDEAL, Website.SHOPCLUES, Website.PAYTM, Website.EBAY, Website.AMAZON);
    @Resource
    IMongoDbManager mdm;
    @Resource
    IProductService productService;
    @Resource
    ICmpSkuService cmpSkuService;

    public static void getProductsFromAffiliate(Map<Website, List<ListProduct>> listProductMap, String keyword) {
        try {
            getProductFromFlipkart(listProductMap, keyword);
        } catch (Exception e) {
            logger.error(String.format("error : search [%s] from [%s].Info [%s]", keyword, Website.FLIPKART, e.getMessage()));
        }
    }

    public static void getProductsFromMSP(Map<Website, List<ListProduct>> listProductMap, String keyword) {
        MspListProcessor listProcessor = new MspListProcessor();
        try {
            List<ListProduct> listProducts = listProcessor.getProductSetByKeyword(keyword, 5);

            for (ListProduct listProduct : listProducts) {

                if (ProductAnalysisService.stringMatch(keyword, listProduct.getTitle()) > 0) {

                    String url = listProduct.getUrl();

                    int index = url.indexOf("?");
                    if (index > 0) {
                        url = url.substring(0, index);
                    }

                    NewMspSkuCompareProcessor nmscp = new NewMspSkuCompareProcessor();
                    MySmartPriceProduct mspProduct = nmscp.parse(url);

                    List<MySmartPriceCmpSku> mspCmpSkus = mspProduct.getCmpSkus();

                    for (MySmartPriceCmpSku cmpSku : mspCmpSkus) {
                        Website website = cmpSku.getWebsite();
                        if (!websites.contains(website)) {
                            continue;
                        }

                        List<ListProduct> listProducts1 = listProductMap.get(website);
                        if (listProducts1 == null) {
                            listProducts1 = new ArrayList<ListProduct>();
                            listProductMap.put(website, listProducts1);
                        }

                        listProducts1.add(
                                new ListProduct(
                                        0L, "", cmpSku.getUrl(), "",
                                        listProduct.getTitle(),
                                        cmpSku.getPrice(),
                                        cmpSku.getWebsite(), ProductStatus.ONSALE
                                )
                        );
                    }

                    break;
                }
            }
        } catch (Exception e) {
            logger.error(String.format("error : search [%s] from [%s]. Info:[%s].", keyword, Website.MYSMARTPRICE, e.getMessage()));
        }
    }

    public static void getProductsFromWebsite(Map<Website, List<ListProduct>> listProductMap, String keyword) {
        keyword = StringUtils.getCleanWordString(keyword);
        //遍历websiteList，添加比较列表
        for (Website website : websites) {
            //todo shopclues反爬，先不抓，跳过
            if (website == Website.SHOPCLUES) {
                continue;
            }

            List<ListProduct> listProducts = listProductMap.get(website);
            if (listProducts == null) {
                listProducts = new ArrayList<ListProduct>();
                listProductMap.put(website, listProducts);
            }

            IListProcessor listProcessor = WebsiteProcessorFactory.getListProcessor(website);

            try {
                List<ListProduct> listProducts2 = listProcessor.getProductSetByKeyword(keyword, 10);

                logger.debug(String.format("found [%d] products. search[%s] from [%s].", listProducts2.size(), keyword, website.name()));

                if (ArrayUtils.hasObjs(listProducts2)) {
                    listProducts.addAll(listProducts2);
                }
            } catch (Exception e) {
                logger.error(String.format("error : search [%s] from [%s].Info : [%s]", keyword, website, e.getMessage()));
                continue;
            }
        }
    }

    public static void getProductFromFlipkart(Map<Website, List<ListProduct>> listProductMap, String keyword) throws Exception {
        Website website = Website.FLIPKART;

        List<ListProduct> listProducts = listProductMap.get(website);
        if (listProducts == null) {
            listProducts = new ArrayList<ListProduct>();
            listProductMap.put(website, listProducts);
        }

        FlipkartAffiliateProductProcessor affProcessor = new FlipkartAffiliateProductProcessor();
        List<AffiliateProduct> affPros = affProcessor.getAffiliateProductByKeyword(keyword, 10);

        for (AffiliateProduct affPro : affPros) {

            listProducts.add(
                    new ListProduct(0L,
                            affPro.getSourceId(),
                            affPro.getUrl(),
                            affPro.getImageUrl(),
                            affPro.getTitle(),
                            affPro.getPrice(),
                            affPro.getWebsite(),
                            ProductStatus.ONSALE)
            );
        }
    }

    public void saveSearchProducts(SrmAutoSearchResult searchResult) {

        //String keyword = searchResult.getTitle();
        //Website logSite = Website.valueOf(searchResult.getFromWebsite());

        //Map<Website, List<ListProduct>> listProductMap = new HashMap<Website, List<ListProduct>>();

        // voodoo - fuck voodoo - return durex
//        VoodooHelper.getProductsFromVoodoo(listProductMap, keyword);

        // read by affiliate api - size always null
//        getProductsFromAffiliate(listProductMap, keyword);

        // read from html
        //getProductsFromWebsite(listProductMap, keyword);

        // msp
//        getProductsFromMSP(listProductMap, keyword);

        //searchResult.setSitePros(listProductMap);
        logger.info("job result info ：{}", searchResult.toString());
        mdm.save(searchResult);
    }

    public SrmAutoSearchResult getSearchResult(SrmSearchLog searchLog) {
        return mdm.queryOne(SrmAutoSearchResult.class, searchLog.getId());
    }

    public SrmAutoSearchResult getSearchResultById(String id) {
        return mdm.queryOne(SrmAutoSearchResult.class, id);
    }
}
