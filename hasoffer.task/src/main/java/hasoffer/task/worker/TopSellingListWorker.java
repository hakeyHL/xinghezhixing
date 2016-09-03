package hasoffer.task.worker;

import hasoffer.base.enums.TaskLevel;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmTopSelling;
import hasoffer.dubbo.api.fetch.service.IFetchDubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created on 2016/8/3.
 */
public class TopSellingListWorker implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(SrmSearchLogListWorker.class);

    private IDataBaseManager dbm;
    private ConcurrentLinkedQueue<PtmCmpSku> queue;
    private IFetchDubboService fetchDubboService;

    public TopSellingListWorker(IDataBaseManager dbm, ConcurrentLinkedQueue<PtmCmpSku> queue, IFetchDubboService fetchDubboService) {
        this.dbm = dbm;
        this.queue = queue;
        this.fetchDubboService = fetchDubboService;
    }

    @Override
    public void run() {

        List<PtmTopSelling> topSellingList = dbm.query("SELECT t FROM PtmTopSelling t WHERE t.status = 'ONLINE' ORDER BY t.count DESC,t.lUpdateTime DESC");

        for (PtmTopSelling topSelling : topSellingList) {

            long productid = topSelling.getId();

            List<PtmCmpSku> skuList = dbm.query("SELECT t FROM PtmCmpSku t WHERE t.productId = ?0 ", Arrays.asList(productid));

            for (PtmCmpSku sku : skuList) {

                if (sku.getWebsite() == null) {
                    continue;
                }

                queue.add(sku);
                fetchDubboService.sendUrlTask(sku.getWebsite(), sku.getUrl(), TaskLevel.LEVEL_1);
                logger.info("send url request success for [" + sku.getId() + "]");
            }
        }
    }
}
