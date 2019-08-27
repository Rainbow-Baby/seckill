package com.alibaba.seckill.dao;

import com.alibaba.seckill.pojo.SeckillGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description TODO
 * @interfaceName SeckillDao
 * @Author Yama
 * @Date 2019/8/21 22:10
 * @Version V1.0
 */
public interface SeckillDao {
    /**
     * 查询所有秒杀商品列表
     * @return
     */
    public List<SeckillGoods> findAll();

    /**
     * 查询单件商品详情页
     * @param sid
     * @return
     */
    public SeckillGoods findById(Integer sid);

    /**
     * 修改库存
     * @param sid
     */
    public void modifySeckillStock(Integer sid);

    /**
     * 查询剩余库存
     * @param sid
     * @return
     */
    public int getSeckillStock(Integer sid);

    /**
     * 修改访问的url
     * @param sid
     * @param url
     */
    public void modifySeckillUrl(@Param("sid")Integer sid, @Param("url")String url);

    /**
     * 悲观锁
     * @param sid
     * @return
     */
    public SeckillGoods findByIdForUpdate(Integer sid);

    /**
     * 乐观锁
     * @param sid
     * @param version
     * @return
     */
    public Integer updateByIdAndVersion(@Param("sid") Integer sid, @Param("version") Long version);
}
