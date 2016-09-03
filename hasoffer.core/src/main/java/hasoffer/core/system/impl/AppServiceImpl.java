package hasoffer.core.system.impl;

import hasoffer.base.enums.AppType;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.PageableResult;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.core.bo.system.SearchCriteria;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.admin.OrderStatsAnalysisPO;
import hasoffer.core.persistence.po.app.AppBanner;
import hasoffer.core.persistence.po.app.AppDeal;
import hasoffer.core.persistence.po.app.AppVersion;
import hasoffer.core.persistence.po.app.AppWebsite;
import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.core.persistence.po.urm.UrmUser;
import hasoffer.core.persistence.po.urm.UrmUserDevice;
import hasoffer.core.system.IAppService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created on 2015/12/30.
 */
@Service
public class AppServiceImpl implements IAppService {

    private static final String Q_APP_VERSION =
            "SELECT t FROM AppVersion t " +
                    " WHERE t.appType = ?0 and marketChannel != 'ZUK'" +
                    " ORDER BY t.publishTime DESC";
    private static final String Q_CHANNEL_APP_VERSION =
            "SELECT t FROM AppVersion t " +
                    " WHERE t.appType = ?0  and marketChannel = ?1" +
                    " ORDER BY t.publishTime DESC";

    private static final String Q_APP_WEBSITE =
            "SELECT t FROM AppWebsite t " +
                    " WHERE t.appshow = ?0 ";

    private static final String Q_APP_ORDERS =
            "SELECT t FROM OrderStatsAnalysisPO t " +
                    " WHERE t.userId = ?0 ";

    private static final String Q_APP_GETUSER =
            "SELECT t FROM UrmUser t " +
                    " WHERE t.userToken = ?0 ";

    private static final String Q_APP_ORDER =
            "SELECT t FROM OrderStatsAnalysisPO t " +
                    " WHERE t.orderId = ?0 and t.userId=?1";

    private static final String Q_APP_CATEGORY =
            "SELECT t FROM PtmCategory t where t.parentId=0 " +
                    " order by level ASC,rank ASC";

    private static final String Q_APP_GETUSERBYTHIRDID =
            "SELECT t FROM UrmUser t " +
                    " where t.thirdId=?0";

    private static final String Q_APP_URMDEVICE_GETIDSBYDEVICEID =
            "SELECT t.id FROM UrmDevice t " +
                    " where t.deviceId=?0";

    private static final String Q_APP_URMUSERDEVICE_GETIDSBYUSERID =
            "SELECT t.deviceId FROM UrmUserDevice t " +
                    " where t.userId=?0";

    private static final String Q_APP_GETDEALS =
            "SELECT t FROM AppDeal t where  t.display='1' and  t.expireTime >= ?0  order by id desc   ";
    private static final String Q_APP_GETBANNERS =
            " SELECT t from AppBanner t where t.deadline >=?0 ORDER BY t.id desc";

    private static final String Q_APP_GEDEALDETAIL =
            " SELECT t from AppDeal t where t.id=?0";

    private static final String Q_APP_GETCHILDCATEGORY =
            "SELECT t FROM PtmCategory t where t.parentId=?0 and level<=3 " +
                    " order by level ASC,rank ASC";

    private static final String Q_APP_CATEGORY_ISHASCHILDNODE =
            "SELECT t FROM PtmCategory t where t.parentId=?0 ";

    private static final String Q_APP_CATEGORY_BYID =
            "SELECT t FROM PtmCategory t where t.id=?0 ";
    @Resource
    IDataBaseManager dbm;
    private String Q_APP_GETPRODUCTS =
            "SELECT t FROM PtmProduct t " +
                    " where 1=1 and ";

    @Override
    public AppVersion getLatestVersion(AppType appType) {
//        return dbm.get(AppVersion.class, 3L);
        List<AppVersion> versions = dbm.query(Q_APP_VERSION, Arrays.asList(appType));

        return ArrayUtils.hasObjs(versions) ? versions.get(0) : null;
    }

    @Override
    public AppVersion getLatestVersion(MarketChannel marketChannel, AppType appType) {
        List<AppVersion> versions = dbm.query(Q_CHANNEL_APP_VERSION, Arrays.asList(appType, marketChannel));
        return ArrayUtils.hasObjs(versions) ? versions.get(0) : null;
    }

    @Override
    public List<AppWebsite> getWebsites(boolean appshow) {
        return dbm.query(Q_APP_WEBSITE, Arrays.asList(appshow));
    }

    @Override
    public List<OrderStatsAnalysisPO> getBackDetails(String userToken) {
        List li = new ArrayList();
        li.add(userToken);
        return dbm.query(Q_APP_ORDERS, li);
    }

    @Override
    public UrmUser getUserByUserToken(String userToken) {
        List li = new ArrayList();
        li.add(userToken);
        UrmUser user = dbm.querySingle(Q_APP_GETUSER, li);
        return user;
    }

    @Override
    public OrderStatsAnalysisPO getOrderDetail(String orderId, String userId) {
        List li = new ArrayList();
        li.add(orderId);
        li.add(userId);
        return dbm.querySingle(Q_APP_ORDER, li);
    }

    @Override
    public PageableResult getDeals(Long page, Long pageSize) {
        return dbm.queryPage(Q_APP_GETDEALS, page.intValue() <= 1 ? 1 : page.intValue(), pageSize.intValue(), Arrays.asList(new Date()));
    }

    @Override
    public List<PtmCategory> getCategory() {
        return dbm.query(Q_APP_CATEGORY);
    }

    @Override
    public AppDeal getDealDetail(String id) {
        List li = new ArrayList();
        li.add(Long.valueOf(id));
        return dbm.querySingle(Q_APP_GEDEALDETAIL, li);
    }

    @Override
    public UrmUser getUserById(String thirdId) {
        List li = Arrays.asList(thirdId);
        List<UrmUser> urmUsers = dbm.query(Q_APP_GETUSERBYTHIRDID, li);
        if (urmUsers != null && urmUsers.size() > 0) {
            return urmUsers.get(0);
        }
        return null;
    }

    @Override
    public List getProductByCriteria(SearchCriteria criteria) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        String categoryId = criteria.getCategoryId();
        if (StringUtils.isNotBlank(categoryId)) {
            sb.append(" categoryId = ?" + i + "");
            i++;
        }
        int comment = criteria.getComment();
        if (comment == 0) {
            sb.append(" order by comment desc ");
        } else {
            sb.append(" order by comment asc ");
        }
        String keyword = criteria.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            sb.append(" title like %" + i + "%");
            i++;
        }
        Long maxPrice = criteria.getMaxPrice();
        Long minPrice = criteria.getMinPrice();

        int page = criteria.getPage();
        int pageSize = criteria.getPageSize();


        Q_APP_GETPRODUCTS = Q_APP_GETPRODUCTS + "ee";
        return null;
    }

    @Override
    public List<String> getUserDevicesByUserId(String userId) {
        return dbm.query(Q_APP_URMUSERDEVICE_GETIDSBYUSERID, Arrays.asList(userId));
    }

    @Override
    @Transactional
    public int addUrmUserDevice(List<UrmUserDevice> urmUserDevices) {
        return dbm.batchSave(urmUserDevices);
    }

    @Override
    public int addUser(UrmUser user) {
        List li = new ArrayList();
        li.add(user);
        return dbm.batchSave(li);
    }

    @Override
    public List<String> getUserDevices(String deviceId) {
        //有的用户绑定设备列表
        return dbm.query(Q_APP_URMDEVICE_GETIDSBYDEVICEID, Arrays.asList(deviceId));
    }

    @Override
    public void updateUserInfo(UrmUser uUser) {
        List li = new ArrayList();
        li.add(uUser);
        dbm.update(li);
    }

    @Override
    public List<AppBanner> getBanners() {
        return dbm.query(Q_APP_GETBANNERS, Arrays.asList(new Date()));
    }

    @Override
    public List<PtmCategory> getChildCategorys(String categoryId) {
        List li = new ArrayList();
        li.add(Long.valueOf(categoryId));
        return dbm.query(Q_APP_GETCHILDCATEGORY, li);
    }

    @Override
    public int isHasChildNode(Long id) {
        List li = new ArrayList();
        li.add(id);
        List<PtmCategory> category = (List) dbm.query(Q_APP_CATEGORY_ISHASCHILDNODE, li);
        if (category == null || category.size() < 1) {
            return 0;
        }
        return 1;
    }

    @Override
    public void countDealClickCount(AppDeal appDeal) {
        appDeal.setDealClickCount(appDeal.getDealClickCount() + 1);
        List deals = Arrays.asList(appDeal);
        dbm.update(deals);
    }

    public PtmCategory getCategoryInfo(Long cateId) {
        List<PtmCategory> query = dbm.query(Q_APP_CATEGORY_BYID, Arrays.asList(cateId));
        if (query != null && query.size() > 0) {
            return query.get(0);
        }
        return null;
    }
}
