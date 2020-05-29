package com.changgou.gateway_web.filter;

import com.changgou.gateway_web.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by wq120 on 3/24/2020.
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered{
    public static final String Authorization = "Authorization";

    @Autowired
    AuthService authService;

    @Value("${auth.loginPage}")
    String loginUrl;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        ServerHttpResponse response = serverWebExchange.getResponse();
        String path = request.getURI().getPath();

        /**
         * if is login, release the request
         */
        if (path.equals("/api/oauth/login") || !URLFilter.hasAuthorize(path)) {
            return gatewayFilterChain.filter(serverWebExchange);
        }

        /**
         * determine whether there is jti, if not, reject request
         */
        String jti = authService.getJtiFromCookie(request);
        if (StringUtils.isEmpty(jti)) {
            //response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //return response.setComplete();
            return this.toLoginPage(loginUrl + "?FROM=" + request.getURI(), serverWebExchange);
        }

        /**
         * check whether there jwt in redis according to jti, if not, reject
         */
        String jwt = authService.getJWTFromRedis(jti);
        if (StringUtils.isEmpty(jwt)) {
            //response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //return response.setComplete();
            return this.toLoginPage(loginUrl+ "?FROM=" + request.getURI(), serverWebExchange);
        }

        /**
         * if has jwt, add the jwt to the header of request
         */
        request.mutate().header(Authorization, "Bearer " + jwt);


        return gatewayFilterChain.filter(serverWebExchange);
    }

    private Mono<Void> toLoginPage(String loginUrl, ServerWebExchange serverWebExchange) {
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location", loginUrl);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
