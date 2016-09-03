package hasoffer.core.third.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import hasoffer.base.model.Website;
import hasoffer.core.cache.ProductCacheManager;
import hasoffer.core.persistence.dbm.Hibernate4DataBaseManager;
import hasoffer.core.persistence.po.app.AppDeal;
import hasoffer.core.product.impl.CmpSkuServiceImpl;
import hasoffer.core.third.ThirdService;
import hasoffer.core.utils.ImageUtil;
import hasoffer.core.utils.JsonHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by hs on 2016/7/4.
 */
@Service
public class ThirdServiceImple implements ThirdService {
    private static String THIRD_GMOBI_DEALS = "SELECT t from AppDeal t where t.createTime <=?0  and t.expireTime >= ?1  ";
    @Resource
    Hibernate4DataBaseManager hdm;
    @Resource
    ProductCacheManager productCacheManager;
    @Resource
    CmpSkuServiceImpl cmpSkuService;
    Logger logger = LoggerFactory.getLogger(ThirdServiceImple.class);

    @Override
    public String getDeals(String acceptJson) {
        JSONObject resJson = new JSONObject();
        StringBuilder sb = new StringBuilder();
        sb.append(THIRD_GMOBI_DEALS);
        if (StringUtils.isEmpty(acceptJson)) {
            logger.error(String.format("json parseException , %s is not a json String", acceptJson));
            resJson.put("errorCode", "10001");
            resJson.put("msg", "you should send a json String ,start with '{' and end with '}' ");
            return resJson.toJSONString();
        }
        JSONObject jsonObject = JSONObject.parseObject(acceptJson);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date createTime = new Date();
        try {
            if (StringUtils.isNotEmpty(jsonObject.getString("createTime"))) {
                createTime = sf.parse(jsonObject.getString("createTime"));
            }
        } catch (ParseException e) {
            logger.error("dataFormat  " + jsonObject.getString("createTime") + " to format yyyyMMddHHmmss failed ");
            resJson.put("errorCode", "10001");
            resJson.put("msg", "can't parse your createTime " + jsonObject.getString("createTime") + "  , because it is not the pattern as yyyyMMddHHmmss ");
            return resJson.toJSONString();
        }
        JSONArray sites = null;
        try {
            sites = jsonObject.getJSONArray("sites");
        } catch (Exception e) {
            logger.error(" sites is not a JsonArray String ");
            resJson.put("errorCode", "10001");
            resJson.put("msg", "required a Array like [\"a\",\"b\"] ");
            return resJson.toJSONString();
        }
        List dataList = new ArrayList();
        if (sites != null) {
            logger.error("has sites");
            sb.append(" and t.website=?2 ");
            sb.append(" order by createTime desc  ");
            for (int i = 0; i < sites.size(); i++) {
                List li = new ArrayList();
                Website website = Website.valueOf((String) sites.get(i));
                li.add(createTime);
                li.add(new Date());
                li.add(website);
                List<AppDeal> deals = hdm.query(sb.toString(), li);
                if (deals != null && deals.size() > 0) {
                    dataList.addAll(deals);
                }
            }
        } else {
            logger.error("no sites");
            sb.append(" order by createTime desc  ");
            List<AppDeal> deals = hdm.query(sb.toString(), Arrays.asList(createTime, new Date()));
            if (deals != null && deals.size() > 0) {
                dataList.addAll(deals);
            }
        }
        PropertyFilter propertyFilter = JsonHelper.filterProperty(new String[]{"push", "display"});
        for (AppDeal appDeal : (List<AppDeal>) dataList) {
            appDeal.setImageUrl(ImageUtil.getImageUrl(appDeal.getImageUrl()));
        }
        resJson.put("deals", dataList);
        resJson.put("errorCode", "00000");
        resJson.put("msg", "ok");
        return JSON.toJSONString(resJson, propertyFilter);
    }


}
