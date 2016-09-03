package hasoffer.core.persistence.dbm.osql;

import hasoffer.base.model.PageableResult;
import hasoffer.core.persistence.dbm.HibernateDao;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2016/4/11.
 */
@Component
public class HibernateDaoImpl implements HibernateDao {

    @Resource
    protected SessionFactory sf;

    private static final Pattern PLACE_HOLDER = Pattern.compile("\\?");
    private static final Pattern ORDER = Pattern.compile(" order +by +.*$", Pattern.CASE_INSENSITIVE);

    protected Query buildQuery(boolean nativeSql, String ql, Map<String, Object> values) {
        Query query = null;
        if (nativeSql) {
            query = cs().createSQLQuery(ql);
        } else {
            query = cs().createQuery(ql);
        }

        for (String name : values.keySet()) {
            setParameter(query, name, values.get(name));
        }
        return query;
    }

    protected Query buildQuery(boolean isSql, String ql, Object[] values) {
        int paramCnt = 0;
        if (values.length > 0) {// replace ? to :param{i}
            Matcher m = PLACE_HOLDER.matcher(ql);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, ":param" + paramCnt);
                paramCnt++;
            }
            m.appendTail(sb);
            if (paramCnt != values.length) {
                throw new HibernateException("Params' count is: " + paramCnt + " but expected to be: " + values.length);
            }
            ql = sb.toString();
        }

        Query query = isSql ? cs().createSQLQuery(ql) : cs().createQuery(ql);
        for (int i = 0; i < values.length; i++) {
            setParameter(query, "param" + i, values[i]);
        }
        return query;
    }

    @Override
    public SessionFactory sf() {
        return sf;
    }

    @Override
    public Session cs() {
        return sf.getCurrentSession();
    }


    @Override
    public int countBySql(String sql, Map<String, Object> params) {
        sql = ORDER.matcher(sql).replaceFirst("");
        sql = "Select Count(*) From (" + sql + ") t";
        Number nmb = this.findUniqueBySql(sql, params);
        return nmb.intValue();
    }

    @Override
    public int countBySql(String sql, Object... values) {
        sql = ORDER.matcher(sql).replaceFirst("");
        sql = "Select Count(*) From (" + sql + ") t";
        Number nmb = this.findUniqueBySql(sql, values);
        return nmb.intValue();
    }

    @Override
    public <T> List<T> find(String hql, Map<String, Object> params) {
        Query q = buildQuery(false, hql, params);
        @SuppressWarnings("unchecked")
        List<T> ret = q.list();
        return ret;
    }

    @Override
    public <T> List<T> find(String hql, Object... values) {
        Query q = buildQuery(false, hql, values);
        @SuppressWarnings("unchecked")
        List<T> ret = q.list();
        return ret;
    }

    @Override
    public <T> List<T> findAll(Class<T> clazz) {
        String name = sf.getClassMetadata(clazz).getEntityName();
        String hql = "SELECT m FROM " + name + " m ";
        return find(hql);
    }

    @Override
    public <T> List<T> findBySql(String sql, Map<String, Object> params) {
        Query q = buildQuery(true, sql, params);
        @SuppressWarnings("unchecked")
        List<T> ret = q.list();
        return ret;
    }

    @Override
    public <T> List<T> findBySql(String sql, Object... values) {
        Query q = buildQuery(true, sql, values);
        @SuppressWarnings("unchecked")
        List<T> ret = q.list();
        return ret;
    }

    @Override
    public List<Map<String, Object>> findMapBySql(String sql, Map<String, Object> params) {
        Query query = buildQuery(true, sql, params);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> ret = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return ret;
    }

    @Override
    public List<Map<String, Object>> findMapBySql(String sql, Object... values) {
        Query query = buildQuery(true, sql, values);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> ret = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return ret;
    }

    @Override
    public <T> T findUnique(String hql, Map<String, Object> params) {
        Query q = buildQuery(false, hql, params);
        @SuppressWarnings("unchecked")
        T ret = (T) q.uniqueResult();
        return ret;
    }

    @Override
    public <T> T findUnique(String hql, Object... values) {
        Query q = buildQuery(false, hql, values);
        @SuppressWarnings("unchecked")
        T ret = (T) q.uniqueResult();
        return ret;
    }

    @Override
    public <T> T findUniqueBySql(String sql, Map<String, Object> parmas) {
        @SuppressWarnings("unchecked")
        T ret = (T) buildQuery(true, sql, parmas).uniqueResult();
        return ret;
    }

    @Override
    public <T> T findUniqueBySql(String sql, Object... values) {
        @SuppressWarnings("unchecked")
        T ret = (T) buildQuery(true, sql, values).uniqueResult();
        return ret;
    }

    @Override
    public <T> PageableResult<T> findPageBySql(String sql, int page, int pageSize, Map<String, Object> params) {
        int totalCount = countBySql(sql, params);
        Query q = buildQuery(true, sql, params).setFirstResult((page - 1) * pageSize).setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<T> items = q.list();
        return new PageableResult<T>(items, totalCount, page, pageSize);
    }

    @Override
    public <T> PageableResult<T> findPageBySql(String sql, int page, int pageSize, Object... values) {
        int totalCount = countBySql(sql, values);
        Query q = buildQuery(true, sql, values).setFirstResult((page - 1) * pageSize).setMaxResults(pageSize);
        @SuppressWarnings("unchecked")
        List<T> items = q.list();
        return new PageableResult<T>(items, totalCount, page, pageSize);
    }

    @Override
    public PageableResult<Map<String, Object>> findPageOfMapBySql(String sql, int page, int pageSize, Map<String, Object> params) {
        int totalCount = countBySql(sql, params);
        Query q = buildQuery(true, sql, params).setFirstResult((page - 1) * pageSize).setMaxResults(pageSize)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = q.list();
        return new PageableResult<Map<String, Object>>(items, totalCount, page, pageSize);
    }

    @Override
    public PageableResult<Map<String, Object>> findPageOfMapBySql(String sql, int page, int pageSize, Object... values) {
        int totalCount = countBySql(sql, values);
        Query q = buildQuery(true, sql, values).setFirstResult((page - 1) * pageSize).setMaxResults(pageSize)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = q.list();
        return new PageableResult<Map<String, Object>>(items, totalCount, page, pageSize);
    }


    @Override
    public int update(String hql, Map<String, Object> namedVars) {
        Query q = buildQuery(false, hql, namedVars);
        return q.executeUpdate();
    }

    @Override
    public int update(String hql, Object... values) {
        Query q = buildQuery(false, hql, values);
        return q.executeUpdate();
    }

    @Override
    public int updateBySql(String sql, Map<String, Object> namedVars) {
        Query q = buildQuery(true, sql, namedVars);
        return q.executeUpdate();
    }

    @Override
    public int updateBySql(String sql, Object... values) {
        Query q = buildQuery(true, sql, values);
        return q.executeUpdate();
    }

    @Override
    public int deleteBySql(String sql, Object... values){
        Query q = buildQuery(true, sql, values);
        int result= q.executeUpdate();
        cs().flush(); //清理缓存，执行批量插入
        cs().clear();//清空缓存中的 对象
        return result;
    }

    @Override
    public void save(Object e)
    {
        this.sf.getCurrentSession().saveOrUpdate(e);
    }

    /**
     * Binding named varaible. if value is a array/collection type, binding as collection parameter.
     *
     * @param query
     * @param name
     * @param value
     */
    protected void setParameter(Query query, String name, Object value) {
        if (value instanceof Collection) {
            query.setParameterList(name, (Collection<?>) value);
        } else if (value instanceof Object[]) {
            query.setParameterList(name, (Object[]) value);
        } else {
            query.setParameter(name, value);
        }
    }
}
