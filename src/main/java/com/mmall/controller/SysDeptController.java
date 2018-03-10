package com.mmall.controller;

import com.mmall.common.JsonData;
import com.mmall.dto.DeptLevelDto;
import com.mmall.param.DeptParm;
import com.mmall.service.SysDeptService;
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
 * 部门
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/5
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Controller
@RequestMapping("/sys/dept")
public class SysDeptController {
    @Resource
    private SysDeptService sysDeptService;

    @Resource
    private SysTreeService sysTreeService;

    @PostMapping(value = "sava.json",produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public JsonData saveDept(DeptParm deptParm){
        sysDeptService.save(deptParm);
        return JsonData.success();
    }
    @GetMapping("tree.json")
    @ResponseBody
    public JsonData tree(){
        List<DeptLevelDto> dtoList=sysTreeService.deptTree();
        return JsonData.success(dtoList);
    }

    @PostMapping("update.json")
    @ResponseBody
    public JsonData updateDept(DeptParm deptParm){
        sysDeptService.update(deptParm);
        return JsonData.success();
    }
    @GetMapping("dept.page")
    public ModelAndView page(){
        return new ModelAndView("dept");
    }
}

