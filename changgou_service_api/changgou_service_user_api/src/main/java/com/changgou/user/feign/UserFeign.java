package com.changgou.user.feign;

import com.changgou.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by wq120 on 3/24/2020.
 */
@FeignClient("user")
@RequestMapping(value = "/user")
public interface UserFeign {

    @RequestMapping(value = "/load/{username}", method = RequestMethod.GET)
    public User findUserByUsername(@PathVariable("username") String username);
}
