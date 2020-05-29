package com.changgou.web_order.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.pay.feign.WxPayFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by wq120 on 3/26/2020.
 */
@Controller
@RequestMapping("/wxpay")
public class PayController {

    @Autowired
    private OrderFeign orderFeign;

    @Autowired
    private WxPayFeign wxPayFeign;

    /**
     * 微信支付二维码
     * @param orderId
     * @return
     */
    @GetMapping
    public String wxPay(String orderId, Model model){
        //根据orderId查询订单
        Result orderResult = orderFeign.findById( orderId );
        if(orderResult.getData()==null){ //如果订单不存在
            return "fail";//出错页
        }
        Order order = (Order) orderResult.getData();
        //判断支付状态
        if( !"0".equals( order.getPayStatus() )){// 如果不是未支付订单
            return "fail";//出错页
        }
        Result payResult = wxPayFeign.nativePay( orderId, order.getPayMoney() );
        if(payResult.getData()==null){
            return "fail";//出错页
        }
        Map payMap=  (Map)payResult.getData();
        payMap.put( "payMoney", order.getPayMoney() );
        payMap.put( "orderId" ,orderId );
        model.addAllAttributes( payMap );
        return "wxpay";
    }

    @GetMapping("/toPaySuccess")
    public String topaysuccess(@RequestParam("payMoney") String payMoney, Model model){
        System.out.println(payMoney);
        model.addAttribute("payMoney",payMoney);
        return "paysuccess";
    }

}
