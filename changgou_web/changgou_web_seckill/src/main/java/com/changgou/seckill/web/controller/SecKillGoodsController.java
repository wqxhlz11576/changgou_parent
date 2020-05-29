package com.changgou.seckill.web.controller;

import com.changgou.entity.Result;
import com.changgou.seckill.feign.SecKillFeign;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.util.DateUtil;
import com.changgou.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wq120 on 3/27/2020.
 */
@Controller
@RequestMapping("/wseckillgoods")
public class SecKillGoodsController {
    @Autowired
    private SecKillFeign secKillFeign;

    @RequestMapping("/toIndex")
    public String toIndex(){
        return "seckill-index";
    }

    @RequestMapping(value = "/timeMenus")
    @ResponseBody
    public List<String> dateMenus(){
        List<Date> dateMenus = DateUtil.getDateMenus();
        List<String> result = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date dateMenu : dateMenus) {
            String format = simpleDateFormat.format(dateMenu);
            result.add(format);
        }
        return result;
    }

    @RequestMapping("/list")
    @ResponseBody
    public Result<List<SeckillGoods>> list(String time){
        Result<List<SeckillGoods>> listResult = secKillFeign.list(DateUtil.formatStr(time));
        return listResult;
    }
}
