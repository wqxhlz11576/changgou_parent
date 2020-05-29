package com.changgou.order.listen;

import com.alibaba.fastjson.JSON;
import com.changgou.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by wq120 on 3/26/2020.
 */
@Component
@RabbitListener(queues = "order_pay")
public class OrderPayListener {

    @Autowired
    private OrderService orderService;

    /**
     * 更新支付状态
     * @param message
     */
    @RabbitHandler
    public void updatePayStatus(String message){
        System.out.println("接收到消息："+message);
        Map map = JSON.parseObject( message, Map.class );
        orderService.updatePayStatus( (String)map.get("orderId"), (String)map.get("transactionId") );
    }
}
