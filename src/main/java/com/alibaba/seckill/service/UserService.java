package com.alibaba.seckill.service;

import com.alibaba.seckill.pojo.User;

/**
 * @Description TODO
 * @interfaceName UserService
 * @Author Yama
 * @Date 2019/8/21 22:10
 * @Version V1.0
 */
public interface UserService {
    /**
     * 用户登录查询对应信息
     * @param phone
     * @return
     */
    public User findByPhone(String phone, String password);

}
