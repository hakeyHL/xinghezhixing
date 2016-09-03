package hasoffer.analysis.api.controller;

import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.analysis.ITagService;
import hasoffer.core.analysis.LingHelper;
import hasoffer.core.bo.match.TagMatchResult;
import hasoffer.core.bo.match.TagType;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.match.TagMatched;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.task.ListProcessTask;
import hasoffer.core.task.worker.ILister;
import hasoffer.core.task.worker.IProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by chevy on 2016/7/2.
 */
@Controller
@RequestMapping(value = "/analysis")
public class TitleAnalysisController {

    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;
    @Resource
    ITagService tagService;

    private Logger logger = LoggerFactory.getLogger(TitleAnalysisController.class);

    @RequestMapping(value = "/t", method = RequestMethod.POST)
    public ModelAndView analysisTitle(HttpServletRequest request) {

        String title = request.getParameter("title");

//        Map<String, List<String>> tagMap = LingHelper.analysis(title);

        Map<String, List<TagMatchResult>> tagMap = LingHelper.analysis2(title);

        ModelAndView mav = new ModelAndView();
        mav.addObject("tagMap", tagMap);
        mav.addObject("title", title);

        return mav;
    }

    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public
    @ResponseBody
    String matchSkuTitles() {

        ListProcessTask<PtmProduct> listAndProcessTask2 = new ListProcessTask<PtmProduct>(new ILister() {
            @Override
            public PageableResult getData(int page) {
//                return dbm.queryPage("select t from PtmCmpSku t where t.title is not null", page, 2000);
                return dbm.queryPage("select t from PtmProduct t where t.title is not null", page, 2000);
            }

            @Override
            public boolean isRunForever() {
                return false;
            }

            @Override
            public void setRunForever(boolean runForever) {

            }
        }, new IProcessor<PtmProduct>() {
            @Override
            public void process(PtmProduct o) {
                String title = o.getTitle();
                if (StringUtils.isEmpty(title)) {
                    return;
                }

                Map<String, List<String>> tagMap = LingHelper.analysis(title);

                List<String> brands = tagMap.get(TagType.BRAND.name());
                String brandStr = StringUtils.arrayToString(brands);

                List<String> models = tagMap.get(TagType.MODEL.name());
                String modelStr = StringUtils.arrayToString(models);

                TagMatched tm = new TagMatched(o.getId(), title, brandStr, modelStr);
                tagService.saveTagMatched(tm);
            }
        });

        listAndProcessTask2.go();

        return "ok";
    }

    /**
     * curl http://localhost:8888/analysis/test2
     *
     * @return
     */
    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public
    @ResponseBody
    String matchSkuTitles2() {

        ListProcessTask<PtmCmpSku> listAndProcessTask2 = new ListProcessTask<PtmCmpSku>(new ILister() {
            @Override
            public PageableResult getData(int page) {
                return dbm.queryPage("select t from PtmCmpSku t where t.title is not null", page, 2000);
//                return dbm.queryPage("select t from PtmProduct t where t.title is not null", page, 2000);
            }

            @Override
            public boolean isRunForever() {
                return false;
            }

            @Override
            public void setRunForever(boolean runForever) {

            }
        }, new IProcessor<PtmCmpSku>() {
            @Override
            public void process(PtmCmpSku o) {
                String title = o.getTitle();
                if (StringUtils.isEmpty(title)) {
                    return;
                }

                Map<String, List<String>> tagMap = LingHelper.analysis(title);

                List<String> brands = tagMap.get(TagType.BRAND.name());
                String brandStr = StringUtils.arrayToString(brands);

                List<String> models = tagMap.get(TagType.MODEL.name());
                String modelStr = StringUtils.arrayToString(models);

                TagMatched tm = new TagMatched(o.getId(), title, brandStr, modelStr);
                try {
                    tagService.saveTagMatched(tm);
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                    return;
                }
            }
        });

        listAndProcessTask2.go();

        return "ok";
    }
}
