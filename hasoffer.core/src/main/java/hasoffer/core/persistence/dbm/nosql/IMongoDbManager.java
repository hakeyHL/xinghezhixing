package hasoffer.core.persistence.dbm.nosql;

import hasoffer.base.model.PageableResult;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * Created on 2016/1/4.
 */
public interface IMongoDbManager {

    <T> void save(T t);

    <T> long count(Class<T> clazz, Query query);

    <T> List<T> query(Class<T> clazz, Query query);

    <T> PageableResult<T> queryPage(Class<T> clazz, Query query, int page, int size);

    <T> List<T> query(Class<T> clazz, Query query, int page, int size);

    <T> T queryOne(Class<T> clazz);

    <T> int update(Class<T> clazz, Object id, Update update);

    <T> T queryOne(Class<T> clazz, Object id);

    <T> AggregationResults<T> aggregate(Class<T> clazz, TypedAggregation<T> agg);

    // table
    <T> boolean exists(Class<T> clazz, Object id);
}
