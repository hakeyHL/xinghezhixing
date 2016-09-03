package hasoffer.core.test;

import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.core.persistence.po.ptm.PtmCategory3;
import hasoffer.core.product.ICategoryService;
import hasoffer.core.product.solr.CategoryIndexServiceImpl;
import hasoffer.core.product.solr.CategoryModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Date : 2016/1/18
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class CategoryTest {

    private final static String Q_CATE_PARENTID_LEVEL_1 =
            "SELECT t FROM PtmCategory t WHERE t.parentId=?0 AND t.level=?1 ORDER BY t.rank ASC";
    private final static String Q_CATE_PARENTID_LEVEL_2 =
            "SELECT t FROM PtmCategory3 t WHERE t.parentId=?0 AND t.level=?1";

    @Resource
    IDataBaseManager dbm;
    @Resource
    ICategoryService categoryService;
    @Resource
    CategoryIndexServiceImpl categoryIndexService;

    @Test
    public void showCate1() {
        List<PtmCategory> cates1 = dbm.query(Q_CATE_PARENTID_LEVEL_1, Arrays.asList(0L, 1));

        for (PtmCategory cate1 : cates1) {
            List<PtmCategory> cates2 = dbm.query(Q_CATE_PARENTID_LEVEL_1, Arrays.asList(cate1.getId(), 2));

            for (PtmCategory cate2 : cates2) {
                List<PtmCategory> cates3 = dbm.query(Q_CATE_PARENTID_LEVEL_1, Arrays.asList(cate2.getId(), 3));

                System.out.print(cate1.getId() + "_" + cate1.getName() + "\t");
                System.out.print(cate2.getId() + "_" + cate2.getName() + "\t");

                System.out.println();

                for (PtmCategory cate3 : cates3) {
                    System.out.print(cate1.getId() + "_" + cate1.getName() + "\t");
                    System.out.print(cate2.getId() + "_" + cate2.getName() + "\t");
                    System.out.print(cate3.getId() + "_" + cate3.getName() + "\t");
                    System.out.println();
                }
            }
        }

    }

    @Test
    public void showCate2() {
        List<PtmCategory3> cates1 = dbm.query(Q_CATE_PARENTID_LEVEL_2, Arrays.asList(0L, 1));

        for (PtmCategory3 cate1 : cates1) {
            List<PtmCategory3> cates2 = dbm.query(Q_CATE_PARENTID_LEVEL_2, Arrays.asList(cate1.getId(), 2));

            for (PtmCategory3 cate2 : cates2) {
                System.out.print(cate1.getId() + "_" + cate1.getName() + "\t");
                System.out.print(cate2.getId() + "_" + cate2.getName() + "\t" + cate2.getId());
                System.out.println();
            }
        }

    }

    @Test
    public void testCate() {
        StringBuilder sb = new StringBuilder();
        getSubCates(null, sb);
        System.out.println(sb.toString());
    }

    private void getSubCates(PtmCategory cate, StringBuilder sb) {

        long parentId = 0L;
        int level = 1;

        if (cate != null) {
            parentId = cate.getId();
            level = cate.getLevel() + 1;

            String splitStr = "";
            if (level == 2) {
                splitStr = "";
            } else if (level == 3) {
                splitStr = "\t";
            }

            System.out.println(splitStr + cate.getId() + "\t" + cate.getLevel() + "\t" + cate.getName());
            sb.append("," + cate.getId());
        }

        if (level < 3) {
            List<PtmCategory> subCates = dbm.query(Q_CATE_PARENTID_LEVEL_1, Arrays.asList(parentId, level));
            for (PtmCategory subCate : subCates) {
                getSubCates(subCate, sb);
            }
        }
    }

    @Test
    public void testShow() {
        List<PtmCategory> categories = categoryService.listSubCategories(0L);

        for (PtmCategory category : categories) {
            System.out.println(category.getId() + " - " + category.getName());
            List<PtmCategory> categories2 = categoryService.listSubCategories(category.getId());

            for (PtmCategory category2 : categories2) {
                System.out.println("\t" + category2.getId() + " - " + category2.getName());

                List<PtmCategory> categories3 = categoryService.listSubCategories(category2.getId());

                for (PtmCategory category3 : categories3) {
                    System.out.println("\t\t" + category3.getId() + " - " + category3.getName());
                }
            }
        }

    }

    @Test
    public void testCache() {
        for (int i = 0; i < 5; i++) {
            testCache_1();
        }
    }

    public void testCache_1() {
        List<PtmCategory> categories = categoryService.getRouterCategoryList(202);

        for (PtmCategory cate : categories) {
            System.out.println(cate.toString());
        }
    }

    @Test
    public void update() {

        long cateId = 23;
        String keyword = "headphones";

        categoryService.getRouterCategoryList(cateId);
    }

    @Test
    public void updateIndex() {
        categoryService.reimportCategoryIndex();
    }

    @Test
    public void search() {
//        String title = "CS2 Sony Mh750 Wired Headset Sony Mh750 Wired Headset Wired Headphones"; N
//        String title = "Enfin Homes M4XPBS Barbeque Black ";// Barbeque Y
        String title = "Adidas SPRINGBLADE DRIVE 2 M Running Shoes ";

        List<CategoryModel> categories = categoryIndexService.simpleSearch(title);

        for (CategoryModel cate : categories) {
            System.out.println(cate);
        }
    }

}
