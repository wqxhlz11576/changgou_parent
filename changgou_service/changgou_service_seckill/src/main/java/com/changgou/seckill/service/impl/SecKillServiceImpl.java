package com.changgou.seckill.service.impl;

import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wq120 on 3/27/2020.
 */
@Service
public class SecKillServiceImpl implements SecKillService {
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String SECKILL_KEY = "seckill_goods_";
    private static final String SECKILL_GOODS_STOCK_COUNT_KEY="seckill_goods_stock_count_";

    @Override
    public List<SeckillGoods> list(String time) {
        /**
         * 查询秒杀商品列表
         * @param time
         * @return
         */
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps(SECKILL_KEY+time).values();

        for (SeckillGoods seckillGoods : seckillGoodsList) {
            String value = (String)redisTemplate.opsForValue().get(SECKILL_GOODS_STOCK_COUNT_KEY+seckillGoods.getId());
            seckillGoods.setStockCount(Integer.parseInt(value));
        }

        return seckillGoodsList;
    }
}
