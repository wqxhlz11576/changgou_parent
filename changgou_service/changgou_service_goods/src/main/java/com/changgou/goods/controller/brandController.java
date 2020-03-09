package com.changgou.goods.controller;

import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.goods.service.BrandService;
import com.changgou.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wq120 on 3/8/2020.
 */
@RestController
@RequestMapping("/brand")
public class brandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("findAll")
    public Result<Brand> findAll() {
        List<Brand> brands = brandService.findAll();

        return new Result<>(true, StatusCode.OK, "Get brand list success", brands);
    }

}
