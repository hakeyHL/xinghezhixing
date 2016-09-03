package hasoffer.admin.worker;

import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.ptm.updater.PtmCmpSkuUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created on 2016/5/18.
 */
public class FixSkuErrorInPriceWorker implements Runnable {

    private static final String Q_PTMCMPSKU_BYPRODUCTID = "SELECT t FROM PtmCmpSku t WHERE t.productId = ?0 ";
    private static final String Q_PRODUCT_BYID = "SELECT t FROM PtmProduct t WHERE t.id = ?0 ";

    private static Logger logger = LoggerFactory.getLogger(FixSkuErrorInPriceWorker.class);

    private ConcurrentLinkedQueue<Long> idQueue;
    private IDataBaseManager dbm;

    public FixSkuErrorInPriceWorker(ConcurrentLinkedQueue<Long> idQueue, IDataBaseManager dbm) {
        this.idQueue = idQueue;
        this.dbm = dbm;
    }

    @Override
    public void run() {

        while (true) {

            Long id = idQueue.poll();

            if (id != null) {

                List<PtmCmpSku> skus = dbm.query(Q_PTMCMPSKU_BYPRODUCTID, Arrays.asList(id));

                PtmProduct product = dbm.querySingle(Q_PRODUCT_BYID, Arrays.asList(id));

                if (skus == null) {
                    continue;
                }

                if (product == null) {
                    continue;
                }

                if (skus.size() < 0) {
                    logger.debug(id + " get sku size small than 0");
                    continue;
                }

                float basePrice = -5.5f;

                for (int i = skus.size() - 1; i >= 0; i--) {

                    if (StringUtils.isEmpty(skus.get(i).getTitle())) {
                        continue;
                    }

                    if (StringUtils.isEqual(skus.get(i).getTitle(), product.getTitle())) {
                        if (skus.get(i).getPrice() != 0.0) {
                            basePrice = skus.get(i).getPrice();
                        }
                    }
                }

                if (basePrice == -5.5f) {
                    logger.debug("no title like this in skus for " + id);
                } else if (skus.size() <= 1) {
                    logger.debug("only one or zero left in skus fro " + id);
                } else {
                    for (PtmCmpSku sku : skus) {

                        if (sku.getPrice() > basePrice * 1.6 || sku.getPrice() < basePrice * 0.4) {

                            if (sku.getPrice() != 0) {

                                PtmCmpSkuUpdater updater = new PtmCmpSkuUpdater(sku.getId());

                                updater.getPo().setProductId(999999999L);

                                dbm.update(updater);

                                logger.debug(sku.getId() + " remove success price = [" + sku.getPrice() + "] from " + id);
                                logger.debug("basePrice:" + basePrice);

                            }

                        }
                    }
                }
            }
        }
    }
}
