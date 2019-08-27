package com.alibaba.seckill.service.impl;

import com.alibaba.seckill.dao.UserDao;
import com.alibaba.seckill.pojo.User;
import com.alibaba.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Name UserServiceImpl
 * @Author Yama
 * @Date 2019/8/21 22:13
 * @Version V1.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired(required = false)
    private UserDao userDao;
    @Override
    public User findByPhone(String phone, String password) {
        User user = userDao.findByPhone(phone);
        if (user == null) {
            throw new RuntimeException("该手机号不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        return user;
    }
}
