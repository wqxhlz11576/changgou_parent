package com.changgou.order.service.impl;

import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wq120 on 3/24/2020.
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    SpuFeign spuFeign;

    @Autowired
    SkuFeign skuFeign;

    @Autowired
    RedisTemplate redisTemplate;

    private static String cart = "cart_";

    @Override
    public void addItemToCart(String username, String skuId, Integer num) {

        OrderItem orderItem = (OrderItem) redisTemplate.boundHashOps(cart + username).get(skuId);

        if (orderItem != null) {
            orderItem.setNum(orderItem.getNum()+num);
            orderItem.setMoney(orderItem.getNum()*orderItem.getPrice());
            orderItem.setPayMoney(orderItem.getNum()*orderItem.getPrice());
        } else {
            Result<Sku> skuInfo = skuFeign.findById(skuId);
            Sku sku = skuInfo.getData();

            Result<Spu> spuInfo = spuFeign.findById(sku.getSpuId());
            Spu spu = spuInfo.getData();
            orderItem = this.createCartItem(num, sku, spu);
        }

        redisTemplate.boundHashOps(cart + username).put(skuId, orderItem);
    }

    @Override
    public Map getCartItemList(String username) {
        Map res = new HashMap();
        List<OrderItem> orderItems = redisTemplate.boundHashOps(cart + username).values();
        res.put("orderItemList", orderItems);

        Integer totalNum = 0;
        Integer totalPrice = 0;

        for (OrderItem orderItem : orderItems) {
            totalNum += orderItem.getNum();
            totalPrice += orderItem.getPrice() * orderItem.getNum();
        }

        res.put("totalNum", totalNum);
        res.put("totalMoney", totalPrice);

        return res;
    }

    private OrderItem createCartItem(Integer num, Sku sku, Spu spu) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(num*orderItem.getPrice());       //单价*数量
        orderItem.setPayMoney(num*orderItem.getPrice());    //实付金额
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight()*num);           //重量=单个重量*数量

        //分类ID设置
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }
}
