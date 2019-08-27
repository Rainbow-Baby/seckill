package com.alibaba.seckill.service.impl;

import com.alibaba.seckill.Utils.MD5Utils;
import com.alibaba.seckill.common.Common;
import com.alibaba.seckill.dao.SeckillDao;
import com.alibaba.seckill.pojo.SeckillGoods;
import com.alibaba.seckill.redis.RedisLock;
import com.alibaba.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description TODO
 * @Name SeckillServiceImpl
 * @Author Yama
 * @Date 2019/8/21 22:12
 * @Version V1.0
 */
@Service
@Transactional
public class SeckillServiceImpl implements SeckillService {
    // 线程安全的 map 结构
    private ConcurrentHashMap<String, Boolean> overMap = new ConcurrentHashMap<>();

    @Autowired(required = false)
    private SeckillDao seckillDao;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<SeckillGoods> findAllSeckillGoods() {
        return seckillDao.findAll();
    }

    @Override
    public Map<String, Object> findSeckillGoodsById(Integer sid) {
        Map<String, Object> map = new HashMap<>();
        SeckillGoods seckillGoods = seckillDao.findById(sid);

        // 秒杀开始前的时间
        long remainTime = 0;
        // 秒杀所处状态，-1 表示未开始， 0 表示进行中， 1 表示结束
        long seckillState = 0;

        Date beginTime = seckillGoods.getBeginTime();
        Date endTime = seckillGoods.getEndTime();
        Date currentTime = new Date();
        long interval = beginTime.getTime() - currentTime.getTime();
        // 秒杀未开始
        if (interval > 0) {
            // remainTime 为毫秒值，故除以 1000
            remainTime = interval / 1000;
            seckillState = -1;
        } else {
            long  endInterval = endTime.getTime() - currentTime.getTime();
            // 当前时间超过结束时间，秒杀结束
            if (endInterval < 0) {
                seckillState = 1;
                remainTime = -1;
            } else {
                /*
                当前时间在开始和结束中间
                获取秒杀商品的属于库存
                 */
                Double stock = seckillGoods.getSeckillStock();
                // 秒杀结束
                if (stock <= 0) {
                    seckillState = 1;
                    remainTime = -1;
                } else {
                    // 秒杀进行中
                    seckillState = 0;
                    remainTime = 0;
                }
            }
        }

        map.put("seckill", seckillGoods);
        map.put("remainTime", remainTime);
        map.put("seckillState", seckillState);

        return map;
    }

//    @Override
//    public void modifySeckillStock(Integer sid) {
//        // 上锁
//        synchronized (SeckillServiceImpl.class) {
//            int stock = seckillDao.getSeckillStock(sid);
//            // 判断是否有库存
//            if (stock > 0) {
//                // 库存减 1
//                seckillDao.modifySeckillStock(sid);
//                // 生成订单
//            } else {
//                throw new RuntimeException("已经秒杀结束~");
//            }
//        }
//    }

    // 悲观锁
//    @Override
//    public void modifySeckillStock(Integer sid) {
//        SeckillGoods seckillGoods = seckillDao.findByIdForUpdate(sid);
//        // 判断是否有库存
//        if (seckillGoods.getSeckillStock() > 0) {
//            // 库存减 1
//            seckillDao.modifySeckillStock(sid);
//            // 生成订单
//        } else {
//            throw new RuntimeException("已经秒杀结束~");
//        }
//    }

    // 乐观锁
//    @Override
//    public void modifySeckillStock(Integer sid) {
//
//        SeckillGoods seckillGoods = seckillDao.findById(sid);
//        // 判断是否有库存
//        if (seckillGoods.getSeckillStock() > 0) {
//            // 库存减 1
//            seckillDao.updateByIdAndVersion(sid, seckillGoods.getVersion());
//            // 生成订单
//        } else {
//            throw new RuntimeException("已经秒杀结束~");
//        }
//    }

    // 使用 redis 加锁
//    @Override
//    public void modifySeckillStock(Integer sid) {
//        Long value = System.currentTimeMillis();
//
//        if(redisLock.lock("sid" + sid, value.toString())) {
//            try {
//                int stock = seckillDao.getSeckillStock(sid);
//                if (stock > 0) {
//                    // 库存 -1
//                    seckillDao.modifySeckillStock(sid);
//
//                    // 生产订单
//                }else {
//                    throw new RuntimeException("已经秒杀结束~");
//                }
//            } catch(Exception e) {
//
//            } finally {
//                redisLock.unlock("sid" + sid, value.toString());
//            }
//        } else  {
//          return;
//        }
//    }

    // redis 中预减库存，借助消息队列，从mysql减库存，生成订单
    @Override
    public void modifySeckillStock(Integer sid) {

        Boolean state = overMap.get("sid" + sid);
        if (state != null && state == true) {
            throw new RuntimeException("秒杀结束");
        }

        // 针对数字结构中的数据，进行增加或者减少的炒作
        // 返回的是运算后的最新数据
        Long stock = redisTemplate.opsForHash().increment("stockInfo", "sid" + sid, -1);
        if (stock >= 0) {
            System.out.println("---------------------------------------");
            synchronized (SeckillServiceImpl.class) {
                Common.count++;
            }
            // 执行相关逻辑
            // 向消息队列中写数据

        } else {
            System.out.println(Common.count);
            // 预减库存结束，无法秒杀
            overMap.put("sid" + sid, true);
            throw new RuntimeException("秒杀结束");
        }
    }

    @Override
    public SeckillGoods findGoodsInfoById(Integer sid) {
        return seckillDao.findById(sid);
    }

    @Override
    public Map<String, Object> findSeckillInfo(Integer sid) {
        Map<String, Object> map = new HashMap<>();
        SeckillGoods seckillGoods = seckillDao.findById(sid);

        // 秒杀开始前的时间
        long remainTime = 0;
        // 秒杀所处状态，-1 表示未开始， 0 表示进行中， 1 表示结束
        int seckillState = 0;
        // 秒杀商品的剩余库存
        int seckillStock = seckillGoods.getSeckillStock().intValue();

        String md5Info = "";

        Date beginTime = seckillGoods.getBeginTime();
        Date endTime = seckillGoods.getEndTime();
        Date currentTime = new Date();
        long interval = beginTime.getTime() - currentTime.getTime();
        // 秒杀未开始
        if (interval > 0) {
            // remainTime 为毫秒值，故除以 1000
            remainTime = interval / 1000;
            seckillState = -1;
        } else {
            long endInterval = endTime.getTime() - currentTime.getTime();
            // 当前时间超过结束时间，秒杀结束
            if (endInterval < 0) {
                seckillState = 1;
                remainTime = -1;
            } else {
                /*
                当前时间在开始和结束中间
                获取秒杀商品的属于库存
                 */
                Double stock = seckillGoods.getSeckillStock();
                // 秒杀结束
                if (stock <= 0) {
                    seckillState = 1;
                    remainTime = -1;
                } else {
                    // 秒杀进行中
                    seckillState = 0;
                    remainTime = 0;
                    // 盐值
                    String salt = "Rainbow";
                    md5Info = MD5Utils.md5(salt + sid);
                }
            }
        }

        map.put("seckillStock", seckillStock);
        map.put("remainTime", remainTime);
        map.put("seckillState", seckillState);
        map.put("md5Info", md5Info);
        return map;
    }

    @Override
    public void updateSeckillUrlById(Integer sid, String url) {
        seckillDao.modifySeckillUrl(sid, url);
    }



}
