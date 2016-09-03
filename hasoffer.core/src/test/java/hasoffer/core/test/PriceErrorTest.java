package hasoffer.core.test;

import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import jodd.io.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2016/5/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class PriceErrorTest {

    @Resource
    IDataBaseManager dbm;

    @Test
    public void testErrorPriceSku() throws IOException {

        final String Q_PRODUCT_BYID = "SELECT t FROM PtmProduct t WHERE t.id = ?0 ";
        final String Q_SKU_BYPRODUCTID = "SELECT t FROM PtmCmpSku t WHERE t.productId = ?0 ";

        String[] strings = FileUtil.readLines("C:/Users/wing/Desktop/title.txt");


        for (String id : strings) {

            StringBuilder stringBuilder = new StringBuilder();
            List<PtmCmpSku> skus = dbm.query(Q_SKU_BYPRODUCTID, Arrays.asList(Long.parseLong(id)));
            PtmProduct product = dbm.querySingle(Q_PRODUCT_BYID, Arrays.asList(Long.parseLong(id)));

            if (skus.size() > 0) {
                stringBuilder.append(id + "\t");
            }

            float basePrice = -5.5f;

            for (int i = skus.size()-1; i >= 0; i--) {

                if (StringUtils.isEmpty(skus.get(i).getTitle())) {
                    continue;
                }

                if (skus.get(i).getTitle().equals(product.getTitle())) {
                    basePrice = skus.get(i).getPrice();
                    stringBuilder.append(basePrice+"\t");
                }

                if (skus.get(i).getPrice() == 0.0f) {
                    skus.remove(skus.get(i));
                }

            }

            if (basePrice == -5.5f) {
                stringBuilder.append("no title like this in skus\t");
            } else if (skus.size() <= 1) {
                stringBuilder.append("only one or zero left in skus\t");
            } else {
                for (PtmCmpSku sku : skus) {

                    if (sku.getPrice() > basePrice * 1.6 || sku.getPrice() < basePrice * 0.6) {
                        continue;
                    } else {
                        stringBuilder.append(sku.getId() + "\t" + sku.getPrice() + "\t");
                    }
                }
            }

            FileUtil.appendString("C:/Users/wing/Desktop/error.txt", stringBuilder.toString() + "\n");
        }

    }

}
