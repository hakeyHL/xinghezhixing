package hasoffer.core.product.impl;

import hasoffer.affiliate.affs.flipkart.FlipkartAffiliateProductProcessor;
import hasoffer.affiliate.model.AffiliateProduct;
import hasoffer.affiliate.model.FlipkartAttribute;
import hasoffer.affiliate.model.FlipkartSkuInfo;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.analysis.ProductAnalysisService;
import hasoffer.core.analysis.model.FlipkartSearchedSkuAnalysisResult;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmStdDef;
import hasoffer.core.persistence.po.ptm.PtmStdProduct;
import hasoffer.core.persistence.po.ptm.PtmStdSku;
import hasoffer.core.persistence.po.ptm.PtmStdSkuValue;
import hasoffer.core.product.IStdProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by chevy on 2016/8/12.
 */
@Service
public class StdProductServiceImpl implements IStdProductService {

    @Resource
    IDataBaseManager dbm;

    @Override
    public Map<String, FlipkartSkuInfo> searchSku(String keyword) throws Exception {
        FlipkartAffiliateProductProcessor fapp = new FlipkartAffiliateProductProcessor();
        List<AffiliateProduct> searchedPros = fapp.getAffiliateProductByKeyword(keyword, 10);

        if (ArrayUtils.isNullOrEmpty(searchedPros)) {
            System.out.println("no searched results.");
            return null;
        }

        FlipkartSkuInfo skuInfo = null;
        for (AffiliateProduct ap : searchedPros) {
            FlipkartSkuInfo for_skuInfo = fapp.getSkuInfo(ap.getSourceId());
            System.out.println("[title]" + for_skuInfo.getTitle() + "\t| [brand]\t" + for_skuInfo.getProductBrand() + "\t| [model]"
                    + for_skuInfo.getModelNum() + "|"
                    + for_skuInfo.getModelName() + "|"
                    + for_skuInfo.getModelId());
            if (keyword.contains(for_skuInfo.getProductBrand().toLowerCase())
                    &&
                    (keyword.contains(for_skuInfo.getModelNum().toLowerCase())
                            || keyword.contains(for_skuInfo.getModelName().toLowerCase())
                            || keyword.contains(for_skuInfo.getModelId().toLowerCase())
                    )) {
                skuInfo = for_skuInfo;
                break;
            }
        }

        if (skuInfo == null) {
            return null;
        }

        Map<String, FlipkartSkuInfo> skuInfoMap = new HashMap<>();

        String[] sourceIds = skuInfo.getProductFamily();
        skuInfoMap.put(skuInfo.getProductId(), skuInfo);

        for (String sid : sourceIds) {
            try {
                FlipkartSkuInfo skuInfo1 = fapp.getSkuInfo(sid);
                skuInfoMap.put(skuInfo1.getProductId(), skuInfo1);

                System.out.println(skuInfo1.getProductBrand() + "|\t" + skuInfo1.getModelName() + "|\t" + skuInfo1.getAttributes());
            } catch (Exception e) {
                System.out.println("error");
            }
        }

        return skuInfoMap;
    }

    public Map<String, FlipkartSkuInfo> searchSku_bak(String keyword) throws Exception {
        FlipkartAffiliateProductProcessor fapp = new FlipkartAffiliateProductProcessor();
        List<AffiliateProduct> searchedPros = fapp.getAffiliateProductByKeyword(keyword, 10);

        if (ArrayUtils.isNullOrEmpty(searchedPros)) {
            System.out.println("no searched results.");
            return null;
        }

        List<FlipkartSearchedSkuAnalysisResult> analysisResults = new ArrayList<>();

        for (AffiliateProduct ap : searchedPros) {
            float score = ProductAnalysisService.stringMatch(keyword, ap.getTitle());
            System.out.println(ap.getSourceId() + "\t" + ap.getTitle() + "\t" + score);
            analysisResults.add(new FlipkartSearchedSkuAnalysisResult(score, ap));
        }

        Collections.sort(analysisResults, new Comparator<FlipkartSearchedSkuAnalysisResult>() {
            @Override
            public int compare(FlipkartSearchedSkuAnalysisResult o1, FlipkartSearchedSkuAnalysisResult o2) {
                if (o1.getScore() > o2.getScore()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        String sourceId = analysisResults.get(0).getAp().getSourceId();
        FlipkartSkuInfo skuInfo = fapp.getSkuInfo(sourceId);

        if (StringUtils.isEmpty(skuInfo.getProductBrand()) || StringUtils.isEmpty(skuInfo.getModelName())) {
            System.out.println(skuInfo.getTitle() + "\t|\t" + skuInfo.getProductBrand() + "\t|\t" + skuInfo.getModelName());
            return null;
        }

        Map<String, FlipkartSkuInfo> skuInfoMap = new HashMap<>();

        String[] sourceIds = skuInfo.getProductFamily();
        skuInfoMap.put(sourceId, skuInfo);

        for (String sid : sourceIds) {
            try {
                FlipkartSkuInfo skuInfo1 = fapp.getSkuInfo(sid);
                skuInfoMap.put(skuInfo1.getProductId(), skuInfo1);

                System.out.println(skuInfo1.getProductBrand() + "|\t" + skuInfo1.getModelName() + "|\t" + skuInfo1.getAttributes());
            } catch (Exception e) {
                System.out.println("error");
            }
        }

        return skuInfoMap;
    }

    @Override
    @Transactional
    public PtmStdProduct createStd(Map<String, FlipkartSkuInfo> skuInfoMap) {
        if (skuInfoMap == null) {
            System.out.println("sku map is null.");
            return null;
        }
        Set<Map.Entry<String, FlipkartSkuInfo>> skuInfoSet = skuInfoMap.entrySet();
        Iterator<Map.Entry<String, FlipkartSkuInfo>> it = skuInfoSet.iterator();

        // No.1
        Map.Entry<String, FlipkartSkuInfo> kv = it.next();
        FlipkartSkuInfo skuInfo = kv.getValue();

        // build std product
        String productName = skuInfo.getTitle();
        String brandName = skuInfo.getProductBrand();
        String desc = skuInfo.getDesc();

        String modelName = skuInfo.getModelName();
        if (StringUtils.isEmpty(modelName)) {
            modelName = skuInfo.getModelNum();
            if (StringUtils.isEmpty(modelName)) {
                modelName = skuInfo.getModelId();
            }
        }

        if (StringUtils.isEmpty(brandName) || StringUtils.isEmpty(modelName)) {
            System.out.println(String.format("brand[%s].model[%s].one is empty.skipped.", brandName, modelName));
            return null;
        }

        // query product by brand and model
        long count = dbm.querySingle("select count(t.id) from PtmStdProduct t where t.brand=?0 and t.model=?1 ", Arrays.asList(brandName, modelName));
        if (count >= 1) {
            System.out.println(String.format("brand[%s].model[%s].exists.", brandName, modelName));
            return null;
        }

        PtmStdProduct stdProduct = new PtmStdProduct(productName, brandName, modelName, desc);

        // create product
        dbm.create(stdProduct);

        do {
            createStdSku(stdProduct.getId(), skuInfo);

            if (it.hasNext()) {
                kv = it.next();
                skuInfo = kv.getValue();
            } else {
                break;
            }
        } while (skuInfo != null);

        System.out.println(skuInfoMap.size());
        return stdProduct;
    }

    private void createStdSku(long stdProductId, FlipkartSkuInfo fsi) {
        PtmStdSku stdSku = new PtmStdSku(stdProductId, fsi.getTitle(), fsi.getFlipkartSellingPrice().getAmount());
        // create sku
        dbm.create(stdSku);

        createStdSkuValues(stdSku.getId(), fsi.getAttributes());
    }

    private void createStdSkuValues(long stdSkuId, FlipkartAttribute fa) {

        String color = fa.getColor();
        String displaySize = fa.getDisplaySize();
        String size = fa.getSize();
        String sizeUnit = fa.getSizeUnit();
        String storage = fa.getStorage();

        setStdSkuValue(stdSkuId, "color", color);
        setStdSkuValue(stdSkuId, "displaySize", displaySize);
        setStdSkuValue(stdSkuId, "size", size);
        setStdSkuValue(stdSkuId, "sizeUnit", sizeUnit);
        setStdSkuValue(stdSkuId, "storage", storage);
    }

    private void setStdSkuValue(long stdSkuId, String stdName, String value) {
        if (StringUtils.isEmpty(value) || StringUtils.isEmpty(value.trim())) {
            return;
        }

        PtmStdDef ptmStdDef = new PtmStdDef(stdName);
        dbm.createIfNoExist(ptmStdDef);

        PtmStdSkuValue stdSkuValue = new PtmStdSkuValue(stdSkuId, ptmStdDef.getId(), ptmStdDef.getStdName(), value);
        dbm.create(stdSkuValue);
    }
}
