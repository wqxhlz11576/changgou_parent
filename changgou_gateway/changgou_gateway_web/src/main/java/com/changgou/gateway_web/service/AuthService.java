package com.changgou.gateway_web.service;

import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * Created by wq120 on 3/24/2020.
 */
public interface AuthService {
    String getJtiFromCookie(ServerHttpRequest request);

    String getJWTFromRedis(String jti);
}
