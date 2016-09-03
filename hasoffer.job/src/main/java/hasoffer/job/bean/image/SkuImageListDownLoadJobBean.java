package hasoffer.job.bean.image;

import hasoffer.base.model.PageableResult;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSkuImage;
import hasoffer.core.product.IPtmCmpSkuImageService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/8/9.
 */
public class SkuImageListDownLoadJobBean extends QuartzJobBean {

    @Resource
    IDataBaseManager dbm;
    @Resource
    IPtmCmpSkuImageService ptmCmpSkuImageService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        final ConcurrentLinkedQueue<PtmCmpSkuImage> ptmCmpSkuImageQueue = new ConcurrentLinkedQueue<PtmCmpSkuImage>();

        ExecutorService es = Executors.newCachedThreadPool();

        es.execute(new Runnable() {

            @Override
            public void run() {

                int page = 1;
                int pageSize = 1000;

                PageableResult<PtmCmpSkuImage> pageableResult = dbm.queryPage("SELECT t FROM PtmCmpSkuImage t WHERE t.fetched = 0", page, pageSize);

                long totalPage = pageableResult.getTotalPage();

                while (page < totalPage) {

                    if (ptmCmpSkuImageQueue.size() > 10000) {
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {

                        }
                        continue;
                    }

                    if (page > 1) {
                        pageableResult = dbm.queryPage("SELECT t FROM PtmCmpSkuImage t WHERE t.fetched = 0", page, pageSize);
                    }

                    List<PtmCmpSkuImage> ptmCmpSkuImageList = pageableResult.getData();

                    ptmCmpSkuImageQueue.addAll(ptmCmpSkuImageList);

                    page++;

                }
            }
        });

        for (int i = 0; i < 10; i++) {
            es.execute(new Runnable() {

                @Override
                public void run() {
                    while (true) {

                        PtmCmpSkuImage ptmCmpSkuImage = ptmCmpSkuImageQueue.poll();

                        if (ptmCmpSkuImage == null) {
                            try {
                                TimeUnit.SECONDS.sleep(5);
                            } catch (InterruptedException e) {

                            }
                            continue;
                        }

                        boolean fetchResultFlag = ptmCmpSkuImageService.downloadPtmCmpSkuImage(ptmCmpSkuImage.getId());

                        if (fetchResultFlag) {
                            ptmCmpSkuImageService.updateFetchStatus(ptmCmpSkuImage.getId(), fetchResultFlag);
                        }
                    }
                }
            });
        }

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                break;
            }
        }
    }
}
