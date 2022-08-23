package com.xxxx.seckill.vo;

import com.xxxx.seckill.validator.IsMoblie;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class LoginVo {

    @NotNull
    @IsMoblie
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}
