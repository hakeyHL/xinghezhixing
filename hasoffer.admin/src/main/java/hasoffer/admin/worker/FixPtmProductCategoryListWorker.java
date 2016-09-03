package hasoffer.admin.worker;

import hasoffer.base.model.PageableResult;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/6/30.
 */
public class FixPtmProductCategoryListWorker implements Runnable {

    private static final String Q_FLIPKART = "SELECT t FROM PtmCmpSku t WHERE t.website = 'FLIPKART' ORDER BY t.id";
    private static final String Q_PRODUCT_BYID = "SELECT t FROM PtmProduct t WHERE t.id = ?0";

    private IDataBaseManager dbm;
    private ConcurrentLinkedQueue<PtmCmpSku> quene;

    public FixPtmProductCategoryListWorker(IDataBaseManager dbm, ConcurrentLinkedQueue<PtmCmpSku> quene) {
        this.dbm = dbm;
        this.quene = quene;
    }

    @Override
    public void run() {

        int pageSize = 1000;
        int curPage = 1;

        PageableResult<PtmCmpSku> pageableResult = dbm.queryPage(Q_FLIPKART, curPage, pageSize);

        long totalPage = pageableResult.getTotalPage();

        while (curPage < totalPage) {

            pageableResult = dbm.queryPage(Q_FLIPKART, curPage, pageSize);

            List<PtmCmpSku> skus = pageableResult.getData();

            for (PtmCmpSku sku : skus) {

                if (sku.getCategoryId() == null) {
                    continue;
                }
                if (sku.getCategoryId() == 0) {
                    continue;
                }
                if (sku.getProductId() == 0) {
                    continue;
                }

                PtmProduct product = dbm.querySingle(Q_PRODUCT_BYID, Arrays.asList(sku.getProductId()));
                if (product == null) {
                    continue;
                }

//              在二次修复ptmproduct的类目数据时，需要注释掉以下部分
//                if (product.getCategoryId() != 0) {
//                    continue;
//                }

                quene.add(sku);

                if (quene.size() > 3000) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            curPage++;
            System.out.println("FixPtmProductCategoryListWorker curpage:" + curPage + ",totalpage:" + totalPage);
        }
    }
}
