package com.xxxx.seckill.controller;

import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping("toLogin")
    public String toLogin(Model model) {
        model.addAttribute("msg", "请重新登录");
        return "pages-login";
    }

    //登录功能
    @RequestMapping("doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest req, HttpServletResponse rep) {
        return iUserService.doLogin(loginVo, rep, req);
    }
}
