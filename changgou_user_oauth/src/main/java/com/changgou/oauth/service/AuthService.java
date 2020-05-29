package com.changgou.oauth.service;

import com.changgou.oauth.util.AuthToken;

import java.util.Map;

/**
 * Created by wq120 on 3/23/2020.
 */
public interface AuthService {
    AuthToken applyToken(String username, String password, String clientId, String clientSecret);
}
