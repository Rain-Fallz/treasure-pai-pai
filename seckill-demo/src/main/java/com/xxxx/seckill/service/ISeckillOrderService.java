package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;

public interface ISeckillOrderService extends IService<SeckillOrder> {

    //获取秒杀结果
    //成功：orderId，失败：-1，排队中：0
    Long getResult(User user, Long goodsId);
}
