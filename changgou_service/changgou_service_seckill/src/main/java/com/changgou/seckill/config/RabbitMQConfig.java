package com.changgou.seckill.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wq120 on 3/27/2020.
 */
@Configuration
public class RabbitMQConfig {

    //秒杀商品订单消息
    public static final String SECKILL_ORDER_KEY="seckill_order";

    @Bean
    public Queue queue(){
        //开启队列持久化
        return new Queue(SECKILL_ORDER_KEY,true);
    }
}
