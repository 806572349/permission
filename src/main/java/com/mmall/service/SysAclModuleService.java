package com.mmall.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.dto.AclModuleLevelDto;
import com.mmall.exception.ParamException;
import com.mmall.model.SysAclModule;
import com.mmall.model.SysDept;
import com.mmall.param.AclModuleParam;
import com.mmall.param.DeptParm;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.LevleUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/15
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SysAclModuleService {
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;



    public List<AclModuleLevelDto> aclModuleLevelDtoList(){
        List<SysAclModule> allAclModule = sysAclModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> dtoList= Lists.newArrayList();
        for (SysAclModule sysAclModule:allAclModule){
            dtoList.add(AclModuleLevelDto.adapt(sysAclModule));
        }
        return aclModuleListToTree(dtoList);
    }

    public List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList){
        if (CollectionUtils.isEmpty(dtoList)){
            return  Lists.newArrayList();
        }
        Multimap<String , AclModuleLevelDto> levelDtoArrayListMultimap = ArrayListMultimap.create();
        ArrayList<AclModuleLevelDto> rootList = Lists.newArrayList();

        for (AclModuleLevelDto dto:dtoList){
            levelDtoArrayListMultimap.put(dto.getLevel(),dto);
            if (LevleUtil.ROOT.equals(dto.getLevel())){
                rootList.add(dto);
            }
        }
        Collections.sort(rootList,aclModuleLevelDtoComparator);
        transformAclModeuleTree(rootList,LevleUtil.ROOT,levelDtoArrayListMultimap);
        return rootList;

    }
    public void transformAclModeuleTree(List<AclModuleLevelDto> dtoList,String level,Multimap<String , AclModuleLevelDto> map){
        for (int i=0;i<dtoList.size();i++){
            AclModuleLevelDto aclModuleLevelDto = dtoList.get(i);
            String nextlevel1 = LevleUtil.calculateLevel(level, aclModuleLevelDto.getId());
            List<AclModuleLevelDto> tempList= (List<AclModuleLevelDto>) map.get(nextlevel1);
            if (CollectionUtils.isNotEmpty(tempList)){
                Collections.sort(tempList,aclModuleLevelDtoComparator);
                aclModuleLevelDto.setAclModuleList(tempList);
                transformAclModeuleTree(tempList,nextlevel1,map);
            }


        }

    }

    //比较器
    public Comparator<AclModuleLevelDto> aclModuleLevelDtoComparator=new Comparator<AclModuleLevelDto>() {
        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq()-o2.getSeq();

        }
    };
    public void save(AclModuleParam parm) {
        BeanValidator.check(parm);
        if (checkExist(parm.getParentId(), parm.getName(), parm.getId())) {
            throw new ParamException("同一层级下存在相同的名称权限");
        }
        SysAclModule aclModule = SysAclModule.builder()
                .name(parm.getName())
                .parentId(parm.getParentId())
                .seq(parm.getSeq())
                .status(parm.getStatus()).remark(parm.getRemark())
                .build();
        aclModule.setLevel(LevleUtil.calculateLevel(getLevel(parm.getParentId()),parm.getParentId()));
        aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModule.setOperateTime(new Date());
        sysAclModuleMapper.insertSelective(aclModule);


    }

    public void update(AclModuleParam parm) {
        BeanValidator.check(parm);
        if (checkExist(parm.getParentId(), parm.getName(), parm.getId())) {
            throw new ParamException("同一层级下存在相同的名称权限");
        }
        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(parm.getId());
        Preconditions.checkNotNull(before, "待更新的权限模块不存在");
        SysAclModule after = SysAclModule.builder().id(parm.getId())
                .name(parm.getName())
                .parentId(parm.getParentId())
                .seq(parm.getSeq())
                .status(parm.getStatus()).remark(parm.getRemark())
                .build();
        after.setLevel(LevleUtil.calculateLevel(getLevel(parm.getParentId()),parm.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        updateWithChild(before,after);


    }
    @Transactional
    public void updateWithChild(SysAclModule before, SysAclModule after) {
        String newLevlPrefix=after.getLevel();
        String oldLevelPrefix=before.getLevel();
        if (!after.getLevel().equals(before.getLevel())){
            List<SysAclModule> deptList=sysAclModuleMapper.getChildDeptListByLevel(after.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)){
                for (SysAclModule dept:deptList){
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix)!=0){
//                        level=newLevlPrefix+level.substring(oldLevelPrefix.length());
                        dept.setLevel(newLevlPrefix);
                        sysAclModuleMapper.batchUpdateLevel(level,dept.getId());
                    }
                }

            }
        }



        sysAclModuleMapper.updateByPrimaryKeySelective(after);


    }

    private boolean checkExist(Integer parentId,
                               String aclModuleName,
                               Integer aclModuleId) {
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, aclModuleId) > 0;
    }

    private String getLevel(Integer aclModuleId) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (sysAclModule == null) {
            return null;
        }
        return sysAclModule.getLevel();
    }
}
