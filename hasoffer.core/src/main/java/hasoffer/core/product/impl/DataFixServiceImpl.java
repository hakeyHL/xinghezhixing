package hasoffer.core.product.impl;

import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.bo.product.SkuBo;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.updater.PtmCmpSkuUpdater;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.product.IDataFixService;
import hasoffer.fetch.helper.WebsiteHelper;
import hasoffer.fetch.sites.theitdepot.TheitdepotHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/19
 */
@Service
public class DataFixServiceImpl implements IDataFixService {

    private static final String Q_CMPSKU_NULL_WEBSITE =
            "SELECT t FROM PtmCmpSku t WHERE website IS NULL";

    //该sql用来查询同一个ptmproductid下的所有sku
    private static final String Q_SKUS_BYPRODUCTID =
            "SELECT t FROM PtmCmpSku t WHERE t.productId = ?0 AND t.status = 'ONSALE' ORDER BY t.id ASC";
    private static final String Q_MAXPRODUCTID =
            "SELECT MAX(t.productId) FROM PtmCmpSku t ";

    @Resource
    IDataBaseManager dbm;
    @Resource
    ICmpSkuService cmpSkuService;

    @Override
    public List<PtmCmpSku> findErrorCmpSkus() {
        return dbm.query(Q_CMPSKU_NULL_WEBSITE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fixCmpskuUrl(PtmCmpSku cmpSku) {
        if (StringUtils.isEmpty(cmpSku.getOriUrl())) {
            return;
        }

        PtmCmpSkuUpdater ptmCmpSkuUpdater = new PtmCmpSkuUpdater(cmpSku.getId());

        String url = cmpSku.getUrl();
        String realUrl = WebsiteHelper.getRealUrl(cmpSku.getOriUrl());

        if (url == null || !url.equals(realUrl)) {
            ptmCmpSkuUpdater.getPo().setUrl(realUrl);
        }

        Website website = WebsiteHelper.getWebSite(realUrl);
        if (website != cmpSku.getWebsite()) {
            ptmCmpSkuUpdater.getPo().setWebsite(website);
        }

        dbm.update(ptmCmpSkuUpdater);
    }

    @Override
    public void fixCmpskuWebsite(PtmCmpSku cmpSku) {

        String url = cmpSku.getUrl();
        String oriUrl = cmpSku.getOriUrl();

        PtmCmpSkuUpdater updater = new PtmCmpSkuUpdater(cmpSku.getId());

        if (url != null && !"null".equals(url)) {
            Website webSite = WebsiteHelper.getWebSite(url);
            if (webSite == null) {
                url = WebsiteHelper.getRealUrl(oriUrl);
                webSite = WebsiteHelper.getWebSite(url);
                if (webSite == null) {
                    return;
                }
                updater.getPo().setWebsite(webSite);
                dbm.update(updater);
                return;
            }
            updater.getPo().setWebsite(webSite);
            dbm.update(updater);
            return;
        }
    }

    @Override
    public long getMaxProductId() {
        long maxProductId = dbm.querySingle(Q_MAXPRODUCTID);
        return maxProductId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fixCmpskuWebsiteRepeat(long productId) {
        List<PtmCmpSku> cmpSkus = dbm.query(Q_SKUS_BYPRODUCTID, Arrays.asList(productId));

        if (!ArrayUtils.hasObjs(cmpSkus)) {
            return;
        }

        List<Website> websiteList = new ArrayList<Website>();
        for (PtmCmpSku ptmCmpSku : cmpSkus) {
            if (ptmCmpSku.getWebsite() == null || websiteList.contains(ptmCmpSku.getWebsite())) {
                dbm.delete(PtmCmpSku.class, ptmCmpSku.getId());
            } else {
                if (ptmCmpSku.getWebsite() != null) {
                    websiteList.add(ptmCmpSku.getWebsite());
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fixtTheitdepotUrl(PtmCmpSku ptmCmpSku) {

        String theItDepotPreFix = "https://www.theitdepot.com/details.php";
        String oriUrl = ptmCmpSku.getOriUrl();
        if (StringUtils.isEmpty(oriUrl)) {
            dbm.delete(PtmCmpSku.class, ptmCmpSku.getId());
            return;
        }
        String sourceId = TheitdepotHelper.getSourceIdByUrl(oriUrl);

        PtmCmpSkuUpdater updater = new PtmCmpSkuUpdater(ptmCmpSku.getId());

        updater.getPo().setUrl(theItDepotPreFix + "?prodid=" + sourceId);

        dbm.update(updater);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SkuBo> getErrorSkuInPriceByProductId(long productId) {

        List<PtmCmpSku> skus = dbm.query(Q_SKUS_BYPRODUCTID, Arrays.asList(productId));

        if (skus.size() <= 1) {
            return null;
        }

        List<SkuBo> skuBoList = new ArrayList<SkuBo>();

        for (PtmCmpSku sku : skus) {

            SkuBo skuBo = new SkuBo();

            skuBo.setId(sku.getId());

            if (sku.getPrice() == 0) {
                continue;
            }

            skuBo.setPrice(sku.getPrice());

            skuBoList.add(skuBo);
        }

        if (skuBoList.size() <= 1) {
            return null;
        }

        Collections.sort(skuBoList, new Comparator<SkuBo>() {
            @Override
            public int compare(SkuBo o1, SkuBo o2) {
                if (o1.getPrice() > o2.getPrice()) {
                    return 1;
                } else if (o1.getPrice() < o2.getPrice()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        float maxPrice = skuBoList.get(skuBoList.size() - 1).getPrice();
        float minPrice = skuBoList.get(0).getPrice();

        if (maxPrice / minPrice < 5) {
            return null;
        }

        return skuBoList;
    }
}
