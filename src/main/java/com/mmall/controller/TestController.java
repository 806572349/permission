package com.mmall.controller;

import com.mmall.common.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/2/26
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Controller
@RequestMapping("test")
public class TestController {

    @GetMapping("hello.json")
    @ResponseBody
    public JsonData hello(){
        log.info("hello");
        return JsonData.success("hello permission");
    }
}
