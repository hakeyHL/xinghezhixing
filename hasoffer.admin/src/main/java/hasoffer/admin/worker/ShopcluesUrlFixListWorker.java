package hasoffer.admin.worker;

import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/4/20.
 */
public class ShopcluesUrlFixListWorker implements Runnable {

    private Logger logger = LoggerFactory.getLogger(ShopcluesUrlFixListWorker.class);

    private IDataBaseManager dbm;
    private ConcurrentLinkedQueue<PtmCmpSku> skuQueue;
    private String queryString;

    public ShopcluesUrlFixListWorker(IDataBaseManager dbm, ConcurrentLinkedQueue<PtmCmpSku> skuQueue, String queryString) {
        this.dbm = dbm;
        this.skuQueue = skuQueue;
        this.queryString = queryString;
    }

    @Override
    public void run() {

//        while (true) {
//            try {
        r();
//                TimeUnit.MINUTES.sleep(10);
//            } catch (InterruptedException e) {
//                continue;
//            } catch (Exception e) {
//                continue;
//            }
//        }

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
                    continue;
                } catch (InterruptedException e) {
                    break;
                }
            }

            logger.info(String.format("update : %d/%d .", pageNum, pageCount));

            if (pageNum > 1) {
                cmpSkus = dbm.query(queryString, pageNum, PAGE_SIZE);
            }

            if (ArrayUtils.hasObjs(cmpSkus)) {
                for (PtmCmpSku cmpSku : cmpSkus) {
                    skuQueue.add(cmpSku);
                }
            }

            pageNum++;
        }
    }

}
