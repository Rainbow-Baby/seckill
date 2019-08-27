package com.alibaba.seckill.dao;

import com.alibaba.seckill.pojo.User;

/**
 * @Description TODO
 * @interfaceName UserDao
 * @Author Yama
 * @Date 2019/8/21 21:57
 * @Version V1.0
 */
public interface UserDao {
    /**
     * 用户登录查询对应信息
     * @param phone
     * @return
     */
    public User findByPhone(String phone);
}
