package hasoffer.core.product.impl;

import hasoffer.affiliate.affs.AffiliateFactory;
import hasoffer.affiliate.affs.IAffiliateProcessor;
import hasoffer.affiliate.exception.AffiliateAPIException;
import hasoffer.affiliate.model.AffiliateProduct;
import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.exception.ERROR_CODE;
import hasoffer.core.product.IFetchService;
import hasoffer.core.search.exception.WebsiteErrorException;
import hasoffer.fetch.core.IImageProcessor;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.core.ISummaryProductProcessor;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.helper.WebsiteProcessorFactory;
import hasoffer.fetch.helper.WebsiteSummaryProductProcessorFactory;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.model.Product;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.sites.flipkart.FlipkartHelper;
import hasoffer.fetch.sites.snapdeal.SnapdealHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * Date : 2015/10/28
 */
@Service
public class FetchServiceImpl implements IFetchService {

    private Logger logger = LoggerFactory.getLogger(FetchServiceImpl.class);

    // 通过url抓取的service方法
    @Override
    public Product fetchByUrl(String url) {
        //获取真实website
        Website website = WebsiteHelper.getWebSite(url);
        //根据website获取对应实现
        IProductProcessor productProcessor = WebsiteProcessorFactory.getProductProcessor(website);
        //如果得到productProcessor为null，代表该网站暂不支持
        if (productProcessor == null) {
            return null;
        }
        //
        Product product = null;

        try {
            product = productProcessor.parseProduct(url);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return product;
    }

    @Override
    public OriFetchedProduct fetchSummaryProductByUrl(String url) throws HttpFetchException, ContentParseException, AffiliateAPIException, IOException, InterruptedException {

        url = URLDecoder.decode(url);

        Website website = WebsiteHelper.getWebSite(url);

        ISummaryProductProcessor summaryProductProcessor = WebsiteSummaryProductProcessorFactory.getSummaryProductProcessor(website);

        if (summaryProductProcessor == null) {
            return null;
        }

        //用来标记联盟解析是否成功
        boolean flag = true;
        OriFetchedProduct oriFetchedProduct = null;

        //如果是flipkart的商品，尝试用联盟解析
        if (Website.FLIPKART.equals(website) && url.contains("?pid=")) {
            flag = false;
            try {
                String sourceId = FlipkartHelper.getProductIdByUrl(url);
                oriFetchedProduct = getAffiliateSummaryProduct(website, sourceId);
            } catch (Exception e) {
                flag = true;
            }
        }

        //如果是snapdeal的商品，尝试用联盟解析
        if (Website.SNAPDEAL.equals(website)) {
            String[] subStr = url.split("#");
            url = subStr[0];
            flag = false;
            try {
                String sourceId = SnapdealHelper.getProductIdByUrl(url);
                oriFetchedProduct = getAffiliateSummaryProduct(website, sourceId);
            } catch (Exception e) {
                flag = true;
            }
        }

        if (flag) {
            oriFetchedProduct = summaryProductProcessor.getSummaryProductByUrl(url);
        }

        // 暂停使用amazon联盟
//        if (flag && !Website.AMAZON.equals(website)) {//联盟解析失败或者不适用联盟解析
//            summaryProduct = summaryProductProcessor.getSummaryProductByUrl(url);
//        } else if (flag && Website.AMAZON.equals(website)) {
//
//            try {
//                summaryProduct = summaryProductProcessor.getSummaryProductByUrl(url);
//            } catch (Exception e) {
//                if (e instanceof AmazonRobotCheckException) {
//                    logger.debug("AmazonRobotCheckException");
//                    TimeUnit.MINUTES.sleep(1);
//                }
//                String sourceId = AmazonHelper.getProductIdByUrl(url);
//                summaryProduct = getAffiliateSummaryProduct(website, sourceId, url);
//            }
//        }
        return oriFetchedProduct;
    }

    @Override
    public OriFetchedProduct udpateSkuInAnyWay(String url, Website website) throws IOException, HttpFetchException, ContentParseException {

        boolean flag = true;

        if (Website.FLIPKART.equals(website) || Website.SNAPDEAL.equals(website)) {

            String sourceSid = WebsiteHelper.getSkuIdFromUrl(website, url);

            if (!StringUtils.isEmpty(sourceSid)) {

                IAffiliateProcessor processor = AffiliateFactory.getAffiliateProductProcessor(website);

                AffiliateProduct affiliateProduct = null;
                try {
                    affiliateProduct = processor.getAffiliateProductBySourceId(sourceSid);
                } catch (AffiliateAPIException e) {
                    logger.debug("api parse fail for [" + sourceSid + "]" + " website [" + website + "]");
                    flag = false;
                }

                if (flag) {
                    ProductStatus status = null;
                    if (StringUtils.isEqual("true", affiliateProduct.getProductStatus())) {
                        status = ProductStatus.ONSALE;
                    } else if (StringUtils.isEqual("false", affiliateProduct.getProductStatus())) {
                        status = ProductStatus.OUTSTOCK;
                    }

                    OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();

                    oriFetchedProduct.setSourcePid("0");
                    oriFetchedProduct.setUrl(WebsiteHelper.getCleanUrl(website, affiliateProduct.getUrl()));
                    oriFetchedProduct.setImageUrl(affiliateProduct.getImageUrl());
                    oriFetchedProduct.setPrice(affiliateProduct.getPrice());
                    oriFetchedProduct.setWebsite(affiliateProduct.getWebsite());
//                此处要求联盟返回的title字段为空
//                fetchedProduct.setTitle(affiliateProduct.getTitle());
//                fetchedProduct.setSubTitle(affiliateProduct.getTitle());
                    oriFetchedProduct.setSourceSid(affiliateProduct.getSourceId());
                    oriFetchedProduct.setProductStatus(status);

                    return oriFetchedProduct;
                }
            }
        }

        if (WebsiteHelper.DEFAULT_WEBSITES.contains(website)) {

            ISummaryProductProcessor processor = WebsiteSummaryProductProcessorFactory.getSummaryProductProcessor(website);

            OriFetchedProduct oriFetchedProduct = processor.getSummaryProductByUrl(url);

            return oriFetchedProduct;
        }

        return null;
    }


    @Override
    public String fetchWebsiteImageUrl(Website website, String url) throws HttpFetchException, ContentParseException {

        IImageProcessor imageProcessor = WebsiteProcessorFactory.getImageProcessor(website);

        if (imageProcessor == null) {
            throw new WebsiteErrorException(ERROR_CODE.UNKNOWN, "website not correct");
        }

        return imageProcessor.getWebsiteImageUrl(url);

    }

    private OriFetchedProduct getAffiliateSummaryProduct(Website website, String sourceId) throws AffiliateAPIException, IOException {

        IAffiliateProcessor affiliateProductProcessor = AffiliateFactory.getAffiliateProductProcessor(website);
        AffiliateProduct affiliateProduct = affiliateProductProcessor.getAffiliateProductBySourceId(sourceId);

        if (affiliateProduct == null) {
            return null;
        }

        String imageUrl = affiliateProduct.getImageUrl();
        float price = affiliateProduct.getPrice();
        String title = affiliateProduct.getTitle();
        String productStatus = affiliateProduct.getProductStatus();
        String url = affiliateProduct.getUrl();

        OriFetchedProduct oriFetchedProduct = new OriFetchedProduct();
        oriFetchedProduct.setPrice(price);
        oriFetchedProduct.setSourceSid(sourceId);
        oriFetchedProduct.setImageUrl(imageUrl);
        oriFetchedProduct.setTitle(title);
        oriFetchedProduct.setUrl(url);

        //todo 商品状态对于flipkart联盟的解析有一些问题，待改进
        if ("false".equals(productStatus)) {
            oriFetchedProduct.setProductStatus(ProductStatus.OUTSTOCK);
        } else if ("true".equals(productStatus)) {
            oriFetchedProduct.setProductStatus(ProductStatus.ONSALE);
        } else if ("none".equals(productStatus)) {
            oriFetchedProduct.setProductStatus(ProductStatus.OFFSALE);
        } else {
            oriFetchedProduct.setProductStatus(null);
        }

        return oriFetchedProduct;
    }

}
