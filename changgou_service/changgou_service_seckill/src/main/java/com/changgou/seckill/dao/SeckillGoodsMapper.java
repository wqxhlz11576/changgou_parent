package com.changgou.seckill.dao;

import com.changgou.seckill.pojo.SeckillGoods;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by wq120 on 3/27/2020.
 */
public interface SeckillGoodsMapper extends Mapper<SeckillGoods> {
    @Update("update tb_seckill_goods set stock_count=stock_count-1 where id=#{id} and stock_count>=1")
    int updateStockCount(Long seckillId);
}
