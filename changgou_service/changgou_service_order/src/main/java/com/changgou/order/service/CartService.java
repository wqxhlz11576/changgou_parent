package com.changgou.order.service;

import java.util.Map;

/**
 * Created by wq120 on 3/24/2020.
 */
public interface CartService {
    void addItemToCart(String username, String skuId, Integer num);

    Map getCartItemList(String username);
}
