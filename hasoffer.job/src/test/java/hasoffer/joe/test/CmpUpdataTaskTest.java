package hasoffer.joe.test;

import hasoffer.base.model.PageableResult;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.dubbo.api.fetch.service.IFetchDubboService;
import hasoffer.job.worker.CmpSkuUpdateWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created on 2016/3/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:quartz-applicationContext.xml","classpath:dubbo-spring.xml","classpath:spring-beans.xml"} )
public class CmpUpdataTaskTest {

    private static final String Q_PTM_CMPSKUTEST = "SELECT t FROM PtmCmpSku t WHERE website = 'FLIPKART' ";
    private static Logger logger = LoggerFactory.getLogger(CmpUpdataTaskTest.class);
    @Resource
    IDataBaseManager dbm;
    @Resource
    ICmpSkuService cmpSkuService;
    @Resource
    IFetchDubboService flipkartFetchService;

    @Test
    public void testCmpUpdateTask() {

        logger.debug("------------------------------------CmpUpdateTask-START------------------------------------");
        final ConcurrentLinkedQueue<PtmCmpSku> skuQueue = new ConcurrentLinkedQueue<PtmCmpSku>();

        final AtomicBoolean listTaskFinished = new AtomicBoolean(false);

        Runnable listTask = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    r();
                }//                listTaskFinished.set(true);
            }

            private void r() {
                int pageNum = 1, PAGE_SIZE = 500;
                PageableResult<PtmCmpSku> pageableResult = dbm.queryPage(Q_PTM_CMPSKUTEST, pageNum, PAGE_SIZE);

                int pageCount = (int) pageableResult.getTotalPage();

                List<PtmCmpSku> cmpSkus = pageableResult.getData();
                while (pageNum <= pageCount) {

                    if (skuQueue.size() > 600) {
                        try {
                            TimeUnit.SECONDS.sleep(3);
                            continue;
                        } catch (InterruptedException e) {
                            break;
                        }
                    }

                    logger.info(String.format("update sku : %d/%d .", pageNum, pageCount));

                    if (pageNum > 1) {
                        cmpSkus = dbm.query(Q_PTM_CMPSKUTEST, pageNum, PAGE_SIZE);
                    }

                    skuQueue.addAll(cmpSkus);
                    pageNum++;
                }
            }
        };

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(listTask);

        for (int i = 0; i < 10; i++) {
            es.execute(new CmpSkuUpdateWorker(skuQueue, cmpSkuService, flipkartFetchService));
        }

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(5);
                if (listTaskFinished.get() && skuQueue.size() == 0) {
                    break;
                }
            } catch (InterruptedException e) {
                break;
            }
        }

        es.shutdown();

        logger.debug("------------------------------------CmpUpdateTask-END------------------------------------");
    }

}
