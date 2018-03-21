package com.mmall.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.mmall.beans.Mail;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysUser;
import com.mmall.param.UserParm;
import com.mmall.util.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/10
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    // region   保存用户


    public void save(UserParm userParm){
        BeanValidator.check(userParm);
        if (checkEmailExist(userParm.getMail(),userParm.getId())){
            throw new ParamException("邮箱被占用");
        }
        if (checkPhone(userParm.getTelephone(),userParm.getId())){
            throw  new ParamException("手机号被占用");
        }
        // TODO: 2018/3/10
        String password= PasswrodUtil.randomPasswrod();
        String encPassword= MD5Util.encrypt(password);
        SysUser sysUser=SysUser.builder().username(userParm.getUsername())
                .telephone(userParm.getTelephone())
                .mail(userParm.getMail())
                .password(encPassword)
                .deptId(userParm.getDeptId())
                .status(userParm.getStatus())
                .remark(userParm.getRemark()).build();
        sysUser.setOperator(RequestHolder.getCurrentUser().getUsername()); //TODO:
        sysUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysUser.setOperateTime(new Date());
        // todo: send Email
        Set<String> set  = Sets.<String>newConcurrentHashSet();
        set.add(userParm.getMail());
        Mail mail = Mail.builder().receivers(set).message("您的用户名是:" + userParm.getMail() + "初始密码是:" + password + "请尽快登录后台修改你的初始密码").build();
        MailUtil.sendAsyc(mail);
        sysUserMapper.insertSelective(sysUser);

    }

    public  boolean checkEmailExist(String mail,Integer userId){
        return sysUserMapper.countByMail(mail, userId)>0;
    }
    public  boolean checkPhone(String phone,Integer userId){
        return sysUserMapper.countByPhone(phone, userId)>0;

    }
//endregion


    // region   更新用户

    public void update(UserParm userParm){
        BeanValidator.check(userParm);
        if (checkEmailExist(userParm.getMail(),userParm.getId())){
            throw new ParamException("邮箱被占用");
        }
        if (checkPhone(userParm.getTelephone(),userParm.getId())){
            throw  new ParamException("手机号被占用");
        }
        SysUser before = sysUserMapper.selectByPrimaryKey(userParm.getId());
        Preconditions.checkNotNull(before,"更新用户不存在");
        SysUser after=SysUser.builder().id(userParm.getId()).username(userParm.getUsername())
                .telephone(userParm.getTelephone())
                .mail(userParm.getMail())
                .deptId(userParm.getDeptId())
                .status(userParm.getStatus())
                .operateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()))
                .operator(RequestHolder.getCurrentUser().getUsername())
                .operateTime(new Date())
                .remark(userParm.getRemark()).build();


        sysUserMapper.updateByPrimaryKeySelective(after);

        //endregion
    }
    //endregion


    public  SysUser findByKeyWord(String keyword){
        SysUser byKeyWord = sysUserMapper.findByKeyWord(keyword);
        return byKeyWord;
    }


    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery page){
        BeanValidator.check(page);
        int count=sysUserMapper.countByDeptId(deptId);
        if (count>0){
            List<SysUser> list=sysUserMapper.getPageByDeptid(deptId,page);
            return PageResult.<SysUser>builder().total(count).data(list).build();
        }
        return PageResult.<SysUser>builder().build();
    }


}
