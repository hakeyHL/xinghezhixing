package hasoffer.core.admin;

import hasoffer.core.persistence.mongo.StatHijackFetchCount;

/**
 * Created on 2016/6/7.
 */
public interface IHijackFetchService {

    StatHijackFetchCount findStatHijackCount(String id);

}
