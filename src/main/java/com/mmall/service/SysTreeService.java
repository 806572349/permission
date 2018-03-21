package com.mmall.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dto.AclModuleLevelDto;
import com.mmall.dto.DeptLevelDto;
import com.mmall.model.SysAclModule;
import com.mmall.model.SysDept;
import com.mmall.util.LevleUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nemo
 * Date: 2018/3/5
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SysTreeService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    public List<AclModuleLevelDto> aclModuleLevelTree() {
        List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> aclModuleLevelDtoList = Lists.newArrayList();
        for (SysAclModule sysAclModule:aclModuleList){
            aclModuleLevelDtoList.add(AclModuleLevelDto.adapt(sysAclModule));
        }
        return aclModuleLevelDtoListToTree(aclModuleLevelDtoList);
    }

    public  List<AclModuleLevelDto> aclModuleLevelDtoListToTree(List<AclModuleLevelDto> dtoList){
        if(CollectionUtils.isEmpty(dtoList)){
            return Lists.newArrayList();
        }
        Multimap<String, AclModuleLevelDto> levelAclMap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();
        for (AclModuleLevelDto dto : dtoList) {
            levelAclMap.put(dto.getLevel(), dto);
            if (LevleUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        Collections.sort(rootList, new Comparator<AclModuleLevelDto>() {
            @Override
            public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });
        transFromAclMoudleTree(rootList,LevleUtil.ROOT,levelAclMap);
        return  rootList;

    }

    public void transFromAclMoudleTree(List<AclModuleLevelDto> dtoList,
                                       String level,
                                       Multimap<String, AclModuleLevelDto> levelAclMap){
        for (int i=0;i<dtoList.size();i++){
            AclModuleLevelDto dto=dtoList.get(i);
            String nextLevel=LevleUtil.calculateLevel(level,dto.getId());
            List<AclModuleLevelDto>tempList= (List<AclModuleLevelDto>) levelAclMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempList)){
                Collections.sort(tempList,(o1,o2)->  o1.getSeq()-o2.getSeq());
                dto.setAclModuleList(tempList);
                transFromAclMoudleTree(tempList,nextLevel,levelAclMap);
            }


        }

    }

    public List<DeptLevelDto> deptTree() {
        List<SysDept> deptList = sysDeptMapper.getAllDept();

        List<DeptLevelDto> dtoList = Lists.newArrayList();
        for (SysDept dept : deptList) {
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }
        return deptListToTree(dtoList);

    }

    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelDtoList) {
        if (CollectionUtils.isEmpty(deptLevelDtoList)) {
            return Lists.newArrayList();
        }
        //level ->[dept1,dept2 ....]
        Multimap<String, DeptLevelDto> levelDepeMap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();
        for (DeptLevelDto dto : deptLevelDtoList) {
            levelDepeMap.put(dto.getLevel(), dto);
            if (LevleUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        //按照seq 从小到大进行排序
        Collections.sort(rootList, new Comparator<DeptLevelDto>() {
            @Override
            public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });

        //递归生成树
        transFromDeptTree(rootList, LevleUtil.ROOT, levelDepeMap);
        return rootList;


    }

    public void transFromDeptTree(List<DeptLevelDto> deptLevelDtos,
                                  String level,
                                  Multimap<String, DeptLevelDto> levelDtoMultimap
    ) {
        for (int i = 0; i < deptLevelDtos.size(); i++) {
            //遍历该层的每个元素
            DeptLevelDto deptLevelDto = deptLevelDtos.get(i);
            //处理当前层级的数据
            String nextLevel = LevleUtil.calculateLevel(level, deptLevelDto.getId());
            //处理下一层
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDtoMultimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                //排序
                Collections.sort(tempDeptList, deptLevelDtoComparator);

                //设置下一层部门
                deptLevelDto.setDeptList(tempDeptList);

                //进入到下一层处理
                transFromDeptTree(tempDeptList, nextLevel, levelDtoMultimap);

            }

        }
    }

    public Comparator<DeptLevelDto> deptLevelDtoComparator = new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
