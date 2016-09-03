package hasoffer.core.test;

import hasoffer.base.utils.StringUtils;
import hasoffer.fetch.sites.shopclues.ShopcluesHelper;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created on 2016/4/21.
 */
public class ShopcluesOffsaleTest {

    @Test
    public void testShopcluesOffsale() {

        String url = "http://www.shopclues.com/rage-microsdhc-16-gb-class-10.html";
        float priceOrigin = 350;
        String keyword = ShopcluesHelper.getKeywordFromSkuUrl(url);

        float newPrice = 349;

        if (newPrice < priceOrigin * 0.8 || newPrice > priceOrigin * 1.2) {
            System.out.println("价格超出范围");
            return;
        }

        //匹配第一个单词
        String title = "Samsung MB-MA16D MicroSDHC 16GB Class 6 Memory Card (Black)";
        if (!matchedFirstWord(keyword, title)) {
            System.out.println("第一个单词不匹配");
            return;
        }

        //过滤特殊字符
        title = StringUtils.filterAndTrim(title, Arrays.asList("[", "]", ";", "%", "$", "@", "#", "(", ")")).replace("-", " ");
        keyword = StringUtils.filterAndTrim(keyword, Arrays.asList("[", "]", ";", "%", "$", "@", "#", "(", ")")).replace("-", " ");

        //求相似度
        float mc = StringUtils.wordMatchD(title, keyword);

        if (mc > 0.6) {
            System.out.println("相似度大于0.6");
            return;
        }
        System.out.println("相似度小于0.6");
        return;
    }

    /**
     * 判断一个sku和搜索结果的匹配度
     *
     * @return
     */
    private boolean matchedFirstWord(String title, String keyword) {

        keyword = StringUtils.toLowerCase(keyword);
        title = StringUtils.toLowerCase(title);

        keyword = StringUtils.filterAndTrim(keyword, Arrays.asList("[", "]", ";", "%", "$", "@", "#", "(", ")"));
        title = StringUtils.filterAndTrim(title, Arrays.asList("[", "]", ";", "%", "$", "@", "#", "(", ")"));

        //匹配第一个单词
        String[] subStr1 = keyword.split(" ");
        String[] subStr2 = title.split(" ");

        if (StringUtils.isEqual(subStr1[0], subStr2[0])) {
            return true;
        } else {
            return false;
        }
    }

}
