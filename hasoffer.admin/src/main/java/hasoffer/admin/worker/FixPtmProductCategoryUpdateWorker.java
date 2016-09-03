package hasoffer.admin.worker;

import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.ptm.updater.PtmProductUpdater;
import hasoffer.core.product.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created on 2016/6/30.
 */
public class FixPtmProductCategoryUpdateWorker implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(FixPtmProductCategoryUpdateWorker.class);

    private ConcurrentLinkedQueue<PtmCmpSku> quene;
    private IDataBaseManager dbm;
    private IProductService productService;

    public FixPtmProductCategoryUpdateWorker(ConcurrentLinkedQueue<PtmCmpSku> quene, IDataBaseManager dbm, IProductService productService) {
        this.quene = quene;
        this.dbm = dbm;
        this.productService = productService;
    }


    @Override
    public void run() {
        while (true) {

            PtmCmpSku skuQ = quene.poll();

            if (skuQ == null) {
                continue;
            }

            PtmProductUpdater updater = new PtmProductUpdater(skuQ.getProductId());
            updater.getPo().setCategoryId(skuQ.getCategoryId());
            dbm.update(updater);

            PtmProduct product = productService.getProduct(skuQ.getProductId());
            productService.importProduct2Solr(product);

            System.out.println("update success for [" + skuQ.getProductId() + "] ,categoryid [" + skuQ.getCategoryId() + "]");
        }
    }
}
