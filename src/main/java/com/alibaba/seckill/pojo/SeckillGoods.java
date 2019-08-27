package com.alibaba.seckill.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Name SeckillGoods
 * @Author Yama
 * @Date 2019/8/21 22:00
 * @Version V1.0
 */
@Data
public class SeckillGoods {
    private Integer sid;
    private Goods goods;
    private Double seckillStock;
    private Date beginTime;
    private Date endTime;
    private Double seckillPrice;
    private String seckillUrl;
    private Long version;
}
