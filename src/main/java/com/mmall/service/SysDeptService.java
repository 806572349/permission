package com.mmall.service;

import com.mmall.dao.SysDeptMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysDept;
import com.mmall.param.DeptParm;
import com.mmall.util.BeanValidator;
import com.mmall.util.LevleUtil;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/5
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    public  void save(DeptParm parm){
        BeanValidator.check(parm);
        if (checkExist(parm.getParentId(),parm.getName(),parm.getId())){
            throw new ParamException("同一层级下存在相同的名称部门");
        }
        SysDept dept= SysDept.builder()
                .name(parm.getName())
                .parentId(parm.getParentId())
                .seq(parm.getSeq())
                .remark(parm.getRemark()).build();
        dept.setLevel(LevleUtil.calculateLevel(getLevel(parm.getParentId()),parm.getParentId()));

        dept.setOperator("system"); //TODO:
        dept.setOperateIp("127.0.0.1");
        dept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(dept);
    }

    private boolean checkExist(Integer parentId,
                               String deptName,
                               Integer depId){
        //TODO:
        return true;
    }

    private String getLevel(Integer deptId){
        SysDept dept=sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept==null){
            return null;
        }
        return dept.getLevel();

    }

}
