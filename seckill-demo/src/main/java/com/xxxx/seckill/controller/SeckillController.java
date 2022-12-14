package com.xxxx.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.tools.json.JSONUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.xxxx.seckill.config.AccessLimit;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillMessage;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MQSender;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.utils.JsonUtil;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/")
@Slf4j
public class SeckillController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private RedisScript<Long> redisScript;

    private Map<Long, Boolean> emptyStockMap = new HashMap<>();

    //998
    //1978
    //4454 (redis decrement)
    //5312 (redis + lua????????????????????????????????????????????? 100????????? JMeter ?????????????????????
    @RequestMapping(value = "doSeckill/{path}", method = RequestMethod.POST)
//    @RequestMapping(value = "doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(User user, Long goodsId, @PathVariable String path) { //@PathVariable String path
        if (null == user) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
//        ValueOperations valueOperations = redisTemplate.opsForValue();
        boolean check = orderService.checkPath(user, goodsId, path);
        if (!check) {
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //????????????????????????
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //?????????????????????redis????????????
        //???????????????????????????????????????????????????????????????????????????????????? redis ?????????????????????????????????????????????????????????
        if (emptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //???????????????Redis ?????????/????????????????????????
//        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if (stock <= 0) {
            emptyStockMap.put(goodsId, true);
            //valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);//????????????
        }
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);
    }

    //??????????????????
    //?????????orderId????????????-1???????????????0
    @RequestMapping(value = "getResult", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        if (null == user) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }


    @AccessLimit(second = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "path", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user, Long goodsId, String captcha, HttpServletRequest request) {
        if (null == user) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        //???????????????
        boolean check = orderService.checkCaptcha(user, goodsId, captcha);
        if (!check) {
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str = orderService.createPath(user, goodsId);
//        String str = "path";
        return RespBean.success(str);
    }

    @RequestMapping(value = "captcha", method = RequestMethod.GET)
    @ResponseBody
    public void verifyCode(User user, Long goodsId, HttpServletResponse rep) {
        if (null == user || goodsId < 0) {
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //???????????????????????????????????????
        rep.setContentType("image/jpg");
        rep.setHeader("Pargam", "No-cache");
        rep.setHeader("Cache-Control", "no-cache");
        rep.setDateHeader("Expires", 0);
        //???????????????
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId,
                captcha.text(), 300, TimeUnit.SECONDS);
        try {
            captcha.out(rep.getOutputStream());
        } catch (IOException e) {
            log.error("?????????????????????", e.getMessage());
        }
    }

    //????????????????????????????????????????????????redis
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount()); // ?????????????????????
            emptyStockMap.put(goodsVo.getId(), false);
        });
    }

    @RequestMapping("doSeckill2")
    public String doSeckill2(Model model, User user, Long goodsId) {
        if (null == user) {
            return "pages-login";
        }
        model.addAttribute("user", user);
        GoodsVo goodsVoByGoodsId = goodsService.findGoodsVoByGoodsId(goodsId);
        //????????????
        if (goodsVoByGoodsId.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK);
            return "seckillFail";
        }
        //????????????????????????
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id", user.getId())
                .eq("goods_id", goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR);
            return "seckillFail";
        }
        Order order = orderService.seckill(user, goodsVoByGoodsId);
        model.addAttribute("order", order);
        model.addAttribute("goods", goodsVoByGoodsId);
        return "orderDetail";
    }
}
