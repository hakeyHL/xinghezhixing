package hasoffer.affiliate.core;

import hasoffer.base.model.Website;

/**
 * Date : 2016/3/7
 * Function :
 */
public interface IAffClient {

    /**
     * 查询该网站的类目
     * @param website
     */
    void listCategoies(Website website);

    /**
     * 查询该网站的商品列表
     *
     * @param website
     * @param cateId
     */
    void listProducts(String cateId);

    /**
     * 获取商品详情
     *
     * @param website
     * @param proId
     */
    void getProductDetail(String proId);
}
