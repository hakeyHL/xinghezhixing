package hasoffer.task.worker;

import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/3/21
 * Function :
 */
public class CmpSkuListWorker implements Runnable {

    private Logger logger = LoggerFactory.getLogger(CmpSkuListWorker.class);
    private IDataBaseManager dbm;
    private ConcurrentLinkedQueue<PtmCmpSku> skuQueue;
    private String queryString;

    public CmpSkuListWorker(IDataBaseManager dbm, ConcurrentLinkedQueue<PtmCmpSku> skuQueue, String queryString) {
        this.dbm = dbm;
        this.skuQueue = skuQueue;
        this.queryString = queryString;
    }

    public void run() {

        long startLongDate = TimeUtils.today();//用来记录当天的0点

        while (true) {

            try {

                if (TimeUtils.now() > startLongDate) {
                    //重新拼接queryString
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    queryString = queryString + " AND t.updateTime < '" + sdf.format(TimeUtils.toDate(TimeUtils.today())) + "' ORDER BY t.id ";

                    r();//执行当天的更新
                    startLongDate = startLongDate + TimeUtils.MILLISECONDS_OF_1_DAY;
                } else {
                    logger.debug("skuUpdate list worker finish sleep 10min");
                    TimeUnit.MINUTES.sleep(10);//当天任务已完成，不再执行
                }

            } catch (InterruptedException e) {
                continue;
            } catch (Exception e) {
                continue;
            }

        }
    }

    public void r() {
        int pageNum = 1, PAGE_SIZE = 500;

        PageableResult<PtmCmpSku> pageableResult = dbm.queryPage(queryString, pageNum, PAGE_SIZE);

        int pageCount = (int) pageableResult.getTotalPage();

        List<PtmCmpSku> cmpSkus = pageableResult.getData();

        while (pageNum <= pageCount) {

            if (skuQueue.size() > 600) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                    logger.debug("task list queue size > 6000 , sleep 3 seconds");
                    continue;
                } catch (InterruptedException e) {
                    break;
                }
            }

            logger.info(String.format("update sku : %d/%d .", pageNum, pageCount));

            if (pageNum > 1) {
                cmpSkus = dbm.query(queryString, pageNum, PAGE_SIZE);
            }

            if (ArrayUtils.hasObjs(cmpSkus)) {
                for (PtmCmpSku cmpSku : cmpSkus) {
                    if (cmpSku.getUpdateTime().getTime() < TimeUtils.today()) {
                        skuQueue.add(cmpSku);
                    }
                }
            }

            pageNum++;
        }
    }
}
