package hasoffer.affiliate.affs;


import hasoffer.affiliate.exception.AffiliateAPIException;
import hasoffer.affiliate.model.AffiliateCategory;
import hasoffer.affiliate.model.AffiliateOrder;
import hasoffer.affiliate.model.AffiliateProduct;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created on 2016/3/7.
 */
public interface IAffiliateProcessor<T> {

    /**
     * 如有必要获取一个动态的token
     */
    String getAffiliateToken() throws IOException;


    /**
     * 获取订单
     * @param headerMap
     * @param parameterMap
     * @return
     */
    List<T> getAffiliateOrderList(Map<String, String> headerMap, Map<String, String> parameterMap);

    String sendRequest(String url, Map<String, String> headerMap , Map<String, String> paramMap) throws AffiliateAPIException, IOException;

    /**
     * @return
     * @throws AffiliateAPIException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    List<AffiliateCategory> getProductDirectory() throws AffiliateAPIException, IllegalAccessException, InstantiationException, IOException;

    /**
     * 根据nextUrl解析Product信息
     *
     * @param nextUrl
     * @return
     */
    List<AffiliateProduct> getProductList(String nextUrl) throws AffiliateAPIException, IOException;

    /**
     * 获取分页的商品信息
     *
     * @param productUrl
     * @return
     */
    List<String> getProductNextUrlList(String productUrl) throws AffiliateAPIException, IOException;

    /**
     * 根据关键字查询商品
     * @param keyword
     * @param resultNum
     * @return
     * @throws AffiliateAPIException
     * @throws IOException
     */
    List<AffiliateProduct> getAffiliateProductByKeyword(String keyword, int resultNum) throws AffiliateAPIException, IOException;

    /**
     * 根据商品在联盟网站中的sourceId获取商品信息
     *
     * @param sourceId
     * @return
     */
    AffiliateProduct getAffiliateProductBySourceId(String sourceId) throws AffiliateAPIException, IOException;

    ///**
    // * 获取订单信息
    // * @return
    // */
    //List<T> getAffiliateOrderList(Map<String, String> parameterMap);

}
