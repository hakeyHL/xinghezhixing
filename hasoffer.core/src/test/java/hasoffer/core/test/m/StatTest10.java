package hasoffer.core.test.m;

import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.mongo.UrmDeviceRequestLog;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2016/4/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class StatTest10 {

    Pattern pattern1 = Pattern.compile(".*(action=shop)$");
    Pattern pattern2 = Pattern.compile(".*(action=shop)&.*");

    @Resource
    IMongoDbManager mdm;

    @Test
    public void testSnapdealShopping() throws ParseException {

        int countSkuidIs0 = 0;
        int countSkuidNot0 = 0;

        Query query = new Query();

        query.addCriteria(Criteria.where("createTime").gte(TimeUtils.parse("2016-04-19")).lt(TimeUtils.parse("2016-04-20")));
        query.addCriteria(Criteria.where("curShopApp").is("SNAPDEAL"));
        query.with(new Sort(Sort.Direction.DESC, "id"));

        int currentPage = 1;
        int pageSize = 100;

        PageableResult<UrmDeviceRequestLog> pageableResult = mdm.queryPage(UrmDeviceRequestLog.class, query, currentPage, pageSize);

        long numFund = pageableResult.getNumFund();

        List<UrmDeviceRequestLog> urmDeviceRequestLogList = pageableResult.getData();

        while (currentPage <= numFund) {

            for (UrmDeviceRequestLog urmDeviceRequestLog : urmDeviceRequestLogList) {

//                if(urmDeviceRequestLog.getCreateTime().getTime()> TimeUtils.parse("2016-04-20").getTime()){
//                    continue;
//                }
//
//                if(urmDeviceRequestLog.getCreateTime().getTime()< TimeUtils.parse("2016-04-19").getTime()){
//                    continue;
//                }
//
//                if(!"SNAPDEAL".equals(urmDeviceRequestLog.getCurShopApp())){
//                    continue;
//                }

                String queryString = urmDeviceRequestLog.getQuery();
                if (StringUtils.isEmpty(queryString)) {
                    continue;
                } else {

                    Matcher matcher1 = pattern1.matcher(queryString);
                    Matcher matcher2 = pattern2.matcher(queryString);
                    boolean b1 = matcher1.matches();
                    boolean b2 = matcher2.matches();

                    if (!b1 && !b2) {
                        continue;
                    }

                    String[] params = queryString.split("&");

                    if (params.length != 2) {
                        continue;
                    }

                    String value = "";
                    for (int i = 0; i < 2; i++) {
                        if (params[i].contains("value=")) {
                            value = params[i].replace("value=", "");
                        }
                    }

                    if (StringUtils.isEmpty(value)) {
                        continue;
                    }

                    // value 两种情况： proId_website | skuId
                    long proId = 0L;
                    Website website = null;

                    int _index = value.indexOf("_");
                    if (_index > 0) {
                        String proIdStr = value.substring(0, _index);
                        if (NumberUtils.isDigits(proIdStr)) {
                            proId = Long.valueOf(proIdStr);
                        }
                        String websiteString = value.substring(_index + 1).toUpperCase();
                        if (!"SNAPDEAL".equals(websiteString)) {
                            continue;
                        }
                    } else {
                        continue;
                    }

                    System.out.println("id:"+urmDeviceRequestLog.getId());
                    System.out.println("query:"+urmDeviceRequestLog.getQuery());

                    if (proId <= 0) {
                        countSkuidIs0++;
                    } else {
                        countSkuidNot0++;
                    }
                }
            }

            currentPage++;

            pageableResult = mdm.queryPage(UrmDeviceRequestLog.class, query, currentPage, pageSize);

            urmDeviceRequestLogList.addAll(pageableResult.getData());
        }


        System.out.println("from snapdeal data is right has " + urmDeviceRequestLogList.size());


        System.out.print("is 0 =" + countSkuidIs0);
        System.out.print("not 0 =" + countSkuidNot0);
    }

}
