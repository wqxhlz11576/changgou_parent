package com.changgou.pay.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wq120 on 3/26/2020.
 */
@FeignClient("pay")
@RequestMapping("/wxpay")
public interface WxPayFeign {
    @GetMapping("/nativePay")
    public Result nativePay(@RequestParam("orderId") String orderId,
                            @RequestParam("money") Integer money );
}
