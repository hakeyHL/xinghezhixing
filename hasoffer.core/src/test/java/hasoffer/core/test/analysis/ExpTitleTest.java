package hasoffer.core.test.analysis;

import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.product.IProductService;
import jodd.io.FileUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by chevy on 2016/7/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class ExpTitleTest {

    private static final String Q_CATE = "SELECT t FROM PtmCategory t WHERE t.level <= 3 ORDER BY t.level";

    private static final String Q_PRODUCT = "SELECT COUNT(t.id) FROM PtmProduct t WHERE t.categoryId=?0";

    final Map<Long, PtmCategory> cateMap1 = new HashMap<Long, PtmCategory>();
    final Map<Long, PtmCategory> cateMap2 = new HashMap<Long, PtmCategory>();
    final Map<Long, PtmCategory> cateMap3 = new HashMap<Long, PtmCategory>();
    final List<PtmCategory> cates = new ArrayList<PtmCategory>();
    final List<Long> cateIds = new ArrayList<Long>();

    @Resource
    IDataBaseManager dbm;
    @Resource
    IProductService productService;

    @Test
    public void getI6() throws Exception {
        initCateMap();

        List<Long> stdCates = new ArrayList<>();
        stdCates.add(1L);
        stdCates.add(257L);
        stdCates.add(4662L);
        stdCates.add(1504L);
        stdCates.add(2334L);

        String U_CATEGORY = "UPDATE ptmcategory SET STD=0 WHERE id=%d;";
        String U_PRODUCT = "UPDATE ptmproduct SET STD=%d WHERE categoryid=%d;";

        int len = cates.size();
        for (int i = 0; i < len; i++) {

            PtmCategory cate = cates.get(i);

            boolean std = stdCates.contains(cate.getId()) || stdCates.contains(cate.getParentId());

            if (!std) {
                String sql1 = String.format(U_CATEGORY, cate.getId());
                System.out.println(sql1);
            }

            String sql2 = String.format(U_PRODUCT, std ? 1 : 0, cate.getId());
            System.out.println(sql2);
        }

        System.out.println("all finished.");
    }

    @Test
    public void setStdbyFile() throws IOException {
        File file_ori = new File("d:/datas/hasoffer/predicted_values.txt");
        List<String> lines = FileUtils.readLines(file_ori);

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        int index = 0;
        int count_0 = 0;
        int count_1 = 0;
        for (int i = 0, len = lines.size(); i < len; i = i + 2) {
            String keyStr = lines.get(i).trim();
            String valStr = lines.get(i + 1).trim();

            long key = Long.parseLong(keyStr);
            long val = Long.parseLong(valStr);

            System.out.println(key + "\t" + val);

            if (val == 0) {
                count_0++;
                if (count_0 % 200 == 0) {
                    PtmProduct product = productService.getProduct(key);
                    if (product != null && !StringUtils.isEmpty(product.getTitle())) {
                        sb1.append(val + "\t" + getTitle(product.getTitle())).append("\n");
                    }
                }
            } else {
                count_1++;
                if (count_1 % 200 == 0) {
                    PtmProduct product = productService.getProduct(key);
                    if (product != null && !StringUtils.isEmpty(product.getTitle())) {
                        sb2.append(val + "\t" + getTitle(product.getTitle())).append("\n");
                    }
                }
            }
        }

        System.out.println(count_0 + "\t" + count_1);

        String t0 = "d:/datas/test/t0.txt";
        String t1 = "d:/datas/test/t1.txt";

        File f0 = createFile(t0, true);
        File f1 = createFile(t1, true);

        FileUtils.write(f0, sb1.toString());
        FileUtils.write(f1, sb2.toString());
    }

    @Test
    public void getI2() {
        initCateMap();

        String fileDir = "d:/datas/hasoffer/";

        File file1;
        try {
            file1 = createFile(fileDir + "nocate_id_titles", true);
        } catch (Exception e) {
            System.out.println("error in create file");
            return;
        }

        int page = 1, PAGE_SIZE = 2000;

        PageableResult<PtmProduct> pagedPros = productService.listPagedProducts(page, PAGE_SIZE);

        long totalPage = pagedPros.getTotalPage();

        while (page <= totalPage) {
            if (page > 1) {
                pagedPros = productService.listPagedProducts(page, PAGE_SIZE);
            }

            List<PtmProduct> products = pagedPros.getData();

            if (ArrayUtils.hasObjs(products)) {
                StringBuilder sb = new StringBuilder();
                for (PtmProduct o : products) {
                    if (StringUtils.isEmpty(o.getTitle()) || cateIds.contains(o.getCategoryId())) {
                        System.out.println(String.format("[%s].[%d]...CONTINUE...", o.getTitle(), o.getCategoryId()));
                        continue;
                    }

                    String newTitle = getTitle(o.getTitle());

                    sb.append(o.getId())
                            .append(" ")
                            .append(newTitle)
                            .append("\n");
                }

                try {
                    FileUtil.appendString(file1, sb.toString());
                } catch (IOException e) {
                    System.out.println(String.format("error[IO ERROR] in exp to file"));
                }
            }

            page++;
        }

        System.out.println("all finished.");
    }

    private String getTitle(String title) {
        return title.replaceAll("[\\n\\r]", "");
    }

    @Test
    public void test() throws Exception {
        String fileDir = "d:/datas/hasoffer/";
        File file1 = null;
        try {
            file1 = createFile(fileDir + "testtesttest", true);
        } catch (Exception e) {
            System.out.println("error in create file");
            return;
        }

        PtmProduct product = dbm.get(PtmProduct.class, 611437L);

        String newTitle = getTitle(product.getTitle());

        StringBuilder sb = new StringBuilder();
        sb.append("0")
                .append(" ")
                .append(newTitle)
                .append("\n");

        System.out.println(sb.toString());

        FileUtils.write(file1, sb.toString(), true);
    }

    @Test
    public void getI5() throws Exception {
        initCateMap();

        String fileDir = "d:/datas/hasoffer/";

        List<Long> stdCates = new ArrayList<>();
        stdCates.add(1L);
        stdCates.add(257L);
        stdCates.add(4662L);
        stdCates.add(1504L);
        stdCates.add(2334L);

        File file1 = null;
        try {
            file1 = createFile(fileDir + "title_if_std", true);
        } catch (Exception e) {
            System.out.println("error in create file");
            return;
        }

        int len = cates.size();
        for (int i = 0; i < len; i++) {

            PtmCategory cate = cates.get(i);

            boolean std = stdCates.contains(cate.getId()) || stdCates.contains(cate.getParentId());

            System.out.println(String.format("exp No.[%d] cate[%d] to files", i, cate.getId()));

            List<PtmProduct> products = productService.listProducts(cate.getId(), 1, Integer.MAX_VALUE);

            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (PtmProduct o : products) {
                if (StringUtils.isEmpty(o.getTitle())) {
                    continue;
                }

                String newTitle = o.getTitle().replaceAll("[\\n\\r]", "");

                sb.append(std ? "1" : "0")
                        .append(" ")
                        .append(newTitle)
                        .append("\n");

                if (count % 2000 == 0) {
                    FileUtil.appendString(file1, sb.toString());
                    sb = new StringBuilder();
                }

                count++;
            }

            FileUtil.appendString(file1, sb.toString());
        }

        System.out.println("all finished.");
    }

    @Test
    public void getI4() {
        initCateMap();

        String fileDir = "D:/datas/hasoffer/";

        File file1 = null;
        try {
            file1 = createFile(fileDir + "titles_in_1st_cate", true);
        } catch (Exception e) {
            System.out.println("error in create file");
            return;
        }

        int len = cates.size();
        for (int i = 0; i < len; i++) {
            PtmCategory cate = cates.get(i);

            long cate_1st_id = cate.getId();

            System.out.println(String.format("exp No.[%d] cate[%d] to files", i, cate.getId()));
            if (cate.getLevel() == 2) {
                cate_1st_id = cate.getParentId();
            } else if (cate.getLevel() == 3) {
                PtmCategory cate2 = cateMap2.get(cate.getParentId());
                cate_1st_id = cate2.getParentId();
            }

            List<PtmProduct> products = productService.listProducts(cate.getId(), 1, Integer.MAX_VALUE);

            for (PtmProduct o : products) {
                if (StringUtils.isEmpty(o.getTitle())) {
                    continue;
                }

                try {
                    FileUtil.appendString(file1, StringUtils.filterAndTrim(cate_1st_id + " " + o.getTitle(), Arrays.asList("\n")) + "\n");
                } catch (IOException e) {
                    System.out.println(String.format("error[IO ERROR] in exp to file[%s].[%d]", o.getTitle(), o.getCategoryId()));
                }
            }
        }

        System.out.println("all finished.");
    }

    @Test
    public void getI3() {
        String fileDir = "D:/workspace/datas/hasoffer/";

        File file1;
        try {
            file1 = createFile(fileDir + "all_titles", true);
        } catch (Exception e) {
            System.out.println("error in create file");
            return;
        }

        int page = 1, PAGE_SIZE = 2000;

        PageableResult<PtmProduct> pagedPros = productService.listPagedProducts(page, PAGE_SIZE);
        List<PtmProduct> products = pagedPros.getData();
        long totalPage = pagedPros.getTotalPage();

        while (page <= totalPage) {
            if (page > 1) {
                pagedPros = productService.listPagedProducts(page, PAGE_SIZE);
//                break;
            }

            products = pagedPros.getData();

            if (ArrayUtils.hasObjs(products)) {
                for (PtmProduct o : products) {
                    if (StringUtils.isEmpty(o.getTitle())) {
                        System.out.println(String.format("[%s].[%d]...CONTINUE...", o.getTitle(), o.getCategoryId()));
                        continue;
                    }

                    try {
                        FileUtil.appendString(file1, StringUtils.filterAndTrim(o.getTitle(), Arrays.asList("\n")) + "\n");
                    } catch (IOException e) {
                        System.out.println(String.format("error[IO ERROR] in exp to file[%s].[%d]", o.getTitle(), o.getCategoryId()));
                    }
                }
            }

            page++;
        }

        System.out.println("all finished.");
    }

    @Test
    public void getI() {
        initCateMap();

        String fileDir = "E:/data-match/exp/";

        File file1 = null;
        try {
            file1 = createFile(fileDir + "hastitle", true);
        } catch (Exception e) {
            System.out.println("error in create file");
            return;
        }

        int len = cates.size();
        for (int i = 0; i < len; i++) {
            PtmCategory cate = cates.get(i);

            System.out.println(String.format("exp No.[%d] cate[%d] to files", i, cate.getId()));

            List<PtmProduct> products = productService.listProducts(cate.getId(), 1, Integer.MAX_VALUE);

            for (PtmProduct o : products) {
                if (StringUtils.isEmpty(o.getTitle())) {
                    continue;
                }

                try {
                    FileUtil.appendString(file1, StringUtils.filterAndTrim(o.getCategoryId() + " " + o.getTitle(), Arrays.asList("\n")) + "\n");
                } catch (IOException e) {
                    System.out.println(String.format("error[IO ERROR] in exp to file[%s].[%d]", o.getTitle(), o.getCategoryId()));
                }
            }
        }

        System.out.println("all finished.");
    }

    private File createFile(String filePath, boolean delIfExists) throws IOException {
        File file1 = new File(filePath);
        if (file1.exists()) {
            if (delIfExists) {
                file1.delete();
            }
        }
        file1.createNewFile();
        return file1;
    }

    public void initCateMap() {
        List<PtmCategory> categories = dbm.query(Q_CATE);

        for (PtmCategory category : categories) {
            Map<Long, PtmCategory> cateMap = null;
            int level = category.getLevel();
            switch (level) {
                case 1:
                    cateMap = cateMap1;
                    break;
                case 2:
                    if (!cateMap1.containsKey(category.getParentId())) {
                        continue;
                    }
                    cateMap = cateMap2;
                    break;
                case 3:
                    if (!cateMap2.containsKey(category.getParentId())) {
                        continue;
                    }
                    cateMap = cateMap3;
                    break;
                default:
                    break;
            }
            if (cateMap != null) {
                if (category.getLevel() == 3) {
                    long count = dbm.querySingle(Q_PRODUCT, Arrays.asList(category.getId()));
                    if (count == 0) {
                        continue;
                    }
                }
                cateMap.put(category.getId(), category);
                cates.add(category);
                cateIds.add(category.getId());
            }
        }

        showMap(cateMap1);
        showMap(cateMap2);
        showMap(cateMap3);
    }

    private void showMap(Map<Long, PtmCategory> cateMap) {
        System.out.println("... show category map ...");
        for (Map.Entry<Long, PtmCategory> cate : cateMap.entrySet()) {
//            long count = dbm.querySingle(Q_PRODUCT, Arrays.asList(cate.getValue().getId()));
//            System.out.println(cate.getKey() + "\t" + cate.getValue().getName() + "\t" + count);
            System.out.println(cate.getKey() + "\t" + cate.getValue().getName());
        }
        System.out.println(".........................");
    }
}
