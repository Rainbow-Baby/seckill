package com.alibaba.seckill.controller;

import com.alibaba.seckill.Utils.MD5Utils;
import com.alibaba.seckill.common.JsonResult;
import com.alibaba.seckill.pojo.SeckillGoods;
import com.alibaba.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Name SeckillController
 * @Author Yama
 * @Date 2019/8/21 22:57
 * @Version V1.0
 */
@Controller
public class SeckillController {
    @Autowired
    private SeckillService seckillService;

    @RequestMapping("/seckill/list.do")
    public String listSeckillGoods(Model model) {
        List<SeckillGoods> list = seckillService.findAllSeckillGoods();
        model.addAttribute("seckillList", list);
        return "seckillList";
    }

    @RequestMapping("/seckill/detail")
    public String findSeckillInfo(Integer sid, Model model) {
        Map<String, Object> map = seckillService.findSeckillGoodsById(sid);
        model.addAttribute("seckillInfo", map);
        return "seckillDetail";
    }

    @RequestMapping("/seckill/info")
    @ResponseBody
    public JsonResult findSeckillInfo(Integer sid) {
        Map<String, Object> map = seckillService.findSeckillInfo(sid);
        return new JsonResult(1, map);
    }

    @RequestMapping("/seckill/buy")
    public String buySeckillGoods(Integer sid, String info) {

        String md5Info = MD5Utils.md5("Rainbow" + sid);
        // 传来的md5如果不一样，则认为数据造假
        if (!info.equals(md5Info)) {
            return "seckillFail";
        }
        seckillService.modifySeckillStock(sid);
        return "orderDetail";
    }

    // 获取 md5 数据
    @RequestMapping("/seckill/urlInfo")
    @ResponseBody
    public JsonResult getMd5Info(Integer sid) {
        String md5Info = MD5Utils.md5("Rainbow" + sid);
        return new JsonResult(1, md5Info);
    }
}
