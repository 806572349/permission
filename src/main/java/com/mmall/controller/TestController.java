package com.mmall.controller;

import com.mmall.common.ApplicationContextHelper;
import com.mmall.common.JsonData;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.model.SysAclModule;
import com.mmall.param.TestVo;
import com.mmall.util.BeanValidator;
import com.mmall.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

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

    @GetMapping("validate.json")
    @ResponseBody
    public JsonData validate(TestVo vo){
        log.info("validate");
        SysAclModuleMapper sysAclModuleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(1);
        log.info(JsonMapper.obj2String(sysAclModule));
//        BeanValidator.check(vo);
        return JsonData.success("hello validate");
    }
}
