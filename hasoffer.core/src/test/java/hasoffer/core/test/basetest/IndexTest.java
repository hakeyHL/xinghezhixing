package hasoffer.core.test.basetest;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.PageableResult;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HexDigestUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.exception.CmpSkuUrlNotFoundException;
import hasoffer.core.exception.MultiUrlException;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.StatHijackFetch;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmCmpSkuIndex2;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.ProductStatus;
import hasoffer.fetch.sites.flipkart.FlipkartHelper;
import hasoffer.fetch.sites.flipkart.FlipkartListProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created  on 2016/6/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class IndexTest {

    @Resource
    ICmpSkuService cmpSkuService;
    @Resource
    IMongoDbManager mdm;
    @Resource
    IDataBaseManager dbm;

    @Test
    public void testIndex() {

//        Website website = Website.SNAPDEAL;
        Website website = Website.FLIPKART;
        String cliQ = "Karbonn K2S Dual Sim - Black & Red(Black)";
        String sourceSid = "MOBDMEQVZJEMWTDG";

        PtmCmpSkuIndex2 cmpSkuIndex2 = cmpSkuService.getCmpSkuIndex2(website, sourceSid, cliQ);

        if (cmpSkuIndex2 != null) {
            System.out.println("success");
        } else {
            System.out.println("null");
        }

    }

    @Test
    public void testindex2() throws HttpFetchException, ContentParseException {

        List<PtmCmpSku> skuList = new ArrayList<PtmCmpSku>();

        String cliQ = "Envie BEETLE Camera Battery Charger";

        FlipkartListProcessor processor = new FlipkartListProcessor();

        List<ListProduct> productList = processor.getProductSetByKeyword(cliQ, 10);

        for (ListProduct product : productList) {
            String cleanChars1 = StringUtils.getCleanChars(product.getTitle());
            String cleanChars2 = StringUtils.getCleanChars(cliQ);
            if (StringUtils.isEqual(cleanChars1, cleanChars2)) {

                if (ProductStatus.OFFSALE.equals(product.getStatus())) {
                    continue;
                }

                SkuStatus skuStatus = null;
                if (ProductStatus.OUTSTOCK.equals(product.getStatus())) {
                    skuStatus = skuStatus.OUTSTOCK;
                } else {
                    skuStatus = skuStatus.ONSALE;
                }

                PtmCmpSku sku = new PtmCmpSku();

                sku.setProductId(0);
                sku.setUpdateTime(TimeUtils.nowDate());
                sku.setUrl(FlipkartHelper.getCleanUrl(product.getUrl()));
                sku.setOriImageUrl(product.getImageUrl());
                sku.setPrice(product.getPrice());
                sku.setWebsite(product.getWebsite());
                sku.setTitle(product.getTitle());
                sku.setSkuTitle(product.getTitle());
                sku.setSourceSid(product.getSourceId());
                sku.setStatus(skuStatus);

                skuList.add(sku);
            }
        }

        System.out.println();
    }

    @Test
    public void test3() {


        long totalAmount = 0;
        long multiUrlAmount = 0;
        long nullUrlAmount = 0;
        long successAmount = 0;
        long failAmount = 0;

        int curPage = 1;
        int pageSize = 1000;

        Query query = new Query();
        query.addCriteria(Criteria.where("website").is(Website.FLIPKART));//2.使用flipkart网站过滤，
        query.addCriteria(Criteria.where("lCreateTime").gt(1465142400000L).lt(1465228800000L));//1.先将2016-06-06 00:00:00到2016-06-07 00:00:00的StatHijackFetch查出来，

        PageableResult<StatHijackFetch> pageableResult = mdm.queryPage(StatHijackFetch.class, query, curPage, pageSize);

        long totalPage = pageableResult.getTotalPage();
        List<StatHijackFetch> data = pageableResult.getData();

        while (curPage <= totalPage) {

            if (curPage > 1) {
                pageableResult = mdm.queryPage(StatHijackFetch.class, query, curPage, pageSize);
                data = pageableResult.getData();
            }

            totalAmount += data.size();

            for (StatHijackFetch statHijackFetch : data) {

                //3.然后将当天的数据的sourceId和cliq再跑一遍，统计
                String cliQ = statHijackFetch.getCliQ();
                Website website = statHijackFetch.getWebsite();
                String sourceId = statHijackFetch.getSourceId();

                PtmCmpSkuIndex2 cmpSkuIndex2 = null;

                try {

                    cmpSkuIndex2 = cmpSkuService.getCmpSkuIndex2(website, sourceId, cliQ);

                    if (cmpSkuIndex2 != null) {
                        successAmount++;
                    } else {
                        failAmount++;
                    }

                } catch (MultiUrlException e) {

                    multiUrlAmount++;

                } catch (CmpSkuUrlNotFoundException e) {

                    nullUrlAmount++;

                }

            }

            System.out.println("curPage=" + curPage);
            curPage++;
        }

        System.out.println("totalAmount=" + totalAmount);
        System.out.println("multiUrlAmount=" + multiUrlAmount);
        System.out.println("nullUrlAmount=" + nullUrlAmount);
        System.out.println("successAmount=" + successAmount);
        System.out.println("failAmount=" + failAmount);
    }


    @Test
    public void test4() {

        int curPage = 1;
        int pageSize = 1000;

        PageableResult<PtmCmpSkuIndex2> pageableResult = dbm.queryPage("SELECT t FROM PtmCmpSkuIndex2 t ORDER BY t.id ASC", curPage, pageSize);

        long totalPage = pageableResult.getTotalPage();
        List<PtmCmpSkuIndex2> indexList = pageableResult.getData();

        while (curPage <= totalPage) {

            if (curPage > 1) {
                pageableResult = dbm.queryPage("SELECT t FROM PtmCmpSkuIndex2 t ORDER BY t.id ASC", curPage, pageSize);
                indexList = pageableResult.getData();
            }

            for (PtmCmpSkuIndex2 index : indexList) {

                String skuTitle = index.getSkuTitle();
                Website website = index.getWebsite();

                String siteSkutitleIndex = HexDigestUtil.md5(website.name() + StringUtils.getCleanChars(skuTitle));

                String oriSiteSkuTitleIndex = index.getSiteSkuTitleIndex();

                if (!StringUtils.isEqual(siteSkutitleIndex, oriSiteSkuTitleIndex)) {
                    System.out.println("id =" + index.getId());
//                    System.out.println("ori =" + oriSiteSkuTitleIndex);
//                    System.out.println("new =" + siteSkutitleIndex);
                }
            }
            curPage++;
        }
    }
}
