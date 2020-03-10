package com.changgou.goods.service;

import com.changgou.pojo.Brand;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by wq120 on 3/8/2020.
 */
public interface BrandService {
    List<Brand> findAll();
    Brand findById(Integer id);
    void insert(Brand brand);
    void update(Brand brand);
    void delete(Integer id);
    Page findPageBySearch(Map<String, String> searchMap, Integer page, Integer pageSize);
}
