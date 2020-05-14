package edu.cn.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import com.alibaba.fastjson.JSON;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    public <T> T get(KeyPrefix prefix, String key,Class<T> clazz) {
//        Jedis jedis = jedisPool.getResource();
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
//            生成正真的key
            String realKey = prefix.getPrefix() + key;
            String  str = jedis.get(key);
            T t =  stringToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }

    }

    public <T> boolean set(KeyPrefix prefix,String key,T value) {
//        Jedis jedis = jedisPool.getResource();
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
//            String  str = jedis.get(key);
//            T t =  stringToBean(str);
            String str = beanToString(value);
            if (str == null || str.length() <= 0){
                return false;
            }
            String realKey = prefix.getPrefix() + key;
            int seconds =  prefix.expireSeconds();
            if(seconds <= 0) {
                jedis.set(realKey, str);
            }else {
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }

    }

    public Boolean exists(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realPrefix = prefix.getPrefix() + key;
            return jedis.exists(realPrefix);
        }finally {
            returnToPool(jedis);
        }
    }

    public Long incr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realPrefix = prefix.getPrefix() + key;
            return jedis.incr(realPrefix);
        }finally {
            returnToPool(jedis);
        }
    }

    public Long decr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realPrefix = prefix.getPrefix() + key;
            return jedis.decr(realPrefix);
        }finally {
            returnToPool(jedis);
        }
    }

    private <T> String beanToString(T value) {

        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return "" + value;
        }else {
            return com.alibaba.fastjson.JSON.toJSONString(value);
        }
    }

    private <T> T stringToBean(String str,Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return com.alibaba.fastjson.JSON.toJavaObject(com.alibaba.fastjson.JSON.parseObject(str), clazz);
        }

    }

    private void returnToPool(Jedis jedis) {
        if (jedis!=null){
            jedis.close();
        }

    }


}
