package com.changgou.gateway_web.service.impl;

import com.changgou.gateway_web.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

/**
 * Created by wq120 on 3/24/2020.
 */
@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public String getJtiFromCookie(ServerHttpRequest request) {
        HttpCookie cookie = request.getCookies().getFirst("uid");

        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    @Override
    public String getJWTFromRedis(String jti) {
        String jwt = stringRedisTemplate.boundValueOps(jti).get();
        return jwt;
    }
}
