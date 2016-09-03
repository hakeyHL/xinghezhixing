package hasoffer.core.redis;

import java.util.Map;
import java.util.Set;

/**
 * Date : 2016/5/3
 * Function :
 */
public interface ICacheService<T> {

    T get(Class<T> clazz, String key, long seconds);

    boolean add(String key, T t, long seconds);

    String get(String key, long seconds);

    boolean add(String key, String value, long seconds);

    void del(String key);

    boolean update(String key, T t);

    boolean mapPut(final String mName, final String key, final String value);

    String mapGet(final String mName, final String key);

    boolean expire(String key, long seconds);

    boolean exists(String key);

    Map<String, String> mapGetAll(final String mName);

    Set<String> keys(final String pattern);
}
