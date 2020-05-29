package com.changgou.seckill.service;

import com.changgou.seckill.pojo.SeckillGoods;

import java.util.List;

/**
 * Created by wq120 on 3/27/2020.
 */
public interface SecKillService {
    List<SeckillGoods> list(String time);
}
