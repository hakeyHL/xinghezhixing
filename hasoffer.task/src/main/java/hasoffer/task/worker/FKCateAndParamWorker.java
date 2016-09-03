package hasoffer.task.worker;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCategory3;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.product.ICategoryService;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import hasoffer.core.utils.Httphelper;
import hasoffer.fetch.sites.flipkart.FlipkartHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/6/20.
 */
public class FKCateAndParamWorker implements Runnable {

    private final String requestUrl = "https://www.flipkart.com/api/3/page/dynamic/product";

    private Logger logger = LoggerFactory.getLogger(FKCateAndParamWorker.class);
    private ListProcessWorkerStatus<PtmCmpSku> ws;
    private IDataBaseManager dbm;
    private ICategoryService categoryService;
    private ICmpSkuService cmpSkuService;

    public FKCateAndParamWorker(IDataBaseManager dbm, ListProcessWorkerStatus<PtmCmpSku> ws, ICategoryService categoryService, ICmpSkuService cmpSkuService) {
        this.dbm = dbm;
        this.ws = ws;
        this.categoryService = categoryService;
        this.cmpSkuService = cmpSkuService;
    }

    @Override
    public void run() {
        while (true) {

            PtmCmpSku sku = ws.getSdQueue().poll();

            if (sku == null) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                    logger.info("flipkart category and param fetch get null sleep 3 seconds");
                } catch (InterruptedException e) {
                    return;
                }
                continue;
            }

            //for test
//            url = "http://www.flipkart.com/ap-pulse-solid-women-s-round-neck-pink-t-shirt/p/itme8arfjjawfkxv?pid=TSHE8ARFKUCKH4EH";

            try {
                createCateAndGetParam(sku);
            } catch (Exception e) {
                logger.debug(e.toString());
            }
        }
    }

    private void createCateAndGetParam(PtmCmpSku sku) {

        String sourceId = FlipkartHelper.getSkuIdByUrl(sku.getUrl());

        String json = "{\"requestContext\":{\"productId\":\"" + sourceId + "\"}}";

        Map<String, String> header = new HashMap<>();

        header.put("x-user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 FKUA/website/41/website/Desktop");

        try {

            String response = Httphelper.doPostJsonWithHeader(requestUrl, json, header);

            JSONObject jsonObject = JSONObject.parseObject(response.trim());

            JSONArray pathArray = jsonObject.getJSONObject("RESPONSE").getJSONObject("data").getJSONObject("product_breadcrumb").getJSONArray("data").getJSONObject(0).getJSONObject("value").getJSONArray("productBreadcrumbs");

            long parentid = 0;
            long categoryid = 0;

            for (int i = 1; i < pathArray.size(); i++) {

                //取最长3位
                if (i > 3) {
                    break;
                }

                //忽略第一个home节点
                String path = pathArray.getJSONObject(i).getString("title");

                PtmCategory3 ptmCategory3 = dbm.querySingle("SELECT t FROM PtmCategory3 t WHERE t.name = ?0 ", Arrays.asList(path));

                if (ptmCategory3 == null) {

                    ptmCategory3 = new PtmCategory3();
                    ptmCategory3.setLevel(i + 1);
                    ptmCategory3.setName(path);
                    ptmCategory3.setParentId(parentid);
                    ptmCategory3.setSkuid(sku.getId());

                    PtmCategory3 category = categoryService.createAppCategory(ptmCategory3);
                    categoryid = category.getId();
                } else {
                    parentid = ptmCategory3.getId();
                    categoryid = ptmCategory3.getId();
                }
            }

            cmpSkuService.updateCategoryid2(sku.getId(), categoryid);
            logger.info("update flipkart categoryid2 success for " + sku.getId());

        } catch (Exception e) {

        }
    }
}
