package com.changgou.oauth.service.impl;

import com.changgou.oauth.service.AuthService;
import com.changgou.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wq120 on 3/23/2020.
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${auth.ttl}")
    private long ttl;

    @Override
    public AuthToken applyToken(String username, String password, String clientId, String clientSecret) {
        //1. get url to apply for token
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-auth");
        URI uri = serviceInstance.getUri();
        String url = uri + "/oauth/token";

        //2. Assemble request
        //2.1 header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Authorization", this.getHttpBasicAuthorization(clientId, clientSecret));

        //2.2 body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);

        //Http entity
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, header);

        //400 401不做处理
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });


        //send request
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        Map result = exchange.getBody();

        if (result == null || result.get("access_token") == null || result.get("refresh_token") == null || result.get("jti") == null) {
            throw new RuntimeException("create token failure");
        }

        AuthToken token = new AuthToken();
        token.setJti((String)result.get("jti"));
        token.setAccessToken((String)result.get("access_token"));
        token.setRefreshToken((String)result.get("refresh_token"));

        //save the jwt to redis
        redisTemplate.boundValueOps(token.getJti()).set(token.getAccessToken(), ttl, TimeUnit.SECONDS);
        return token;
    }

    private String getHttpBasicAuthorization(String clientId, String clientSecret) {
        String s = clientId + ":" + clientSecret;
        byte[] encode = Base64Utils.encode(s.getBytes());
        return "Basic " + new String(encode);
    }
}
