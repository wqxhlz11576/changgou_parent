package com.changgou.goods.controller;

import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wq120 on 3/9/2020.
 */
@ControllerAdvice
public class BaseController {

    @ExceptionHandler
    @ResponseBody
    public Result exceptionHandler(Exception e) {
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, "System exception");
    }
}
