package com.changgou.order.feign;

import com.changgou.entity.Result;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by wq120 on 3/25/2020.
 */
@FeignClient("order")
@RequestMapping("/order")
public interface OrderFeign {
    @PostMapping
    public Result<String> add(@RequestBody Order order);

    @GetMapping("/{id}")
    public Result<Order> findById(@PathVariable("id") String id);
}
