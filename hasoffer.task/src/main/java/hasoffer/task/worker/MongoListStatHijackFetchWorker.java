package hasoffer.task.worker;

import hasoffer.base.utils.ArrayUtils;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.mongo.StatHijackFetch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2016/6/1.
 */
public class MongoListStatHijackFetchWorker implements Runnable {

    private Logger logger = LoggerFactory.getLogger(MongoListStatHijackFetchWorker.class);

    private ConcurrentLinkedQueue<StatHijackFetch> queue;
    private IMongoDbManager mdm;

    public MongoListStatHijackFetchWorker(ConcurrentLinkedQueue<StatHijackFetch> queue, IMongoDbManager mdm) {
        this.queue = queue;
        this.mdm = mdm;
    }

    @Override
    public void run() {

        long lStartTime = TimeUtils.today();

        while (true) {

            Query query = new Query();
            query.addCriteria(Criteria.where("lCreateTime").gt(lStartTime));
            query.addCriteria(Criteria.where("status").is("NO_INDEX"));
            query.limit(1000);
            query.with(new Sort(Sort.Direction.ASC, "lCreateTime"));

            List<StatHijackFetch> statList = mdm.query(StatHijackFetch.class, query);

            if (ArrayUtils.hasObjs(statList)) {

                lStartTime = statList.get(statList.size() - 1).getlCreateTime();
                queue.addAll(statList);

            } else {
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (queue.size() > 6000) {
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
