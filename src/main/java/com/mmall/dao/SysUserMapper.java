package com.mmall.dao;

import com.mmall.beans.PageQuery;
import com.mmall.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findByKeyWord(@Param("keyword") String keyword);
    int countByMail(@Param("mail") String mail,@Param("id")Integer id);
    int countByPhone(@Param("telephone")String phone,@Param("id")Integer id);

    int countByDeptId(@Param("deptId") int deptId);

    List<SysUser>getPageByDeptid(@Param("deptId") int deptId, @Param("page") PageQuery page);
}