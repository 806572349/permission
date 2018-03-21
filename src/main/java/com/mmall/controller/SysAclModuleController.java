package com.mmall.controller;

import com.mmall.common.JsonData;
import com.mmall.dto.AclModuleLevelDto;
import com.mmall.param.AclModuleParam;
import com.mmall.param.DeptParm;
import com.mmall.service.SysAclModuleService;
import com.mmall.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/15
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class SysAclModuleController {

    @Resource
    private SysAclModuleService sysAclModuleService;

    @Resource
    private SysTreeService sysTreeService;
    @GetMapping("acl.page")
    public ModelAndView page(){
        return new ModelAndView("acl");
    }


    @PostMapping(value = "save.json",produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public JsonData saveAclModuleParam(AclModuleParam aclModuleParam){
        sysAclModuleService.save(aclModuleParam);


        return JsonData.success();
    }

    @PostMapping("update.json")
    @ResponseBody
    public JsonData updateAclModule(AclModuleParam aclModuleParam){
        sysAclModuleService.update(aclModuleParam);
        return JsonData.success();
    }

    @GetMapping(value = "tree.json")
    @ResponseBody
    public JsonData tree(){
        List<AclModuleLevelDto> aclModuleLevelDtoList = sysTreeService.aclModuleLevelTree();
        return JsonData.success(aclModuleLevelDtoList);
    }


}
