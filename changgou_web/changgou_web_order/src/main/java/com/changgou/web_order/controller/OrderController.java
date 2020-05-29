package com.changgou.web_order.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.feign.AddressFeign;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by wq120 on 3/25/2020.
 */
@Controller
@RequestMapping("/worder")
public class OrderController {
    @Autowired
    private CartFeign cartFeign;

    @Autowired
    private AddressFeign addressFeign;

    @Autowired
    private OrderFeign orderFeign;


    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody Order order){
        Result<String> result = orderFeign.add(order);
        return  result;
    }

    @RequestMapping("/ready/order")
    public String reaadyOrder(Model model) {
        List<Address> addressList = addressFeign.list().getData();
        model.addAttribute("address", addressList);

        Map map = cartFeign.getCartItemList();

        model.addAttribute("carts", (List<OrderItem>)map.get("orderItemList"));
        model.addAttribute("totalMoney", (Integer)map.get("totalMoney"));
        model.addAttribute("totalNum", (Integer)map.get("totalNum"));

        //加载默认收件人信息
        for (Address address : addressList) {
            if ("1".equals(address.getIsDefault())){
                model.addAttribute("defAddr",address);
            }
        }

        return "order";
    }

    @RequestMapping("/toPayPage")
    public String toPayPage(@RequestParam("orderId") String orderId, Model model) {
        Order data = orderFeign.findById(orderId).getData();
        model.addAttribute("orderId", orderId);
        model.addAttribute("payMoney", data.getTotalMoney());
        return "pay";
    }
}
