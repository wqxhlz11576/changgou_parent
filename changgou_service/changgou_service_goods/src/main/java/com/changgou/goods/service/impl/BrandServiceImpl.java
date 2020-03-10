package com.changgou.goods.service.impl;

import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.service.BrandService;
import com.changgou.pojo.Brand;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * Created by wq120 on 3/8/2020.
 */
@Service
public class BrandServiceImpl implements BrandService{
    @Autowired
    private BrandMapper brandMapper;


    @Override
    public List<Brand> findAll() {
        List<Brand> brandList = brandMapper.selectAll();
        return brandList;
    }

    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insert(Brand brand) {
        brandMapper.insertSelective(brand);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Page findPageBySearch(Map<String, String> searchMap, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        Example example = new Example(Brand.class);

        Example.Criteria criteria = example.createCriteria();

        if (searchMap != null && !StringUtils.isEmpty(searchMap.get("name"))) {
            criteria.andLike("name", "%" + searchMap.get("name") + "%");
        }

        if (searchMap != null && !StringUtils.isEmpty(searchMap.get("letter"))) {
            criteria.andEqualTo("letter", searchMap.get("letter") );
        }

        Page brands = (Page) brandMapper.selectByExample(example);

        return brands;
    }
}
