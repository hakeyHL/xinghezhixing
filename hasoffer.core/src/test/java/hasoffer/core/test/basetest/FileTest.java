package hasoffer.core.test.basetest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import hasoffer.base.config.AppConfig;
import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.utils.JSONUtil;
import hasoffer.base.utils.http.HttpUtils;
import hasoffer.core.bo.match.AnalysisResult;
import jodd.io.FileUtil;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2016/6/30.
 */
public class FileTest {

    @Test
    public void show() {
        String path = "D:\\tmp\\po";
        File file = new File(path);
        File[] dirs = file.listFiles();
        for (File d : dirs) {
            System.out.println("---" + d.getName() + "---");
            File[] dirss = d.listFiles();
            for (File d2 : dirss) {
                System.out.println(d2.getName());
            }
        }
    }

    @Test
    public void ts3() throws Exception {
        Pattern pattern = Pattern.compile("^[0|1]\\s.+");
//        BufferedReader fr = new BufferedReader(new FileReader(new File("d:/datas/hasoffer/all_titles")));
        BufferedReader fr = new BufferedReader(new FileReader(new File("d:/datas/hasoffer/title_if_std")));

        String line;
        while ((line = fr.readLine()) != null) {
//            String line = fr.readLine();
            Matcher m = pattern.matcher(line);
            if (!m.matches()) {
                System.out.println(line);
            }
        }

        fr.close();
    }

    @Test
    public void ts2() {
        Map<String, Object> formMap = new HashMap<String, Object>();
        formMap.put("title", "MapmyIndia ICENAV 301 IN-Dash AVN-Universal GPS Navigation Device");

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Accept", "application/json, text/javascript, */*; q=0.01");

        try {
            String url = AppConfig.get(AppConfig.ANALYSIS_TITLE_URL);
            HttpResponseModel responseModel = HttpUtils.post(url, formMap, headerMap);

            AnalysisResult ar = JSONUtil.toObject(responseModel.getBodyString(), AnalysisResult.class);

            System.out.println(ar.getTitle());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    @Test
    public void ts() throws Exception {
        String json = "{\"title\":\"MapmyIndia ICENAV 301 IN-Dash AVN-Universal GPS Navigation Device\",\"tagMap\":{\"MODEL\":[{\"tag\":\"301\",\"hasTag\":{\"id\":27522,\"tag\":\"301\",\"alias\":\"\",\"score\":0}},{\"tag\":\"-\",\"hasTag\":{\"id\":25,\"tag\":\"-\",\"alias\":\"\",\"score\":0}},{\"tag\":\"-\",\"hasTag\":{\"id\":25,\"tag\":\"-\",\"alias\":\"\",\"score\":0}}],\"BRAND\":[{\"tag\":\"IN\",\"hasTag\":{\"id\":4477,\"tag\":\"in\",\"alias\":\"\",\"score\":0}},{\"tag\":\"Dash\",\"hasTag\":{\"id\":447,\"tag\":\"dash\",\"alias\":\"\",\"score\":4}},{\"tag\":\"Universal\",\"hasTag\":{\"id\":6253,\"tag\":\"universal\",\"alias\":\"\",\"score\":0}},{\"tag\":\"GPS\",\"hasTag\":{\"id\":5051,\"tag\":\"gps\",\"alias\":\"\",\"score\":0}}]}}";

        JSONObject jsonObj = JSON.parseObject(json);

        AnalysisResult ar = JSONUtil.toObject(json, AnalysisResult.class);
        System.out.println(ar.getTitle());
    }

    @Test
    public void test() throws IOException {

        String[] strings = FileUtil.readLines(new File("C:/Users/wing/Desktop/wing.txt"));

        for (String str : strings) {
            System.out.println(str);
        }

    }

    @Test
    public void test1() throws IOException {

        String[] lines = FileUtil.readLines(new File("C:/Users/wing/Desktop/haha.log"), "UTF-8");

        StringBuilder stringBuilder = new StringBuilder();

        String destlevel = "";
        String destId = "";

        for (int i = 0; i < lines.length; i++) {

            String line = lines[i];

            String[] subStr = line.split(":");

            if (i % 3 == 0) {
                destId = subStr[1];
            } else if (i % 3 == 2) {
                destlevel = subStr[1] + "0";
                stringBuilder.append("update ptmcategory set level = " + destlevel + " where id = " + destId + " ;\n");
                FileUtil.appendString(new File("C:/Users/wing/Desktop/heihei.sql"), stringBuilder.toString());
                stringBuilder = new StringBuilder();
            }

        }
    }
}
