package hasoffer.core.analysis.impl;

import hasoffer.base.config.AppConfig;
import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.JSONUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.http.HttpUtils;
import hasoffer.core.analysis.ITagService;
import hasoffer.core.analysis.LingHelper;
import hasoffer.core.analysis.TagMapHelper;
import hasoffer.core.bo.match.AnalysisResult;
import hasoffer.core.bo.match.TagType;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.match.TagBrand;
import hasoffer.core.persistence.po.match.TagMatched;
import hasoffer.core.persistence.po.match.TagModel;
import hasoffer.nlp.core.model.HasTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2016/7/1.
 */
@Service
public class TagServiceImpl implements ITagService {
    private static String Q_TAG_BRAND = "select t from TagBrand t";
    private static String Q_TAG_MODEL = "select t from TagModel t";
    @Resource
    IDataBaseManager dbm;
    private Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);

    @Override
    public AnalysisResult analysis(String title) {
        Map<String, Object> formMap = new HashMap<String, Object>();
        formMap.put("title", title);

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Accept", "application/json, text/javascript, */*; q=0.01");

        try {
            String url = AppConfig.get(AppConfig.ANALYSIS_TITLE_URL);
            HttpResponseModel responseModel = HttpUtils.post(url, formMap, headerMap);

            AnalysisResult ar = JSONUtil.toObject(responseModel.getBodyString(), AnalysisResult.class);

            return ar;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public void saveTagMatched(TagMatched tm) {
        dbm.create(tm);
    }

    @Override
    public void loadWordDicts() {
        // todo 从缓存、序列化中查询词库是否存在，如果存在则直接加载
        // 如果不存在，则读取数据库进行创建
        buildWordDicts();

        LingHelper.createDictionaryChunker();
    }

    private void buildWordDicts() {
        // 清除词典内容
        logger.info("clear dictionary...");
        LingHelper.clearDict();

        final int START_PAGE = 1, PAGE_SIZE = 1000;

        long total_page = 0;
        int page = 1;

        // build brand dict
        logger.info("build brand dict...");
        PageableResult<TagBrand> pagedTagBrands = getPagedBrandTags(START_PAGE, PAGE_SIZE);
        total_page = pagedTagBrands.getTotalPage();
        page = 1;
        List<TagBrand> tagBrands = pagedTagBrands.getData();
        while (page <= total_page) {
            if (page > 1) {
                tagBrands = listBrandTags(page, PAGE_SIZE);
            }

            if (ArrayUtils.hasObjs(tagBrands)) {
                for (TagBrand tagBrand : tagBrands) {
                    addTag(tagBrand, TagType.BRAND);
                }
            }

            page++;
        }
        logger.info("build brand dict..OK.");

        logger.info("build model dict...");
        // build model dict
        PageableResult<TagModel> pagedTagModels = getPagedModelTags(START_PAGE, PAGE_SIZE);
        total_page = pagedTagModels.getTotalPage();
        page = 1;
        List<TagModel> tagModels = pagedTagModels.getData();
        while (page <= total_page) {
            if (page > 1) {
                tagModels = listModelTags(page, PAGE_SIZE);
            }

            if (ArrayUtils.hasObjs(tagModels)) {
                for (TagModel tagModel : tagModels) {
                    addTag(tagModel, TagType.MODEL);
                }
            }

            page++;
        }

        logger.info("build model dict..OK.");

        int dictSize = LingHelper.getDictSize();
        logger.info("dict size = " + dictSize);
    }

    private void addTag(HasTag tag, TagType type) {
        LingHelper.addToDict(tag.getTag(), type, tag.getScore());
        TagMapHelper.addToMap(type, tag.getTag(), tag);

        String alias = tag.getAlias();
        if (!StringUtils.isEmpty(alias)) {
            String[] alis = alias.split(",");
            for (String ali : alis) {
                LingHelper.addToDict(ali, type, tag.getScore());
                TagMapHelper.addToMap(type, ali, tag);
            }
        }
    }

    /*private void addBrand(TagBrand tagBrand) {
        LingHelper.addToDict(tagBrand.getTag(), TagType.BRAND, tagBrand.getScore());
        TagMapHelper.addToMap(TagType.BRAND, tagBrand.getTag(), tagBrand);

        String alias = tagBrand.getAlias();
        if (!StringUtils.isEmpty(alias)) {
            String[] alis = alias.split(",");
            for (String ali : alis) {
                LingHelper.addToDict(ali, TagType.BRAND, tagBrand.getScore());
                TagMapHelper.addToMap(TagType.BRAND, ali, tagBrand);
            }
        }
    }*/

    @Override
    public List<TagBrand> listBrandTags(int page, int size) {
        return dbm.query(Q_TAG_BRAND, page, size);
    }

    @Override
    public List<TagModel> listModelTags(int page, int size) {
        return dbm.query(Q_TAG_MODEL, page, size);
    }

    @Override
    public PageableResult<TagBrand> getPagedBrandTags(int page, int size) {
        return dbm.queryPage(Q_TAG_BRAND, page, size);
    }

    @Override
    public PageableResult<TagModel> getPagedModelTags(int page, int size) {
        return dbm.queryPage(Q_TAG_MODEL, page, size);
    }

    @Override
    @Transactional
    public TagBrand createTagBrand(String brand) {
        TagBrand tagBrand = new TagBrand(brand, "", 0);
        dbm.create(tagBrand);
        return tagBrand;
    }

    @Override
    @Transactional
    public TagModel createTagModel(String model) {
        TagModel tagModel = new TagModel(model, "", 0);
        dbm.create(tagModel);
        return tagModel;
    }
}
