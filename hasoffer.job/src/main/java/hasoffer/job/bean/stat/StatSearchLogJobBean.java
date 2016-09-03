package hasoffer.job.bean.stat;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.cache.SearchLogCacheManager;
import hasoffer.core.product.IProductService;
import hasoffer.core.search.ISearchService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

/**
 * Created on 2016/6/27.
 */
public class StatSearchLogJobBean extends QuartzJobBean {

    @Resource
    SearchLogCacheManager logCacheManager;
    @Resource
    ISearchService searchService;
    @Resource
    IProductService productService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//        String ymd = TimeUtils.parse(TimeUtils.today(), "yyyyMMdd");
        String ymd = TimeUtils.parse(TimeUtils.yesterday(), "yyyyMMdd");

        // 保存所有被搜索过的商品
        searchService.saveSearchCount(ymd);

        // top selling
        productService.expTopSellingsFromSearchCount(ymd);

        // 统计比价质量
        searchService.statSearchCount(ymd);
    }
}
