package hasoffer.task.worker;

import hasoffer.base.enums.TaskLevel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.model.Website;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.persistence.po.search.SrmProductSearchCount;
import hasoffer.dubbo.api.fetch.service.IFetchDubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/5/13.
 */
public class SrmProductSearchCountListWorker implements Runnable {

    private static final String Q_PTMCMPSKU_BYPRODUCTID = "SELECT t FROM PtmCmpSku t WHERE t.productId = ?0 ORDER BY t.id ASC";
    private static Logger logger = LoggerFactory.getLogger(SrmProductSearchCountListWorker.class);

    private IFetchDubboService fetchDubboService;
    private IDataBaseManager dbm;
    private ConcurrentLinkedQueue<PtmCmpSku> queue;

    public SrmProductSearchCountListWorker(IDataBaseManager dbm, ConcurrentLinkedQueue<PtmCmpSku> queue, IFetchDubboService fetchDubboService) {
        this.dbm = dbm;
        this.queue = queue;
        this.fetchDubboService = fetchDubboService;
    }

    @Override
    public void run() {

        int page = 1;
        int pageSize = 1000;

        String startDateString = TimeUtils.parse(TimeUtils.today() - TimeUtils.MILLISECONDS_OF_1_DAY * 1, "yyyyMMdd");

        String Q_LOG_BYUPDATETIME = "SELECT t FROM SrmProductSearchCount t WHERE t.ymd = ?0 AND t.count > 5 ORDER BY t.id ASC";

        PageableResult<SrmProductSearchCount> pageableResult = dbm.queryPage(Q_LOG_BYUPDATETIME, page, pageSize, Arrays.asList(startDateString));

        long totalPage = pageableResult.getTotalPage();
        logger.info("totalPage :" + totalPage);

        while (page <= totalPage) {

            if (queue.size() > 50000) {
                logger.info("queue size =" + queue.size());
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            logger.info("curpage :" + page);

            if (page > 1) {
                pageableResult = dbm.queryPage(Q_LOG_BYUPDATETIME, page, pageSize, Arrays.asList(startDateString));
            }

            List<SrmProductSearchCount> dataList = pageableResult.getData();

            if (ArrayUtils.hasObjs(dataList)) {

                //暂时先拼凑一个srmsearchlog用于适配更新的接口
                for (SrmProductSearchCount log : dataList) {

                    long productId = log.getProductId();

                    List<PtmCmpSku> skuList = dbm.query(Q_PTMCMPSKU_BYPRODUCTID, Arrays.asList(productId));

                    for (PtmCmpSku sku : skuList) {
                        //判断，如果该sku 当天更新过价格, 直接跳过
                        Date updateTime = sku.getUpdateTime();
                        if (updateTime != null) {
                            if (updateTime.compareTo(TimeUtils.toDate(TimeUtils.today())) > 0) {
                                continue;
                            }
                        }

                        //offsale的不再更新
                        if(SkuStatus.OFFSALE.equals(sku.getStatus())){
                            continue;
                        }

                        Website website = sku.getWebsite();

                        //暂时过滤掉myntra
                        if (Website.MYNTRA.equals(website)) {
                            continue;
                        }

                        //高优先级的网站
                        if (Website.SNAPDEAL.equals(website) || Website.FLIPKART.equals(website) || Website.AMAZON.equals(website)) {

                            //过滤掉snapdeal中viewAllSeller的情况
                            if (Website.SNAPDEAL.equals(website)) {
                                String url = sku.getUrl();
                                url = StringUtils.filterAndTrim(url, Arrays.asList("/viewAllSellers"));
                                sku.setUrl(url);
                            }
                            //过滤掉amazon中gp/offer-listing的url,该url没有描述等信息
                            if (Website.AMAZON.equals(website)) {
                                String url = sku.getUrl();
                                url = url.replace("gp/offer-listing", "dp");
                                sku.setUrl(url);
                            }

                            queue.add(sku);
                            fetchDubboService.sendUrlTask(sku.getWebsite(), sku.getUrl(), TaskLevel.LEVEL_2);
                        } else {
                            queue.add(sku);
                            fetchDubboService.sendUrlTask(sku.getWebsite(), sku.getUrl(), TaskLevel.LEVEL_5);
                        }

                        logger.info("send url request succes for " + sku.getWebsite() + " sku id is [" + sku.getId() + "]");
                    }
                }
            }

            page++;
        }

        logger.info("send url finish");
    }
}
