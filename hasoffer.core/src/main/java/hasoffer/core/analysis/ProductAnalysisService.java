package hasoffer.core.analysis;

import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.bo.product.ProductBo;
import hasoffer.core.bo.product.SearchedSku;
import hasoffer.core.persistence.mongo.SrmAutoSearchResult;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.fetch.model.ListProduct;
import hasoffer.fetch.model.WebFetchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created on 2016/7/1.
 */
public class ProductAnalysisService {

    private static Logger logger = LoggerFactory.getLogger(ProductAnalysisService.class);

    public static void analysisProducts(SrmAutoSearchResult searchResult, ProductBo productBo) {
        if (searchResult == null) {
            return;
        }
        Map<Website, List<SearchedSku>> searchedSkusMap = new LinkedHashMap<Website, List<SearchedSku>>();

        Comparator comparator = new Comparator<SearchedSku>() {
            @Override
            public int compare(SearchedSku p1, SearchedSku p2) {
                float score1 = p1.getTitleScore();
                float score2 = p2.getTitleScore();

                if (score1 > score2) {
                    return -1;
                } else if (score1 < score2) {
                    return 1;
                } else if (score1 == score2) {
                    float priceScore1 = p1.getPriceScore();
                    float priceScore2 = p2.getPriceScore();

                    if (priceScore1 == 0 && priceScore2 == 0) {
                        return 0;
                    } else {
                        if (priceScore1 > priceScore2) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                }
                return 0;
            }
        };

        Map<Website, WebFetchResult> listProductMap = searchResult.getSitePros();

        String keyword = searchResult.getTitle();
        Website logSite = Website.valueOf(searchResult.getFromWebsite());

        float stdPrice = 0;//searchResult.getPrice();
        float maxTitleScore = 0;

        // 如果已经关联了商品，则要考虑已关联商品的情况
        if (productBo != null) {

            keyword = productBo.getTitle();

            List<PtmCmpSku> relatedCmpSkus = productBo.getCmpSkus();

            float sumPrice = 0.0f;
            int validPriceCount = 0;
            for (PtmCmpSku cmpSku : relatedCmpSkus) {
                float price = cmpSku.getPrice();
                if (price <= 0) {
                    continue;
                }

                sumPrice += price;
                validPriceCount++;
            }

            stdPrice = sumPrice / validPriceCount;
        } else {
            WebFetchResult webFetchResult = listProductMap.get(logSite);
            if (webFetchResult == null) {
                return;
            }
            List<ListProduct> logPros = webFetchResult.getProductList();
            if (ArrayUtils.hasObjs(logPros)) {
                for (ListProduct lp : logPros) {
                    float titleScore = stringMatch(lp.getTitle(), keyword);
                    if (maxTitleScore < titleScore) {
                        maxTitleScore = titleScore;
                        stdPrice = lp.getPrice();
                    }
                }
            } else {
                return;
            }
        }

        for (Map.Entry<Website, WebFetchResult> kv : listProductMap.entrySet()) {
            Website website = kv.getKey();
            List<ListProduct> products = kv.getValue().getProductList();
            List<SearchedSku> searchedSkus = new ArrayList<SearchedSku>();

            for (ListProduct lp : products) {
                float titleScore = stringMatch(lp.getTitle(), keyword);
                float priceScore = 0.0f;
                if (stdPrice > 0) {
                    priceScore = Math.abs(stdPrice - lp.getPrice()) / stdPrice;
                }

                searchedSkus.add(
                        new SearchedSku(lp.getWebsite(), lp.getTitle(),
                                titleScore, lp.getPrice(), priceScore,
                                lp.getSourceId(), lp.getUrl(),
                                lp.getImageUrl(), lp.getStatus())
                );
            }

            if (ArrayUtils.hasObjs(searchedSkus)) {
                logger.debug(String.format("Get [%d] skus from [%s]", searchedSkus.size(), website.name()));
                Collections.sort(searchedSkus, comparator);
                searchedSkusMap.put(website, searchedSkus);
            }
        }

        searchResult.setFinalSkus(searchedSkusMap);
    }

    public static float stringMatch(String s1, String s2) {

        s1 = StringUtils.toLowerCase(s1);
        s2 = StringUtils.toLowerCase(s2);

        String[] ss1 = StringUtils.getCleanWords(s1);
        String[] ss2 = StringUtils.getCleanWords(s2);

        return StringUtils.wordsMatchD(ss1, ss2);
    }

}
