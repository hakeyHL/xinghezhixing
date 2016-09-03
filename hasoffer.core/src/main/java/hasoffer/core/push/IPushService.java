package hasoffer.core.push;

import hasoffer.base.enums.MarketChannel;
import hasoffer.core.bo.push.AppPushBo;
import hasoffer.core.persistence.po.urm.UrmDevice;

import java.util.List;

/**
 * Date : 2016/4/27
 * Function :
 */
public interface IPushService {

    String push(String to, AppPushBo pushBo);

    List<UrmDevice> getGcmTokens(String version);

    void sendPush(int page, int size);

    List<MarketChannel> getAllMarketChannels();

    List<String> getAllAppVersions();
}
