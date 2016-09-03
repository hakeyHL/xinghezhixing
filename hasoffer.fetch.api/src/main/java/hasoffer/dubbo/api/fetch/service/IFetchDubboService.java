package hasoffer.dubbo.api.fetch.service;

import hasoffer.base.enums.TaskLevel;
import hasoffer.base.enums.TaskStatus;
import hasoffer.base.model.Website;
import hasoffer.spider.model.FetchResult;
import hasoffer.spider.model.FetchUrlResult;

public interface IFetchDubboService {

    /**
     * 获取结果
     *
     * @param webSite
     * @param keyword
     * @return
     */
    FetchResult getProductsKeyWord(Website webSite, String keyword);

    /**
     * 提交关键词任务
     *
     * @param website
     * @param keyword
     */
    void sendKeyWordTask(Website website, String keyword);

    /**
     * 获取关键词任务对应的状态
     *
     * @param webSite
     * @param keyword
     * @return
     */
    TaskStatus getKeyWordTaskStatus(Website webSite, String keyword);

    /**
     * 提交URL更新任务，该任务级别默认为TaskLevel.LEVEL_5(最低)。
     *
     * @param website
     * @param url
     */
    void sendUrlTask(Website website, String url);

    /**
     * 提交URL更新任务
     *
     * @param website
     * @param url
     * @param taskLevel 任务优先度，分为5级，TaskLevel.LEVEL_1(最高)，TaskLevel.LEVEL_5(最低)。
     */
    void sendUrlTask(Website website, String url, TaskLevel taskLevel);

    /**
     * 获取URL任务的状态。
     *
     * @param website
     * @param url
     * @return
     */
    TaskStatus getUrlTaskStatus(Website website, String url);

    /**
     * 获取URL任务的结果
     *
     * @param skuId
     * @param webSite
     * @param url
     * @return
     */
    FetchUrlResult getProductsByUrl(Long skuId, Website webSite, String url);


}
