package com.changgou.seckill.service;

import com.changgou.seckill.pojo.SeckillOrder;

/**
 * Created by wq120 on 3/27/2020.
 */
public interface SecKillOrderService {
    boolean add(Long id, String time, String username);

    int createOrder(SeckillOrder seckillOrder);
}
