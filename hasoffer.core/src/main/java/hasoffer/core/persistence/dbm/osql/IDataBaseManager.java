package hasoffer.core.persistence.dbm.osql;

import hasoffer.base.model.PageableResult;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;

/**
 * Created by glx on 2015/5/20.
 */
public interface IDataBaseManager {
    <ID extends Serializable, T extends Identifiable<ID>> T get(Class<T> tClass, ID id);

    <ID extends Serializable, T extends Identifiable<ID>> ID create(T t);

    <T> int batchSave(final List<T> array);

    <ID extends Serializable, T extends Identifiable<ID>> void createIfNoExist(final T t);

    int batchDelete(String jpaSql, String[] ids);

    <T> T querySingle(String jpaSql);

    <T> T querySingle(String jpaSql, List params);

    <T> List<T> query(String jpaSql);

    <T> List<T> query(String jpaSql, List params);

    <T> List<T> query(String jpaSql, int pageNumber, int pageSize);

    <T> List<T> query(String jpaSql, int pageNumber, int pageSize, List params);

    <T> PageableResult<T> queryPage(String jpaSql, int pageNumber, int pageSize);

    <T> PageableResult<T> queryPage(String jpaSql, int pageNumber, int pageSize, List params);

    <ID extends Serializable, T extends Identifiable<ID>> void update(Updater<ID, T> updater);

    <ID extends Serializable, T extends Identifiable<ID>> void delete(Class<T> tClass, ID id);

    List queryByIds(String jpaSql, String[] ids);

    void update(final List<T> array);

    void deleteBySQL(final String sql);
}
