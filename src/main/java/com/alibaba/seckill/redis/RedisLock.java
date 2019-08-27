package com.alibaba.seckill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Name RedisLock
 * @Author Yama
 * @Date 2019/8/24 11:21
 * @Version V1.0
 */
@Component
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 加锁
    public boolean lock(String sid, String value) {
        if (redisTemplate.opsForValue().setIfAbsent(sid, value)) {
            // 考虑到可能无法手动解锁，设置超时时间
            redisTemplate.expire(sid, 10, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    // 解锁
    public void unlock(String sid, String value) {
        String v = redisTemplate.opsForValue().get(sid);
        if (v != null && v.equals(value)) {
            // 删除 Key
            redisTemplate.delete(sid);
        }
    }
}
