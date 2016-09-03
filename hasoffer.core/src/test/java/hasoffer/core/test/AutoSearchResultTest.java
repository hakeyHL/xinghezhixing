package hasoffer.core.test;

import hasoffer.base.model.PageableResult;
import hasoffer.core.cache.SearchLogCacheManager;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.mongo.SrmAutoSearchResult;
import hasoffer.core.search.ISearchService;
import hasoffer.core.task.ListProcessTask;
import hasoffer.core.task.worker.ILister;
import hasoffer.core.task.worker.IProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Date : 2016/5/25
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class AutoSearchResultTest {

    @Resource
    IMongoDbManager mdm;
    @Resource
    ISearchService searchService;
    @Resource
    SearchLogCacheManager cacheManager;
    private Logger logger = LoggerFactory.getLogger(AutoSearchResultTest.class);

    @Test
    public void ts5() {
        Map<Long, Long> map = cacheManager.getProductCount("20160708");

        for (Map.Entry<Long, Long> kv : map.entrySet()) {
            System.out.println(kv.getKey() + "\t " + kv.getValue());
        }
    }

    @Test
    public void ts4() {
        long[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (long id : ids) {
            cacheManager.countSearchedProduct(id);
        }
    }

    @Test
    public void ts3() {
        SrmAutoSearchResult asr = mdm.queryOne(SrmAutoSearchResult.class, "47f4cc9c4501b4e0c71890360764fd65");
        try {
            searchService.analysisAndRelate(asr);
        } catch (Exception e) {
            logger.debug("[" + asr.getId() + "]" + e.getMessage());
        }
    }

    @Test
    public void f() {
        final int pageSize = 100;

        final ConcurrentHashMap<Integer, AtomicInteger> countMap = new ConcurrentHashMap<Integer, AtomicInteger>();
        for (int i = 0; i <= 6; i++) {
            countMap.put(i, new AtomicInteger(0));
        }

        ListProcessTask<SrmAutoSearchResult> listAndProcessTask2 = new ListProcessTask<SrmAutoSearchResult>(new ILister() {
            @Override
            public PageableResult getData(int page) {
                Query query = Query.query(Criteria.where("relatedProId").gt(0));

                PageableResult<SrmAutoSearchResult> pagedSearchResults = mdm.queryPage(SrmAutoSearchResult.class, query, page, pageSize);

                return pagedSearchResults;
            }

            @Override
            public boolean isRunForever() {
                return false;
            }

            @Override
            public void setRunForever(boolean runForever) {

            }
        }, new IProcessor<SrmAutoSearchResult>() {
            @Override
            public void process(SrmAutoSearchResult sr) {
                if (sr.getFinalSkus().size() == 5) {
                    logger.debug(sr.getId() + "\t" + sr.getTitle());
                }

                countMap.get(sr.getFinalSkus().size()).addAndGet(1);
            }
        });

        listAndProcessTask2.go();

        for (int i = 0; i <= 6; i++) {
            logger.debug(String.format("%d = %d", i, countMap.get(i).get()));
        }
    }

}
