package com.xxxx.seckill.exception;

import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RespBean exceptionHandler(Exception e) {
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return RespBean.error(ex.getRespBeanEnum());
        }else if (e instanceof BindException){
            BindException bex = (BindException) e;
            RespBean respError = RespBean.error(RespBeanEnum.BIND_ERROR);
            respError.setMessage("参数校验异常" + bex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respError;
        }
        System.out.println("error:" + e.getMessage());
        return RespBean.error(RespBeanEnum.ERROR);
    }
}
