package hasoffer.core.persistence.dbm;

import hasoffer.base.model.PageableResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Map;

/**
 * Created on 2016/4/11.
 */
public interface HibernateDao {

    /** Get holded {@link SessionFactory} */
    SessionFactory sf();

    /**
     * Get current {@link Session} by invoke
     * {@link SessionFactory#getCurrentSession()}, Just for convenience.
     */
    Session cs();

    /**
     * Rebuild the sql to a 'count' form, and execute it to exmain the rows' count that <code>sql</code> actually will
     * fetched
     *
     * @param sql
     *            a 'select' form (native) sql
     * @param namedVars
     *            named binding varables.
     * @return rows' count that <code>sql</code> actually will fetched
     */
    int countBySql(String sql, Map<String, Object> namedVars);

    /**
     * Rebuild the sql to a 'count' form, and execute it to exmain the rows' count that <code>sql</code> actually will
     * fetched
     *
     * @param sql
     *            a 'select' form (native) sql
     * @param vars
     *            binding varables.
     * @return rows' count that <code>sql</code> actually will fetched
     */
    int countBySql(String sql, Object... vars);

    /**
     * Find entities by execute a 'select' ql.
     *
     * @param ql
     *            the underlying orm ql. maybe hql, jpql and so forth
     * @param namedVars
     *            named binding varables.
     * @return a list of entities.
     */
    <T> List<T> find(String ql, Map<String, Object> namedVars);

    /**
     * Find entities by execute a 'select' ql.
     *
     * @param ql
     *            the underlying orm ql. maybe hql, jpql and so forth
     * @param vars
     *            binding varables.
     * @return a list of entities.
     */
    <T> List<T> find(String ql, Object... vars);

    /**
     * Find all entities(mapped by <code>clazz</code>) by execute a 'select' ql.
     *
     * @param clazz
     *            mapped entity class
     * @return a list of entities.
     */
    <T> List<T> findAll(Class<T> clazz);

    /**
     * Find entities by execute a 'select' sql.
     *
     * @param sql
     *            native sql
     * @param namedVars
     *            named binding varables.
     * @return a list of entities. element type will be array if multiple columns selected.
     */
    <T> List<T> findBySql(String sql, Map<String, Object> namedVars);

    /**
     * Find entities by execute a 'select' sql.
     *
     * @param sql
     *            native sql
     * @param vars
     *            binding varables.
     * @return a list of entities. element type will be array if multiple columns selected.
     */
    <T> List<T> findBySql(String sql, Object... vars);

    /**
     * Find records by execute a 'select' sql, Each row of results is a Map from alias to values/entities.
     *
     * @param sql
     *            native sql
     * @param namedVars
     *            named binding varables.
     * @return a list of Maps. the keys of map will be <b>returned alias</b>.
     */
    List<Map<String, Object>> findMapBySql(String sql, Map<String, Object> namedVars);

    /**
     * Find records by execute a 'select' sql, Each row of results is a Map from alias to values/entities.
     *
     * @param sql
     *            native sql
     * @param vars
     *            binding varables.
     * @return a list of Maps. the keys of map will be <b>returned alias</b>.
     */
    List<Map<String, Object>> findMapBySql(String sql, Object... vars);

    /**
     * Find a page of objects by <code>sql</code>.
     *
     * @param sql
     *            native sql
     * @param page
     *            page index, from 1.
     * @param pageSize
     *            max size per page.
     * @param namedVars
     *            named binding varables.
     * @return a page of objects, element type T will be array if multiple columns selected.
     */
    <T> PageableResult<T> findPageBySql(String sql, int page, int pageSize, Map<String, Object> namedVars);

    /**
     * Find a page of objects by <code>sql</code>.
     *
     * @param sql
     *            native sql
     * @param page
     *            page index, from 1.
     * @param pageSize
     *            max size per page.
     * @param vars
     *            binding varables.
     * @return a page of objects, element type T will be array if multiple columns selected.
     */
    <T> PageableResult<T> findPageBySql(String sql, int page, int pageSize, Object... vars);

    /**
     * Find a page of objects by <code>sql</code>.
     *
     * @param sql
     *            native sql
     * @param page
     *            page index, from 1.
     * @param pageSize
     *            max size per page.
     * @param namedVars
     *            named binding varables.
     * @return a page of <code>Map</code>s, the keys of <code>Map</code> will be <b>returned alias</b>.
     */
    PageableResult<Map<String, Object>> findPageOfMapBySql(String sql, int page, int pageSize, Map<String, Object> namedVars);

    /**
     * Find a page of objects by <code>sql</code>.
     *
     * @param sql
     *            native sql
     * @param page
     *            page index, from 1.
     * @param pageSize
     *            max size per page.
     * @param vars
     *            binding varables
     * @return a page of <code>Map</code>s, the keys of <code>Map</code> will be <b>returned alias</b>.
     */
    PageableResult<Map<String, Object>> findPageOfMapBySql(String sql, int page, int pageSize, Object... vars);

    /**
     *
     * @param hql
     *            the underlying orm ql. maybe hql, jpql and so forth
     * @param namedVars
     *            named binding variables
     * @return a single entity
     * @throws RuntimeException
     *             if ql return more than one result.
     */
    <T> T findUnique(String hql, Map<String, Object> namedVars);

    /**
     *
     * @param ql
     *            the underlying orm ql. maybe hql, jpql and so forth
     * @param vars
     *            binding variables
     * @return a single entity
     * @throws RuntimeException
     *             if ql return more than one result.
     */
    <T> T findUnique(String ql, Object... vars);

    /**
     * Find a unique result.
     *
     * @param sql
     *            native sql
     * @param namedVars
     *            named binding variables
     * @return a single result, T will be array if multiple columns selected.
     * @throws RuntimeException
     *             if sql return more than one result.
     */
    <T> T findUniqueBySql(String sql, Map<String, Object> namedVars);

    /**
     * Find a unique result.
     *
     * @param sql
     *            native sql
     * @param vars
     *            binding variables
     * @return a single result, T will be array if multiple columns selected.
     * @throws RuntimeException
     *             if sql return more than one result.
     */
    <T> T findUniqueBySql(String sql, Object... vars);


    /**
     * Execute a 'update' <code>ql</code>.
     *
     * @param ql
     *            the underlying orm ql. maybe hql, jpql and so forth
     * @param namedVars
     *            named binding variables
     * @return affected rows' count
     */
    int update(String ql, Map<String, Object> namedVars);

    /**
     * Execute a 'update' <code>ql</code>.
     *
     * @param ql
     *            the underlying orm ql. maybe hql, jpql and so forth
     * @param vars
     *            binding variables
     * @return affected rows' count
     */
    int update(String ql, Object... vars);

    /**
     * Execute a 'update' <code>sql</code>.
     *
     * @param sql
     *            native sql
     * @param namedVars
     *            named binding variables
     * @return affected rows' count
     */
    int updateBySql(String sql, Map<String, Object> namedVars);

    /**
     * Execute a 'update' <code>sql</code>.
     *
     * @param sql
     *            native sql
     * @param vars
     *            binding varables
     * @return affected rows' count
     *
     *
     *
     */
    int updateBySql(String sql, Object... vars);

    int deleteBySql(String sql, Object... values);

    void save(Object paramObject);
}
