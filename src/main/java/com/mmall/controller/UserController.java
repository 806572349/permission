package com.mmall.controller;

import com.mmall.model.SysUser;
import com.mmall.service.SysUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.security.krb5.internal.PAData;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/10
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class UserController {
    @Resource
    private SysUserService sysUserService;

    @RequestMapping("logout.page")
    public  void logout(HttpServletRequest request,
                        HttpServletResponse response) throws IOException {
        request.getSession().invalidate();//移除session
        String path="signin.jsp";
        response.sendRedirect(path);

    }
    @RequestMapping("/login.page")
    public void login(HttpServletRequest request,
                      HttpServletResponse response
    ) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        SysUser sysUser = sysUserService.findByKeyWord(username);
        String errorMsg = "";
        String ret = request.getParameter("ret");

        if (StringUtils.isBlank(username)) {
            errorMsg = "用户名空";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "密码为空";
        } else if (sysUser == null) {
            errorMsg = "用户错误";
        } else if (sysUser.getPassword().equals(MD5Util.encrypt(password))) {
            errorMsg = "用户冻结";
        } else {
            request.getSession().setAttribute("user", sysUser);
            if (StringUtils.isNoneBlank(ret)) {
                    response.sendRedirect(ret);
            }else {
                response.sendRedirect("/admin/index.page"); // TODO: 2018/3/10
            }
        }
        request.setAttribute("error",errorMsg);
        request.setAttribute("username",username);
        if (StringUtils.isNoneBlank(ret)){
            request.setAttribute("ret",ret);
        }
        String path="signin.jsp";
        request.getRequestDispatcher(path).forward(request,response);


    }
}
