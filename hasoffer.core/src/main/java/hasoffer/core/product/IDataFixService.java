package hasoffer.core.product;


import hasoffer.core.bo.product.SkuBo;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;

import java.util.List;

public interface IDataFixService {

    List<PtmCmpSku> findErrorCmpSkus();

    void fixCmpskuUrl(PtmCmpSku cmpSku);

    void fixCmpskuWebsite(PtmCmpSku cmpSku);

    long getMaxProductId();

    void fixCmpskuWebsiteRepeat(long productId);

    void fixtTheitdepotUrl(PtmCmpSku ptmCmpSku);

    List<SkuBo> getErrorSkuInPriceByProductId(long productId);

}
