package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by wq120 on 3/14/2020.
 */
@FeignClient("goods")
@RequestMapping("/sku")
public interface SkuFeign {
    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search" )
    public Result findList(@RequestParam Map searchMap);

    @GetMapping("/search/spuId/{spuId}")
    public List<Sku> findSkuListBySpuId(@RequestParam("spuId") String spuId);

    @GetMapping("/{id}")
    public Result<Sku> findById(@PathVariable("id") String id);

    @PostMapping("/decr/count")
    public Result decrCount(@RequestParam("username") String username);
}
