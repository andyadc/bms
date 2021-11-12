package com.andyadc.bms.cache.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked"})
@Component
public class RedisOperator {

    private static final Logger logger = LoggerFactory.getLogger(RedisOperator.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisSerializer keySerializer;
    private final RedisSerializer valueSerializer;

    public RedisOperator(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        keySerializer = this.redisTemplate.getKeySerializer();
        valueSerializer = this.redisTemplate.getValueSerializer();
    }

    /**
     * by command `scan`
     *
     * @param pattern key, like *key*
     * @param count   scan key number, not result size
     */
    public Set<String> keys(String pattern, long count) {
        return redisTemplate.execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
                ScanOptions options = ScanOptions.scanOptions().match(pattern).count(count).build();
                Set<String> set = new HashSet<>();
                Cursor<byte[]> cursor = connection.scan(options);
                while (cursor.hasNext()) {
                    set.add(new String(cursor.next()));
                }
                return set;
            }
        });
    }

    public boolean exist(String key) {
        Long count = redisTemplate.countExistingKeys(Collections.singletonList(key));
        return count != null && count > 0L;
    }

    public boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            redisTemplate.expire(key, timeout, unit);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean del(String... keys) {
        return this.del(Arrays.asList(keys));
    }

    public boolean del(List<String> keys) {
        try {
            if (keys != null && keys.size() > 0) {
                redisTemplate.delete(keys);
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public void batctSet(Map<String, Object> kvs) {
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                kvs.forEach((k, v) -> {
                    connection.set(Objects.requireNonNull(keySerializer.serialize(k)), Objects.requireNonNull(valueSerializer.serialize(v)));
                });
                return null;
            }
        });
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public List<Object> multiGet(List<String> keys) {
        List<Object> list = redisTemplate.opsForValue().multiGet(Sets.newHashSet(keys));
        List<Object> result = Lists.newArrayList();
        Optional.ofNullable(list).ifPresent(e -> list.forEach(el -> Optional.ofNullable(el).ifPresent(result::add)));
        return result;
    }

    public boolean hset(String key, Map<String, Object> hmap) {
        try {
            redisTemplate.opsForHash().putAll(key, hmap);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean hset(String key, String hkey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hkey, value);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public Object hget(String key, String hkey) {
        return redisTemplate.opsForHash().get(key, hkey);
    }
}
