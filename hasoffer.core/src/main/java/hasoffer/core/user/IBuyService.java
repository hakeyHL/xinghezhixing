package hasoffer.core.user;

import hasoffer.base.model.PageableResult;
import hasoffer.core.persistence.po.stat.StatUserBuy;

import java.util.List;

/**
 * Created on 2016/4/11.
 */
public interface IBuyService {

    StatUserBuy getStatUerBuyById(long id);

    void updateStatUserBuy(long id, StatUserBuy statUserBuy);

    PageableResult<StatUserBuy> listStatUserBuy(int page, int size);

    StatUserBuy createStatUserBuy(StatUserBuy statUserBuy);

}
