package com.changgou.seckill.web.aspect;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Scope
@Aspect
public class AccessLimitAop {

    //设置令牌生成的速度
    private RateLimiter rateLimiter = RateLimiter.create(10.0);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Pointcut("@annotation(com.changgou.seckill.web.aspect.AccessLimit)")
    public void limit(){};

    @Around("limit()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint){

        //判断当前是否能够访问（被限流）
        boolean flag = rateLimiter.tryAcquire();
        Object object = null;

        try {
            if (flag){
                //放行
                object = proceedingJoinPoint.proceed();
            }else{
                //被限流
                String errorMessage = JSON.toJSONString(new Result<>(false, StatusCode.ACCESSERROR,"访问过快，请稍后重试"));
                //写回客户端
                this.outMessage(httpServletResponse,errorMessage);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return object;
    }

    private void outMessage(HttpServletResponse response, String errorMessage) {
        try {
            response.getOutputStream().write(errorMessage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
