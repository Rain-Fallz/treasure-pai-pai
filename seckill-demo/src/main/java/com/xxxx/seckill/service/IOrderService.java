package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.OrderDetailVo;

public interface IOrderService extends IService<Order> {

    //秒杀
    Order seckill(User user, GoodsVo goodsVoByGoodsId);

    OrderDetailVo orderDetail(Long orderId);

    //获取秒杀地址
    String createPath(User user, Long goodsId);

    boolean checkPath(User user, Long goodsId, String path);

    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
