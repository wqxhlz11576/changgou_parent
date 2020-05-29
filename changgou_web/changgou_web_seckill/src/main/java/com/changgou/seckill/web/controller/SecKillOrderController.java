package com.changgou.seckill.web.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.seckill.feign.SecKillOrderFeign;
import com.changgou.seckill.web.aspect.AccessLimit;
import com.changgou.seckill.web.util.CookieUtil;
import com.changgou.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Created by wq120 on 3/27/2020.
 */
@Controller
@CrossOrigin
@RequestMapping("/wseckillorder")
public class SecKillOrderController {

    @Autowired
    private SecKillOrderFeign secKillOrderFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 秒杀下单
     * @param time 当前时间段
     * @param id 秒杀商品id
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @AccessLimit
    public Result add(String time, Long id, String randomCode){
        String cookieValue = this.readCookie();
        String redisCode = (String) redisTemplate.boundValueOps("randomcode_" + cookieValue).get();

        if (!redisCode.equals(randomCode)) {
            return new Result(false, StatusCode.ERROR, "Order failed");
        }
        return secKillOrderFeign.add(time, id);
    }

    @GetMapping("/getToken")
    @ResponseBody
    public String getToken(){
        String randomString = RandomUtil.getRandomString();

        String cookieValue = this.readCookie();
        redisTemplate.boundValueOps("randomcode_"+cookieValue).set(randomString,20, TimeUnit.SECONDS);
        return randomString;
    }

    //读取cookie
    private String readCookie(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String cookieValue = CookieUtil.readCookie(request, "uid").get("uid");
        return cookieValue;
    }
}
