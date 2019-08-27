package com.alibaba.seckill.controller;

import com.alibaba.seckill.common.JsonResult;
import com.alibaba.seckill.pojo.SeckillGoods;
import com.alibaba.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Description TODO
 * @Name ThymeleafController
 * @Author Yama
 * @Date 2019/8/22 20:53
 * @Version V1.0
 */
@Controller
public class ThymeleafController {
    // 注入模板引擎的对象
    @Autowired
    @Qualifier("myTemplateEngine")
    private TemplateEngine templateEngine;
    @Autowired
    private SeckillService seckillService;

    @RequestMapping("/createHtml")
    @ResponseBody
    public JsonResult createHtml(Integer sid) {
        // 从数据库中获取商品信息
        SeckillGoods seckillGoods = seckillService.findGoodsInfoById(sid);
        // Thymeleaf 的上下文对象
        Context context = new Context();
        // 将秒杀商品信息，存入 context 对象中
        context.setVariable("seckillGoods", seckillGoods);
        // 获取Web程序运行时，文件所在的物理路径
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String filepath = path + "/static/" + "seckill_Detail" + sid + ".html";

        FileWriter writer = null;
        File file = new File(filepath);
        // 如果文件存在，进行删除操作
        if (file.exists()) {
            file.delete();
        }

        try {
            writer = new FileWriter(filepath);
        } catch (Exception e) {

        }

        try {
        /*
        生成静态页面
        第一个参数：模板名称
        第二个参数：模板用到的上下文对象
        第三个参数：生成静态页面文件的 IO 流对象
         */
            templateEngine.process("seckill_Detail", context, writer);
            // 更新数据库
            seckillService.updateSeckillUrlById(sid, "seckill_Detail" + sid + ".html");
        } catch (Exception e) {

        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new JsonResult(1, null);
    }
}
