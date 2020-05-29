package com.changgou.web_order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.feign.CartFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by wq120 on 3/24/2020.
 */
@Controller
@RequestMapping("/wcart")
public class CartWebController {
    @Autowired
    CartFeign cartFeign;

    /**
     * 添加购物车
     */
    @GetMapping("/add")
    @ResponseBody
    public Result<Map> add(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num){

        cartFeign.addItemToCart(skuId, num);

        Map map = cartFeign.getCartItemList();
        return new Result<>(true, StatusCode.OK,"添加购物车成功",map);
    }


    @GetMapping("/list")
    public String list(Model model){
        Map map = cartFeign.getCartItemList();
        model.addAttribute("items",map);
        return "cart";
    }


}
