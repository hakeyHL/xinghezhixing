package hasoffer.task.worker;

import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.enums.SearchPrecise;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.core.persistence.po.search.updater.SrmSearchLogUpdater;
import hasoffer.core.product.IProductService;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/5/13.
 */
public class FixPtmProductWorker implements Runnable {

    private static final String Q_SRMSEARCHLOG_BYID = "SELECT t FROM SrmSearchLog t WHERE t.ptmProductId = ?0 ";
    private Logger logger = LoggerFactory.getLogger(FixPtmProductWorker.class);
    private ListProcessWorkerStatus<PtmProduct> ws;
    private IDataBaseManager dbm;
    private IProductService productService;

    public FixPtmProductWorker(ListProcessWorkerStatus<PtmProduct> ws, IDataBaseManager dbm, IProductService productService) {
        this.ws = ws;
        this.dbm = dbm;
        this.productService = productService;
    }

    @Override
    public void run() {

        while (true) {

            PtmProduct ptmProduct = ws.getSdQueue().poll();

            if (ptmProduct != null) {

                Long id = ptmProduct.getId();

                List<SrmSearchLog> logList = dbm.query(Q_SRMSEARCHLOG_BYID, Arrays.asList(id));

                //udpate SrmSearchLog by ptmptoductId
                for (SrmSearchLog log : logList) {

                    SrmSearchLogUpdater updater = new SrmSearchLogUpdater(log.getId());

                    updater.getPo().setPtmProductId(0);
                    updater.getPo().setPrecise(SearchPrecise.NOCHECK);

                    dbm.update(updater);
                    logger.debug("-----update " + log.getId() + " success");

                }

                //delete PtmProduct
                productService.deleteProduct(id);

            } else {
                logger.debug("pull get null sleep 5 seconds");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
