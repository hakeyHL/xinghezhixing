package hasoffer.core.test;

import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2016/4/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class ShopcluesKeywordFromUrl {

    @Resource
    IDataBaseManager dbm;

    Pattern URL_PATTERN = Pattern.compile("http://www.shopclues.com/(.*).html.*");
    Pattern KEYWORD_PATTERN = Pattern.compile("(.*)-\\d+");

    @Test
    public void testShopcluesKeywordFromUrl() {

        List<PtmCmpSku> skuList = dbm.query("SELECT t FROM PtmCmpSku t WHERE t.website = 'SHOPCLUES' ORDER BY t.id");

        for (PtmCmpSku sku : skuList) {
            sku.setUrl("http://www.shopclues.com/sansui-sa3521-smart-phone-black2.html");
            String keyword = getKeywordFromSkuUrl(sku.getUrl());

            System.out.println(sku.getUrl());
            System.out.println(sku.getTitle() + ":" + keyword);
            break;
        }

    }

    private String getKeywordFromSkuUrl(String url) {
        Matcher m = URL_PATTERN.matcher(url);
        if (m.matches()) {
            String keyword = m.group(1);
            if (StringUtils.isEmpty(keyword)) {
                return "";
            }

            Matcher m2 = KEYWORD_PATTERN.matcher(keyword);
            if (m2.matches()) {
                keyword = m2.group(1);
            }

            return keyword.replace("-", " ");
        }
        return "";
    }
}
