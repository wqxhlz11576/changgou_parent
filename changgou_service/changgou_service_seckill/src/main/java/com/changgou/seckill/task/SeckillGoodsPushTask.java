package com.changgou.seckill.task;

import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by wq120 on 3/27/2020.
 */
@Component
public class SeckillGoodsPushTask {
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String SECKILL_GOODS_KEY="seckill_goods_";
    private static final String SECKILL_GOODS_STOCK_COUNT_KEY="seckill_goods_stock_count_";

    @Scheduled(cron = "0/30 * * * * ?")
    public void loadSecKillGoodsToRedis(){

        List<Date> dateMenus = DateUtil.getDateMenus();
        for (Date dateMenu : dateMenus) {
            //每次用最好都重新new
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String redisExtName = DateUtil.date2Str(dateMenu);

            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status","1");
            criteria.andGreaterThan("stockCount",0);
            criteria.andGreaterThanOrEqualTo("startTime",simpleDateFormat.format(dateMenu));
            criteria.andLessThan("endTime",simpleDateFormat1.format(DateUtil.addDateHour(dateMenu,2)));
            Set keys = redisTemplate.boundHashOps(SECKILL_GOODS_KEY + redisExtName).keys();
            if (keys!=null && keys.size()>0){
                criteria.andNotIn("id",keys);
            }
            List<SeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);

            //添加到缓存中
            for (SeckillGoods seckillGoods : seckillGoodsList) {
                redisTemplate.boundHashOps(SECKILL_GOODS_KEY + redisExtName).put(seckillGoods.getId(),seckillGoods);

                Integer stockCount = seckillGoods.getStockCount();
                redisTemplate.boundValueOps(SECKILL_GOODS_STOCK_COUNT_KEY + seckillGoods.getId()).set(stockCount);
            }
        }
    }
}
