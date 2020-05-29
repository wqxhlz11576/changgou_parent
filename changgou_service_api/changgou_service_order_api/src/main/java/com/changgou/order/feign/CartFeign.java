package com.changgou.order.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by wq120 on 3/24/2020.
 */
@FeignClient("order")
@RequestMapping("/cart")
public interface CartFeign {
    @GetMapping("/add")
    public Result addItemToCart(@RequestParam("skuId") String skuId,
                                @RequestParam("num") Integer num);

    @GetMapping("/list")
    public Map getCartItemList();
}
