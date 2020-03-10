package com.changgou.goods.controller;

import com.changgou.common.entity.PageResult;
import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.goods.service.BrandService;
import com.changgou.pojo.Brand;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by wq120 on 3/8/2020.
 */
@RestController
@RequestMapping("/brand")
public class brandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("/all")
    public Result<Brand> findAll() {
        List<Brand> brands = brandService.findAll();

        return new Result<>(true, StatusCode.OK, "Get brand list success", brands);
    }

    @GetMapping("/{id}")
    public  Result<Brand> findById(@PathVariable("id") Integer id) {
        Brand brand = brandService.findById(id);
        return new Result<>(true, StatusCode.OK, "Get brand by id success", brand);
    }

    @PostMapping
    public Result insert(@RequestBody Brand brand) {
        brandService.insert(brand);
        return new Result(true, StatusCode.OK, "Insert success");
    }

    @PutMapping
    public Result update(@RequestBody Brand brand) {
        brandService.update(brand);
        return new Result(true, StatusCode.OK, "Update success");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        brandService.delete(id);
        return new Result(true, StatusCode.OK, "Delete success");
    }

    @GetMapping("/search/{page}/{pageSize}")
    public PageResult<Brand> findPageBySearch(@RequestBody Map<String, String> searchMap,
                                 @PathVariable("page") Integer page,
                                 @PathVariable("pageSize") Integer pageSize) {
        Page brandList = brandService.findPageBySearch(searchMap, page, pageSize);
        PageResult<Brand> result = new PageResult<>(brandList.getTotal(), brandList.getResult());

        return result;
    }

}
