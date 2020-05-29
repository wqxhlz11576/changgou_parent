package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by wq120 on 3/24/2020.
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;

    /**
     * The user has to login first
     * @return
     */
    @GetMapping("/add")
    public Result addItemToCart(@RequestParam("skuId") String skuId,
                                @RequestParam("num") Integer num) {
        String username = tokenDecode.getUserInfo().get("username");

        cartService.addItemToCart(username, skuId, num);
        return new Result(true, StatusCode.OK, "Add item to cart successfully!");
    }

    @GetMapping("/list")
    public Map getCartItemList() {
        String username = tokenDecode.getUserInfo().get("username");
        Map cartItemList = cartService.getCartItemList(username);
        return cartItemList;
    }
}
