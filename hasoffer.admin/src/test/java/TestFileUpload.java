import hasoffer.base.utils.IDUtil;
import hasoffer.core.persistence.po.ptm.PtmTopSelling;
import hasoffer.core.utils.ImageUtil;
import jodd.io.FileUtil;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lihongde on 2016/6/24 14:22
 */
public class TestFileUpload {

    public static byte[] getBytesFromFile(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
        byte[] b = new byte[1000];
        int n;
        while ((n = stream.read(b)) != -1)
            out.write(b, 0, n);
        stream.close();
        out.close();
        return out.toByteArray();
    }

    @Test
    public void f() {
        List<PtmTopSelling> sscs = new ArrayList<PtmTopSelling>();

//        sscs.add(new PtmTopSelling("123456", 30, 20L));
//        sscs.add(new PtmTopSelling("123456", 31, 19L));
//        sscs.add(new PtmTopSelling("123456", 32, 23L));
//        sscs.add(new PtmTopSelling("123456", 33, 21L));
//        sscs.add(new PtmTopSelling("123456", 34, 5L));

        Collections.sort(sscs, new Comparator<PtmTopSelling>() {
            @Override
            public int compare(PtmTopSelling o1, PtmTopSelling o2) {
                if (o1.getCount() > o2.getCount()) {
                    return -1;
                } else if (o1.getCount() < o2.getCount()) {
                    return 1;
                }
                return 0;
            }
        });

        System.out.println(sscs.size());
    }

    @Test
    public void upload() {
        String fileName = "D:\\Capture001.png";
        File file = new File(fileName);
        try {
            File imageFile = FileUtil.createTempFile(IDUtil.uuid(), ".jpg", null);
            FileUtil.writeBytes(imageFile, getBytesFromFile(file));
            String path = ImageUtil.uploadImage(imageFile);
            System.out.println(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
