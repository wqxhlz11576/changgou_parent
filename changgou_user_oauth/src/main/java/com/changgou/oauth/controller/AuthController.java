package com.changgou.oauth.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.oauth.service.AuthService;
import com.changgou.oauth.util.AuthToken;
import com.changgou.oauth.util.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by wq120 on 3/23/2020.
 */
@Controller
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Value("${auth.clientId}")
    String clientId;

    @Value("${auth.clientSecret}")
    String clientSecret;

    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;

    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestParam("username") String username, @RequestParam("password") String password) {
        //1. check parameters
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("The username is empty");
        }

        if (StringUtils.isEmpty(password)) {
            throw new RuntimeException("The password is empty");
        }

        //2. apply for token
        AuthToken token = authService.applyToken(username, password, clientId, clientSecret);

        //3. set jti to cookie
        String jti = token.getJti();
        this.setJtiToCookis(jti);

        return new Result(true, StatusCode.OK, "login successfully");
    }

    @RequestMapping("/toLogin")
    public String toLogin(@RequestParam(value = "FROM", required = false, defaultValue = "") String from,
                          Model model){
        model.addAttribute("from", from);
        return "login";
    }

    private void setJtiToCookis(String jti) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", jti, cookieMaxAge, false);
    }
}
