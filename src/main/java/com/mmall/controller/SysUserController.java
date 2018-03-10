package com.mmall.controller;

import com.mmall.common.JsonData;
import com.mmall.dao.SysUserMapper;
import com.mmall.param.DeptParm;
import com.mmall.param.UserParm;
import com.mmall.service.SysUserService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/10
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    @PostMapping(value = "save.json")
    @ResponseBody
    public JsonData saveUser(UserParm userParm){
        sysUserService.save(userParm);
        return JsonData.success();
    }
    @PostMapping("update.json")
    @ResponseBody
    public JsonData updateUser(UserParm userParm){
        sysUserService.update(userParm);
        return JsonData.success();
    }

    @GetMapping("user.json")
    @ResponseBody
    public JsonData userList(){

        return JsonData.success("test");
    }

}
