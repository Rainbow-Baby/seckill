package com.alibaba.seckill.service;

import com.alibaba.seckill.pojo.SeckillGoods;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @interfaceName SeckillService
 * @Author Yama
 * @Date 2019/8/21 22:10
 * @Version V1.0
 */
public interface SeckillService {

    /**
     * 查询所有商品详情
     * @return
     */
    public List<SeckillGoods> findAllSeckillGoods();

    /**
     * 查询单件商品详情
     * @param sid
     * @return
     */
    public Map<String, Object> findSeckillGoodsById(Integer sid);

    /**
     * 秒杀后修改库存
     * @param sid
     */
    public void modifySeckillStock(Integer sid);

    // 供生成静态页面的控制器使用
    public SeckillGoods findGoodsInfoById(Integer sid);

    // 页面静态化后，发送 Ajax 请求获取数据时调用的方法
    public Map<String, Object> findSeckillInfo(Integer sid);

    public void updateSeckillUrlById(Integer sid, String url);


}
