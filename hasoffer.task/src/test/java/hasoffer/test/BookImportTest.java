package hasoffer.test;

import hasoffer.core.bo.system.SearchLogBo;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.search.ISearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class BookImportTest {
    @Resource
    IDataBaseManager dbm;

    @Resource
    ISearchService searchService;

    @Test
    public void insertIntoDB(){
        File file = new File("D:/log/webmagic/book-title.2016-07-11.log");
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
                line++;
                System.out.println("insert i=" + line);
                SearchLogBo searchLogBo = new SearchLogBo(null, tempString, null, "EBAY",0,0, 0, 0);
                searchService.saveSearchLog(searchLogBo);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
