package com.neusoft.nursingcenter.redisdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

//Dao层专用注解
@Repository
public class RedisDaoImpl implements RedisDao {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void set(String key, String value) {
        // TODO Auto-generated method stub
        ValueOperations vo= redisTemplate.opsForValue();
//		向redis中存储键值对
        vo.set(key, value);

    }

    @Override
    public String get(String key) {
        // TODO Auto-generated method stub
        ValueOperations vo= redisTemplate.opsForValue();
//        String val= vo.get(key).toString();
        String val=(String) vo.get(key);
        return val;
    }

    @Override
    public void set(String key, String value, long time, TimeUnit unit) {
        // TODO Auto-generated method stub
        ValueOperations vo= redisTemplate.opsForValue();
//		调用带过期时间的存储方法
        vo.set(key, value, time, unit);
    }

    @Override
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

}
