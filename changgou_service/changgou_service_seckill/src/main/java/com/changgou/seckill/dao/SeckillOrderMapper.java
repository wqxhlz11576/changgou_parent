package com.changgou.seckill.dao;

import com.changgou.seckill.pojo.SeckillOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by wq120 on 3/27/2020.
 */
public interface SeckillOrderMapper extends Mapper<SeckillOrder> {
    @Select("select * from tb_seckill_order where user_id=#{username} and seckill_id=#{id}")
    SeckillOrder getSecKillOrderByUsernameAndGoodsId(@Param("username") String username, @Param("id")Long id);
}
