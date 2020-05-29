package com.changgou.pay.service;

import java.util.Map;

/**
 * Created by wq120 on 3/26/2020.
 */
public interface WxPayService {
    Map nativePay(String orderId, Integer money);

    Map queryOrder(String orderId);
}
