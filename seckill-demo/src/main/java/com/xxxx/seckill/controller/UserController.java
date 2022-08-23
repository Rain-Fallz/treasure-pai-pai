package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MQSender;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private MQSender mqSender;

//    @RequestMapping("userInfo")
//    @ResponseBody
//    public RespBean userInfo(User user) {
//        return RespBean.success(user);
//    }


//    @RequestMapping("mq/fanout")
//    @ResponseBody
//    public void mqfanout() {
//        mqSender.send("hello");
//    }
//
//    @RequestMapping("mq/direct")
//    @ResponseBody
//    public void mqdirect() {
//        mqSender.send01("hello red");
//        mqSender.send02("hello green");
//    }



}
