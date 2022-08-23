package com.xxxx.seckill;

import com.xxxx.seckill.utils.UUIDUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SeckillDemoApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisScript<Boolean> redisScript;

    @Test
    void contextLoads() {
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        String s = UUID.randomUUID().toString();
//        Boolean isLock = valueOperations.setIfAbsent("k1", s, 120, TimeUnit.SECONDS);
//        if (isLock) {
//            valueOperations.set("name", "xxxx");
//            String name = (String) valueOperations.get("name");
//            System.out.println("name = " + name);
//            System.out.println(valueOperations.get("k1"));
//            Boolean result = (Boolean) redisTemplate.execute(redisScript, Collections.singletonList("k1"), s);
//            System.out.println(result);
//        }else {
//            System.out.println("有线程在使用，请稍后");
//        }


        
    }


    public int[] singleNumber(int[] nums) {
        int result = 0;
        for (int i = 0; i < nums.length; i++) {
            result = result ^ nums[i];
        }
        int rightOne = result & (~result + 1);//提取result最右的1
        //System.out.println(rightOne);
        int firstResult = 0;
        for (int num : nums) {
            if ((rightOne & num) == rightOne) {
                firstResult ^= num;
            }
        }
        return new int[]{firstResult, result ^ firstResult};
    }

}
