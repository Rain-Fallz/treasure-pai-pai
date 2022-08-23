package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IUserService extends IService<User> {

    //登录
    RespBean doLogin(LoginVo loginVo, HttpServletResponse rep, HttpServletRequest req);

    //根据cookie获取用户
    User getUserByCookie(String userTicket, HttpServletResponse rep, HttpServletRequest req);

    //更新密码
    RespBean updatePassword(String userTicket, String password, HttpServletResponse rep, HttpServletRequest req);
}
