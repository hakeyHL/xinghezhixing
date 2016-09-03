package hasoffer.core.test.basetest;

import hasoffer.base.model.Website;
import hasoffer.core.persistence.aws.AwsSummaryProduct;
import hasoffer.core.persistence.dbm.aws.AwsDynamoDbService;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.task.worker.IProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Date : 2016/6/15
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class DynamodbTest {

    AwsDynamoDbService awsDynamoDbService = new AwsDynamoDbService();

    @Resource
    IDataBaseManager dbm;

    @Test
    public void testCount2() {
        String queryStr = "id > :v1 and price < :v2";

        List params = new ArrayList();
        params.add(32);
        params.add(8090);

        long count = awsDynamoDbService.count(AwsSummaryProduct.class, queryStr, params);

        System.out.println(count);
    }

    @Test
    public void testCount() {
        long count = awsDynamoDbService.count(AwsSummaryProduct.class);

        System.out.println(count);
    }

    @Test
    public void testQuery() {

        String queryStr = " id > :v1 and price > :v2 ";

        List params = new ArrayList();
        params.add(20);
        params.add(500);

        long count = awsDynamoDbService.count(AwsSummaryProduct.class, queryStr, params);
        System.out.println(count);

//        PageableResult<AwsSummaryProduct> pageableResult = awsDynamoDbService.scan(AwsSummaryProduct.class, queryStr, params);
//
//        System.out.println(pageableResult.getNumFund());
//        for (AwsSummaryProduct asp : pageableResult.getData()) {
//            System.out.println(asp.getId() + "\t" + asp.getWebsite() + "\t" + asp.getlCreateTime());
//        }
    }

    @Test
    public void testQuery1() {

        String queryStr = "id > :val1 and website = :val2";

        List params = new ArrayList();
        params.add(20);
        params.add(Website.FLIPKART.name());

//        PageableResult<AwsSummaryProduct> pageableResult = awsDynamoDbService.scan(AwsSummaryProduct.class, queryStr, params);
//
//        System.out.println(pageableResult.getNumFund());
//        for (AwsSummaryProduct asp : pageableResult.getData()) {
//            System.out.println(asp.getId() + "\t" + asp.getWebsite() + "\t" + asp.getlCreateTime());
//        }
    }

    @Test
    public void testQuery2() {

        String queryStr = "id > :v1 and website = :v2";

        List params = new ArrayList();
        params.add(20);
        params.add(Website.FLIPKART.name());

        awsDynamoDbService.scanAll(AwsSummaryProduct.class, queryStr, params, new IProcessor<AwsSummaryProduct>() {
            @Override
            public void process(AwsSummaryProduct asp) {
                System.out.println(asp.getId() + "\t" + asp.getPrice());
            }
        });
    }

    @Test
    public void testDel() {
        awsDynamoDbService.deleteTable(AwsSummaryProduct.class);
    }

    @Test
    public void testUpdate() {
        awsDynamoDbService.updateTable(AwsSummaryProduct.class, 10, 10);
    }

    @Test
    public void testCreate() {
        awsDynamoDbService.createTable(AwsSummaryProduct.class);
//
//        System.out.println(awsDynamoDbService.descTable(AwsSummaryProduct.class));

        System.out.println("show tables...");
        List<String> tableNames = awsDynamoDbService.listTables();
        for (String tname : tableNames) {
            System.out.println(tname);
        }
    }

    @Test
    public void testLoad() {
        AwsSummaryProduct asp = awsDynamoDbService.get(AwsSummaryProduct.class, 1163);
        System.out.println(asp.toString());
    }

    @Test
    public void test2() {
        List<String> tableNames = awsDynamoDbService.listTables();
        for (String tname : tableNames) {
            System.out.println(tname);
        }
    }

    @Test
    public void init_datas() {
        String sql = "select t from PtmCmpSku t";

        List<PtmCmpSku> cmpskus = dbm.query(sql, 2, 2000);

        List<AwsSummaryProduct> awsSummaryProducts = new ArrayList<AwsSummaryProduct>();

        AwsSummaryProduct[] awsSummaryProducts1 = new AwsSummaryProduct[0];

        for (PtmCmpSku cmpSku : cmpskus) {
            System.out.println(cmpSku.toString());
            AwsSummaryProduct awsSummaryProduct = new AwsSummaryProduct(cmpSku);
            awsSummaryProducts.add(awsSummaryProduct);

            awsDynamoDbService.save(awsSummaryProduct);

        }
    }
}
