package com.alibaba.seckill;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeckillApplicationTests {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void contextLoads() {

        // 操作普通的 Key-value 数据（value 类型时字符串）
        redisTemplate.opsForValue().set("Name", "小甜甜");

        String name = redisTemplate.opsForValue().get("name");
        System.out.println(name);

        redisTemplate.opsForHash().put("user", "name", "Ayo，在你的头上暴扣");
        redisTemplate.opsForHash().put("user", "age", "19");

        Boolean password = redisTemplate.opsForValue().setIfAbsent("password", "123456789");
        System.out.println(password);
    }



}
