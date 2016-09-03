package hasoffer.admin.fetch.service.impl;

import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.dubbo.api.fetch.service.IFetchUpdateService;
import hasoffer.spider.model.FetchUrlResult;
import hasoffer.spider.model.FetchedProduct;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created on 2016/8/4.
 */
@Service
public class FetchUpdateServiceImpl implements IFetchUpdateService {

    @Resource
    ICmpSkuService cmpSkuService;

    @Override
    public void updatePtmCmpSkuInfo(FetchUrlResult fetchUrlResult) {

        Long skuId = fetchUrlResult.getSkuId();

        FetchedProduct fetchedProduct = fetchUrlResult.getFetchProduct();

        PtmCmpSku ptmCmpSku = cmpSkuService.getCmpSkuById(skuId);

        //更新ptmcmpsku
        cmpSkuService.updateCmpSkuBySpiderFetchedProduct(skuId, fetchedProduct);
        //多图
        cmpSkuService.createPtmCmpSkuImage(skuId, fetchedProduct);
        //描述
        cmpSkuService.createDescription(ptmCmpSku, fetchedProduct);

    }
}
