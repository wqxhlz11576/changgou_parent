package com.changgou.search.service;

import com.changgou.search.pojo.SkuInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZJ
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public Map search(Map<String, String> searchMap) {
        if (searchMap ==null || searchMap.size() == 0) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        /**
         * 获取查询条件
         */
        //获取查询的关键字
        String keywords = searchMap.get("keywords");

        /**
         * 封装查询对象
         */
        //创建顶级的查询条件对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //创建组合查询条件对象(这里可以放多种查询条件)
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        /**
         * 根据关键字查询
         */
        if (!StringUtils.isEmpty(keywords)) {
            //应该, 是or或者的意思
            //boolQueryBuilder.should();
            //非, not的意思
            //boolQueryBuilder.mustNot();
            //必须, 相当于and的意思
            //QueryBuilders.matchQuery是将查询的关键字根据指定的分词器切分词后, 将切分出来的词一个一个查询,
            //将查询到的结果组合到一起返回
            //operator(Operator.AND)是控制切分词后, 根据每个词查询出来结果, 这些结果组合在一块返回的关系
            boolQueryBuilder.must(QueryBuilders.matchQuery("name", keywords).operator(Operator.AND));
        }
        //将组合查询对象放入顶级查询对象中
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);

        /**
         * 根据品牌过滤查询
         */

        /**
         * 根据规格过滤查询
         */

        /**
         * 根据价格过滤查询
         */

        /**
         * 分页查询
         */

        /**
         * 高亮查询
         */

        /**
         * 排序查询
         */

        /**
         * 根据品牌聚合查询
         */

        /**
         * 根据规格聚合查询
         */


        /**
         * 查询并返回结果集(包含高亮)
         */

        AggregatedPage<SkuInfo> skuInfos = esTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);

        /**
         * 获取根据品牌聚合的结果集
         */

        /**
         * 获取根据规格聚合的结果集
         */

        /**
         * 封装查询结果后返回
         */
        resultMap.put("rows", skuInfos.getContent());
        resultMap.put("totalPage", skuInfos.getTotalPages());
        resultMap.put("total", skuInfos.getTotalElements());

        return resultMap;
    }
}
