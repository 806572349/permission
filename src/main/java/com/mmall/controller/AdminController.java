package com.mmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/10
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @RequestMapping("index.page")
    public ModelAndView index(){
        return new ModelAndView("admin");
    }

}
