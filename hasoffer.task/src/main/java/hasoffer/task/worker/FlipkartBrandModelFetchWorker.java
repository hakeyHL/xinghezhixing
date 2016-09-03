package hasoffer.task.worker;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import hasoffer.core.utils.Httphelper;
import hasoffer.fetch.sites.flipkart.FlipkartHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/8/18.
 */
public class FlipkartBrandModelFetchWorker implements Runnable {
    private final String requestUrl = "https://www.flipkart.com/api/3/page/dynamic/product";

    private Logger logger = LoggerFactory.getLogger(FKCateAndParamWorker.class);
    private ListProcessWorkerStatus<PtmCmpSku> ws;
    private ICmpSkuService cmpSkuService;

    public FlipkartBrandModelFetchWorker(ICmpSkuService cmpSkuService, ListProcessWorkerStatus<PtmCmpSku> ws) {
        this.cmpSkuService = cmpSkuService;
        this.ws = ws;
    }

    @Override
    public void run() {
        while (true) {

            PtmCmpSku sku = ws.getSdQueue().poll();

            if (sku == null) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                    logger.debug("flipkart category and param fetch get null sleep 3 seconds");
                } catch (InterruptedException e) {
                    return;
                }
                continue;
            }

            //for test
//            url = "http://www.flipkart.com/ap-pulse-solid-women-s-round-neck-pink-t-shirt/p/itme8arfjjawfkxv?pid=TSHE8ARFKUCKH4EH";

            try {
                fetchBranAmdModel(sku);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchBranAmdModel(PtmCmpSku sku) throws Exception {

        String sourceId = FlipkartHelper.getSkuIdByUrl(sku.getUrl());

        String json = "{\"requestContext\":{\"productId\":\"" + sourceId + "\"}}";

        Map<String, String> header = new HashMap<>();

        header.put("x-user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 FKUA/website/41/website/Desktop");

        String response = Httphelper.doPostJsonWithHeader(requestUrl, json, header);

        JSONObject jsonObject = JSONObject.parseObject(response.trim());

        String brand = "";
        String model = "";

        JSONObject specificationJo = jsonObject.getJSONObject("RESPONSE").getJSONObject("data").getJSONObject("product_specification_1");
        if (specificationJo != null) {
            JSONArray specs = specificationJo.getJSONArray("data");
            for (int i = 0; i < specs.size(); i++) {
                JSONObject specJsonTab = specs.getJSONObject(i);
                JSONArray specAttrs = specJsonTab.getJSONObject("value").getJSONArray("attributes");
                for (int j = 0; j < specAttrs.size(); j++) {
                    JSONObject specJson = specAttrs.getJSONObject(j);
                    String key = specJson.getString("name");
                    if (StringUtils.isEmpty(key)) {
                        continue;
                    }

                    String val = specJson.getJSONArray("values").getString(0);

                    //在参数中寻找brand
                    if (StringUtils.isEqual(key, "Brand")) {
                        brand = val;
                    }

                    //在参数中寻找“Model Name”，“Model”，“Model Number”,"Model ID","Part Number","Series","model_name"
                    if (StringUtils.isEqual(key, "Model Name")) {
                        model += val + ",";
                    }
                    if (StringUtils.isEqual(key, "Model")) {
                        model += val + ",";
                    }
                    if (StringUtils.isEqual(key, "Model Number")) {
                        model += val + ",";
                    }
                    if (StringUtils.isEqual(key, "Model ID")) {
                        model += val + ",";
                    }
                    if (StringUtils.isEqual(key, "Part Number")) {
                        model += val + ",";
                    }
                    if (StringUtils.isEqual(key, "Series")) {
                        model += val + ",";
                    }
                    if (StringUtils.isEqual(key, "model_name")) {
                        model += val + ",";
                    }
                }
            }
        }

        if (!StringUtils.isEmpty(brand) || !StringUtils.isEmpty(model)) {
            cmpSkuService.updateFlipakrtSkuBrandAndModel(sku.getId(), brand, model);
            System.out.println("fetch brand and model for flipkart sku success for " + sku.getId());
        }
    }
}
