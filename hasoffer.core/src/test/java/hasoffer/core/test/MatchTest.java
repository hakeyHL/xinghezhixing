package hasoffer.core.test;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chevy on 2016/7/13.
 */
public class MatchTest {

    @Test
    public void getUnProcessedProductId() throws Exception {

        File file = hasoffer.base.utils.FileUtils.createFile("d:\\tmp\\price_1.txt", true);

        Set<String> idSet = new HashSet<>();

        List<String> lines = FileUtils.readLines(new File("D:\\tmp\\price_0.txt"));

        for (String line : lines) {
            String id = line.split("\t")[0];
            System.out.println(id);
            idSet.add(id);
        }

        lines = FileUtils.readLines(new File("D:\\tmp\\price.txt"));
        for (String line : lines) {
            String id = line.split("\t")[0];
            if (idSet.contains(id)) {
                System.out.println(String.format("Exists [%s]", id));
            } else {
                FileUtils.write(file, line + "\n", true);
            }
        }
    }

}
