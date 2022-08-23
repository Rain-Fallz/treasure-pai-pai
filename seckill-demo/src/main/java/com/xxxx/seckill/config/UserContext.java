package com.xxxx.seckill.config;

import com.xxxx.seckill.pojo.User;

/**
 * 存的是每个线程的私有数据
 */
public class UserContext {

    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }

}
