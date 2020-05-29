package com.changgou.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.config.CustomMessageSender;
import com.changgou.seckill.config.RabbitMQConfig;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SecKillOrderService;
import com.changgou.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by wq120 on 3/27/2020.
 */
@Service
public class SecKillOrderServiceImpl implements SecKillOrderService{
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CustomMessageSender customMessageSender;

    private static final String SECKILL_KEY = "seckill_goods_";
    private static final String SECKILL_GOODS_STOCK_COUNT_KEY="seckill_goods_stock_count_";


    @Override
    public boolean add(Long id, String time, String username) {
        /**
         * prevent order repeatly
         */
        String repeatCommitResult = this.preventRepeatCommit(username, id);
        if ("fail".equals(repeatCommitResult)) {
            return false;
        }

        /**
         * prevent the same user by the same product repeatly.
         */
         SeckillOrder seckillOrder = seckillOrderMapper.getSecKillOrderByUsernameAndGoodsId(username, id);
         if (seckillOrder != null) {
             return false;
         }

        /**
         * get the goods information from redis
         */
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SECKILL_KEY + time).get(id);

        /**
         * get the stock count of the goods
         */
        Integer redisStockCount = Integer.parseInt((String)redisTemplate.opsForValue().get(SECKILL_GOODS_STOCK_COUNT_KEY + seckillGoods.getId()));

        if (redisStockCount <= 0) {
            return false;
        }

        /**
         * decrease the count of the goods
         */
        Long decrement = redisTemplate.opsForValue().decrement(SECKILL_GOODS_STOCK_COUNT_KEY + seckillGoods.getId());
        if (decrement < 0) {
            redisTemplate.boundHashOps(SECKILL_KEY + time).delete(id);
            redisTemplate.delete(SECKILL_GOODS_STOCK_COUNT_KEY + id);
            return false;
        }

        /**
         * create the order
         */
        seckillOrder = new SeckillOrder();
        seckillOrder.setId(idWorker.nextId());
        seckillOrder.setSeckillId(id);
        seckillOrder.setMoney(seckillGoods.getCostPrice());
        seckillOrder.setUserId(username);
        seckillOrder.setSellerId(seckillGoods.getSellerId());
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setStatus("0");

        //发送消息(消息必达)
        customMessageSender.sendMessage("", RabbitMQConfig.SECKILL_ORDER_KEY, JSON.toJSONString(seckillOrder));

        return true;
    }


    //防止重复提交
    private String preventRepeatCommit(String username,Long id) {

        String redisKey = "seckill_user_" + username+"_id_"+id;
        long count = redisTemplate.opsForValue().increment(redisKey, 1);
        if (count == 1){
            //设置有效期五分钟
            redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
            return "success";
        }

        if (count>1){
            return "fail";
        }

        return "fail";

    }


    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Override
    @Transactional
    public int createOrder(SeckillOrder seckillOrder) {

        int result =seckillGoodsMapper.updateStockCount(seckillOrder.getSeckillId());
        if (result<=0){
            return result;
        }

        result =seckillOrderMapper.insertSelective(seckillOrder);
        if (result<=0){
            return result;
        }

        return 1;
    }
}
