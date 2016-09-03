package hasoffer.core.test.basetest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.analysis.ITagService;
import hasoffer.core.analysis.LingHelper;
import hasoffer.core.bo.match.SkuValType;
import hasoffer.core.bo.match.TagType;
import hasoffer.core.bo.match.TitleStruct;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.mongo.PtmCmpSkuDescription;
import hasoffer.core.persistence.po.match.TagBrand;
import hasoffer.core.persistence.po.match.TagCategory;
import hasoffer.core.persistence.po.match.TagModel;
import hasoffer.core.persistence.po.match.TagSkuVal;
import hasoffer.core.task.ListProcessTask;
import hasoffer.core.task.worker.ILister;
import hasoffer.core.task.worker.IProcessor;
import jodd.io.FileUtil;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Date : 2016/6/15
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class TagTest {

    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;
    @Resource
    ITagService tagService;
    ConcurrentHashMap<String, TagCategory> cateTagMap = new ConcurrentHashMap<String, TagCategory>();
    ConcurrentHashMap<String, TagSkuVal> skuValTagMap = new ConcurrentHashMap<String, TagSkuVal>();
    ConcurrentHashMap<String, TagBrand> brandTagMap = new ConcurrentHashMap<String, TagBrand>();
    ConcurrentHashMap<String, TagModel> modelTagMap = new ConcurrentHashMap<String, TagModel>();
    ConcurrentHashSet<String> newBrandSet = new ConcurrentHashSet<String>();
    ConcurrentHashSet<String> newModelSet = new ConcurrentHashSet<String>();
    private Logger logger = LoggerFactory.getLogger(TagTest.class);


    @Test
    public void matchTest() {
        tagService.loadWordDicts();

        String title = "BQ S 40 Dual Sim 1 GB (Grey)";

        logger.info("start to analysis...");
        Map<String, List<String>> tagMap = LingHelper.analysis(title);

        List<String> brands = tagMap.get(TagType.BRAND.name());
        String brandStr = StringUtils.arrayToString(brands);

        List<String> models = tagMap.get(TagType.MODEL.name());
        String modelStr = StringUtils.arrayToString(models);

        logger.debug(brandStr);
        logger.debug(modelStr);

        logger.info("ok.");
    }

    @Test
    public void getTags() {
        fillMaps();

        ListProcessTask<PtmCmpSkuDescription> listAndProcessTask2 = new ListProcessTask<PtmCmpSkuDescription>(
                new ILister<PtmCmpSkuDescription>() {
                    @Override
                    public PageableResult<PtmCmpSkuDescription> getData(int page) {
                        return mdm.queryPage(PtmCmpSkuDescription.class, new Query(), page, 100);
                    }

                    @Override
                    public boolean isRunForever() {
                        return false;
                    }

                    @Override
                    public void setRunForever(boolean runForever) {

                    }
                },
                new IProcessor<PtmCmpSkuDescription>() {
                    @Override
                    public void process(PtmCmpSkuDescription o) {
//                        getBrandSet(o);
                        getModelSet(o);
                    }
                }
        );

        listAndProcessTask2.go();

//        saveBrands();
        saveModels();

        System.out.println("saved brands ok!");
    }

    private void saveModels() {
        System.out.println(String.format("find %d models", newModelSet.size()));

        for (String model : newModelSet) {
            if (modelTagMap.containsKey(model)) {
                System.out.println(model + "\t exists...");
            } else {
                try {
                    TagModel tagModel = tagService.createTagModel(model);
                    modelTagMap.put(model, tagModel);
                } catch (Exception e) {
                    System.out.println(e.getMessage() + "_error_!!!");
                }
            }
        }
    }

    private void getModelSet(PtmCmpSkuDescription o) {
        JSONObject json = JSON.parseObject(o.getJsonParam());
        for (Map.Entry<String, Object> kv : json.entrySet()) {
            String name = kv.getKey().trim();
            String value = kv.getValue().toString().trim();

            if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) {
                continue;
            }

            name = name.toLowerCase();
            value = value.toLowerCase();

            if (name.contains("model")) {
                newModelSet.add(value);
                return;
            }
        }
    }

    private void saveBrands() {

        System.out.println(String.format("find %d brands", newBrandSet.size()));

        for (String brand : newBrandSet) {
            if (brandTagMap.containsKey(brand)) {
                System.out.println(brand + "\t exists...");
            } else {
                try {
                    TagBrand tagBrand = tagService.createTagBrand(brand);
                    brandTagMap.put(brand, tagBrand);
                } catch (Exception e) {
                    System.out.println(e.getMessage() + "_error_!!!");
                }
            }
        }

    }

    private void getBrandSet(PtmCmpSkuDescription cmpSkuDescription) {
        JSONObject json = JSON.parseObject(cmpSkuDescription.getJsonParam());
        for (Map.Entry<String, Object> kv : json.entrySet()) {
            String name = kv.getKey().trim();
            String value = kv.getValue().toString().trim();

            if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) {
                continue;
            }

            name = name.toLowerCase();
            value = value.toLowerCase();

            if (name.contains("brand")) {
                newBrandSet.add(value);
                return;
            }
        }
    }

    private void getAttrs(PtmCmpSkuDescription cmpSkuDescription) {
        JSONObject json = JSON.parseObject(cmpSkuDescription.getJsonParam());
        for (Map.Entry<String, Object> kv : json.entrySet()) {
            String name = kv.getKey().trim();
            String value = kv.getValue().toString().trim();

            if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) {
                continue;
            }

            name = name.toLowerCase();
            value = value.toLowerCase();

            if (name.contains("brand") || name.contains("model")) {
                if (brandTagMap.containsKey(value)) {
                    System.out.println(String.format("brand[%s] exists...............................", value));
                } else {
                    TagBrand tagBrand = tagService.createTagBrand(value);
                    brandTagMap.put(value, tagBrand);
                }
            }
        }
    }

    @Test
    public void fillMaps() {
        List<TagCategory> tagCategories = dbm.query("select t from TagCategory t");
        List<TagSkuVal> tagSkuVals = dbm.query("select t from TagSkuVal t");
        List<TagBrand> tagBrands = dbm.query("select t from TagBrand t");
        List<TagBrand> tagModels = dbm.query("select t from TagModel t");

        fill(cateTagMap, tagCategories);
        fill(skuValTagMap, tagSkuVals);
        fill(brandTagMap, tagBrands);
        fill(modelTagMap, tagModels);
    }

    private void getStructInfo(TitleStruct ts,
                               Map<String, TagCategory> cateTagMap,
                               Map<String, TagSkuVal> skuValTagMap,
                               Map<String, TagBrand> brandTagMap) {

        String title = ts.getTitle().toLowerCase();

        for (Map.Entry<String, TagCategory> kv : cateTagMap.entrySet()) {
            if (title.contains(kv.getKey())) {
                ts.getCateTag().add(kv.getKey());
            }
        }

        for (Map.Entry<String, TagSkuVal> kv : skuValTagMap.entrySet()) {
            if (title.contains(kv.getKey())) {
                if (kv.getValue().getSkuValType() == SkuValType.COLOR) {
                    ts.getColorTag().add(kv.getKey());
                } else {
                    ts.getSizeTag().add(kv.getKey());
                }
            }
        }

        for (Map.Entry<String, TagBrand> kv : brandTagMap.entrySet()) {
            if (title.contains(kv.getKey())) {
                ts.getBrandTag().add(kv.getKey());
            }
        }
    }

    private void fill(Map tagMap, List tags) {

        for (Object o : tags) {

            HasTag iTag = (HasTag) o;

            tagMap.put(iTag.getTag(), o);

            if (!StringUtils.isEmpty(iTag.getAlias())) {
                String[] alias = iTag.getAlias().split(",");
                for (String ali : alias) {
                    tagMap.put(ali, o);
                }
            }
        }

    }

    @Test
    public void importSkuValTags() throws Exception {
        String filePath = "d:/TMP/tags/skuattrtag.txt";

        String[] lines = FileUtil.readLines(new File(filePath));

        List<TagSkuVal> tags = new ArrayList<TagSkuVal>();

        for (String line : lines) {
            HasTag tag = cleanBySplit(line);

            tags.add(new TagSkuVal(tag.getTag(), tag.getAlias(), 4));
        }

        dbm.batchSave(tags);
    }

    @Test
    public void importBrandTags() throws Exception {
        String filePath = "d:/TMP/tags/brand.txt";

        String[] lines = FileUtil.readLines(new File(filePath));

        List<TagBrand> tags = new ArrayList<TagBrand>();

        for (String line : lines) {
            HasTag tag = cleanBySplit(line);

            tags.add(new TagBrand(tag.getTag(), tag.getAlias(), 4));
        }

        dbm.batchSave(tags);
    }

    @Test
    public void importCategoryTags() throws Exception {
        String filePath = "d:/TMP/tags/catetag.txt";

        String[] lines = FileUtil.readLines(new File(filePath));

        List<TagCategory> tags = new ArrayList<TagCategory>();

        for (String line : lines) {
            HasTag tag = cleanBySplit(line);

            tags.add(new TagCategory(tag.getTag(), tag.getAlias(), 10));
        }

        dbm.batchSave(tags);
    }

    private HasTag cleanBySplit(String line) {

        String[] tags = line.split(",");

        String keyword = tags[0].trim().toLowerCase();

        HasTag ht = new HasTag();
        ht.setTag(keyword);

        int len = tags.length;
        if (len > 1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 1; i < len; i++) {
                String tag = tags[i].trim().toLowerCase();
                if (!StringUtils.isEmpty(tag)) {
                    sb.append(tag).append(",");
                }
            }

            if (sb.length() > 0) {
                if (sb.charAt(sb.length() - 1) == ',') {
                    sb.delete(sb.lastIndexOf(","), sb.length());
                }

                ht.setAlias(sb.toString());
            }
        }

        return ht;
    }

}
