package hasoffer.core.system;

import hasoffer.base.enums.AppType;
import hasoffer.base.enums.MarketChannel;
import hasoffer.base.model.PageableResult;
import hasoffer.core.bo.system.SearchCriteria;
import hasoffer.core.persistence.po.admin.OrderStatsAnalysisPO;
import hasoffer.core.persistence.po.app.AppBanner;
import hasoffer.core.persistence.po.app.AppDeal;
import hasoffer.core.persistence.po.app.AppVersion;
import hasoffer.core.persistence.po.app.AppWebsite;
import hasoffer.core.persistence.po.ptm.PtmCategory;
import hasoffer.core.persistence.po.urm.UrmUser;
import hasoffer.core.persistence.po.urm.UrmUserDevice;

import java.util.List;

/**
 * Created on 2015/12/30.
 */
public interface IAppService {

    AppVersion getLatestVersion(AppType appType);

    AppVersion getLatestVersion(MarketChannel marketChannel, AppType appType);

    List<AppWebsite> getWebsites(boolean appshow);

    List<OrderStatsAnalysisPO> getBackDetails(String userId);

    UrmUser getUserByUserToken(String userToken);

    OrderStatsAnalysisPO getOrderDetail(String orderId, String userId);

    PageableResult getDeals(Long page, Long pageSize);

    List<PtmCategory> getCategory();

    AppDeal getDealDetail(String id);

    UrmUser getUserById(String thirdId);

    List getProductByCriteria(SearchCriteria criteria);

    List<String> getUserDevicesByUserId(String userId);

    int addUrmUserDevice(List<UrmUserDevice> urmUserDevices);

    int addUser(UrmUser urmUser);

    List<String> getUserDevices(String deviceId);

    void updateUserInfo(UrmUser uUser);

    List<AppBanner> getBanners();

    List<PtmCategory> getChildCategorys(String categoryId);

    int isHasChildNode(Long id);

    void countDealClickCount(AppDeal appDeal);
}
