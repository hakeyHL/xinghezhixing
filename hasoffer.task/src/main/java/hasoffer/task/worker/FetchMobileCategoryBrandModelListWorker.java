package hasoffer.task.worker;

import hasoffer.base.enums.TaskLevel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.dubbo.api.fetch.service.IFetchDubboService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/8/26.
 */
public class FetchMobileCategoryBrandModelListWorker implements Runnable {

    private IDataBaseManager dbm;
    private ConcurrentLinkedQueue cmpSkuQueue;
    private IFetchDubboService fetchDubboService;

    public FetchMobileCategoryBrandModelListWorker(IDataBaseManager dbm, ConcurrentLinkedQueue cmpSkuQueue, IFetchDubboService fetchDubboService) {
        this.dbm = dbm;
        this.cmpSkuQueue = cmpSkuQueue;
        this.fetchDubboService = fetchDubboService;
    }

    @Override
    public void run() {
        int curPage = 1;
        int pageSize = 1000;
        PageableResult<PtmProduct> pageableResult = dbm.queryPage("SELECT t FROM PtmProduct t WHERE t.categoryId = 5 ORDER BY t.id", curPage, pageSize);

        long totalPage = pageableResult.getTotalPage();
        System.out.println("total page " + totalPage);

        while (curPage <= totalPage) {

            if (cmpSkuQueue.size() > 10000) {
                try {
                    System.out.println("queue size" + cmpSkuQueue.size());
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {

                }
                continue;
            }

            if (curPage > 1) {
                pageableResult = dbm.queryPage("SELECT t FROM PtmProduct t WHERE t.categoryId = 5 ORDER BY t.id", curPage, pageSize);
            }

            List<PtmProduct> ptmProductList = pageableResult.getData();

            for (PtmProduct ptmProduct : ptmProductList) {

                List<PtmCmpSku> skuList = dbm.query("SELECT t FROM PtmCmpSku t WHERE t.productId = ?0", Arrays.asList(ptmProduct.getId()));

                for (PtmCmpSku ptmCmpSku : skuList) {

                    Website website = ptmCmpSku.getWebsite();
                    //flipkart,snapdeal,amazon,ebay
                    if (Website.FLIPKART.equals(website) || Website.SNAPDEAL.equals(website) || Website.AMAZON.equals(website) || Website.EBAY.equals(website)) {
                        cmpSkuQueue.add(ptmCmpSku);
//                        System.out.println("add success to queue " + ptmCmpSku.getId());
                        fetchDubboService.sendUrlTask(ptmCmpSku.getWebsite(), ptmCmpSku.getUrl(), TaskLevel.LEVEL_2);
//                        System.out.println("send request success for " + ptmCmpSku.getId());
                    }
                }
            }

            System.out.println("curPage = " + curPage);
            curPage++;
        }
    }
}
