package com.changgou.search.controller;

import com.changgou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author ZJ
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 高级搜索
     * @param searchMap
     * @return
     */
    @GetMapping
    public Map search(@RequestParam Map<String, String> searchMap) {
        Map resultMap = searchService.search(searchMap);
        return resultMap;
    }
}
