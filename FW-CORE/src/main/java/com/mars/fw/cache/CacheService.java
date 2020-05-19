package com.mars.fw.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis 基础应用类
 *
 * @Author King
 * @create 2020/4/21 16:20
 */
@Component
public class CacheService implements InitializingBean {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void afterPropertiesSet() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    }

    /**
     * 发布订阅消息
     *
     * @param chanel
     * @param msg
     */
    public void publish(String chanel, Object msg) {
        redisTemplate.convertAndSend(chanel, msg);
    }

    /**
     * 通配 key 值
     * 1) *：通配任意多个字符(至少一个);
     * 例：*admin* 匹配 admin、cccadmin、adminbbb、bbbadminccc
     * 2) ?：通配单个字符;
     * 例：admin?  匹配 admin1、admin2
     * 3) []：通配括号内的某一个字符 )
     * 例：j[u]
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 设置超时时间
     *
     * @param key
     * @param expireTimeSecond 秒
     * @return
     */
    public boolean setExpire(final String key, final long expireTimeSecond) {
        return redisTemplate.expire(key, expireTimeSecond, TimeUnit.SECONDS);
    }

    /**
     * 获取剩余时间
     *
     * @param key
     * @return
     */
    public long getExpire(final String key) {
        return redisTemplate.getExpire(key);
    }


    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public void set(final String key, Object value) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @param expireTime 秒
     */
    public void set(final String key, Object value, Long expireTime) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        this.setExpire(key, expireTime);
    }

    public void setWithTime(final String key, Object value, Long expireTime, TimeUnit timeUnit) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value, expireTime, timeUnit);
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key（通配）
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKeyValue
     */
    public void hmSet(String key, Map<String, Object> hashKeyValue) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        for (Map.Entry<String, Object> entry : hashKeyValue.entrySet()) {
            hash.put(key, entry.getKey(), entry.getValue());
        }
    }

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 哈希删除数据
     *
     * @param key
     * @param hashKey
     */
    public void hmDel(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.delete(key, hashKey);
    }

    /**
     * 列表 获取长度
     *
     * @param k
     * @return
     */
    public long lLen(String k) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.size(k);
    }


    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    /**
     * 列表获取
     *
     * @param k
     * @param l
     * @return
     */
    public Object lIndex(String k, long l) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.index(k, l);
    }

    /**
     * 列表获取
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    public void add(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key, value);
    }

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }


}
