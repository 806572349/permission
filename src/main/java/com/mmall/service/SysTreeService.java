package com.mmall.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dto.DeptLevelDto;
import com.mmall.model.SysDept;
import com.mmall.util.LevleUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        for (int i=0;i<deptLevelDtos.size();i++){
            //遍历该层的每个元素
            DeptLevelDto deptLevelDto = deptLevelDtos.get(i);
            //处理当前层级的数据
            String nextLevel=LevleUtil.calculateLevel(level,deptLevelDto.getId());
            //处理下一层
            List<DeptLevelDto> tempDeptList= (List<DeptLevelDto>) levelDtoMultimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)){
                //排序
                Collections.sort(tempDeptList,deptLevelDtoComparator);

                //设置下一层部门
                deptLevelDto.setDeptList(tempDeptList);

                //进入到下一层处理
                transFromDeptTree(tempDeptList,nextLevel,levelDtoMultimap);

            }

        }
    }

    public Comparator<DeptLevelDto> deptLevelDtoComparator=new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq()-o2.getSeq();
        }
    };
}
