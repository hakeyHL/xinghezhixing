package hasoffer.task.controller;

import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.mongo.SrmAutoSearchResult;
import hasoffer.core.search.ISearchService;
import hasoffer.core.task.ListProcessTask;
import hasoffer.core.task.worker.ILister;
import hasoffer.core.task.worker.IProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by chevy on 2016/7/7.
 */
@Controller
@RequestMapping(value = "/automatch")
public class AutoMatchController {
    @Resource
    ISearchService searchService;
    @Resource
    IMongoDbManager mdm;

    private Logger logger = LoggerFactory.getLogger(AutoSearchMatchController.class);

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    @ResponseBody
    public String autosearch() {
        logger.debug("automatch");

        final long stime = TimeUtils.now() - TimeUtils.MILLISECONDS_OF_1_HOUR;

        ListProcessTask<SrmAutoSearchResult> listAndProcessTask2 = new ListProcessTask<SrmAutoSearchResult>(
                new ILister() {

                    long startTime = stime;
                    boolean runForever = true;

                    @Override
                    public PageableResult getData(int page) {
                        Query query = new Query(
                                Criteria.where("lUpdateTime").gt(startTime)
//                                        .andOperator(Criteria.where("relatedProId").is(0)
//                                                .andOperator(Criteria.where("lRelateTime").is(0)))
                        );

                        query.with(new Sort(Sort.Direction.ASC, "lUpdateTime"));

                        PageableResult<SrmAutoSearchResult> pageableResult = mdm.queryPage(SrmAutoSearchResult.class, query, 1, 500);
                        List<SrmAutoSearchResult> datas = pageableResult.getData();
                        if (ArrayUtils.hasObjs(datas)) {
                            startTime = datas.get(datas.size() - 1).getlUpdateTime();
                            logger.info(startTime + "\t-\t" + TimeUtils.parse(startTime, "yyyy-MM-dd HH:mm:ss"));
                        }

                        return pageableResult;
                    }

                    @Override
                    public boolean isRunForever() {
                        return runForever;
                    }

                    @Override
                    public void setRunForever(boolean runForever) {
                        this.runForever = runForever;
                    }

                },
                new IProcessor<SrmAutoSearchResult>() {
                    @Override
                    public void process(SrmAutoSearchResult asr) {
                        try {
                            searchService.analysisAndRelate(asr);
                        } catch (Exception e) {
                            logger.debug("[" + asr.getId() + "]" + e.getMessage());
                        }
                    }
                }
        );

        listAndProcessTask2.go();

        return "ok";
    }
}
