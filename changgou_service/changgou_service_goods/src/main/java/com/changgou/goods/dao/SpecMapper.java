package com.changgou.goods.dao;

import com.changgou.goods.pojo.Spec;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SpecMapper extends Mapper<Spec> {

    /***
     * select specification by category name
     * @param categoryName
     * @return
     */
    @Select("select * from tb_spec where template_id in (" +
            "select template_id from tb_category where name = #{categoryName});")
    List<Map> selectByCategoryName(@Param("categoryName") String categoryName);
}
