package com.alibaba.seckill.controller;

import com.alibaba.seckill.common.JsonResult;
import com.alibaba.seckill.pojo.User;
import com.alibaba.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Description TODO
 * @Name UserController
 * @Author Yama
 * @Date 2019/8/21 22:21
 * @Version V1.0
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/login.do")
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/user/login.do")
    @ResponseBody
    public JsonResult findByPhone(String phone, String password, HttpSession session) {
        User user = userService.findByPhone(phone, password);
        session.setAttribute("loginuser", user);
        return new JsonResult(1, null);
    }
}
