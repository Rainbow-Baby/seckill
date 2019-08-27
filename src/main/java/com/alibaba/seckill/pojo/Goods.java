package com.alibaba.seckill.pojo;

import lombok.Data;

/**
 * @Description TODO
 * @Name Goods
 * @Author Yama
 * @Date 2019/8/21 22:02
 * @Version V1.0
 */
@Data
public class Goods {
    private Integer gid;
    private String gname;
    private Double Price;
    private String imgPath;
}
