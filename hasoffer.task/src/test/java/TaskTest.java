import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.product.IFetchService;
import hasoffer.fetch.model.OriFetchedProduct;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created  on 2016/4/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class TaskTest {

    private static final String Q_SKU_PRODUCTID = "SELECT t FROM PtmCmpSku t WHERE t.productId = ?0 ";
    @Resource
    IDataBaseManager dbm;
    @Resource
    IFetchService fetchService;
    @Resource
    ICmpSkuService cmpSkuService;
    @Resource
    IMongoDbManager mongoDbManager;

    @Test
    public void test2() {

        Long productId = 2812L;

        List<PtmCmpSku> skus = dbm.query(Q_SKU_PRODUCTID, Arrays.asList(productId));

        for (PtmCmpSku sku : skus) {

            OriFetchedProduct oriFetchedProduct = null;
            try {

                if (TimeUtils.now() - sku.getUpdateTime().getTime() < TimeUtils.MILLISECONDS_OF_1_DAY) {
                    continue;
                }

                oriFetchedProduct = fetchService.udpateSkuInAnyWay(sku.getUrl(), sku.getWebsite());
                cmpSkuService.updateCmpSkuByOriFetchedProduct(sku.getId(), oriFetchedProduct);

            } catch (Exception e) {
                System.out.println("update sku fail for [" + sku.getId() + "]");
            }
        }

    }
}
