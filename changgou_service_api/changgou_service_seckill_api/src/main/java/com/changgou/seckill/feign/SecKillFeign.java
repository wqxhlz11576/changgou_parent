package com.changgou.seckill.feign;

import com.changgou.entity.Result;
import com.changgou.seckill.pojo.SeckillGoods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by wq120 on 3/27/2020.
 */
@FeignClient(name="seckill")
public interface SecKillFeign {

    /**
     * 查询秒杀商品列表
     * @param time
     * @return
     */
    @RequestMapping("/seckill/list")
    public Result<List<SeckillGoods>> list(@RequestParam("time") String time);
}
