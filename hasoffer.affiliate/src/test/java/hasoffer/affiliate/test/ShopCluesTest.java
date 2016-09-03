package hasoffer.affiliate.test;

import hasoffer.affiliate.affs.IAffiliateProcessor;
import hasoffer.affiliate.affs.shopclues.ShopcluesProductProcessor;
import hasoffer.affiliate.affs.shopclues.model.ShopcluesOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopCluesTest {

    public static void main(String[] args) {
        IAffiliateProcessor<ShopcluesOrder> productProcessor = new ShopcluesProductProcessor();
        Map<String, String> paraMap = new HashMap<String, String>();
        //paraMap.put("start_date", "2016-06-12");
        //paraMap.put("end_date", "2016-06-12");
        paraMap.put("api_key", "nVqV7Uh2Aj9Vw033EnmoXA");
        paraMap.put("affiliate_id", "2892");
        paraMap.put("changes_since", "2016-06-12");
        paraMap.put("include_new_conversions", "true");
        paraMap.put("start_at_row", "1");
        paraMap.put("row_limit", "0");
        paraMap.put("sort_field", "conversion_id");
        paraMap.put("sort_descending", "true");
        //paraMap.put("status", "approved");
//        status=approved

        List<ShopcluesOrder> affiliateOrderList = productProcessor.getAffiliateOrderList(null, paraMap);
        for (ShopcluesOrder order : affiliateOrderList) {
            System.out.println(order);
        }
        System.out.println(affiliateOrderList.size());
    }
}
