import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.search.SrmProductSearchCount;
import hasoffer.core.persistence.po.search.updater.SrmProductSearchCountUpdater;
import hasoffer.core.product.IProductService;
import hasoffer.core.product.solr.ProductIndexServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by chevy on 2016/7/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class TestMain {

    @Resource
    IDataBaseManager dbm;
    @Resource
    IProductService productService;
    @Resource
    ProductIndexServiceImpl productIndexService;

    @Test
    public void setsearchcounts() {
        // 初始化 srmproductsearchcount 表
        initData();

        // 根据 srmproductsearchcount 表的数据更新 solr
        String sql = "SELECT DISTINCT(t.productId) FROM SrmProductSearchCount t";

        List<Long> ids = dbm.query(sql);

        for (Long id : ids) {
            PtmProduct product = productService.getProduct(id);

            if (product != null) {
                productService.importProduct2Solr(product);
            }
        }
    }

    @Transactional
    private void initData() {
        List<SrmProductSearchCount> srmProductSearchCounts = dbm.query("select t from SrmProductSearchCount t");

        for (SrmProductSearchCount count : srmProductSearchCounts) {

            Long now = TimeUtils.now();

            long cou = now % 1000;

            SrmProductSearchCountUpdater updater = new SrmProductSearchCountUpdater(count.getId());
            updater.getPo().setCount(cou);
            dbm.update(updater);

            System.out.println(cou);
        }
    }

}
