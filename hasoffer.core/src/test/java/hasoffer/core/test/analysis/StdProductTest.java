package hasoffer.core.test.analysis;

import hasoffer.affiliate.model.FlipkartSkuInfo;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.ptm.PtmStdProduct;
import hasoffer.core.persistence.po.search.SrmProductSearchCount;
import hasoffer.core.product.IStdProductService;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by chevy on 2016/8/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class StdProductTest {

    @Resource
    IStdProductService stdProductService;

    @Resource
    IDataBaseManager dbm;

    @Test
    public void expProductTitles() throws Exception {
        final String Q_P = "SELECT t FROM SrmProductSearchCount t WHERE t.ymd=?0";

//        String ymd = "20160812";
//        File file = new File("/home/work/std.log");
        String ymd = "20160720";
        File file = hasoffer.base.utils.FileUtils.createFile("d:/tmp/std.log", true);

        StringBuffer sb = new StringBuffer();

        List<SrmProductSearchCount> spscs = dbm.query(Q_P, Arrays.asList(ymd));

        int count = 1;
        for (SrmProductSearchCount spsc : spscs) {
            PtmProduct product = dbm.get(PtmProduct.class, spsc.getProductId());

            if (product != null && StringUtils.isEmpty(product.getTitle())) {
                sb.append(product.getTitle()).append("\n");
                System.out.println(product.getTitle());
            } else {
                continue;
            }

            count++;
            if (count % 1000 == 0) {
                System.out.println("write to file..............");
                FileUtils.writeStringToFile(file, sb.toString());
                sb = new StringBuffer();
            }
        }

        FileUtils.writeStringToFile(file, sb.toString());
    }

    @Test
    public void buildStdRespBySearchLog() throws Exception {

        File keywordFile = new File("D:\\datas\\fetch\\mobiles\\title_2.txt");

        File okFile = hasoffer.base.utils.FileUtils.createFile("D:\\datas\\fetch\\mobiles\\std_ok.log", true);
        File errFile = hasoffer.base.utils.FileUtils.createFile("D:\\datas\\fetch\\mobiles\\std_err.log", true);

        StringBuffer sb_ok = new StringBuffer();
        StringBuffer sb_err = new StringBuffer();

        List<String> keywords = FileUtils.readLines(keywordFile);

        int count = 1;
        for (String keyword : keywords) {
            System.out.println(keyword);
            String keyword_2 = keyword.toLowerCase().trim();

            if (StringUtils.isEmpty(keyword_2)) {
                continue;
            }

            int tryTimes = 1;
            while (tryTimes++ <= 3) {
                try {
                    if (tryTimes > 2) {
                        System.out.print("retry - ");
                    }
                    System.out.println("search - " + keyword);

                    Map<String, FlipkartSkuInfo> skuInfoMap = stdProductService.searchSku(keyword_2);

                    PtmStdProduct sp = stdProductService.createStd(skuInfoMap);
                    if (sp != null) {
                        sb_ok.append(keyword_2).append("\t")
                                .append("OK").append("\t")
                                .append(sp.getId()).append("\t")
                                .append(sp.getTitle()).append("\t")
                                .append(sp.getBrand()).append("\t")
                                .append(sp.getModel()).append("\t")
                                .append("\n");
                    } else {
                        sb_err.append(keyword_2).append("\t")
                                .append("NULL").append("\n");
                    }

                    break;
                } catch (Exception e) {
                    if (tryTimes > 3) {
                        sb_err.append(keyword).append("\t")
                                .append("ERROR").append("\n");
                    }
                    System.out.println(keyword + "...create error...");
                }
            }

            count++;
            if (count % 10 == 0) {
                FileUtils.writeStringToFile(okFile, sb_ok.toString());
                sb_ok = new StringBuffer();

                FileUtils.writeStringToFile(errFile, sb_err.toString());
                sb_err = new StringBuffer();
            }
        }

        FileUtils.writeStringToFile(okFile, sb_ok.toString());
        FileUtils.writeStringToFile(errFile, sb_err.toString());
    }

    @Test
    public void buildStdResp() throws Exception {
        final String Q_P = "SELECT t FROM SrmProductSearchCount t WHERE t.ymd=?0";

        File file = hasoffer.base.utils.FileUtils.createFile("d:/tmp/std.log", true);

        StringBuffer sb = new StringBuffer();

        List<SrmProductSearchCount> spscs = dbm.query(Q_P, Arrays.asList("20160720"));
        int count = 1;
        for (SrmProductSearchCount spsc : spscs) {
            PtmProduct product = dbm.get(PtmProduct.class, spsc.getProductId());
            if (product == null) {
                System.out.println(String.format("%d not exists.", spsc.getProductId()));
                continue;
            }

            String keyword = product.getTitle();
            String keyword_2 = keyword.toLowerCase().trim();

            int tryTimes = 1;
            while (tryTimes++ <= 3) {
                try {
                    Map<String, FlipkartSkuInfo> skuInfoMap = stdProductService.searchSku(keyword_2);

                    if (tryTimes > 2) {
                        System.out.print("retry - ");
                    }
                    System.out.println(keyword);

                    PtmStdProduct sp = stdProductService.createStd(skuInfoMap);
                    if (sp != null) {
                        sb.append(keyword_2).append("\t").append("OK").append("\t")
                                .append(sp.getId()).append("\t")
                                .append(sp.getTitle()).append("\t")
                                .append(sp.getBrand()).append("\t")
                                .append(sp.getModel()).append("\t");
                    } else {
                        sb.append(keyword_2).append("\t").append("NULL");
                    }
                    sb.append("\n");
                    break;
                } catch (Exception e) {
                    if (tryTimes > 3) {
                        sb.append(keyword_2).append("\t").append("ERROR").append("\n");
                    }
                    System.out.println(keyword + "...create error...");
                }
            }

            count++;
            if (count % 1000 == 0) {
                FileUtils.writeStringToFile(file, sb.toString());
                sb = new StringBuffer();
            }
        }

        FileUtils.writeStringToFile(file, sb.toString());
    }

    @Test
    public void getFlipkartProduct() throws Exception {
        String keyword = "Chilli B03";

        Map<String, FlipkartSkuInfo> skuInfoMap = stdProductService.searchSku(keyword);

        stdProductService.createStd(skuInfoMap);
    }

}
