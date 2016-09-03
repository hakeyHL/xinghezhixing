package hasoffer.fetch.sites.voodoo;

import com.google.gson.Gson;
import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.model.Website;
import hasoffer.base.utils.JSONUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.http.HttpUtils;
import hasoffer.fetch.model.OriFetchedProduct;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.ProductStatus;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date : 2016/5/4
 * Function :
 */
public class VoodooHelper {

    public static void getProductsFromVoodoo(Map<Website, ListProduct> proMap, String title) {

        String json = JSONUtil.toJSON(new VoodooRequest(title));
        Map<String, String> header = new HashMap<String, String>();
        header.put("key", voodoo(voodoo(voodoo(String.valueOf(json.length())))));
        header.put("Content-Type", "application/json; charset=UTF-8");

        HttpResponseModel responseModel = HttpUtils.postByRaw("http://api.getvoodoo.in/rest/sherlock/similar-products", json, header);

        Gson gson = new Gson();
        try {
            VoodooData vd = gson.fromJson(responseModel.getBodyString(), VoodooData.class);
            List<VoodooProduct> vps = vd.getProducts();

            getListProducts(vps, proMap);

        } catch (Exception e) {
            // error
        }
    }

    private static void getListProducts(List<VoodooProduct> vps, Map<Website, ListProduct> proMap) {
        for (VoodooProduct vp : vps) {
            try {
//                System.out.println(vp.getMerchant() + "\t" + vp.getTitle());
                ListProduct lp = getListProduct(vp);

                if(proMap.containsKey(lp.getWebsite())){
                    continue;
                }

                proMap.put(lp.getWebsite(), lp);
            } catch (Exception e) {
                continue;
            }
        }
    }

    private static ListProduct getListProduct(VoodooProduct vp) {
        String site = vp.getMerchant().toUpperCase();
        Website website = Website.valueOf(site);

        String sourceId = "";
        String title = StringUtils.captureTitle(vp.getTitle());

        String imageUrl = vp.getImgUrl();

        boolean inStock = vp.isInStock();
        ProductStatus productStatus = inStock ? ProductStatus.ONSALE : ProductStatus.OUTSTOCK;

        return new ListProduct(0L, sourceId, vp.getProductUrl(), imageUrl, title, vp.getVoodooPrice(), website, productStatus);
    }

    private static void getSummaryProducts(List<VoodooProduct> vps, Map<Website, OriFetchedProduct> proMap) {
        for (VoodooProduct vp : vps) {
            try {
                OriFetchedProduct sp = getSummaryProduct(vp);
                proMap.put(sp.getWebsite(), sp);
            } catch (Exception e) {
                continue;
            }
        }
    }

    private static OriFetchedProduct getSummaryProduct(VoodooProduct vp) {
        String site = vp.getMerchant().toUpperCase();
        Website website = Website.valueOf(site);

        String sourceId = "";
        String title = vp.getTitle();

        String imageUrl = vp.getImgUrl();

        boolean inStock = vp.isInStock();
        ProductStatus productStatus = inStock ? ProductStatus.ONSALE : ProductStatus.OUTSTOCK;

        return new OriFetchedProduct(sourceId, website, title, imageUrl, productStatus, vp.getVoodooPrice(), vp.getProductUrl(), vp.getDeeplinkUrl());
    }

    // voodoo 用于加密的算法
    public static String voodoo(String paramString) {
        try {
            String str;
            for (paramString = new BigInteger(1, MessageDigest.getInstance("MD5").digest(paramString.getBytes())).toString(16); ; paramString = "0" + paramString) {
                str = paramString;
                if (paramString.length() >= 32) {
                    break;
                }
            }
            return str;
        } catch (NoSuchAlgorithmException s) {
            return null;
        }
    }
}
