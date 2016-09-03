package hasoffer.core.persistence.dbm.mongo;

import com.mongodb.WriteResult;
import hasoffer.base.model.PageableResult;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created on 2016/1/4.
 */
@Component
public class MongoDbManager implements IMongoDbManager {

    @Resource
    MongoTemplate mongoTemplate;

    @Override
    public <T> T queryOne(Class<T> clazz) {
        return mongoTemplate.findOne(new Query(), clazz);
    }

    public <T> void save(T t) {
        mongoTemplate.save(t);
    }

    private void pageQuery(Query query, int page, int size) {
        int mongoPage = page - 1;
        // mongo db 的 首页 是从0开始的
        if (mongoPage < 0) {
            mongoPage = 0;
            page = 1;
        }
        query.with(new QPageRequest(mongoPage, size));
    }

    public <T> boolean exists(Class<T> clazz, Object id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.exists(query, clazz);
    }

    @Override
    public <T> PageableResult<T> queryPage(Class<T> clazz, Query query, int page, int size) {
        pageQuery(query, page, size);
        long count = mongoTemplate.count(query, clazz);
        List<T> datas = mongoTemplate.find(query, clazz);

        return new PageableResult<T>(datas, count, page, size);
    }

    @Override
    public <T> List<T> query(Class<T> clazz, Query query, int page, int size) {
        pageQuery(query, page, size);

        return mongoTemplate.find(query, clazz);
    }

    @Override
    public <T> long count(Class<T> clazz, Query query) {
        return mongoTemplate.count(query, clazz);
    }

    @Override
    public <T> List<T> query(Class<T> clazz, Query query) {
        return mongoTemplate.find(query, clazz);
    }

    @Override
    public <T> int update(Class<T> clazz, Object id, Update update) {
        Query query = new Query(Criteria.where("id").is(id));
        WriteResult wr = mongoTemplate.updateMulti(query, update, clazz);
        return wr.getN();
    }

    @Override
    public <T> T queryOne(Class<T> clazz, Object id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, clazz);
    }

    @Override
    public <T> AggregationResults<T> aggregate(Class<T> clazz, TypedAggregation<T> agg) {
        return mongoTemplate.aggregate(agg, clazz);
    }
}
