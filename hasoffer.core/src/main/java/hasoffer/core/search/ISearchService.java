package hasoffer.core.search;

import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.core.bo.system.SearchLogBo;
import hasoffer.core.persistence.enums.SearchPrecise;
import hasoffer.core.persistence.mongo.SrmAutoSearchResult;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.ptm.PtmProduct;
import hasoffer.core.persistence.po.search.SrmProductSearchCount;
import hasoffer.core.persistence.po.search.SrmProductSearchStat;
import hasoffer.core.persistence.po.search.SrmSearchLog;
import hasoffer.fetch.model.ListProduct;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created on 2015/12/29.
 */

public interface ISearchService {

    void statSearchCount(String ymd);

    void saveSearchCount(String ymd);

    void mergeProducts(PtmProduct finalProduct, Map<String, PtmCmpSku> cmpSkuMap, PtmProduct product);

    void relateUnmatchedSearchLog(SrmSearchLog searchLog, Map<Website, ListProduct> listProductMap);

    void relateUnmatchedSearchLogx(SrmAutoSearchResult autoSearchResult);

    void saveSearchLog(SearchLogBo searchLogBo);

    PageableResult<SrmSearchLog> listSearchLogs(int page, int size);

    PageableResult<SrmSearchLog> listSearchLogs(SearchPrecise precise, Date startTime, Date endTime, int searchCount, int page, int size);

    /**
     * @param queryType 0-全部，1-未匹配，2-匹配
     * @param page
     * @param size
     * @return
     */
    PageableResult<SrmSearchLog> listSearchLogs(int queryType, String sort, int page, int size);

    /**
     * @param queryType
     * @param sort
     * @param startTime
     * @param endTime
     * @param precise
     * @param page
     * @param size
     * @return
     */
    PageableResult<SrmSearchLog> listSearchLogs(int queryType, String sort, Date startTime, Date endTime,
                                                String precise, int page, int size);

    PageableResult<SrmSearchLog> listNoresultSearchLogs(int page, int size);

    PageableResult<SrmSearchLog> listNoresultSearchLogs(Date startDate, int page, int size);

    PageableResult<SrmSearchLog> listNoresultSearchLogs(SearchPrecise searchPrecise, Date startDate, int page, int size);


    void setResultPreciseOk(String logId, long cmpSkuId, String skuUrl);

    /**
     * 通过SrmSearchLog的id查询单个SrmSearchLog的记录
     *
     * @param id
     * @return
     */
    SrmSearchLog findSrmSearchLogById(String id);

    void setResultPreciseManualset(String logId);

    void setPtmProductId(String srmSearchLogId, long productId);

    long statLogsCountByProduct(long productId);

    PageableResult<SrmSearchLog> listSearchLogsByProductId(long productId, int page, int size);

    @Transactional(rollbackFor = Exception.class)
    void batchSaveSearchLog(List<SearchLogBo> searchLogBoList);

    long findKeywordCount(String website, String keyword);

    void updateSrmSearchLogStatus(String id, long productId, SearchPrecise precise);

    void saveLogCount(List<SrmProductSearchCount> searchCounts);

    void analysisAndRelate(SrmAutoSearchResult asr);

    PageableResult<SrmProductSearchCount> findSearchCountsByYmd(String ymd, int page, int size);

    List<SrmProductSearchStat> findSearchCountStats();

    SrmProductSearchCount findSearchCountByProductId(Long proId);
}
