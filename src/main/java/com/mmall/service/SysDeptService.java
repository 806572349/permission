package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysDeptMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysDept;
import com.mmall.param.DeptParm;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.LevleUtil;
import com.sun.xml.internal.bind.v2.TODO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

        dept.setOperator(RequestHolder.getCurrentUser().getUsername()); //TODO:
        dept.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        dept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(dept);
    }


    public  void update(DeptParm parm){
        BeanValidator.check(parm);
        if (checkExist(parm.getParentId(),parm.getName(),parm.getId())){
            throw new ParamException("同一层级下存在相同的名称部门");
        }
        SysDept before=sysDeptMapper.selectByPrimaryKey(parm.getId());
        Preconditions.checkNotNull(before,"待更新的部门不存在");
        SysDept after= SysDept.builder()
                .name(parm.getName())
                .id(parm.getId())
                .parentId(parm.getParentId())
                .seq(parm.getSeq())
                .remark(parm.getRemark()).build();
        after.setLevel(LevleUtil.calculateLevel(getLevel(parm.getParentId()),parm.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername()); //TODO:
        after.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());


        updateWithChild(before,after);

    }
    @Transactional
    public void updateWithChild(SysDept before, SysDept after){
        String newLevlPrefix=after.getLevel();
        String oldLevelPrefix=before.getLevel();
        if (!after.getLevel().equals(before.getLevel())){
            List<SysDept>deptList=sysDeptMapper.getChildDeptListByLevel(after.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)){
                for (SysDept dept:deptList){
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix)!=0){
//                        level=newLevlPrefix+level.substring(oldLevelPrefix.length());
                        dept.setLevel(newLevlPrefix);
                        sysDeptMapper.batchUpdateLevel(level,dept.getId());
                    }
                }

            }
        }
        sysDeptMapper.updateByPrimaryKey(after);
    }

    private boolean checkExist(Integer parentId,
                               String deptName,
                               Integer depId){
        return sysDeptMapper.countByNameAndParentId(parentId,deptName,depId)>0;
    }

    private String getLevel(Integer deptId){
        SysDept dept=sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept==null){
            return null;
        }
        return dept.getLevel();

    }

}
