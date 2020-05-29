package com.changgou.user.feign;

import com.changgou.entity.Result;
import com.changgou.user.pojo.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by wq120 on 3/25/2020.
 */
@FeignClient("user")
@RequestMapping("/address")
public interface AddressFeign {
    @GetMapping(value = "/list")
    public Result<List<Address>> list();
}
