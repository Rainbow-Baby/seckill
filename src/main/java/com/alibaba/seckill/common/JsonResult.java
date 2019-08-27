package com.alibaba.seckill.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Name JsonResult
 * @Author Yama
 * @Date 2019/8/21 21:53
 * @Version V1.0
 */
@Data // 生成getter,setter等函数
@AllArgsConstructor // 生成全参数构造函数
@NoArgsConstructor// 生成无参构造函数
public class JsonResult {
    private Integer code;
    private Object info;
}
