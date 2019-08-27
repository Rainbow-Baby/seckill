package com.alibaba.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description TODO
 * @Name User
 * @Author Yama
 * @Date 2019/8/21 21:58
 * @Version V1.0
 */
@Data // 自动生成对应 get/set 方法
@ToString // 从写 ToString 方法
@NoArgsConstructor // 生成无参的构造方法
@AllArgsConstructor // 生成带所有参数的构造方法
public class User {
    private Integer id;
    private String phone;
    private String password;
}
