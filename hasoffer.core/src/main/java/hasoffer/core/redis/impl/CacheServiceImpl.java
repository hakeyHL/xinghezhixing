package hasoffer.core.redis.impl;

import hasoffer.base.utils.JSONUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.dbm.osql.Identifiable;
import hasoffer.core.redis.ICacheService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Date : 2016/5/3
 * Function :
 */
@Component
public class CacheServiceImpl<T extends Identifiable> implements ICacheService<T> {

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public boolean expire(final String key, final long seconds) {
//        return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        return (Boolean) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.expire(key.getBytes(), seconds);
                return true;
            }
        });
    }

    @Override
    public String mapGet(final String mName, final String key) {
        return (String) redisTemplate.execute(new RedisCallback() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] vs = redisConnection.hGet(mName.getBytes(), key.getBytes());
                if (vs == null) {
                    return null;
                }
                return new String(vs);
            }
        });
    }

    @Override
    public Map<String, String> mapGetAll(final String mName) {
        Map<String, String> map = new HashMap<String, String>();
        Map<byte[], byte[]> mapBytes = (Map<byte[], byte[]>) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.hGetAll(mName.getBytes());
            }
        });

        if (mapBytes != null && mapBytes.size() > 0) {
            for (Map.Entry<byte[], byte[]> kv : mapBytes.entrySet()) {
                map.put(new String(kv.getKey()), new String(kv.getValue()));
            }
        }

        return map;
    }

    @Override
    public Set<String> keys(final String pattern) {

        return (Set<String>) redisTemplate.execute(new RedisCallback() {
            @Override
            public Set<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Set<byte[]> keys = redisConnection.keys(pattern.getBytes());

                Set<String> keySet = new HashSet<String>();

                for (byte[] array : keys) {

                    String key = new String(array);

                    keySet.add(key);
                }
                return keySet;
            }
        });
    }

    @Override
    public boolean mapPut(final String mName, final String key, final String value) {
        return (Boolean) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.hSet(mName.getBytes(), key.getBytes(), value.getBytes());
                return true;
            }
        });
    }

    @Override
    public String get(final String key, final long seconds) {
        return (String) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] values = redisConnection.get(key.getBytes());
                if (values == null) {
                    return null;
                }

                if (seconds > 0) {
                    redisConnection.expire(key.getBytes(), seconds);
                }

                return new String(values);
            }
        });
    }

    @Override
    public boolean add(final String key, final String value, final long seconds) {
        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set(key.getBytes(), value.getBytes());

                if (seconds > 0) {
                    redisConnection.expire(key.getBytes(), seconds);
                }

                return true;
            }
        });
    }

    @Override
    public T get(final Class<T> clazz, final String key, final long seconds) {
        String objJson = get(key, seconds);

        if (StringUtils.isEmpty(objJson)) {
            return null;
        }

        try {
            return (T) JSONUtil.toObject(objJson, (Class) clazz);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean add(String key, final T t, long seconds) {
        try {
            String value = JSONUtil.toJSON(t);
            add(key, value, seconds);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void del(final String key) {
//        redisTemplate.delete(key);
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.del(key.getBytes());
                return null;
            }
        });
    }

    @Override
    public boolean exists(final String key) {
        return (Boolean) redisTemplate.execute(new RedisCallback() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(key.getBytes());
            }
        });
    }

    @Override
    public boolean update(final String key, T t) {
        return false;
    }
}
