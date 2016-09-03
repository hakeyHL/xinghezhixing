package hasoffer.admin.controller;

import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created on 2016/8/3.
 * 检查部分任务的运行状态
 */
@Controller
@RequestMapping(value = "/check")
public class CheckStatusController {

    private static final AtomicBoolean taskRunning1 = new AtomicBoolean(false);

    @Resource
    IDataBaseManager dbm;

    //check/skuupdate
    @RequestMapping(value = "/skuupdate")
    @ResponseBody
    public String skuupdate() {

        if (taskRunning1.get()) {
            return "task running";
        }

        String todayStartString = TimeUtils.parse(TimeUtils.today(), "yyyy-MM-dd HH:mm:ss");

        //大概14秒,100kb左右的数据量
        List<Long> productIdList = dbm.query("SELECT distinct t.ptmProductId FROM SrmSearchLog t WHERE t.updateTime > ?0", Arrays.asList(TimeUtils.toDate(TimeUtils.today())));

        long updateSuccess = 0;
        long needUpdate = 0;

        for (Long productid : productIdList) {

            long needUpdateNumber = dbm.querySingle("SELECT count(*) FROM PtmCmpSku t WHERE t.productId = ?0 ", Arrays.asList(productid));
            long updateSuccessNumber = dbm.querySingle("SELECT count(*) FROM PtmCmpSku t WHERE t.productId = ?0 AND t.updateTime > ?1", Arrays.asList(productid, TimeUtils.add(TimeUtils.toDate(TimeUtils.today()), -1)));

            updateSuccess += updateSuccessNumber;
            needUpdate += needUpdateNumber;

        }

        System.out.println("needUpdate = " + needUpdate);
        System.out.println("updateSuccess = " + updateSuccess);

        taskRunning1.set(true);

        return "OK";
    }

}
