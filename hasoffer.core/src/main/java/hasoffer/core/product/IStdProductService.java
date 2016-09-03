package hasoffer.core.product;

import hasoffer.affiliate.model.FlipkartSkuInfo;
import hasoffer.core.persistence.po.ptm.PtmStdProduct;

import java.util.Map;

/**
 * Created by chevy on 2016/8/12.
 */
public interface IStdProductService {

    PtmStdProduct createStd(Map<String, FlipkartSkuInfo> skuInfoMap);

    Map<String, FlipkartSkuInfo> searchSku(String keyword) throws Exception;

}
